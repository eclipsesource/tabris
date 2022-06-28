## Building an Application

Tabris applications are just Java web applications. For this reason you can use whatever build technology you like. We offer a bunch of examples that can be used as a starting point.

## Maven Archetype

As you might know from the getting started guide we offer a Maven archetype called `tabris-application`. Creating a project from this archetype also generates a `pom` that will produce a deployable `war` file. Just step inside the created project and execute `mvn package`.

## Tycho, PDE Build... if OSGi == true

If you use Tabris in an OSGi environment we can recommend to take a look at the following build template:

- [Official Tabris Demo Build](https://github.com/eclipsesource/tabris-demos/blob/master/build/com.eclipsesource.tabris.demos.build/pom.xml)
- [RAP Build Examples](https://github.com/hstaudacher/org.eclipse.rap.build.examples)

## Articles on how to build RAP/Tabris applications

- [How to Build a RAP Application with Tycho](https://eclipsesource.com/blogs/2011/01/17/how-to-build-a-rap-application-with-tycho/)
- [How to Build a Server-Side Equinox/RAP Application](https://eclipsesource.com/blogs/2011/02/07/how-to-build-a-server-side-equinoxrap-application/)
- [Create a WAR from RAP Application with Libra WAR Product](https://angelozerr.wordpress.com/2012/06/12/warproduct_step1/)
