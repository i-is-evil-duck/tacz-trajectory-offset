package com.j3ly.tacz0ing;

import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.attachment.AttachmentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TrajectoryData {
    private static final String PITCH_KEY = "tacz0ing_pitch";
    private static final String YAW_KEY = "tacz0ing_yaw";
    private static final String LOCK_KEY = "tacz0ing_trajectory_locked";

    public static final float MAX_OFFSET = 5.0f;
    public static final float MIN_OFFSET = -5.0f;
    public static final float STEP = 0.01f;

    /**
     * Get the scope ItemStack from a player's held gun.
     * Returns ItemStack.EMPTY if no gun or no scope.
     */
    public static ItemStack getScopeStack(Player player) {
        ItemStack gunStack = player.getMainHandItem();
        IGun igun = IGun.getIGunOrNull(gunStack);
        if (igun == null) return ItemStack.EMPTY;
        return igun.getAttachment(gunStack, AttachmentType.SCOPE);
    }

    // --- Pitch (vertical) - stored on scope item NBT (per-instance) ---

    public static float getPitchOffset(Player player) {
        ItemStack scope = getScopeStack(player);
        if (scope.isEmpty()) return 0.0f;
        CompoundTag tag = scope.getTag();
        if (tag == null || !tag.contains(PITCH_KEY)) return 0.0f;
        return tag.getFloat(PITCH_KEY);
    }

    public static void setPitchOffset(Player player, float offset) {
        ItemStack scope = getScopeStack(player);
        if (scope.isEmpty()) return;
        offset = Math.max(MIN_OFFSET, Math.min(MAX_OFFSET, offset));
        scope.getOrCreateTag().putFloat(PITCH_KEY, offset);
    }

    public static void adjustPitchOffset(Player player, float delta) {
        setPitchOffset(player, getPitchOffset(player) + delta);
    }

    // --- Yaw (horizontal) - stored on scope item NBT (per-instance) ---

    public static float getYawOffset(Player player) {
        ItemStack scope = getScopeStack(player);
        if (scope.isEmpty()) return 0.0f;
        CompoundTag tag = scope.getTag();
        if (tag == null || !tag.contains(YAW_KEY)) return 0.0f;
        return tag.getFloat(YAW_KEY);
    }

    public static void setYawOffset(Player player, float offset) {
        ItemStack scope = getScopeStack(player);
        if (scope.isEmpty()) return;
        offset = Math.max(MIN_OFFSET, Math.min(MAX_OFFSET, offset));
        scope.getOrCreateTag().putFloat(YAW_KEY, offset);
    }

    public static void adjustYawOffset(Player player, float delta) {
        setYawOffset(player, getYawOffset(player) + delta);
    }

    // --- Lock state - on player persistent data (session-level) ---

    public static boolean isLocked(Player player) {
        return player.getPersistentData().getBoolean(LOCK_KEY);
    }

    public static void setLocked(Player player, boolean locked) {
        player.getPersistentData().putBoolean(LOCK_KEY, locked);
    }

    // --- Combined ---

    public static void clearAllOffsets(Player player) {
        setPitchOffset(player, 0.0f);
        setYawOffset(player, 0.0f);
    }

    // --- Legacy compat ---

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
        setPitchOffset(player, 0.0f);
    }

    // --- Events ---

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        CompoundTag dest = event.getEntity().getPersistentData();
        CompoundTag orig = event.getOriginal().getPersistentData();
        if (orig.contains(LOCK_KEY)) {
            dest.putBoolean(LOCK_KEY, orig.getBoolean(LOCK_KEY));
        }
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = (Player) event.getEntity();
        player.getPersistentData().remove(LOCK_KEY);
    }
}
