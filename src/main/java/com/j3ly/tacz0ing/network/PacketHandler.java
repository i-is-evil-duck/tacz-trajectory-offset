package com.j3ly.tacz0ing.network;

import com.j3ly.tacz0ing.TaczTrajectoryOffset;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(TaczTrajectoryOffset.MOD_ID, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void register() {
        INSTANCE.messageBuilder(SetTrajectoryOffsetPacket.class, packetId++, NetworkDirection.PLAY_TO_SERVER)
            .encoder(SetTrajectoryOffsetPacket::encode)
            .decoder(SetTrajectoryOffsetPacket::decode)
            .consumerMainThread(SetTrajectoryOffsetPacket::handle)
            .add();
    }
}
