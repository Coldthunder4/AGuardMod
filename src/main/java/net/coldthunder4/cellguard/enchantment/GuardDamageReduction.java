package net.coldthunder4.cellguard.enchantment;

import net.coldthunder4.cellguard.entity.custom.GuardEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class GuardDamageReduction extends Enchantment {


    public GuardDamageReduction(Rarity p_44676_, EnchantmentCategory p_44677_, EquipmentSlot p_44678_) {
        super(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_LEGS, new EquipmentSlot[]{EquipmentSlot.LEGS});
    }

    @Override
    public void doPostHurt(LivingEntity pAttacker, Entity pTarget, int pLevel) {
        if(!pAttacker.level.isClientSide()) {
            ServerLevel world = ((ServerLevel)pAttacker.level);
            if (pLevel == 4) {
                if (pTarget instanceof GuardEntity){
                    ((LivingEntity) pTarget).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 1, true, true));
                }
            }
        }

        super.doPostAttack(pAttacker, pTarget, pLevel);
    }

    @Override
    public int getDamageProtection(int level, DamageSource source) {
        if (source.isBypassInvul()) return 0;

        if (source instanceof EntityDamageSource direct
                && direct.getEntity() instanceof GuardEntity) {
            return 6;
        }
        return 0;
    }

    @Override
    public int getMaxLevel() {
        return 4;
        //15% damage reduction per level from entity GuardEntity rather than add to the vanilla DR, needs to be coded in
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return super.checkCompatibility(other) && other != Enchantments.ALL_DAMAGE_PROTECTION;
    }

}
