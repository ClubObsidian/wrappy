package com.clubobsidian.wrappy.helper;

import java.util.List;

import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class NodeHelper<T>
{
	public T get(ConfigurationNode node, Class<T> clazz)
	{
		try 
		{
			return node.getValue(TypeToken.of(clazz));
		} 
		catch (ObjectMappingException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public List<T> getList(ConfigurationNode node, Class<T> clazz)
	{
		try 
		{
			return node.getList(TypeToken.of(clazz));
		} 
		catch (ObjectMappingException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
}