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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectImpl;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.tabris.internal.ui.rendering.ActionRenderer;
import com.eclipsesource.tabris.internal.ui.rendering.UIRenderer;
import com.eclipsesource.tabris.test.RWTRunner;
import com.eclipsesource.tabris.test.TabrisTestUtil;
import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.PageData;
import com.eclipsesource.tabris.ui.PageStyle;
import com.eclipsesource.tabris.ui.UI;


@SuppressWarnings("restriction")
@RunWith( RWTRunner.class )
public class RemotePageTest {

  private RemoteObjectImpl remoteObject;
  private PageDescriptor descriptor;
  private UI ui;
  private RemoteUI uiRenderer;
  private Shell shell;

  @Before
  public void setUp() {
    shell = new Shell( new Display() );
    remoteObject = ( RemoteObjectImpl )TabrisTestUtil.mockRemoteObject();
    ui = mock( UI.class );
    uiRenderer = mock( RemoteUI.class );
    when( uiRenderer.getRemoteUIId() ).thenReturn( "foo" );
    mockDescriptor();
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( RemotePage.class ) );
  }

  private void mockDescriptor() {
    descriptor = mock( PageDescriptor.class );
    when( descriptor.getId() ).thenReturn( "foo" );
    when( descriptor.getTitle() ).thenReturn( "bar" );
    doReturn( Boolean.TRUE ).when( descriptor ).isTopLevel();
    when( descriptor.getPageStyle() ).thenReturn( new PageStyle[] { PageStyle.DEFAULT } );
    List<ActionDescriptor> actions = new ArrayList<ActionDescriptor>();
    actions.add( new ActionDescriptor( "actionFoo", new TestAction() ) );
    when( descriptor.getActions() ).thenReturn( actions );
    when( descriptor.getImage() ).thenReturn( UITestUtil.getImageBytes() );
    doReturn( TestPage.class ).when( descriptor ).getPageType();
  }

  @Test
  public void testGetRemoteId() {
    RemotePage page = new RemotePage( ui, uiRenderer, descriptor, mock( PageData.class ) );

    assertEquals( remoteObject.getId(), page.getId() );
  }

  @Test
  public void testSetsDefaultAttributes() {
    when( uiRenderer.getRemoteUIId() ).thenReturn( "foo1" );
    RemotePage remotePage = new RemotePage( ui, uiRenderer, descriptor, mock( PageData.class ) );
    remotePage.createControl( shell );

    verify( remoteObject ).set( "parent", "foo1" );
    verify( remoteObject ).set( "title", "bar" );
    verify( remoteObject ).set( "control", WidgetUtil.getId( remotePage.getControl() ) );
    verify( remoteObject ).set( "style", new JsonArray().add( "DEFAULT" ) );
    verify( remoteObject ).set( "topLevel", true );
    ArgumentCaptor<JsonArray> captor = ArgumentCaptor.forClass( JsonArray.class );
    verify( remoteObject ).set( eq( "image" ), captor.capture() );
    assertTrue( captor.getValue().get( 0 ).isString() );
    assertEquals( 49, captor.getValue().get( 1 ).asInt() );
    assertEquals( 43, captor.getValue().get( 2 ).asInt() );
  }

  @Test
  public void testSetsFullScreenStyle() {
    PageDescriptor localDescriptor = new PageDescriptor( "foo", TestPage.class );
    localDescriptor.setPageStyle( PageStyle.FULLSCREEN );

    RemotePage remotePage = new RemotePage( ui, uiRenderer, localDescriptor, mock( PageData.class ) );
    remotePage.createControl( shell );

    verify( remoteObject ).set( "style", new JsonArray().add( "FULLSCREEN" ) );
  }

  @Test
  public void testSetsNoEmptyScreenStyle() {
    PageDescriptor localDescriptor = new PageDescriptor( "foo", TestPage.class );

    new RemotePage( ui, uiRenderer, localDescriptor, mock( PageData.class ) );

    verify( remoteObject, never() ).set( eq( "style" ), any( JsonArray.class ) );
  }

  @Test
  public void testGetActionRenderers() {
    RemotePage page = new RemotePage( ui, uiRenderer, descriptor, mock( PageData.class ) );
    ActionRenderer renderer = mock( ActionRenderer.class );
    RemoteRendererFactory factory = mock( RemoteRendererFactory.class );
    when( factory.createActionRenderer( any( UI.class ), any( UIRenderer.class ), any( ActionDescriptor.class ) ) )
    .thenReturn( renderer );

    page.createActions( factory, shell );

    verify( renderer ).createUi( shell );
  }

  @Test
  public void testCreateActionsCallsActionCreateUi() {
    RemotePage page = new RemotePage( ui, uiRenderer, descriptor, mock( PageData.class ) );
    page.createActions( RemoteRendererFactory.getInstance(), shell );

    List<ActionRenderer> actions = page.getActionRenderers();

    assertEquals( 1, actions.size() );
    assertEquals( "actionFoo", actions.get( 0 ).getDescriptor().getId() );
  }

  @Test
  public void testUpdateCreatesNewActionsCallsActionCreateUi() {
    List<ActionDescriptor> actions = new ArrayList<ActionDescriptor>();
    actions.add( new ActionDescriptor( "actionFoo", new TestAction() ) );
    when( descriptor.getActions() ).thenReturn( actions );
    RemotePage page = new RemotePage( ui, uiRenderer, descriptor, mock( PageData.class ) );
    page.createActions( RemoteRendererFactory.getInstance(), shell );
    actions.add( new ActionDescriptor( "actionFoo2", new TestAction() ) );

    page.update( descriptor, RemoteRendererFactory.getInstance(), shell );

    List<ActionRenderer> renderers = page.getActionRenderers();
    assertEquals( 2, renderers.size() );
    assertEquals( "actionFoo2", renderers.get( 1 ).getDescriptor().getId() );
  }

  @Test
  public void testUpdateDestroysOldActionsIfDeleted() {
    List<ActionDescriptor> actions = new ArrayList<ActionDescriptor>();
    actions.add( new ActionDescriptor( "actionFoo", new TestAction() ) );
    actions.add( new ActionDescriptor( "actionFoo2", new TestAction() ) );
    when( descriptor.getActions() ).thenReturn( actions );
    RemotePage page = new RemotePage( ui, uiRenderer, descriptor, mock( PageData.class ) );
    page.createActions( RemoteRendererFactory.getInstance(), shell );
    descriptor.getActions().remove( 1 );

    page.update( descriptor, RemoteRendererFactory.getInstance(), shell );

    List<ActionRenderer> renderers = page.getActionRenderers();
    assertEquals( 1, renderers.size() );
    assertEquals( "actionFoo", renderers.get( 0 ).getDescriptor().getId() );
  }

  @Test
  public void testSetTitle() {
    RemotePage page = new RemotePage( ui, uiRenderer, descriptor, mock( PageData.class ) );

    page.setTitle( "foo" );

    verify( remoteObject ).set( "title", "bar" );
  }

  @Test
  public void testGetData() {
    PageData data = mock( PageData.class );
    RemotePage page = new RemotePage( ui, uiRenderer, descriptor, data );

    assertSame( data, page.getData() );
  }

  @Test
  public void testDestroyDoesNotDestroyActions() {
    RemotePage page = new RemotePage( ui, uiRenderer, descriptor, mock( PageData.class ) );
    page.createControl( shell );

    page.destroy();

    verify( remoteObject ).destroy();
  }

  @Test
  public void testDestroyActionsSendsDestroy() {
    RemotePage remotePage = new RemotePage( ui, uiRenderer, descriptor, mock( PageData.class ) );
    remotePage.createControl( shell );

    remotePage.destroy();

    TestPage page = ( TestPage )remotePage.getPage();
    assertTrue( page.wasDestroyed() );
  }

  @Test
  public void testDestroyCallsDestroyOnPage() {
    RemotePage page = new RemotePage( ui, uiRenderer, descriptor, mock( PageData.class ) );
    page.createActions( RemoteRendererFactory.getInstance(), shell );

    page.destroyActions();

    verify( remoteObject ).destroy();
  }

  @Test
  public void testGetDescriptor() {
    RemotePage page = new RemotePage( ui, uiRenderer, descriptor, mock( PageData.class ) );

    assertSame( descriptor, page.getDescriptor() );
  }

  @Test
  public void testGetCreatesControl() {
    RemotePage page = new RemotePage( ui, uiRenderer, descriptor, mock( PageData.class ) );
    page.createControl( shell );

    assertNotNull( page.getControl() );
  }

  @Test
  public void testDestoyDisposesControl() {
    RemotePage page = new RemotePage( ui, uiRenderer, descriptor, mock( PageData.class ) );
    page.createControl( shell );

    page.destroy();

    assertTrue( page.getControl().isDisposed() );
  }

  @Test
  public void testCreatesPage() {
    RemotePage remotePage = new RemotePage( ui, uiRenderer, descriptor, mock( PageData.class ) );

    Page page = remotePage.getPage();

    assertNotNull( page );
    assertTrue( page instanceof TestPage );
  }
}
