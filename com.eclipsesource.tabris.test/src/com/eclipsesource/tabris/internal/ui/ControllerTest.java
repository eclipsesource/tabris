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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.rwt.internal.remote.RemoteObjectImpl;
import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import com.eclipsesource.tabris.Store;
import com.eclipsesource.tabris.internal.ZIndexStackLayout;
import com.eclipsesource.tabris.test.TabrisTestUtil;
import com.eclipsesource.tabris.ui.TransitionListener;


@SuppressWarnings("restriction")
public class ControllerTest {

  private Shell shell;
  private DescriptorHolder contentHolder;
  private UIContextImpl context;
  private RemoteObjectImpl remoteObject;
  private ZIndexStackLayout layout;
  private TransitionListener listener;

  @Before
  public void setUp() {
    Fixture.setUp();
    Display display = new Display();
    shell = new Shell( display );
    layout = mock( ZIndexStackLayout.class );
    shell.setLayout( layout );
    contentHolder = new DescriptorHolder();
    context = mock( UIContextImpl.class );
    mockUI();
    remoteObject = ( RemoteObjectImpl )TabrisTestUtil.mockRemoteObject();
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
  }

  private void mockUI() {
    UIImpl ui = mock( UIImpl.class );
    List<TransitionListener> listeners = new ArrayList<TransitionListener>();
    listener = mock( TransitionListener.class );
    listeners.add( listener );
    when( ui.getTransitionListeners() ).thenReturn( listeners );
    when( context.getUI() ).thenReturn( ui );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test( expected = IllegalStateException.class )
  public void testCreatesRootPagesFailsWithoutRootPages() {
    PageDescriptor descriptor = mock( PageDescriptor.class );
    doReturn( TestPage.class ).when( descriptor ).getPageType();
    doReturn( Boolean.FALSE ).when( descriptor ).isTopLevel();
    contentHolder.add( descriptor );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );

    controller.createRootPages( context );
  }

  @Test
  public void testCreatesRootPages() {
    PageDescriptor descriptor = createRootPage( "foo" );
    doReturn( TestPage.class ).when( descriptor ).getPageType();

    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );

    controller.createRootPages( context );

