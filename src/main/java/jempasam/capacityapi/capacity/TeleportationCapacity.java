package jempasam.capacityapi.capacity;

import jempasam.capacityapi.network.CapacityNetwork;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.entity.player.EntityPlayerMP;

@Loadable
public class TeleportationCapacity implements Capacity{
	
	
	
	@LoadableParameter
	public TeleportationCapacity() {
	}
	
	
	
	@Override
	public boolean use(CapacityContext context) {
		sendParticleBomb(context, context.getTarget().getPositionVector(), 10);
		sendParticleBomb(context, context.getPos(), 10);
		context.getTarget().setPosition(context.getPos().x, context.getPos().y, context.getPos().z);
		if(context.getTarget() instanceof EntityPlayerMP)CapacityNetwork.sendPositionAndMotion((EntityPlayerMP)context.getTarget());
		return true;
	}
	
	@Override
	public String getName(CapacityContext context) {
		return "Teleportation";
	}
	
	@Override
	public int getColor(CapacityContext context) {
		return 0x6F0073;
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return 60;
	}
}
