package jempasam.capacityapi.register;

import jempasam.objectmanager.HashObjectManager;
import jempasam.objectmanager.ObjectManager;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CAPIRegistry {
	
	
	
	public static ObjectManager<ItemEntry> ITEMS;
	public static ObjectManager<BlockEntry> BLOCKS;
	public static ObjectManager<CreativeTabs> TABS;
	
	
	
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
