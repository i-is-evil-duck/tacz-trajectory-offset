# TaCZ Scope Zeroing

A Minecraft Forge 1.20.1 addon for [Timeless and Classics Zero (TaCZ)](https://modrinth.com/mod/timeless-and-classics-zero) that lets you adjust your scope's zeroing — both vertically and horizontally.


## What it adds

TaCZ Scope Zeroing adds precise, per-scope angle adjustment to TaCZ guns. Hold a gun and use your arrow keys to adjust pitch (up/down) and yaw (left/right) in 0.01 degree increments to compensate for bullet drop and dial in long-range shots.


## Features

- **Pitch & Yaw Adjustment** — UP/DOWN adjusts vertical (pitch), LEFT/RIGHT adjusts horizontal (yaw) in 0.01° steps
- **Range** — -5 to +5 degrees on each axis
- **Per-Scope Offsets** — Each scope instance stores its own zeroing independently. Two identical scopes will have separate offset values. Offsets travel with the scope when moved between guns.
- **Lock Key** — Press **L** to freeze current offsets and prevent further adjustments. Press again to unlock.
- **Reset Key** — Press **;** to reset both pitch and yaw to zero.
- **HUD Display** — Offset values appear above the hotbar when you adjust, then fade out after 1.5 seconds.
- **Toggle Command** — Run `/tacz0ing toggle` to enable or disable the HUD display entirely.
- **Configurable Keys** — Lock and reset keys can be changed in `config/tacz0ing-common.toml` via GLFW key codes.


## Key Bindings

| Key | Action |
|-----|--------|
| UP / DOWN | Adjust pitch (vertical zeroing) |
| LEFT / RIGHT | Adjust yaw (horizontal zeroing) |
| L | Toggle lock (freeze current offsets) |
| ; | Reset both pitch and yaw to zero |
| `/tacz0ing toggle` | Show / hide the HUD display |


## Config

Keys are configurable in `config/tacz0ing-common.toml`:

```toml
[tacz0ing]
  # GLFW key code for the lock key (default 76 = L)
  lockKey=76
  # GLFW key code for the reset key (default 59 = semicolon ;)
  resetKey=59
```


## Important

**Requires [Timeless and Classics Zero (TaCZ)](https://modrinth.com/mod/timeless-and-classics-zero) to be installed.** Will not function without it.


**Please report any issues on the [issue tracker](https://github.com/i-is-evil-duck/tacz-trajectory-offset/issues)** — it really helps :)
