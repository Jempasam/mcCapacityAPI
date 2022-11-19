package jempasam.capacityapi.capacity;

import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EntityDamageSource;

@Loadable
public class DamageCapacity implements Capacity {
	
	
	
	private float damage;
	
	
	
	@LoadableParameter(paramnames = {"damage"})
	public DamageCapacity(float damage) {
		super();
		this.damage = damage;
	}
	
	
	
	@Override
	public boolean use(CapacityContext context) {
		if(context.getTarget() instanceof EntityLiving) {
			((EntityLiving)context.getTarget()).attackEntityFrom(new EntityDamageSource("magic", context.getSender()), damage*context.getPower());
			sendParticleBomb(context, context.getTarget().getPositionVector(), 30);
			return true;
		}
		else return false;
	}
	
	@Override
	public int getColor(CapacityContext context) {
		return 0xFF005B;
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return (int)(damage*10*context.getPower());
	}
	
	@Override
	public String getName(CapacityContext context) {
		int ip=(int)(damage/2);
		switch(ip) {
		case 0:
			return "Ouch";
		case 1:
			return "Damage";
		case 2:
			return "Violent Damage";
		default:
			StringBuilder sb=new StringBuilder();
			sb.append("De");
			for(int i=0; i<ip; i++) sb.append('a');
			sb.append("th");
			return sb.toString();
		}
	}
}
