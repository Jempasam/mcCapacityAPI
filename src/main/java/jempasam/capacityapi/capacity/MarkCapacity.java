package jempasam.capacityapi.capacity;

import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;

@Loadable
public class MarkCapacity implements Capacity {
	
	
	
	private Capacity capacity;
	
	
	
	@LoadableParameter(paramnames = {"then"})
	public MarkCapacity(Capacity capacity){
		this.capacity=capacity;
	}
	
	
	
	@Override
	public boolean use(CapacityContext context) {
		context.mark(context.getTarget());
		sendParticleLine(context, context.getPos(), context.getPos().addVector(0, 4, 0), 4);
		capacity.use(context);
		return true;
	}
	
	@Override
	public int getColor(CapacityContext context) {
		return capacity.getColor(context);
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return capacity.getMana(context);
	}
	
	@Override
	public String getName(CapacityContext context) {
		return capacity.getName(context);
	}
}
