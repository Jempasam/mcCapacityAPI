package jempasam.capacityapi.effect;

import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.capacity.CapacityContext;
import jempasam.capacityapi.material.MagicMaterial;
import jempasam.capacityapi.register.CAPIRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class CapacityEffect extends Potion{
	
	public void applyCapacityEffect(EntityLivingBase target, Capacity capacity, int duration, int amplifer) {
		target.addPotionEffect(new PotionEffect(this, duration, amplifer));
		target.getEntityData().setString("effect_capacity", CAPIRegistry.CAPACITIES.idOf(capacity));
	}
	public void applyCapacityEffect(EntityLivingBase target, Capacity capacity, MagicMaterial material) {
		applyCapacityEffect(target, capacity, material.getInfusion(), material.getPower()-1);
	}
	public void applyCapacityEffect(EntityLivingBase target, Capacity capacity, MagicMaterial material, float d, float a) {
		applyCapacityEffect(target, capacity, (int)(material.getInfusion()*d), (int)((material.getPower()-1)*a));
	}
	
	public static float cadency(float amplifier, MagicMaterial mat) {
		return 20f/(400/((int)((mat.getPower()-1)*amplifier)*2+1));
	}
	
	public static int duration(float duration, MagicMaterial mat) {
		return (int)(mat.getInfusion()*duration);
	}
	
	public static int activationTickRatio(int amplifier) {
		return 400/(2*amplifier+1);
	}

	public CapacityEffect() {
		super(false, 0xff0000);
		setRegistryName("capacity_potion");
		setIconIndex(0, 0);
	}
	
	@Override
	public String getName() {
		return "Capacity";
	}
	
	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {
		String name=entityLivingBaseIn.getEntityData().getString("effect_capacity");
		if(name!=null) {
			Capacity capacity=CAPIRegistry.CAPACITIES.get(name);
			if(capacity!=null) {
				capacity.use(CapacityContext.atSender(entityLivingBaseIn, amplifier+1));
			}
		}
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		int time=activationTickRatio(amplifier);
		if(time<=0)time=2;
		if(duration%time==0)return true;
		else return false;
	}
	
}
