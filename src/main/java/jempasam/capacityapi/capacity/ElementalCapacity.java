package jempasam.capacityapi.capacity;

import jempasam.capacityapi.entity.EntityCapacityBlaze;
import jempasam.capacityapi.register.CAPIRegistry;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.WorldServer;

@Loadable
public class ElementalCapacity implements Capacity {
	
	
	
	private Capacity then;
	@LoadableParameter private int cadency=60;
	@LoadableParameter private float health=20;
	@LoadableParameter private float moveSpeed=20;
	
	
	
	@LoadableParameter(paramnames = {"then"})
	public ElementalCapacity(Capacity then) {
		super();
		this.then = CAPIRegistry.makeExistCapacity(then, "_elemental");
	}
	
	
	
	@Override
	public boolean use(CapacityContext context) {
		WorldServer world=(WorldServer)context.getWorld();
		EntityCapacityBlaze ret=new EntityCapacityBlaze(world);
		ret.setPosition(context.getPos().x,context.getPos().y,context.getPos().z);
		ret.rotationPitch=context.getRotation().x;
		ret.rotationYaw=context.getRotation().y;
		ret.ccontainer.fill(then, context.getSender(), context.getMarked(), context.getPower());
		ret.cadency=getCadency(context);
		ret.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getHealth(context));
		ret.setHealth(ret.getMaxHealth());
		ret.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(getSpeed(context));
		ret.wild=false;
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
		return then.getName(context)+" Elemental";
	}
	
	@Override
	public int getColor(CapacityContext context) {
		return then.getColor(context);
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return (int)((1f+getHealth(context)*60/getCadency(context)*getSpeed(context))*then.getMana(context));
	}
}
