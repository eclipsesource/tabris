## Sample Applications

We provide a whole palette of Example applications covering nearly all Tabris features. The [Tabris screencasts](https://eclipsesource.com/products/tabris/demos) are also made up from these demos.

## Clone the Repository

The first step is to clone the official Tabris demos repository:

`git clone git@github.com:eclipsesource/tabris-demos.git`

After the cloning was successful, you can import this project into Eclipse.

_Please note: we have used Eclipse to build the demos. If you donâ€™t want to install Eclipse, please jump directly to the Build section on this page._

## Setting up Eclipse

After importing the project, you need to set the target platform which is included in the project. The file is called [tabris.target](https://raw.githubusercontent.com/eclipsesource/tabris-demos/master/com.eclipsesource.tabris.demos/tabris.target). Just open the file in Eclipse and press the â€�set as target platformâ€� link in the top right.

## Run from within Eclipse

Open the "Run Configurations" menu entry from the "Run" menu in your Eclipse IDE and select "Tabris Demos" from the OSGi Framework element. Run the configuration. You should get a console message that a connector has started on port 9090. To ensure everything is working as expected, point your browser to:Â `https://localhost:9090/ui`. You should see a UI rendering inside the browser.

## Choose the demo

This demo application contains several Tabris demos at once. To access a specific one, you have to define the URL for it. The following applications are currently available:

- `https://SERVER:9090/input`
- `https://SERVER:9090/buttons`
- `https://SERVER:9090/simple-tree`
- `https://SERVER:9090/location`
- `https://SERVER:9090/draw`
- `https://SERVER:9090/launcher`
- `https://SERVER:9090/camera`
- `https://SERVER:9090/gallery`
- `https://SERVER:9090/video`
- `https://SERVER:9090/keyboard`
- `https://SERVER:9090/swipe`
- `https://SERVER:9090/appevents`
- `https://SERVER:9090/ui`
- `https://SERVER:9090/dynamic-ui`
- `https://SERVER:9090/device`

## Build

There are some build projects located in the build directory of the cloned repository. The build compiles the demo project into a war file that can be deployed on a servlet container. The build uses [Maven Tycho](https://eclipse.org/tycho/) and can be launched if Maven 3 is available. Just step to the `build/com.eclipsesource.tabris.demos.build` folder and run `Â mvn clean verify`. After the build has succeeded you will find the .war file within the `build/com.eclipsesource.tabris.demos.product/target folder`.

Alternatively, you can use the Eclipse m2e Tooling and run the launch configuration located in `build/com.eclipsesource.tabris.demos.build.`
