package jempasam.mcsam;

import org.lwjgl.input.Keyboard;

import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class KeyUtils {
	private KeyUtils() {}
	
	public static int getKeynum(int num) {
		switch (num) {
		case 0: return Keyboard.KEY_NUMPAD0;
		case 1: return Keyboard.KEY_NUMPAD1;
		case 2: return Keyboard.KEY_NUMPAD2;
		case 3: return Keyboard.KEY_NUMPAD3;
		case 4: return Keyboard.KEY_NUMPAD4;
		case 5: return Keyboard.KEY_NUMPAD5;
		case 6: return Keyboard.KEY_NUMPAD6;
		case 7: return Keyboard.KEY_NUMPAD7;
		case 8: return Keyboard.KEY_NUMPAD8;
		case 9: return Keyboard.KEY_NUMPAD9;
		default: return 0;
		}
	}
}
