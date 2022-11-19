package jempasam.mcsam.command;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandContext {
	
	
	
	public final MinecraftServer server;
	public final ICommandSender sender;
	public final String[] args;
	public final String name;
	public final int actual;
	public final BlockPos target;
	
	private Map<String,Object> parameters;
	
	
	
	public CommandContext(String name, MinecraftServer server, ICommandSender sender, String[] args, int actual, Map<String,Object> parameters, BlockPos pos) {
		super();
		this.name = name;
		this.server = server;
		this.sender = sender;
		this.args = args;
		this.actual = actual;
		this.target = pos;
		this.parameters = parameters;
	}
	
	public static CommandContext forExecution(String name, MinecraftServer server, ICommandSender sender, String[] args) {
		return new CommandContext(name, server, sender, args, 0, new HashMap<>(), null);
	}
	
	public static CommandContext forCompletion(String name, MinecraftServer server, ICommandSender sender, String[] args, BlockPos target) {
		return new CommandContext(name, server, sender, args, 0, null, target);
	}
	
	public static CommandContext forPermission(String name, MinecraftServer server, ICommandSender sender) {
		return new CommandContext(name, server, sender, null, 0, null, null);
	}
	
	public static CommandContext forUsage(String name, ICommandSender sender) {
		return new CommandContext(name, null, sender, null, 0, null, null);
	}
	
	
	
	public CommandContext forward(int count) {
		return new CommandContext(name, server, sender, args, actual+count, parameters, target);
	}
	
	public boolean hasRemaining(int remaining) {
		return actual+remaining<=args.length;
	}
	
	public int remaining() {
		return args.length-actual;
	}
	
	public String arg(int index) {
		return args[actual+index];
	}
	
	public <T> void addParameter(String name, T value) {
		parameters.put(name, value);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getParameter(String name) {
		return (T)parameters.get(name);
	}
	
	
}
