package jempasam.capacityapi.item.functionnalities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;

public interface ICommonItem {
	default String getString(ItemStack stack, String name) {
		NBTTagCompound nbt=stack.getTagCompound();
		if(nbt!=null) {
			return nbt.getString(name);
		}
		return null;
	}
	
	default void setString(ItemStack stack, String name, String str) {
		NBTTagCompound nbt=stack.getTagCompound();
		if(nbt==null) {
			nbt=new NBTTagCompound();
			stack.setTagCompound(nbt);
		}
		if(name!=null)nbt.setString(name, str);
	}
	
	default void destroy(ItemStack itemstack, EntityLivingBase player) {
		player.renderBrokenItemStack(itemstack);
		itemstack.shrink(1);
		if(player instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer)player;
			entityplayer.addStat(StatList.getObjectBreakStats(itemstack.getItem()));
		}
	}
}
