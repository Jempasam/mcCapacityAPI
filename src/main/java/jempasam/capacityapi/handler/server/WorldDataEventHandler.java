package jempasam.capacityapi.handler.server;

import com.google.common.base.Predicates;

import jempasam.capacityapi.capability.CAPICapabilities;
import jempasam.capacityapi.capability.ICapacityOwner;
import jempasam.capacityapi.capability.SimpleCapabilityProvider;
import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.network.CapacityNetwork;
import jempasam.capacityapi.utils.ColorUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class WorldDataEventHandler{
	
	
	
	private String MODID;
	
	
	
	public WorldDataEventHandler(String MODID) {
		super();
		this.MODID = MODID;
	}
	
	
	
	@SubscribeEvent
	public void save(WorldEvent.Load event) {
	}
	
	
	
	private static class CapacityData extends WorldSavedData{
		
		public CapacityData(String name) { super(name); }
		
		@Override
		public void readFromNBT(NBTTagCompound nbt) {
			
		}
		
		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound compound) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
