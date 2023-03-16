package jempasam.capacityapi.register;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import jempasam.capacityapi.block.factoryrecipe.IFactoryRecipe;
import jempasam.capacityapi.capability.ICapacityOwner;
import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.gui.GuiMana;
import jempasam.capacityapi.material.MagicMaterial;
import jempasam.capacityapi.register.loader.CAPILoading;
import jempasam.capacityapi.register.loader.CAPIObjectManager;
import jempasam.data.chunk.ObjectChunk;
import jempasam.data.deserializer.DataDeserializers;
import jempasam.mcsam.NBTUtils;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import scala.collection.generic.BitOperations.Int;

public class CAPIWorldData extends WorldSavedData{
	
	
	
	public ObjectChunk data;
	public CAPIObjectManager<Capacity> CAPACITIES;
	public CAPIObjectManager<MagicMaterial> MATERIALS;
	public CAPIObjectManager<IFactoryRecipe> FACTORY_RECIPES;
	public CAPILoading LOADER;
	public HashMap<String, Integer> COLORS;
	
	
	
	public CAPIWorldData(String name) {
		super(name);
		CAPACITIES=new CAPIObjectManager<>();
		MATERIALS=new CAPIObjectManager<>();
		FACTORY_RECIPES=new CAPIObjectManager<>();
		LOADER=new CAPILoading(CAPACITIES, MATERIALS, FACTORY_RECIPES, COLORS);
	}
	
	
	
	public void clear() {
		CAPACITIES.clear();
		MATERIALS.clear();
		FACTORY_RECIPES.clear();
	}
	
	public CAPILoading loader() {
		return LOADER;
	}
	
	public void reload() {
		Map<ICapacityOwner, NBTBase> backup=LOADER.getBackup();
		LOADER.loadAll();
		LOADER.loadBackup(backup);
	}
	
	private static int ccounter=0;
	public Capacity makeExistCapacity(Capacity capacity, String prefix) {
		if(CAPACITIES.idOf(capacity)==null) {
			CAPACITIES.register(prefix+ccounter, capacity);
			ccounter++;
		}
		return capacity;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		try {
			String content=nbt.getString("data");
			ObjectChunk data=DataDeserializers.loadFrom(new ByteArrayInputStream(content.getBytes()));
			ObjectChunk capacities=data.getObject("capacities");
			if(capacities!=null) LOADER.loadCapacities(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		return NBTUtils.saveChunk(data);
	}

}
