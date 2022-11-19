package jempasam.capacityapi.capacity;

import java.util.function.IntUnaryOperator;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import jempasam.mcsam.OperatorUtils;
import net.minecraft.world.storage.WorldInfo;

@Loadable
public class WeatherCapacity implements Capacity {
	
	
	
	private IntUnaryOperator duration;
	public int weather=2;
	
	
	
	@LoadableParameter void clear(boolean IGNORED) {weather=0;}
	@LoadableParameter void covered(boolean IGNORED) {weather=1;}
	@LoadableParameter void rain(boolean IGNORED) {weather=2;}
	@LoadableParameter void thunder(boolean IGNORED) {weather=3;}
	
	
	
	@LoadableParameter(paramnames = {"duration"})
	public WeatherCapacity(IntUnaryOperator duration) {
		super();
		this.duration = duration;
	}
	
	
	
	@Override
	public boolean use(CapacityContext context) {
		int maxoffset=context.getPower()*1000;
		WorldInfo world=context.getWorld().getWorldInfo();
		System.out.println("FROM "+world.getThunderTime()+" "+world.getRainTime()+" "+world.getCleanWeatherTime());
		System.out.println("BOOL "+world.isThundering()+" "+world.isRaining()+" ");
		if(!world.isThundering())world.setThunderTime(0);
		if(!world.isRaining())world.setRainTime(0);
		if(!world.isThundering()&&!world.isRaining())world.setCleanWeatherTime(0);
		System.out.println("SO "+world.getThunderTime()+" "+world.getRainTime()+" "+world.getCleanWeatherTime());
		if(weather==0) {
			world.setRaining(false);
			world.setThundering(false);
			int original=world.getCleanWeatherTime();
			world.setCleanWeatherTime(original+OperatorUtils.getOffset(duration, original, -maxoffset, maxoffset));
			world.setRainTime(0);
			world.setThunderTime(0);
			System.out.println("CLEAN "+world.getCleanWeatherTime());
		}
		else if(weather==1) {
			world.setRaining(false);
			world.setThundering(true);
			int original=world.getThunderTime();
			world.setThunderTime(original+OperatorUtils.getOffset(duration, original, -maxoffset, maxoffset));
			world.setRainTime(0);
			world.setCleanWeatherTime(0);
			System.out.println("THUNDER "+world.getThunderTime());
		}
		else if(weather==2) {
			world.setRaining(true);
			world.setThundering(false);
			int original=world.getRainTime();
			world.setRainTime(original+OperatorUtils.getOffset(duration, original, -maxoffset, maxoffset));
			world.setThunderTime(0);
			world.setCleanWeatherTime(0);
			System.out.println("RAIN "+world.getRainTime());
		}
		else if(weather==3) {
			world.setRaining(true);
			world.setThundering(true);
			int original=world.getRainTime();
			world.setRainTime(original+OperatorUtils.getOffset(duration, original, -maxoffset, maxoffset));
			original=world.getThunderTime();
			world.setThunderTime(original+OperatorUtils.getOffset(duration, original, -maxoffset, maxoffset));
			world.setCleanWeatherTime(0);
			System.out.println("THUNDERAIN "+world.getThunderTime()+" "+world.getRainTime());
		}
		return true;
	}
	
	@Override
	public int getColor(CapacityContext context) {
		return 0xFFA400;
	}
	
	@Override
	public int getMana(CapacityContext context) {
		int maxoffset=context.getPower()*1000;
		return Math.max(OperatorUtils.getOffset(duration, 10000, -maxoffset, maxoffset), OperatorUtils.getOffset(duration, 0, -maxoffset, maxoffset));
	}
	
	@Override
	public String getName(CapacityContext context) {
		int d=duration.applyAsInt(0);
		switch(weather) {
		case 0: return d+"h Sun";
		case 1: return d+"h Covered";
		case 2: return d+"h Rain";
		default: return d+"h Storm";
		}
	}
}
