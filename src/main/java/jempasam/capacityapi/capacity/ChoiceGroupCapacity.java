package jempasam.capacityapi.capacity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;

@Loadable
public class ChoiceGroupCapacity implements Capacity{
	
	
	
	private List<Capacity> capacities;
	
	
	
	@LoadableParameter public void then(Capacity capacity){ capacities.add(capacity); }
	
	
	
	@LoadableParameter
	public ChoiceGroupCapacity() {
		capacities=new ArrayList<>();
	}
	
	
	
	private static Random rand=new Random();
	@Override
	public boolean use(CapacityContext context) {
		int choice=rand.nextInt(capacities.size());
		Capacity choiced=capacities.get(choice);
		choiced.sendParticleBomb(context,context.getPos(),10);
		return choiced.use(context);
	}
	
	@Override
	public int getColor(CapacityContext context) {
		long color=0x000000;
		for(Capacity c : capacities) {
			color+=c.getColor(context);
		}
		return (int)(color/capacities.size());
	}
	
	@Override
	public int getMana(CapacityContext context) {
		int mana=0;
		for(Capacity c : capacities) {
			mana+=c.getColor(context);
		}
		return mana/capacities.size();
	}
	
	@Override
	public String getName(CapacityContext context) {
		return capacities.stream().map(e->e.getName(context)).collect(Collectors.joining(" or "));
	}
}
