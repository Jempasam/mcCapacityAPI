package jempasam.capacityapi.handler.client;

import jempasam.capacityapi.CapacityAPI;
import jempasam.capacityapi.gui.GuiMana;
import jempasam.capacityapi.key.CAPIKeyBindings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientHandler {
	
	
	
	public ClientHandler(CapacityAPI mod) {
		System.out.println("jey");
		CAPIKeyBindings.register();
		MinecraftForge.EVENT_BUS.register(CAPIKeyBindings.class);
		MinecraftForge.EVENT_BUS.register(ParticleHandler.class);
		MinecraftForge.EVENT_BUS.register(new CreativeTabRegistry());
    	MinecraftForge.EVENT_BUS.register(new ModelHandler(mod));
    	MinecraftForge.EVENT_BUS.register(new GuiMana());
    	MinecraftForge.EVENT_BUS.register(new ParticleHandler());
	}
}
