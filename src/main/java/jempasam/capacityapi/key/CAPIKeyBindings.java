package jempasam.capacityapi.key;

import java.awt.event.KeyEvent;

import org.lwjgl.input.Keyboard;

import jempasam.capacityapi.network.CapacityNetwork;
import jempasam.mcsam.KeyUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CAPIKeyBindings {
	public static final KeyBinding SEND=new KeyBinding("usecapacity",Keyboard.KEY_R, "capacityapi");
	public static final KeyBinding NEXT_CAPACITY=new KeyBinding("nextcapacity", Keyboard.KEY_F, "capacityapi");
	public static final KeyBinding PREVIOUS_CAPACITY=new KeyBinding("previouscapacity", 0, "capacityapi");
	public static final KeyBinding NEXT_MATERIAL=new KeyBinding("nextmaterial", Keyboard.KEY_G, "capacityapi");
	public static final KeyBinding PREVIOUS_MATERIAL=new KeyBinding("previousmaterial", 0, "capacityapi");
	public static final KeyBinding[] SEND_SPECIFIC=new KeyBinding[10];
	static {
		for(int i=0; i<10; i++)SEND_SPECIFIC[i]=new KeyBinding("usecapacity"+i, KeyUtils.getKeynum(i), "capacityapi");
	}
	
	public static void register() {
		ClientRegistry.registerKeyBinding(SEND);
		ClientRegistry.registerKeyBinding(PREVIOUS_CAPACITY);
		ClientRegistry.registerKeyBinding(NEXT_CAPACITY);
		ClientRegistry.registerKeyBinding(PREVIOUS_MATERIAL);
		ClientRegistry.registerKeyBinding(NEXT_MATERIAL);
		for(int i=0; i<10; i++)ClientRegistry.registerKeyBinding(SEND_SPECIFIC[i]);
	}
	
	@SubscribeEvent
	public static void onKeyInput(InputEvent.KeyInputEvent event) {
		if(Keyboard.getEventKeyState()) {
			if(SEND.isKeyDown())CapacityNetwork.sendKey(0,-1);
			else if(PREVIOUS_CAPACITY.isKeyDown())CapacityNetwork.sendKey(1,-1);
			else if(NEXT_CAPACITY.isKeyDown())CapacityNetwork.sendKey(1,1);
			else if(PREVIOUS_MATERIAL.isKeyDown())CapacityNetwork.sendKey(2,-1);
			else if(NEXT_MATERIAL.isKeyDown())CapacityNetwork.sendKey(2,1);
			else for(int i=0; i<10; i++) {
				if(SEND_SPECIFIC[i].isKeyDown()) {
					CapacityNetwork.sendKey(0,i);
					break;
				}
			}
		}
	}
}
