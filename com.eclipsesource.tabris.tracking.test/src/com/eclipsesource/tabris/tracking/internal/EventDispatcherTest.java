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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.mockito.InOrder;

import com.eclipsesource.tabris.tracking.Tracker;
import com.eclipsesource.tabris.tracking.TrackingEvent;


public class EventDispatcherTest {

  @Test
  public void testSchedulesItselfOnExecutor() {
    ScheduledExecutorService executor = mock( ScheduledExecutorService.class );
    EventDispatcher dispatcher = new EventDispatcher( executor );

    verify( executor ).scheduleWithFixedDelay( dispatcher, 5, EventDispatcher.FLUSH_DELAY, TimeUnit.SECONDS );
  }

  @Test( expected = IllegalStateException.class )
  public void testThrowsISEWithoutExceptionHandler() {
    Tracker tracker = mock( Tracker.class );
    List<Tracker> trackers = new ArrayList<>();
    trackers.add( tracker );

    doThrow( RuntimeException.class ).when( tracker ).handleEvent( any( TrackingEvent.class ) );
    EventDispatcher dispatcher = new EventDispatcher( mock( ScheduledExecutorService.class ) );
    TrackingEvent event = mock( TrackingEvent.class );

    dispatcher.dispatch( new DispatchTask( trackers, event ) );
    dispatcher.run();
  }

  @Test
  public void testCallExceptionHandlerOnException() {
    Tracker tracker = mock( Tracker.class );
    List<Tracker> trackers = new ArrayList<>();
    trackers.add( tracker );
    doThrow( RuntimeException.class ).when( tracker ).handleEvent( any( TrackingEvent.class ) );
    EventDispatcher dispatcher = new EventDispatcher( mock( ScheduledExecutorService.class ) );
    UncaughtExceptionHandler handler = mock( UncaughtExceptionHandler.class );
    dispatcher.setUncaughtExceptionHandler( handler );
    TrackingEvent event = mock( TrackingEvent.class );

    dispatcher.dispatch( new DispatchTask( trackers, event ) );
    dispatcher.run();

    verify( handler ).uncaughtException( any( Thread.class ), any( Throwable.class ) );
  }

  @Test
  public void testFlushesQueueOnRun() {
    Tracker tracker = mock( Tracker.class );
    List<Tracker> trackers = new ArrayList<>();
    trackers.add( tracker );
    EventDispatcher dispatcher = new EventDispatcher( mock( ScheduledExecutorService.class ) );
    TrackingEvent event = mock( TrackingEvent.class );

    dispatcher.dispatch( new DispatchTask( trackers, event ) );
    dispatcher.run();

    verify( tracker ).handleEvent( event );
  }

  @Test
  public void testFlushesQueueNotWithoutRun() {
    Tracker tracker = mock( Tracker.class );
    List<Tracker> trackers = new ArrayList<>();
    trackers.add( tracker );
    EventDispatcher dispatcher = new EventDispatcher( mock( ScheduledExecutorService.class ) );
    TrackingEvent event = mock( TrackingEvent.class );

    dispatcher.dispatch( new DispatchTask( trackers, event ) );

    verify( tracker, never() ).handleEvent( event );
  }

  @Test
  public void testFlushesQueueOnRunWithMultipleEvents() {
    Tracker tracker = mock( Tracker.class );
    List<Tracker> trackers = new ArrayList<>();
    trackers.add( tracker );
    EventDispatcher dispatcher = new EventDispatcher( mock( ScheduledExecutorService.class ) );
    TrackingEvent event1 = mock( TrackingEvent.class );
    TrackingEvent event2 = mock( TrackingEvent.class );

    dispatcher.dispatch( new DispatchTask( trackers, event1 ) );
    dispatcher.dispatch( new DispatchTask( trackers, event2 ) );
    dispatcher.run();

    InOrder order = inOrder( tracker );
    order.verify( tracker ).handleEvent( event1 );
    order.verify( tracker ).handleEvent( event2 );
  }
}
