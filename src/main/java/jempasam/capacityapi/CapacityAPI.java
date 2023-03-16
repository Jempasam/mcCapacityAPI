package jempasam.capacityapi;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

import electroblob.wizardry.spell.Satiety;
import electroblob.wizardry.spell.Spell;
import jempasam.capacityapi.command.CapacityCommands;
import jempasam.capacityapi.effect.CapacityEffect;
import jempasam.capacityapi.handler.client.ClientHandler;
import jempasam.capacityapi.handler.common.CommonHandler;
import jempasam.capacityapi.handler.server.ServerHandler;
import jempasam.capacityapi.register.CAPIRegistry;



@Mod(modid = CapacityAPI.MODID, name = CapacityAPI.NAME, version = CapacityAPI.VERSION)
public class CapacityAPI
{
    public static final String MODID = "capacityapi";
    public static final String NAME = "Capacity API";
    public static final String VERSION = "1.0";
    
   
    public static Logger logger;
    public static CapacityEffect EFFECT;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	WorldServer world;
    	logger = event.getModLog();
    	MinecraftForge.EVENT_BUS.register(new CommonHandler(this));
    	if(event.getSide().isClient())MinecraftForge.EVENT_BUS.register(new ClientHandler(this));
    	MinecraftForge.EVENT_BUS.register(new ServerHandler(MODID));
    }
    
    public void init(FMLInitializationEvent event) {
    	CAPIRegistry.loadAll();
    }
    
    @EventHandler
    public void initServer(FMLServerAboutToStartEvent server) {
    	//MinecraftForge.EVENT_BUS.register(new ServerHandler(MODID));
    }
    
    @EventHandler
    public void start(FMLServerStartingEvent event) {
    	event.registerServerCommand(CapacityCommands.buildCommandCapacity());
    	event.registerServerCommand(CapacityCommands.buildCommandAlter());
    }
    
    
    
   
}
