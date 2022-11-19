package jempasam.capacityapi.register.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Predicates;

import jempasam.capacityapi.CapacityAPI;
import jempasam.capacityapi.block.factoryrecipe.IFactoryRecipe;
import jempasam.capacityapi.capability.CAPICapabilities;
import jempasam.capacityapi.capability.ICapacityOwner;
import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.capacity.CapacityContext;
import jempasam.capacityapi.capacity.DisplayCapacity;
import jempasam.capacityapi.capacity.provider.EntityProvider;
import jempasam.capacityapi.material.MagicMaterial;
import jempasam.capacityapi.utils.ColorUtils;
import jempasam.converting.SimpleValueParser;
import jempasam.converting.ValueParser;
import jempasam.converting.ValueParsers;
import jempasam.data.chunk.ObjectChunk;
import jempasam.data.deserializer.DataDeserializer;
import jempasam.data.deserializer.DataDeserializers;
import jempasam.data.deserializer.ModifiersDataDeserializer;
import jempasam.data.loader.ObjectLoader;
import jempasam.data.loader.SimpleObjectLoader;
import jempasam.data.modifier.DataModifier;
import jempasam.data.modifier.ExtracterDataModifier;
import jempasam.data.modifier.InlinerDataModifier;
import jempasam.data.modifier.placer.AdvancedDataPlacer;
import jempasam.data.modifier.placer.DataPlacer;
import jempasam.data.modifier.placer.PlacerDataModifier;
import jempasam.data.serializer.DataSerializer;
import jempasam.data.serializer.JsonDataSerializer;
import jempasam.logger.SLogger;
import jempasam.logger.SLoggers;
import jempasam.mcsam.data.ModFilterDataModifier;
import jempasam.objectmanager.HashObjectManager;
import jempasam.objectmanager.ObjectManager;
import jempasam.objectmanager.groups.GroupObjectManager;
import jempasam.objectmanager.groups.ObjectGroupBuilder;
import jempasam.samstream.SamStreams;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class CAPILoading {
	
	
	
	private GroupObjectManager<Capacity> capacities;
	private GroupObjectManager<MagicMaterial> materials;
	private GroupObjectManager<IFactoryRecipe> recipes;
	private HashMap<String, Integer> colors;
	private DataDeserializer deserializer;
	private ValueParser parser;
	private DataSerializer serializer;
	private SLogger logger;
	
	
	
	public CAPILoading(GroupObjectManager<Capacity> capacities, GroupObjectManager<MagicMaterial> materials, GroupObjectManager<IFactoryRecipe> recipes, HashMap<String, Integer> colors) {
		super();
		this.capacities = capacities;
		this.materials = materials;
		this.recipes = recipes;
		this.colors=colors;
		logger=SLoggers.OUT;
		parser=createParser();
		serializer=new JsonDataSerializer();
		ModFilterDataModifier modfilter=new ModFilterDataModifier();
		ExtracterDataModifier extracter=new ExtracterDataModifier(logger,"(",")","|");
		PlacerDataModifier placer=new PlacerDataModifier(logger, Arrays.asList(new DataPlacer[] {new AdvancedDataPlacer(extracter.variables()::get, "%", "%")}),"(",")","|",":");
		InlinerDataModifier inliner=new InlinerDataModifier(logger);
		deserializer=new ModifiersDataDeserializer(DataDeserializers.createSquareIndentedBaliseDS(logger), Arrays.asList(modfilter, extracter,placer,inliner));
	}
	
	
	
	public <T> void load(String filename, ObjectManager<T> manager, Class<T> clazz, String prefix, String suffix) {
		try {
    		ObjectChunk data=deserializer.loadFrom(new FileInputStream(new File(Minecraft.getMinecraft().mcDataDir.getPath(), filename+".swjib")));
    		System.out.println(serializer.write(data));
    		ObjectLoader<T> loader=new SimpleObjectLoader<>( logger, parser, clazz, prefix, suffix);
			loader.load(manager, data);
		} catch (FileNotFoundException e) {
			CapacityAPI.logger.info("No "+filename+" file finded");
		}
	}
	
	public <T, G extends ObjectGroupBuilder<T>> void loadWithCategories(String filename, GroupObjectManager<T> manager, Class<T> clazz, Class<G> builderClazz, String prefix, String suffix) {
		load(filename, manager, clazz, prefix, suffix);
		ObjectManager<G> categoryManager=new HashObjectManager<>();
		load(filename+"_group", categoryManager, builderClazz, "", "");
	}
	
	public Map<ICapacityOwner, NBTBase> getBackup(){
		HashMap<ICapacityOwner, NBTBase> datas=new HashMap<>();
		SamStreams.create(DimensionManager.getWorlds())
	    	.flatMap(w->SamStreams.create(w.getPlayers(EntityPlayerMP.class, Predicates.alwaysTrue())))
	    	.map(p->p.getCapability(CAPICapabilities.CAPACITY_OWNER, null))
	    	.notNull()
	    	.forEach(o->datas.put(o,CAPICapabilities.CAPACITY_OWNER.writeNBT(o, null)));
		return datas;
	}
	
	public void loadBackup(Map<ICapacityOwner, NBTBase> backup) {
		backup.entrySet().stream().forEach(e->CAPICapabilities.CAPACITY_OWNER.readNBT(e.getKey(), null, e.getValue()));
	}
	
	public void loadCapacities() {
		load("capacities", capacities, Capacity.class, "jempasam.capacityapi.capacity.", "Capacity");
		
		// Color and name optimization
		for(Map.Entry<String, Capacity> capacity : capacities.stream().fixed()) {
    		System.out.println(capacity.getKey());
    		if(!capacity.getKey().startsWith("_")) {
    			String key=capacity.getKey();
    			Capacity value=capacity.getValue();
    			capacities.register(key, DisplayCapacity.bake(value));
    			capacities.register("_o_"+key, value);
    		}
    	}
		
		ObjectManager<CAPIBuilders.CapacityBuilder> categoryManager=new HashObjectManager<>();
		load("capacities_group", categoryManager, CAPIBuilders.CapacityBuilder.class, "", "");
		
		// Fetch Category Infos
		CapacityContext context=CapacityContext.withPower(1);
		for(Map.Entry<String, Collection<Capacity>> category : capacities.categories()) {
			int[] colorsum= {0,0,0};
			for(Capacity capacity : category.getValue()) {
				int color[]=ColorUtils.asRGB(capacity.getColor(context));
				colorsum[0]+=color[0]; colorsum[1]+=color[1]; colorsum[2]+=color[2];
			}
			colorsum[0]/=category.getValue().size();
			colorsum[1]/=category.getValue().size();
			colorsum[2]/=category.getValue().size();
			colors.put(category.getKey(), ColorUtils.asInt(colorsum));
		}
	}
	
	public void loadMaterials() {
		loadWithCategories("materials", materials, MagicMaterial.class, CAPIBuilders.MaterialBuilder.class, "jempasam.capacityapi.material.", "" );
	}
	
	public void loadRecipes() {
		loadWithCategories("recipes", recipes, IFactoryRecipe.class, CAPIBuilders.FactoryBuilder.class, "jempasam.capacityapi.block.factoryrecipe.", "" );
	}

	private SimpleValueParser createParser() {
		SimpleValueParser ret=ValueParsers.createCompleteValueParser();
		ret.add(String.class, Item.class, str->ForgeRegistries.ITEMS.getValue(new ResourceLocation(str)));
		ret.add(String.class, Block.class, str->ForgeRegistries.BLOCKS.getValue(new ResourceLocation(str)));
		ret.add(String.class, Potion.class, str->ForgeRegistries.POTIONS.getValue(new ResourceLocation(str)));
		ret.add(String.class, Capacity.class, capacities::get);
		ret.add(String.class, MagicMaterial.class, materials::get);
		ret.add(String.class, EntityProvider.class, EntityProvider.MAP::get);
		ret.add(String.class, CAPISuppliers.Capacities.class, str->{
			Capacity capacity=capacities.get(str);
			if(capacity!=null) return ()->SamStreams.singleton(capacity);
			else return ()->capacities.ofCategories(str);
		});
		ret.add(String.class, CAPISuppliers.Materials.class, str->{
			MagicMaterial material=materials.get(str);
			if(material!=null)return ()->SamStreams.singleton(material);
			else return ()->materials.ofCategories(str);
		});
		return ret;
	}
	
	
	
}
