package jempasam.converting;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.LongSupplier;
import java.util.function.LongUnaryOperator;
import java.util.function.Supplier;

import jempasam.data.chunk.ObjectChunk;
import jempasam.data.chunk.value.StringChunk;

public class ValueParsers {
	public static SimpleValueParser createSimpleValueParser() {
		SimpleValueParser ret=new SimpleValueParser();
		
		ret.add(String.class, Boolean.TYPE, Boolean::parseBoolean);
		
		ret.add(String.class, Character.TYPE,	(s)->s.charAt(0));
		
		ret.add(String.class, Byte.TYPE, str->parseNum(str, Byte::parseByte));
		ret.add(String.class, Short.TYPE, str->parseNum(str, Short::parseShort));
		ret.add(String.class, Integer.TYPE, str->parseNum(str, Integer::parseInt));
		ret.add(String.class, Long.TYPE, str->parseNum(str, Long::parseLong));
		
		ret.add(String.class, Float.TYPE, Float::parseFloat);
		ret.add(String.class, Double.TYPE, Double::parseDouble);
		
		ret.add(String.class, Boolean.class, Boolean::parseBoolean);
		
		ret.add(String.class, Character.class,	(s)->s.charAt(0));
		
		ret.add(String.class, Byte.class, str->parseNum(str, Byte::parseByte));
		ret.add(String.class, Short.class, str->parseNum(str, Short::parseShort));
		ret.add(String.class, Integer.class, str->parseNum(str, Integer::parseInt));
		ret.add(String.class, Long.class, str->parseNum(str, Long::parseLong));
		
		ret.add(String.class, Float.class, Float::parseFloat);
		ret.add(String.class, Double.class, Double::parseDouble);
		
		ret.add(String.class, String.class, Function.identity());
		
		ret.add(ObjectChunk.class, ObjectChunk.class, oc->{
			try {
				return oc.clone();
			} catch (CloneNotSupportedException e) {
				return null;
			}
		});
		ret.add(ObjectChunk.class, StringChunk.class, objectchunk->{
			if(objectchunk.size()==1 && objectchunk.get(0) instanceof StringChunk) {
				return (StringChunk)objectchunk.get(0);
			}
			else return null;
		});
		return ret;
	}
	
	public static SimpleValueParser createSimpleValueSerializer() {
		SimpleValueParser ret=createSimpleValueParser();
		
		ret.add(Short.class, String.class, v->v.toString());
		ret.add(Integer.class, String.class, v->v.toString());
		ret.add(BigInteger.class, String.class, v->v.toString());
		ret.add(Long.class, String.class, v->v.toString());
		
		ret.add(Float.class, String.class, v->v.toString());
		ret.add(BigDecimal.class, String.class, v->v.toString());
		ret.add(Double.class, String.class, v->v.toString());
		
		ret.add(Byte.class, String.class, v->v.toString());
		ret.add(Boolean.class, String.class, v->v.toString());
		ret.add(String.class, String.class, v->v.toString());
		
		ret.add(Supplier.class, String.class, supplier->ret.parse(String.class, supplier.get()));
		ret.add(IntSupplier.class, String.class, supplier->Integer.toString(supplier.getAsInt()));
		ret.add(LongSupplier.class, String.class, supplier->Long.toString(supplier.getAsLong()));
		ret.add(DoubleSupplier.class, String.class, supplier->Double.toString(supplier.getAsDouble()));
		ret.add(BooleanSupplier.class, String.class, supplier->Boolean.toString(supplier.getAsBoolean()));
		
		ret.add(Iterable.class, String.class, obj->{
			System.out.println("iterable");
			StringBuilder sb=new StringBuilder();
			for(Object o : obj)sb.append(ret.parse(String.class, o));
			return sb.toString();
		});
		
		/*ret.add(Serializable.class, String.class, obj->{
			System.out.println("serializable");
			try {
				ObjectOutputStream output = new ObjectOutputStream(new ByteArrayOutputStream());
				output.writeObject(obj);
				return obj.toString();
			} catch (IOException e) { return null; }
		});*/
		
		ret.add(Object.class, String.class, Object::toString);
		return ret;
	}
	
