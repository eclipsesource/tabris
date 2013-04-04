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
package com.eclipsesource.tabris.geolocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.geolocation.PositionError.PositionErrorCode;


public class PositionErrorTest {

  private PositionError positionError;

  @Before
  public void setUp() {
    positionError = new PositionError( PositionErrorCode.POSITION_UNAVAILABLE, "test" );
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( PositionError.class ) );
  }

  @Test
  public void testCode() {
    assertEquals( PositionErrorCode.POSITION_UNAVAILABLE, positionError.getCode() );
  }

  @Test
  public void testMessage() {
    assertEquals( "test", positionError.getMessage() );
  }
}
