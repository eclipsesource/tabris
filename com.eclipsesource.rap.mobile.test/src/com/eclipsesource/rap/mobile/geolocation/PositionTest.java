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
package com.eclipsesource.rap.mobile.geolocation;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.rap.mobile.geolocation.Coordinates;
import com.eclipsesource.rap.mobile.geolocation.Position;


public class PositionTest {
  
  private Date date;
  private Position position;

  @Before
  public void setUp() {
    date = new Date();
    Coordinates coordinates = new Coordinates( 10, 11, 12, 13, 14, 15, 16 );
    position = new Position( coordinates, date );
  }
  
  @Test
  public void testCoordinates() {
    assertEquals( 10, position.getCoords().getLatitude(), 0 );
    assertEquals( 11, position.getCoords().getLongitude(), 0 );
    assertEquals( 12, position.getCoords().getAltitude(), 0 );
    assertEquals( 13, position.getCoords().getAccuracy(), 0 );
    assertEquals( 14, position.getCoords().getAltitudeAccuracy(), 0 );
    assertEquals( 15, position.getCoords().getHeading(), 0 );
    assertEquals( 16, position.getCoords().getSpeed(), 0 );
  }
  
  @Test
  public void testTimestamp() {
    assertEquals( date, position.getTimestamp() );
  }
}
