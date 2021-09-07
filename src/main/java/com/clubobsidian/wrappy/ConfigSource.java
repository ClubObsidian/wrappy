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

package com.clubobsidian.wrappy;

import org.spongepowered.configurate.loader.AbstractConfigurationLoader;

public abstract class ConfigSource<T> {

    private final T source;
    private final Class<T> clazz;

    public ConfigSource(T source) {
        this.source = source;
        this.clazz = (Class<T>) source.getClass();
    }

    protected T getSource() {
        return this.source;
    }

    public abstract void load(Configuration.BuilderOpts opts, AbstractConfigurationLoader.Builder<?,?> loader);
}