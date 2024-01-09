package net.coldthunder4.cellguard.entity.ai.goals;

import net.coldthunder4.cellguard.entity.custom.GuardEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CellGuardSetWatchBlockPosPacket {
    private final int entityId;

    public CellGuardSetWatchBlockPosPacket(int entityId) {
        this.entityId = entityId;
    }

    public static CellGuardSetWatchBlockPosPacket decode(FriendlyByteBuf buf) {
        return new CellGuardSetWatchBlockPosPacket(buf.readInt());
    }

    public static void encode(CellGuardSetWatchBlockPosPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
    }

    public int getEntityId() {
        return this.entityId;
    }

    public static void handle(CellGuardSetWatchBlockPosPacket msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (msg != null) {
                context.get().enqueueWork(() -> {
                    ServerPlayer player = ((NetworkEvent.Context) context.get()).getSender();
                    if (player != null && player.level instanceof ServerLevel) {
                        Entity entity = player.level.getEntity(msg.getEntityId());
                        if (entity instanceof GuardEntity) {
                            GuardEntity cellGuard = (GuardEntity) entity;
                            BlockPos pos = cellGuard.blockPosition();
                            if (cellGuard.blockPosition() != null)
                                cellGuard.setWatchBlock(pos);
                        }
                    }
                });
            }
        });
        context.get().setPacketHandled(true);
    }

}
