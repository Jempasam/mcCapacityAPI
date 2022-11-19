package jempasam.capacityapi.register.loader;

import java.util.List;

import jempasam.objectmanager.HashObjectManager;
import jempasam.objectmanager.groups.DoubleGroupObjectManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CAPIObjectManager<T> extends DoubleGroupObjectManager<T>{
	
	
	
	public CAPIObjectManager() {
		super(new HashObjectManager<>());
	}
	
	
	
	public List<T> ofWorld(World world, BlockPos pos){
		String biomename="spawn."+world.getBiome(pos).getBiomeName();
		List<T> capacities=ofCategories(biomename).toList();
		if(world.isDaytime())ofCategories(biomename+".day").forEach(c->capacities.add(c));
		else ofCategories(biomename+".night").forEach(c->capacities.add(c));
		if(world.isThundering())ofCategories(biomename+".thunder").forEach(c->capacities.add(c));
		else if(world.isRaining())ofCategories(biomename+".raining").forEach(c->capacities.add(c));
		else ofCategories(biomename+".clear").forEach(c->capacities.add(c));
		if(world.isDaytime())ofCategories("spawn.day").forEach(c->capacities.add(c));
		else  ofCategories("spawn.night").forEach(c->capacities.add(c));
		return capacities;
	}
}
