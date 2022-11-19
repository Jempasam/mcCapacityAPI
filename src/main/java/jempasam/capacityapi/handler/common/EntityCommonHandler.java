package jempasam.capacityapi.handler.common;

import jempasam.capacityapi.CapacityAPI;
import jempasam.capacityapi.entity.EntityCapacityBlaze;
import jempasam.capacityapi.entity.EntityCapacityGolem;
import jempasam.capacityapi.entity.EntityCapacityProjectile;
import jempasam.capacityapi.entity.EntityCapacityTrap;
import jempasam.capacityapi.entity.EntityCapacityTurret;
import jempasam.capacityapi.network.CapacityNetwork;
import jempasam.capacityapi.recipe.CAPIRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityCommonHandler {
	
	
	
	public CapacityAPI capacity;

	
	
	public EntityCommonHandler(CapacityAPI capacity) {
		super();
		this.capacity = capacity;
	}
	
	
	@SubscribeEvent
	public void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		EntityRegistry.registerModEntity(new ResourceLocation(capacity.MODID, "capacity_projectile"), EntityCapacityProjectile.class, "capacityprojectile", 0, capacity, 40, 1, true);
    	EntityRegistry.registerModEntity(new ResourceLocation(capacity.MODID, "capacity_trap"), EntityCapacityTrap.class, "capacitytrap", 1, capacity, 40, 1, false);
    	EntityRegistry.registerModEntity(new ResourceLocation(capacity.MODID, "capacity_turret"), EntityCapacityTurret.class, "capacityturret", 2, capacity, 40, 1, false);
    	EntityRegistry.registerModEntity(new ResourceLocation(capacity.MODID, "capacity_golem"), EntityCapacityGolem.class, "capacitygolem", 3, capacity, 60, 1, false);
    	EntityRegistry.registerModEntity(new ResourceLocation(capacity.MODID, "capacity_elemental"), EntityCapacityBlaze.class, "capacityelemental", 4, capacity, 60, 1, false);
	}
	
}
