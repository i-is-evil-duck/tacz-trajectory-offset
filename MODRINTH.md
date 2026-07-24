# TaCZ Scope Zeroing

Adjust your scope's zeroing on Timeless and Classics Zero guns — both vertically and horizontally — to compensate for bullet drop and dial in long-range shots.

## Features

- **Pitch & Yaw Adjustment** — Arrow keys adjust vertical (pitch) and horizontal (yaw) in 0.01° steps
- **Range** — -5 to +5 degrees on each axis
- **Per-Scope Offsets** — Each scope stores its own zeroing independently. Offsets travel with the scope when moved between guns
- **Lock Key** — Press **L** to freeze current offsets and prevent further adjustments
- **Reset Key** — Press **;** to reset both pitch and yaw to zero
- **HUD Display** — Offset values appear above the hotbar when you adjust, then fade out after 1.5 seconds
- **Toggle Command** — Run `/tacz0ing toggle` to enable or disable the HUD display
- **Configurable Keys** — Lock and reset keys can be changed in the config file

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
  lockKey=76
  resetKey=59
```

## Requirements

**Requires [Timeless and Classics Zero (TaCZ)](https://modrinth.com/mod/timeless-and-classics-zero)** to be installed. Will not function without it.

## Links

- [Source Code](https://github.com/i-is-evil-duck/tacz-trajectory-offset)
- [Issue Tracker](https://github.com/i-is-evil-duck/tacz-trajectory-offset/issues)