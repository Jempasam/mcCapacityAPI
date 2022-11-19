package jempasam.data.deserializer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import jempasam.data.chunk.DataChunk;
import jempasam.data.chunk.ObjectChunk;
import jempasam.data.chunk.SimpleObjectChunk;
import jempasam.data.chunk.value.StringChunk;
import jempasam.logger.SLogger;
import jempasam.samstream.stream.SamStream;

public class StrobjoDataDeserializer extends AbstractDataDeserializer{
	
	
	
	private String openToken="(";
	private String closeToken=")";
	private String affectationToken=":";
	private String separatorToken=",";
	
	public String getOpenToken() { return openToken; }
	public String getCloseToken() { return closeToken; }
	public String getAffectationToken() { return affectationToken; }
	public String getSeparatorToken() { return separatorToken; }
	
	public void setOpenToken(String s) { openToken=s; }
	public void setCloseToken(String s) { closeToken=s; }
	public void setAffectationToken(String s) { affectationToken=s; }
	public void setSeparatorToken(String s) { separatorToken=s; }
	
	
	
	public StrobjoDataDeserializer(Function<InputStream, SamStream<String>> tokenizerSupplier, SLogger logger) {
		super(logger,tokenizerSupplier);
	}
		
	
	
	@Override
	public ObjectChunk loadFrom(InputStream input) {
		ObjectChunk ret=loadChunk(tokenizerSupplier.apply(input).buffered(5));
		ret.setName("root");
		return ret;
	}
	
	private ObjectChunk loadChunk(SamStream.BufferedSStream<String> tokenizer) {
		logger.enter().debug("new Object");
		ObjectChunk newchunk=new SimpleObjectChunk(null);
		
		String token;
		boolean endofobject=false;
		boolean endofparameter;
		
		List<String> names=new ArrayList<>();
		List<DataChunk> values=new ArrayList<>();
		int i=1;
		//Load names of parameter and their values
		while(!endofobject) {
			logger.enter().debug("In Parameter "+i+":");
			
			endofparameter=false;
			
			//LOAD NAMES
			while(true) {
				token=tokenizer.tryNext();
				//CLOSE OBJECT
				if(token==null || token.equals(closeToken)) {
					endofobject=true;
					break;
				}
				//CLOSE NAME LIST
				else if(token.equals(affectationToken)) {
					break;
				}
				//CLOSE PARAMETER
				else if(token.equals(separatorToken)) {
					endofparameter=true;
					break;
				}
				//ADD NAME
				else names.add(token);
			}
			logger.debug("names:"+names);
			
			//LOAD VALUES
			if(!endofobject && !endofparameter)
			while(true) {
				token=tokenizer.tryNext();
				//CLOSE OBJECT
				if(token==null || token.equals(closeToken)) {
					endofobject=true;
					break;
				}
				else if(token.equals(separatorToken)) {
					break;
				}
				else{
					tokenizer.back();
					DataChunk dc=loadDataChunkValue(tokenizer);
					if(dc!=null) {
						values.add(dc);
					}
				}
			}
			//CANCELING ERROR
			createDataChunks(names, values).forEach(newchunk::add);
			
			values.clear();
			names.clear();
			i++;
			logger.exit();
		}
		logger.debug("Result: "+newchunk);
		logger.exit();
		return newchunk;
	}
	
	private DataChunk loadDataChunkValue(SamStream.BufferedSStream<String> tokenizer) {
		String token;
		if((token=tokenizer.tryNext())!=null) {
			if(token.equals(openToken)) {
				logger.debug("As object");
				return loadChunk(tokenizer);
			}
			else{
				logger.debug("As value");
				logger.debug("="+token);
				return new StringChunk("", token);
			}
		}
		else return null;
	}
}
