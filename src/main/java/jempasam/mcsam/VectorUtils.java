package jempasam.mcsam;

import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class VectorUtils {
	private VectorUtils() {}
	
	public static final double DEG_ON_RAD=180./Math.PI;
	
	public static Vec2f getPitchYaw(Vec3d offset) {
		double max=Math.sqrt(offset.x*offset.x+offset.z*offset.z);
		double max2=Math.sqrt(offset.y*offset.y+max*max);
		return new Vec2f((float)(-Math.asin(offset.y/max2)*DEG_ON_RAD), -(float)(Math.acos(offset.z/max)*DEG_ON_RAD*Math.signum(offset.x)));
	}
}
