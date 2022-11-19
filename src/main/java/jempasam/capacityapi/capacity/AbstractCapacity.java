package jempasam.capacityapi.capacity;

import jempasam.data.loader.tags.LoadableParameter;

public abstract class AbstractCapacity implements Capacity {
	
	
	
	@LoadableParameter
	protected boolean ignorePower=false;
	
	protected int getPower(CapacityContext context) {
		if(ignorePower)return 1;
		else return context.getPower();
	}
}
