package com.eclipsesource.tabris.geolocation;

import org.eclipse.rap.rwt.client.Client;
import org.eclipse.rap.rwt.client.service.ClientService;

/**
 * <p>
 * the <code>Geolocation</code> can be used to determine a client's geographical location. Location updates
 * can be received once or periodically. The clients needs to allow location services in the app. An instance of
 * {@link Geolocation} can be accessed using RWT.getClient().getService( Geolocation.class ).
 * </p>
 *
 * @see GeolocationCallback
 * @see GeolocationOptions
 * @see Client
 *
 * @since 0.6
 */
public interface Geolocation extends ClientService {

  /**
   * <p>
   * Determines the client's location once. The callback will be called when the server recieves an answer from
   * the mobile device. Determining locations is coupled with a configuration passed in as
   * <code>GeolocationOptions</code>.
   * </p>
   *
   * @param callback The callback to call when the client answers. Must not be <code>null</code>.
   * @param options The configuration for determining the location. Must not be <code>null</code>.
   */
  void getCurrentPosition( GeolocationCallback callback, GeolocationOptions options );

  /**
   * <p>
   * Instructs the client to determine and send the location periodically. The update interval needs to be configured in
   * the <code>GeolocationOptions</code> parameter.
   * </p>
   *
   * @param callback The callback to call when the client answers. Must not be <code>null</code>.
   * @param options The configuration for determining the location. Must not be <code>null</code>.
   */
  void watchPosition( GeolocationCallback callback, GeolocationOptions options );

  /**
   * <p>
   * Instructs the mobile device to stop sending location updates periodically.
   * </p>
   */
  void clearWatch();

}