	public static SimpleValueParser createCompleteValueParser() {
		Random rd=new Random();
		SimpleValueParser ret=createSimpleValueParser();
		ret.add(String.class, IntUnaryOperator.class, (String str)->{
			if(str.startsWith("=")) {
				String nbstr=str.substring(1);
				if(nbstr.contains("..")) {
					String[] parts=nbstr.split("\\.\\.", 2);
					int aa=parseNum(parts[0], Integer::parseInt);
					int bb=parseNum(parts[1], Integer::parseInt)-aa;
					return new IntUnaryOperator() { public int applyAsInt(int a) { return aa+rd.nextInt(bb); } public String toString() { return aa+"-"+(aa+bb); } };
				}
				else {
					int nb=parseNum(nbstr, Integer::parseInt);
					return new IntUnaryOperator() { public int applyAsInt(int a) { return nb; } public String toString() { return ""+nb; } };
				}
			}
			else {
				if(str.contains("..")) {
					String[] parts=str.split("\\.\\.", 2);
					int aa=parseNum(parts[0], Integer::parseInt);
					int bb=parseNum(parts[1], Integer::parseInt)-aa;
					return new IntUnaryOperator() { public int applyAsInt(int a) { return a+aa+rd.nextInt(bb); } public String toString() { return aa+"-"+(aa+bb); } };
				}
				else {
					int nb=parseNum(str, Integer::parseInt);
					return new IntUnaryOperator() { public int applyAsInt(int a) { return a+nb; } public String toString() { return ""+nb; } };
				}
			}
		});
		ret.add(String.class, DoubleUnaryOperator.class, (String str)->{
			if(str.startsWith("=")) {
				String nbstr=str.substring(1);
				if(nbstr.contains("..")) {
					String[] parts=nbstr.split("\\.\\.", 2);
					double aa=Double.parseDouble(parts[0]);
					double bb=Double.parseDouble(parts[1])-aa;
					return new DoubleUnaryOperator() { public double applyAsDouble(double a) { return aa+rd.nextDouble()*bb; } public String toString() { return aa+"-"+(aa+bb); } };
				}
				else {
					double nb=Double.parseDouble(nbstr);
					return new DoubleUnaryOperator() { public double applyAsDouble(double a) { return nb; } public String toString() { return ""+nb; } };
				}
			}
			else {
				if(str.contains("..")) {
					String[] parts=str.split("\\.\\.", 2);
					double aa=Double.parseDouble(parts[0]);
					double bb=Double.parseDouble(parts[1])-aa;
					return new DoubleUnaryOperator() { public double applyAsDouble(double a) { return a+aa+rd.nextDouble()*bb; } public String toString() { return aa+"-"+(aa+bb); } };
				}
				else {
					double nb=Double.parseDouble(str);
					return new DoubleUnaryOperator() { public double applyAsDouble(double a) { return a+nb; } public String toString() { return ""+nb; } };
				}
			}
		});
		ret.add(String.class, LongUnaryOperator.class, (String str)->{
			if(str.startsWith("=")) {
				String nbstr=str.substring(1);
				if(nbstr.contains("..")) {
					String[] parts=nbstr.split("\\.\\.", 2);
					long aa=parseNum(parts[0], Long::parseLong);
					int bb=(int)(parseNum(parts[1], Long::parseLong)-aa);
					return new LongUnaryOperator() { public long applyAsLong(long a) { return aa+rd.nextInt(bb); } public String toString() { return aa+"-"+(aa+bb); } };
				}
				else {
					int nb=parseNum(nbstr, Integer::parseInt);
					return new LongUnaryOperator() { public long applyAsLong(long a) { return nb; } public String toString() { return ""+nb; } };
				}
			}
			else {
				if(str.contains("..")) {
					String[] parts=str.split("\\.\\.", 2);
					long aa=parseNum(parts[0], Long::parseLong);
					int bb=(int)(parseNum(parts[1], Long::parseLong)-aa);
					return new LongUnaryOperator() { public long applyAsLong(long a) { return a+aa+rd.nextInt(bb); } public String toString() { return aa+"-"+(aa+bb); } };
				}
				else {
					long nb=parseNum(str, Long::parseLong);
					return new LongUnaryOperator() { public long applyAsLong(long a) { return a+nb; } public String toString() { return ""+nb; } };
				}
			}
		});
		return ret;
	}
	
	private static <T> T parseNum(String str, BiFunction<String, Integer, T> converter) {
		try {
			if(str.length()>2 && str.startsWith("0x")) {
				return converter.apply(str.substring(2), 16);
			}
			return converter.apply(str, 10);
		}catch (NumberFormatException e) {
			return null;
		}
	}
	
}
