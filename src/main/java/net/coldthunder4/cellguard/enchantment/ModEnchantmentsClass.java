package net.coldthunder4.cellguard.enchantment;

import net.coldthunder4.cellguard.CellGuard;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantmentsClass {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, CellGuard.MOD_ID);

    public static final RegistryObject<Enchantment> GUARD_DAMAGE_REDUCTION =
            ENCHANTMENTS.register("guard_damage_reduction", () -> new GuardDamageReduction(Enchantment.Rarity.RARE,
                    EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.CHEST));

    public static void register(IEventBus eventBus){
        ENCHANTMENTS.register(eventBus);
    }
}

