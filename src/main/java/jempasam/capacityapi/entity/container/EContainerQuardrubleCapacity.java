package jempasam.capacityapi.entity.container;

import io.netty.buffer.ByteBuf;
import jempasam.capacityapi.capacity.Capacity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EContainerQuardrubleCapacity extends EContainerDoubleCapacity{
	
	
	
	private Capacity capacity3=null;
	private Capacity capacity4=null;
	@SideOnly(Side.CLIENT)public float red3;
	@SideOnly(Side.CLIENT)public float green3;
	@SideOnly(Side.CLIENT)public float blue3;
	@SideOnly(Side.CLIENT)public float red4;
	@SideOnly(Side.CLIENT)public float green4;
	@SideOnly(Side.CLIENT)public float blue4;
	@SideOnly(Side.CLIENT)public boolean hasCapacity3=false;
	@SideOnly(Side.CLIENT)public boolean hasCapacity4=false;
	
	
	
	public EContainerQuardrubleCapacity(Entity holder) {
		super(holder);
	}
	
	
	
	public void setCapacity3(Capacity capacity) { this.capacity3=capacity; }
	public void setCapacity4(Capacity capacity) { this.capacity4=capacity; }
	
	public Capacity capacity3() { return capacity3; }
	public Capacity capacity4() { return capacity4; }
	
	public void use3(Entity target) { capacity3.use(getContext(target)); }
	public void use4(Entity target) { capacity4.use(getContext(target)); }
	
	
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		super.deserializeNBT(nbt);
		capacity3=getCapacity(nbt, "capacity3");
		capacity4=getCapacity(nbt, "capacity4");
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt=super.serializeNBT();
		setCapacity(nbt, "capacity3", capacity3);
		setCapacity(nbt, "capacity4", capacity4);
		return nbt;
	}
	
	@Override
	public void readSpawnData(ByteBuf buffer) {
		super.readSpawnData(buffer);
		
		red3=buffer.readFloat();
		if(red3!=-1) {
			green3=buffer.readFloat();
			blue3=buffer.readFloat();
			hasCapacity3=true;
		}
		else {red3=red2; green3=green2; blue3=blue2;}
		
		red4=buffer.readFloat();
		if(red4!=-1) {
			green4=buffer.readFloat();
			blue4=buffer.readFloat();
			hasCapacity4=true;
		}
		else {red4=red3; green4=green3; blue4=blue3;}
	}
	
	@Override
	public void writeSpawnData(ByteBuf buffer) {
		super.writeSpawnData(buffer);
		writeCapacityColor(capacity3, buffer);
		writeCapacityColor(capacity4, buffer);
	}
}
