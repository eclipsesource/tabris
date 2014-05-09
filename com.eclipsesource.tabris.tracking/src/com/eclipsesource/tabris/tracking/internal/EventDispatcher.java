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

import java.io.Serializable;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.eclipsesource.tabris.tracking.Tracker;


public class EventDispatcher implements Runnable, Serializable {

  static final long FLUSH_DELAY = 5;

  private final Deque<DispatchTask> queue;
  private final Object lock = new Object();

  private UncaughtExceptionHandler exceptionHandler;

  EventDispatcher() {
    this( Executors.newSingleThreadScheduledExecutor() );
  }

  public void setUncaughtExceptionHandler( UncaughtExceptionHandler exceptionHandler ) {
    this.exceptionHandler = exceptionHandler;
  }

  EventDispatcher( ScheduledExecutorService executor ) {
    this.queue = new ArrayDeque<DispatchTask>();
    executor.scheduleWithFixedDelay( this, 5, FLUSH_DELAY, TimeUnit.SECONDS );
  }

  public void dispatch( DispatchTask task ) {
    synchronized( lock ) {
      queue.add( task );
    }
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
      DispatchTask task;
      while( ( task = queue.poll() ) != null ) {
        for( Tracker tracker : task.getTrackers() ) {
          tracker.handleEvent( task.getEvent() );
        }
      }
    }
  }

}
