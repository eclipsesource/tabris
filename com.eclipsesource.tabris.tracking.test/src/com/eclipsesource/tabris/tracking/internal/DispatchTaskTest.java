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
package com.eclipsesource.tabris.tracking.internal;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.eclipsesource.tabris.tracking.Tracker;
import com.eclipsesource.tabris.tracking.TrackingEvent;


public class DispatchTaskTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullTracker() {
    new DispatchTask( null, mock( TrackingEvent.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullEvent() {
    new DispatchTask( new ArrayList<Tracker>(), null );
  }

  @Test
  public void testHasTrackers() {
    ArrayList<Tracker> trackers = new ArrayList<Tracker>();
    DispatchTask task = new DispatchTask( trackers, mock( TrackingEvent.class ) );

    List<Tracker> actualTrackers = task.getTrackers();

    assertSame( trackers, actualTrackers );
  }

  @Test
  public void testHasEvent() {
    TrackingEvent event = mock( TrackingEvent.class );
    DispatchTask task = new DispatchTask( new ArrayList<Tracker>(), event );

    TrackingEvent actualEvent = task.getEvent();

    assertSame( event, actualEvent );
  }
}
