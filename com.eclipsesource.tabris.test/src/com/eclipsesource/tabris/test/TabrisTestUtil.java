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
import static org.mockito.Mockito.when;

import java.util.Random;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.internal.remote.ConnectionImpl;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectImpl;
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

  public static RemoteObject mockServiceObject() {
    RemoteObject serviceObject = createRemoteObject();
    RemoteObject remoteObject = createRemoteObject();
    ConnectionImpl connection = mock( ConnectionImpl.class );
    when( connection.createRemoteObject( anyString() ) ).thenReturn( remoteObject );
    when( connection.createServiceObject( anyString() ) ).thenReturn( serviceObject );
    Fixture.fakeConnection( connection );
    return serviceObject;
  }

  private static RemoteObjectImpl createRemoteObject() {
    RemoteObjectImpl remoteObject = mock( RemoteObjectImpl.class );
    String id = "w" + random.nextInt();
    when( remoteObject.getId() ).thenReturn( id );
    return remoteObject;
  }

  public static void dispatchSet( RemoteObject remoteObject, JsonObject properties ) {
    RemoteObjectImpl remoteObjectImpl = ( RemoteObjectImpl )remoteObject;
    remoteObjectImpl.getHandler().handleSet( properties );
  }

  public static void dispatchNotify( RemoteObject remoteObject,
                                     String eventName,
                                     JsonObject properties )
  {
    RemoteObjectImpl remoteObjectImpl = ( RemoteObjectImpl )remoteObject;
    remoteObjectImpl.getHandler().handleNotify( eventName, properties );
  }

  public static void dispatchCall( RemoteObject remoteObject,
                                   String methodName,
                                   JsonObject parameters )
  {
    RemoteObjectImpl remoteObjectImpl = ( RemoteObjectImpl )remoteObject;
    remoteObjectImpl.getHandler().handleCall( methodName, parameters );
  }

  private TabrisTestUtil() {
    // prevent instantiation
  }
}
