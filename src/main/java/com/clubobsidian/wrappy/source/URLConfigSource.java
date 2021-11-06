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

package com.clubobsidian.wrappy.source;

import com.clubobsidian.wrappy.ConfigSource;
import com.clubobsidian.wrappy.Configuration;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.Callable;

public class URLConfigSource extends ConfigSource<URL> {

    public URLConfigSource(URL url) {
        super(url);
    }

    @Override
    public ConfigurationLoader<?> load(Configuration.Options opts, AbstractConfigurationLoader.Builder<?, ?> builder) {
        try {
            URLConnection connection = this.getSource().openConnection();
            connection.setConnectTimeout(opts.getConnectTimeout());
            connection.setReadTimeout(opts.getReadTimeout());
            connection.setDoInput(true);
            connection.setUseCaches(false);
            for(Map.Entry<String, String> next : opts.getRequestProperties().entrySet()) {
                connection.setRequestProperty(next.getKey(), next.getValue());
            }
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            Callable<BufferedReader> callable = () -> reader;
            builder.source(callable);
            return builder.build();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}