package jempasam.mcsam;

import java.util.function.DoubleUnaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.function.LongUnaryOperator;

public class OperatorUtils {
	private OperatorUtils() {}
	
	public static int getOffset(IntUnaryOperator operator, int input, int min, int max) {
		int offset=operator.applyAsInt(input)-input;
		if(offset>max)return max;
		else if(offset<min)return min;
		else return offset;
	}
	
	public static long getOffset(LongUnaryOperator operator, long input, long min, long max) {
		long offset=operator.applyAsLong(input)-input;
		if(offset>max)return max;
		else if(offset<min)return min;
		else return offset;
	}
	
	public static double getOffset(DoubleUnaryOperator operator, double input, double min, double max) {
		double offset=operator.applyAsDouble(input)-input;
		if(offset>max)return max;
		else if(offset<min)return min;
		else return offset;
	}
}
