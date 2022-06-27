## Client Services

To allow access to device functionality Tabris makes use of [RAP Client Services](https://eclipse.org/rap/developers-guide/devguide.php?topic=client.html&version=2.2) most of the time. You can access a client service any time within the UI thread using this code:

```
ClientService service = RWT.getClient().getService( ClientService.class );
```

In this case, we would get an instance of the type `ClientService`. The method call would return `null` if no service of the needed type is registered. This can happen e.g. when you want to get a Tabris service but you access the application from the browser because RAP would use different `Client` implementation for it.

## Available Services

As already mentioned Tabris uses Client Services to make use of device functionality like camera, geolocation and many more. Currently, these services are available:

- **App:** Allows to register App events, set a badge
- **AppLauncher:** Allows to launch other apps on the device.
- **ClientDevice:** Provides device information like the platform, rotation…
- **ClientStore:** Allows to store data on the client’s device.
- **Camera:** Provides access to the device’s camera.
- **Geolocation:** Allows access to the device’s location.
- **Printer:** Allows printing resources using the client’s device printing capabilities.
- **CloudPush:** Allows registering a client to receive cloud push messages.
