/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal.ui;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import org.eclipse.rap.rwt.remote.RemoteObject;
import org.junit.Test;


public class RemoteObjectHolderTest {

  @Test
  public void testRemoteObjectIsNullBydefault() {
    RemoteObjectHolder remoteObjectHolder = new RemoteObjectHolder();

    RemoteObject remoteObject = remoteObjectHolder.getRemoteObject();

    assertNull( remoteObject );
  }

  @Test
  public void testSavesRemoteObject() {
    RemoteObjectHolder remoteObjectHolder = new RemoteObjectHolder();
    RemoteObject remoteObject = mock( RemoteObject.class );

    remoteObjectHolder.setRemotObject( remoteObject );
    RemoteObject actualRemoteObject = remoteObjectHolder.getRemoteObject();

    assertSame( remoteObject, actualRemoteObject );
  }
}
