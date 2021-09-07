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

public enum ConfigurationType {

	YAML,
	JSON,
	HOCON,
	XML,
	UNKNOWN;

	public static ConfigurationType fromString(final String name) {
		if(name != null) {
			if(name.endsWith(".yml")) {
				return YAML;
			} else if(name.endsWith(".conf")) {
				return HOCON;
			} else if(name.endsWith(".json")) {
				return JSON;
			} else if(name.endsWith(".xml")) {
				return XML;
			}
		}
		return UNKNOWN;
	}
}