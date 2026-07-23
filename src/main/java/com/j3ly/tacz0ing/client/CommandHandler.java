package com.j3ly.tacz0ing.client;

import com.j3ly.tacz0ing.ModConfig;
import com.j3ly.tacz0ing.TaczTrajectoryOffset;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TaczTrajectoryOffset.MOD_ID, value = Dist.CLIENT)
public class CommandHandler {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("tacz0ing")
                .then(Commands.literal("toggle")
                    .executes(ctx -> {
                        ModConfig.toggleHud();
                        boolean visible = ModConfig.isHudVisible();
                        ctx.getSource().sendSuccess(
                            () -> Component.literal("TACZ HUD display " + (visible ? "enabled" : "disabled"))
                                .withStyle(visible ? ChatFormatting.GREEN : ChatFormatting.RED),
                            false
                        );
                        return 1;
                    })
                )
        );
    }
}
