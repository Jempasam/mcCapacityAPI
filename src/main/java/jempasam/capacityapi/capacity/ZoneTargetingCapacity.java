package jempasam.capacityapi.capacity;

import java.util.Collections;
import java.util.List;

import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;

@Loadable
public class ZoneTargetingCapacity implements Capacity{
	
	
	
	@LoadableParameter public Capacity then;
	@LoadableParameter public int minx;
	@LoadableParameter public int maxx;
	@LoadableParameter public int miny;
	@LoadableParameter public int maxy;
	@LoadableParameter public int minz;
	@LoadableParameter public int maxz;
	@LoadableParameter public int maxnumber=Integer.MAX_VALUE;
	@LoadableParameter public boolean ignoreSender=false;
	@LoadableParameter public boolean ignoreTarget=false;
	@LoadableParameter
	public void range(int r) {
		minx=-r; maxx=r;
		miny=-r; maxy=r;
		minz=-r; maxz=r;
	}
	
	
	
	@LoadableParameter
	public ZoneTargetingCapacity() { }
	
	
	
	@Override
	public boolean use(CapacityContext context) {
		AxisAlignedBB range=new AxisAlignedBB(
				context.getPos().x+minx-context.getPower(), context.getPos().y+miny-context.getPower(), context.getPos().z+minz-context.getPower(),
				context.getPos().x+maxx+context.getPower(), context.getPos().y+maxy+context.getPower(), context.getPos().z+maxz+context.getPower()
				);
		sendParticleZone(context, range, 40);
		boolean suceed=false;
		int i=maxnumber;
		List<Entity> entities=context.getWorld().getEntitiesWithinAABB(Entity.class, range);
		Collections.shuffle(entities);;
		for( Entity e : entities){
			if((!ignoreSender || e!=context.getSender()) && (!ignoreTarget || e!=context.getTarget())) {
				CapacityContext c=context.clone();
				c.setTarget(e);
				c.setPos(e.getPositionVector());
				if(then.use(c))suceed=true;
				sendParticleLine(context,context.getPos(), c.getPos(), 10);
				i--;
				if(i<=0)break;
			}
		}
		return suceed;
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return then.getMana(context)*Math.min((maxx-minx+context.getPower()*4+maxy-miny)/2, maxnumber);
	}
	
	@Override
	public int getColor(CapacityContext context) {
		return then.getColor(context);
	}
	
	@Override
	public String getName(CapacityContext context) {
		return "Zone "+then.getName(context);
	}
}
