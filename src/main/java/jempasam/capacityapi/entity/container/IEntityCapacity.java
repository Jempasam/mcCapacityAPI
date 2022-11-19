package jempasam.capacityapi.entity.container;

import jempasam.capacityapi.entity.render.IColorable;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public interface IEntityCapacity<T extends EContainerCapacity> extends IColorable{
	public T capacities();
	public Entity asEntity();
	
	@Override
	default Vec3d getColor() {
		return new Vec3d(capacities().red, capacities().green, capacities().blue);
	}
}
