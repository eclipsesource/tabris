package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.internal.Preconditions.checkArgumentNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.remote.ConnectionImpl;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;

import com.eclipsesource.tabris.geolocation.Coordinates;
import com.eclipsesource.tabris.geolocation.Geolocation;
import com.eclipsesource.tabris.geolocation.GeolocationCallback;
import com.eclipsesource.tabris.geolocation.GeolocationOptions;
import com.eclipsesource.tabris.geolocation.Position;
import com.eclipsesource.tabris.geolocation.PositionError;
import com.eclipsesource.tabris.geolocation.PositionError.PositionErrorCode;


@SuppressWarnings("restriction")
public class GeolocationImpl extends AbstractOperationHandler implements Geolocation {

  private static final String TYPE = "tabris.Geolocation";
  private static final String PROP_ENABLE_HIGH_ACCURACY = "enableHighAccuracy";
  private static final String PROP_MAXIMUM_AGE = "maximumAge";
  private static final String PROP_FREQUENCY = "frequency";
  private static final String PROP_NEEDS_POSITION = "needsPosition";
  private static final String LOCATION_UPDATE_ERROR_EVENT = "LocationUpdateError";
  private static final String LOCATION_UPDATE_EVENT = "LocationUpdate";
  private static final String PROP_TIMESTAMP = "timestamp";
  private static final String PROP_SPEED = "speed";
  private static final String PROP_HEADING = "heading";
  private static final String PROP_ALTITUDE_ACCURACY = "altitudeAccuracy";
  private static final String PROP_ACCURACY = "accuracy";
  private static final String PROP_ALTITUDE = "altitude";
  private static final String PROP_LONGITUDE = "longitude";
  private static final String PROP_LATITUDE = "latitude";
  private static final String PROP_ERROR_MESSAGE = "errorMessage";
  private static final String PROP_ERROR_CODE = "errorCode";

  private enum NeedsPositionFlavor {
    NEVER, ONCE, CONTINUOUS
  }

  private final RemoteObject remoteObject;
  private GeolocationCallback callback;

  public GeolocationImpl() {
    remoteObject = ( ( ConnectionImpl )RWT.getUISession().getConnection() ).createServiceObject( TYPE );
    remoteObject.setHandler( this );
    remoteObject.set( PROP_NEEDS_POSITION, NeedsPositionFlavor.NEVER.toString() );
  }

  @Override
  public void handleNotify( String event, Map<String,Object> properties ) {
    checkCallback();
    if( LOCATION_UPDATE_EVENT.equals( event ) ) {
      callback.onSuccess( getPosition( properties ) );
    } else if( LOCATION_UPDATE_ERROR_EVENT.equals( event ) ) {
      callback.onError( getPositionError( properties ) );
    }
  }

  private void checkCallback() {
    if( callback == null ) {
      throw new IllegalStateException( "Callback must not be null" );
    }
  }

  private Position getPosition( Map<String, Object> properties ) {
    String timestampValue = ( String )properties.get( PROP_TIMESTAMP );
    try {
      Date timestamp = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ" ).parse( timestampValue );
      Coordinates coordinates = getCoordinates( properties );
      Position position = new Position( coordinates, timestamp );
      return position;
    } catch( ParseException e ) {
      throw new IllegalStateException( "Could not parse date from string: " +  timestampValue
                                       + ", needs to be yyyy-MM-dd'T'HH:mm:ssZ" );
    }
  }

  private Coordinates getCoordinates( Map<String, Object> properties ) {
    return new Coordinates( getPropertyAsDouble( properties, PROP_LATITUDE ),
                            getPropertyAsDouble( properties, PROP_LONGITUDE ),
                            getPropertyAsDouble( properties, PROP_ALTITUDE ),
                            getPropertyAsDouble( properties, PROP_ACCURACY ),
                            getPropertyAsDouble( properties, PROP_ALTITUDE_ACCURACY ),
                            getPropertyAsDouble( properties, PROP_HEADING ),
                            getPropertyAsDouble( properties, PROP_SPEED ) );
  }

  private double getPropertyAsDouble( Map<String, Object> properties, String propertyName ) {
    double result = -1;
    Object value = properties.get( propertyName );
    if( value instanceof Double ) {
      result = ( ( Double )value ).doubleValue();
    } else if( value instanceof Integer ) {
      result = ( ( Integer )value ).doubleValue();
    }
    return result;
  }

  private PositionError getPositionError( Map<String, Object> properties ) {
    String code = ( String )properties.get( PROP_ERROR_CODE );
    PositionErrorCode errorCode = PositionErrorCode.valueOf( code );
    String message = ( String )properties.get( PROP_ERROR_MESSAGE );
    PositionError positionError = new PositionError( errorCode, message );
    return positionError;
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
  @Override
  public void getCurrentPosition( GeolocationCallback callback, GeolocationOptions options ) {
    startUpdatePosition( NeedsPositionFlavor.ONCE, callback, options );
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
  @Override
  public void watchPosition( GeolocationCallback callback, GeolocationOptions options ) {
    startUpdatePosition( NeedsPositionFlavor.CONTINUOUS, callback, options );
  }

  private void startUpdatePosition( NeedsPositionFlavor flavor,
                                    GeolocationCallback callback,
                                    GeolocationOptions options )
  {
    checkArgumentNotNull( callback, "Callback" );
    checkArgumentNotNull( options, "Options" );
    this.callback = callback;
    remoteObject.set( PROP_NEEDS_POSITION, flavor.toString() );
    setOptions( options );
  }

  private void setOptions( GeolocationOptions options ) {
    remoteObject.set( PROP_FREQUENCY, options.getFrequency() );
    remoteObject.set( PROP_MAXIMUM_AGE, options.getMaximumAge() );
    remoteObject.set( PROP_ENABLE_HIGH_ACCURACY, options.isEnableHighAccuracy() );
  }

  /**
   * <p>
   * Instructs the mobile device to stop sending location updates periodically.
   * </p>
   */
  @Override
  public void clearWatch() {
    remoteObject.set( PROP_NEEDS_POSITION, NeedsPositionFlavor.NEVER.toString() );
    this.callback = null;
  }

  RemoteObject getRemoteObject() {
    return remoteObject;
  }

}
