package jempasam.data.serializer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import jempasam.data.chunk.DataChunk;
import jempasam.data.chunk.ObjectChunk;
import jempasam.data.chunk.value.StringChunk;

public class LightDataSerializer implements DataSerializer{
	@Override
	public void write(OutputStream output, ObjectChunk data) {
		DataOutputStream writer=new DataOutputStream(output);
		try {
			writer.writeChar(data.getName().getBytes().length);
			writer.writeBytes(data.getName());
			writeObject(writer,data);
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	private void writeObject(DataOutputStream writer, ObjectChunk data) throws IOException{
		writer.writeChar(data.size());
		for(DataChunk child : data) {
			if(child instanceof ObjectChunk) {
				writer.writeChar(child.getName().getBytes().length);
				writer.writeBytes(child.getName());
				writeObject(writer, (ObjectChunk)child);
			}
			else if(child instanceof StringChunk){
				StringChunk v=(StringChunk)child;
				writer.writeChar(v.getName().getBytes().length);
				writer.writeBytes(v.getName());
				writer.writeChar(10000+v.getValue().getBytes().length);
				writer.writeBytes(v.getValue());
			}
		}
	}
}
