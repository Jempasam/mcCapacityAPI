package jempasam.capacityapi.capacity;

import java.util.function.DoubleUnaryOperator;
import jempasam.capacityapi.utils.ColorUtils;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.util.math.Vec2f;

@Loadable
public class DirectionCapacity implements Capacity{
	
	
	
	private Capacity then;
	@LoadableParameter public DoubleUnaryOperator pitch;
	@LoadableParameter public DoubleUnaryOperator yaw;
	
	
	
	@LoadableParameter(paramnames = {"then"})
	public DirectionCapacity(Capacity then) {
		this.then=then;
		pitch=DoubleUnaryOperator.identity();
		yaw=DoubleUnaryOperator.identity();
	}
	
	
	
	@Override
	public boolean use(CapacityContext context) {
		context.setRotation(new Vec2f(
				(float)pitch.applyAsDouble(context.getRotation().x),
				(float)yaw.applyAsDouble(context.getRotation().y)
				));
		return then.use(context);
	}

	@Override
	public int getColor(CapacityContext context) {
		return ColorUtils.saturation(then.getColor(context), (int)(pitch.applyAsDouble(0)+yaw.applyAsDouble(0))/20);
	}

	@Override
	public String getName(CapacityContext context) {
		return "Cyclo-"+then.getName(context);
	}

	@Override
	public int getMana(CapacityContext context) {
		int m=then.getMana(context);
		return m+m/4;
	}
	
}
