package net.coldthunder4.cellguard.entity;

import net.coldthunder4.cellguard.CellGuard;
import net.coldthunder4.cellguard.entity.custom.GuardEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CellGuard.MOD_ID);

    public static final RegistryObject<EntityType<GuardEntity>> GUARD = ENTITY_TYPES.register("guard", () -> EntityType.Builder.of(GuardEntity::new, MobCategory.CREATURE).sized(0.8f, 1.8f).build(new ResourceLocation(CellGuard.MOD_ID, "guard").toString()));

    @javax.annotation.Nullable
    public GuardEntity spawn(ServerLevel serverLevel, @javax.annotation.Nullable ItemStack itemStack, @javax.annotation.Nullable Player player, Player player1, BlockPos pos, MobSpawnType mobSpawnType, boolean b, boolean b1) {
        return this.spawn(serverLevel, itemStack == null ? null : ItemStack.of(itemStack.getTag()), itemStack != null && itemStack.hasCustomHoverName() ? (Player) itemStack.getHoverName() : null, player, pos, mobSpawnType, b, b1);
    }

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
