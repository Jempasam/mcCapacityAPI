package jempasam.data.deserializer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import jempasam.data.chunk.ObjectChunk;
import jempasam.data.chunk.SimpleObjectChunk;
import jempasam.data.chunk.value.StringChunk;

public class LightDataDeserializer implements DataDeserializer{
	@Override
	public ObjectChunk loadFrom(InputStream i) {
		DataInputStream reader=new DataInputStream(i);
		try {
			char namesize=reader.readChar();
			byte[] namebyte=new byte[namesize];
			reader.read(namebyte);
			String name=new String(namebyte);
			
			char size=reader.readChar();
			ObjectChunk ret=loadObject(size, reader);
			ret.setName(name);
			return ret;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private ObjectChunk loadObject(char size, DataInputStream input) throws IOException {
		SimpleObjectChunk ret=new SimpleObjectChunk("");
		for(int i=0; i<size; i++) {
			char namesize=input.readChar();
			byte[] namebyte=new byte[namesize];
			input.read(namebyte);
			String name=new String(namebyte);
			
			char valuesize=input.readChar();
			if(valuesize>=10000) {
				byte[] valuebyte=new byte[valuesize-10000];
				input.read(valuebyte);
				String value=new String(valuebyte);
				ret.add(new StringChunk(name, value));
			}
			else {
				ObjectChunk child=loadObject(valuesize, input);
				child.setName(name);
				ret.add(child);
			}
		}
		return ret;
	}
}
