package jempasam.capacityapi.handler.server;

import java.util.List;
import java.util.Random;

import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.entity.EntityCapacityBlaze;
import jempasam.capacityapi.register.CAPIRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MobSpawnHandler {
	private static Random random=new Random();
	@SubscribeEvent
	public void spawnMobs(CheckSpawn spawn) {
		if(spawn.getEntity().isCreatureType(EnumCreatureType.MONSTER, false) && random.nextFloat()>0.98f) {
			spawn.setResult(Result.DENY);
			List<Capacity> capacities=CAPIRegistry.CAPACITIES.ofWorld(spawn.getWorld(), new BlockPos(spawn.getX(), spawn.getY(), spawn.getZ()));
			if(capacities.size()>0) {
				Capacity capacity=capacities.get(random.nextInt(capacities.size()));
				EntityCapacityBlaze blaze=new EntityCapacityBlaze(spawn.getWorld());
				blaze.ccontainer.fill(capacity, blaze, blaze, 1);
				blaze.wild=true;
				blaze.cadency=30;
				blaze.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20f);
				blaze.setHealth(blaze.getMaxHealth());
				blaze.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.0f);
				blaze.setPosition(spawn.getX(), spawn.getY(), spawn.getZ());
				spawn.getWorld().spawnEntity(blaze);
			}
		}
	}
}
