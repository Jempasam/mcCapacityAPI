package jempasam.data.modifier;

import java.util.List;
import jempasam.data.chunk.DataChunk;
import jempasam.data.chunk.ObjectChunk;
import jempasam.data.chunk.value.StringChunk;

public class StringDesindexingDataModifier implements DataModifier {
	
	
	
	private List<String> indexeds;
	
	
	
	public StringDesindexingDataModifier(List<String> indexeds) {
		super();
		this.indexeds = indexeds;
	}
	
	
	
	@Override
	public void applyOn(ObjectChunk data) {
		for(DataChunk chunk : data.recursiveStream()) {
			chunk.setName(getValue(chunk.getName()));
			if(chunk instanceof StringChunk) {
				StringChunk strchunk=(StringChunk)chunk;
				strchunk.setValue(getValue(strchunk.getValue()));
			}
		}
	}
	
	private String getValue(String index) {
		return indexeds.get(stringToInt(index));
	}
	
	private static int stringToInt(String str) {
		return (int)str.charAt(0);
	}
	
}
