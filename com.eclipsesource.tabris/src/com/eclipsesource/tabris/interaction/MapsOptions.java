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
package com.eclipsesource.tabris.interaction;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_LATITUDE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_LONGITUDE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_QUERY;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ZOOM;


/**
 * <p>
 * Concrete launch option to open coordinates or address in the Maps App.
 * <p>
 *
 * @since 0.9
 */
public class MapsOptions extends LaunchOptions {

  public MapsOptions( double latitude, double longitude ) {
    super( App.MAPS );
    add( PROPERTY_LATITUDE, String.valueOf( latitude ) );
    add( PROPERTY_LONGITUDE, String.valueOf( longitude ) );
  }

  public MapsOptions( double latitude, double longitude, int zoomLevel ) {
    this( latitude, longitude );
    validateZoom( zoomLevel );
    add( PROPERTY_ZOOM, String.valueOf( zoomLevel ) );
  }

  public MapsOptions( String query ) {
    super( App.MAPS );
    whenNull( query ).thenIllegalArgument( "Query must not be null" );
    when( query.isEmpty() ).thenIllegalArgument( "Query must not be empty" );
    add( PROPERTY_QUERY, query );
  }

  public MapsOptions( String query, int zoomLevel ) {
    this( query );
    validateZoom( zoomLevel );
    add( PROPERTY_ZOOM, String.valueOf( zoomLevel ) );
  }

  private void validateZoom( int zoomLevel ) {
    when( zoomLevel < 0 ).thenIllegalArgument( "ZoomLevel should be >= 0 but was " + zoomLevel );
  }
}
