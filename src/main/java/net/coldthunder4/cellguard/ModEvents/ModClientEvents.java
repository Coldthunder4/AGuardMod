package net.coldthunder4.cellguard.ModEvents;


import net.coldthunder4.cellguard.CellGuard;
import net.coldthunder4.cellguard.entity.ModEntityTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.coldthunder4.cellguard.entity.client.GuardRenderer;

@Mod.EventBusSubscriber(modid = CellGuard.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {



    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(ModEntityTypes.GUARD.get(), GuardRenderer::new);
    }

}
