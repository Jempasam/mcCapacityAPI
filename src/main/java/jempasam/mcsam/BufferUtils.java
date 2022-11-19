package jempasam.mcsam;

import io.netty.buffer.ByteBuf;

public class BufferUtils {
	private BufferUtils() {}
	
	
	public static String readString(ByteBuf buffer) {
		byte[] ret=new byte[buffer.readInt()];
		buffer.readBytes(ret);
		return new String(ret);
	}
	
	public static int[] readIntArray(ByteBuf buffer) {
		int[] ret=new int[buffer.readInt()];
		for(int i=0; i<ret.length; i++)ret[i]=buffer.readInt();
		return ret;
	}
	
	public static float[] readFloatArray(ByteBuf buffer) {
		float[] ret=new float[buffer.readInt()];
		for(int i=0; i<ret.length; i++)ret[i]=buffer.readInt();
		return ret;
	}
	
	public static double[] readDoubleArray(ByteBuf buffer) {
		double[] ret=new double[buffer.readInt()];
		for(int i=0; i<ret.length; i++)ret[i]=buffer.readDouble();
		return ret;
	}
	
	public static byte[] readByteArray(ByteBuf buffer) {
		byte[] ret=new byte[buffer.readInt()];
		for(int i=0; i<ret.length; i++)ret[i]=buffer.readByte();
		return ret;
	}
	
	public static boolean[] readBooleanArray(ByteBuf buffer) {
		boolean[] ret=new boolean[buffer.readInt()];
		for(int i=0; i<ret.length; i++)ret[i]=buffer.readBoolean();
		return ret;
	}
	
	public static long[] readLongArray(ByteBuf buffer) {
		long[] ret=new long[buffer.readInt()];
		for(int i=0; i<ret.length; i++)ret[i]=buffer.readLong();
		return ret;
	}
	
	public static short[] readShortArray(ByteBuf buffer) {
		short[] ret=new short[buffer.readInt()];
		for(int i=0; i<ret.length; i++)ret[i]=buffer.readShort();
		return ret;
	}
	
	public static void writeString(ByteBuf buffer, String str) {
		buffer.writeInt(str.length());
		buffer.writeBytes(str.getBytes());
	}
	
	public static void writeIntArray(ByteBuf buffer, int[] array) {
		buffer.writeInt(array.length);
		for(int i=0; i<array.length; i++)buffer.writeInt(array[i]);
	}
	
	public static void writeFloatArray(ByteBuf buffer, float[] array) {
		buffer.writeInt(array.length);
		for(int i=0; i<array.length; i++)buffer.writeFloat(array[i]);
	}
	
	public static void writeDoubleArray(ByteBuf buffer, double[] array) {
		buffer.writeInt(array.length);
		for(int i=0; i<array.length; i++)buffer.writeDouble(array[i]);
	}
	
	public static void writeByteArray(ByteBuf buffer, byte[] array) {
		buffer.writeInt(array.length);
		for(int i=0; i<array.length; i++)buffer.writeByte(array[i]);
	}
	
	public static void writeBooleanArray(ByteBuf buffer, boolean[] array) {
		buffer.writeInt(array.length);
		for(int i=0; i<array.length; i++)buffer.writeBoolean(array[i]);
	}
	
	public static void writeLongArray(ByteBuf buffer, long[] array) {
		buffer.writeInt(array.length);
		for(int i=0; i<array.length; i++)buffer.writeLong(array[i]);
	}
	
	public static void writeShortArray(ByteBuf buffer, short[] array) {
		buffer.writeInt(array.length);
		for(int i=0; i<array.length; i++)buffer.writeShort(array[i]);
	}
}
