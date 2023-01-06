package net.coldthunder4.cellguard.entity;

import net.coldthunder4.cellguard.CellGuard;
import net.coldthunder4.cellguard.entity.custom.GuardEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CellGuard.MOD_ID);

    public static final RegistryObject<EntityType<GuardEntity>> GUARD = ENTITY_TYPES.register("guard", () -> EntityType.Builder.of(GuardEntity::new, MobCategory.CREATURE).sized(0.8f, 1.8f).build(new ResourceLocation(CellGuard.MOD_ID, "guard").toString()));

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
