# Tabris [![Build Status](https://travis-ci.org/eclipsesource/tabris.png)](https://travis-ci.org/eclipsesource/tabris)

This repository contains the [Tabris](https://github.com/eclipsesource/tabris/blob/master/com.eclipsesource.tabris.documentation/public/home.md) server parts and additional features that are not contained in the [RAP core](http://eclipse.org/rap/).

Tabris for Eclipse RAP provides two components: a target platform for your server-side code, and Tabris App Launcher for App / Play Store. You can download them [here](https://github.com/eclipsesource/tabris/blob/master/com.eclipsesource.tabris.documentation/public/doc/downloads.md).

It is free to develop a component and test your application with our generic app launcher. To build and publish your own app, you need access to the [Tabris.js registry](https://npm.tabrisjs.com/) under a [commercial license](https://eclipsesource.com/products/tabris/pricing/).

## Documentation
A detailed Developer Guide incl. JavaDoc can be found on the [official project documentation site](https://github.com/eclipsesource/tabris/blob/master/com.eclipsesource.tabris.documentation/public/doc/index.md).

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
* **release:** `http://download.eclipsesource.com/technology/tabris/downloads/releases` Contains the latest releases.
* **staging:** `http://download.eclipsesource.com/technology/tabris/downloads/staging` Can be used as a stable target. We will not purge any version. Contains picked nightly builds.
* **nightly:** `http://download.eclipsesource.com/technology/tabris/downloads/nightly` Contains the last 10 nightly builds. Older builds will be purged.

## Demos
Demos using the Tabris features are located in the [tabris-demos repository](https://github.com/eclipsesource/tabris-demos).

## Build from Source
Tabris uses an Eclipse Tycho based Maven build. Detailed instructions you ca find in the [documentation](https://github.com/eclipsesource/tabris/blob/master/com.eclipsesource.tabris.documentation/public/doc/building-tabris-from-source.md).

## License
The server-side code is published under the terms of the [Eclipse Public License, version 1.0](http://www.eclipse.org/legal/epl-v10.html).

The client-side [Tabris.js](https://tabris.com/) is free.

To build and publish your own app, a commercial license is required.

Tabris for Eclipse RAP is free for open-source and academic projects.
