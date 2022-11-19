package jempasam.capacityapi.block;

import jempasam.capacityapi.register.CAPIRegistry;
import jempasam.capacityapi.register.CAPIRegistry.BlockEntry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;

public class CAPIBlocks {
	private CAPIBlocks() {}
	
	
	
	public static Block FACTORY;
	
	
	public static void init() {
		FACTORY=CAPIRegistry.BLOCKS.register("factory", BlockEntry.create(new BlockMaterialFactory()).tab(CreativeTabs.REDSTONE)).block;
	}
}
