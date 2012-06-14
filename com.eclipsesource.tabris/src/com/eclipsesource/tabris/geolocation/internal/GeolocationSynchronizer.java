/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.geolocation.internal;

import static com.eclipsesource.tabris.geolocation.internal.GeolocationAdapter.NeedsPositionFlavor.NEVER;
import static com.eclipsesource.tabris.geolocation.internal.GeolocationAdapter.NeedsPositionFlavor.ONCE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.rwt.internal.protocol.IClientObject;

import com.eclipsesource.tabris.geolocation.Coordinates;
import com.eclipsesource.tabris.geolocation.Geolocation;
import com.eclipsesource.tabris.geolocation.GeolocationOptions;
import com.eclipsesource.tabris.geolocation.Position;
import com.eclipsesource.tabris.geolocation.PositionError;
import com.eclipsesource.tabris.geolocation.PositionError.PositionErrorCode;
import com.eclipsesource.tabris.geolocation.internal.GeolocationAdapter.NeedsPositionFlavor;
import com.eclipsesource.tabris.internal.AbstractObjectSynchronizer;


@SuppressWarnings("restriction")
public class GeolocationSynchronizer extends AbstractObjectSynchronizer {

  static final String TYPE = "rap.mobile.Geolocation";

  // write properties
  static final String PROP_ENABLE_HIGH_ACCURACY = "enableHighAccuracy";
  static final String PROP_MAXIMUM_AGE = "maximumAge";
  static final String PROP_FREQUENCY = "frequency";
  static final String PROP_NEEDS_POSITION = "needsPosition";
  // read properties
  static final String PROP_TIMESTAMP = "timestamp";
  static final String PROP_SPEED = "speed";
  static final String PROP_HEADING = "heading";
  static final String PROP_ALTITUDE_ACCURACY = "altitudeAccuracy";
  static final String PROP_ACCURACY = "accuracy";
  static final String PROP_ALTITUDE = "altitude";
  static final String PROP_LONGITUDE = "longitude";
  static final String PROP_LATITUDE = "latitude";
  static final String PROP_ERROR_MESSAGE = "errorMessage";
  static final String PROP_ERROR_CODE = "errorCode";
  
  
  public GeolocationSynchronizer( Geolocation location ) {
    super( location );
  }

  public void readData( Object object ) {
    Geolocation geolocation = ( Geolocation )object;
    if( readPropertyValue( PROP_LATITUDE ) != null ) {
      extractGeolocationProperties( geolocation );
    } else if( readPropertyValue( PROP_ERROR_CODE ) != null ) {
      handlePositionError( geolocation );
    }
  }

  private void extractGeolocationProperties( Geolocation geolocation ) {
    String timestampValue = readPropertyValue( PROP_TIMESTAMP );
    try {
      Date timestamp = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ" ).parse( timestampValue );
      Coordinates coordinates = getCoordinates( geolocation );
      Position position = new Position( coordinates, timestamp );
      geolocation.getAdapter( GeolocationAdapter.class ).setPosition( position );
    } catch( ParseException e ) {
      throw new IllegalStateException( "Could not parse date from string: " +  timestampValue );
    }
  }

  private Coordinates getCoordinates( Geolocation geolocation ) {
    return new Coordinates( getPropertyAsDouble( PROP_LATITUDE ), 
                            getPropertyAsDouble( PROP_LONGITUDE ), 
                            getPropertyAsDouble( PROP_ALTITUDE ), 
                            getPropertyAsDouble( PROP_ACCURACY ), 
                            getPropertyAsDouble( PROP_ALTITUDE_ACCURACY ), 
                            getPropertyAsDouble( PROP_HEADING ), 
                            getPropertyAsDouble( PROP_SPEED ) );
  }

  private double getPropertyAsDouble( String propertyName ) {
    String value = readPropertyValue( propertyName );
    double result = -1;
    if( value != null ) {
      result = Double.valueOf( value  ).doubleValue();
    }
    return result;
  }

