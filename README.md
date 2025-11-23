Got you. I’ll clean it up, make it sleek, readable, and professional — but still punchy.

---

# **🍄 ZMushroom — Interactive Harvesting System (v1.4 Full Version)**

A fully–modular harvesting system built for modern Minecraft servers.

Designed for RPG, Survival, and MMO servers that want an immersive, animated, and customizable gathering experience — with cooldowns, holograms, ItemsAdder support, and anti-exploit logic baked in.

---

## **✨ Key Features**

### **🎯 Right-Click to Harvest**

Players harvest by simply right-clicking the configured block.
A built-in countdown prevents spam and boosts immersion.

---

### **🌟 Custom 3D Mushroom Models (ItemsAdder)**

Seamless **ItemsAdder** integration.
Use any custom 3D model — mushrooms, plants, crystals, whatever you want.

---

### **🔮 Dynamic Holograms (TextDisplay)**

Lightweight holograms showing:

* 🟢 **Ready**
* 🔴 **Cooldown**

Uses **TextDisplay**, not ArmorStands → better server performance.

---

### **⏳ Per-Block Cooldowns**

Each material can have:

* Its own cooldown time
* Custom commands
* Individual success rates
* Fully persistent state across restarts

---

### **🛡️ Anti-Abuse Smart System**

* Only one player can harvest a block at a time
* Cancels if player moves
* Blocks macro / auto-click spam
* Prevents multi-interactions on the same node

---

### **🌍 World & Material Filtering**

Enable harvesting only in specific worlds
Define exactly which ItemsAdder models can be harvested

---

### **🎵 Fully Customizable Feedback**

Adjust everything to your server style:

* Titles
* Subtitles
* Actionbar countdown
* Sounds
* Commands
* Holograms

---

### **💾 Persistent Storage**

All holograms + cooldown data stored in `holograms.yml`
Restores exactly after server restarts.

---

## **📝 Commands**

```
/zmushroom reload
```

Reloads config + cooldown state without restarting the server.

---

## **📦 Installation**

1. Put the `.jar` into `/plugins`
2. Ensure **ItemsAdder** is installed & loaded
3. Restart your server
4. Edit `config.yml`
5. Run `/zmushroom reload` after changes

---

## **⚙️ Example Configuration**

```yaml
### VERSION 1.4-Beta

countdown-time: 5
enable-world: "mushm"

materials:
  # Hard - Yellow
  workshop_six:glow_mushroom_patch_large:
    command: "mmoitems give MATERIAL MUSHROOM_LV4 %player% 1 0 100 0 silent"
    cooldown-time: 10
    success-rate: 50.0
  workshop_six:glow_mushroom_patch:
    command: "mmoitems give MATERIAL MUSHROOM_LV4 %player% 1 0 100 0 silent"
    cooldown-time: 10
    success-rate: 55.0

  # Medium-Hard - Orange
  workshop_six:orange_mushroom_patch_large:
    command: "mmoitems give MATERIAL MUSHROOM_LV3 %player% 1 0 100 0 silent"
    cooldown-time: 8
    success-rate: 50.0
  workshop_six:orange_mushroom_patch:
    command: "mmoitems give MATERIAL MUSHROOM_LV3 %player% 1 0 100 0 silent"
    cooldown-time: 8
    success-rate: 50.0

  # Medium - Red
  workshop_six:red_mushroom_patch_large:
    command: "mmoitems give MATERIAL MUSHROOM_LV2 %player% 1 0 100 0 silent"
    cooldown-time: 5
    success-rate: 65.0
  workshop_six:red_mushroom_patch:
    command: "mmoitems give MATERIAL MUSHROOM_LV2 %player% 1 0 100 0 silent"
    cooldown-time: 5
    success-rate: 70.0

  # Easy - Brown
  workshop_six:brown_mushroom_patch_large:
    command: "mmoitems give MATERIAL MUSHROOM_LV1 %player% 1 0 100 0 silent"
    cooldown-time: 3
    success-rate: 80.0
  workshop_six:brown_mushroom_patch:
    command: "mmoitems give MATERIAL MUSHROOM_LV1 %player% 1 0 100 0 silent"
    cooldown-time: 3
    success-rate: 85.0

hologram:
  ready: "§aMushroom is ready!"
  cooldown: "§cGrowing... Please wait."

harvest-messages:
  countdown-title: "§e%time%"
  countdown-subtitle: "§aHarvesting..."

  countdown:
    5: "&c"
    4: "&a&c"
    3: "&a&c"
    2: "&a&c"
    1: "&a&c"

  success-title: "§aHarvest complete!"
  success-subtitle: "§eDon't eat it (probably)."
  fail-title: "§cOops! You dropped it!"
  fail-subtitle: "§eBe careful next time."

  cooldown-title: "§cStill growing..."
  cooldown-subtitle: "§eBack in: %time%"

  already-harvesting-title: "§cYou’re already harvesting!"
  already-harvesting-subtitle: "§eFinish the current mushroom first."

  active-player-title: "§cSomeone is harvesting this already!"
  active-player-subtitle: "§eCurrent player: §f%player%"

  move-title: "§cYou moved!"
  move-subtitle: "§eHarvest cancelled."

failed-command:
  1: "playsound minecraft:entity.villager.no player %player% %player_x% %player_y% %player_z%"
  2: "playsound minecraft:entity.villager.celebrate player %player% %player_x% %player_y% %player_z%"

custom-sound:
  console-command:
    enabled: true
    5: "playsound minecraft:block.note_block.pling player %player% %player_x% %player_y% %player_z%"
    4: "playsound minecraft:block.note_block.pling player %player% %player_x% %player_y% %player_z%"
    3: "playsound minecraft:block.note_block.pling player %player% %player_x% %player_y% %player_z%"
    2: "playsound minecraft:block.note_block.pling player %player% %player_x% %player_y% %player_z%"
    1: "playsound minecraft:block.note_block.pling player %player% %player_x% %player_y% %player_z%"
    0: "playsound minecraft:block.note_block.bell player %player% %player_x% %player_y% %player_z%"

messages:
  reload-success: "Reload complete!"
  reload-usage: "§cAn internal error occurred."
  no-hologram: "§cHologram data not found."

# © 2025 zPleum. Licensed under MIT License. | https://zpleum.site/
```

---

## 📜 License (MIT)

```markdown
MIT License

Copyright (c) 2025 Wiraphat Makwong

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights  
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell  
copies of the Software, and to permit persons to whom the Software is  
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in  
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR  
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,  
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE  
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER  
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING  
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER  
DEALINGS IN THE SOFTWARE.
```
