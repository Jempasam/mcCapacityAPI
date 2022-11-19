package jempasam.capacityapi.entity.render.model;

import jempasam.capacityapi.entity.EntityCapacityGolem;
import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class ModelCapacityGolem extends ModelIronGolem {
	
	
	
	protected ModelRenderer armMagicLeft;
	protected ModelRenderer armMagicRight;
	protected ModelRenderer legMagicLeft;
	protected ModelRenderer legMagicRight;
	protected ModelRenderer magicShield;
	protected ModelRenderer magicTank;
	
	public ModelCapacityGolem() {
		super();
		this.armMagicRight = (new ModelRenderer(this)).setTextureSize(128, 128);
        this.armMagicRight.setRotationPoint(0.0F, -7.0F, 0.0F);
        this.armMagicRight.setTextureOffset(60, 41).addBox(-13.0F, 17.5F, -3.0F, 4, 10, 6, 0.5f);
        
        this.armMagicLeft = (new ModelRenderer(this)).setTextureSize(128, 128);
        this.armMagicLeft.setRotationPoint(0.0F, -7.0F, 0.0F);
        this.armMagicLeft.setTextureOffset(60, 78).addBox(9.0F, 17.5F, -3.0F, 4, 10, 6, 0.5f);
        
        this.legMagicLeft = (new ModelRenderer(this, 0, 22)).setTextureSize(128, 128);
        this.legMagicLeft.setRotationPoint(-4.0F, 11.0F, 0.0F);
        this.legMagicLeft.setTextureOffset(37, 1).addBox(-3.5F, 1F, -3.0F, 6, 8, 5, 0.5f);
        
        this.legMagicRight = (new ModelRenderer(this, 0, 22)).setTextureSize(128, 128);
        this.legMagicRight.mirror = true;
        this.legMagicRight.setTextureOffset(60, 1).setRotationPoint(5.0F, 11.0F, 0.5F);
        this.legMagicRight.addBox(-3.5F, 1.0F, -3.5F, 6, 8, 5, 0.5f);
        
        this.magicShield = (new ModelRenderer(this)).setTextureSize(128, 128);
        this.magicShield.setRotationPoint(0.0F, -7.0F, 0.0F);
        this.magicShield.setTextureOffset(0, 40).addBox(-9.0F, -2.0F, -10.0F, 18, 12, 11, -2.0f);
        
        this.magicTank = (new ModelRenderer(this)).setTextureSize(128, 128);
        this.magicTank.setRotationPoint(0.0F, -8.5f, 9.5f);
        this.magicTank.setTextureOffset(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18, 12, 11, -1.5f);
	}
	
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		EntityCapacityGolem golem=(EntityCapacityGolem)entityIn;
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		
		GlStateManager.color(golem.red, golem.green, golem.blue);
        this.ironGolemHead.render(scale);
        this.ironGolemBody.render(scale);
        this.ironGolemLeftLeg.render(scale);
        this.ironGolemRightLeg.render(scale);
        this.ironGolemRightArm.render(scale);
        this.ironGolemLeftArm.render(scale);
        
        if(golem.has1)GlStateManager.color(golem.ccontainer.red, golem.ccontainer.green, golem.ccontainer.blue);
        else GlStateManager.color(golem.red, golem.green, golem.blue);
        this.armMagicLeft.render(scale);
        this.armMagicRight.render(scale);
        
        if(golem.has2)GlStateManager.color(golem.ccontainer.red2, golem.ccontainer.green2, golem.ccontainer.blue2);
        else GlStateManager.color(golem.red, golem.green, golem.blue);
        this.legMagicLeft.render(scale);
        this.legMagicRight.render(scale);
        
        if(golem.has3)GlStateManager.color(golem.ccontainer.red3, golem.ccontainer.green3, golem.ccontainer.blue3);
        else GlStateManager.color(golem.red, golem.green, golem.blue);
        this.magicShield.render(scale);
        
        if(golem.has4)GlStateManager.color(golem.ccontainer.red4, golem.ccontainer.green4, golem.ccontainer.blue4);
        else GlStateManager.color(golem.red, golem.green, golem.blue);
        this.magicTank.render(scale);
        
        GlStateManager.color(1.0f, 1.0f, 1.0f);
	}
	
	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
		super.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);
		armMagicLeft.rotateAngleX=ironGolemLeftArm.rotateAngleX;
		armMagicLeft.rotateAngleY=ironGolemLeftArm.rotateAngleY;
		armMagicLeft.rotateAngleZ=ironGolemLeftArm.rotateAngleZ;
		armMagicRight.rotateAngleX=ironGolemRightArm.rotateAngleX;
		armMagicRight.rotateAngleY=ironGolemRightArm.rotateAngleY;
		armMagicRight.rotateAngleZ=ironGolemRightArm.rotateAngleZ;
		
		legMagicLeft.rotateAngleX=ironGolemLeftLeg.rotateAngleX;
		legMagicLeft.rotateAngleY=ironGolemLeftLeg.rotateAngleY;
		legMagicLeft.rotateAngleZ=ironGolemLeftLeg.rotateAngleZ;
		legMagicRight.rotateAngleX=ironGolemRightLeg.rotateAngleX;
		legMagicRight.rotateAngleY=ironGolemRightLeg.rotateAngleY;
		legMagicRight.rotateAngleZ=ironGolemRightLeg.rotateAngleZ;
	}
}
