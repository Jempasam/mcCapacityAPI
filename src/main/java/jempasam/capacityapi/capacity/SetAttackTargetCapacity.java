package jempasam.capacityapi.capacity;

import jempasam.capacityapi.capacity.provider.EntityProvider;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

@Loadable
public class SetAttackTargetCapacity implements Capacity {
	
	
	
	@LoadableParameter private EntityProvider target=EntityProvider.TARGET;
	@LoadableParameter private EntityProvider attacker=EntityProvider.MARKED;
	
	
	
	@LoadableParameter
	public SetAttackTargetCapacity() {
		super();
	}
	
	
	
	@Override
	public boolean use(CapacityContext context) {
		Entity etarget=target.getEntity(context);
		Entity eattacker=attacker.getEntity(context);
		if(etarget instanceof EntityLivingBase && eattacker instanceof EntityLivingBase) {
			((EntityLivingBase)eattacker).setRevengeTarget((EntityLivingBase)etarget);
			return true;
		}
		else return false;
	}
	
	@Override
	public int getColor(CapacityContext context) {
		return 0x342D46;
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return 200;
	}
	
	@Override
	public String getName(CapacityContext context) {
		return "Anger";
	}
	
}
