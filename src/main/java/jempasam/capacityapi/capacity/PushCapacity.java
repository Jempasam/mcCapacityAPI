package jempasam.capacityapi.capacity;

import jempasam.capacityapi.network.CapacityNetwork;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;

@Loadable
public class PushCapacity implements Capacity {
	
	
	
	private float force;
	
	
	
	@LoadableParameter(paramnames = {"force"})
	public PushCapacity(float force) {
		super();
		this.force=force;
	}
	
	
	@Override
	public boolean use(CapacityContext context) {
		if(context.getTarget()!=null) {
			sendParticleBomb(context, context.getTarget().getPositionVector(), 10);
			float f = -MathHelper.sin(context.getRotation().y* 0.017453292F) * MathHelper.cos(context.getRotation().x* 0.017453292F)*force*context.getPower();
	        float f1 = -MathHelper.sin(context.getRotation().x* 0.017453292F)*force*context.getPower();
	        float f2 = MathHelper.cos(context.getRotation().y* 0.017453292F) * MathHelper.cos(context.getRotation().x* 0.017453292F)*force*context.getPower();
			context.getTarget().addVelocity(f,f1,f2);
			if(context.getTarget() instanceof EntityPlayerMP)CapacityNetwork.sendPositionAndMotion((EntityPlayerMP)context.getTarget());
			return true;
		}
		else return false;
	}
	
	@Override
	public int getColor(CapacityContext context) {
		return 0xB8BBD3;
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return (int)(force*50*context.getPower());
	}
	
	@Override
	public String getName(CapacityContext context) {
		return "Push";
	}
	
}
