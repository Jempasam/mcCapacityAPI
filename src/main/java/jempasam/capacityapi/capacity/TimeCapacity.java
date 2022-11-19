package jempasam.capacityapi.capacity;

import java.util.function.LongUnaryOperator;

import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import jempasam.mcsam.OperatorUtils;

@Loadable
public class TimeCapacity implements Capacity {
	
	
	
	private LongUnaryOperator time;
	
	
	
	@LoadableParameter(paramnames = {"time"})
	public TimeCapacity(LongUnaryOperator time) {
		super();
		this.time = time;
	}
	
	
	
	@Override
	public boolean use(CapacityContext context) {
		long worldtime=context.getWorld().getWorldTime();
		int maxoffset=context.getPower()*1000;
		context.getWorld().setWorldTime(worldtime+OperatorUtils.getOffset(time, worldtime, -maxoffset, maxoffset));
		return true;
	}
	
	@Override
	public int getColor(CapacityContext context) {
		return 0xFFA400;
	}
	
	@Override
	public int getMana(CapacityContext context) {
		int maxoffset=context.getPower()*1000;
		return (int) Math.max(OperatorUtils.getOffset(time, 12000, -maxoffset, maxoffset), OperatorUtils.getOffset(time, 0, -maxoffset, maxoffset));
	}
	
	@Override
	public String getName(CapacityContext context) {
		return (time.applyAsLong(0)/1000)+"h Time Shift";
	}
}
