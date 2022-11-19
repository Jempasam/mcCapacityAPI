package jempasam.data.deserializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import jempasam.data.chunk.ObjectChunk;
import jempasam.data.modifier.DataModifier;
import jempasam.data.modifier.StringDesindexingDataModifier;
import jempasam.data.modifier.StringIndexingDataModifier;
import jempasam.data.modifier.TemplateDataModifier;
import jempasam.data.serializer.DataSerializer;
import jempasam.data.serializer.LightDataSerializer;
import jempasam.logger.SLogger;
import jempasam.samstream.text.TokenizerConfig;
import jempasam.samstream.text.TokenizerSStream;

public class DataDeserializers {
	
	public static DataDeserializer createStrobjoDS(SLogger logger) {
		TokenizerConfig config=new TokenizerConfig();
		config.cutChars=" \n\r\t";
		config.uniqueChars=":(),";
		config.escapeAroundChars="\"'";
		config.commentChars="#";
		StrobjoDataDeserializer ret=new StrobjoDataDeserializer( (i)->new TokenizerSStream(i, config), logger );
		return ret;
	}
	
	public static DataDeserializer createJSONLikeStrobjoDS(SLogger logger) {
		TokenizerConfig config=new TokenizerConfig();
		config.cutChars=" \n\r\t";
		config.uniqueChars=":{},";
		config.escapeAroundChars="\"'";
		StrobjoDataDeserializer ret=new StrobjoDataDeserializer( (i)->new TokenizerSStream(i, config), logger );
		ret.setCloseToken("}");
		ret.setOpenToken("{");
		ret.setSeparatorToken(",");
		ret.setAffectationToken(":");
		return ret;
	}
	
	public static DataDeserializer createURLEncodedDS(SLogger logger) {
		TokenizerConfig config=new TokenizerConfig();
		config.cutChars="";
		config.uniqueChars="&.=";
		config.escapeAroundChars="\"'";
		config.escapeChars="\\";
		config.commentChars="#";
		FormURLEncodedDataDeserializer ret=new FormURLEncodedDataDeserializer( (i)->new TokenizerSStream(i, config), logger );
		ret.assignementToken="=";
		ret.memberToken=".";
		ret.separatorToken="&";
		return ret;
	}
	
	public static DataDeserializer createYAMLLikeChardentDS(SLogger logger) {
		TokenizerConfig config=new TokenizerConfig();
		config.cutChars=" \r";
		config.uniqueChars=":\n\t";
		config.escapeAroundChars="\"'";
		config.commentChars="#";
		ChardentDataDeserializer ret=new ChardentDataDeserializer( config::create, logger );
		ret.setAffectationToken(":");
		ret.setIndentorToken("\t");
		ret.setSeparatorToken("\n");
		return ret;
	}
	
	public static DataDeserializer createIndentedBaliseDS(SLogger logger) {
		TokenizerConfig config=new TokenizerConfig();
		config.cutChars=" \r";
		config.uniqueChars=":\t(),\n";
		config.escapeAroundChars="\"'";
		config.commentChars="#";
		ChardentBaliseDataDeserializer ret=new ChardentBaliseDataDeserializer( config::create, logger );
		ret.affectationToken=":";
		ret.openToken="(";
		ret.closeToken=")";
		ret.separatorToken=",";
		ret.indentorToken="\t";
		return ret;
	}
	
	public static DataDeserializer createSquareIndentedBaliseDS(SLogger logger) {
		TokenizerConfig config=new TokenizerConfig();
		config.cutChars=" \r";
		config.uniqueChars=":\t[],\n";
		config.escapeAroundChars="\"'";
		config.commentChars="#";
		ChardentBaliseDataDeserializer ret=new ChardentBaliseDataDeserializer( config::create, logger );
		ret.affectationToken=":";
		ret.openToken="[";
		ret.closeToken="]";
		ret.separatorToken=",";
		ret.indentorToken="\t";
		return ret;
	}
	
	public static DataDeserializer createSGMLLikeBaliseDS(SLogger logger) {
		TokenizerConfig config=new TokenizerConfig();
		config.cutChars=" \n\r\t";
		config.uniqueChars="<>=;/";
		config.escapeAroundChars="\"'";
		config.commentChars="#";
		BaliseDataDeserializer ret=new BaliseDataDeserializer( (i)->new TokenizerSStream(i, config), logger );
		return ret;
	}
	
