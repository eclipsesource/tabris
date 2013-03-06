package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.internal.Preconditions.checkArgumentNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.remote.ConnectionImpl;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;

import com.eclipsesource.tabris.geolocation.Coordinates;
import com.eclipsesource.tabris.geolocation.Geolocation;
import com.eclipsesource.tabris.geolocation.GeolocationListener;
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
  private final List<GeolocationListener> listeners;

  public GeolocationImpl() {
    remoteObject = ( ( ConnectionImpl )RWT.getUISession().getConnection() ).createServiceObject( TYPE );
    remoteObject.setHandler( this );
    remoteObject.set( PROP_NEEDS_POSITION, NeedsPositionFlavor.NEVER.toString() );
    listeners = new ArrayList<GeolocationListener>();
  }

  @Override
  public void handleNotify( String event, Map<String,Object> properties ) {
    if( LOCATION_UPDATE_EVENT.equals( event ) ) {
      Position position = getPosition( properties );
      notifyListenersWithPosition( position );
    } else if( LOCATION_UPDATE_ERROR_EVENT.equals( event ) ) {
      PositionError error = getPositionError( properties );
      notifyListenersWithError( error );
    }
  }

  private void notifyListenersWithPosition( Position position ) {
    for( GeolocationListener listener : listeners ) {
      listener.positionReceived( position );
    }
  }

  private void notifyListenersWithError( PositionError error ) {
    for( GeolocationListener listener : listeners ) {
      listener.errorReceived( error );
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

  @Override
  public void determineCurrentPosition( GeolocationOptions options ) {
    startUpdatePosition( NeedsPositionFlavor.ONCE, options );
  }

  @Override
  public void watchPosition( GeolocationOptions options ) {
    startUpdatePosition( NeedsPositionFlavor.CONTINUOUS, options );
  }

  private void startUpdatePosition( NeedsPositionFlavor flavor, GeolocationOptions options ) {
    checkArgumentNotNull( options, "Options" );
    remoteObject.set( PROP_NEEDS_POSITION, flavor.toString() );
    setOptions( options );
  }

  private void setOptions( GeolocationOptions options ) {
    remoteObject.set( PROP_FREQUENCY, options.getFrequency() );
    remoteObject.set( PROP_MAXIMUM_AGE, options.getMaximumAge() );
    remoteObject.set( PROP_ENABLE_HIGH_ACCURACY, options.isEnableHighAccuracy() );
  }

  @Override
  public void clearWatch() {
    remoteObject.set( PROP_NEEDS_POSITION, NeedsPositionFlavor.NEVER.toString() );
  }

  RemoteObject getRemoteObject() {
    return remoteObject;
  }

  @Override
  public void addGeolocationListener( GeolocationListener listener ) {
    checkArgumentNotNull( listener, GeolocationListener.class.getSimpleName() );
    listeners.add( listener );
  }

  @Override
  public void removeGeolocationListener( GeolocationListener listener ) {
    checkArgumentNotNull( listener, GeolocationListener.class.getSimpleName() );
    listeners.remove( listener );
  }

}
