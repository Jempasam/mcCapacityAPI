package jempasam.samstream.adapter;

import java.util.function.Consumer;

import jempasam.samstream.stream.SamStream;

public class ArraySStream<T> implements SamStream<T>{
	
	
	
	private T[] array;
	private int index;
	private int start;
	
	
	
	public ArraySStream(T[] array, int start) {
		super();
		this.array=array;
		this.start=start-1;
		index=this.start;
	}
	
	
	
	@Override
	public boolean hasSucceed() {
		return index<array.length;
	}
	
	@Override
	public T tryNext() {
		index++;
		if(index<array.length)return array[index];
		else return null;
	}
	
	@Override
	public synchronized void syncNext(Consumer<T> action) {
		if(index<array.length) {
			action.accept(array[index]);
			index++;
		}
	}
	
	@Override
	public synchronized void reset() {
		index=this.start;
	}

}
