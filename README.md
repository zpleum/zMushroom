# **🍄 ZMushroom — Interactive Harvesting System (v1.4 Full Version)**

```markdown
# 🍄 ZMushroom — Interactive Harvesting System (v1.4 Full Version)

An immersive and fully interactive harvesting system for Minecraft servers.  
Designed for RPG, Survival, and MMO-style gameplay with custom cooldowns, holograms, and ItemsAdder support.

---

## ✨ Features

### 🎯 Right-Click to Harvest
Players can harvest specific blocks simply by right-clicking.  
A configurable countdown timer adds immersion and prevents spam interactions.

### 🌟 Custom 3D Mushroom Model
Integrated with **ItemsAdder API** to display a fully custom 3D mushroom or any custom model you define.

### 🔮 Dynamic Holograms (TextDisplay)
Floating holograms indicate:
- 🟢 **Ready**
- 🔴 **Cooldown**

Uses **TextDisplay** instead of ArmorStands for better performance.

### ⏳ Cooldown System
Each block type can have its own cooldown time with full persistence across restarts.

### 🛡️ Anti-Abuse Protection
- Prevents multiple players from harvesting the same block  
- Cancels harvesting if the player moves  
- Prevents macro or autoclick exploits

### 🌍 World & Material Filtering
Enable harvesting only in specific worlds  
Allow only selected block materials to be harvestable

### 🎵 Fully Customizable Feedback
Customizable:
- Titles
- Subtitles
- Holograms
- Sounds
- Command rewards
- Countdown effects

### 💾 Persistent Storage
All holograms and cooldown timers are stored in `holograms.yml`, restoring perfectly after restarts.

### 📝 Commands

```

/zmushroom reload

````

Reloads configuration and cooldown data without restarting the server.

---

## 📦 Installation

1. Drop the plugin `.jar` into `/plugins`
2. Install and configure **ItemsAdder**
3. Restart your server
4. Edit `config.yml` to your liking
5. Use `/zmushroom reload` when modifying the config

---

## ⚙️ Example Configuration

```yaml
### SYSTEM BY ZPLEUM SUD XAO ###

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
  ready: "§aเห็ดพร้อมเก็บเกี่ยวแล้ว!"
  cooldown: "§cเห็ดกำลังโต! กรุณารอสักครู่"

harvest-messages:
  countdown-title: "§e%time%"
  countdown-subtitle: "§aคุณกำลังเก็บเห็ด อดทนหน่อยนะ"

  countdown:
    5: "&c"
    4: "&a&c"
    3: "&a&c"
    2: "&a&c"
    1: "&a&c"

  success-title: "§aคุณได้ทำการเก็บเห็ดแล้ว!"
  success-subtitle: "§eอย่าเผลอกินเข้าไปล่ะ!"
  fail-title: "§cคุณเผลอทำเห็ดหลุดมือ!"
  fail-subtitle: "§eคราวหลังก็ระวังด้วยล่ะ!"

  cooldown-title: "§cเห็ดกำลังโต! กลับมาในภายหลังนะ"
  cooldown-subtitle: "§eเห็ดจะโตในอีก: %time%"

  already-harvesting-title: "§cคุณเก็บเห็ดได้ทีละต้นเท่านั้นนะ!"
  already-harvesting-subtitle: "§eคุณกำลังเก็บเห็ดอีกต้นอยู่แล้ว!"

  active-player-title: "§cมีคุณกำลังเก็บเห็ดต้นนี้อยู่แล้ว!"
  active-player-subtitle: "§eผู้เล่นที่กําลังเก็บเกี่ยว §f%player%"

  move-title: "§cคุณเผลอทำเห็ดหลุดมือ!"
  move-subtitle: "§eการเก็บเห็ดถูกยกเลิก! น่าเสียดาย"

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
  reload-success: "ทำการรีโหลด Config และการตั้งค่าสำเร็จ!"
  reload-usage: "§cAn internal error occurred while attempting to perform this command."
  no-hologram: "§cAn internal error occurred while attempting to perform this command."

### SYSTEM BY ZPLEUM SUD XAO ###
````

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
