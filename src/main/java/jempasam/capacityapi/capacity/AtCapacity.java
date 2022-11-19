package jempasam.capacityapi.capacity;

import jempasam.capacityapi.capacity.provider.EntityProvider;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;

@Loadable
public class AtCapacity implements Capacity{
	
	
	
	private Capacity capacity;
	@LoadableParameter private EntityProvider target=EntityProvider.TARGET;
	
	
	
	@LoadableParameter(paramnames = {"then"})
	public AtCapacity(Capacity capacity) {
		this.capacity=capacity;
	}
	
	
	
	@Override
	public boolean use(CapacityContext context) {
		context.setPos(target.getEntity(context).getPositionVector());
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
