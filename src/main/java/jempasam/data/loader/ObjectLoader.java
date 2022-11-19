package jempasam.data.loader;

import jempasam.data.chunk.ObjectChunk;
import jempasam.objectmanager.ObjectManager;

public interface ObjectLoader<T> {
	void load(ObjectManager<? super T> manager, ObjectChunk data);
	T load(ObjectChunk data);
	void hydrate(T target, ObjectChunk hydrated);
}