    assertEquals( 1, shell.getChildren().length );
  }

  @Test
  public void testCreatesGlobalActions() {
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    when( descriptor.getTitle() ).thenReturn( "foo" );
    contentHolder.add( descriptor );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );

    controller.createGlobalActions( context );

    verify( remoteObject ).set( "title", "foo" );
  }

  @Test
  public void testShowRootPageCreatesControl() {
    createRootPage( "foo" );
    PageDescriptor root2 = createRootPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );

    controller.showRoot( context, root2, mock( Store.class ) );

    assertEquals( 2, shell.getChildren().length );
  }

  @Test
  public void testShowRootPageActivatesPage() {
    PageDescriptor root1 = createRootPage( "foo" );
    PageDescriptor root2 = createRootPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );
    Store store = mock( Store.class );

    controller.showRoot( context, root2, store );

    assertTrue( getTestPage( controller, root1 ).wasDeactivated() );
    assertTrue( getTestPage( controller, root2 ).wasActivated() );
  }

  private TestPage getTestPage( Controller controller, PageDescriptor descriptor ) {
    RemotePage remotePage = controller.getRootPages().get( descriptor );
    return ( TestPage )remotePage.getPage();
  }

  @Test
  public void testShowRootPageActivatesPageWithStore() {
    PageDescriptor root1 = createRootPage( "foo" );
    PageDescriptor root2 = createRootPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );
    Store store = mock( Store.class );

    controller.showRoot( context, root2, store );

    assertTrue( getTestPage( controller, root1 ).wasDeactivated() );
    assertTrue( getTestPage( controller, root2 ).wasActivated() );
  }

  @Test
  public void testShowRootPageActivateRemotePage() {
    createRootPage( "foo" );
    PageDescriptor root2 = createRootPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );

    controller.showRoot( context, root2, mock( Store.class ) );

    verify( remoteUI, times( 2 ) ).activate( remoteObject.getId() );
  }

  @Test
  public void testShowRootPageMakesControlVisible() {
    createRootPage( "foo" );
    PageDescriptor root2 = createRootPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );

    controller.showRoot( context, root2, mock( Store.class ) );

    verify( layout, times( 2 ) ).setOnTopControl( any( Composite.class ) );
  }

  @Test
  public void testShowRootPageCallsListener() {
    PageDescriptor root1 = createRootPage( "foo" );
    PageDescriptor root2 = createRootPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );

    controller.showRoot( context, root2, mock( Store.class ) );

    InOrder order = inOrder( listener );
    order.verify( listener ).before( context, getTestPage( controller, root1 ), getTestPage( controller, root2 ) );
    order.verify( listener ).after( context, getTestPage( controller, root1 ), getTestPage( controller, root2 ) );
  }

  @Test
  public void testShowPageCreatesControl() {
    PageDescriptor root1 = createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );

    TestPage testPage = ( TestPage )controller.showPage( context, page, mock( Store.class ) ).getPage();

    assertTrue( getTestPage( controller, root1 ).wasCreated() );
    assertTrue( testPage.wasCreated() );
  }

  @Test
  public void testShowPageTwiceCreatesControls() {
    PageDescriptor root1 = createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );

    TestPage testPage1 = ( TestPage )controller.showPage( context, page, mock( Store.class ) ).getPage();
    TestPage testPage2 = ( TestPage )controller.showPage( context, page, mock( Store.class ) ).getPage();

    assertTrue( getTestPage( controller, root1 ).wasCreated() );
    assertTrue( testPage1.wasCreated() );
    assertTrue( testPage2.wasCreated() );
  }

  @Test
  public void testShowPageActivatesPage() {
    PageDescriptor root1 = createRootPage( "foo" );
    PageDescriptor page2 = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );
    Store store = mock( Store.class );

    TestPage page = ( TestPage )controller.showPage( context, page2, store ).getPage();

    assertTrue( getTestPage( controller, root1 ).wasDeactivated() );
    assertTrue( page.wasActivated() );
    assertSame( store, controller.getCurrentStore() );
  }

  @Test
  public void testShowPageActivatesPageWithDelegatedStore() {
    PageDescriptor root1 = createRootPage( "foo" );
    PageDescriptor page2 = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );
    Store store = mock( Store.class );

    TestPage page = ( TestPage )controller.showPage( context, page2, store ).getPage();

    assertTrue( getTestPage( controller, root1 ).wasDeactivated() );
    assertTrue( page.wasActivated() );
    assertSame( store, controller.getCurrentStore() );
  }

  @Test
  public void testShowPageActivatesRemotePage() {
    createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );

    controller.showPage( context, page, mock( Store.class ) );

    verify( remoteUI, times( 2 ) ).activate( remoteObject.getId() );
  }

  @Test
  public void testShowPageMakesControlVisible() {
    createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );

    controller.showPage( context, page, mock( Store.class ) );

    verify( layout, times( 2 ) ).setOnTopControl( any( Control.class ) );
  }

  @Test
  public void testShowPageNotifiesListener() {
    PageDescriptor root1 = createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );

    TestPage testPage = ( TestPage )controller.showPage( context, page, mock( Store.class ) ).getPage();

    InOrder order = inOrder( listener );
    order.verify( listener ).before( context, getTestPage( controller, root1 ), testPage );
    order.verify( listener ).after( context, getTestPage( controller, root1 ), testPage );
  }

  @Test
  public void testShowPreviousIsFalseOnRoot() {
    createRootPage( "foo" );
    PageDescriptor page = createRootPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );

    controller.showRoot( context, page, mock( Store.class ) );
    boolean success = controller.showPreviousPage( context );

    assertFalse( success );
  }

  @Test
  public void testShowPreviousIsTrueOnPage() {
    createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );

    controller.showPage( context, page, mock( Store.class ) );
    boolean success = controller.showPreviousPage( context );

    assertTrue( success );
  }

  @Test
  public void testShowPreviousisNotifiesListeners() {
    PageDescriptor rootPage = createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );

    TestPage testPage = ( TestPage )controller.showPage( context, page, mock( Store.class ) ).getPage();
    controller.showPreviousPage( context );

    InOrder order = inOrder( listener );
    order.verify( listener ).before( context, testPage, getTestPage( controller, rootPage ) );
    order.verify( listener ).after( context, testPage, getTestPage( controller, rootPage ) );
  }

  @Test
  public void testShowPreviousMakesControlVisible() {
    createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );

    controller.showPage( context, page, mock( Store.class ) );
    controller.showPreviousPage( context );

    verify( layout, times( 3 ) ).setOnTopControl( any( Composite.class ) );
  }

  @Test
  public void testShowPreviousDestroysRemotePage() {
    createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );

    controller.showPage( context, page, mock( Store.class ) );
    controller.showPreviousPage( context );

    verify( remoteObject ).destroy();
  }

  @Test
  public void testShowPreviousDoesNotEffectPageStore() {
    createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    PageDescriptor page2 = createPage( "bar2" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );
    Store pageStore = mock( Store.class );

    controller.showPage( context, page, pageStore );
    controller.showPage( context, page2, mock( Store.class ) );
    controller.showPreviousPage( context );

    assertSame( pageStore, controller.getCurrentStore() );
  }

  @Test
  public void testShowDeactivatesOldPage() {
    PageDescriptor rootPage = createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );
    Store store = mock( Store.class );

    TestPage testPage = ( TestPage )controller.showPage( context, page, store ).getPage();
    controller.showPreviousPage( context );

    assertTrue( testPage.wasDeactivated() );
    assertTrue( getTestPage( controller, rootPage ).wasActivated() );
  }

  @Test
  public void testGetCurrentPage() {
    createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );

    controller.showPage( context, page, mock( Store.class ) );

    assertTrue( controller.getCurrentPage() instanceof TestPage );
  }

  @Test
  public void testGetCurrentStore() {
    createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );
    Store store = mock( Store.class );

    controller.showPage( context, page, store );

    assertSame( store, controller.getCurrentStore() );
  }

  @Test
  public void testSetTitle() {
    createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );

    controller.showPage( context, page, mock( Store.class ) );
    controller.setCurrentTitle( "foobar" );

    verify( remoteObject ).set( "title", "foobar" );
  }

  @Test
  public void testSetVisibleFindsGlobalAction() {
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    when( descriptor.getId() ).thenReturn( "foo" );
    contentHolder.add( descriptor );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createGlobalActions( context );

    controller.setActionVisible( "foo", true );

    verify( remoteObject ).set( "visibility", true );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetVisibleGlobalActionFailsWithoutAction() {
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    when( descriptor.getId() ).thenReturn( "foo" );
    contentHolder.add( descriptor );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createGlobalActions( context );

    controller.setActionVisible( "foo2", true );
  }

  @Test
  public void testSetEnabledFindsGlobalAction() {
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    when( descriptor.getId() ).thenReturn( "foo" );
    contentHolder.add( descriptor );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createGlobalActions( context );

    controller.setActionEnabled( "foo", true );

    verify( remoteObject ).set( "enabled", true );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetEnabledGlobalActionFailsWithoutAction() {
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    when( descriptor.getId() ).thenReturn( "foo" );
    contentHolder.add( descriptor );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createGlobalActions( context );

    controller.setActionEnabled( "foo2", true );
  }

  @Test
  public void testSetVisibleFindsPageAction() {
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    when( descriptor.getId() ).thenReturn( "foo" );
    PageDescriptor rootPage = createRootPage( "foo" );
    List<ActionDescriptor> actions = new ArrayList<ActionDescriptor>();
    actions.add( descriptor );
    when( rootPage.getActions() ).thenReturn( actions );
    contentHolder.add( descriptor );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );

    controller.setActionVisible( "foo", true );

    verify( remoteObject ).set( "visibility", true );
  }

  @Test
  public void testSetEnabledFindsPageAction() {
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    when( descriptor.getId() ).thenReturn( "foo" );
    PageDescriptor rootPage = createRootPage( "foo" );
    List<ActionDescriptor> actions = new ArrayList<ActionDescriptor>();
    actions.add( descriptor );
    when( rootPage.getActions() ).thenReturn( actions );
    contentHolder.add( descriptor );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );

    controller.setActionEnabled( "foo", true );

    verify( remoteObject ).set( "enabled", true );
  }

  @Test
  public void testFindsPageIdByRemoteId() {
    createRootPage( "foo" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    Controller controller = new Controller( shell, remoteUI, contentHolder );
    controller.createRootPages( context );

    String pageId = controller.getPageId( remoteObject.getId() );

    assertEquals( "foo", pageId );
  }

  private PageDescriptor createRootPage( String id ) {
    PageDescriptor descriptor = mock( PageDescriptor.class );
    when( descriptor.getId() ).thenReturn( id );
    doReturn( TestPage.class ).when( descriptor ).getPageType();
    doReturn( Boolean.TRUE ).when( descriptor ).isTopLevel();
    contentHolder.add( descriptor );
    return descriptor;
  }

  private PageDescriptor createPage( String id ) {
    PageDescriptor descriptor = mock( PageDescriptor.class );
    when( descriptor.getId() ).thenReturn( id );
    doReturn( TestPage.class ).when( descriptor ).getPageType();
    doReturn( Boolean.FALSE ).when( descriptor ).isTopLevel();
    contentHolder.add( descriptor );
    return descriptor;
  }
}
