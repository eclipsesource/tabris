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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.tabris.test.util.TabrisEnvironment;
import com.eclipsesource.tabris.tracking.TrackingEvent.EventType;
import com.eclipsesource.tabris.tracking.internal.EventDispatcher;
import com.eclipsesource.tabris.ui.Action;
import com.eclipsesource.tabris.ui.ActionConfiguration;
import com.eclipsesource.tabris.ui.ActionListener;
import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.PageConfiguration;
import com.eclipsesource.tabris.ui.TransitionListener;
import com.eclipsesource.tabris.ui.UI;
import com.eclipsesource.tabris.ui.UIConfiguration;
import com.eclipsesource.tabris.ui.action.SearchActionListener;


public class TrackingTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private Display display;

  @Before
  public void setUp() {
    display = new Display();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullTracker() {
    new Tracking( ( Tracker )null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullTrackerButOtherTrackers() {
    new Tracking( null, new Tracker[] { mock( Tracker.class ) } );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullOtherTrackers() {
    new Tracking( mock( Tracker.class ), ( Tracker[] )null );
  }

  @Test
  public void testAddsOneTracker() {
    Tracker tracker = mock( Tracker.class );
    Tracking tracking = new Tracking( tracker );

    List<Tracker> trackers = tracking.getTrackers();

    assertSame( tracker, trackers.get( 0 ) );
    assertEquals( 1, trackers.size() );
  }

  @Test
  public void testAddsMoreTrackers() {
    Tracker tracker1 = mock( Tracker.class );
    Tracker tracker2 = mock( Tracker.class );
    Tracker tracker3 = mock( Tracker.class );
    Tracking tracking = new Tracking( tracker1, tracker2, tracker3 );

    List<Tracker> trackers = tracking.getTrackers();

    assertSame( tracker1, trackers.get( 0 ) );
    assertSame( tracker2, trackers.get( 1 ) );
    assertSame( tracker3, trackers.get( 2 ) );
    assertEquals( 3, trackers.size() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testStartFailsWithNullConfiguration() {
    Tracking tracking = new Tracking( mock( Tracker.class ) );

    tracking.start( null );
  }

  @Test
  public void testStartAddsTransitionListener() {
    Tracking tracking = new Tracking( mock( Tracker.class ) );
    UIConfiguration configuration = mock( UIConfiguration.class );

    tracking.start( configuration );

    verify( configuration ).addTransitionListener( any( TransitionListener.class ) );
  }

  @Test
  public void testStartAddsActionListener() {
    Tracking tracking = new Tracking( mock( Tracker.class ) );
    UIConfiguration configuration = mock( UIConfiguration.class );

    tracking.start( configuration );

    verify( configuration ).addActionListener( any( SearchActionListener.class ) );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsToStopBeforeStart() {
    Tracking tracking = new Tracking( mock( Tracker.class ) );

    tracking.stop();
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsToStopTwice() {
    Tracking tracking = new Tracking( mock( Tracker.class ) );
    UIConfiguration configuration = mock( UIConfiguration.class );

    tracking.start( configuration );
    tracking.stop();
    tracking.stop();
  }

  @Test
  public void testStopRemovesTransitionListener() {
    Tracking tracking = new Tracking( mock( Tracker.class ) );
    UIConfiguration configuration = mock( UIConfiguration.class );

    tracking.start( configuration );
    tracking.stop();

    verify( configuration ).removeTransitionListener( any( TransitionListener.class ) );
  }

  @Test
  public void testStopRemovesActionListener() {
    Tracking tracking = new Tracking( mock( Tracker.class ) );
    UIConfiguration configuration = mock( UIConfiguration.class );

    tracking.start( configuration );
    tracking.stop();

    verify( configuration ).removeActionListener( any( SearchActionListener.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullExceptionHandler() {
    EventDispatcher dispatcher = mock( EventDispatcher.class );
    Tracking tracking = new Tracking( dispatcher );

    tracking.setUncaughtExceptionHandler( null );
  }

  @Test
  public void testSetsExceptionHandlerOnDispatcher() {
    UncaughtExceptionHandler handler = mock( UncaughtExceptionHandler.class );
    EventDispatcher dispatcher = mock( EventDispatcher.class );
    Tracking tracking = new Tracking( dispatcher );

    tracking.setUncaughtExceptionHandler( handler );

    verify( dispatcher ).setUncaughtExceptionHandler( handler );
  }

  @Test
  public void testDispatchesPageTransition() {
    EventDispatcher dispatcher = mock( EventDispatcher.class );
    Tracking tracking = new Tracking( dispatcher );
    PageConfiguration pageConfiguration = mock( PageConfiguration.class );
    UIConfiguration configuration = mock( UIConfiguration.class );

    tracking.start( configuration );
    fakePageTransition( configuration, pageConfiguration );

    ArgumentCaptor<TrackingEvent> captor = ArgumentCaptor.forClass( TrackingEvent.class );
    verify( dispatcher ).dispatch( captor.capture() );
    assertSame( EventType.PAGE_VIEW, captor.getValue().getType() );
    assertNotNull( captor.getValue().getInfo() );
    assertSame( pageConfiguration, captor.getValue().getDetail() );
  }

  private void fakePageTransition( UIConfiguration configuration, PageConfiguration pageConfiguration ) {
    ArgumentCaptor<TransitionListener> captor = ArgumentCaptor.forClass( TransitionListener.class );
    verify( configuration ).addTransitionListener( captor.capture() );
    UI ui = mock( UI.class );
    when( ui.getDisplay() ).thenReturn( display );
    when( ui.getPageConfiguration( any( Page.class ) ) ).thenReturn( pageConfiguration );
    captor.getValue().after( ui, mock( Page.class ), mock( Page.class ) );
  }

  @Test
  public void testDispatchesActionExecution() {
    EventDispatcher dispatcher = mock( EventDispatcher.class );
    Tracking tracking = new Tracking( dispatcher );
    ActionConfiguration actionConfiguration = mock( ActionConfiguration.class );
    UIConfiguration configuration = mock( UIConfiguration.class );

    tracking.start( configuration );
    fakeActionExecution( configuration, actionConfiguration );

    ArgumentCaptor<TrackingEvent> captor = ArgumentCaptor.forClass( TrackingEvent.class );
    verify( dispatcher ).dispatch( captor.capture() );
    assertSame( EventType.ACTION, captor.getValue().getType() );
    assertNotNull( captor.getValue().getInfo() );
    assertSame( actionConfiguration, captor.getValue().getDetail() );
  }

  private void fakeActionExecution( UIConfiguration configuration, ActionConfiguration actionConfiguration ) {
    ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass( ActionListener.class );
    verify( configuration ).addActionListener( captor.capture() );
    UI ui = mock( UI.class );
    when( ui.getDisplay() ).thenReturn( display );
    when( ui.getActionConfiguration( any( Action.class ) ) ).thenReturn( actionConfiguration );
    captor.getValue().executed( ui, mock( Action.class ) );
  }

  @Test
  public void testDispatchesSearch() {
    EventDispatcher dispatcher = mock( EventDispatcher.class );
    Tracking tracking = new Tracking( dispatcher );
    ActionConfiguration actionConfiguration = mock( ActionConfiguration.class );
    UIConfiguration configuration = mock( UIConfiguration.class );

    tracking.start( configuration );
    fakeSearch( configuration, actionConfiguration, "foo" );

    ArgumentCaptor<TrackingEvent> captor = ArgumentCaptor.forClass( TrackingEvent.class );
    verify( dispatcher ).dispatch( captor.capture() );
    assertSame( EventType.SEARCH, captor.getValue().getType() );
    assertNotNull( captor.getValue().getInfo() );
    assertEquals( "foo", captor.getValue().getInfo().getSearchQuery() );
    assertSame( actionConfiguration, captor.getValue().getDetail() );
  }

  private void fakeSearch( UIConfiguration configuration, ActionConfiguration actionConfiguration, String query ) {
    ArgumentCaptor<SearchActionListener> captor = ArgumentCaptor.forClass( SearchActionListener.class );
    verify( configuration ).addActionListener( captor.capture() );
    UI ui = mock( UI.class );
    when( ui.getDisplay() ).thenReturn( display );
    when( ui.getActionConfiguration( any( Action.class ) ) ).thenReturn( actionConfiguration );
    captor.getValue().searched( ui, mock( Action.class ), query );
  }

}
