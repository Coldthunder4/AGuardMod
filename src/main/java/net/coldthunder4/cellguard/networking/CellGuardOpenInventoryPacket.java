package net.coldthunder4.cellguard.networking;
import java.util.function.Supplier;

import net.coldthunder4.cellguard.CellGuardPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class CellGuardOpenInventoryPacket {
    private final int id;
    private final int size;
    private final int entityId;

    public CellGuardOpenInventoryPacket(int id, int size, int entityId) {
        this.id = id;
        this.size = size;
        this.entityId = entityId;
    }

    public static CellGuardOpenInventoryPacket decode(FriendlyByteBuf buf) {
        return new CellGuardOpenInventoryPacket(buf.readUnsignedByte(), buf.readVarInt(), buf.readInt());
    }

    public static void encode(CellGuardOpenInventoryPacket msg, FriendlyByteBuf buf) {
        buf.writeByte(msg.id);
        buf.writeVarInt(msg.size);
        buf.writeInt(msg.entityId);
    }

    public int getId() {
        return this.id;
    }

    public int getSize() {
        return this.size;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public static void handle(CellGuardOpenInventoryPacket msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            CellGuardPacketHandler.openGuardInventory(msg);
        });
        context.get().setPacketHandled(true);
    }
}
