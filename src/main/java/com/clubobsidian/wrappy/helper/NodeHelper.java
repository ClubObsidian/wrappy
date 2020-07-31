/*  
   Copyright 2020 Club Obsidian and contributors.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
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