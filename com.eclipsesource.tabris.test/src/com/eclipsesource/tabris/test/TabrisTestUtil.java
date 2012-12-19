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

import org.eclipse.rap.rwt.internal.remote.RemoteObject;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectFactory;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectImpl;
import org.eclipse.rap.rwt.testfixture.Fixture;


@SuppressWarnings("restriction")
public class TabrisTestUtil {
  
  private static final Random random = new Random(); 

  public static RemoteObject mockRemoteObject() {
    RemoteObject serviceObject = createRemoteObject();
    RemoteObject remoteObject = createRemoteObject();
    RemoteObjectFactory factory = mock( RemoteObjectFactory.class );
    when( factory.createRemoteObject( anyString() ) ).thenReturn( remoteObject );
    when( factory.createServiceObject( anyString() ) ).thenReturn( serviceObject );
    Fixture.fakeRemoteObjectFactory( factory );
    return remoteObject;
  }
  
  public static RemoteObject mockServiceObject() {
    RemoteObject serviceObject = createRemoteObject();
    RemoteObject remoteObject = createRemoteObject();
    RemoteObjectFactory factory = mock( RemoteObjectFactory.class );
    when( factory.createRemoteObject( anyString() ) ).thenReturn( remoteObject );
    when( factory.createServiceObject( anyString() ) ).thenReturn( serviceObject );
    Fixture.fakeRemoteObjectFactory( factory );
    return serviceObject;
  }

  private static RemoteObject createRemoteObject() {
    RemoteObjectImpl remoteObject = mock( RemoteObjectImpl.class );
    String id = "w" + random.nextInt();
    when( remoteObject.getId() ).thenReturn( id );
    return remoteObject;
  }
  
  private TabrisTestUtil() {
    // prevent instantiation
  }
}
