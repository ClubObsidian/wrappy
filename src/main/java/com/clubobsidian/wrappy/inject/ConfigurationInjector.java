/*
 *    Copyright 2021 Club Obsidian and contributors.
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
package com.clubobsidian.wrappy.inject;

import com.clubobsidian.wrappy.ConfigHolder;
import com.clubobsidian.wrappy.ConfigurationSection;
import com.clubobsidian.wrappy.transformer.NodeTransformer;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ConfigurationInjector {

    private final ConfigurationSection config;
    private Object injectInto;

    public ConfigurationInjector(ConfigurationSection config, Object injectInto) {
        this.config = config;
        this.injectInto = injectInto;
    }

    public void inject(Collection<NodeTransformer> transformers) {
        Class<?> injectClazz;
        if(this.injectInto instanceof Class) {
            injectClazz = (Class<?>) injectInto;
            this.injectInto = null;
        } else {
            injectClazz = this.injectInto.getClass();
        }
        for(Field field: injectClazz.getDeclaredFields()) {
            for(Node node : field.getAnnotationsByType(Node.class)) {
                field.setAccessible(true);
                Class<?> fieldClazz = field.getType();
                String nodePath = node.value();
                Object nodeValue;
                if(nodePath.equals("%key%")) {
                    this.setField(field, this.config.getName());
                    break;
                }
                if (this.isConfigHolder(fieldClazz)) {
                    Object holder = this.getField(field);
                    ConfigurationSection pathSection = this.config.getConfigurationSection(nodePath);
                    new ConfigurationInjector(pathSection, holder).inject(transformers);
                    break;
                } else if(fieldClazz.equals(Map.class)) {
                    nodeValue = this.config.getMap(nodePath, node.type(), node.valueType());
                } else if(fieldClazz.equals(List.class)) {
                    nodeValue = this.config.getList(nodePath, node.type());
                } else {
                    nodeValue = this.config.get(nodePath, fieldClazz);
                }
                if(nodeValue != null) {
                    for (NodeTransformer transformer : transformers) {
                        if (transformer.getClassToTransform().equals(fieldClazz)) {
                            nodeValue = transformer.transform(nodeValue);
                        }
                    }
                }
                this.setField(field, nodeValue);
                break;
            }
        }
    }

    private Object getField(Field field) {
        try {
            return field.get(this.injectInto instanceof Class ? null : this.injectInto);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isConfigHolder(Class<?> clazz) {
        for (Class<?> i : clazz.getInterfaces()) {
            if (i.equals(ConfigHolder.class)) {
                return true;
            }
        }
        return false;
    }

    private void setField(Field field, Object nodeValue) {
        try {
            field.setAccessible(true);
            field.set(this.injectInto, nodeValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
