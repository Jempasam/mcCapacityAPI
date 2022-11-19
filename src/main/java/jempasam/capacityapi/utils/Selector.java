package jempasam.capacityapi.utils;

public interface Selector<T> {
	
	int getSelectedIndex();
	default T getSelected() { return get(getSelectedIndex()); }
	T get(int index);
	
	default boolean isEmpty() { return count()==0; }
	int count();
	
	void setSelected(int selected);
	
	default void move(int offset) { if(count()>0)setSelected((getSelectedIndex()+offset)%count()); }
	default void moveNext() { move(1); }
	default void movePrevious() { move(-1); }
}
