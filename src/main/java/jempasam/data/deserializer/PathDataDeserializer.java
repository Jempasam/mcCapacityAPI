package jempasam.data.deserializer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import jempasam.data.chunk.DataChunk;
import jempasam.data.chunk.ObjectChunk;
import jempasam.data.chunk.SimpleObjectChunk;
import jempasam.data.chunk.value.StringChunk;
import jempasam.logger.SLogger;
import jempasam.samstream.SamStreams;
import jempasam.samstream.stream.SamStream;
import jempasam.samstream.stream.SamStream.BufferedSStream;

public class PathDataDeserializer extends AbstractDataDeserializer{
	
	
	
	
	public String separatorToken="<";
	public String assignementToken=">";
	public String memberToken=".";
	
	
	
	public PathDataDeserializer(Function<InputStream, SamStream<String>> tokenizerSupplier, SLogger logger) {
		super(logger,tokenizerSupplier);
	}
	
	
	
	@Override
	public ObjectChunk loadFrom(InputStream i) {
		BufferedSStream<String> input=tokenizerSupplier.apply(i).buffered(10);
		ObjectChunk ret=new SimpleObjectChunk("root");
		while(input.hasNext()) {
			
			// Get parent and name
			ObjectChunk parent=ret;
			String name="";
			while(input.hasNext()) {
				String token=input.tryNext();
				if(token.equals(memberToken)) {
					if(name!=null) {
						ObjectChunk newparent=parent.getObject(name);
						if(newparent==null) {
							newparent=new SimpleObjectChunk(name);
							parent.add(newparent);
						}
						parent=newparent;
					}
					if(input.hasNext())name=input.tryNext();
					else name="";
				}
				else if(token.equals(assignementToken)) break;
				else if(token.equals(separatorToken)) {
					input.back();
					break;
				}
				else name+=token;
			}
			
			//Get value
			List<String> values=new ArrayList<>();
			while(input.hasNext()) {
				String token=input.tryNext();
				if(token.equals(assignementToken)) values.add(input.tryNext());
				else if(token.equals(separatorToken)) break;
				else {
					if(values.isEmpty())values.add(token);
					else values.set(values.size()-1, values.get(values.size()-1)+token);
				}
			}
			
			createDataChunks(new ArrayList<>(Arrays.asList(name)), SamStreams.create(values).<DataChunk>map(str->new StringChunk("", str)).toList()).forEach(parent::add);
		}
		return ret;
	}
	
}
