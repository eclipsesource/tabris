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

import org.junit.Test;


public class RemoteActionHolderTest {

  @Test
  public void testRemoteObjectIsNullBydefault() {
    RemoteActionHolder remoteActionHolder = new RemoteActionHolder();

    RemoteAction remoteAction = remoteActionHolder.getRemoteAction();

    assertNull( remoteAction );
  }

  @Test
  public void testSavesRemoteObject() {
    RemoteActionHolder remoteObjectHolder = new RemoteActionHolder();
    RemoteAction remoteAction = mock( RemoteAction.class );

    remoteObjectHolder.setRemoteAction( remoteAction );
    RemoteAction actualRemoteAction = remoteObjectHolder.getRemoteAction();

    assertSame( remoteAction, actualRemoteAction );
  }
}
