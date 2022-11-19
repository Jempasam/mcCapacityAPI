package jempasam.capacityapi.handler.client;

import jempasam.capacityapi.network.CapacityNetwork;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ParticleHandler {
	
	private static long tick=0;
	
	@SubscribeEvent
	public void tick(ClientTickEvent event) {
		if(System.currentTimeMillis()-tick>100 && CapacityNetwork.CAPACITY.lastMessage!=null && CapacityNetwork.CAPACITY.lastMessage.repeat>0) {
			CapacityNetwork.CAPACITY.onMessage(CapacityNetwork.CAPACITY.lastMessage, null);
			tick=System.currentTimeMillis();
		}
	}
}
