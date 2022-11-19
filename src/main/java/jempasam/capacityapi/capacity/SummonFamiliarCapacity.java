package jempasam.capacityapi.capacity;

import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Loadable
public class SummonFamiliarCapacity implements Capacity {
	
	
	private Class<? extends Entity> entitytype;
	
	
	public SummonFamiliarCapacity(Class<? extends Entity> entitytype) {
		super();
		this.entitytype = entitytype;
	}
	
	@LoadableParameter(paramnames = {"entity"})
	public SummonFamiliarCapacity(String id) {
		entitytype=ForgeRegistries.ENTITIES.getValue(new ResourceLocation(id)).getEntityClass();
	}
	
	@Override
	public boolean use(CapacityContext context) {
		try {
			for(int i=0; i<context.getPower(); i++) {
				WorldServer world=(WorldServer)context.getWorld();
				Entity entity=entitytype.getConstructor(World.class).newInstance(world);
				if(entity instanceof EntityTameable && context.getTarget() instanceof EntityPlayer) {
					((EntityTameable) entity).setOwnerId(context.getTarget().getUniqueID());
				}
				entity.setPosition(context.getPos().x+Math.random()*0.1-0.5,context.getPos().y,context.getPos().z+Math.random()*0.1-0.5);
				world.spawnEntity(entity);
			}
			sendParticleBomb(context, context.getPos(), 10);
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
		return 0xbd00ff;
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return 150*context.getPower();
	}
}
