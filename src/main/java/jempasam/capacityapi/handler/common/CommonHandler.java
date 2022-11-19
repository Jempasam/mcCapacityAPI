package jempasam.capacityapi.handler.common;

import jempasam.capacityapi.CapacityAPI;
import jempasam.capacityapi.block.CAPIBlocks;
import jempasam.capacityapi.item.CAPIItems;
import jempasam.capacityapi.network.CapacityNetwork;
import jempasam.capacityapi.recipe.CAPIRecipes;
import jempasam.capacityapi.register.CAPIRegistry;
import net.minecraftforge.common.MinecraftForge;

public class CommonHandler {
	
	public CommonHandler(CapacityAPI mod) {
		super();
		CAPIRegistry.preInit();
    	CapacityNetwork.init();
		CAPIItems.init();
		CAPIBlocks.init();
    	CAPIRecipes.registerRecipes();
    	System.out.println("LE CHIEN EST VERT");
    	
		MinecraftForge.EVENT_BUS.register(new RegisterEventHandler(mod));
		MinecraftForge.EVENT_BUS.register(new EntityCommonHandler(mod));
	}
}
