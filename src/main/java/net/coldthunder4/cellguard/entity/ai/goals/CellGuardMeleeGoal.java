package net.coldthunder4.cellguard.entity.ai.goals;

import net.coldthunder4.cellguard.entity.custom.GuardEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.level.pathfinder.Path;


public class CellGuardMeleeGoal extends MeleeAttackGoal {
    public final GuardEntity cellGuard;

    private Path path;

    public CellGuardMeleeGoal(GuardEntity cellGuard, double SpeedIn, boolean useLongMemory){
        super(cellGuard, SpeedIn, useLongMemory);
        this.cellGuard = cellGuard;
    }

    public void tick() { try {
        LivingEntity target = cellGuard.getTarget();
        if (target != null || this.canUse()) {
            if (target.distanceTo(cellGuard) <= 32.0) {
                cellGuard.lookAt(target, 30.0F, 30.0F);
            }
            super.tick();
        }
    } catch (Exception e) {
        throw new RuntimeException(e);
    }

    }

    @Override
    protected double getAttackReachSqr(LivingEntity attackTarget){
        return super.getAttackReachSqr(attackTarget) * 3.75D;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
        double d0 = this.getAttackReachSqr(enemy);
        if (distToEnemySqr <= d0 && this.getTicksUntilNextAttack() <= 10) {
            this.resetAttackCooldown();
            cellGuard.swing(InteractionHand.MAIN_HAND);
            cellGuard.doHurtTarget(enemy);
        }
    }

    @Override
    public boolean canUse() {
        //double lowHealth = cellGuard.getMaxHealth() * .3;
        LivingEntity livingEntity = cellGuard.getTarget();
        if (livingEntity != null){
            boolean closeEnoughX = livingEntity.getBlockX() > (cellGuard.getWatchBlock().getX() - 10) && livingEntity.getBlockX() < (cellGuard.getWatchBlock().getX() + 10);
            boolean closeEnoughY = livingEntity.getBlockY() > (cellGuard.getWatchBlock().getY() - 10) && livingEntity.getBlockY() < (cellGuard.getWatchBlock().getY() + 10);
            boolean closeEnoughZ = livingEntity.getBlockZ() > (cellGuard.getWatchBlock().getZ() - 10) && livingEntity.getBlockZ() < (cellGuard.getWatchBlock().getZ() + 10);


            if (livingEntity != null /*&& cellGuard.getHealth() >= lowHealth*/ && closeEnoughX && closeEnoughY && closeEnoughZ){



                return true;}


        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }
}
