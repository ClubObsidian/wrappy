# wrappy
[![build](https://github.com/ClubObsidian/wrappy/workflows/build/badge.svg)](https://github.com/ClubObsidian/wrappy/actions?query=workflow%3Abuild)
[![](https://jitpack.io/v/clubobsidian/wrappy.svg)](https://jitpack.io/#clubobsidian/wrappy)
[![codecov](https://codecov.io/gh/ClubObsidian/wrappy/branch/master/graph/badge.svg)](https://codecov.io/gh/ClubObsidian/wrappy)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/0c01276584f342eba75d5aaa71c85240)](https://www.codacy.com/app/virustotalop/wrappy?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ClubObsidian/wrappy&amp;utm_campaign=Badge_Grade)
[![Known Vulnerabilities](https://snyk.io/test/github/ClubObsidian/wrappy/badge.svg?targetFile=build.gradle)](https://snyk.io/test/github/ClubObsidian/wrappy?targetFile=build.gradle)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A wrapper to simplify the usage of [configurate.](https://github.com/SpongePowered/configurate)

Supported formats
* Yaml
* Json
* Hocon
* XML

## Build Artifacts

Build artifacts are hosted via [Jitpack.](https://jitpack.io/#clubobsidian/wrappy/)

## Setting up as a dependency

### Gradle

``` groovy
repositories {
	maven { url 'https://jitpack.io' }
	maven { url 'https://repo.spongepowered.org/maven' }
}

compile 'com.github.clubobsidian:wrappy:2.0.0'
```

### Maven

``` xml
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
	<repository>
		<id>sponge</id>
		<url>https://repo.spongepowered.org/maven</url>
	</repository>
</repositories>

<dependency>
	<groupId>com.github.clubobsidian</groupId>
	<artifactId>wrappy</artifactId>
	<version>2.0.0</version>
</dependency>
```

## Dependencies

* [configurate](https://github.com/SpongePowered/configurate)
  * Yaml
  * Json
  * Hocon
  * XML

## Development

### Eclipse

1.  Git clone the project
2.  Generate eclipse files with `gradlew eclipse`
3.  Import project
4.  Right click project -> Add gradle nature

### Intellij

1.  Git clone the project
2.  Generate intellij files with `gradlew idea`
3.  Import project

### Building

`gradlew shadowJar`
