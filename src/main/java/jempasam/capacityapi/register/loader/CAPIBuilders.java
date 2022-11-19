package jempasam.capacityapi.register.loader;

import jempasam.capacityapi.block.factoryrecipe.IFactoryRecipe;
import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.material.MagicMaterial;
import jempasam.capacityapi.register.CAPIRegistry;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import jempasam.objectmanager.groups.ObjectGroupBuilder;

public class CAPIBuilders {
	@Loadable public static class CapacityBuilder extends ObjectGroupBuilder<Capacity>{ @LoadableParameter public CapacityBuilder() { super(CAPIRegistry.CAPACITIES); } }
	@Loadable public static class MaterialBuilder extends ObjectGroupBuilder<MagicMaterial>{ @LoadableParameter public MaterialBuilder() { super(CAPIRegistry.MATERIALS); } }
	@Loadable public static class FactoryBuilder extends ObjectGroupBuilder<IFactoryRecipe>{ @LoadableParameter public FactoryBuilder() { super(CAPIRegistry.FACTORY_RECIPES); } }
}
