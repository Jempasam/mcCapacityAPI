package jempasam.capacityapi.capacity;

import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;

@Loadable
public class MultiCapacity implements Capacity {

	@LoadableParameter
	public Capacity then;
	@LoadableParameter
	public int repeat;
	
	@LoadableParameter
	public MultiCapacity() { }
	public MultiCapacity(Capacity capacity, int repeat) {
		super();
		this.then = capacity;
		this.repeat = repeat;
	}
	
	
	@Override
	public boolean use(CapacityContext context) {
		boolean ret=false;
		
		int repeat=getRepeat(context);
		for(int i=0; i<repeat; i++) {
			if(then.use(context.clone()))ret=true;
		}
		sendParticleBomb(context,context.getPos(),10);
		return ret;
	}
	
	@Override
	public int getColor(CapacityContext context) {
		return then.getColor(context);
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return then.getMana(context)*getRepeat(context);
	}
	
	private int getRepeat(CapacityContext context) {
		return (int)(repeat*(0.5+context.getPower()*0.5));
	}
	
	@Override
	public String getName(CapacityContext context) {
		return "Multi "+then.getName(context);
	}
}
