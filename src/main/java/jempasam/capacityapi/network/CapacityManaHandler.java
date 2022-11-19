package jempasam.capacityapi.network;

import io.netty.buffer.ByteBuf;
import jempasam.capacityapi.gui.GuiMana;
import jempasam.mcsam.BufferUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import scala.actors.threadpool.Arrays;

public class CapacityManaHandler implements IMessageHandler<CapacityManaHandler.CapacityManaMessage, IMessage>{
	
	@Override
	public IMessage onMessage(CapacityManaMessage message, MessageContext ctx) {
		GuiMana.setMana(message.mana, message.manamax, message.color);
		return null;
	}
	
	public static class CapacityManaMessage implements IMessage{
		public int mana;
		public int manamax;
		public int[] color;
		public void fromBytes(ByteBuf buf) {
			mana=buf.readInt();
			manamax=buf.readInt();
			color=BufferUtils.readIntArray(buf);
		}
		public void toBytes(ByteBuf buf) {
			buf.writeInt(mana);
			buf.writeInt(manamax);
			BufferUtils.writeIntArray(buf, color);
		}
	}
}
