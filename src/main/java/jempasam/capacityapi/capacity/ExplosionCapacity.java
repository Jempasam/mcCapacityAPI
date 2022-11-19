package jempasam.capacityapi.capacity;

import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;

@Loadable
public class ExplosionCapacity implements Capacity {
	
	@LoadableParameter
	public float power;
	
	@LoadableParameter
	public boolean fire=false;
	
	@LoadableParameter
	public ExplosionCapacity() { }
	public ExplosionCapacity(float power) {
		super();
		this.power = power;
	}
	
	@Override
	public boolean use(CapacityContext context) {
		context.getWorld().newExplosion(null, context.getPos().x, context.getPos().y, context.getPos().z, getExplosionPower(context), fire, true);
		return true;
	}
	
	@Override
	public int getColor(CapacityContext context) {
		return 0xfd4100;
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return (int)(getExplosionPower(context)*getExplosionPower(context)*50);
	}
	
	@Override
	public String getName(CapacityContext context) {
		int ip=(int)power;
		StringBuilder sb=new StringBuilder();
		if(fire)sb.append("Fire");
		switch(ip) {
		case 0:
			sb.append("Bim");
			break;
		case 1:
			sb.append("Bam");
			break;
		case 2:
			sb.append("Boom");
			break;
		default:
			sb.append('B');
			for(int i=0; i<ip; i++) sb.append('o');
			sb.append('m');
		}
		return sb.toString();
	}
	
	private float getExplosionPower(CapacityContext context) {
		float p=(0.5f+context.getPower()/2f);
		return power*p;
	}
}
