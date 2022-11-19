package jempasam.capacityapi.capacity;

import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;

@Loadable
public class NotTargetCapacity implements Capacity{
	
	@LoadableParameter
	public Capacity then;
	
	@LoadableParameter
	public NotTargetCapacity() { }
	
	@Override
	public int getColor(CapacityContext context) {
		return then.getColor(context);
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return then.getMana(context);
	}
	
	@Override
	public String getName(CapacityContext context) {
		return "Safe "+then.getName(context);
	}
	
	@Override
	public boolean use(CapacityContext context) {
		if(context.getTarget()!=context.getSender())return use(context);
		else return false;
	}
}
