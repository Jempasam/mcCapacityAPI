package jempasam.capacityapi.handler.common;

import jempasam.capacityapi.CapacityAPI;
import jempasam.capacityapi.effect.CapacityEffect;
import jempasam.capacityapi.register.CAPIRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.oredict.OreDictionary;

@Mod.EventBusSubscriber
public class RegisterEventHandler {
	
	
	public RegisterEventHandler(CapacityAPI mod) {
		super();
	}
	
	
	@SubscribeEvent
    public void registerEntity(RegistryEvent.Register<EntityEntry> event) {
    	/*event.getRegistry().registerAll
    	(
    			EntityEntryBuilder.create()
    				.entity(EntityCapacityProjectile.class)
    				.name("Capacity Projectile")
    				.id(modid+":capacity_projectile", 0)
    				.tracker(80, 3, true)
    				.build()
    	);*/
    }
	
	@SubscribeEvent
    public void registerItem(RegistryEvent.Register<Item> event) {
		System.out.println("LE CHIEN EST ROUGE");
    	CAPIRegistry.ITEMS.forEach((entry)->{
    		
    		Item item=entry.getValue().item;
    		String name=entry.getKey();
    		
    		item.setRegistryName(name);
    		item.setUnlocalizedName(name);
    		item.setCreativeTab(entry.getValue().tab);
    		
    		event.getRegistry().register(item);
    		
    		if(entry.getValue().oredict!=null)OreDictionary.registerOre(entry.getValue().oredict, item);
    	});
    	CAPIRegistry.BLOCKS.forEach((entry)->{
    		
    		Item item=new ItemBlock(entry.getValue().block);
    		String name=entry.getKey();
    		
    		item.setRegistryName(name);
    		item.setUnlocalizedName(name);
    		item.setCreativeTab(entry.getValue().tab);
    		
    		event.getRegistry().register(item);
    		
    		if(entry.getValue().oredict!=null)OreDictionary.registerOre(entry.getValue().oredict, item);
    	});
    }
	
	@SubscribeEvent
    public void registerBlock(RegistryEvent.Register<Block> event) {
    	CAPIRegistry.BLOCKS.forEach((entry)->{
    		
    		Block block=entry.getValue().block;
    		String name=entry.getKey();
    		
    		block.setRegistryName(name);
    		block.setUnlocalizedName(name);
    		
    		event.getRegistry().register(block);
    	});
    }
	
	@SubscribeEvent
    public static void registerRecipe(RegistryEvent.Register<IRecipe> event) {
    }
	
	@SubscribeEvent
    public static void registerPotion(RegistryEvent.Register<Potion> event) {
		CapacityAPI.EFFECT=new CapacityEffect();
		event.getRegistry().register(CapacityAPI.EFFECT);
    }
	
}
