package jempasam.capacityapi.handler.client;

import jempasam.capacityapi.CapacityAPI;
import jempasam.capacityapi.entity.EntityCapacityBlaze;
import jempasam.capacityapi.entity.EntityCapacityGolem;
import jempasam.capacityapi.entity.EntityCapacityProjectile;
import jempasam.capacityapi.entity.EntityCapacityTrap;
import jempasam.capacityapi.entity.EntityCapacityTurret;
import jempasam.capacityapi.entity.render.RenderCapacityElemental;
import jempasam.capacityapi.entity.render.RenderCapacityGolem;
import jempasam.capacityapi.entity.render.RenderCapacityItem;
import jempasam.capacityapi.entity.render.RenderColorableBlock;
import jempasam.capacityapi.key.CAPIKeyBindings;
import jempasam.capacityapi.register.CAPIRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelHandler {
	
	
	
	private CapacityAPI mod;
	
	
	
	public ModelHandler(CapacityAPI mod) {
		super();
		this.mod = mod;
	}
	
	
	
	@SubscribeEvent
    public void registerEntityModel(ModelRegistryEvent event) {
    	RenderingRegistry.registerEntityRenderingHandler(EntityCapacityProjectile.class, manager->new RenderColorableBlock(manager));
    	
    	RenderingRegistry.registerEntityRenderingHandler(EntityCapacityTrap.class,
    			manager->new RenderCapacityItem<>(manager, Items.SNOWBALL, Minecraft.getMinecraft().getRenderItem()));
    	
    	RenderingRegistry.registerEntityRenderingHandler(EntityCapacityTurret.class, manager->new RenderColorableBlock(manager));
    	
    	RenderingRegistry.registerEntityRenderingHandler(EntityCapacityGolem.class, manager->new RenderCapacityGolem(manager));
    	
    	RenderingRegistry.registerEntityRenderingHandler(EntityCapacityBlaze.class, manager->new RenderCapacityElemental(manager));
    }
	
	@SubscribeEvent
	public void registerItemModels(final ModelRegistryEvent event) {
		System.out.println("START MODEL");
			CAPIRegistry.ITEMS.forEach(entry -> {
				System.out.println("MODEL:"+entry.getKey());
				Item item=entry.getValue().item;
				final ModelResourceLocation fullModelLocation = new ModelResourceLocation(new ResourceLocation(mod.MODID,entry.getKey()), "inventory");     
				ModelLoader.setCustomModelResourceLocation(item, 0, fullModelLocation);
	     });
	}
		
	@SubscribeEvent
	public void registerItemModels(final ColorHandlerEvent.Item event) {
		CAPIRegistry.ITEMS.forEach(entry -> {
			Item item=entry.getValue().item;
			//Colorable item
			if(item instanceof IItemColor)event.getItemColors().registerItemColorHandler((IItemColor)item, item);
		});
	}
	
}
