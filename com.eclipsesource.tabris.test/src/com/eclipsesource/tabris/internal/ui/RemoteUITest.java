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
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.internal.protocol.ProtocolUtil;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectImpl;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.internal.ui.rendering.PageRenderer;
import com.eclipsesource.tabris.test.TabrisTestUtil;
import com.eclipsesource.tabris.ui.PageOperator;
import com.eclipsesource.tabris.ui.UI;


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
  public void testGetPageParent() {
    RemoteUI remoteUI = new RemoteUI( shell );

    Composite pageParent = remoteUI.getPageParent();

    assertSame( shell, pageParent );
  }

  @Test
  public void testGetActionsParent() {
    RemoteUI remoteUI = new RemoteUI( shell );

    Composite uiParent = remoteUI.getActionsParent();

    assertSame( shell, uiParent );
  }

  @Test
  public void testActivateAddsActivateCall() {
    RemoteUI remoteUI = new RemoteUI( shell );
    RemotePage page = mock( RemotePage.class );
    when( page.getId() ).thenReturn( "foo" );

    remoteUI.activate( page );

    verify( remoteObject ).set( "activePage", "foo" );
  }

  @Test
  public void testShowPageEventCallsUI() {
    UI ui = mock( UI.class );
    PageOperator pageOperator = mock( PageOperator.class );
    when( ui.getPageOperator() ).thenReturn( pageOperator );
    RemoteUI remoteUI = spy( new RemoteUI( shell ) );
    remoteUI.setUi( ui );
    remoteUI.setController( mock( Controller.class ) );
    when( remoteObject.getHandler() ).thenReturn( remoteUI );
    doReturn( "bar" ).when( remoteUI ).getPageId( "foo" );

    JsonObject properties = new JsonObject().add( "pageId", "foo" );
    TabrisTestUtil.dispatchNotify( remoteObject, "ShowPage", properties );

    verify( pageOperator ).openPage( "bar" );
  }

  @Test
  public void testShowPreviousPageEventCallsUI() {
    UI ui = mock( UI.class );
    PageOperator pageOperator = mock( PageOperator.class );
    when( ui.getPageOperator() ).thenReturn( pageOperator );
    createRemoteUI( ui );

    TabrisTestUtil.dispatchNotify( remoteObject, "ShowPreviousPage", null );

    verify( pageOperator ).closeCurrentPage();
  }

  @Test
  public void testSendsForeground() {
    RemoteUI remoteUI = createRemoteUI( mock( UI.class ) );

    remoteUI.setForeground( new Color( shell.getDisplay(), 100, 200, 150 ) );

    verify( remoteObject ).set( "foreground", new JsonArray().add( 100 ).add( 200 ).add( 150 ) );
  }

  @Test
  public void testSendsBackground() {
    RemoteUI remoteUI = createRemoteUI( mock( UI.class ) );

    remoteUI.setBackground( new Color( shell.getDisplay(), 100, 120, 150 ) );

    verify( remoteObject ).set( "background", new JsonArray().add( 100 ).add( 120 ).add( 150 ) );
  }

  @Test
  public void testSendsImage() {
    Image image = new Image( shell.getDisplay(), UITestUtil.class.getResourceAsStream( "testImage.png" ) );
    RemoteUI remoteUI = createRemoteUI( mock( UI.class ) );

    remoteUI.setImage( image );

    verify( remoteObject ).set( "image", ProtocolUtil.getJsonForImage( image ) );
  }

  @Test
  public void testFindsPageIdByRemoteId() {
    RemoteUI remoteUI = createRemoteUI( mock( UI.class ) );
    RemotePage remotePage = mock( RemotePage.class );
    Controller controller = mock( Controller.class );
    List<PageRenderer> pages = new ArrayList<PageRenderer>();
    pages.add( remotePage );
    when( controller.getAllPages() ).thenReturn( pages  );
    remoteUI.setController( controller );
    PageDescriptor pageDescriptor = mock( PageDescriptor.class );
    when( pageDescriptor.getId() ).thenReturn( "bar" );
    when( remotePage.getDescriptor() ).thenReturn( pageDescriptor );
    when( remotePage.getId() ).thenReturn( "foo" );

    String pageId = remoteUI.getPageId( "foo" );

    assertEquals( "bar", pageId );
  }

  private RemoteUI createRemoteUI( UI ui ) {
    RemoteUI remoteUI = new RemoteUI( shell );
    when( remoteObject.getHandler() ).thenReturn( remoteUI );
    remoteUI.setUi( ui );
    return remoteUI;
  }

}
