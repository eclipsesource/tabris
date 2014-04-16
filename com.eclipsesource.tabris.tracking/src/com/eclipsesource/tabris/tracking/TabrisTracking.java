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

import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.rap.rwt.RWT;

import com.eclipsesource.tabris.TabrisClient;
import com.eclipsesource.tabris.tracking.TrackingEvent.EventType;
import com.eclipsesource.tabris.tracking.internal.EventDispatcher;
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
public class TabrisTracking {

  private final EventDispatcher dispatcher;
  private UIConfiguration configuration;
  private final SearchActionListener actionListener;
  private final TransitionAdapter transitionListener;

  public TabrisTracking( Tracker tracker ) {
    this( tracker, new Tracker[] {} );
  }

  public TabrisTracking( Tracker tracker, Tracker... otherTrackers ) {
    this( new EventDispatcher( createTrackerList( tracker, otherTrackers ) ) );
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

  TabrisTracking( EventDispatcher disptacher ) {
    this.dispatcher = disptacher;
    this.actionListener = createActionListener();
    this.transitionListener = createTransitionListener();
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

  public void start( UIConfiguration configuration ) {
    whenNull( configuration ).throwIllegalArgument( "UIConfiguration must not be null." );
    this.configuration = configuration;
    configuration.addTransitionListener( transitionListener );
    configuration.addActionListener( actionListener );
  }

  private void dispatchPageView( UI ui, Page to ) {
    PageConfiguration pageConfiguration = ui.getPageConfiguration( to );
    dispatchEvent( EventType.PAGE_VIEW, createInfo( ui ), pageConfiguration );
  }

  private void dispatchAction( UI ui, Action action ) {
    ActionConfiguration actionConfiguration = ui.getActionConfiguration( action );
    dispatchEvent( EventType.ACTION, createInfo( ui ), actionConfiguration );
  }

  private void dispatchSearch( UI ui, Action action, String query ) {
    ActionConfiguration actionConfiguration = ui.getActionConfiguration( action );
    TrackingInfo info = createInfo( ui );
    info.setSearchQuery( query );
    dispatchEvent( EventType.SEARCH, info, actionConfiguration );
  }

  private void dispatchEvent( EventType type, TrackingInfo info, Object detail ) {
    TrackingEvent event = new TrackingEvent( type, info, detail, System.currentTimeMillis() );
    dispatcher.dispatch( event );
  }

  private TrackingInfo createInfo( UI ui ) {
    return TrackingInfoFactory.createInfo( ui );
  }

  public void stop() {
    whenNull( configuration ).throwIllegalState( "Tracking was not active." );
    configuration.removeTransitionListener( transitionListener );
    configuration.removeActionListener( actionListener );
    configuration = null;
  }

  List<Tracker> getTrackers() {
    return dispatcher.getTrackers();
  }

}
