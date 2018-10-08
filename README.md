# wrappy
[![Build Status](https://travis-ci.org/ClubObsidian/wrappy.svg?branch=master)](https://travis-ci.org/ClubObsidian/wrappy)
[![codecov](https://codecov.io/gh/ClubObsidian/wrappy/branch/master/graph/badge.svg)](https://codecov.io/gh/ClubObsidian/wrappy)
[![](https://jitpack.io/v/clubobsidian/wrappy.svg)](https://jitpack.io/#clubobsidian/wrappy)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A wrapper to simplify the usage of [configurate.](https://github.com/SpongePowered/configurate)

Supported formats
* Yaml
* Json
* Hocon


## Build Artifacts

Build artifacts are hosted via [Jitpack.](https://jitpack.io/#clubobsidian/wrappy/)

## Setting up as a dependency

### Gradle

```
repositories {
	maven { url 'https://jitpack.io' }
}

compile 'com.github.clubobsidian:wrappy:1.4.0'
```

### Maven

```
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>

<dependency>
	<groupId>com.github.clubobsidian</groupId>
	<artifactId>wrappy</artifactId>
	<version>1.4.0</version>
</dependency>
```

## Dependencies

* [configurate](https://github.com/SpongePowered/configurate)
  * Yaml
  * Json
  * Hocon

## Development

### Eclipse

1. Git clone the project
2. Generate eclipse files with `gradlew eclipse`
3. Import project

### Intellij

1. Git clone the project
2. Generate intellij files with `gradlew idea`
3. Import project

### Building

`gradlew shadowJar`