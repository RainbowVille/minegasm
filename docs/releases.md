[⬅️ Home](./)

# Releases
## v0.4
* Now covers Minecraft 1.12 to 1.20
* Migrated hardware control library to [`buttplug4j`](https://github.com/blackspherefollower/buttplug4j) (this supersedes v0.3)

| Minecraft      | Forge                                                                                            | Java | JAR file                                                                                                                                                       |
|----------------|--------------------------------------------------------------------------------------------------|------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **1.20**.1     | [47.1.0](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.20.1.html)       | 17   | [minegasm-0.4.0-1.20.1-Forge-47.1.0.jar](https://github.com/RainbowVille/minegasm/releases/download/v0.4.0/minegasm-0.4.0-1.20.1-Forge-47.1.0.jar)             |
| **1.19**.4     | [45.1.0](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.19.4.html)       | 17   | [minegasm-0.4.0-1.19.4-Forge-45.1.0.jar](https://github.com/RainbowVille/minegasm/releases/download/v0.4.0/minegasm-0.4.0-1.19.4-Forge-45.1.0.jar)             |
| **1.18**.2     | [40.2.0](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.18.2.html)       | 17   | [minegasm-0.4.0-1.18.2-Forge-40.2.0.jar](https://github.com/RainbowVille/minegasm/releases/download/v0.4.0/minegasm-0.4.0-1.18.2-Forge-40.2.0.jar)             |
| **1.17**.1     | [37.1.1](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.17.1.html)       | 16   | [minegasm-0.4.0-1.17.1-Forge-37.1.1.jar](https://github.com/RainbowVille/minegasm/releases/download/v0.4.0/minegasm-0.4.0-1.17.1-Forge-37.1.1.jar)             |
| **1.16**.5     | [36.2.34](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.16.5.html)      | 11\* | [minegasm-0.4.0-1.16.5-Forge-36.2.34.jar](https://github.com/RainbowVille/minegasm/releases/download/v0.4.0/minegasm-0.4.0-1.16.5-Forge-36.2.34.jar)           |
| **1.15**.2     | [31.2.57](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.15.2.html)      | 11\* | [minegasm-0.4.0-1.15.2-Forge-31.2.57.jar](https://github.com/RainbowVille/minegasm/releases/download/v0.4.0/minegasm-0.4.0-1.15.2-Forge-31.2.57.jar)           |
| **1.14**.4     | [28.2.26](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.14.4.html)      | 11\* | [minegasm-0.4.0-1.14.4-Forge-28.2.26.jar](https://github.com/RainbowVille/minegasm/releases/download/v0.4.0/minegasm-0.4.0-1.14.4-Forge-28.2.26.jar)           |
| **1.13**.2     | [25.0.223](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.13.2.html)     | 11\* | [minegasm-0.4.0-1.13.2-Forge-25.0.223.jar](https://github.com/RainbowVille/minegasm/releases/download/v0.4.0/minegasm-0.4.0-1.13.2-Forge-25.0.223.jar)         |
| ~~**1.12**.2~~ | [14.23.5.2859](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.12.2.html) | 11\* | [minegasm-0.4.0-1.12.2-Forge-14.23.5.2859.jar](https://github.com/RainbowVille/minegasm/releases/download/v0.4.0/minegasm-0.4.0-1.12.2-Forge-14.23.5.2859.jar) |

Notes:
* Java 11 is required because of the `buttplug4j` library.
* The mod for v1.12.2 is reported to be broken.

## v0.3
*Internal only*. Switched the hardware control library to [`buttplug-rs-ffi`](https://github.com/buttplugio/buttplug-rs-ffi). Abandoned in favor of v0.4.

## v0.2
The mod is now configurable. From the mod config menu, you can choose between 3 predefined modes:

* NORMAL: the toy will vibrate when you attack other entities, mine, or gain XP.
* MASOCHIST: the toy will vibrate when you're hurt or dying.
* HEDONIST: the toy will vibrate on all events defined in other modes, except dying (this is replaced, instead, by having the toy vibrate when you're full and healthy).

The fourth CUSTOM mode allows you to set your own preferences by defining the intensity of the vibration on various events.

| Minecraft  | Forge                                                                                            | Java | JAR file                                                                                                                                                       |
|------------|--------------------------------------------------------------------------------------------------|------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **1.16**.5 | [36.0.14](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.16.5.html)      | 8    | [minegasm-0.2.1.jar](https://github.com/RainbowVille/minegasm/releases/download/v0.2.1-1.16.5/minegasm-0.2.1.jar)           |
| **1.12**.2 | [14.23.5.2855](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.12.2.html) | 8    | [minegasm-0.2.2-beta1.jar](https://github.com/RainbowVille/minegasm/releases/download/v0.2.2-1.12.2-beta/minegasm-0.2.2-beta1.jar) |

## v0.1
Initial release with very basic functionalities. The mod will connect to the toy upon starting and issue a vibrate command with a random intensity and duration whenever the player is hurt.

| Minecraft  | Forge                                                                                      | Java | JAR file                                                                                                                                                      |
|------------|--------------------------------------------------------------------------------------------|------|---------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **1.16**.3 | [34.1.0](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.16.3.html) | 8    | [minegasm-0.1.1.jar](https://github.com/RainbowVille/minegasm/releases/download/v0.1.1-1.16.3/minegasm-0.1.1.jar)           |

Other outdated releases can be found on: <https://github.com/RainbowVille/minegasm/releases>