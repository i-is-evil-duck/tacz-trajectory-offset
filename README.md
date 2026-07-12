# TACZ Trajectory Offset Mod

A Forge 1.20.1 addon for [TACZ](https://www.curseforge.com/minecraft/mc-mods/timeless-and-classics-zero) (Timeless and Classics Zero) that allows players to adjust bullet trajectory vertical angle using arrow keys.

## Features

- **Arrow Key Control**: Press UP/DOWN arrow keys while holding a TACZ gun to adjust bullet trajectory
- **1 Degree Steps**: Each keypress adjusts by exactly 1 degree
- **Range**: -45 to +45 degrees
- **HUD Display**: Shows current offset above the hotbar when holding a TACZ gun
- **Server-Side Sync**: Offset is stored server-side and synced from client input
- **Per-Player**: Each player has their own independent offset

## Controls

| Key | Action |
|---|---|
| UP / RIGHT Arrow | Increase upward offset (+1 degree) |
| DOWN / LEFT Arrow | Decrease offset (-1 degree) |
| R | Reset offset to 0 |

## How It Works

### Client Side
1. Detects when player is holding a TACZ gun (`IGun` item)
2. Captures UP/DOWN/LEFT/RIGHT arrow key and R key presses
3. Tracks offset locally (for HUD display) and sends change to server via custom network packet
4. Renders HUD showing current offset

### Server Side
1. Receives offset change packets from clients
2. Stores per-player offset in `TrajectoryData`
3. When a TACZ bullet (`EntityKineticBullet`) spawns, the Mixin intercepts `shootFromRotation` and modifies the pitch angle
4. A fallback `EntityJoinLevelEvent` handler provides debug logging if the Mixin fails to apply
5. The bullet then travels at the modified angle

## Building

### Prerequisites
- Java 17+
- TACZ mod installed in your dev environment

### Setup
1. Clone this repository
2. Run `./gradlew build` (Linux/Mac) or `gradlew.bat build` (Windows)
3. The built jar will be in `build/libs/`

### Dev Environment
```bash
# Generate IDE runs
./gradlew genIntellijRuns  # For IntelliJ IDEA
./gradlew genEclipseRuns   # For Eclipse

# Run client
./gradlew runClient
```

## Important Notes

### TACZ is Closed Source
TACZ's source code is not publicly available. This mod uses:
- **Mixin** (primary) to modify the pitch parameter in `EntityKineticBullet.shootFromRotation`
- **Forge Events** (`EntityJoinLevelEvent`) as a fallback/debug mechanism
- **Reflection** via TACZ's API (`IGun`, `getOwner()`) for safe interactions

### Mixin Target
The Mixin targets `EntityKineticBullet.shootFromRotation(Entity, float, float, float, float, Vector2d)` and modifies the first float parameter (pitch / `pX`). If TACZ changes this method signature, the Mixin will fail and the fallback event handler will log a warning.

## Multiplayer

This mod works on both client and server:
- **Client**: Handles key input, local offset tracking, and HUD rendering
- **Server**: Stores offsets and applies them to bullets via Mixin
- Both sides need the mod installed for full functionality

## Compatibility

- **Minecraft**: 1.20.1
- **Forge**: 47.0.0+
- **TACZ**: 1.1.0+

## Troubleshooting

### "Cannot find TACZ classes" during build
Make sure the TACZ dependency in `build.gradle` points to a valid CurseMaven artifact. You may need to update the file ID.

### Bullets not changing trajectory
1. Check server logs for debug messages from `BulletInterceptor`
2. The Mixin may be failing to apply — check for Mixin error messages in the log
3. Verify the method signature in `EntityKineticBullet.shootFromRotation` matches what the Mixin expects

### Arrow keys not working
Make sure no other mod is capturing those keys. The mod only processes keys when:
- No GUI screen is open
- Player is holding a TACZ gun in main hand

## License

MIT License
