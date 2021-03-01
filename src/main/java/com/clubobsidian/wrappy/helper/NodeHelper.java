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

import com.clubobsidian.wrappy.util.NodeUtil;
import io.leangen.geantyref.TypeToken;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;


public class NodeHelper<T> {
	
	private ConfigurationNode node;
	
	public NodeHelper(ConfigurationNode node) {
		this.node = node;
	}
	
	public T get(String path, Class<T> clazz) {
		try {
			ConfigurationNode parsed = NodeUtil.parsePath(this.node, path);
			return parsed.get(TypeToken.get(clazz));
		} catch (SerializationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public T get(String path, Class<T> clazz, T defaultValue) {
		try {
			ConfigurationNode parsed = NodeUtil.parsePath(this.node, path);
			return parsed.get(TypeToken.get(clazz), defaultValue);
		} catch (SerializationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<T> getList(String path, Class<T> clazz) {
		try {
			ConfigurationNode parsed = NodeUtil.parsePath(this.node, path);
			return parsed.getList(TypeToken.get(clazz));
		} catch (SerializationException e) {
			e.printStackTrace();
		}
		return null;
	}
}