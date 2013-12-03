# Tabris [![Build Status](https://travis-ci.org/eclipsesource/tabris.png)](https://travis-ci.org/eclipsesource/tabris)

This repository contains the [Tabris](http://developer.eclipsesource.com/tabris/) server parts and additional features that are not contained in the [RAP core](http://eclipse.org/rap/).  

## Documentation
A detailed [Developer Guide](http://developer.eclipsesource.com/tabris/docs/1.2/) incl. [JavaDoc](http://developer.eclipsesource.com/tabris/docs/1.2/javadoc/) can be found on the [official project documentation site](http://developer.eclipsesource.com/tabris/docs/).   

## Tabris via Maven
Tabris is just a single jar and can be obtained from Maven:
```xml
<dependency>
    <groupId>com.eclipsesource.tabris</groupId>
    <artifactId>tabris</artifactId>   
</dependency>
```
You can also use the Tabris archetype to create Tabris applications:
```sh
mvn archetype:generate -DarchetypeGroupId=com.eclipsesource.tabris -DarchetypeArtifactId=tabris-application -DgroupId=app -DartifactId=app -DpackageName=app -Dversion=0.1-SNAPSHOT -DinteractiveMode=false
```

## Tabris via p2 Software Sites
The following sites can be used to consume Tabris as an Eclipse Target Platform.  
* **release:** `http://download.eclipsesource.com/technology/tabris/downloads/releases` Contains a the latest releases.  
* **staging:** `http://download.eclipsesource.com/technology/tabris/downloads/staging` Can be used as a stable target. We will not purge any version. Contains picked nightly builds.  
* **nightly:** `http://download.eclipsesource.com/technology/tabris/downloads/nightly` Contains the last 10 nightly builds.

## Demos
Demos using the Tabris features are located in the [tabris-demos repository](https://github.com/eclipsesource/tabris-demos).

## Build from Source
Tabris uses an Eclipse Tycho based Maven build. Detailed instructions are in the [Developer Guide](http://developer.eclipsesource.com/tabris/docs/1.2/hacking/build/).

## License
The code is published under the terms of the [Eclipse Public License, version 1.0](http://www.eclipse.org/legal/epl-v10.html).
