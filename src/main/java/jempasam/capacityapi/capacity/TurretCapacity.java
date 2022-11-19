package jempasam.capacityapi.capacity;

import jempasam.capacityapi.entity.EntityCapacityTurret;
import jempasam.capacityapi.register.CAPIRegistry;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;

@Loadable
public class TurretCapacity extends AbstractCapacity{
	
	
	
	@LoadableParameter public int reloadTime=20;
	@LoadableParameter public int repetition=1;
	@LoadableParameter public float yawRotation=0;
	private Capacity capacity;
	private Capacity capacity2=null;
	
	
	
	@LoadableParameter(paramnames = {"then"})
	public TurretCapacity(Capacity capacity) {
		super();
		this.capacity=CAPIRegistry.makeExistCapacity(capacity, "_turret");
	}
	
	
	
	@LoadableParameter(name = "thenFinal")
	void setCapacity2(Capacity capacity) {
		capacity2=CAPIRegistry.makeExistCapacity(capacity, "_turretfinal");
	}
	
	@Override
	public boolean use(CapacityContext context) {
		EntityCapacityTurret projectile=new EntityCapacityTurret(context.getWorld());
		projectile.ccontainer.fill(capacity, context);
		projectile.ccontainer.setCapacity2(capacity2);
		projectile.setPosition(context.getPos().x, context.getPos().y, context.getPos().z);
		projectile.rotationPitch=context.getRotation().x;
		projectile.rotationYaw=context.getRotation().y;
		projectile.yawRotation=yawRotation;
		projectile.repetition=getRepetion(context);
		projectile.reloadTime=getReloadTime(context);
		context.getWorld().spawnEntity(projectile);
		sendParticleBomb(context, context.getPos(), 10);
		return true;
	}
	
	public int getReloadTime(CapacityContext context) {
		return (int)(reloadTime/(getPower(context)/2f+0.5f));
	}
	
	public int getRepetion(CapacityContext context) {
		return (int)(repetition*(getPower(context)/2f+0.5f));
	}
	
	@Override
	public int getColor(CapacityContext context) {
		return capacity.getColor(context);
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return (int)(capacity.getMana(context)*getRepetion(context)*(1.0f+1f/getReloadTime(context)*10)) + (capacity2==null ? 0 : capacity2.getMana(context));
	}
	
	@Override
	public String getName(CapacityContext context) {
		return capacity.getName(context)+" Turret";
	}
}
