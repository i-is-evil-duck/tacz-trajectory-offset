package com.example.tacztrajectory.client;

import com.example.tacztrajectory.TaczTrajectoryOffset;
import com.example.tacztrajectory.TrajectoryData;
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

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        // Only show when holding a TACZ gun
        ItemStack heldItem = mc.player.getMainHandItem();
        if (!(heldItem.getItem() instanceof IGun)) return;

        float offset = TrajectoryData.getOffset(mc.player);

        GuiGraphics graphics = event.getGuiGraphics();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        String text;
        int color;
        if (offset > 0) {
            text = String.format("↑ Trajectory: +%.0f° UP", offset);
            color = 0x55FF55; // Green for up
        } else if (offset < 0) {
            text = String.format("↓ Trajectory: %.0f° DOWN", offset);
            color = 0xFF5555; // Red for down
        } else {
            text = "→ Trajectory: 0° (Flat)";
            color = 0xFFFFFF; // White for neutral
        }

        int textWidth = mc.font.width(text);
        int x = screenWidth / 2 - textWidth / 2;
        int y = screenHeight - 65; // Above hotbar

        // Draw shadow background
        graphics.fill(x - 3, y - 2, x + textWidth + 3, y + mc.font.lineHeight + 2, 0xAA000000);
        // Draw text
        graphics.drawString(mc.font, text, x, y, color, false);

        // Draw hint
        String hint = "[↑/↓ Adjust  |  R Reset]";
        int hintWidth = mc.font.width(hint);
        int hintX = screenWidth / 2 - hintWidth / 2;
        int hintY = y - mc.font.lineHeight - 2;
        graphics.fill(hintX - 2, hintY - 1, hintX + hintWidth + 2, hintY + mc.font.lineHeight + 1, 0x66000000);
        graphics.drawString(mc.font, hint, hintX, hintY, 0xAAAAAA, false);
    }
}
