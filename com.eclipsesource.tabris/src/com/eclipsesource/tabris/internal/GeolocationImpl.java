package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.internal.Constants.EVENT_LOCATION_UPDATE_ERROR_EVENT;
import static com.eclipsesource.tabris.internal.Constants.EVENT_LOCATION_UPDATE_EVENT;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ACCURACY;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ALTITUDE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ALTITUDE_ACCURACY;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ERROR_CODE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ERROR_MESSAGE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_FREQUENCY;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_HEADING;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_HIGH_ACCURACY;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_LATITUDE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_LONGITUDE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_MAXIMUM_AGE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_NEEDS_POSITION;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_SPEED;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_TIMESTAMP;
import static com.eclipsesource.tabris.internal.Constants.TYPE_GEOLOCATION;
import static com.eclipsesource.tabris.internal.Preconditions.checkArgumentNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
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

  private enum NeedsPositionFlavor {
    NEVER, ONCE, CONTINUOUS
  }

  private final RemoteObject remoteObject;
  private final List<GeolocationListener> geolocationListeners;

  public GeolocationImpl() {
    remoteObject = ( ( ConnectionImpl )RWT.getUISession().getConnection() ).createServiceObject( TYPE_GEOLOCATION );
    remoteObject.setHandler( this );
    remoteObject.set( PROPERTY_NEEDS_POSITION, NeedsPositionFlavor.NEVER.toString() );
    geolocationListeners = new ArrayList<GeolocationListener>();
  }

  @Override
  public void handleNotify( String event, JsonObject properties ) {
    if( EVENT_LOCATION_UPDATE_EVENT.equals( event ) ) {
      Position position = getPosition( properties );
      notifyListenersWithPosition( position );
    } else if( EVENT_LOCATION_UPDATE_ERROR_EVENT.equals( event ) ) {
      PositionError error = getPositionError( properties );
      notifyListenersWithError( error );
    }
  }

  private void notifyListenersWithPosition( Position position ) {
    List<GeolocationListener> listeners = new ArrayList<GeolocationListener>( geolocationListeners );
    for( GeolocationListener listener : listeners ) {
      listener.positionReceived( position );
    }
  }

  private void notifyListenersWithError( PositionError error ) {
    List<GeolocationListener> listeners = new ArrayList<GeolocationListener>( geolocationListeners );
    for( GeolocationListener listener : listeners ) {
      listener.errorReceived( error );
    }
  }

  private Position getPosition( JsonObject properties ) {
    String timestampValue = properties.get( PROPERTY_TIMESTAMP ).asString();
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

  private Coordinates getCoordinates( JsonObject properties ) {
    return new Coordinates( getPropertyAsDouble( properties, PROPERTY_LATITUDE ),
                            getPropertyAsDouble( properties, PROPERTY_LONGITUDE ),
                            getPropertyAsDouble( properties, PROPERTY_ALTITUDE ),
                            getPropertyAsDouble( properties, PROPERTY_ACCURACY ),
                            getPropertyAsDouble( properties, PROPERTY_ALTITUDE_ACCURACY ),
                            getPropertyAsDouble( properties, PROPERTY_HEADING ),
                            getPropertyAsDouble( properties, PROPERTY_SPEED ) );
  }

  private double getPropertyAsDouble( JsonObject properties, String propertyName ) {
    double result = -1;
    JsonValue value = properties.get( propertyName );
    if( value.isNumber() ) {
      result = value.asDouble();
    }
    return result;
  }

  private PositionError getPositionError( JsonObject properties ) {
    String code = properties.get( PROPERTY_ERROR_CODE ).asString();
    PositionErrorCode errorCode = PositionErrorCode.valueOf( code );
    String message = properties.get( PROPERTY_ERROR_MESSAGE ).asString();
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
    remoteObject.set( PROPERTY_NEEDS_POSITION, flavor.toString() );
    setOptions( options );
  }

  private void setOptions( GeolocationOptions options ) {
    remoteObject.set( PROPERTY_FREQUENCY, options.getFrequency() );
    remoteObject.set( PROPERTY_MAXIMUM_AGE, options.getMaximumAge() );
    remoteObject.set( PROPERTY_HIGH_ACCURACY, options.isHighAccuracyEnabled() );
  }

  @Override
  public void clearWatch() {
    remoteObject.set( PROPERTY_NEEDS_POSITION, NeedsPositionFlavor.NEVER.toString() );
  }

  RemoteObject getRemoteObject() {
    return remoteObject;
  }

  @Override
  public void addGeolocationListener( GeolocationListener listener ) {
    checkArgumentNotNull( listener, GeolocationListener.class.getSimpleName() );
    geolocationListeners.add( listener );
  }

  @Override
  public void removeGeolocationListener( GeolocationListener listener ) {
    checkArgumentNotNull( listener, GeolocationListener.class.getSimpleName() );
    geolocationListeners.remove( listener );
  }

}
