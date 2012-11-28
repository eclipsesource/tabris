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

import org.eclipse.rap.rwt.internal.remote.RemoteObject;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectFactory;
import org.eclipse.rap.rwt.testfixture.Fixture;


@SuppressWarnings("restriction")
public class TabrisTestUtil {

  public static RemoteObject mockRemoteObject() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    RemoteObjectFactory factory = mock( RemoteObjectFactory.class );
    when( factory.createRemoteObject( anyString() ) ).thenReturn( remoteObject );
    Fixture.fakeRemoteObjectFactory( factory );
    return remoteObject;
  }
  
  private TabrisTestUtil() {
    // prevent instantiation
  }
}
