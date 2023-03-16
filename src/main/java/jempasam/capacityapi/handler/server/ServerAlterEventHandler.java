package jempasam.capacityapi.handler.server;

import com.google.common.base.Predicates;

import jempasam.capacityapi.capability.CAPICapabilities;
import jempasam.capacityapi.capability.ICapacityOwner;
import jempasam.capacityapi.capability.SimpleCapabilityProvider;
import jempasam.capacityapi.capability.SimpleCapacityOwner;
import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.network.CapacityNetwork;
import jempasam.capacityapi.utils.ColorUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ServerAlterEventHandler {
	
	
	
	private String MODID;
	
	
	
	public ServerAlterEventHandler(String MODID) {
		super();
		this.MODID = MODID;
	}
	
	
	
	@SubscribeEvent
    public void attachCapabilityEntity(AttachCapabilitiesEvent<Entity> event) {
    	if(event.getObject() instanceof EntityPlayer) {
    		event.addCapability(new ResourceLocation(MODID, "capacity"), new SimpleCapabilityProvider<>(CAPICapabilities.CAPACITY_OWNER, new SimpleCapacityOwner(), null));
    	}
    }
    
    @SubscribeEvent
    public void cloneCapability(PlayerEvent.Clone event) {
    	ICapacityOwner from=event.getOriginal().getCapability(CAPICapabilities.CAPACITY_OWNER, null);
    	ICapacityOwner to=event.getEntity().getCapability(CAPICapabilities.CAPACITY_OWNER, null);
    	CAPICapabilities.CAPACITY_OWNER.readNBT(to, null, CAPICapabilities.CAPACITY_OWNER.writeNBT(from, null));
    }
	
	@SubscribeEvent
    public void tickPlayer(PlayerTickEvent event) {
		if(event.side.isServer()) {
			long worldtime=event.player.world.getTotalWorldTime();
			if(worldtime%5==0) {
				ICapacityOwner owner=event.player.getCapability(CAPICapabilities.CAPACITY_OWNER, null);
				if(owner!=null && !owner.materialSelector().isEmpty()) {
					if(worldtime%20==0) {
						owner.addMana(owner.materialSelector().getSelected().getInfusion()/40);
						CapacityNetwork.sendMana((EntityPlayerMP)event.player, owner);
						if(worldtime%400==0) {
							int rgb[]=ColorUtils.asRGB(owner.materialSelector().getSelected().getColor());
							AxisAlignedBB box=new AxisAlignedBB(-0.5f, 0, -0.5f, 0.5f, 2f, 0.5f).offset(event.player.getPositionVector());
							Capacity.sendParticleZone(rgb[0]/255f, rgb[1]/255f, rgb[2]/255f, box, 1+owner.materialSelector().getSelected().getPower()/4);
						}
					}
					if(worldtime-owner.getLastActionTime()<80) {
						int rgb[]=ColorUtils.asRGB(owner.materialSelector().getSelected().getColor());
						AxisAlignedBB box=new AxisAlignedBB(-0.5f, 0, -0.5f, 0.5f, 2f, 0.5f).offset(event.player.getPositionVector());
						Capacity.sendParticleZone(rgb[0]/255f, rgb[1]/255f, rgb[2]/255f, box, 1+owner.materialSelector().getSelected().getPower());
					}
				}
			}
		}
    }
	
    @SubscribeEvent
    public void initialCapability(EntityJoinWorldEvent event) {
    	if(event.getEntity() instanceof EntityPlayerMP) {
    		ICapacityOwner owner=event.getEntity().getCapability(CAPICapabilities.CAPACITY_OWNER, null);
    		owner.setMana(0);
    		if(owner.capacities().size()==0) {
    			owner.resetCapacities(-1);
        		owner.generate(-1, 1);
        		for(EntityPlayerMP player : event.getWorld().getPlayers(EntityPlayerMP.class, Predicates.alwaysTrue())) {
        			TextComponentString message=new TextComponentString("New Player "+event.getEntity().getName()+": Alter \""+owner.getAlterName()+"\"");
        			message.setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE).setBold(true));
        			player.sendMessage(message);
        		}
    		}
    	}
    }
    
    
    
}
