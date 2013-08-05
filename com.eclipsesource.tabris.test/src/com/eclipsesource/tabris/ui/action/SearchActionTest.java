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
package com.eclipsesource.tabris.ui.action;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.rap.rwt.remote.RemoteObject;
import org.junit.Test;

import com.eclipsesource.tabris.internal.ui.RemoteObjectHolder;
import com.eclipsesource.tabris.internal.ui.TestSearchAction;

public class SearchActionTest {

  @Test
  public void testCallsOpenWithoutQuery() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    TestSearchAction searchAction = new TestSearchAction();
    searchAction.getAdapter( RemoteObjectHolder.class ).setRemotObject( remoteObject );

    searchAction.open();

    verify( remoteObject ).call( "open", null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetMessageFailsWithNull() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    TestSearchAction searchAction = new TestSearchAction();
    searchAction.getAdapter( RemoteObjectHolder.class ).setRemotObject( remoteObject );

    searchAction.setMessage( null );
  }

  @Test
  public void testSetMessage() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    TestSearchAction searchAction = new TestSearchAction();
    searchAction.getAdapter( RemoteObjectHolder.class ).setRemotObject( remoteObject );

    searchAction.setMessage( "foo" );

    verify( remoteObject ).set( "message", "foo" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetQueryFailsWithNull() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    TestSearchAction searchAction = new TestSearchAction();
    searchAction.getAdapter( RemoteObjectHolder.class ).setRemotObject( remoteObject );

    searchAction.setQuery( null );
  }

  @Test
  public void testSetQuery() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    TestSearchAction searchAction = new TestSearchAction();
    searchAction.getAdapter( RemoteObjectHolder.class ).setRemotObject( remoteObject );

    searchAction.setQuery( "foo" );

    verify( remoteObject ).set( "query", "foo" );
  }

  @Test
  public void testHasRemoteObjectHolder() {
    TestSearchAction searchAction = new TestSearchAction();

    RemoteObjectHolder remoteObjectHolder = searchAction.getAdapter( RemoteObjectHolder.class );

    assertNotNull( remoteObjectHolder );
  }

  @Test
  public void testHasOneRemoteObjectHolder() {
    TestSearchAction searchAction = new TestSearchAction();

    RemoteObjectHolder remoteObjectHolder = searchAction.getAdapter( RemoteObjectHolder.class );
    RemoteObjectHolder remoteObjectHolder2 = searchAction.getAdapter( RemoteObjectHolder.class );

    assertSame( remoteObjectHolder, remoteObjectHolder2 );
  }
}
