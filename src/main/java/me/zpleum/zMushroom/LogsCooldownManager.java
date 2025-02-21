package me.zpleum.zMushroom;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class LogsCooldownManager {
    private final Map<Location, Long> cooldowns = new ConcurrentHashMap();
    private final File file;
    private final FileConfiguration config;

    public LogsCooldownManager(File dataFolder) {
        this.file = new File(dataFolder, "logs_cooldown.yml");
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().severe("Can not create logs_cooldown.yml file!");
                e.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(this.file);
        this.loadCooldowns();
    }

    public void loadCooldowns() {
        this.cooldowns.clear();
        if (this.config.contains("cooldowns")) {
            for(String key : this.config.getConfigurationSection("cooldowns").getKeys(false)) {
                String[] parts = key.split(",");
                if (parts.length == 4) {
                    String worldName = parts[0];
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    int z = Integer.parseInt(parts[3]);
                    World world = Bukkit.getWorld(worldName);
                    if (world != null) {
                        Location loc = new Location(world, (double)x, (double)y, (double)z);
                        long cooldownEnd = this.config.getLong("cooldowns." + key);
                        if (cooldownEnd > System.currentTimeMillis()) {
                            this.cooldowns.put(loc, cooldownEnd);
                        }
                    }
                }
            }
        }

    }

    public void saveCooldown(Location location, long durationMillis) {
        String var10000 = location.getWorld().getName();
        String key = var10000 + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
        this.cooldowns.put(location, durationMillis);
        this.config.set("cooldowns." + key, durationMillis);

        try {
            this.config.save(this.file);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Can not save logs_cooldown.yml for " + key);
            e.printStackTrace();
        }

    }

    public void removeCooldown(Location location) {
        this.cooldowns.remove(location);
        String var10000 = location.getWorld().getName();
        String key = var10000 + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
        this.config.set("cooldowns." + key, (Object)null);

        try {
            this.config.save(this.file);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Can not remove cooldown from logs_cooldown.yml for " + key);
            e.printStackTrace();
        }

    }

    public boolean isInCooldown(Location location) {
        return this.cooldowns.containsKey(location) && (Long)this.cooldowns.get(location) > System.currentTimeMillis();
    }

    public long getCooldownTimeLeft(Location location) {
        return this.isInCooldown(location) ? (Long)this.cooldowns.get(location) - System.currentTimeMillis() : 0L;
    }

    public Map<Location, Long> getCooldowns() {
        return this.cooldowns;
    }
}
