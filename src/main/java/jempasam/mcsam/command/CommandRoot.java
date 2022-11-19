package jempasam.mcsam.command;

import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandRoot implements ICommand{
	
	
	
	private ICommandPart startPart;
	private String name;
	private List<String> alias;
	
	
	
	public CommandRoot(String name, List<String> alias, ICommandPart startPart) {
		super();
		this.startPart = startPart;
		this.name = name;
		this.alias = alias;
	}
	
	
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		try {
			startPart.execute(CommandContext.forExecution(getName(), server, sender, args));
		} catch (CommandPartException e) {throw new CommandException(e.getMessage());}
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		CommandContext context=CommandContext.forUsage(getName(), sender);
		StringBuilder sb=new StringBuilder();
		for(String str: startPart.complete(context)) {
			sb.append("/").append(str).append("\n");
		}
		if(sb.length()>0)sb.setLength(sb.length()-1);
		return sb.toString();
	}
	
	@Override
	public List<String> getAliases() {
		return alias;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
		CommandContext context=CommandContext.forCompletion(getName(),server, sender, args, targetPos);
		return startPart.complete(context);
	}
	
	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}
	
	@Override
	public int compareTo(ICommand o) {
		int a=getName().hashCode();
		int b=o.getName().hashCode();
		return a>b ? 1 : (b>a ? -1 : 0);
	}
	
}
