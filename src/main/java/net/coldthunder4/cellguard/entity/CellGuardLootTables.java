package net.coldthunder4.cellguard.entity;

import java.util.function.Consumer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.coldthunder4.cellguard.CellGuard;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
public class CellGuardLootTables {
    public static final BiMap<ResourceLocation, LootContextParamSet> REGISTRY = HashBiMap.create();
    public static final LootContextParamSet SLOT = register("slot", (p_216252_0_) -> {
        p_216252_0_.required(LootContextParams.THIS_ENTITY);
    });

    public static final ResourceLocation CELLGUARD_MAIN_HAND = new ResourceLocation(CellGuard.MOD_ID, "entities/cellguard_main_hand");
    public static final ResourceLocation CELLGUARD_HELMET = new ResourceLocation(CellGuard.MOD_ID, "entities/cellguard_helmet");
    public static final ResourceLocation CELLGUARD_CHEST = new ResourceLocation(CellGuard.MOD_ID, "entities/cellguard_chestplate");
    public static final ResourceLocation CELLGUARD_LEGGINGS = new ResourceLocation(CellGuard.MOD_ID, "entities/cellguard_legs");
    public static final ResourceLocation CELLGUARD_FEET = new ResourceLocation(CellGuard.MOD_ID, "entities/cellguard_feet");

    public static LootContextParamSet register(String p_81429_, Consumer<LootContextParamSet.Builder> p_81430_) {
        LootContextParamSet.Builder lootcontextparamset$builder = new LootContextParamSet.Builder();
        p_81430_.accept(lootcontextparamset$builder);
        LootContextParamSet lootcontextparamset = lootcontextparamset$builder.build();
        ResourceLocation resourcelocation = new ResourceLocation(CellGuard.MOD_ID + p_81429_);
        LootContextParamSet lootcontextparamset1 = REGISTRY.put(resourcelocation, lootcontextparamset);
        if (lootcontextparamset1 != null) {
            throw new IllegalStateException("Loot table parameter set " + resourcelocation + " is already registered");
        } else {
            return lootcontextparamset;
        }
    }
}
