Minegasm is a Minecraft (Java Edition) Forge mod that uses connected sex toys to enhance the gameplay experience. This mod is intended to be used only by consenting adults.

## Download
JAR file: [minegasm-0.2.0.jar](https://github.com/RainbowVille/minegasm/releases/download/v0.2.0/minegasm-0.2.0.jar)

### Dependencies
1. Intiface Desktop: [https://intiface.com/desktop/](https://intiface.com/desktop/)
2. Minecraft Forge - MC 1.16.5: [http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.16.5.html](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.16.5.html)

## How to Use
1. Make sure you have all the dependencies installed.
2. Download the JAR file and put it in your mods directory. If you don't know where your mods directory is, you can simply open your Minecraft, click the 'Mods' button on the main menu, then click 'Open Mods Folder'.
3. Run the Intiface Desktop and turn on the Regular Websockets server (on 127.0.0.1:12345). For advanced users, it is possible to use a buttplug server on a different endpoint by specifying it in the Minegasm config file.
4. Turn on the device.
5. Start Minecraft and connect to a world. If everything works properly, you should see a message stating that Minegasm is connected to your device when you enter the world. If the connection fails, go back to Intiface Desktop and make sure that the Server Status shows that the server is running. To force Minegasm to retry to connect, leave the world and then re-enter.
6. Have fun!

## Supported Devices
In theory, this mod should be compatible with any devices on this list: [IoST Index – Vibrators with Buttplug.io Support](https://iostindex.com/?filter0ButtplugSupport=4&filter1Features=OutputsVibrators)

It has been confirmed to work with:
- [Lovense Edge](https://www.lovense.com/r/qvl9jn) (prostate massager)
- [Lovense Hush](https://www.lovense.com/r/zrzb5e) (butt plug)
- [Lovense Max 2](https://www.lovense.com/r/n4x2bh) (male masturbator)

## Configuration
Minegasm is configurable through the `Mods > Config` screen, as well as by manually editing the `minegasm-client.toml` config file.

| Config   | Config Key                             | Description                                                           | Version |
| -------- | -------------------------------------- | --------------------------------------------------------------------- | ------- |
| Vibrate  | `minegasm.vibrate`                     | Enable/disable vibration                                              | 0.2+    |
| Mode     | `minegasm.mode`                        | Select gameplay mode: NORMAL, MASOCHIST, HEDONIST, or CUSTOM          | 0.2+    |
| Attack   | `minegasm.intensity.attackIntensity`   | Vibration intensity when attacking on custom mode                     | 0.2+    |
| Hurt     | `minegasm.intensity.hurtIntensity`     | Vibration intensity when hurting on custom mode                       | 0.2+    |
| Mine     | `minegasm.intensity.mineIntensity`     | Vibration intensity when mining on custom mode                        | 0.2+    |
| XP       | `minegasm.intensity.xpChangeIntensity` | Vibration intensity when gaining XP on custom mode                    | 0.2+    |
| Harvest  | `minegasm.intensity.harvestIntensity`  | Vibration intensity when harvesting on custom mode                    | 0.2+    |
| Vitality | `minegasm.intensity.vitalityIntensity` | Vibration intensity on high level of player's vitality on custom mode | 0.2+    |

Additionally, the config file contains a configurable `buttplug.serverUrl` to allow connection to a custom buttplug server.

## Gameplay modes
### NORMAL
In this mode, the toy will vibrate when you attack other entities, mine, or gain XP.

|              | Intensity |
| ------------ | --------- |
| **Attack**   | 60%       |
| **Hurt**     | Off       |
| **Mine**     | 80%       |
| **XP**       | 100%      |
| **Harvest**  | Off       |
| **Vitality** | Off       |

### MASOCHIST
In this mode, the toy will vibrate when you're hurt or dying.

|              | Intensity |
| ------------ | --------- |
| **Attack**   | Off       |
| **Hurt**     | 100%      |
| **Mine**     | Off       |
| **XP**       | Off       |
| **Harvest**  | Off       |
| **Vitality** | 10%, when dying |

### HEDONIST
In this mode, the toy will vibrate on all events defined in other modes, except dying (this is replaced, instead, by having the toy vibrate when you're full and healthy).

|              | Intensity |
| ------------ | --------- |
| **Attack**   | 60%       |
| **Hurt**     | 10%       |
| **Mine**     | 80%       |
| **XP**       | 100%      |
| **Harvest**  | 20%       |
| **Vitality** | 10%       |

### CUSTOM
In this mode, the toy will vibrate depending on the intensity levels configured by the user.

To emulate the behavior of Minegasm v0.1, you can use the following settings:

|              | Intensity |
| ------------ | --------- |
| **Attack**   | Off       |
| **Hurt**     | 100%      |
| **Mine**     | Off       |
| **XP**       | Off       |
| **Harvest**  | Off       |
| **Vitality** | Off       |

## Troubleshooting
As with a lot of computer issues, the very first thing you should try is to restart everything. While not strictly necessary, we recommend that you start your device and the Intiface server before Minecraft. This should solve most intermittent issues.

If you run this mod alongside other mods, try disabling them all first and then reenable them one by one when the mods run successfully. If you found any mods that are incompatible with this mod, please file an issue on our [issue tracker](https://github.com/RainbowVille/minegasm/issues).

## Support
In principle, we do not provide any dedicated (technical) supports. If you need any assistance, you can use the [discussion page](https://github.com/RainbowVille/minegasm/discussions). Alternatively, you can join our [Discord server](https://discord.gg/Kc7ueWC) and ping `@RSwoop` in the `#minegasm` channel, but please be advised that he might not always be able to help you.

If you have any suggestions or found any bugs, please post them to our [issue tracker](https://github.com/RainbowVille/minegasm/issues) on GitHub. Please try to be as descriptive as possible.

The status of confirmed bugs and planned future enhancements are available on our [project board](https://github.com/RainbowVille/minegasm/projects/1).

## Credits
- `Lone_Destroyer` for the logo
- `qdot` for the buttplug library
