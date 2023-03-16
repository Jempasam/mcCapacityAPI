package jempasam.mcsam;

import java.util.UUID;

import jempasam.data.chunk.DataChunk;
import jempasam.data.chunk.ObjectChunk;
import jempasam.data.chunk.SimpleObjectChunk;
import jempasam.data.chunk.value.StringChunk;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;

public class NBTUtils {
	private NBTUtils() {}
	
	public static void writeEntity(NBTTagCompound target, String name, Entity entity) {
		if(entity!=null)target.setUniqueId(name, entity.getUniqueID());
	}
	
	public static Entity readEntity(NBTTagCompound target, String name, World world) {
		UUID uuid=target.getUniqueId(name);
		return uuid==null ? null : world.getMinecraftServer().getEntityFromUuid(uuid);
	}
	
	public static void saveChunk(NBTTagCompound target, ObjectChunk chunk) {
		
		for(DataChunk child : chunk) {
			if(child instanceof ObjectChunk) {
				NBTTagCompound nobj=new NBTTagCompound();
				target.setTag(child.getName(), nobj);
				saveChunk(nobj, (ObjectChunk)child);
			}
			else if(child instanceof StringChunk){
				target.setString(child.getName(), ((StringChunk)child).getValue());
			}
		}
	}
	
	public static NBTTagCompound saveChunk(ObjectChunk chunk) {
		NBTTagCompound nbt=new NBTTagCompound();
		saveChunk(nbt, chunk);
		return nbt;
	}
	
	public static ObjectChunk loadChunk(NBTTagCompound nbt) {
		ObjectChunk ret=new SimpleObjectChunk("root");
		for(String name : nbt.getKeySet()) {
			NBTBase child=nbt.getTag(name);
			if(child instanceof NBTTagString) {
				ret.add(new StringChunk(name, ((NBTTagString)child).getString()));
			}
			else if(child instanceof NBTTagCompound) {
				ret.add(loadChunk((NBTTagCompound)child));
			}
		}
		return ret;
	}
}
