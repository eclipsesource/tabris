## Client Binding

By default each Tabris client can talk to every Tabris server. While this is very comfortable during development time it's often needed to bind a client to a specific type of server. With type we don't mean the `Server URL`. The type is something an application needs to define.

## Creating a Binding

When registering your application on the server, you are using the `TabrisClientInstaller`. There are two `install` methods on it. One is taking a second argument called serverId. This id will be transfered to the client during the first request. The client (App) also needs to define this id to allow the connection. This can look like this:

`TabrisClientInstaller.install( application, "myTabrisApp" );`

In this snippet we define the id `myTabrisApp`. After instructing the server to use this id it only allows clients that are sending this id. See the client launchers documents how to configure this id on the client side ([iOS](/?page_id=35960)/[Android](/?page_id=35966)).

## Disable the Tabris Version Check

By default only Tabris clients can connect to a server that uses the same version e.g. a 1.3 Server and a 1.3 Client. With this we can guarentee that the accessing client is working as expected. Anyway, during development time it's sometimes useful to disable this check. This can be done on the server by setting this system property:

`-Dcom.eclipsesource.tabris.version.check=false`

## Custom Version Check

In some cases the default version check of Tabris is not enough e.g. if you want to have a strict or range based comparison. For this reason you can set a custom `VersionCheck` on the `TabrisClientInstaller`:

```
TabrisClientInstaller.setVersionCheck( new VersionCheck() {

  @Override
  public boolean accept( String clientVersion, String serverVersion ) {
    return clientVersion.equals( serverVersion );
  }

  @Override
  public String getErrorMessage( String clientVersion, String serverVersion ) {
    return "You shall not pass!";
  }
} );
```
