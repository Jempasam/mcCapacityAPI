package jempasam.capacityapi.capacity;

import jempasam.capacityapi.capacity.provider.EntityProvider;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntityDamageSource;

@Loadable
public class LifeStealCapacity implements Capacity {
	
	
	
	private float damage;
	@LoadableParameter private EntityProvider stealed=EntityProvider.TARGET;
	@LoadableParameter private EntityProvider stealer=EntityProvider.MARKED;
	
	
	
	@LoadableParameter(paramnames = {"damage"})
	public LifeStealCapacity(float damage) {
		super();
		this.damage = damage;
	}
	
	
	
	@Override
	public boolean use(CapacityContext context) {
		Entity from=stealed.getEntity(context);
		Entity to=stealer.getEntity(context);
		if(from instanceof EntityLivingBase && to instanceof EntityLivingBase) {
			EntityLivingBase lfrom=(EntityLivingBase)from;
			EntityLivingBase lto=(EntityLivingBase)to;
			float life=lfrom.getHealth();
			from.attackEntityFrom(new EntityDamageSource("magic", context.getSender()), damage*context.getPower());
			life-=lfrom.getHealth();
			lto.heal(life);
			sendParticleLine(context, to.getPositionVector(), from.getPositionVector(), 5);
			return true;
		}
		else return false;
	}
	
	@Override
	public int getColor(CapacityContext context) {
		return 0xBB003A;
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return (int)(damage*30*context.getPower());
	}
	
	@Override
	public String getName(CapacityContext context) {
		int ip=(int)(damage/6);
		switch(ip) {
		case 0:
			return "Life Drain";
		default:
			return "Vampiring";
		}
	}
	
}
