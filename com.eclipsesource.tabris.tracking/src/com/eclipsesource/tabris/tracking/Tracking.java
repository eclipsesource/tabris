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

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import java.io.Serializable;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.widgets.Display;

import com.eclipsesource.tabris.TabrisClient;
import com.eclipsesource.tabris.tracking.TrackingEvent.EventType;
import com.eclipsesource.tabris.tracking.internal.DispatchTask;
import com.eclipsesource.tabris.tracking.internal.EventDispatcher;
import com.eclipsesource.tabris.tracking.internal.EventDispatcherProvider;
import com.eclipsesource.tabris.tracking.internal.TrackingInfoFactory;
import com.eclipsesource.tabris.ui.Action;
import com.eclipsesource.tabris.ui.ActionConfiguration;
import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.PageConfiguration;
import com.eclipsesource.tabris.ui.TransitionAdapter;
import com.eclipsesource.tabris.ui.UI;
import com.eclipsesource.tabris.ui.UIConfiguration;
import com.eclipsesource.tabris.ui.action.SearchActionListener;


/**
 * @since 1.4
 */
@SuppressWarnings("restriction")
public class Tracking implements Serializable {

  private final EventDispatcher dispatcher;
  private final List<Tracker> trackers;

  public Tracking( Tracker tracker ) {
    this( tracker, new Tracker[] {} );
  }

  public Tracking( Tracker tracker, Tracker... otherTrackers ) {
    this( EventDispatcherProvider.getDispatcher(), createTrackerList( tracker, otherTrackers ) );
  }

  Tracking( EventDispatcher disptacher, List<Tracker> trackers ) {
    this.dispatcher = disptacher;
    this.trackers = trackers;
  }

  private static List<Tracker> createTrackerList( Tracker tracker, Tracker... otherTrackers ) {
    whenNull( tracker ).throwIllegalArgument( "Tracker must not be null." );
    whenNull( otherTrackers ).throwIllegalArgument( "OtherTrackers must not be null." );
    List<Tracker> trackers = new ArrayList<Tracker>();
    trackers.add( tracker );
    trackers.addAll( Arrays.asList( otherTrackers ) );
    return trackers;
  }

  public void setUncaughtExceptionHandler( UncaughtExceptionHandler exceptionHandler ) {
    whenNull( exceptionHandler ).throwIllegalArgument( "ExceptionHandler must not be null." );
    dispatcher.setUncaughtExceptionHandler( exceptionHandler );
  }

  public void submitOrder( Display display, Order order ) {
    whenNull( display ).throwIllegalArgument( "Display must not be null" );
    whenNull( order ).throwIllegalArgument( "Order must not be null" );
    TrackingInfo info = createInfo( display );
    dispatchEvent( EventType.ORDER, info, order );
  }

  public void submitEvent( Display display, String eventId ) {
    whenNull( display ).throwIllegalArgument( "Display must not be null" );
    whenNull( eventId ).throwIllegalArgument( "EventId must not be null" );
    when( eventId.isEmpty() ).throwIllegalArgument( "EventId must not be empty" );
    TrackingInfo info = createInfo( display );
    dispatchEvent( EventType.EVENT, info, eventId );
  }

  public void attach( UIConfiguration configuration ) {
    whenNull( configuration ).throwIllegalArgument( "UIConfiguration must not be null." );
    configuration.addTransitionListener( createTransitionListener() );
    configuration.addActionListener( createActionListener() );
  }

  private TransitionAdapter createTransitionListener() {
    return new TransitionAdapter() {

      @Override
      public void after( UI ui, Page from, Page to ) {
        if( RWT.getClient() instanceof TabrisClient ) {
          dispatchPageView( ui, to );
        }
      }

    };
  }

  private SearchActionListener createActionListener() {
    return new SearchActionListener() {

      @Override
      public void executed( UI ui, Action action ) {
        if( RWT.getClient() instanceof TabrisClient ) {
          dispatchAction( ui, action );
        }
      }

      @Override
      public void searched( UI ui, Action action, String query ) {
        if( RWT.getClient() instanceof TabrisClient ) {
          dispatchSearch( ui, action, query );
        }
      }

      @Override
      public void modified( UI ui, Action action, String query ) {
        // not used for tracking
      }
    };
  }

  private void dispatchPageView( UI ui, Page to ) {
    PageConfiguration pageConfiguration = ui.getPageConfiguration( to );
    dispatchEvent( EventType.PAGE_VIEW, createInfo( ui.getDisplay() ), pageConfiguration.getId() );
  }

  private void dispatchAction( UI ui, Action action ) {
    ActionConfiguration actionConfiguration = ui.getActionConfiguration( action );
    dispatchEvent( EventType.ACTION, createInfo( ui.getDisplay() ), actionConfiguration.getId() );
  }

  private void dispatchSearch( UI ui, Action action, String query ) {
    ActionConfiguration actionConfiguration = ui.getActionConfiguration( action );
    TrackingInfo info = createInfo( ui.getDisplay() );
    info.setSearchQuery( query );
    dispatchEvent( EventType.SEARCH, info, actionConfiguration.getId() );
  }

  private TrackingInfo createInfo( Display display ) {
    return TrackingInfoFactory.createInfo( display );
  }

  private void dispatchEvent( EventType type, TrackingInfo info, Object detail ) {
    TrackingEvent event = new TrackingEvent( type, info, detail, System.currentTimeMillis() );
    dispatcher.dispatch( new DispatchTask( trackers, event ) );
  }

  List<Tracker> getTrackers() {
    return trackers;
  }

}
