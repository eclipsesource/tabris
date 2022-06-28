## Building Tabris from Source

Building Tabris from source is pretty easy. You first need to clone the repository:
`git clone git@github.com:eclipsesource/tabris.git`

Afterwards, you can step into the build project and execute the maven build.

`cd tabris/com.eclipsesource.tabris.build`
`mvn clean verify`

After a successful build you will find the artifacts in `tabris/com.eclipsesource.tabris.repository/target/.`
