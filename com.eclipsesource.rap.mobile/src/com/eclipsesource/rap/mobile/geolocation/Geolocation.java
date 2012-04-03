package com.eclipsesource.rap.mobile.geolocation;

import static com.eclipsesource.rap.mobile.geolocation.internal.GeolocationAdapter.NeedsPositionFlavor.CONTINUOUS;
import static com.eclipsesource.rap.mobile.geolocation.internal.GeolocationAdapter.NeedsPositionFlavor.NEVER;
import static com.eclipsesource.rap.mobile.geolocation.internal.GeolocationAdapter.NeedsPositionFlavor.ONCE;

import org.eclipse.rwt.Adaptable;
import org.eclipse.rwt.internal.protocol.ClientObjectAdapter;
import org.eclipse.rwt.internal.protocol.IClientObjectAdapter;

import com.eclipsesource.rap.mobile.geolocation.internal.GeolocationAdapter;
import com.eclipsesource.rap.mobile.geolocation.internal.GeolocationSynchronizer;

/**
 * Geolocation API, more information at 
 * http://docs.phonegap.com/en/1.4.1/phonegap_geolocation_geolocation.md.html#Geolocation
 */
@SuppressWarnings("restriction")
public class Geolocation implements Adaptable {
  
  private final GeolocationAdapter geolocationAdapter;
  private ClientObjectAdapter clientObjectAdapter;
  
  public Geolocation() {
    clientObjectAdapter = new ClientObjectAdapter( "l" );
    new GeolocationSynchronizer( this );
    geolocationAdapter = new GeolocationAdapter();
    geolocationAdapter.setFlavor( NEVER );
  }
  
  public void getCurrentPosition( GeolocationCallback callback, GeolocationOptions options ) {
    geolocationAdapter.setFlavor( ONCE );
    geolocationAdapter.setOptions( options );
    geolocationAdapter.setCallback( callback );
  }

  public void watchPosition( GeolocationCallback callback, GeolocationOptions options ) {
    geolocationAdapter.setFlavor( CONTINUOUS );
    geolocationAdapter.setOptions( options );
    geolocationAdapter.setCallback( callback );
  }

  public void clearWatch() {
    geolocationAdapter.setFlavor( NEVER );
    geolocationAdapter.setCallback( null );
    geolocationAdapter.setOptions( null );
  }
  
  public void dispose() {
    geolocationAdapter.dispose();
  }

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
