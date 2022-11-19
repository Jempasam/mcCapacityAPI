package jempasam.data.modifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jempasam.data.chunk.DataChunk;
import jempasam.data.chunk.ObjectChunk;
import jempasam.data.chunk.value.StringChunk;

public class StringIndexingDataModifier implements DataModifier {
	
	
	
	private List<String> indexeds;
	private Map<String, Integer> map;
	private int id;
	
	
	
	public StringIndexingDataModifier(List<String> indexeds) {
		super();
		this.indexeds = indexeds;
	}
	
	
	
	@Override
	public void applyOn(ObjectChunk data) {
		map=new HashMap<>();
		indexeds.clear();
		id=0;
		for(DataChunk chunk : data.recursiveStream()) {
			chunk.setName(getIndex(chunk.getName()));
			if(chunk instanceof StringChunk) {
				StringChunk strchunk=(StringChunk)chunk;
				strchunk.setValue(getIndex(strchunk.getValue()));
			}
		}
	}
	
	private String getIndex(String name) {
		Integer value=map.get(name);
		if(value==null) {
			value=id;
			id++;
			map.put(name, value);
			indexeds.add(name);
		}
		return intToString((char)value.intValue());
	}
	
	private static String intToString(char a) {
		StringBuilder sb=new StringBuilder();
		sb.append(a);
		return sb.toString();
	}
	
}
