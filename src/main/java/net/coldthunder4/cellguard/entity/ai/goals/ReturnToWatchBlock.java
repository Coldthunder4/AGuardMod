package net.coldthunder4.cellguard.entity.ai.goals;

import net.coldthunder4.cellguard.entity.custom.GuardEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class ReturnToWatchBlock extends Goal {

    private final GuardEntity cellGuard;
    private final double speed;
    private final double belowThirty = .3; //30%


    public ReturnToWatchBlock(GuardEntity cellGuard, double SpeedIn) {
        this.cellGuard = cellGuard;
        this.speed = SpeedIn;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public boolean canUse() {
        boolean entityExists = false;
        LivingEntity target = cellGuard.getTarget();
        try {
            if (target != null)
                entityExists = true;
        }
        catch (Exception e) {
            entityExists = false;

        }
        if (entityExists) {
            boolean closeEnoughX = target.getBlockX() > (cellGuard.getWatchBlock().getX() - 10) && target.getBlockX() < (cellGuard.getWatchBlock().getX() + 10);
            boolean closeEnoughY = target.getBlockY() > (cellGuard.getWatchBlock().getY() - 10) && target.getBlockY() < (cellGuard.getWatchBlock().getY() + 10);
            boolean closeEnoughZ = target.getBlockZ() > (cellGuard.getWatchBlock().getZ() - 10) && target.getBlockZ() < (cellGuard.getWatchBlock().getZ() + 10);


            boolean noTarget = (this.cellGuard.getTarget() == null); // Not Targetting anything = true
            double lowHealth = this.cellGuard.getMaxHealth() * belowThirty; //gets 30% of max health
            // checks current health and checks if it is below 30% of it's max health, also if it has no current target
            return this.cellGuard.getHealth() <= lowHealth || !closeEnoughX || !closeEnoughY || !closeEnoughZ;
        }
            else return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void tick() {
        BlockPos blockpos = this.cellGuard.getWatchBlock();
        if (blockpos != null) {
            Vec3 vector3d = Vec3.atBottomCenterOf(blockpos);
            if (!this.cellGuard.getWatchBlock().closerThan(this.cellGuard.blockPosition(), 0)) {
                this.cellGuard.getNavigation().moveTo(vector3d.x(), vector3d.y(), vector3d.z(), this.speed);
            }

        }
    }
}
