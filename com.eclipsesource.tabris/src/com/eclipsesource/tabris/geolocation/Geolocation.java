package com.eclipsesource.tabris.geolocation;

import static com.eclipsesource.tabris.geolocation.internal.GeolocationAdapter.NeedsPositionFlavor.CONTINUOUS;
import static com.eclipsesource.tabris.geolocation.internal.GeolocationAdapter.NeedsPositionFlavor.NEVER;
import static com.eclipsesource.tabris.geolocation.internal.GeolocationAdapter.NeedsPositionFlavor.ONCE;

import org.eclipse.rap.rwt.Adaptable;
import org.eclipse.rap.rwt.internal.protocol.ClientObjectAdapter;
import org.eclipse.rap.rwt.internal.protocol.IClientObjectAdapter;

import com.eclipsesource.tabris.geolocation.internal.GeolocationAdapter;
import com.eclipsesource.tabris.geolocation.internal.GeolocationSynchronizer;

/**
 * <p>
 * the <code>Geolocation</code> can be used to determine a client's geographical location. Location updates
 * can be received once or periodically. The clients needs to allow location services in the app.
 * </p>
 * 
 * @see GeolocationCallback
 * @see GeolocationOptions
 * @since 0.6
 */
@SuppressWarnings("restriction")
public class Geolocation implements Adaptable {
  
  private final GeolocationAdapter geolocationAdapter;
  private final ClientObjectAdapter clientObjectAdapter;
  
  public Geolocation() {
    clientObjectAdapter = new ClientObjectAdapter( "l" );
    new GeolocationSynchronizer( this );
    geolocationAdapter = new GeolocationAdapter();
    geolocationAdapter.setFlavor( NEVER );
  }
  
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
  public void getCurrentPosition( GeolocationCallback callback, GeolocationOptions options ) {
    geolocationAdapter.setFlavor( ONCE );
    geolocationAdapter.setOptions( options );
    geolocationAdapter.setCallback( callback );
  }

  /**
   * <p>
   * Instructs the client to determine and send the location periodically. The update interval needs to be configured in
   * the <code>GeolocationOptions</code> parameter.
   * </p>
   * 
   * @param callback The callback to call when the client answers. Must not be <code>null</code>.
   * @param options The configuration for determining the location. Must not be <code>null</code>.
   */
  public void watchPosition( GeolocationCallback callback, GeolocationOptions options ) {
    geolocationAdapter.setFlavor( CONTINUOUS );
    geolocationAdapter.setOptions( options );
    geolocationAdapter.setCallback( callback );
  }

  /**
   * <p>
   * Instructs the mobile device to stop sending location updates periodically.
   * </p>
   */
  public void clearWatch() {
    geolocationAdapter.setFlavor( NEVER );
    geolocationAdapter.setCallback( null );
    geolocationAdapter.setOptions( null );
  }
  
  /**
   * <p>
   * Destroys the camera object. Behaves the same like other SWT Widgets.
   * </p>
   */
  public void dispose() {
    geolocationAdapter.dispose();
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getAdapter( Class<T> adapter ) {
    T result = null;
    if( adapter == GeolocationAdapter.class ) {
      result = ( T )geolocationAdapter;
    } else if( adapter == IClientObjectAdapter.class ) {
      result = ( T )clientObjectAdapter;
    }
    return result;
  }

} 
