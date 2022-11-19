package jempasam.data.doc;

import java.util.List;

import jempasam.data.chunk.DataChunk;
import jempasam.data.chunk.ObjectChunk;

public interface ObjectDocMaker {
	ObjectChunk getDocFor(List<Class<?>> classes);
}
