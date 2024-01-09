package net.coldthunder4.cellguard.networking;

import net.coldthunder4.cellguard.entity.ModEntityTypes;
import net.coldthunder4.cellguard.item.custom.GuardItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class SpawnGuardPacket {

    public SpawnGuardPacket() {



    }

    public SpawnGuardPacket(FriendlyByteBuf buf) {



    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //this is all on server side
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();

            ModEntityTypes.GUARD.get().spawn(level, null, null, player.blockPosition(), MobSpawnType.COMMAND, true, false);







        });
        return true;

    }

}
