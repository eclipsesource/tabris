/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.eclipsesource.tabris.tracking.TrackingEvent.EventType;


public class TrackingEventTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullType() {
    new TrackingEvent( null, mock( TrackingInfo.class ), new Object(), 1 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullInfo() {
    new TrackingEvent( EventType.PAGE_VIEW, null, new Object(), 1 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullDetail() {
    new TrackingEvent( EventType.PAGE_VIEW, mock( TrackingInfo.class ), null, 1 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNegativeTimestamp() {
    new TrackingEvent( EventType.PAGE_VIEW, mock( TrackingInfo.class ), new Object(), -1 );
  }

  @Test
  public void testHasType() {
    TrackingEvent event = new TrackingEvent( EventType.PAGE_VIEW, mock( TrackingInfo.class ), new Object(), 1 );

    EventType type = event.getType();

    assertSame( EventType.PAGE_VIEW, type );
  }

  @Test
  public void testHasTrackingInfo() {
    TrackingInfo info = mock( TrackingInfo.class );
    TrackingEvent event = new TrackingEvent( EventType.PAGE_VIEW, info, new Object(), 1 );

    TrackingInfo actualInfo = event.getInfo();

    assertSame( info, actualInfo );
  }

  @Test
  public void testHasDetail() {
    Object detail = new Object();
    TrackingEvent event = new TrackingEvent( EventType.PAGE_VIEW, mock( TrackingInfo.class ), detail, 1 );

    Object actualDetail = event.getDetail();

    assertSame( detail, actualDetail );
  }

  @Test
  public void testHasTimestamp() {
    Object detail = new Object();
    TrackingEvent event = new TrackingEvent( EventType.PAGE_VIEW, mock( TrackingInfo.class ), detail, 23 );

    long timestamp = event.getTimestamp();

    assertEquals( 23, timestamp );
  }
}
