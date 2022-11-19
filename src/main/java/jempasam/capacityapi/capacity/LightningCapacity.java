package jempasam.capacityapi.capacity;

import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.world.WorldServer;

@Loadable
public class LightningCapacity implements Capacity {
	
	
	
	@LoadableParameter
	public LightningCapacity() {
	}
	
	
	
	@Override
	public boolean use(CapacityContext context) {
		for(int i=0; i<context.getPower(); i++) {
			WorldServer world=(WorldServer)context.getWorld();
			world.addWeatherEffect(new EntityLightningBolt(world, context.getPos().x, context.getPos().y, context.getPos().z, false));
		}
		return true;
	}
	
	@Override
	public String getName(CapacityContext context) {
		return "Strike";
	}
	
	@Override
	public int getColor(CapacityContext context) {
		return 0xC1FFFF;
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return 50*context.getPower();
	}
}
