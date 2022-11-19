package jempasam.capacityapi.capacity;

import jempasam.capacityapi.entity.EntityCapacityGolem;
import jempasam.capacityapi.register.CAPIRegistry;
import jempasam.capacityapi.utils.ColorUtils;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.WorldServer;

@Loadable
public class GolemCapacity implements Capacity {
	
	
	
	@LoadableParameter private Capacity attack=null;
	@LoadableParameter private Capacity movement=null;
	@LoadableParameter private Capacity shield=null;
	@LoadableParameter private Capacity generator=null;
	@LoadableParameter private int cadency=60;
	@LoadableParameter private float health=20;
	@LoadableParameter private float moveSpeed=20;
	
	
	
	@LoadableParameter
	public GolemCapacity() {
		super();
	}
	
	
	
	@LoadableParameter(name = "attack")
	public void setAttack(Capacity capacity) {
		attack=CAPIRegistry.makeExistCapacity(capacity, "_attack");
	}
	
	@LoadableParameter(name = "movement")
	public void setMovement(Capacity capacity) {
		movement=CAPIRegistry.makeExistCapacity(capacity, "_movement");
	}
	
	@LoadableParameter(name = "shield")
	public void setShield(Capacity capacity) {
		shield=CAPIRegistry.makeExistCapacity(capacity, "_shield");
	}
	
	@LoadableParameter(name = "generator")
	public void setGenerator(Capacity capacity) {
		generator=CAPIRegistry.makeExistCapacity(capacity, "_generator");
	}
	
	@Override
	public boolean use(CapacityContext context) {
		WorldServer world=(WorldServer)context.getWorld();
		EntityCapacityGolem ret=new EntityCapacityGolem(world);
		ret.setPosition(context.getPos().x,context.getPos().y,context.getPos().z);
		ret.rotationPitch=context.getRotation().x;
		ret.rotationYaw=context.getRotation().y;
		ret.ccontainer.fill(attack, context.getSender(), context.getMarked(), context.getPower());
		ret.ccontainer.setCapacity2(movement);
		ret.ccontainer.setCapacity3(shield);
		ret.ccontainer.setCapacity4(generator);
		ret.cadency=getCadency(context);
		ret.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getHealth(context));
		ret.setHealth(ret.getMaxHealth());
		ret.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(getSpeed(context));
		ret.wild=false;
		int[] color=ColorUtils.asRGB(getColor(context));
		ret.red=color[0]/255f;
		ret.green=color[1]/255f;
		ret.blue=color[2]/255f;
		world.spawnEntity(ret);
		sendParticleBomb(context, context.getPos(), 10);
		return true;
	}
	
	public int getCadency(CapacityContext context) {
		return (int)(cadency/(0.5f+context.getPower()*0.5f));
	}
	
	public double getHealth(CapacityContext context) {
		return health*context.getPower();
	}
	
	public double getSpeed(CapacityContext context) {
		return moveSpeed*(0.5f+context.getPower()*0.5f);
	}
	
	@Override
	public String getName(CapacityContext context) {
		StringBuilder sb=new StringBuilder();
		if(attack!=null)sb.append(attack.getName(context)).append(" Punching ");
		if(movement!=null)sb.append(attack.getName(context)).append(" Walking ");
		if(shield!=null)sb.append(attack.getName(context)).append(" Shieldied ");
		if(generator!=null)sb.append(attack.getName(context)).append(" Generating ");
		if(moveSpeed<0.5)sb.append("Slow ");
		else if(moveSpeed>2.0)sb.append("Fast ");
		if(health<8.0)sb.append("Weak ");
		else if(health>40.0)sb.append("Tank ");
		if(cadency<10)sb.append("Gatling ");
		sb.append("Golem");
		return sb.toString();
	}
	
	@Override
	public int getColor(CapacityContext context) {
		long tcolor=0;
		int t=0;
		if(attack!=null) {
			tcolor+=attack.getColor(context);
			t++;
		}
		if(shield!=null) {
			tcolor+=shield.getColor(context);
			t++;
		}
		if(generator!=null) {
			tcolor+=generator.getColor(context);
			t++;
		}
		if(movement!=null) {
			tcolor+=movement.getColor(context);
			t++;
		}
		return (int)(tcolor/t);
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return (int)((1f+getHealth(context)*70/getCadency(context)*getSpeed(context))*totalMana(context));
	}
	
	private int totalMana(CapacityContext context) {
		return (attack==null ? 0 : attack.getMana(context)) + (shield==null ? 0 : shield.getMana(context)) + (generator==null ? 0 : generator.getMana(context)) + (movement==null ? 0 : movement.getMana(context));
	}
}
