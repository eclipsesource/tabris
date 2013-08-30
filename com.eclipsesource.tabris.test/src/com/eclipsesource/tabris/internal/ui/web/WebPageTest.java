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
package com.eclipsesource.tabris.internal.ui.web;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.rwt.client.WebClient;
import org.eclipse.rap.rwt.client.service.JavaScriptExecutor;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.internal.ui.ActionDescriptor;
import com.eclipsesource.tabris.internal.ui.PageDescriptor;
import com.eclipsesource.tabris.internal.ui.RemotePageTest;
import com.eclipsesource.tabris.internal.ui.TestAction;
import com.eclipsesource.tabris.internal.ui.TestPage;
import com.eclipsesource.tabris.internal.ui.UITestUtil;
import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.PageData;
import com.eclipsesource.tabris.ui.PageStyle;
import com.eclipsesource.tabris.ui.UI;


public class WebPageTest {

  private Shell shell;
  private UI ui;
  private WebUI uiRenderer;
  private PageDescriptor descriptor;
  private WebPage webPage;

  @Before
  public void setUp() {
    Fixture.setUp();
    shell = new Shell( new Display() );
    ui = mock( UI.class );
    uiRenderer = mock( WebUI.class );
    mockDescriptor();
    webPage = new WebPage( ui, uiRenderer, descriptor, mock( PageData.class ) );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testCreate_addsPageToUI() throws Exception {
    verify( uiRenderer ).pageCreated( webPage );
  }

  @Test
  public void testDestroy_removesPageFromUI() throws Exception {
    webPage.destroy();

    verify( uiRenderer ).pageDestroyed( webPage );
  }

  @Test
  public void testDestroy_disposesControl() throws Exception {
    webPage.createControl( shell );

    webPage.destroy();

    assertNull( webPage.getControl() );
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( WebPage.class ) );
  }

  @Test
  public void testGetDescriptor() {
    assertSame( descriptor, webPage.getDescriptor() );
  }

  @Test
  public void testGetData() {
    PageData data = mock( PageData.class );
    webPage = new WebPage( ui, uiRenderer, descriptor, data );

    assertSame( data, webPage.getData() );
  }

  @Test
  public void testGetPage() {
    Page page = webPage.getPage();

    assertNotNull( page );
    assertTrue( page instanceof TestPage );
  }

  @Test
  public void testGetCreatesControl() {
    webPage.createControl( shell );

    assertNotNull( webPage.getControl() );
  }

  @Test
  public void testSetTitle_rendersTitle() {
    WebClient webClient = mock( WebClient.class );
    JavaScriptExecutor javaScriptExecutor = mock( JavaScriptExecutor.class );
    when( webClient.getService( JavaScriptExecutor.class ) ).thenReturn( javaScriptExecutor );
    Fixture.fakeClient( webClient );

    webPage.setTitle( "foo" );

    verify( javaScriptExecutor ).execute( eq( "document.title = \"foo\";" ) );
  }

  @Test
  public void testPageActivated_rendersTitle() {
    WebClient webClient = mock( WebClient.class );
    JavaScriptExecutor javaScriptExecutor = mock( JavaScriptExecutor.class );
    when( webClient.getService( JavaScriptExecutor.class ) ).thenReturn( javaScriptExecutor );
    Fixture.fakeClient( webClient );

    webPage.pageActivated();

    verify( javaScriptExecutor ).execute( eq( "document.title = \"bar\";" ) );
  }

  private void mockDescriptor() {
    descriptor = mock( PageDescriptor.class );
    when( descriptor.getId() ).thenReturn( "foo" );
    when( descriptor.getTitle() ).thenReturn( "bar" );
    doReturn( Boolean.TRUE ).when( descriptor ).isTopLevel();
    when( descriptor.getPageStyle() ).thenReturn( new PageStyle[] { PageStyle.DEFAULT } );
    List<ActionDescriptor> actions = new ArrayList<ActionDescriptor>();
    InputStream image = RemotePageTest.class.getResourceAsStream( "testImage.png" );
    actions.add( new ActionDescriptor( "actionFoo",
                                       new TestAction(),
                                       "actionBar",
                                       image,
                                       true,
                                       true ) );
    when( descriptor.getActions() ).thenReturn( actions );
    when( descriptor.getImage() ).thenReturn( UITestUtil.getImageBytes() );
    doReturn( TestPage.class ).when( descriptor ).getPageType();
  }

}
