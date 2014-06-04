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
import org.eclipse.rap.rwt.service.ApplicationContext;
import org.eclipse.rap.rwt.service.UISession;
import org.eclipse.swt.widgets.Display;

import com.eclipsesource.tabris.TabrisClient;
import com.eclipsesource.tabris.tracking.TrackingEvent.EventType;
import com.eclipsesource.tabris.tracking.internal.DispatchTask;
import com.eclipsesource.tabris.tracking.internal.EventDispatcher;
import com.eclipsesource.tabris.tracking.internal.EventDispatcherProvider;
import com.eclipsesource.tabris.tracking.internal.TrackingInfoFactory;
import com.eclipsesource.tabris.tracking.tracker.ConsoleTracker;
import com.eclipsesource.tabris.tracking.tracker.GoogleAnalyticsTracker;
import com.eclipsesource.tabris.tracking.tracker.PiwikTracker;
import com.eclipsesource.tabris.ui.Action;
import com.eclipsesource.tabris.ui.ActionConfiguration;
import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.PageConfiguration;
import com.eclipsesource.tabris.ui.TransitionAdapter;
import com.eclipsesource.tabris.ui.UI;
import com.eclipsesource.tabris.ui.UIConfiguration;
import com.eclipsesource.tabris.ui.action.SearchActionListener;


/**
 * <p>
 * A {@link Tracking} is responsible for tracking UI events. UI events are fired e.g. when a user navigates through an
 * app or a button was pressed. A {@link Tracking} can be attached to a {@link UIConfiguration} which starts an
 * automated tracking for the associated Tabris UI. Besides the automated tracking you can use a {@link Tracking}
 * instance to fire custom or ecommerce events.
 * </p>
 * <p>
 * A tracking always needs one or more concrete {@link Tracker} instances to delegate the events. The {@link Tracking}
 * handles all scheduling and threading topics. It creates a {@link TrackingEvent} for every UI event and dispatches it
 * to the added {@link Tracker} instances.
 * </p>
 * <p>
 * <b>Please Note:</b> You should create only one {@link Tracking} per application, <b>not</b> per {@link UISession}.
 * Consider sharing the {@link Tracking} instance as attribute in the {@link ApplicationContext}.
 * </p>
 * <p>
 * Usually the {@link Tracking} will be used with popular tracking services like
 * <a href="http://www.google.com/analytics/">Google Analytics</a> or <a href="http://piwik.org/">Piwik</a>. For this
 * reason some ready to use {@link Tracker} implementations exist:
 *   <ul>
 *   <li>{@link GoogleAnalyticsTracker}</li>
 *   <li>{@link PiwikTracker}</li>
 *   <li>{@link ConsoleTracker}</li>
 *   </ul>
 * </p>
 * <p>
 * A simple example using the {@link ConsoleTracker} looks like this:
 *   <pre>
 *   ...
 *   Tracking tracking = new Tracking( new ConsoleTracker() );
 *   tracking.attach( myUIConfiguration );
 *   RWT.getApplicationContext().setAttribute( "tracking", tracking );
 *   ...
 *   </pre>
 *   </p>
 *
 * @see Tracker
 *
 * @since 1.4
 */
@SuppressWarnings("restriction")
public class Tracking implements Serializable {

  private final EventDispatcher dispatcher;
  private final List<Tracker> trackers;

  /**
   * <p>
   * Creates a {@link Tracking} instance with one {@link Tracker}.
   * </p>
   *
   * @param tracker the tracker to use. Must not be <code>null</code>.
   */
  public Tracking( Tracker tracker ) {
    this( tracker, new Tracker[] {} );
  }

  /**
   * <p>
   * Creates a {@link Tracking} instance with one or more {@link Tracker}.
   * </p>
   *
   * @param tracker the tracker to use. Must not be <code>null</code>.
   * @param otherTrackers additional {@link Tracker} to use. Must not be <code>null</code>.
   */
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

  /**
   * <p>
   * A {@link TrackingEvent} will be processed asynchronous. A {@link Tracker} implementation can throw exceptions when
   * something goes wrong. To prevent failures you can set an {@link UncaughtExceptionHandler} to catch those
   * and handle those exceptions properly.
   * </p>
   *
   * @param exceptionHandler the exception handler to use for error handling. Must not be <code>null</code>.
   */
  public void setUncaughtExceptionHandler( UncaughtExceptionHandler exceptionHandler ) {
    whenNull( exceptionHandler ).throwIllegalArgument( "ExceptionHandler must not be null." );
    dispatcher.setUncaughtExceptionHandler( exceptionHandler );
  }

  /**
   * <p>
   * To track ecommerce events you can submit orders e.g. within you shop application.
   * </p>
   *
   * @param display the display of the {@link UISession} the order was made in. Must not be <code>null</code>.
   * @param order the {@link Order} to track. Must not be <code>null</code>.
   */
  public void submitOrder( Display display, Order order ) {
    whenNull( display ).throwIllegalArgument( "Display must not be null" );
    whenNull( order ).throwIllegalArgument( "Order must not be null" );
    TrackingInfo info = createInfo( display );
    dispatchEvent( EventType.ORDER, info, order );
  }

  /**
   * <p>
   * Within you application you may want to track custom events e.g. when a special button was pressed and so on.
   * To accomplish this you can submit events with a custom id.
   * </p>
   *
   * @param display the display of the {@link UISession} the order was made in. Must not be <code>null</code>.
   * @param eventId the custom event id. Must not be <code>null</code> or empty.
   */
  public void submitEvent( Display display, String eventId ) {
    whenNull( display ).throwIllegalArgument( "Display must not be null" );
    whenNull( eventId ).throwIllegalArgument( "EventId must not be null" );
    when( eventId.isEmpty() ).throwIllegalArgument( "EventId must not be empty" );
    TrackingInfo info = createInfo( display );
    dispatchEvent( EventType.EVENT, info, eventId );
  }

  /**
   * <p>
   * To track Tabris UI navigation and action events you can attach a {@link UIConfiguration} to a {@link Tracking}
   * instance. The {@link Tracking} will dispatch all {@link Page} navigations, {@link Action} executions and searches
   * to the added {@link Tracker}s.
   * </p>
   *
   * @param configuration the configuration of the Tabris UI to track. Must not be <code>null</code>.
   */
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
