package jempasam.capacityapi.capacity;

import java.util.function.IntUnaryOperator;

import jempasam.capacityapi.utils.ColorUtils;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Loadable
public class ForwardCapacity extends AbstractCapacity {
	
	
	
	private Capacity then;
	private IntUnaryOperator maxdistance;
	
	
	
	@LoadableParameter(paramnames = {"then","maxdistance"})
	public ForwardCapacity(Capacity capacity, IntUnaryOperator maxdistance) {
		super();
		this.then = capacity;
		this.maxdistance = maxdistance;
	}
	
	@Override
	public boolean use(CapacityContext context) {
		int i=0;
		int max=getRange(context);
		Vec3d initial=context.getPos();
		Vec3d last=initial;
		BlockPos bpos=null;
		do{
			last=context.getPos();
			context.setPos(context.getPos().add(Vec3d.fromPitchYawVector(context.getRotation()).scale(0.5)));
			i++;
			bpos=new BlockPos(context.getPos());
		}while(context.getWorld().getBlockState(bpos).getBlock().isReplaceable(context.getWorld(), bpos)&&i<max);
		sendParticleLine(context, initial, last, max/2);
		context.setPos(last);
		return then.use(context);
	}
	
	@Override
	public int getColor(CapacityContext context) {
		return ColorUtils.lightness(then.getColor(context), maxdistance.applyAsInt(0)/4);
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return then.getMana(context)+getRange(context)/10;
	}
	
	@Override
	public String getName(CapacityContext context) {
		return maxdistance.toString()+"m "+then.getName(context);
	}
	
	private int getRange(CapacityContext context) {
		return (int)(maxdistance.applyAsInt(0)*(0.5f+getPower(context)/2f));
	}
	
}