	public static DataDeserializer createBoxLikeBaliseDS(SLogger logger) {
		TokenizerConfig config=new TokenizerConfig();
		config.cutChars=" \n\r\t";
		config.uniqueChars="[:=,]";
		config.escapeAroundChars="\"'";
		config.commentChars="#";
		BaliseDataDeserializer ret=new BaliseDataDeserializer( (i)->new TokenizerSStream(i, config), logger );
		ret.setPermissive(true);
		ret.setSeparatorToken(",");
		ret.setCloseBaliseToken(":");
		ret.setOpenBaliseToken("[");
		ret.setEndBaliseToken("]");
		return ret;
	}
	
	public static DataDeserializer createStructLikeStrobjoDS(SLogger logger) {
		TokenizerConfig config=new TokenizerConfig();
		config.cutChars=" \n\r\t";
		config.uniqueChars="{}=;";
		config.escapeAroundChars="\"'";
		config.commentChars="#";
		StrobjoDataDeserializer ret=new StrobjoDataDeserializer( (i)->new TokenizerSStream(i, config), logger );
		ret.setCloseToken("}");
		ret.setOpenToken("{");
		ret.setSeparatorToken(";");
		ret.setAffectationToken("=");
		return ret;
	}
	
	public static TemplateDataModifier createVerboseTemplateDM(SLogger logger) {
		return new TemplateDataModifier(logger, "@template-", "-", "..", "@");
	}
	
	public static TemplateDataModifier createLightTemplateDM(SLogger logger) {
		return new TemplateDataModifier(logger, "@@", "-", "..", "@");
	}
	
	public static DataDeserializer createCompleteTemplateJsonLikeDS(SLogger logger) {
		TemplateDataModifier templateDM=createLightTemplateDM(logger);
		HashMap<String, String> vars=new HashMap<>();
		templateDM.setVariableHolder(vars);
		
		DataDeserializer deserializer=createJSONLikeStrobjoDS(logger);
		ModifiersDataDeserializer templated=new ModifiersDataDeserializer(deserializer,Arrays.asList(templateDM));
		OnLoadDataDeserializer onload=new OnLoadDataDeserializer(templated);
		
		onload.register((dd)->{
			vars.put("time", DateTimeFormatter.ISO_LOCAL_TIME.format(LocalTime.now()));
			vars.put("date", DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now()));
			vars.put("name", System.getProperty("user.name"));
		});
		return onload;
	}
	
	public static void saveTo(OutputStream output, ObjectChunk data) throws IOException{
		DataOutputStream writer=new DataOutputStream(output);
		
		// Index Strings
		List<String> indexeds=new ArrayList<>();
		DataModifier indexer=new StringIndexingDataModifier(indexeds);
		ObjectChunk towrite=indexer.apply(data);
		
		// Write Index
		writer.writeChar(indexeds.size());
		for(String str : indexeds) {
			writer.writeChar(str.getBytes().length);
			writer.writeBytes(str);
		}
		
		// Write Data
		DataSerializer serializer=new LightDataSerializer();
		serializer.write(writer, towrite);
	}
	
	public static ObjectChunk loadFrom(InputStream input) throws IOException{
		DataInputStream reader=new DataInputStream(input);
		
		// Load Index
		List<String> indexeds=new ArrayList<>();
		int indexsize=reader.readChar();
		for(int i=0; i<indexsize; i++) {
			int strsize=reader.readChar();
			byte[] bytes=new byte[strsize];
			reader.read(bytes);
			indexeds.add(new String(bytes));
		}
		
		// Load Data
		DataModifier desindexer=new StringDesindexingDataModifier(indexeds);
		DataDeserializer deserializer=new LightDataDeserializer();
		ObjectChunk ret=deserializer.loadFrom(input);
		desindexer.applyOn(ret);
		return ret;
	}
	
	public static void oldSaveTo(OutputStream output, ObjectChunk data) throws IOException{
		DataSerializer serializer=new LightDataSerializer();
		serializer.write(output, data);
	}
	
	public static ObjectChunk oldLoadFrom(InputStream input) throws IOException{
		DataDeserializer deserializer=new LightDataDeserializer();
		return deserializer.loadFrom(input);
	}
}
