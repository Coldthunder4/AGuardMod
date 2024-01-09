package net.coldthunder4.cellguard.entity.client.models;

import net.coldthunder4.cellguard.entity.custom.GuardEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.RandomSource;

import java.util.List;

public class CellGuardModel extends PlayerModel<GuardEntity> {


    public CellGuardModel(ModelPart part) {
        super(part, false);

    }

    public void setRotateAngle(ModelPart ModelRenderer, float x, float y, float z) {
        ModelRenderer.xRot = x;
        ModelRenderer.yRot = y;
        ModelRenderer.zRot = z;
    }

    @Override
    protected void setupAttackAnimation(GuardEntity cellGuard, float idk) {
        super.setupAttackAnimation(cellGuard, idk);
    }

    @Override
    public void setupAnim(GuardEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netbipedHeadYaw, float bipedHeadPitch) {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netbipedHeadYaw, bipedHeadPitch);


    }



}
