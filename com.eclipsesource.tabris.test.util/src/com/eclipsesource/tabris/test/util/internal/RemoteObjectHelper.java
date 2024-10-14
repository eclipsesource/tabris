/*******************************************************************************
 * Copyright (c) 2012, 2016 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.test.util.internal;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.eclipse.rap.rwt.internal.remote.ConnectionImpl;
import org.eclipse.rap.rwt.remote.OperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.internal.Fixture;

import com.eclipsesource.tabris.test.util.TestRemoteObject;


@SuppressWarnings("restriction")
public class RemoteObjectHelper {

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

  private RemoteObjectHelper() {
    // prevent instantiation
  }
}
