package jempasam.capacityapi.utils;

import java.util.List;

public class SelectorList<T> implements Selector<T>{
	
	
	
	private List<T> content;
	private int selected;
	
	
	
	public SelectorList(List<T> content) {
		super();
		this.content = content;
		this.selected = 0;
	}
	
	
	
	public List<T> content(){ return content; }
	
	
	
	@Override public int count() { return content.size(); }
	@Override public int getSelectedIndex() { return selected; }
	@Override public void setSelected(int selected) { this.selected=selected; }
	@Override public T get(int index) { return content.get(index);}
}
