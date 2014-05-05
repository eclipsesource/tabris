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

import static com.eclipsesource.tabris.app.EventType.PAUSE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.Serializable;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import com.eclipsesource.tabris.app.App;
import com.eclipsesource.tabris.app.AppEvent;
import com.eclipsesource.tabris.app.AppListener;
import com.eclipsesource.tabris.app.BackNavigationListener;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;


public class AppImplTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( AppImpl.class ) );
  }

  @Test
  public void testAppListenerIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( AppListener.class ) );
  }

  @Test
  public void testBackNavigationListenerIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( BackNavigationListener.class ) );
  }

  @Test
  public void testAddListensTransportsListenOperation() {
    App app = new AppImpl();
    AppListener listener = mock( AppListener.class );

    app.addEventListener( PAUSE, listener );

    verify( environment.getServiceObject() ).listen( "Pause", true );
  }

  @Test
  public void testAddEventListenersTransportsListenOperationOnce() {
    App app = new AppImpl();
    AppListener listener = mock( AppListener.class );
    AppListener listener2 = mock( AppListener.class );

    app.addEventListener( PAUSE, listener );
    app.addEventListener( PAUSE, listener2 );

    verify( environment.getServiceObject(), times( 1 ) ).listen( "Pause", true );
  }

  @Test
  public void testRemoveListensTransportsListenOperation() {
    App app = new AppImpl();
    AppListener listener = mock( AppListener.class );
    app.addEventListener( PAUSE, listener );

    app.removeEventListener( PAUSE, listener );

    RemoteObject remoteObject = environment.getServiceObject();
    InOrder order = inOrder( remoteObject );
    order.verify( remoteObject ).listen( "Pause", true );
    order.verify( remoteObject ).listen( "Pause", false );
  }

  @Test
  public void testRemoveEventListenersTransportsListenOperationOnce() {
    App app = new AppImpl();
    AppListener listener = mock( AppListener.class );
    AppListener listener2 = mock( AppListener.class );
    app.addEventListener( PAUSE, listener );
    app.addEventListener( PAUSE, listener2 );

    app.removeEventListener( PAUSE, listener );
    app.removeEventListener( PAUSE, listener2 );

    RemoteObject remoteObject = environment.getServiceObject();
    InOrder order = inOrder( remoteObject );
    order.verify( remoteObject, times( 1 ) ).listen( "Pause", true );
    order.verify( remoteObject, times( 1 ) ).listen( "Pause", false );
  }

  @Test
  public void testRemoveNonExistingEventListenersDoesNotFail() {
    App app = new AppImpl();
    AppListener listener = mock( AppListener.class );

    app.removeEventListener( PAUSE, listener );
  }

  @Test
  public void testRemoveOneListenerDoesNotTransportsListenOperation() {
    App app = new AppImpl();
    AppListener listener = mock( AppListener.class );
    AppListener listener2 = mock( AppListener.class );
    app.addEventListener( PAUSE, listener );
    app.addEventListener( PAUSE, listener2 );

    app.removeEventListener( PAUSE, listener );

    RemoteObject remoteObject = environment.getServiceObject();
    InOrder order = inOrder( remoteObject );
    order.verify( remoteObject, times( 1 ) ).listen( "Pause", true );
    order.verify( remoteObject, never() ).listen( "Pause", false );
  }

  @Test
  public void testDelegatesNotify() {
    AppImpl app = new AppImpl();
    AppListener listener = mock( AppListener.class );
    app.addEventListener( PAUSE, listener );

    environment.dispatchNotifyOnServiceObject( "Pause", null );

    ArgumentCaptor<AppEvent> captor = ArgumentCaptor.forClass( AppEvent.class );
    verify( listener ).handleEvent( captor.capture() );
    assertSame( PAUSE, captor.getValue().getType() );
  }

  @Test
  public void testDelegatesNotifyToAllListeners() {
    AppImpl app = new AppImpl();
    AppListener listener = mock( AppListener.class );
    AppListener listener2 = mock( AppListener.class );
    app.addEventListener( PAUSE, listener );
    app.addEventListener( PAUSE, listener2 );

    environment.dispatchNotifyOnServiceObject( "Pause", null );

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
    app.addEventListener( PAUSE, listener );
    JsonObject properties = new JsonObject();
    properties.add( "foo", "bar" );

    environment.dispatchNotifyOnServiceObject( "Pause", properties );

    ArgumentCaptor<AppEvent> captor = ArgumentCaptor.forClass( AppEvent.class );
    verify( listener ).handleEvent( captor.capture() );
    assertSame( PAUSE, captor.getValue().getType() );
    assertEquals( JsonValue.valueOf( "bar" ), captor.getValue().getProperty( "foo" ) );
  }

  @Test
  public void testAddBackNavigationListensTransportsListenOperation() {
    App app = new AppImpl();
    BackNavigationListener listener = mock( BackNavigationListener.class );

    app.addBackNavigationListener( listener );

    verify( environment.getServiceObject() ).listen( "BackNavigation", true );
  }

  @Test
  public void testAddBackNavigationListenersTransportsListenOperationOnce() {
    App app = new AppImpl();
    BackNavigationListener listener = mock( BackNavigationListener.class );
    BackNavigationListener listener2 = mock( BackNavigationListener.class );

    app.addBackNavigationListener( listener );
    app.addBackNavigationListener( listener2 );

    verify( environment.getServiceObject(), times( 1 ) ).listen( "BackNavigation", true );
  }

  @Test
  public void testRemoveBackNavigationListensTransportsListenOperation() {
    App app = new AppImpl();
    BackNavigationListener listener = mock( BackNavigationListener.class );
    app.addBackNavigationListener( listener );

    app.removeBackNavigationListener( listener );

    RemoteObject remoteObject = environment.getServiceObject();
    InOrder order = inOrder( remoteObject );
    order.verify( remoteObject ).listen( "BackNavigation", true );
    order.verify( remoteObject ).listen( "BackNavigation", false );
  }

  @Test
  public void testRemoveBackNavigationListenersTransportsListenOperationOnce() {
    App app = new AppImpl();
    BackNavigationListener listener = mock( BackNavigationListener.class );
    BackNavigationListener listener2 = mock( BackNavigationListener.class );
    app.addBackNavigationListener( listener );
    app.addBackNavigationListener( listener2 );

    app.removeBackNavigationListener( listener );
    app.removeBackNavigationListener( listener2 );

    RemoteObject remoteObject = environment.getServiceObject();
    InOrder order = inOrder( remoteObject );
    order.verify( remoteObject, times( 1 ) ).listen( "BackNavigation", true );
    order.verify( remoteObject, times( 1 ) ).listen( "BackNavigation", false );
  }

  @Test
  public void testRemoveOneBackNavigationListenerDoesNotTransportsListenOperation() {
    App app = new AppImpl();
    BackNavigationListener listener = mock( BackNavigationListener.class );
    BackNavigationListener listener2 = mock( BackNavigationListener.class );
    app.addBackNavigationListener( listener );
    app.addBackNavigationListener( listener2 );

    app.removeBackNavigationListener( listener );

    RemoteObject remoteObject = environment.getServiceObject();
    InOrder order = inOrder( remoteObject );
    order.verify( remoteObject, times( 1 ) ).listen( "BackNavigation", true );
    order.verify( remoteObject, never() ).listen( "BackNavigation", false );
  }

  @Test
  public void testDelegatesBackNavigationNotify() {
    AppImpl app = new AppImpl();
    BackNavigationListener listener = mock( BackNavigationListener.class );
    app.addBackNavigationListener( listener );

    environment.dispatchNotifyOnServiceObject( "BackNavigation", null );

    verify( listener ).navigatedBack();
  }

  @Test
  public void testDelegatesBackNavigationNotifyToAllListeners() {
    AppImpl app = new AppImpl();
    BackNavigationListener listener = mock( BackNavigationListener.class );
    BackNavigationListener listener2 = mock( BackNavigationListener.class );
    app.addBackNavigationListener( listener );
    app.addBackNavigationListener( listener2 );

    environment.dispatchNotifyOnServiceObject( "BackNavigation", null );

    verify( listener ).navigatedBack();
    verify( listener2 ).navigatedBack();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testInactivityTimeoutFailsWithNegativeTime() {
    AppImpl app = new AppImpl();

    app.startInactivityTimer( -1 );

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( environment.getServiceObject() ).call( eq( "startInactivityTimer" ), captor.capture() );
    assertEquals( captor.getValue().get( "inactivityTime" ), JsonValue.valueOf( 10 ) );
  }

  @Test
  public void testCallsActivateInactivityLockOnRemoteObject() {
    AppImpl app = new AppImpl();

    app.startInactivityTimer( 10 );

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( environment.getServiceObject() ).call( eq( "startInactivityTimer" ), captor.capture() );
    assertEquals( captor.getValue().get( "inactivityTime" ), JsonValue.valueOf( 10 ) );
  }

  @Test
  public void testCallsDeactivateInactivityLockOnRemoteObject() {
    AppImpl app = new AppImpl();

    app.stopInactivityTimer();

    verify( environment.getServiceObject() ).call( "stopInactivityTimer", null );
  }

  @Test
  public void testSetScreenProtectionOnRemotObjectWithTrue() {
    AppImpl app = new AppImpl();

    app.setScreenProtection( true );

    verify( environment.getServiceObject() ).set( "screenProtection", true );
  }

  @Test
  public void testSetScreenProtectionOnRemotObjectOnlyOnChange() {
    AppImpl app = new AppImpl();

    app.setScreenProtection( true );
    app.setScreenProtection( true );

    verify( environment.getServiceObject(), times( 1 ) ).set( "screenProtection", true );
  }

  @Test
  public void testSetScreenProtectionOnRemotObjectWithFalse() {
    AppImpl app = new AppImpl();

    app.setScreenProtection( true );
    app.setScreenProtection( false );

    verify( environment.getServiceObject() ).set( "screenProtection", false );
  }

  @Test
  public void testSavesScreenProtection() {
    AppImpl app = new AppImpl();

    app.setScreenProtection( true );

    assertTrue( app.hasScreenProtection() );
  }

  @Test
  public void testDefaultBadgeNumber() {
    AppImpl app = new AppImpl();

    assertEquals( 0, app.getBadgeNumber() );
  }

  @Test
  public void testSetBadgeNumber() {
    AppImpl app = new AppImpl();

    app.setBadgeNumber( 42 );

    verify( environment.getServiceObject() ).set( "badgeNumber", 42 );
  }

  @Test
  public void testSetBadgeNumberOnRemotObjectOnlyOnChange() {
    AppImpl app = new AppImpl();

    app.setBadgeNumber( 42 );
    app.setBadgeNumber( 42 );

    verify( environment.getServiceObject(), times( 1 ) ).set( "badgeNumber", 42 );
  }

  @Test
  public void testSetBadgeNumberOnRemotObjectWithChangedValue() {
    AppImpl app = new AppImpl();

    app.setBadgeNumber( 42 );
    app.setBadgeNumber( 666 );

    verify( environment.getServiceObject() ).set( "badgeNumber", 666 );
  }

  @Test
  public void testDisablesBadgeNumber() {
    AppImpl app = new AppImpl();

    app.setBadgeNumber( SWT.NONE );

    assertEquals( 0, app.getBadgeNumber() );
  }

  @Test
  public void testSavesBadgeNumber() {
    AppImpl app = new AppImpl();

    app.setBadgeNumber( 42 );

    assertEquals( 42, app.getBadgeNumber() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testBadgeNumberFailsWithNegativeNumber() {
    AppImpl app = new AppImpl();

    app.setBadgeNumber( -1 );
  }

  @Test
  public void testSetsTabrisVersion() {
    AppImpl app = new AppImpl();
    JsonObject properties = new JsonObject();
    properties.set( "tabrisVersion", "1.3.0" );

    app.handleSet( properties );

    String tabrisVersion = app.getTabrisVersion();
    assertEquals( "1.3.0", tabrisVersion );
  }

  @Test
  public void testSetsAppId() {
    AppImpl app = new AppImpl();
    JsonObject properties = new JsonObject();
    properties.set( "id", "foo.bar" );

    app.handleSet( properties );

    String id = app.getId();
    assertEquals( "foo.bar", id );
  }

  @Test
  public void testSetsVersion() {
    AppImpl app = new AppImpl();
    JsonObject properties = new JsonObject();
    properties.set( "version", "1.4.0" );

    app.handleSet( properties );

    String version = app.getVersion();
    assertEquals( "1.4.0", version );
  }

  @Test
  public void testSetsOpenurl() {
    AppImpl app = new AppImpl();
    JsonObject properties = new JsonObject();
    properties.set( "openUrl", "app://foo.bar/path" );

    app.handleSet( properties );

    String openUrl = app.getOpenUrl();
    assertEquals( "app://foo.bar/path", openUrl );
  }

}