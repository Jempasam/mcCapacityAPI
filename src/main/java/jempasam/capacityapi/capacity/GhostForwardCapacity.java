package jempasam.capacityapi.capacity;

import java.util.function.IntUnaryOperator;

import jempasam.capacityapi.utils.ColorUtils;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.util.math.Vec3d;

@Loadable
public class GhostForwardCapacity extends AbstractCapacity {
	
	
	
	public IntUnaryOperator distance;
	public Capacity then;
	
	
	
	@LoadableParameter(paramnames = {"then","distance"})
	public GhostForwardCapacity(Capacity capacity, IntUnaryOperator distance) {
		super();
		this.then = capacity;
		this.distance = distance;
	}
	
	
	
	@Override
	public boolean use(CapacityContext context) {
		int i=0;
		int max=getRange(context);
		Vec3d initial=context.getPos();
		Vec3d last=initial;
		do{
			last=context.getPos();
			context.setPos(context.getPos().add(Vec3d.fromPitchYawVector(context.getRotation()).scale(0.5)));
			i++;
		}while(i<max);
		sendParticleLine(context, initial, last, max/3);
		context.setPos(last);
		
		return then.use(context);
	}
	
	@Override
	public int getColor(CapacityContext context) {
		return ColorUtils.lightness(then.getColor(context), distance.applyAsInt(0)/4);
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return then.getMana(context)+getRange(context)/10;
	}
	
	@Override
	public String getName(CapacityContext context) {
		return distance.toString()+"m "+then.getName(context);
	}
	
	private int getRange(CapacityContext context) {
		return (int)(distance.applyAsInt(0)*(0.5f+getPower(context)/2f));
	}
	
}
