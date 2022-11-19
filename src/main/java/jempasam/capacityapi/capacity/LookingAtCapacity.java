package jempasam.capacityapi.capacity;

import jempasam.capacityapi.capacity.provider.EntityProvider;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import jempasam.mcsam.VectorUtils;
import net.minecraft.entity.Entity;

@Loadable
public class LookingAtCapacity implements Capacity{
	
	
	
	private Capacity capacity;
	@LoadableParameter private EntityProvider target=EntityProvider.MARKED;
	
	
	
	@LoadableParameter(paramnames = {"then"})
	public LookingAtCapacity(Capacity capacity) {
		this.capacity=capacity;
	}
	
	
	
	@Override
	public boolean use(CapacityContext context) {
		Entity etarget=target.getEntity(context);
		Entity etargeting=context.getTarget();
		context.setRotation(VectorUtils.getPitchYaw(etarget.getPositionVector().subtract(etargeting.getPositionVector())));
		sendParticleLine(context, etarget.getPositionVector(), etargeting.getPositionVector(), 5);
		return capacity.use(context);
	}
	
	@Override
	public String getName(CapacityContext context) {
		return capacity.getName(context);
	}
	
	@Override
	public int getColor(CapacityContext context) {
		return capacity.getColor(context);
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return capacity.getMana(context);
	}
}
