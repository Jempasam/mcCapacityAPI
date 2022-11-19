package jempasam.capacityapi.utils;

import java.util.Random;

public class RandomUtils {
	private Random rand;
	
	public RandomUtils(Random rand) {
		super();
		this.rand = rand;
	}

	public int nextInt(int min, int max) {
		int offset=max-min;
		if(offset==0)return min;
		return min+rand.nextInt(offset);
	}
	public int nextInt(int[] range) {
		return nextInt(range[0], range[1]);
	}
	
	public float nextFloat(float min, float max) {
		float offset=max-min;
		if(offset==0)return min;
		return min+rand.nextInt((int)(offset*1000))/1000f;
	}
	public float nextFloat(float[] range) {
		return nextFloat(range[0], range[1]);
	}
}
