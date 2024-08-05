---
layout: default
title: Configure
nav_order: 3
---

# Configuration

Since `v0.2`, it is possible to customize the behaviors of the mod to certain extent. It might be done through the mod
config screen (when available) or by editing the `minegasm-client.toml` config file manually with a text editor. The
file should typically reside in the minecraft's config directory (usually `.minecraft/config`).

There are a few tables (collections of key/value pairs) in the config file:

* `buttplug`: to configure the connection to the buttplug server
* `minegasm`: to configure the in-game behaviors of the mod

## buttplug

At the moment, there is only one option:

| Key         | Description                  | Default Value                                                                      |
|-------------|------------------------------|------------------------------------------------------------------------------------|
| `serverUrl` | The buttplug server address. | `ws://localhost:12345/buttplug` or `ws://127.0.0.1:12345/buttplug` (before `v0.4`) |

## minegasm

| Config  | Key       | Description                                                  |
|---------|-----------|--------------------------------------------------------------|
| Vibrate | `vibrate` | Enable/disable vibration                                     |
| Mode    | `mode`    | Select gameplay mode: NORMAL, MASOCHIST, HEDONIST, or CUSTOM |

Furthermore, it contains the following table(s):

* intensity

### intensity

| Config   | Key                 | Description                                                           |
|----------|---------------------|-----------------------------------------------------------------------|
| Attack   | `attackIntensity`   | Vibration intensity when attacking on custom mode                     |
| Hurt     | `hurtIntensity`     | Vibration intensity when hurting on custom mode                       |
| Mine     | `mineIntensity`     | Vibration intensity when mining on custom mode                        |
| XP       | `xpChangeIntensity` | Vibration intensity when gaining XP on custom mode                    |
| Harvest  | `harvestIntensity`  | Vibration intensity when harvesting on custom mode                    |
| Vitality | `vitalityIntensity` | Vibration intensity on high level of player's vitality on custom mode |

The default values of the intensity depends on the set `mode`:

#### NORMAL

| Attack | Hurt | Mine | XP   | Harvest | Vitality |
|--------|------|------|------|---------|----------|
| 60%    | Off  | 80%  | 100% | Off     | Off      |

#### MASOCHIST

| Attack | Hurt | Mine | XP  | Harvest | Vitality        |
|--------|------|------|-----|---------|-----------------|
| Off    | 100% | Off  | Off | Off     | 10%, when dying |

#### HEDONIST

| Attack | Hurt | Mine | XP   | Harvest | Vitality |
|--------|------|------|------|---------|----------|
| 60%    | 10%  | 80%  | 100% | 20%     | 10%      |

#### CUSTOM

No default. To emulate the behavior of Minegasm v0.1, you can use the following settings:

| Attack | Hurt | Mine | XP  | Harvest | Vitality |
|--------|------|------|-----|---------|----------|
| Off    | 100% | Off  | Off | Off     | Off      |
