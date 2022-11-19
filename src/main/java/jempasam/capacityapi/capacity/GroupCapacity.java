package jempasam.capacityapi.capacity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jempasam.capacityapi.utils.ColorUtils;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;

@Loadable
public class GroupCapacity implements Capacity {

	private List<Capacity> capacities;
	
	@LoadableParameter
	public void then(Capacity capacity){
		capacities.add(capacity);
	}
	
	@LoadableParameter
	public GroupCapacity() {
		capacities=new ArrayList<>();
	}
	
	@Override
	public boolean use(CapacityContext context) {
		boolean ret=false;

		for(Capacity c : capacities) {
			if(c.use(context.clone()))ret=true;
		}
		sendParticleBomb(context,context.getPos(),10);
		
		return ret;
	}
	
	@Override
	public int getColor(CapacityContext context) {
		int color=0xaaaaaa;
		for(Capacity c : capacities) {
			color=ColorUtils.shiftto(color, c.getColor(context), 0.5f);
		}
		return color;
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return capacities.stream().collect(Collectors.summingInt(c->c.getMana(context)));
	}
	
	@Override
	public String getName(CapacityContext context) {
		return capacities.stream().map(e->e.getName(context)).collect(Collectors.joining(" and "));
	}
}
