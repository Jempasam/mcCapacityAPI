package jempasam.data.loader.factory;

import java.util.List;

import jempasam.data.chunk.ObjectChunk;

public interface IMultiFactory{
	List<Factory<?>> instantiate(Class<?> baseClass, String name) throws FactoryException;
}
