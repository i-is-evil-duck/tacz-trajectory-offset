package com.j3ly.tacz0ing.client;

import com.j3ly.tacz0ing.ModConfig;
import com.j3ly.tacz0ing.TaczTrajectoryOffset;
import com.j3ly.tacz0ing.TrajectoryData;
import com.tacz.guns.api.item.IGun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TaczTrajectoryOffset.MOD_ID, value = Dist.CLIENT)
public class ClientHudRenderer {

    private static long lastAdjustTime = 0;
    private static final long DISPLAY_DURATION_MS = 1500;

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        if (!ModConfig.isHudVisible()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        ItemStack heldItem = mc.player.getMainHandItem();
        if (!(heldItem.getItem() instanceof IGun)) return;

        float pitch = TrajectoryData.getPitchOffset(mc.player);
        float yaw = TrajectoryData.getYawOffset(mc.player);
        boolean locked = TrajectoryData.isLocked(mc.player);
        long now = System.currentTimeMillis();

        if (pitch != 0.0f || yaw != 0.0f) {
            lastAdjustTime = now;
        }

        long elapsed = now - lastAdjustTime;
        if (elapsed > DISPLAY_DURATION_MS && pitch == 0.0f && yaw == 0.0f && !locked) return;

        float alpha;
        if (pitch != 0.0f || yaw != 0.0f || locked) {
            alpha = 1.0f;
        } else {
            float remaining = 1.0f - (float)(elapsed) / DISPLAY_DURATION_MS;
            alpha = Math.max(0.0f, remaining);
        }
        int alphaByte = (int)(alpha * 255) << 24;

        GuiGraphics graphics = event.getGuiGraphics();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        StringBuilder sb = new StringBuilder();

        // Pitch line
        if (pitch > 0) {
            sb.append(String.format("Pitch: +%.2f\u00b0 UP", pitch));
        } else if (pitch < 0) {
            sb.append(String.format("Pitch: %.2f\u00b0 DOWN", pitch));
        } else {
            sb.append("Pitch: 0.00\u00b0 (Flat)");
        }

        // Yaw line
        sb.append("  |  ");
        if (yaw > 0) {
            sb.append(String.format("Yaw: +%.2f\u00b0 RIGHT", yaw));
        } else if (yaw < 0) {
            sb.append(String.format("Yaw: %.2f\u00b0 LEFT", yaw));
        } else {
            sb.append("Yaw: 0.00\u00b0 (Center)");
        }

        // Lock indicator
        if (locked) {
            sb.append("  [LOCKED]");
        }

        String text = sb.toString();

        int color;
        if (locked) {
            color = 0xFFFF55;
        } else if (pitch == 0.0f && yaw == 0.0f) {
            color = 0xFFFFFF;
        } else {
            color = 0x55FF55;
        }

        int textWidth = mc.font.width(text);
        int x = screenWidth / 2 - textWidth / 2;
        int y = screenHeight - 65;

        int fullColor = alphaByte | (color & 0x00FFFFFF);
        int bgColor = alphaByte;

        graphics.fill(x - 3, y - 2, x + textWidth + 3, y + mc.font.lineHeight + 2, bgColor);
        graphics.drawString(mc.font, text, x, y, fullColor, false);
    }
}
