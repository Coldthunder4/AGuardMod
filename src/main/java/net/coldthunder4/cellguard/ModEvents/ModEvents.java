package net.coldthunder4.cellguard.ModEvents;

import net.coldthunder4.cellguard.CellGuard;
import net.coldthunder4.cellguard.entity.ModEntityTypes;
import net.coldthunder4.cellguard.entity.custom.GuardEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = CellGuard.MOD_ID, bus =Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
    /*
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(()-> {

        });
    }

     */

    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event){
        event.put(ModEntityTypes.GUARD.get(), GuardEntity.createAttributes().build());
    }
}
