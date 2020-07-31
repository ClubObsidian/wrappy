package com.clubobsidian.wrappy.helper;

import org.spongepowered.configurate.ConfigurationNode;

public class NodeUtil {

	public static ConfigurationNode parsePath(ConfigurationNode node, String path) {
		Object[] ar = new Object[1];
		if(path.contains(".")) {
			String[] split = path.split("\\.");
			ar = new Object[split.length];
			for(int i = 0; i < split.length; i++) {
				ar[i] = (Object) split[i];
			}
		} else {
			ar[0] = (Object) path;
		}
		return node.getNode(ar);
	}
}