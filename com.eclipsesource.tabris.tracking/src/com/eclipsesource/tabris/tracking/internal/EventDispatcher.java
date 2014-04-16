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

import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.eclipsesource.tabris.tracking.Tracker;
import com.eclipsesource.tabris.tracking.TrackingEvent;


@SuppressWarnings("restriction")
public class EventDispatcher implements Runnable {

  static final long FLUSH_DELAY = 5;

  private final List<Tracker> trackers;
  private final Deque<TrackingEvent> queue;
  private final Object lock = new Object();

  private UncaughtExceptionHandler exceptionHandler;

  public EventDispatcher( List<Tracker> trackers ) {
    this( Executors.newSingleThreadScheduledExecutor(), trackers );
  }

  public void setUncaughtExceptionHandler( UncaughtExceptionHandler exceptionHandler ) {
    this.exceptionHandler = exceptionHandler;
  }

  EventDispatcher( ScheduledExecutorService executor, List<Tracker> trackers ) {
    whenNull( trackers ).throwIllegalArgument( "Trackers must not be null." );
    this.queue = new ArrayDeque<TrackingEvent>();
    this.trackers = trackers;
    executor.scheduleWithFixedDelay( this, 5, FLUSH_DELAY, TimeUnit.SECONDS );
  }

  public void dispatch( TrackingEvent event ) {
    synchronized( lock ) {
      queue.add( event );
    }
  }

  public List<Tracker> getTrackers() {
    return trackers;
  }

  @Override
  public void run() {
    try {
      flushQueue();
    } catch( Throwable shouldNotHappen ) {
      handleException( shouldNotHappen );
    }
  }

  private void handleException( Throwable exception ) {
    if( exceptionHandler != null ) {
      exceptionHandler.uncaughtException( Thread.currentThread(), exception );
    } else {
      throw new IllegalStateException( exception );
    }
  }

  void flushQueue() {
    synchronized( lock ) {
      TrackingEvent event;
      while( ( event = queue.poll() ) != null ) {
        for( Tracker tracker : trackers ) {
          tracker.handleEvent( event );
        }
      }
    }
  }

}
