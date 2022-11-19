package jempasam.capacityapi.capacity;

import jempasam.capacityapi.capacity.provider.EntityProvider;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;

@Loadable
public class PotionCapacity implements Capacity{
	
	
	
	@LoadableParameter public Potion effect;
	@LoadableParameter public int amplifier;
	@LoadableParameter public int duration;
	@LoadableParameter private EntityProvider target=EntityProvider.TARGET;
	
	
	
	@LoadableParameter
	public PotionCapacity() { }
	public PotionCapacity(Potion effect, int amplifier, int duration) {
		super();
		this.effect = effect;
		this.amplifier = amplifier;
		this.duration = duration;
	}
	
	@Override
	public boolean use(CapacityContext context) {
		Entity entityTarget=target.getEntity(context);
		if(entityTarget!=null && entityTarget instanceof EntityLivingBase) {
			((EntityLivingBase)entityTarget).addPotionEffect(new PotionEffect(effect, getDuration(context), getLevel(context)));
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
		return effect.getLiquidColor();
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return (getLevel(context)+1)*(30+getDuration(context)/2);
	}
	
	@Override
	public String getName(CapacityContext context) {
		StringBuilder sb=new StringBuilder();
		sb.append(new TextComponentTranslation(effect.getName()).getFormattedText());
		for(int i=0; i<amplifier; i++)sb.append('+');
		return sb.toString();
	}
}
