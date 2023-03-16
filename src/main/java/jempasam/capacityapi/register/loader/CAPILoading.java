package jempasam.capacityapi.register.loader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.base.Predicates;

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
import jempasam.data.chunk.DataChunk;
import jempasam.data.chunk.ObjectChunk;
import jempasam.data.deserializer.DataDeserializer;
import jempasam.data.deserializer.DataDeserializers;
import jempasam.data.deserializer.ModifiersDataDeserializer;
import jempasam.data.loader.ObjectLoader;
import jempasam.data.loader.SimpleObjectLoader;
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
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class CAPILoading {
	
	
	
	private CAPIObjectManager<Capacity> capacities;
	private CAPIObjectManager<MagicMaterial> materials;
	private CAPIObjectManager<IFactoryRecipe> recipes;
	private HashMap<String, Integer> colors;
	
	private ObjectLoader<Capacity> capacities_loader;
	private ObjectLoader<MagicMaterial> materials_loader;
	private ObjectLoader<IFactoryRecipe> recipes_loader;
	private ObjectLoader<Object> executor;
	
	private DataDeserializer deserializer;
	private ValueParser parser;
	private DataSerializer serializer;
	private SLogger logger;
	
	
	
	public CAPILoading(CAPIObjectManager<Capacity> capacities, CAPIObjectManager<MagicMaterial> materials, CAPIObjectManager<IFactoryRecipe> recipes, HashMap<String, Integer> colors) {
		super();
		// Other
		logger=SLoggers.OUT;
		
		// Object Manager
		this.capacities = capacities;
		this.materials = materials;
		this.recipes = recipes;
		this.colors=colors;
		
		// Parser
		parser=createParser();
		serializer=new JsonDataSerializer();
		ModFilterDataModifier modfilter=new ModFilterDataModifier();
		ExtracterDataModifier extracter=new ExtracterDataModifier(logger,"(",")","|");
		PlacerDataModifier placer=new PlacerDataModifier(logger, Arrays.asList(new DataPlacer[] {new AdvancedDataPlacer(extracter.variables()::get, "%", "%")}),"(",")","|",":");
		InlinerDataModifier inliner=new InlinerDataModifier(logger);
		deserializer=new ModifiersDataDeserializer(DataDeserializers.createSquareIndentedBaliseDS(logger), Arrays.asList(modfilter, extracter,placer,inliner));
		
		// Object Loader
		this.capacities_loader=new SimpleObjectLoader<>( logger, parser, Capacity.class, "jempasam.capacityapi.capacity.", "Capacity");
		this.materials_loader=new SimpleObjectLoader<>( logger, parser, MagicMaterial.class, "jempasam.capacityapi.material.", "");
		this.recipes_loader=new SimpleObjectLoader<>( logger, parser, IFactoryRecipe.class, "jempasam.capacityapi.block.factoryrecipe.", "");
		this.executor=new SimpleObjectLoader<>(logger, parser, Object.class, "", "");
	}
	
	
	
	public Optional<InputStream> open(String name){
		try {
			return Optional.of(new FileInputStream(FileSystems.getDefault().getPath(Minecraft.getMinecraft().mcDataDir.getPath(),name+".swjlib").toFile()));
		} catch (FileNotFoundException e) {
			logger.error("File \""+name+".swjlib\" not found");
			return Optional.empty();
		}
	}
	
	public Optional<DataChunk> openChunk(String name){
		return open(name).map(i->{
			ObjectChunk ret=deserializer.loadFrom(i);
			logger.debug(serializer.write(ret));
			return ret;
		});
	}
	
	
	public ObjectLoader<Capacity> capacities(){ return capacities_loader; }
	
	public void loadCapacities(ObjectChunk data) {
		capacities_loader.load(capacities, data);
		
		// Color and name optimization
		for(Map.Entry<String, Capacity> capacity : capacities.stream().fixed()) {
    		if(!capacity.getKey().startsWith("_")) {
    			String key=capacity.getKey();
    			Capacity value=capacity.getValue();
    			capacities.register(key, DisplayCapacity.bake(value));
    			capacities.register("_o_"+key, value);
    		}
    	}
	}
	
	public void loadCapacitiesGroup(ObjectChunk data) {
		executor.hydrate(new ObjectGroupBuilder<>(capacities), data);
		
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
	
	
	public ObjectLoader<MagicMaterial> materials(){ return materials_loader; }
	public void loadMaterials(ObjectChunk data) { materials_loader.load(materials, data); }
	public void loadMaterialsGroup(ObjectChunk data) { executor.hydrate(new ObjectGroupBuilder<>(materials), data); }
	
	public ObjectLoader<IFactoryRecipe> recipes(){ return recipes_loader; }
	public void loadRecipes(ObjectChunk data) { recipes_loader.load(recipes, data); }
	public void loadRecipesGroup(ObjectChunk data) { executor.hydrate(new ObjectGroupBuilder<>(recipes), data); }
	
	
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
