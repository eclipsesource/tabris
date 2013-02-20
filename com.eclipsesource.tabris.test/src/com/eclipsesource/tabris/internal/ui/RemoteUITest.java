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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.rwt.internal.remote.RemoteObjectImpl;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.test.TabrisTestUtil;
import com.eclipsesource.tabris.ui.PageManager;
import com.eclipsesource.tabris.ui.UIContext;


@SuppressWarnings("restriction")
public class RemoteUITest {

  private RemoteObjectImpl remoteObject;
  private Shell shell;

  @Before
  public void setUp() {
    Fixture.setUp();
    shell = new Shell( new Display() );
    remoteObject = ( RemoteObjectImpl )TabrisTestUtil.mockRemoteObject();
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testActivateFailsWithNullId() {
    RemoteUI remoteUI = new RemoteUI( shell );

    remoteUI.activate( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testActivateFailsWithEmptyId() {
    RemoteUI remoteUI = new RemoteUI( shell );

    remoteUI.activate( "" );
  }

  @Test
  public void testSetsShellIdId() {
    new RemoteUI( shell );

    verify( remoteObject ).set( "shell", WidgetUtil.getId( shell ) );
  }

  @Test
  public void testGetRemoteUIId() {
    RemoteUI remoteUI = new RemoteUI( shell );

    assertEquals( remoteObject.getId(), remoteUI.getRemoteUIId() );
  }

  @Test
  public void testActivateAddsActivateCall() {
    RemoteUI remoteUI = new RemoteUI( shell );

    remoteUI.activate( "foo" );

    verify( remoteObject ).set( "activePage", "foo" );
  }

  @Test
  public void testShowPageEventCallsContext() {
    UIContext context = mock( UIContext.class );
    PageManager pageManager = mock( PageManager.class );
    when( context.getPageManager() ).thenReturn( pageManager );
    RemoteUI remoteUI = createRemoteUI( context );
    Map<String, Object> properties = new HashMap<String, Object>();
    properties.put( "pageId", "foo" );
    Controller controller = mock( Controller.class );
    when( controller.getPageId( "foo" ) ).thenReturn( "bar" );
    remoteUI.setController( controller );

    Fixture.dispatchNotify( remoteObject, "ShowPage", properties );

    verify( pageManager ).showPage( "bar" );
  }

  @Test
  public void testShowPreviousPageEventCallsContext() {
    UIContext context = mock( UIContext.class );
    PageManager pageManager = mock( PageManager.class );
    when( context.getPageManager() ).thenReturn( pageManager );
    createRemoteUI( context );

    Fixture.dispatchNotify( remoteObject, "ShowPreviousPage", null );

    verify( pageManager ).showPreviousPage();
  }

  @Test
  public void testSendsForeground() {
    RemoteUI remoteUI = createRemoteUI( mock( UIContext.class ) );

    remoteUI.setForeground( new Color( shell.getDisplay(), 100, 200, 150 ) );

    verify( remoteObject ).set( "foreground", new int[] { 100, 200, 150 } );
  }

  @Test
  public void testSendsBackground() {
    RemoteUI remoteUI = createRemoteUI( mock( UIContext.class ) );

    remoteUI.setBackground( new Color( shell.getDisplay(), 100, 120, 150 ) );

    verify( remoteObject ).set( "background", new int[] { 100, 120, 150 } );
  }

  private RemoteUI createRemoteUI( UIContext context ) {
    RemoteUI remoteUI = new RemoteUI( shell );
    when( remoteObject.getHandler() ).thenReturn( remoteUI );
    remoteUI.setContext( context );
    return remoteUI;
  }
}
