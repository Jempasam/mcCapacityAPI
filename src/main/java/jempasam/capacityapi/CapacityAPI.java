package jempasam.capacityapi;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import org.apache.logging.log4j.Logger;

import jempasam.capacityapi.command.CapacityCommands;
import jempasam.capacityapi.effect.CapacityEffect;
import jempasam.capacityapi.handler.client.ClientHandler;
import jempasam.capacityapi.handler.common.CommonHandler;
import jempasam.capacityapi.handler.server.ServerHandler;



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
    	logger = event.getModLog();
    	MinecraftForge.EVENT_BUS.register(new CommonHandler(this));
    	if(event.getSide().isClient())MinecraftForge.EVENT_BUS.register(new ClientHandler(this));
    	MinecraftForge.EVENT_BUS.register(new ServerHandler(MODID));
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
