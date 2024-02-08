/*
 *    Copyright 2018-2024 virustotalop
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.clubobsidian.wrappy.util;

import org.spongepowered.configurate.ConfigurationNode;

public final class NodeUtil {

    private NodeUtil() {
    }

    public static ConfigurationNode parsePath(ConfigurationNode node, Object path) {
        if (!(path instanceof String)) {
            return node.node(path);
        }
        String pathStr = (String) path;
        Object[] ar = new Object[1];
        if (pathStr.contains(".")) {
            String[] split = pathStr.split("\\.");
            ar = new Object[split.length];
            for (int i = 0; i < split.length; i++) {
                ar[i] = split[i];
            }
        } else {
            ar[0] = path;
        }
        return node.node(ar);
    }
}