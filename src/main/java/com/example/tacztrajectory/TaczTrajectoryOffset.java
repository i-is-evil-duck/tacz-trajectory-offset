package com.example.tacztrajectory;

import com.example.tacztrajectory.network.PacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TaczTrajectoryOffset.MOD_ID)
public class TaczTrajectoryOffset {
    public static final String MOD_ID = "tacztrajectory";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public TaczTrajectoryOffset() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new TrajectoryData());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PacketHandler.register();
            LOGGER.info("TACZ Trajectory Offset initialized!");
        });
    }
}
