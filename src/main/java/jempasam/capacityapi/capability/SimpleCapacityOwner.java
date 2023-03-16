package jempasam.capacityapi.capability;

import java.util.ArrayList;
import java.util.List;
import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.material.MagicMaterial;
import jempasam.capacityapi.register.CAPIWorldData;
import jempasam.capacityapi.utils.Selector;
import jempasam.capacityapi.utils.SelectorList;
import net.minecraft.world.World;

public class SimpleCapacityOwner implements ICapacityOwner{
	
	
	
	private List<String> categories;
	private SelectorList<Capacity> capacitiesSelector;
	private SelectorList<MagicMaterial> materialsSelector;
	private int mana;
	private String alterName;
	private long lastActionMade;
	private CAPIWorldData data;
	
	
	public SimpleCapacityOwner(CAPIWorldData data) {
		this.categories=new ArrayList<>();
		this.capacitiesSelector=new SelectorList<>(new ArrayList<>());
		this.materialsSelector=new SelectorList<>(new ArrayList<>());
		this.mana=500;
		this.alterName="";
		this.lastActionMade=0;
		this.data=data;
	}



	public long getLastActionTime() { return lastActionMade; }
	public void tellActionMade(World world) { lastActionMade=world.getTotalWorldTime(); }
	
	@Override public List<Capacity> capacities() { return capacitiesSelector.content(); }
	@Override public SelectorList<Capacity> capacitySelector() { return capacitiesSelector; }
	
	@Override public List<MagicMaterial> materials() { return materialsSelector.content(); }
	@Override public Selector<MagicMaterial> materialSelector() { return materialsSelector; }

	@Override public int getMana() { return mana; }
	@Override public void setMana(int mana) { this.mana=mana; }
	
	@Override public List<String> categories() { return categories; }
	
	@Override public String getAlterName() { return alterName; }
	@Override public void setAlterName(String name) { this.alterName=name; }
	
	@Override public CAPIWorldData getDatas() { return data; }
}
