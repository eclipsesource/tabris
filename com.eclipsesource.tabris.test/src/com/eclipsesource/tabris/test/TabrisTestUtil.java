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
package com.eclipsesource.tabris.test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.internal.remote.ConnectionImpl;
import org.eclipse.rap.rwt.remote.OperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.Fixture;


@SuppressWarnings("restriction")
public class TabrisTestUtil {

  private static final Random random = new Random();

  public static RemoteObject mockRemoteObject() {
    RemoteObject serviceObject = createRemoteObject();
    RemoteObject remoteObject = createRemoteObject();
    ConnectionImpl connection = mock( ConnectionImpl.class );
    when( connection.createRemoteObject( anyString() ) ).thenReturn( remoteObject );
    when( connection.createServiceObject( anyString() ) ).thenReturn( serviceObject );
    Fixture.fakeConnection( connection );
    return remoteObject;
  }

  private static RemoteObject createRemoteObject() {
    String id = "w" + random.nextInt();
    RemoteObject remoteObject = spy( new TestRemoteObject( id ) );
    remoteObject.setHandler( mock( OperationHandler.class ) );
    return remoteObject;
  }


  private TabrisTestUtil() {
    // prevent instantiation
  }

  public static class TestRemoteObject implements RemoteObject {

    private final String id;
    private OperationHandler handler;

    public TestRemoteObject( String id ) {
      this.id = id;
    }

    @Override
    public String getId() {
      return id;
    }

    @Override
    public void set( String name, int value ) {
    }

    @Override
    public void set( String name, double value ) {
    }

    @Override
    public void set( String name, boolean value ) {
    }

    @Override
    public void set( String name, String value ) {
    }

    @Override
    public void set( String name, JsonValue value ) {
    }

    @Override
    public void listen( String eventType, boolean listen ) {
    }

    @Override
    public void call( String method, JsonObject parameters ) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void setHandler( OperationHandler handler ) {
      this.handler = handler;
    }

    public OperationHandler getHandler() {
      return handler;
    }

  }
}
