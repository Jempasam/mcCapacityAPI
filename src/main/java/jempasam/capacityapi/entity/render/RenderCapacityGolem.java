package jempasam.capacityapi.entity.render;

import jempasam.capacityapi.CapacityAPI;
import jempasam.capacityapi.entity.EntityCapacityGolem;
import jempasam.capacityapi.entity.render.model.ModelCapacityGolem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCapacityGolem extends RenderLiving<EntityCapacityGolem>
{
    private static final ResourceLocation CAPACITY_GOLEM_TEXTURES = new ResourceLocation(CapacityAPI.MODID,"textures/entity/capacity_golem.png");

    public RenderCapacityGolem(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelCapacityGolem(), 0.5F);
    }

    protected ResourceLocation getEntityTexture(EntityCapacityGolem entity)
    {
        return CAPACITY_GOLEM_TEXTURES;
    }

    protected void applyRotations(EntityCapacityGolem entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
    {
        super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);

        if ((double)entityLiving.limbSwingAmount >= 0.01D)
        {
            float f = 13.0F;
            float f1 = entityLiving.limbSwing - entityLiving.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
            float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            GlStateManager.rotate(6.5F * f2, 0.0F, 0.0F, 1.0F);
        }
    }
}

