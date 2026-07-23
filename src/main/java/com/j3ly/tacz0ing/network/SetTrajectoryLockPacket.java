package com.j3ly.tacz0ing.network;

import com.j3ly.tacz0ing.TrajectoryData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetTrajectoryLockPacket {
    private final boolean locked;

    public SetTrajectoryLockPacket(boolean locked) {
        this.locked = locked;
    }

    public static void encode(SetTrajectoryLockPacket msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.locked);
    }

    public static SetTrajectoryLockPacket decode(FriendlyByteBuf buf) {
        return new SetTrajectoryLockPacket(buf.readBoolean());
    }

    public static void handle(SetTrajectoryLockPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                TrajectoryData.setLocked(player, msg.locked);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
