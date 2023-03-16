package jempasam.capacityapi.capability;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.material.MagicMaterial;
import jempasam.capacityapi.register.CAPIWorldData;
import jempasam.capacityapi.utils.Selector;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.util.Constants.NBT;

public interface ICapacityOwner{
	
	List<Capacity> capacities();
	List<MagicMaterial> materials();
	List<String> categories();
	
	long getLastActionTime();
	void tellActionMade(World world);
	
	Selector<MagicMaterial> materialSelector();
	Selector<Capacity> capacitySelector();
	
	String getAlterName();
	void setAlterName(String name);
	
	int getMana();
	void setMana(int mana);
	
	CAPIWorldData getDatas();
	
	default void addMana(int mana) {
		int max=getMaxMana();
		int m=getMana()+mana;
		if(m<0)m=0;
		if(m>max)m=max;
		setMana(m);
	}
	
	default void addManaPart(float mana) {
		int max=getMaxMana();
		int m=getMana()+(int)(max*mana)+1;
		if(m<0)m=0;
		if(m>max)m=max;
		setMana(m);
	}
	
	default int getMaxMana() {
		MagicMaterial m=materialSelector().getSelected();
		return m==null ? 0 : m.getCharge();
	}
	
	default int getManaColor() {
		MagicMaterial m=materialSelector().getSelected();
		return m==null ? 0xffffff : m.getColor((int)(System.currentTimeMillis()%m.getColorCount()));
	}
	
	default int[] getManaColors() {
		MagicMaterial m=materialSelector().getSelected();
		return m==null ? new int[] {0xffffff} : m.getColorArray();
	}
	
	default int getPower() {
		MagicMaterial m=materialSelector().getSelected();
		return m==null ? 1 : m.getPower();
	}
	
	default void giveCategory(String category) {
		categories().add(0,category);
		setAlterName(getAlterName()+(getAlterName().length()>0?" ":"")+StringUtils.capitalize(category));
	}
	
	default void generate(int seed, int count) {
		Random rand= seed==-1 ? new Random() : new Random(seed+capacities().size());
		while(count>0) {
			for(String category : categories()) {
				List<Capacity> capacities=getDatas().CAPACITIES.ofCategories(category).toList();
				capacities.removeAll(capacities());
				Collections.shuffle(capacities);
				while(rand.nextFloat()<(0.9f-(capacities().size()-categories().size()*2)*0.1f) && capacities.size()>0 && count>0) {
					int index=rand.nextInt(capacities.size());
					capacities().add(capacities.get(index));
					capacities.remove(index);
					count--;
				}
			}
			Collections.shuffle(categories());
			if(count>0) {
				List<String> keys=getDatas().CAPACITIES.categoriesNames().toList();
				keys.removeAll(categories());
				String ncateg=keys.get(rand.nextInt(keys.size()));
				giveCategory(ncateg);
			}
		}
	}
	
	default void giveCapacity(Capacity capacity) {
		List<Capacity> list=capacities();
		if(!list.contains(capacity))list.add(capacity);
	}
	
	default void resetCapacities(int seed) {
		Random rand= seed==-1 ? new Random() : new Random(seed+959784);
		System.out.println("O"+categories());
		capacities().clear();
		categories().clear();
		materials().clear();
		List<MagicMaterial> materials=getDatas().MATERIALS.ofCategories("body").toList();
		materials().add(materials.get(rand.nextInt(materials.size())));
		materials().add(materials.get(rand.nextInt(materials.size())));
		capacitySelector().setSelected(0);
		setAlterName("");
	}
	
	public static final IStorage<ICapacityOwner> STORAGE=new IStorage<ICapacityOwner>() {
		
		@Override
		public NBTBase writeNBT(Capability<ICapacityOwner> capability, ICapacityOwner instance, EnumFacing side) {
			NBTTagCompound object=new NBTTagCompound();
			object.setInteger("mana", instance.getMana());
			object.setInteger("activatedMaterial", instance.materialSelector().getSelectedIndex());
			object.setString("alterName", instance.getAlterName());
			object.setInteger("activatedCapacity", instance.capacitySelector().getSelectedIndex());
			NBTTagList capacities=new NBTTagList();
			NBTTagList materials=new NBTTagList();
			NBTTagList categories=new NBTTagList();
			object.setTag("capacities", capacities);
			object.setTag("materials", materials);
			object.setTag("categories", categories);
			System.out.println(instance.getDatas().CAPACITIES.stream().map(Map.Entry::getKey).asString());
			for(Capacity c : instance.capacities()) {
				System.out.println(": "+c);
				System.out.println(": "+instance.getDatas().CAPACITIES.idOf(c));
				capacities.appendTag(new NBTTagString(instance.getDatas().CAPACITIES.idOf(c)));
			}
			for(MagicMaterial m : instance.materials())materials.appendTag(new NBTTagString(instance.getDatas().MATERIALS.idOf(m)));
			for(String c : instance.categories())categories.appendTag(new NBTTagString(c));
			return object;
		}
		
		@Override
		public void readNBT(Capability<ICapacityOwner> capability, ICapacityOwner instance, EnumFacing side, NBTBase nbt) {
			System.out.println(nbt.toString());
			if(nbt instanceof NBTTagCompound) {
				NBTTagCompound object=(NBTTagCompound)nbt;
				instance.materialSelector().setSelected(object.getInteger("activatedMaterial"));
				instance.capacitySelector().setSelected(object.getInteger("activatedCapacity"));
				instance.setAlterName(object.getString("alterName"));
				instance.capacities().clear();
				instance.categories().clear();
				instance.materials().clear();
				for(NBTBase e : object.getTagList("capacities", NBT.TAG_STRING)) {
					if(e instanceof NBTTagString) {
						Capacity c=instance.getDatas().CAPACITIES.get(((NBTTagString) e).getString());
						if(c!=null)instance.capacities().add(c);
					}
				}
				for(NBTBase e : object.getTagList("materials", NBT.TAG_STRING)) {
					if(e instanceof NBTTagString) {
						MagicMaterial m=instance.getDatas().MATERIALS.get(((NBTTagString) e).getString());
						if(m!=null)instance.materials().add(m);
					}
				}
				for(NBTBase e : object.getTagList("categories", NBT.TAG_STRING)) {
					if(e instanceof NBTTagString) {
						instance.categories().add(((NBTTagString) e).getString());
					}
				}
				instance.setMana(object.getInteger("mana"));
			}
		}
	};
	
}
