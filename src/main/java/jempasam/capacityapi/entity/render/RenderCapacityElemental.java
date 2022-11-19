package jempasam.capacityapi.entity.render;

import jempasam.capacityapi.CapacityAPI;
import jempasam.capacityapi.entity.EntityCapacityBlaze;
import net.minecraft.client.model.ModelBlaze;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;


public class RenderCapacityElemental extends RenderLiving<EntityCapacityBlaze>
{
    private static final ResourceLocation ELEMENTAL_TEXTURES = new ResourceLocation(CapacityAPI.MODID,"textures/entity/capacity_blaze.png");

    public RenderCapacityElemental(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelBlaze(), 0.5F);
    }
    
    @Override
    public void doRender(EntityCapacityBlaze entity, double x, double y, double z, float entityYaw, float partialTicks) {
    	GlStateManager.color(entity.ccontainer.red, entity.ccontainer.green, entity.ccontainer.blue);
    	super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityCapacityBlaze entity)
    {
        return ELEMENTAL_TEXTURES;
    }
    
}

