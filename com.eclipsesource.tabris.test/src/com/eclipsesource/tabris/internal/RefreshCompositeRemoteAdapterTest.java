/*******************************************************************************
 * Copyright (c) 2016 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;

import static org.eclipse.rap.rwt.internal.lifecycle.DisplayUtil.getLCA;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.lifecycle.RemoteAdapter;
import org.eclipse.rap.rwt.internal.service.UISessionImpl;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.widgets.WidgetRemoteAdapter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.test.util.TabrisEnvironment;
import com.eclipsesource.tabris.widgets.RefreshComposite;
import com.eclipsesource.tabris.widgets.RefreshListener;


@SuppressWarnings("restriction")
public class RefreshCompositeRemoteAdapterTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private Display display;
  private RefreshComposite composite;
  private RemoteObject remoteObject;


  @Before
  public void setUp() {
    display = new Display();
    Shell shell = new Shell( display );
    remoteObject = mock( RemoteObject.class );
    Connection connection = mock( Connection.class );
    when( connection.createRemoteObject( anyString() ) ).thenReturn( remoteObject );
    replaceConnection( connection );
    composite = new RefreshComposite( shell, SWT.NONE );
  }

  @Test
  public void testRenderMessage_initially() {
    render();

    verify( remoteObject, never() ).set( eq( "message" ), anyString() );
  }

  @Test
  public void testRenderMessage_changed() {
    composite.setMessage( "foo" );

    render();

    verify( remoteObject ).set( "message", "foo" );
  }

  @Test
  public void testRenderMessage_changed_twice() {
    composite.setMessage( "foo" );
    render();
    reset( remoteObject );

    composite.setMessage( "bar" );
    render();

    verify( remoteObject ).set( "message", "bar" );
  }

  @Test
  public void testRenderMessage_unchanged() {
    composite.setMessage( "foo" );
    render();
    reset( remoteObject );

    render();

    verify( remoteObject, never() ).set( eq( "message" ), anyString() );
  }

  @Test
  public void testRenderMessage_changedBack() {
    composite.setMessage( "foo" );
    render();
    reset( remoteObject );

    composite.setMessage( "bar" );
    composite.setMessage( "foo" );
    render();

    verify( remoteObject, never() ).set( eq( "message" ), anyString() );
  }

  @Test
  public void testRenderRefreshListener_initially() {
    render();

    verify( remoteObject, never() ).listen( eq( "Refresh" ), anyBoolean() );
  }

  @Test
  public void testRenderRefreshListener_changed() {
    composite.addRefreshListener( mock( RefreshListener.class ) );

    render();

    verify( remoteObject ).listen( "Refresh", true );
  }

  @Test
  public void testRenderRefreshListener_unchanged() {
    composite.addRefreshListener( mock( RefreshListener.class ) );
    render();
    reset( remoteObject );

    render();

    verify( remoteObject, never() ).listen( eq( "Refresh" ), anyBoolean() );
  }

  @Test
  public void testRenderRefreshListener_changedBack() {
    RefreshListener listener = mock( RefreshListener.class );
    composite.addRefreshListener( listener );
    composite.removeRefreshListener( listener );
    render();

    verify( remoteObject, never() ).listen( eq( "Refresh" ), anyBoolean() );
  }

  @Test
  public void testRenderDone() {
    composite.done();

    render();

    verify( remoteObject ).call( "done", null );
  }

  @Test
  public void testRenderDone_onlyOnce() {
    composite.done();
    composite.done();

    render();

    verify( remoteObject, times( 1 ) ).call( "done", null );
  }

  private void render() {
    try {
      getLCA( display ).render( display );
    } catch( IOException shouldNotHappen ) {
      fail( shouldNotHappen.getMessage() );
    }
  }

  protected static WidgetRemoteAdapter getRemoteAdapter( Composite composite ) {
    return ( WidgetRemoteAdapter )composite.getAdapter( RemoteAdapter.class );
  }

  private void replaceConnection( Connection connection ) {
    UISessionImpl uiSession = ( UISessionImpl )RWT.getUISession();
    uiSession.setConnection( connection );
  }


}
