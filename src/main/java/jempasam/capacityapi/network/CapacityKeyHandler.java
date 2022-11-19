package jempasam.capacityapi.network;

import io.netty.buffer.ByteBuf;
import jempasam.capacityapi.capability.CAPICapabilities;
import jempasam.capacityapi.capability.ICapacityOwner;
import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.capacity.CapacityContext;
import jempasam.capacityapi.utils.ColorUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CapacityKeyHandler implements IMessageHandler<CapacityKeyHandler.CapacityKeyMessage, IMessage>{
	
	public IMessage onMessage(CapacityKeyMessage message, MessageContext ctx) {
		if(message.entity instanceof EntityPlayer) {
			ICapacityOwner owner=message.entity.getCapability(CAPICapabilities.CAPACITY_OWNER, null);
			if(owner!=null) {
				if(message.code==0&&!owner.capacitySelector().isEmpty()) {
					CapacityContext context=CapacityContext.atSender(message.entity, owner.getPower());
					Capacity capacity;
					if(message.info==-1)capacity=owner.capacitySelector().getSelected();
					else capacity=owner.capacitySelector().get(message.info%owner.capacitySelector().count());
					int mana=capacity.getMana(context);
					if(owner.getMana()>mana) {
						if(capacity.use(context)) {
							owner.tellActionMade(message.entity.world);
							owner.addMana(-mana);
							TextComponentString text=new TextComponentString("Used "+Integer.toString(mana)+" Mana");
							text.setStyle(new Style().setColor(TextFormatting.AQUA));
							((EntityPlayerMP)message.entity).sendMessage(text);
						}
						CapacityNetwork.sendMana((EntityPlayerMP)message.entity, owner);
					}
					else {
						TextComponentString text=new TextComponentString("Need "+Integer.toString(mana)+" Mana");
						text.setStyle(new Style().setColor(TextFormatting.RED));
						((EntityPlayerMP)message.entity).sendMessage(text);
						CapacityNetwork.sendMana((EntityPlayerMP)message.entity, owner);
					}
				}
				else if(message.code==1) {
					owner.capacitySelector().move(message.info);
					if(!owner.capacitySelector().isEmpty()) {
						TextComponentString txt=new TextComponentString(owner.capacitySelector().getSelectedIndex()+": "+owner.capacitySelector().getSelected().getName(CapacityContext.withPower(1)));
						SPacketTitle spackettitle = new SPacketTitle(SPacketTitle.Type.ACTIONBAR, txt);
						((EntityPlayerMP)message.entity).connection.sendPacket(spackettitle);
						owner.capacitySelector().getSelected().sendParticleBomb(CapacityContext.withPower(1), message.entity.getPositionVector(),10);
					}
				}
				else if(message.code==2) {
					owner.tellActionMade(message.entity.world);
					owner.materialSelector().move(message.info);
					if(!owner.materialSelector().isEmpty()) {
						TextComponentString txt=new TextComponentString(owner.materialSelector().getSelectedIndex()+": "+owner.materialSelector().getSelected().name);
						SPacketTitle spackettitle = new SPacketTitle(SPacketTitle.Type.ACTIONBAR, txt);
						((EntityPlayerMP)message.entity).connection.sendPacket(spackettitle);
						int color[]=ColorUtils.asRGB(owner.getManaColor());
						Capacity.sendParticleBomb(color[0]/255f, color[1]/255f, color[2]/255f, message.entity.getPositionVector(), owner.getPower()*2);
						CapacityNetwork.sendMana((EntityPlayerMP)message.entity, owner);
					}
				}
			}
		}
		return null;
	}
	
	public static class CapacityKeyMessage implements IMessage{
		public int code;
		public int info;
		public World level;
		public Entity entity;
		
		public void fromBytes(ByteBuf buf) {
			code=buf.readInt();
			info=buf.readInt();
			level=DimensionManager.getWorld(buf.readInt());
			entity=level.getEntityByID(buf.readInt());
		}
		public void toBytes(ByteBuf buf) {
			buf.writeInt(code);
			buf.writeInt(info);
			buf.writeInt(level.provider.getDimension());
			buf.writeInt(entity.getEntityId());
		}
	}
}
