package jempasam.capacityapi.capacity;

import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;

@Loadable
public class DisplayCapacity implements Capacity{
	
	
	
	private Capacity then;
	@LoadableParameter
	private String name=null;
	private int color=-1;
	@LoadableParameter public void color(String txt) { color=Integer.parseInt(txt, 16); }	
	
	
	
	@LoadableParameter(paramnames = {"then"})
	public DisplayCapacity(Capacity then) {
		super();
		this.then = then;
	}
	
	public DisplayCapacity(Capacity then, int color, String name) {
		this(then);
		this.color=color;
		this.name=name;
	}
	
	public static DisplayCapacity bake(Capacity tobake) {
		CapacityContext context=CapacityContext.withPower(1);
		return new DisplayCapacity(tobake, tobake.getColor(context), tobake.getName(context));
	}
	
	
	
	@Override
	public int getColor(CapacityContext context) {
		return color==-1 ? then.getColor(context) : color;
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return then.getMana(context);
	}
	
	@Override
	public String getName(CapacityContext context) {
		return name==null ? then.getName(context) : name;
	}
	
	@Override
	public boolean use(CapacityContext context) {
		return then.use(context);
	}
	
	
	
}
