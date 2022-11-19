package jempasam.mcsam.command;

public class CommandPartException extends Exception{
	
	
	
	private static final long serialVersionUID = -6030284876080005132L;
	public final CommandContext context;
	
	
	
	public CommandPartException(String message, CommandContext context) {
		super(message);
		this.context=context;
	}
}
