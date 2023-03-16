package jempasam.capacityapi.handler.server;

import jempasam.capacityapi.capability.ICapacityOwner;
import jempasam.capacityapi.capability.SimpleCapacityOwner;
import jempasam.capacityapi.register.CAPIRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.world.WorldEvent;

public class ServerHandler {
	
	
	
	private String MODID;
	
	
	
	public ServerHandler(String mODID) {
		super();
		CapabilityManager.INSTANCE.register(ICapacityOwner.class, ICapacityOwner.STORAGE, SimpleCapacityOwner::new);
		MinecraftForge.EVENT_BUS.register(new ServerAlterEventHandler(MODID));
		MinecraftForge.EVENT_BUS.register(new MobSpawnHandler());
		MinecraftForge.EVENT_BUS.register(new WorldDataEventHandler(MODID));
    }

}
