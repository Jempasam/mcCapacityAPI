package jempasam.capacityapi.capacity;

import java.util.Random;

import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Loadable
public class SummonProjectileCapacity implements Capacity {
	
	
	private Class<? extends Entity> entitytype;
	@LoadableParameter private float power=1;
	@LoadableParameter private float inacurracy=0;
	
	
	public SummonProjectileCapacity(Class<? extends Entity> entitytype) {
		super();
		this.entitytype = entitytype;
	}
	
	@LoadableParameter(paramnames = {"entity"})
	public SummonProjectileCapacity(String id) {
		entitytype=ForgeRegistries.ENTITIES.getValue(new ResourceLocation(id)).getEntityClass();
	}
	
	@Override
	public boolean use(CapacityContext context) {
		try {
			WorldServer world=(WorldServer)context.getWorld();
			Entity entity=entitytype.getConstructor(World.class).newInstance(world);
			if(entity instanceof IProjectile) {
				Vec3d rotation=Vec3d.fromPitchYawVector(context.getRotation());
				((IProjectile) entity).shoot(rotation.x, rotation.y, rotation.z, power*context.getPower(), inacurracy);
			}
			else if(entity instanceof EntityFireball) {
				Vec3d rotation=Vec3d.fromPitchYawVector(context.getRotation());
				EntityFireball fireball=(EntityFireball)entity;
				Random rand=new Random();
				fireball.accelerationX=(rotation.x + rand.nextGaussian() * 0.007499999832361937D * (double)inacurracy)*power;
				fireball.accelerationY=(rotation.y + rand.nextGaussian() * 0.007499999832361937D * (double)inacurracy)*power;
				fireball.accelerationZ=(rotation.z + rand.nextGaussian() * 0.007499999832361937D * (double)inacurracy)*power;
			}
			entity.setPosition(context.getPos().x,context.getPos().y,context.getPos().z);
			world.spawnEntity(entity);
			sendParticleBomb(context, context.getPos(), 3);
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	public String getName(CapacityContext context) {
		return new TextComponentTranslation("entity."+EntityRegistry.getEntry(entitytype).getName()+".name").getFormattedText()+" Summoning";
	}
	
	@Override
	public int getColor(CapacityContext context) {
		return 0xD2AB79;
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return (int)(75*context.getPower()*(1.0f+power*context.getPower()/4f)-inacurracy*10);
	}
}
