package jempasam.capacityapi.capacity;

import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.entity.EntityLivingBase;

public class MinimumLifeConditionCapacity implements Capacity{
	
	
	
	private float percentage;
	private Capacity capacity;
	
	
	
	@LoadableParameter(paramnames = {"min","then"})
	public MinimumLifeConditionCapacity(float percentage, Capacity capacity) {
		super();
		this.percentage = percentage;
		this.capacity = capacity;
	}
	
	@Override
	public int getColor(CapacityContext context) {
		return capacity.getColor(context);
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return (int)(capacity.getMana(context)*((1-percentage)*0.8f+0.2f));
	}
	
	@Override
	public String getName(CapacityContext context) {
		return "Vital "+capacity.getName(context);
	}
	
	@Override
	public boolean use(CapacityContext context) {
		if(context.getTarget() instanceof EntityLivingBase) {
			EntityLivingBase base=(EntityLivingBase)context.getTarget();
			if(base.getHealth()>=base.getMaxHealth()*percentage)return capacity.use(context);
			else return false;
		}
		else return false;
	}
	
	
	
	
	
	
}
