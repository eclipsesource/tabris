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
package com.eclipsesource.tabris.app;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;


public class EventTypeTest {

  @Test
  public void testNonExsitingFromString() {
    assertNull( EventType.fromName( "hmpf" ) );
  }

  @Test
  public void testLaunchFromString() {
    assertSame( EventType.OPEN, EventType.fromName( "Open" ) );
  }

  @Test
  public void testPauseFromString() {
    assertSame( EventType.PAUSE, EventType.fromName( "Pause" ) );
  }

  @Test
  public void testResumeFromString() {
    assertSame( EventType.RESUME, EventType.fromName( "Resume" ) );
  }

  @Test
  public void testInactiveFromString() {
    assertSame( EventType.INACTIVE, EventType.fromName( "Inactive" ) );
  }

}
