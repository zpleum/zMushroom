package me.zpleum.zMushroom;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dev.lone.itemsadder.api.CustomFurniture;
import dev.lone.itemsadder.api.Events.FurniturePlaceEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import dev.lone.itemsadder.api.ItemsAdder;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.Arrays;

public final class ZMushroom extends JavaPlugin implements Listener {
    private File hologramDataFile;
    private final Map<Player, BukkitRunnable> harvestingTasks = new ConcurrentHashMap();
    private final Map<Player, Location> harvestingPlayers = new ConcurrentHashMap();
    private FileConfiguration hologramData;
    private LogsCooldownManager cooldownManager;
    private final Map<Location, TextDisplay> holograms = new ConcurrentHashMap();
    private final Map<Location, Player> activeUsers = new ConcurrentHashMap();
    private final Map<Player, Long> holdStartTimes = new ConcurrentHashMap();
    private final Map<Location, Boolean> blockInUse = new ConcurrentHashMap();
    private Player currentPlayer = null;
    private boolean isInProgress = false;

    public ZMushroom() {
    }

    public void onEnable() {
        this.saveDefaultConfig();
        this.cooldownManager = new LogsCooldownManager(this.getDataFolder());
        this.hologramDataFile = new File(this.getDataFolder(), "holograms.yml");
        this.hologramData = YamlConfiguration.loadConfiguration(this.hologramDataFile);
        Bukkit.getPluginManager().registerEvents(this, this);
        this.loadHolograms();
        this.startCooldownTask();
        this.getCommand("zmushroom").setExecutor((sender, command, label, args) -> {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                this.reloadConfig();
                this.cooldownManager.loadCooldowns();
                sender.sendMessage(this.getConfig().getString("messages.reload-success", "Config and cooldowns reloaded!"));
                return true;
            } else {
                sender.sendMessage(this.getConfig().getString("messages.reload-usage", "§cAn internal error occurred while attempting to perform this command."));
                return true;
            }
        });
        getLogger().info("");
        getLogger().info("");
        getLogger().info("");
        getLogger().info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        getLogger().info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        getLogger().info("");
        getLogger().info("");
        getLogger().info("!!!!!!!!!!!! zMushroom 1.3 has been enabled! !!!!!!!!!!!!");
        getLogger().info("!!!!!!!!!!!!!!!!!!!!! HAVE A GOOD DAY !!!!!!!!!!!!!!!!!!!!!");
        getLogger().info("");
        getLogger().info("");
        getLogger().info("!!!!!!!!!!!!! Plugin Developing By zPleum ! !!!!!!!!!!!!!");
        getLogger().info("!!!!!!!!!!!!! This Version Is Latest (1.3) !!!!!!!!!!!!!!");
        getLogger().info("");
        getLogger().info("");
        getLogger().info("!!!!!!!!!!!!! Contact HTTPS://WIRAPHAT.ONRENDER.COM !!!!!!!!!!!!!");
        getLogger().info("");
        getLogger().info("");
        getLogger().info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        getLogger().info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        getLogger().info("");
        getLogger().info("");
        getLogger().info("");
    }

    public void onDisable() {
        this.saveHolograms();
        this.holograms.values().forEach((display) -> {
            if (!display.isDead()) {
                display.remove();
            }
        });
        this.holograms.clear();
        getLogger().info("");
        getLogger().info("");
        getLogger().info("");
        getLogger().info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        getLogger().info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        getLogger().info("");
        getLogger().info("");
        getLogger().info("!!!!!!!!!!!! zMushroom 1.3 has been disabled! !!!!!!!!!!!!");
        getLogger().info("!!!!!!!!!!!!!!!!!!!!!! SEE YOU SOON !!!!!!!!!!!!!!!!!!!!!!");
        getLogger().info("");
        getLogger().info("");
        getLogger().info("!!!!!!!!!!!!! Plugin Developing By zPleum ! !!!!!!!!!!!!!");
        getLogger().info("!!!!!!!!!!!!! This Version Is Latest (1.3) !!!!!!!!!!!!!!");
        getLogger().info("");
        getLogger().info("");
        getLogger().info("!!!!!!!!!!!!! Contact HTTPS://WIRAPHAT.ONRENDER.COM !!!!!!!!!!!!!");
        getLogger().info("");
        getLogger().info("");
        getLogger().info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        getLogger().info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        getLogger().info("");
        getLogger().info("");
        getLogger().info("");
    }

    private void startCooldownTask() {
        (new BukkitRunnable() {
            public void run() {
                long currentTime = System.currentTimeMillis();
                ZMushroom.this.cooldownManager.getCooldowns().forEach((location, endTime) -> {
                    if (endTime <= currentTime) {
                        ZMushroom.this.cooldownManager.removeCooldown(location);
                        ZMushroom.this.updateHologram(location, false);
                    }

                });
            }
        }).runTaskTimer(this, 20L, 20L);
    }

    private void resetState(Location blockLocation) {
        this.isInProgress = false;
        if (this.currentPlayer != null) {
            this.harvestingPlayers.remove(this.currentPlayer);
        }

        this.currentPlayer = null;
        if (blockLocation != null) {
            this.activeUsers.remove(blockLocation);
            this.blockInUse.put(blockLocation, false);
        }

    }

    private void sendTitleSafe(Player player, String title, String subtitle) {
        if (player != null && player.isOnline()) {
            player.sendTitle(title, subtitle, 10, 70, 20);
        }

    }

    private void startHarvestingTask(final Player player, final Entity entity, final Location blockLocation, final int cooldownMinutes, final String command) {
        if (this.harvestingTasks.containsKey(player)) {
            return;
        }

        final int countdownStart = ZMushroom.this.getConfig().getInt("countdown-time", 5);

        BukkitRunnable task = new BukkitRunnable() {
            private int countdown = countdownStart;

            @Override
            public void run() {
                if (!player.isOnline() || !ZMushroom.this.harvestingPlayers.containsKey(player)) {
                    ZMushroom.this.cancelHarvest(player, blockLocation);
                    this.cancel();
                    return;
                }

                if (countdown > 0) {
                    String countdownText = ZMushroom.this.getConfig().getString("harvest-messages.countdown." + countdown, "&a" + countdown);
                    countdownText = countdownText.replace("&", "§");

                    ZMushroom.this.sendTitleSafe(player, countdownText,
                            ZMushroom.this.getConfig().getString("harvest-messages.countdown-subtitle", "§aเตรียมรับรางวัล..."));

                    String commandKey = "custom-sound.console-command." + countdown;
                    String consoleCommand = ZMushroom.this.getConfig().getString(commandKey, "");

                    if (consoleCommand != null && !consoleCommand.isEmpty()) {
                        String formattedCommand = consoleCommand.replace("%player%", player.getName());
                        Bukkit.getScheduler().runTask(ZMushroom.this, () -> {
                            try {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formattedCommand);
                            } catch (Exception e) {
                                ZMushroom.this.getLogger().warning("Error executing console command: " + e.getMessage());
                            }
                        });
                    }
                    countdown--;
                } else {
                    // เช็คว่าเป็น ItemsAdder Furniture
                    CustomFurniture furniture = CustomFurniture.byAlreadySpawned(entity);
                    if (furniture == null) {
                        ZMushroom.this.getLogger().warning("No valid furniture found at " + blockLocation);
                        this.cancel();
                        return;
                    }

                    String furnitureId = furniture.getNamespacedID(); // ดึง Namespaced ID ของ ItemsAdder
                    double successRate = ZMushroom.this.getConfig().getDouble("materials." + furnitureId + ".success-rate", 100.0);
                    String command = ZMushroom.this.getConfig().getString("materials." + furnitureId + ".command", "");

                    // เช็คว่ายังมีคำสั่งให้รัน
                    if (command.isEmpty()) {
                        ZMushroom.this.getLogger().warning("No command configured for " + furnitureId);
                        this.cancel();
                        return;
                    }

                    // สุ่มโอกาสสำเร็จ
                    double random = Math.random() * 100;
                    if (random <= successRate) {
                        // สำเร็จ
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                        ZMushroom.this.sendTitleSafe(player,
                                ZMushroom.this.getConfig().getString("harvest-messages.success-title", "§aSuccess!"),
                                ZMushroom.this.getConfig().getString("harvest-messages.success-subtitle", "§eHarvest complete!"));
                    } else {
                        // ล้มเหลว
                        ZMushroom.this.sendTitleSafe(player,
                                ZMushroom.this.getConfig().getString("harvest-messages.fail-title", "§cFailed!"),
                                ZMushroom.this.getConfig().getString("harvest-messages.fail-subtitle", "§eHarvest failed!"));
                    }

                    // เล่นเสียงตอนจบ
                    String commandKey = "custom-sound.console-command.0";
                    String consoleCommand = ZMushroom.this.getConfig().getString(commandKey, "");
                    if (consoleCommand != null && !consoleCommand.isEmpty()) {
                        String formattedCommand = consoleCommand.replace("%player%", player.getName());
                        Bukkit.getScheduler().runTask(ZMushroom.this, () -> {
                            try {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formattedCommand);
                            } catch (Exception e) {
                                ZMushroom.this.getLogger().warning("Error executing console command: " + e.getMessage());
                            }
                        });
                    }

                    // ตั้ง cooldown
                    long cooldownMillis = Math.max(0L, (long) (cooldownMinutes * 60) * 1000L);
                    ZMushroom.this.cooldownManager.saveCooldown(blockLocation, System.currentTimeMillis() + cooldownMillis);
                    ZMushroom.this.updateHologram(blockLocation, true);
                    ZMushroom.this.cancelHarvest(player, blockLocation);

                    harvestingTasks.remove(player);
                    this.cancel();
                }
            }
        };

        task.runTaskTimer(this, 0L, 20L);
        this.harvestingTasks.put(player, task);
    }

    private void cancelHarvest(Player player, Location blockLocation) {
        if (player != null) {
            this.harvestingPlayers.remove(player);
            if (this.harvestingTasks.containsKey(player)) {
                ((BukkitRunnable)this.harvestingTasks.get(player)).cancel();
                this.harvestingTasks.remove(player);
            }
        }

        if (blockLocation != null) {
            this.activeUsers.remove(blockLocation);
            this.blockInUse.put(blockLocation, false);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked(); // ดึง Entity ที่ถูกคลิก

        // ตรวจสอบว่าเป็น CustomFurniture ของ ItemsAdder หรือไม่
        CustomFurniture furniture;
        try {
            furniture = CustomFurniture.byAlreadySpawned(entity);
            if (furniture == null) {
                return; // ถ้าไม่ใช่เฟอร์นิเจอร์จาก ItemsAdder ให้หยุดทำงาน
            }
        } catch (RuntimeException e) {
            // ถ้าเกิด error เมื่อพยายามเรียก CustomFurniture.byAlreadySpawned
            // เช่น entity เป็น PIG ซึ่งไม่สามารถเป็น furniture ได้
            return;
        }

        String furnitureId = furniture.getNamespacedID(); // ดึง Namespaced ID ของ ItemsAdder
        Location blockLocation = entity.getLocation(); // ใช้ตำแหน่งของ Entity เป็น blockLocation

        ConfigurationSection materials = this.getConfig().getConfigurationSection("materials");
        if (materials == null || !materials.contains(furnitureId)) {
            return;
        }

        if (!player.isSneaking()) {
            Location location = entity.getLocation().clone().add(-0.5, 0, -0.5);

            String enabledWorld = this.getConfig().getString("enable-world", "");
            if (location.getWorld().getName().equalsIgnoreCase(enabledWorld)) {
                if (!this.holograms.containsKey(location)) {
                    this.createHologram(location, false);
                }
            }
        }

        this.handleOtherPlayerInteracting(player, blockLocation);
        if ((Boolean) this.blockInUse.getOrDefault(blockLocation, false)) {
            return;
        }

        if (this.harvestingPlayers.containsKey(player)) {
            player.sendTitle(
                    this.getConfig().getString("harvest-messages.already-harvesting-title", "§cCannot harvest!"),
                    this.getConfig().getString("harvest-messages.already-harvesting-subtitle", "§eYou are already harvesting another herb!"),
                    10, 70, 20
            );

            // เล่นเสียง Villager Celebrate
            String consoleCommand = this.getConfig().getString("failed-command.2");

            if (consoleCommand != null && !consoleCommand.isEmpty()) {
                final String formattedCommand = consoleCommand.replace("%player%", player.getName());
                getServer().getScheduler().runTask(this, () -> {
                    try {
                        getServer().dispatchCommand(getServer().getConsoleSender(), formattedCommand);
                    } catch (Exception e) {
                        getLogger().warning("Error executing console command: " + e.getMessage());
                    }
                });
            }
            return;
        }

        if (this.cooldownManager.isInCooldown(blockLocation)) {
            long timeLeftInMillis = this.cooldownManager.getCooldownTimeLeft(blockLocation);
            if (timeLeftInMillis > 0) {
                String cooldownTitle = this.getConfig().getString("harvest-messages.cooldown-title", "§cCooldown");
                String cooldownSubtitle = this.getConfig().getString("harvest-messages.cooldown-subtitle", "§eเหลือเวลา: %time%")
                        .replace("%time%", this.formatCooldownTime(timeLeftInMillis));
                player.sendTitle(cooldownTitle, cooldownSubtitle, 10, 70, 20);

                // เล่นเสียง Villager No
                String consoleCommand = this.getConfig().getString("failed-command.1");

                if (consoleCommand != null && !consoleCommand.isEmpty()) {
                    final String formattedCommand = consoleCommand.replace("%player%", player.getName());
                    getServer().getScheduler().runTask(this, () -> {
                        try {
                            getServer().dispatchCommand(getServer().getConsoleSender(), formattedCommand);
                        } catch (Exception e) {
                            getLogger().warning("Error executing console command: " + e.getMessage());
                        }
                    });
                }
                return;
            }
        }

        int cooldownMinutes = materials.getInt(furnitureId + ".cooldown-time", 15);
        String command = materials.getString(furnitureId + ".command", "");

        this.blockInUse.put(blockLocation, true);
        this.activeUsers.put(blockLocation, player);
        this.harvestingPlayers.put(player, blockLocation);

        // เรียกใช้งาน startHarvestingTask โดยส่ง entity และ blockLocation ที่ถูกต้อง
        this.startHarvestingTask(player, entity, blockLocation, cooldownMinutes, command);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
            Player player = event.getPlayer();
            Location blockLocation = event.getClickedBlock().getLocation();
            Material material = event.getClickedBlock().getType();
            World world = player.getWorld();
            String enabledWorld = this.getConfig().getString("enable-world", "");
            if (!world.getName().equalsIgnoreCase(enabledWorld)) {
                return;
            }

            ConfigurationSection materials = this.getConfig().getConfigurationSection("materials");
            if (materials == null || !materials.contains(material.toString())) {
                return;
            }

            if (!this.holograms.containsKey(blockLocation)) {
                return;
            }

            this.handleOtherPlayerInteracting(player, blockLocation);
            if ((Boolean)this.blockInUse.getOrDefault(blockLocation, false)) {
                return;
            }

            if (this.harvestingPlayers.containsKey(player)) {
                player.sendTitle(this.getConfig().getString("harvest-messages.already-harvesting-title", "§cCannot harvest!"), this.getConfig().getString("harvest-messages.already-harvesting-subtitle", "§eYou are already harvesting another herb!"), 10, 70, 20);
                return;
            }

            long lastHoldTime = (Long)this.holdStartTimes.getOrDefault(player, 0L);
            if (System.currentTimeMillis() - lastHoldTime < 1000L) {
                return;
            }

            this.holdStartTimes.put(player, System.currentTimeMillis());
            if (this.cooldownManager.isInCooldown(blockLocation)) {
                long timeLeftInMillis = this.cooldownManager.getCooldownTimeLeft(blockLocation);
                if (timeLeftInMillis <= 0L) {
                    this.cooldownManager.removeCooldown(blockLocation);
                    return;
                }

                String cooldownTitle = this.getConfig().getString("harvest-messages.cooldown-title", "§cCooldown");
                String cooldownSubtitle = this.getConfig().getString("harvest-messages.cooldown-subtitle", "§eเหลือเวลา: %time%").replace("%time%", this.formatCooldownTime(timeLeftInMillis));
                player.sendTitle(cooldownTitle, cooldownSubtitle, 10, 70, 20);
                this.pushPlayer(player, blockLocation);
                return;
            }

            int cooldownMinutes = materials.getInt(String.valueOf(material) + ".cooldown-time", 15);
            String command = materials.getString(String.valueOf(material) + ".command", "");
            this.holdStartTimes.put(player, System.currentTimeMillis());
            this.blockInUse.put(blockLocation, true);
            this.activeUsers.put(blockLocation, player);
            this.harvestingPlayers.put(player, blockLocation);
            this.startHarvestingTask(player, null, blockLocation, cooldownMinutes, command);
        }
    }

    private void handleOtherPlayerInteracting(Player player, Location blockLocation) {
        if ((Boolean)this.blockInUse.getOrDefault(blockLocation, false)) {
            Player activePlayer = (Player)this.activeUsers.get(blockLocation);
            if (activePlayer != null && !activePlayer.equals(player)) {
                player.sendTitle(this.getConfig().getString("harvest-messages.active-player-title", "§cCannot harvest!"), this.getConfig().getString("harvest-messages.active-player-subtitle", "§eThis block is currently being used by %player%").replace("%player%", activePlayer.getName()), 10, 70, 20);
            }
        }

    }

    private String formatCooldownTime(long timeInMillis) {
        if (timeInMillis <= 0L) {
            return "0 วินาที";
        } else {
            long totalSeconds = timeInMillis / 1000L;
            long hours = totalSeconds / 3600L;
            totalSeconds %= 3600L;
            long minutes = totalSeconds / 60L;
            long seconds = totalSeconds % 60L;
            StringBuilder timeString = new StringBuilder();
            if (hours > 0L) {
                timeString.append(hours).append(" ชั่วโมง ");
            }

            if (minutes > 0L) {
                timeString.append(minutes).append(" นาที ");
            }

            if (seconds > 0L) {
                timeString.append(seconds).append(" วินาที");
            }

            return timeString.toString().trim();
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Location location = event.getBlock().getLocation();
        Material material = event.getBlock().getType();
        ConfigurationSection materials = this.getConfig().getConfigurationSection("materials");
        if (materials != null && materials.contains(material.toString())) {
            event.setCancelled(true);
        } else {
            if (this.holograms.containsKey(location)) {
                TextDisplay hologram = (TextDisplay)this.holograms.remove(location);
                if (!hologram.isDead()) {
                    hologram.remove();
                }
            }

            this.cooldownManager.removeCooldown(location);
            this.hologramData.set(this.locationToString(location), (Object)null);

            try {
                this.hologramData.save(this.hologramDataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (this.harvestingPlayers.containsKey(player)) {
            Location blockLocation = (Location)this.harvestingPlayers.get(player);
            if (event.getFrom().distance(event.getTo()) > 0.1) {
                this.sendTitleSafe(player, this.getConfig().getString("harvest-messages.move-title", "§cYou moved!"), this.getConfig().getString("harvest-messages.move-subtitle", "§eHarvest cancelled!"));
                this.cancelHarvest(player, blockLocation);

                // เล่นเสียง Villager No
                String consoleCommand = this.getConfig().getString("failed-command.1");

                if (consoleCommand != null && !consoleCommand.isEmpty()) {
                    final String formattedCommand = consoleCommand.replace("%player%", player.getName());
                    getServer().getScheduler().runTask(this, () -> {
                        try {
                            getServer().dispatchCommand(getServer().getConsoleSender(), formattedCommand);
                        } catch (Exception e) {
                            getLogger().warning("Error executing console command: " + e.getMessage());
                        }
                    });
                }
                return;
            }
        }

    }

    private void createHologram(Location location, boolean isCooldown) {
        if (this.holograms.containsKey(location)) {
            TextDisplay oldHologram = (TextDisplay)this.holograms.remove(location);
            if (!oldHologram.isDead()) {
                oldHologram.remove();
            }
        }

        World world = location.getWorld();
        if (world != null) {
            TextDisplay hologram = (TextDisplay)world.spawn(location.clone().add((double)0.5F, (double)1.5F, (double)0.5F), TextDisplay.class);
            hologram.setText(isCooldown ? this.getConfig().getString("hologram.cooldown", "§cCooldown") : this.getConfig().getString("hologram.ready", "§aReady"));
            hologram.setBillboard(Billboard.CENTER);
            hologram.setShadowed(true);
            hologram.setSeeThrough(false);
            hologram.setGlowing(isCooldown);
            this.holograms.put(location, hologram);
        }
    }

    private void updateHologram(Location location, boolean isCooldown) {
        // ปรับพิกัดให้เป็น x - 0.5, z - 0.5
        Location adjustedLocation = location.clone().add(-0.5, 0, -0.5);
        this.createHologram(adjustedLocation, isCooldown);
    }

    private void pushPlayer(Player player, Location blockLocation) {
        player.setVelocity(player.getLocation().toVector().subtract(blockLocation.toVector()).normalize().multiply((double)0.5F).setY((double)0.5F));
    }

    private void saveHolograms() {
        this.holograms.forEach((location, hologram) -> {
            if (location.getBlock().getType() == Material.AIR) {
                this.hologramData.set(this.locationToString(location), (Object)null);
            } else {
                boolean isCooldown = this.cooldownManager.isInCooldown(location);
                this.hologramData.set(this.locationToString(location) + ".cooldown", isCooldown);
            }

        });

        try {
            this.hologramData.save(this.hologramDataFile);
        } catch (IOException e) {
            this.getLogger().warning("Failed to save hologram data!");
            e.printStackTrace();
        }

    }

    private void loadHolograms() {
        if (this.hologramDataFile.exists()) {
            this.hologramData.getKeys(false).forEach((key) -> {
                try {
                    Location location = this.stringToLocation(key);
                    if (location.getBlock().getType() == Material.AIR) {
                        this.hologramData.set(key, (Object)null);
                        return;
                    }

                    boolean isCooldown = this.hologramData.getBoolean(key + ".cooldown", false);
                    this.createHologram(location, isCooldown);
                } catch (Exception e) {
                    this.getLogger().warning("Failed to load hologram for key: " + key);
                    e.printStackTrace();
                }

            });

            try {
                this.hologramData.save(this.hologramDataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private Location stringToLocation(String string) {
        String[] parts = string.split(",");
        World world = Bukkit.getWorld(parts[0]);
        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);
        int z = Integer.parseInt(parts[3]);
        return new Location(world, (double)x, (double)y, (double)z);
    }

    private String locationToString(Location location) {
        String var10000 = location.getWorld().getName();
        return var10000 + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
    }
}
