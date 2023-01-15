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
package com.clubobsidian.wrappy.test.mock;

import com.clubobsidian.wrappy.ConfigHolder;
import com.clubobsidian.wrappy.inject.Node;

import java.time.DayOfWeek;
import java.util.List;

public class ConfigHolderStaticMock implements ConfigHolder {

    @Node("foo")
    public static String foo;

    @Node(value = "day-list", type = DayOfWeek.class)
    public static List<DayOfWeek> dayList;
}