  private void handlePositionError( Geolocation geolocation ) {
    String code = readPropertyValue( PROP_ERROR_CODE );
    PositionErrorCode errorCode = PositionErrorCode.valueOf( code );
    String message = readPropertyValue( PROP_ERROR_MESSAGE );
    PositionError positionError = new PositionError( errorCode, message );
    geolocation.getAdapter( GeolocationAdapter.class ).setError( positionError );
  }

  @Override
  public void preserveValues( Object object ) {
    Geolocation geolocation = ( Geolocation )object;
    preserveProperty( PROP_NEEDS_POSITION, getNeedsPosition( geolocation ) );
    preserveOptions( geolocation );
  }

  private void preserveOptions( Geolocation geolocation ) {
    GeolocationAdapter geolocationAdapter = geolocation.getAdapter( GeolocationAdapter.class );
    GeolocationOptions options = geolocationAdapter.getOptions();
    if( options != null ) {
      preserveProperty( PROP_FREQUENCY, options.getFrequency() );
      preserveProperty( PROP_MAXIMUM_AGE, options.getMaximumAge() );
      preserveProperty( PROP_ENABLE_HIGH_ACCURACY, options.isEnableHighAccuracy() );
    }
  }
  
  @Override
  protected void renderInitialization( IClientObject clientObject, Object object ) {
    clientObject.create( TYPE );
  }

  @Override
  public void renderChanges( Object object ) {
    Geolocation geolocation = ( Geolocation )object;
    renderProperty( PROP_NEEDS_POSITION, getNeedsPosition( geolocation ), NeedsPositionFlavor.NEVER );
    renderOptionsChanges( geolocation );
  }

  private void renderOptionsChanges( Geolocation geolocation ) {
    GeolocationAdapter geolocationAdapter = geolocation.getAdapter( GeolocationAdapter.class );
    if( geolocationAdapter.isDisposed() ) {
      destroy();
    }
    GeolocationOptions options = geolocationAdapter.getOptions();
    if( options != null ) {
      renderProperty( PROP_FREQUENCY, options.getFrequency(), 10000 );
      renderProperty( PROP_MAXIMUM_AGE, options.getMaximumAge(), -1 );
      renderProperty( PROP_ENABLE_HIGH_ACCURACY, options.isEnableHighAccuracy(), false );
    }
  }

  private String getNeedsPosition( Geolocation geolocation ) {
    return geolocation.getAdapter( GeolocationAdapter.class ).getFlavor().toString();
  }

  @Override
  protected void processAction( Object object ) {
    Geolocation geolocation = ( Geolocation )object;
    GeolocationAdapter geolocationAdapter = geolocation.getAdapter( GeolocationAdapter.class );
    if( geolocationAdapter.getCallback() != null && geolocationAdapter.getFlavor() != NEVER ) {
      handleGeolocationResult( geolocationAdapter );
    }
    reset( geolocationAdapter );
  }
  
  private void handleGeolocationResult( GeolocationAdapter geolocationAdapter ) {
    if( geolocationAdapter.getPosition() != null ) {
      geolocationAdapter.getCallback().onSuccess( geolocationAdapter.getPosition() );
    } else if( geolocationAdapter.getError() != null ) {
      geolocationAdapter.getCallback().onError( geolocationAdapter.getError() );
    }
  }
  
  private void reset( GeolocationAdapter geolocationAdapter ) {
    if( geolocationAdapter.getPosition() != null && geolocationAdapter.getFlavor() == ONCE ) {
      geolocationAdapter.setFlavor( NEVER );
      geolocationAdapter.setCallback( null );
      geolocationAdapter.setOptions( null );
    }
    geolocationAdapter.setPosition( null );
    geolocationAdapter.setError( null );
  }
  
  // For testing only
  String getObjectId() {
    return getId();
  }
   
}