package jempasam.capacityapi.capacity;

import jempasam.capacityapi.entity.EntityCapacityProjectile;
import jempasam.capacityapi.register.CAPIRegistry;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;

@Loadable
public class ProjectileCapacity implements Capacity{
	
	
	
	private float distance;
	private Capacity capacity=null;
	private Capacity capacity2=null;
	@LoadableParameter public int cadency=20;
	@LoadableParameter public int maxage=-1;
	
	
	
	@LoadableParameter(paramnames = {"then","distance"})
	public ProjectileCapacity(Capacity capacity, float distance) {
		super();
		this.distance=distance;
		this.capacity=CAPIRegistry.makeExistCapacity(capacity, "_projectile");
	}
	
	
	
	@LoadableParameter(name = "repeat")
	public void setRepeatCapacity(Capacity finalCapacity) {
		CAPIRegistry.makeExistCapacity(finalCapacity, "_finalproj");
		capacity2=finalCapacity;
	}
	
	@Override
	public boolean use(CapacityContext context) {
		EntityCapacityProjectile projectile=new EntityCapacityProjectile(context.getWorld());
		projectile.capacities().fill(capacity, context);
		projectile.capacities().setCapacity2(capacity2);
		projectile.cadency=getCadency(context);
		projectile.maxage=getMaxAge(context);
		projectile.setPosition(context.getPos().x, context.getPos().y, context.getPos().z);
		projectile.shoot(context.getTarget(), context.getRotation().x, context.getRotation().y, 0.0f, getDistance(context), 0.0f);
		projectile.shootingEntity=context.getTarget();
		context.getWorld().spawnEntity(projectile);
		return true;
	}
	
	private int getMaxAge(CapacityContext context) {
		return maxage==-1 ? -1 : maxage/2+maxage*context.getPower()/2;
	}
	
	private int getCadency(CapacityContext context) {
		return cadency-cadency*context.getPower()/10;
	}
	
	private float getDistance(CapacityContext context) {
		return distance/2+distance*context.getPower()/2;
	}
	
	@Override
	public int getColor(CapacityContext context) {
		long color=0;
		int count=0;
		if(capacity!=null) {
			color+=capacity.getColor(context);
			count++;
		}
		else if(capacity2!=null) {
			color+=capacity2.getColor(context);
			count++;
		}
		return (int)(color/count);
	}
	
	@Override
	public int getMana(CapacityContext context) {
		int ret=0;
		int maxage=getMaxAge(context);
		float distance= maxage==-1 ? getDistance(context)*60 : Math.min(getDistance(context)*60, maxage);
		if(capacity2!=null)ret+=capacity2.getMana(context)*distance/cadency;
		if(capacity!=null)ret+=capacity.getMana(context)*(1.0f+distance/200f);
		return ret;
	}
	
	@Override
	public String getName(CapacityContext context) {
		StringBuilder sb=new StringBuilder();
		sb.append("Throw");
		if(capacity!=null)sb.append(" ").append(capacity.getName(context));
		if(capacity2!=null)sb.append(" Then ").append(capacity.getName(context));
		return sb.toString();
	}
}
