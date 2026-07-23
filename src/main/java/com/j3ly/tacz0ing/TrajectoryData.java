package com.j3ly.tacz0ing;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TrajectoryData {
    private static final String PITCH_NBT_KEY = "tacz0ing_trajectory_pitch";
    private static final String YAW_NBT_KEY = "tacz0ing_trajectory_yaw";
    private static final String LOCK_NBT_KEY = "tacz0ing_trajectory_locked";

    public static final float MAX_OFFSET = 5.0f;
    public static final float MIN_OFFSET = -5.0f;
    public static final float STEP = 0.01f;

    // --- Pitch (vertical) ---

    public static float getPitchOffset(Player player) {
        CompoundTag data = player.getPersistentData();
        if (data.contains(PITCH_NBT_KEY)) {
            return data.getFloat(PITCH_NBT_KEY);
        }
        return 0.0f;
    }

    public static void setPitchOffset(Player player, float offset) {
        offset = Math.max(MIN_OFFSET, Math.min(MAX_OFFSET, offset));
        player.getPersistentData().putFloat(PITCH_NBT_KEY, offset);
    }

    public static void adjustPitchOffset(Player player, float delta) {
        float current = getPitchOffset(player);
        setPitchOffset(player, current + delta);
    }

    // --- Yaw (horizontal) ---

    public static float getYawOffset(Player player) {
        CompoundTag data = player.getPersistentData();
        if (data.contains(YAW_NBT_KEY)) {
            return data.getFloat(YAW_NBT_KEY);
        }
        return 0.0f;
    }

    public static void setYawOffset(Player player, float offset) {
        offset = Math.max(MIN_OFFSET, Math.min(MAX_OFFSET, offset));
        player.getPersistentData().putFloat(YAW_NBT_KEY, offset);
    }

    public static void adjustYawOffset(Player player, float delta) {
        float current = getYawOffset(player);
        setYawOffset(player, current + delta);
    }

    // --- Lock state ---

    public static boolean isLocked(Player player) {
        return player.getPersistentData().getBoolean(LOCK_NBT_KEY);
    }

    public static void setLocked(Player player, boolean locked) {
        player.getPersistentData().putBoolean(LOCK_NBT_KEY, locked);
    }

    // --- Combined ---

    public static void clearAllOffsets(Player player) {
        setPitchOffset(player, 0.0f);
        setYawOffset(player, 0.0f);
    }

    // --- Legacy compat (getOffset returns pitch for mixin/backwards compat) ---

    public static float getOffset(Player player) {
        return getPitchOffset(player);
    }

    public static void setOffset(Player player, float offset) {
        setPitchOffset(player, offset);
    }

    public static void adjustOffset(Player player, float delta) {
        adjustPitchOffset(player, delta);
    }

    public static void clearOffset(Player player) {
        player.getPersistentData().remove(PITCH_NBT_KEY);
    }

    // --- Events ---

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        CompoundTag orig = event.getOriginal().getPersistentData();
        CompoundTag dest = event.getEntity().getPersistentData();
        if (orig.contains(PITCH_NBT_KEY)) {
            dest.putFloat(PITCH_NBT_KEY, orig.getFloat(PITCH_NBT_KEY));
        }
        if (orig.contains(YAW_NBT_KEY)) {
            dest.putFloat(YAW_NBT_KEY, orig.getFloat(YAW_NBT_KEY));
        }
        if (orig.contains(LOCK_NBT_KEY)) {
            dest.putBoolean(LOCK_NBT_KEY, orig.getBoolean(LOCK_NBT_KEY));
        }
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = (Player) event.getEntity();
        clearOffset(player);
        player.getPersistentData().remove(YAW_NBT_KEY);
        player.getPersistentData().remove(LOCK_NBT_KEY);
    }
}
