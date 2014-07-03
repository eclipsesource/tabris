/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InOrder;

import com.eclipsesource.tabris.push.CloudPushListener;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;

public class CloudPushImplTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  @Test
  public void testCreatesRemoteObject() {
    CloudPushImpl cloudPush = new CloudPushImpl();
    RemoteObject remoteObject = cloudPush.getRemoteObject();

    assertNotNull( remoteObject );
  }

  @Test
  public void testCreatesRemoteObjectOnInitialization() {
    CloudPushImpl cloudPush = new CloudPushImpl();

    RemoteObject remoteObject = cloudPush.getRemoteObject();
    RemoteObject remoteObject2 = cloudPush.getRemoteObject();

    assertSame( remoteObject, remoteObject2 );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddListenerFailsWithNullListener() {
    CloudPushImpl cloudPush = new CloudPushImpl();

    cloudPush.addListener( null );
  }

  @Test
  public void testAddListenerCreatesListenOperation() {
    CloudPushImpl cloudPush = new CloudPushImpl();

    cloudPush.addListener( mock( CloudPushListener.class ) );

    verify( environment.getServiceObject() ).listen( "Notification", true );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveListenerFailsWithNullListener() {
    CloudPushImpl cloudPush = new CloudPushImpl();

    cloudPush.removeListener( null );
  }

  @Test
  public void testRemoveListenerCreatesUnListenOperation() {
    CloudPushImpl cloudPush = new CloudPushImpl();
    CloudPushListener listenerMock = mock( CloudPushListener.class );

    cloudPush.addListener( listenerMock );
    cloudPush.removeListener( listenerMock );

    InOrder order = inOrder( environment.getServiceObject(), environment.getServiceObject() );
    order.verify( environment.getServiceObject() ).listen( "Notification", true );
    order.verify( environment.getServiceObject() ).listen( "Notification", false );
  }

  @Test
  public void testRemoveOneOfManyListenersContinuesToListen() {
    CloudPushImpl cloudPush = new CloudPushImpl();
    CloudPushListener listenerMock1 = mock( CloudPushListener.class );
    CloudPushListener listenerMock2 = mock( CloudPushListener.class );

    cloudPush.addListener( listenerMock1 );
    cloudPush.addListener( listenerMock2 );
    cloudPush.removeListener( listenerMock1 );

    verify( environment.getServiceObject(), atLeastOnce() ).listen( "Notification", true );
    verify( environment.getServiceObject(), never() ).listen( "Notification", false );
  }

  @Test
  public void testCallsListenerWithRegisteredEvent() {
    CloudPushImpl cloudPush = new CloudPushImpl();
    CloudPushListener listener = mock( CloudPushListener.class );
    cloudPush.addListener( listener );

    environment.dispatchNotifyOnServiceObject( "Registered", new JsonObject().add( "token", "foo" ) );

    verify( listener ).registered( "foo" );
  }

  @Test
  public void testCallsAllListenersInOrderWithRegisteredEvent() {
    CloudPushImpl cloudPush = new CloudPushImpl();
    CloudPushListener listener = mock( CloudPushListener.class );
    CloudPushListener listener2 = mock( CloudPushListener.class );
    cloudPush.addListener( listener );
    cloudPush.addListener( listener2 );

    environment.dispatchNotifyOnServiceObject( "Registered", new JsonObject().add( "token", "foo" ) );

    InOrder order = inOrder( listener, listener2 );
    order.verify( listener ).registered( "foo" );
    order.verify( listener2 ).registered( "foo" );
  }

  @Test
  public void testCallsListenerWithMessageReceivedEvent() {
    CloudPushImpl cloudPush = new CloudPushImpl();
    CloudPushListener listener = mock( CloudPushListener.class );
    cloudPush.addListener( listener );

    environment.dispatchNotifyOnServiceObject( "MessageReceived", null );

    verify( listener ).messageReceived();
  }

  @Test
  public void testCallsAllsListenersWithMessageReceivedEvent() {
    CloudPushImpl cloudPush = new CloudPushImpl();
    CloudPushListener listener = mock( CloudPushListener.class );
    CloudPushListener listener2 = mock( CloudPushListener.class );
    cloudPush.addListener( listener );
    cloudPush.addListener( listener2 );

    environment.dispatchNotifyOnServiceObject( "MessageReceived", null );

    InOrder order = inOrder( listener, listener2 );
    order.verify( listener ).messageReceived();
    order.verify( listener2 ).messageReceived();
  }

  @Test
  public void testCallsListenerWithErrorReceivedEvent() {
    CloudPushImpl cloudPush = new CloudPushImpl();
    CloudPushListener listener = mock( CloudPushListener.class );
    cloudPush.addListener( listener );

    environment.dispatchNotifyOnServiceObject( "ErrorReceived",
                                               new JsonObject().add( "errorMessage", "error" ) );

    verify( listener ).errorReceived( "error" );
  }

  @Test
  public void testCallsAllsListenersWithErrorReceivedEvent() {
    CloudPushImpl cloudPush = new CloudPushImpl();
    CloudPushListener listener = mock( CloudPushListener.class );
    CloudPushListener listener2 = mock( CloudPushListener.class );
    cloudPush.addListener( listener );
    cloudPush.addListener( listener2 );

    environment.dispatchNotifyOnServiceObject( "ErrorReceived",
                                               new JsonObject().add( "errorMessage", "error" ) );

    InOrder order = inOrder( listener, listener2 );
    order.verify( listener ).errorReceived( "error" );
    order.verify( listener2 ).errorReceived( "error" );
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testThrowsUnsupportedOperationExceptionWithInvalidEvent() {
    CloudPushImpl cloudPush = new CloudPushImpl();
    CloudPushListener listener = mock( CloudPushListener.class );
    cloudPush.addListener( listener );

    environment.dispatchNotifyOnServiceObject( "blub", new JsonObject().add( "message", "foo" ) );
  }

  @Test
  public void testSetsMessage() {
    CloudPushImpl cloudPush = new CloudPushImpl();
    JsonObject properties = new JsonObject();
    properties.set( "message", "payload" );

    cloudPush.handleSet( properties );

    String message = cloudPush.getMessage();
    assertEquals( "payload", message );
  }
}
