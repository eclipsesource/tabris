/*******************************************************************************
 * Copyright (c) 2014, 2016 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets;

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.REFRESH_COMPOSITE;
import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.service.UISessionImpl;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.internal.RefreshCompositeOperationHandler;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;


@SuppressWarnings("restriction")
public class RefreshCompositeTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private Shell shell;
  private RefreshComposite composite;
  private Connection connection;
  private RemoteObject remoteObject;

  @Before
  public void setUp() {
    shell = new Shell( new Display() );
    remoteObject = mock( RemoteObject.class );
    connection = mock( Connection.class );
    when( connection.createRemoteObject( anyString() ) ).thenReturn( remoteObject );
    replaceConnection( connection );
    composite = new RefreshComposite( shell, SWT.NONE );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullParent() {
    new RefreshComposite( null, SWT.NONE );
  }

  @Test
  public void testContructor_createsRemoteObjectWithCorrectType() {
    verify( connection ).createRemoteObject( eq( "tabris.widgets.RefreshComposite" ) );
  }

  @Test
  public void testContructor_setsParent() {
    verify( remoteObject ).set( "parent", getId( composite ) );
  }

  @Test
  public void testContructor_setsOperationHandler() {
    verify( remoteObject ).setHandler( any( RefreshCompositeOperationHandler.class ) );
  }

  @Test
  public void testContructor_setsRefreshCompositeData() {
    Object data = composite.getData( REFRESH_COMPOSITE.getKey() );

    assertEquals( Boolean.TRUE, data );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullMessage() {
    composite.setMessage( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToAddNullListener() {
    composite.addRefreshListener( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToRemoveNullListener() {
    composite.removeRefreshListener( null );
  }

  @Test
  public void testMessageIsNullByDefault() {
    assertNull( composite.getMessage() );
  }

  @Test
  public void testListenersAreEmptyByDefault() {
    assertTrue( composite.getRefreshListeners().isEmpty() );
  }

  @Test
  public void testCanAddListeners() {
    RefreshListener listener = mock( RefreshListener.class );

    composite.addRefreshListener( listener );

    List<RefreshListener> listeners = composite.getRefreshListeners();
    assertTrue( listeners.contains( listener ) );
  }

  @Test
  public void testCanRemoveListeners() {
    RefreshListener listener = mock( RefreshListener.class );

    composite.addRefreshListener( listener );
    composite.removeRefreshListener( listener );

    List<RefreshListener> listeners = composite.getRefreshListeners();
    assertTrue( listeners.isEmpty() );
  }

  @Test
  public void testListenersAreSafeCopy() {
    List<RefreshListener> listeners = composite.getRefreshListeners();
    List<RefreshListener> listeners2 = composite.getRefreshListeners();

    assertNotSame( listeners, listeners2 );
  }

  @Test
  public void testCanSetMessage() {
    composite.setMessage( "foo" );

    assertEquals( "foo", composite.getMessage() );
  }

  private void replaceConnection( Connection connection ) {
    UISessionImpl uiSession = ( UISessionImpl )RWT.getUISession();
    uiSession.setConnection( connection );
  }

}
