package jempasam.capacityapi.capacity;

import jempasam.capacityapi.entity.EntityCapacityTrap;
import jempasam.capacityapi.register.CAPIRegistry;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;

@Loadable
public class TrapCapacity implements Capacity{
	
	
	
	@LoadableParameter public float range=0.5f;
	@LoadableParameter public int life=1;
	@LoadableParameter public int loading=0;
	@LoadableParameter public boolean self=false;
	public Capacity capacity;
	
	
	
	@LoadableParameter(paramnames = {"then"})
	public TrapCapacity(Capacity capacity) {
		super();
		this.capacity=CAPIRegistry.makeExistCapacity(capacity, "_trap");;
	}
	
	@Override
	public boolean use(CapacityContext context) {
		EntityCapacityTrap projectile=new EntityCapacityTrap(context.getWorld());
		projectile.capacities().fill(capacity, context);
		projectile.setPosition(context.getPos().x, context.getPos().y, context.getPos().z);
		projectile.range=range*context.getPower();
		projectile.life=life*context.getPower();
		projectile.timeout=loading;
		projectile.self=self;
		context.getWorld().spawnEntity(projectile);
		sendParticleBomb(context, context.getPos(), 10);
		return true;
	}
	
	@Override
	public int getColor(CapacityContext context) {
		return capacity.getColor(context);
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return (int)(capacity.getMana(context)*life*Math.max(1.0f+range/5f-loading/200f,1.0f));
	}
	
	@Override
	public String getName(CapacityContext context) {
		return capacity.getName(context)+" Trap";
	}
}
