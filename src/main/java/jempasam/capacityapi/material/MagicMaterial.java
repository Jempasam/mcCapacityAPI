package jempasam.capacityapi.material;

import io.netty.buffer.ByteBuf;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import jempasam.mcsam.BufferUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

@Loadable
public class MagicMaterial implements IMessage{
	
	
	
	@LoadableParameter public String name="";
	@LoadableParameter public int speed=1;
	@LoadableParameter public int power=1;
	@LoadableParameter public int infusion=1;
	@LoadableParameter public int charge=1;
	@LoadableParameter public boolean glowing=false;
	private int[] colors;
	private int colorCount;
	
	@LoadableParameter public void color(String txt) {
		colors[colorCount]=Integer.parseInt(txt, 16);
		colorCount++;
	}	
	
	
	@LoadableParameter
	public MagicMaterial() {
		this.colors = new int[10];
		this.colorCount = 0;
	}
	
	public MagicMaterial(int speed, int power, int infusion, int charge, int color) {
		super();
		this.speed = speed;
		this.power = power;
		this.infusion = infusion;
		this.charge = charge;
		this.colors = new int[] {color};
		this.colorCount = 1;
	}
	
	

	public int getSpeed() {
		return speed;
	}

	public int getPower() {
		return power;
	}

	public int getInfusion() {
		return infusion;
	}

	public int getCharge() {
		return charge;
	}

	public int getColorCount() {
		return colorCount;
	}
	
	public int getColor(int index) {
		return colors[index];
	}
	
	public int getColor() {
		return colors[(int)(System.currentTimeMillis()%(199*colorCount)/200)];
	}
	
	public int[] getColorArray() {
		int[] recolor=new int[colorCount];
		for(int i=0; i<colorCount; i++)recolor[i]=colors[i];
		return recolor;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.charge=buf.readInt();
		this.infusion=buf.readInt();
		this.power=buf.readInt();
		this.speed=buf.readInt();
		
		this.glowing=buf.readBoolean();
		this.colorCount=buf.readInt();
		this.colors=BufferUtils.readIntArray(buf);
		this.name=BufferUtils.readString(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(charge);
		buf.writeInt(infusion);
		buf.writeInt(power);
		buf.writeInt(speed);
		
		buf.writeBoolean(glowing);
		buf.writeInt(colorCount);
		BufferUtils.writeIntArray(buf, colors);
		BufferUtils.writeString(buf, name);
	}
	
	
}
