package jempasam.capacityapi.capacity;

import java.util.function.DoubleUnaryOperator;

import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

@Loadable
public class PositionCapacity implements Capacity{
	
	
	
	@LoadableParameter public Capacity then;
	@LoadableParameter private DoubleUnaryOperator x;
	@LoadableParameter private DoubleUnaryOperator y;
	@LoadableParameter private DoubleUnaryOperator z;
	
	
	
	@LoadableParameter(paramnames = {"then"})
	public PositionCapacity(Capacity then) {
		super();
		this.then = then;
		x=DoubleUnaryOperator.identity();
		y=DoubleUnaryOperator.identity();
		z=DoubleUnaryOperator.identity();
	}
	
	
	
	@Override
	public boolean use(CapacityContext context) {
		Vec3d old=context.getPos();
		if(!context.getWorld().isRemote || context.getTarget() instanceof EntityPlayer)context.setPos(new Vec3d(
			x.applyAsDouble(context.getPos().x),
			y.applyAsDouble(context.getPos().y),
			z.applyAsDouble(context.getPos().z)
		));
		sendParticleLine(context, old, context.getPos(), 5);
		return then.use(context);
	}

	@Override
	public int getColor(CapacityContext context) {
		return then.getColor(context);
	}

	@Override
	public String getName(CapacityContext context) {
		return "XYZ "+then.getName(context);
	}

	@Override
	public int getMana(CapacityContext context) {
		return then.getMana(context)+2;
	}
	
}
