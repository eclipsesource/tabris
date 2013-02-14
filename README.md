Tabris
======

This repository contains the [Tabris](http://developer.eclipsesource.com/tabris/) server parts and additional features that are not contained in the [RAP core](http://eclipse.org/rap/).     

Additional Features
-------------------
* A Bootstraper that sets up the Tabris Client to registers themes and so on. 
* Geolocation API, [inspired by phonegap](http://docs.phonegap.com/en/1.4.1/phonegap_geolocation_geolocation.md.html#Geolocation)
* ClientCanvas, SWT Canvas extension to be able to draw on the client side.
* Video, Video Widget to play videos on a client device.
* Camera, Custom Widget to take pictures form the client's camera.
* Widgets API, API to control keyboard styles, animations, local touch behavior and so on.
* Implementation of the ClientInfo Interface provided by RAP 2.0.
* AppLauncher, API to launch other Apps on the device like Mail, Browser or Maps and others.
* Swipe, UI component based on Composites to allow swipping.
* GroupedEvents, API to add a listener to a group of widgets to emulate one widget.
* AppEvents, to detect events that happened within the app like moving to background and so on.
* ClientStore, to store data on the accessing device. Comparable with cookies for the browser.
* ClientDevice, to get information regarding the accessing device like ConnectionType or Orientation.
* Tabris UI, abstraction of common mobile UI concepts as core framework to develop apps.

Demos
-----
Demos using these features are located in the [tabris-demos repository](https://github.com/eclipsesource/rap-mobile-demos).

License
-------
The code is published under the terms of the [Eclipse Public License, version 1.0](http://www.eclipse.org/legal/epl-v10.html).
