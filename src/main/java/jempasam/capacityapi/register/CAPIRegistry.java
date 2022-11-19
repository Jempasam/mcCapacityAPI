package jempasam.capacityapi.register;

import java.util.HashMap;
import java.util.Map;

import jempasam.capacityapi.block.factoryrecipe.IFactoryRecipe;
import jempasam.capacityapi.capability.ICapacityOwner;
import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.gui.GuiMana;
import jempasam.capacityapi.material.MagicMaterial;
import jempasam.capacityapi.register.loader.CAPILoading;
import jempasam.capacityapi.register.loader.CAPIObjectManager;
import jempasam.objectmanager.HashObjectManager;
import jempasam.objectmanager.ObjectManager;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTBase;

public class CAPIRegistry {
	
	
	
	public static ObjectManager<ItemEntry> ITEMS;
	public static ObjectManager<BlockEntry> BLOCKS;
	public static ObjectManager<CreativeTabs> TABS;
	public static CAPIObjectManager<Capacity> CAPACITIES;
	public static CAPIObjectManager<MagicMaterial> MATERIALS;
	public static CAPIObjectManager<IFactoryRecipe> FACTORY_RECIPES;
	public static HashMap<String, Integer> COLORS;
	
	public static CAPILoading LOADING;
	
	
	
	public static void loadAll() {
		CAPACITIES=new CAPIObjectManager<>();
		MATERIALS=new CAPIObjectManager<>();
		FACTORY_RECIPES=new CAPIObjectManager<>();
		COLORS=new HashMap<>();
		LOADING=new CAPILoading(CAPACITIES, MATERIALS, FACTORY_RECIPES, COLORS);
		LOADING.loadCapacities();
		LOADING.loadMaterials();
		LOADING.loadRecipes();
		GuiMana.setCapacities(new Capacity[]{CAPIRegistry.CAPACITIES.get("fire.gun"),CAPIRegistry.CAPACITIES.get("damage.gun"),CAPIRegistry.CAPACITIES.get("earth.dirt.pose")});
	}
	
	public static void reload() {
		Map<ICapacityOwner, NBTBase> backup=LOADING.getBackup();
		loadAll();
		LOADING.loadBackup(backup);
	}
	
	private static int ccounter=0;
	public static Capacity makeExistCapacity(Capacity capacity, String prefix) {
		if(CAPACITIES.idOf(capacity)==null) {
			CAPACITIES.register(prefix+ccounter, capacity);
			ccounter++;
		}
		return capacity;
	}
	
	
	
	public static void preInit() {
		TABS=new HashObjectManager<>();
		ITEMS=new HashObjectManager<>();
		BLOCKS=new HashObjectManager<>();
	}
	
	
	
	public static class ItemEntry{
		public Item item=null;
		public String oredict=null;
		public CreativeTabs tab=null;
		
		protected ItemEntry(Item item) {
			this.item=item;
		}
		
		public static ItemEntry create(Item item) { return new ItemEntry(item); }
		public ItemEntry oredict(String oredict) { this.oredict=oredict; return this; }
		public ItemEntry tab(CreativeTabs tab) { this.tab=tab; return this; }
		
	}
	
	
	
	public static class BlockEntry{
		public Block block=null;
		public String oredict=null;
		public CreativeTabs tab=null;
		
		protected BlockEntry(Block block) {
			this.block=block;
		}
		
		public static BlockEntry create(Block block) { return new BlockEntry(block); }
		public BlockEntry oredict(String oredict) { this.oredict=oredict; return this; }
		public BlockEntry tab(CreativeTabs tab) { this.tab=tab; return this; }
		
	}
	
	
	
}
