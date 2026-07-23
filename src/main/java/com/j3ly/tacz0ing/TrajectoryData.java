package com.j3ly.tacz0ing;

import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.attachment.AttachmentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TrajectoryData {
    private static final String PITCH_PREFIX = "tacz0ing_pitch_";
    private static final String YAW_PREFIX = "tacz0ing_yaw_";
    private static final String LOCK_KEY = "tacz0ing_trajectory_locked";

    public static final float MAX_OFFSET = 5.0f;
    public static final float MIN_OFFSET = -5.0f;
    public static final float STEP = 0.01f;

    /**
     * Get the ResourceLocation ID of the scope attached to the player's held gun.
     * Returns null if no gun or no scope.
     */
    public static ResourceLocation getScopeId(Player player) {
        ItemStack gunStack = player.getMainHandItem();
        IGun igun = IGun.getIGunOrNull(gunStack);
        if (igun == null) return null;
        ResourceLocation scopeId = igun.getAttachmentId(gunStack, AttachmentType.SCOPE);
        if (scopeId == null) return null;
        ResourceLocation emptyId = new ResourceLocation("tacz", "empty");
        if (scopeId.equals(emptyId)) return null;
        return scopeId;
    }

    // --- Pitch (vertical) - stored per-scope on player persistent data ---

    public static float getPitchOffset(Player player) {
        ResourceLocation scopeId = getScopeId(player);
        if (scopeId == null) return 0.0f;
        CompoundTag data = player.getPersistentData();
        String key = PITCH_PREFIX + scopeId;
        return data.contains(key) ? data.getFloat(key) : 0.0f;
    }

    public static void setPitchOffset(Player player, float offset) {
        ResourceLocation scopeId = getScopeId(player);
        if (scopeId == null) return;
        offset = Math.max(MIN_OFFSET, Math.min(MAX_OFFSET, offset));
        player.getPersistentData().putFloat(PITCH_PREFIX + scopeId, offset);
    }

    public static void adjustPitchOffset(Player player, float delta) {
        setPitchOffset(player, getPitchOffset(player) + delta);
    }

    // --- Yaw (horizontal) - stored per-scope on player persistent data ---

    public static float getYawOffset(Player player) {
        ResourceLocation scopeId = getScopeId(player);
        if (scopeId == null) return 0.0f;
        CompoundTag data = player.getPersistentData();
        String key = YAW_PREFIX + scopeId;
        return data.contains(key) ? data.getFloat(key) : 0.0f;
    }

    public static void setYawOffset(Player player, float offset) {
        ResourceLocation scopeId = getScopeId(player);
        if (scopeId == null) return;
        offset = Math.max(MIN_OFFSET, Math.min(MAX_OFFSET, offset));
        player.getPersistentData().putFloat(YAW_PREFIX + scopeId, offset);
    }

    public static void adjustYawOffset(Player player, float delta) {
        setYawOffset(player, getYawOffset(player) + delta);
    }

    // --- Lock state - on player persistent data ---

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

    // --- Legacy compat (delegates to pitch) ---

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
        CompoundTag orig = event.getOriginal().getPersistentData();
        CompoundTag dest = event.getEntity().getPersistentData();
        for (String key : orig.getAllKeys()) {
            if (key.startsWith(PITCH_PREFIX) || key.startsWith(YAW_PREFIX) || key.equals(LOCK_KEY)) {
                dest.put(key, orig.get(key));
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = (Player) event.getEntity();
        CompoundTag data = player.getPersistentData();
        data.getAllKeys().removeIf(key ->
            key.startsWith(PITCH_PREFIX) || key.startsWith(YAW_PREFIX) || key.equals(LOCK_KEY)
        );
    }
}
