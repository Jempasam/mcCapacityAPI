package jempasam.capacityapi.capacity;

import jempasam.capacityapi.CapacityAPI;
import jempasam.capacityapi.effect.CapacityEffect;
import jempasam.capacityapi.register.CAPIRegistry;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.text.TextComponentTranslation;

@Loadable
public class CapacityPotionCapacity implements Capacity{
	
	
	
	private Capacity capacity;
	private int amplifier;
	private int duration;
	
	
	
	@LoadableParameter(paramnames = {"capacity","duration","amplifier"})
	public CapacityPotionCapacity(Capacity capacity, int duration, int amplifier) {
		this.capacity=CAPIRegistry.makeExistCapacity(capacity, "_potion");
		this.duration=duration;
		this.amplifier=amplifier;
	}
	
	
	
	@Override
	public boolean use(CapacityContext context) {
		if(context.getTarget()!=null && context.getTarget() instanceof EntityLivingBase) {
			CapacityAPI.EFFECT.applyCapacityEffect((EntityLivingBase)context.getTarget(), capacity, getDuration(context), getLevel(context));
			return true;
		}
		else return false;
	}
	
	public int getDuration(CapacityContext context) {
		return (int)(duration*(0.5f+context.getPower()/2f));
	}
	
	public int getLevel(CapacityContext context) {
		return (int)((amplifier+1)*(0.5f+context.getPower()/2f))-1;
	}
	
	@Override
	public int getColor(CapacityContext context) {
		return capacity.getColor(context);
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return capacity.getMana(context)*getDuration(context)/CapacityEffect.activationTickRatio(getLevel(context));
	}
	
	@Override
	public String getName(CapacityContext context) {
		StringBuilder sb=new StringBuilder();
		sb.append(new TextComponentTranslation(capacity.getName(context)).getFormattedText());
		sb.append(" Effect");
		for(int i=0; i<amplifier; i++)sb.append('+');
		return sb.toString();
	}
}
