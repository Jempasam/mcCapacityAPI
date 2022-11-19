package jempasam.capacityapi.entity.render;

import jempasam.capacityapi.entity.EntityCapacityProjectile;
import jempasam.capacityapi.entity.EntityCapacityTrap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RenderCapacityItem<T extends EntityCapacityTrap> extends Render<T> {
	
	protected final Item item;
    private final RenderItem itemRenderer;

    public RenderCapacityItem(RenderManager renderManagerIn, Item itemIn, RenderItem itemRendererIn)
    {
        super(renderManagerIn);
        this.item = itemIn;
        this.itemRenderer = itemRendererIn;
    }

    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        if(entity instanceof IColorable) {
        	Vec3d color=((IColorable)entity).getColor();
        	GlStateManager.color((float)color.x, (float)color.y, (float)color.z);
        }
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        if (this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }
       this.itemRenderer.renderItem(this.getStackToRender(entity), ItemCameraTransforms.TransformType.GROUND);

        if (this.renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    public ItemStack getStackToRender(T entityIn)
    {
    	if(entityIn.item==null) return new ItemStack(Items.AIR);
    	else return entityIn.item;
    }

    protected ResourceLocation getEntityTexture(T entity)
    {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}
