package jempasam.capacityapi.capacity.provider;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import jempasam.capacityapi.capacity.CapacityContext;
import net.minecraft.entity.Entity;

public interface EntityProvider {
	Entity getEntity(CapacityContext context);
	
	public static EntityProvider TARGET=context->context.getTarget();
	public static EntityProvider MARKED=context->context.getMarked();
	public static EntityProvider SENDER=context->context.getSender();
	public static Map<String,EntityProvider> MAP=ImmutableMap.of("target", TARGET, "marked", MARKED, "sender", SENDER);
}
