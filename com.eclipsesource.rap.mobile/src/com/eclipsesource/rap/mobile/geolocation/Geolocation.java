package com.eclipsesource.rap.mobile.geolocation;

import static com.eclipsesource.rap.mobile.geolocation.internal.GeolocationAdapter.NeedsPositionFlavor.CONTINUOUS;
import static com.eclipsesource.rap.mobile.geolocation.internal.GeolocationAdapter.NeedsPositionFlavor.NEVER;
import static com.eclipsesource.rap.mobile.geolocation.internal.GeolocationAdapter.NeedsPositionFlavor.ONCE;

import org.eclipse.rwt.Adaptable;

import com.eclipsesource.rap.mobile.geolocation.internal.GeolocationAdapter;
import com.eclipsesource.rap.mobile.geolocation.internal.GeolocationSynchronizer;

/**
 * Geolocation API, more information at 
 * http://docs.phonegap.com/en/1.4.1/phonegap_geolocation_geolocation.md.html#Geolocation
 */
public class Geolocation implements Adaptable {
  
  private final GeolocationAdapter geolocationAdapter;
  
  public Geolocation() {
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

  @SuppressWarnings("unchecked")
  public <T> T getAdapter( Class<T> adapter ) {
    T result = null;
    if( adapter == GeolocationAdapter.class ) {
      result = ( T )geolocationAdapter;
    }
    return result;
  }

} 
