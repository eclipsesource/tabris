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
package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.event.EventType.PAUSE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import com.eclipsesource.tabris.event.App;
import com.eclipsesource.tabris.event.AppEvent;
import com.eclipsesource.tabris.event.AppListener;
import com.eclipsesource.tabris.test.TabrisTestUtil;


public class AppImplTest {

  @Before
  public void setUp() {
    Fixture.setUp();
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testAddListensTransportsListenOperation() {
    RemoteObject remoteObject = TabrisTestUtil.mockServiceObject();
    App app = new AppImpl();
    AppListener listener = mock( AppListener.class );

    app.addListener( PAUSE, listener );

    verify( remoteObject ).listen( "Pause", true );
  }

  @Test
  public void testAddListenersTransportsListenOperationOnce() {
    RemoteObject remoteObject = TabrisTestUtil.mockServiceObject();
    App app = new AppImpl();
    AppListener listener = mock( AppListener.class );
    AppListener listener2 = mock( AppListener.class );

    app.addListener( PAUSE, listener );
    app.addListener( PAUSE, listener2 );

    verify( remoteObject, times( 1 ) ).listen( "Pause", true );
  }

  @Test
  public void testRemoveListensTransportsListenOperation() {
    RemoteObject remoteObject = TabrisTestUtil.mockServiceObject();
    App app = new AppImpl();
    AppListener listener = mock( AppListener.class );
    app.addListener( PAUSE, listener );

    app.removeListener( PAUSE, listener );

    InOrder order = inOrder( remoteObject );
    order.verify( remoteObject ).listen( "Pause", true );
    order.verify( remoteObject ).listen( "Pause", false );
  }

  @Test
  public void testRemoveListenersTransportsListenOperationOnce() {
    RemoteObject remoteObject = TabrisTestUtil.mockServiceObject();
    App app = new AppImpl();
    AppListener listener = mock( AppListener.class );
    AppListener listener2 = mock( AppListener.class );
    app.addListener( PAUSE, listener );
    app.addListener( PAUSE, listener2 );

    app.removeListener( PAUSE, listener );
    app.removeListener( PAUSE, listener2 );

    InOrder order = inOrder( remoteObject );
    order.verify( remoteObject, times( 1 ) ).listen( "Pause", true );
    order.verify( remoteObject, times( 1 ) ).listen( "Pause", false );
  }

  @Test
  public void testRemoveOneListenerDoesNotTransportsListenOperation() {
    RemoteObject remoteObject = TabrisTestUtil.mockServiceObject();
    App app = new AppImpl();
    AppListener listener = mock( AppListener.class );
    AppListener listener2 = mock( AppListener.class );
    app.addListener( PAUSE, listener );
    app.addListener( PAUSE, listener2 );

    app.removeListener( PAUSE, listener );

    InOrder order = inOrder( remoteObject );
    order.verify( remoteObject, times( 1 ) ).listen( "Pause", true );
    order.verify( remoteObject, never() ).listen( "Pause", false );
  }

  @Test
  public void testDelegatesNotify() {
    AppImpl app = new AppImpl();
    AppListener listener = mock( AppListener.class );
    app.addListener( PAUSE, listener );

    Fixture.dispatchNotify( app.getRemoteObject(), "Pause", null );

    ArgumentCaptor<AppEvent> captor = ArgumentCaptor.forClass( AppEvent.class );
    verify( listener ).handleEvent( captor.capture() );
    assertSame( PAUSE, captor.getValue().getType() );
  }

  @Test
  public void testDelegatesNotifyToAllListeners() {
    AppImpl app = new AppImpl();
    AppListener listener = mock( AppListener.class );
    AppListener listener2 = mock( AppListener.class );
    app.addListener( PAUSE, listener );
    app.addListener( PAUSE, listener2 );

    Fixture.dispatchNotify( app.getRemoteObject(), "Pause", null );

    ArgumentCaptor<AppEvent> captor = ArgumentCaptor.forClass( AppEvent.class );
    verify( listener ).handleEvent( captor.capture() );
    verify( listener2 ).handleEvent( captor.capture() );
    assertSame( PAUSE, captor.getAllValues().get( 0 ).getType() );
    assertSame( PAUSE, captor.getAllValues().get( 1 ).getType() );
  }

  @Test
  public void testDelegatesNotifyWithProperties() {
    AppImpl app = new AppImpl();
    AppListener listener = mock( AppListener.class );
    app.addListener( PAUSE, listener );
    Map<String, Object> properties = new HashMap<String, Object>();
    properties.put( "foo", "bar" );

    Fixture.dispatchNotify( app.getRemoteObject(), "Pause", properties );

    ArgumentCaptor<AppEvent> captor = ArgumentCaptor.forClass( AppEvent.class );
    verify( listener ).handleEvent( captor.capture() );
    assertSame( PAUSE, captor.getValue().getType() );
    assertEquals( "bar", captor.getValue().getProperty( "foo" ) );
  }

}
