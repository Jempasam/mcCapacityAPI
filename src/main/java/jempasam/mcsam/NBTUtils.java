package jempasam.mcsam;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
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
}
