Minegasm is a Minecraft (Java Edition) Forge mod that uses connected sex toys to enhance the gameplay experience. This mod is intended to be used only by consenting adults.

## Download

| Minecraft  | Forge        | JAR file                                                                                                          |
|------------|--------------|-------------------------------------------------------------------------------------------------------------------|
| **1.20**.1 | 47.0.19      | _coming soon_                                                                                                     |
| **1.19**.4 | 45.1.2       | _coming soon_                                                                                                     |
| **1.18**.2 | 40.2.0       | _coming soon_                                                                                                     |
| **1.17**   | -            | _coming soon_                                                                                                     |
| **1.16**.5 | [36.1.4](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.16.5.html)    | [minegasm-0.2.1.jar](https://github.com/RainbowVille/minegasm/releases/download/v0.2.1-1.16.5/minegasm-0.2.1.jar) |
| **1.15**   | -            | _coming soon_                                                                                                     |
| **1.14**   | -            | _coming soon_                                                                                                     |
| **1.13**   | -            | _coming soon_                                                                                                     |
| **1.12**.2 | 14.23.5.2855 | _coming soon_                                                                                                     |

Old releases can be found here: <https://github.com/RainbowVille/minegasm/releases>

### Dependencies
1. Intiface Central: [https://intiface.com/central/](https://intiface.com/central/)
2. Minecraft Forge: the versions mentioned on the corresponding Forge column above are the tested version (newer versions are probably okay) 

## How to Use
1. Make sure you have all the dependencies installed.
2. Download the JAR file and put it in your mods directory. If you don't know where your mods directory is, you can simply open your Minecraft, click the 'Mods' button on the main menu, then click 'Open Mods Folder'.
3. Run the Intiface Central and start the server (`ws://localhost:12345`). For advanced users, it is possible to use an Intiface server on a different endpoint by specifying it in the Minegasm config file.
4. Turn on the device.
5. Start Minecraft and connect to a world. If everything works properly, you should see a message stating that Minegasm is connected to your device when you enter the world. If the connection fails, go back to Intiface Central and make sure that the Server Status shows that the server is running. To force Minegasm to retry to connect, leave the world and then re-enter.
6. Have fun!

## Known Issues
The mod has only undergone limited testing in multiplayer mode and it might not work with some mods. See the *Troubleshooting* part below if you encounter any issues.

- \[[#12](https://github.com/RainbowVille/minegasm/issues/12)\] In the multiplayer mode, the on hurt vibration is not triggered.

See our [issue tracker](https://github.com/RainbowVille/minegasm/issues?q=is%3Aissue+is%3Aopen+label%3Abug) for all the reported issues.

## Supported Devices
In theory, this mod should be compatible with any devices on this list: [IoST Index – Vibrators with Buttplug.io Support](https://iostindex.com/?filter0ButtplugSupport=4&filter1Features=OutputsVibrators)

It has been confirmed to work with:
- [Lovense Edge](https://www.lovense.com/r/qvl9jn) (prostate massager)
- [Lovense Hush](https://www.lovense.com/r/zrzb5e) (butt plug)
- [Lovense Max 2](https://www.lovense.com/r/n4x2bh) (male masturbator)

## Configuration
Minegasm is configurable through the `Mods > Config` screen, as well as by manually editing the `minegasm-client.toml` config file.

| Config   | `minegasm.*`                  | Description                                                           | Version |
| -------- | ----------------------------- | --------------------------------------------------------------------- | ------- |
| Vibrate  | `vibrate`                     | Enable/disable vibration                                              | 0.2+    |
| Mode     | `mode`                        | Select gameplay mode: NORMAL, MASOCHIST, HEDONIST, or CUSTOM          | 0.2+    |
| Attack   | `intensity.attackIntensity`   | Vibration intensity when attacking on custom mode                     | 0.2+    |
| Hurt     | `intensity.hurtIntensity`     | Vibration intensity when hurting on custom mode                       | 0.2+    |
| Mine     | `intensity.mineIntensity`     | Vibration intensity when mining on custom mode                        | 0.2+    |
| XP       | `intensity.xpChangeIntensity` | Vibration intensity when gaining XP on custom mode                    | 0.2+    |
| Harvest  | `intensity.harvestIntensity`  | Vibration intensity when harvesting on custom mode                    | 0.2+    |
| Vitality | `intensity.vitalityIntensity` | Vibration intensity on high level of player's vitality on custom mode | 0.2+    |

Additionally, the config file contains a configurable `buttplug.serverUrl` to allow connection to a custom buttplug server.

## Gameplay modes
### NORMAL
In this mode, the toy will vibrate when you attack other entities, mine, or gain XP.

| Attack | Hurt | Mine | XP | Harvest | Vitality |
| --- | --- | --- | --- | --- | ---|
| 60% | Off | 80% | 100% | Off | Off |

### MASOCHIST
In this mode, the toy will vibrate when you're hurt or dying.

| Attack | Hurt | Mine | XP | Harvest | Vitality |
| --- | --- | --- | --- | --- | ---|
| Off | 100% | Off | Off | Off | 10%, when dying |

### HEDONIST
In this mode, the toy will vibrate on all events defined in other modes, except dying (this is replaced, instead, by having the toy vibrate when you're full and healthy).

| Attack | Hurt | Mine | XP | Harvest | Vitality |
| --- | --- | --- | --- | --- | ---|
| 60% | 10% | 80% | 100% | 20% | 10% |

### CUSTOM
In this mode, the toy will vibrate depending on the intensity levels configured by the user.

To emulate the behavior of Minegasm v0.1, you can use the following settings:

| Attack | Hurt | Mine | XP | Harvest | Vitality |
| --- | --- | --- | --- | --- | ---|
| Off | 100% | Off | Off | Off | Off |

## Troubleshooting
As with a lot of computer issues, the very first thing you should try is to restart everything. While not strictly necessary, we recommend that you start your device and the Intiface server before Minecraft. This should solve most intermittent issues.

If you run this mod alongside other mods, try disabling them all first and then reenable them one by one when the mods run successfully. If you found any mods that are incompatible with this mod, please file an issue on our [issue tracker](https://github.com/RainbowVille/minegasm/issues).

## Support
In principle, we do not provide any dedicated (technical) supports. If you need any assistance, you can use the [discussion page](https://github.com/RainbowVille/minegasm/discussions). Alternatively, you can join our [Discord server](https://discord.gg/Kc7ueWC) and ping `@RSwoop` in the `#minegasm` channel, but please be advised that he might not always be able to help you.

If you have any suggestions or found any bugs, please post them to our [issue tracker](https://github.com/RainbowVille/minegasm/issues) on GitHub. Please try to be as descriptive as possible and include your `debug.log` (please remove any references to your username first if that matters). The `debug.log` file is typically under the `logs` directory of your minecraft instance. In the vanilla installation on Windows, it is typically on `<drive>:\Users\<username>\AppData\Roaming\.minecraft`.

The status of confirmed bugs and planned future enhancements are available on our [project board](https://github.com/RainbowVille/minegasm/projects/1).

## Credits
- `Lone_Destroyer` for the logo
- `qdot` for the buttplug library
