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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.rap.rwt.RWT;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.eclipsesource.tabris.TabrisClient;
import com.eclipsesource.tabris.internal.ZIndexStackLayout;
import com.eclipsesource.tabris.internal.ui.rendering.ActionRenderer;
import com.eclipsesource.tabris.internal.ui.rendering.PageRenderer;
import com.eclipsesource.tabris.internal.ui.rendering.RendererFactory;
import com.eclipsesource.tabris.internal.ui.rendering.UIRenderer;
import com.eclipsesource.tabris.test.TabrisTestUtil;
import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.PageConfiguration;
import com.eclipsesource.tabris.ui.PageData;
import com.eclipsesource.tabris.ui.TransitionListener;
import com.eclipsesource.tabris.ui.UI;
import com.eclipsesource.tabris.ui.UIConfiguration;


@SuppressWarnings("restriction")
public class ControllerTest {

  private Shell shell;
  private UIDescriptor uiDescriptor;
  private UIImpl ui;
  private RemoteObjectImpl remoteObject;
  private ZIndexStackLayout layout;
  private TransitionListener listener;

  @Before
  public void setUp() {
    Fixture.setUp();
    Fixture.fakeClient( mock( TabrisClient.class ) );
    Display display = new Display();
    shell = new Shell( display );
    layout = mock( ZIndexStackLayout.class );
    shell.setLayout( layout );
    uiDescriptor = spy( new UIDescriptor() );
    ui = mock( UIImpl.class );
    mockUI();
    remoteObject = ( RemoteObjectImpl )TabrisTestUtil.mockRemoteObject();
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
  }

  private void mockUI() {
    UIConfiguration configuration = mock( UIConfiguration.class );
    listener = mock( TransitionListener.class );
    when( configuration.getAdapter( UIDescriptor.class ) ).thenReturn( uiDescriptor );
    uiDescriptor.addTransitionListener( listener );
    when( ui.getConfiguration() ).thenReturn( configuration );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( Controller.class ) );
  }


  @Test
  public void testRegistersItselfAsUpdater() {
    PageDescriptor descriptor = createRootPage( "foo" );
    doReturn( TestPage.class ).when( descriptor ).getPageType();
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );

    Controller controller = new Controller( remoteUI, uiDescriptor );

    Object attribute = RWT.getUISession().getAttribute( UpdateUtil.UPDATER_PROPERTY );
    assertSame( controller, attribute );
  }

  @Test( expected = IllegalStateException.class )
  public void testCreatesRootPagesFailsWithoutRootPages() {
    PageDescriptor descriptor = mock( PageDescriptor.class );
    doReturn( TestPage.class ).when( descriptor ).getPageType();
    doReturn( Boolean.FALSE ).when( descriptor ).isTopLevel();
    uiDescriptor.add( descriptor );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );

    controller.createRootPages( ui );
  }

  @Test
  public void testCreatesRootPages() {
    PageDescriptor descriptor = createRootPage( "foo" );
    doReturn( TestPage.class ).when( descriptor ).getPageType();

    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );

    controller.createRootPages( ui );

    assertEquals( 1, shell.getChildren().length );
  }

  @Test
  public void testUpdateCreatesNewRootPages() {
    PageDescriptor descriptor = createRootPage( "foo" );
    doReturn( TestPage.class ).when( descriptor ).getPageType();
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    when( remoteUI.getActionsParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );
    UIConfiguration configuration = mock( UIConfiguration.class );
    PageDescriptor descriptor2 = createRootPage( "fooBar" );
    when( configuration.getAdapter( UIDescriptor.class ) ).thenReturn( uiDescriptor );

    controller.update( configuration );

    Map<PageDescriptor, PageRenderer> rootPages = controller.getRootPages();
    assertNotNull( rootPages.get( descriptor2 ) );
  }

  @Test
  public void testUpdateCreatesUIElementForNewRootPages() {
    PageDescriptor descriptor = createRootPage( "foo" );
    doReturn( TestPage.class ).when( descriptor ).getPageType();
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    when( remoteUI.getActionsParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );
    UIConfiguration configuration = mock( UIConfiguration.class );
    createRootPage( "fooBar" );
    when( configuration.getAdapter( UIDescriptor.class ) ).thenReturn( uiDescriptor );

    controller.update( configuration );

    assertEquals( 2, shell.getChildren().length );
  }

  @Test
  public void testCreatesGlobalActions() {
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    when( descriptor.getTitle() ).thenReturn( "foo" );
    uiDescriptor.add( descriptor );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );

    controller.createGlobalActions( ui );

    verify( remoteObject ).set( "title", "foo" );
  }

  @Test
  public void testUpdateCreatesNewGlobalActions() {
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    when( descriptor.getTitle() ).thenReturn( "foo" );
    when( descriptor.getId() ).thenReturn( "foo" );
    uiDescriptor.add( descriptor );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    when( remoteUI.getActionsParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createGlobalActions( ui );
    ActionDescriptor descriptor2 = mock( ActionDescriptor.class );
    when( descriptor2.getTitle() ).thenReturn( "foo2" );
    when( descriptor2.getId() ).thenReturn( "foo2" );
    uiDescriptor.add( descriptor2 );
    UIConfiguration configuration = mock( UIConfiguration.class );
    when( configuration.getAdapter( UIDescriptor.class ) ).thenReturn( uiDescriptor );

    controller.update( configuration );

    verify( remoteObject ).set( "title", "foo2" );
  }

  @Test
  public void testUpdateRemovesDestroyedGlobalActions() {
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    when( descriptor.getTitle() ).thenReturn( "foo" );
    when( descriptor.getId() ).thenReturn( "foo" );
    uiDescriptor.add( descriptor );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    when( remoteUI.getActionsParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createGlobalActions( ui );
    when( uiDescriptor.getGlobalActions() ).thenReturn( new ArrayList<ActionDescriptor>() );
    UIConfiguration configuration = mock( UIConfiguration.class );
    when( configuration.getAdapter( UIDescriptor.class ) ).thenReturn( uiDescriptor );

    controller.update( configuration );

    verify( remoteObject ).destroy();
  }

  @Test
  public void testCreatesGlobalActionsWithUI() {
    ActionRenderer renderer = mock( ActionRenderer.class );
    RemoteRendererFactory factory = mock( RemoteRendererFactory.class );
    when( factory.createActionRenderer( any( UI.class ), any( UIRenderer.class ), any( ActionDescriptor.class ) ) ).thenReturn( renderer );
    when( uiDescriptor.getRendererFactory() ).thenReturn( factory );
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    when( descriptor.getTitle() ).thenReturn( "foo" );
    uiDescriptor.add( descriptor );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getActionsParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );

    controller.createGlobalActions( ui );

    verify( renderer ).createUi( shell );
  }

  @Test
  public void testShowRootPageCreatesControl() {
    createRootPage( "foo" );
    PageDescriptor root2 = createRootPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );

    controller.showRoot( ui, root2, mock( PageData.class ) );

    assertEquals( 2, shell.getChildren().length );
  }

  @Test
  public void testShowRootPageActivatesPage() {
    PageDescriptor root1 = createRootPage( "foo" );
    PageDescriptor root2 = createRootPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );
    PageData data = mock( PageData.class );

    controller.showRoot( ui, root2, data );

    assertTrue( getTestPage( controller, root1 ).wasDeactivated() );
    assertTrue( getTestPage( controller, root2 ).wasActivated() );
  }

  private TestPage getTestPage( Controller controller, PageDescriptor descriptor ) {
    PageRenderer remotePage = controller.getRootPages().get( descriptor );
    return ( TestPage )remotePage.getPage();
  }

  @Test
  public void testShowRootPageActivatesPageWithPageData() {
    PageDescriptor root1 = createRootPage( "foo" );
    PageDescriptor root2 = createRootPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );
    PageData data = mock( PageData.class );

    controller.showRoot( ui, root2, data );

    assertTrue( getTestPage( controller, root1 ).wasDeactivated() );
    assertTrue( getTestPage( controller, root2 ).wasActivated() );
  }

  @Test
  public void testShowRootPageActivateRemotePage() {
    createRootPage( "foo" );
    PageDescriptor root2 = createRootPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );

    controller.showRoot( ui, root2, mock( PageData.class ) );

    PageRenderer pageRenderer = controller.getRootPages().get( root2 );
    verify( remoteUI ).activate( pageRenderer );
  }

  @Test
  public void testShowRootPageMakesControlVisible() {
    createRootPage( "foo" );
    PageDescriptor root2 = createRootPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );

    controller.showRoot( ui, root2, mock( PageData.class ) );

    verify( layout, times( 2 ) ).setOnTopControl( any( Composite.class ) );
  }

  @Test
  public void testShowRootPageCallsListener() {
    PageDescriptor root1 = createRootPage( "foo" );
    PageDescriptor root2 = createRootPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );

    controller.showRoot( ui, root2, mock( PageData.class ) );

    InOrder order = inOrder( listener );
    order.verify( listener ).before( ui, getTestPage( controller, root1 ), getTestPage( controller, root2 ) );
    order.verify( listener ).after( ui, getTestPage( controller, root1 ), getTestPage( controller, root2 ) );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsToRemovePageFromCurrentFlow() {
    createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );
    controller.showPage( ui, page, mock( PageData.class ) ).getPage();
    PageConfiguration configuration = mock( PageConfiguration.class );
    when( configuration.getAdapter( PageDescriptor.class ) ).thenReturn( page );

    controller.remove( configuration );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsToRemoveRootPageFromCurrentFlow() {
    PageDescriptor rootPage = createRootPage( "foo" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );
    controller.showRoot( ui, rootPage, new PageData() );
    PageConfiguration configuration = mock( PageConfiguration.class );
    when( configuration.getAdapter( PageDescriptor.class ) ).thenReturn( rootPage );

    controller.remove( configuration );
  }

  @Test
  public void testRemovesRootPageCallsDestroyOnRenderer() {
    PageDescriptor rootPage = createRootPage( "foo" );
    PageDescriptor rootPage2 = createRootPage( "foo1" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );
    controller.showRoot( ui, rootPage, new PageData() );
    PageConfiguration configuration = mock( PageConfiguration.class );
    when( configuration.getAdapter( PageDescriptor.class ) ).thenReturn( rootPage2 );

    controller.remove( configuration );

    verify( remoteObject ).destroy();
  }

  @Test
  public void testShowPageCreatesControl() {
    PageDescriptor root1 = createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );

    TestPage testPage = ( TestPage )controller.showPage( ui, page, mock( PageData.class ) ).getPage();

    assertTrue( getTestPage( controller, root1 ).wasCreated() );
    assertTrue( testPage.wasCreated() );
  }

  @Test
  public void testUpdatePageConfigurationTriggersPageUpdate() {
    RendererFactory factory = mockRendererFactory();
    when( uiDescriptor.getRendererFactory() ).thenReturn( factory );
    createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    when( remoteUI.getActionsParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );
    controller.showPage( ui, page, mock( PageData.class ) ).getPage();
    PageConfiguration configuration = mock( PageConfiguration.class );
    when( configuration.getAdapter( PageDescriptor.class ) ).thenReturn( page );

    controller.update( configuration );

    PageRenderer renderer = controller.getAllPages().get( 2 );
    verify( renderer ).update( page, factory, shell );
  }

  private RendererFactory mockRendererFactory() {
    RendererFactory factory = mock( RendererFactory.class );
    doAnswer( new Answer<PageRenderer>() {

      @Override
      public PageRenderer answer( InvocationOnMock invocation ) throws Throwable {
        PageRenderer renderer = mock( PageRenderer.class );
        when( renderer.getData() ).thenReturn( ( PageData )invocation.getArguments()[ 3 ] );
        when( renderer.getPage() ).thenReturn( mock( Page.class ) );
        when( renderer.getDescriptor() ).thenReturn( ( PageDescriptor )invocation.getArguments()[ 2 ] );
        return renderer;
      }
    } ).when( factory ).createPageRenderer( any( UI.class ),
                                            any( UIRenderer.class ),
                                            any( PageDescriptor.class ),
                                            any( PageData.class ) );
    return factory;
  }

  @Test
  public void testShowPageTwiceCreatesControls() {
    PageDescriptor root1 = createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );

    TestPage testPage1 = ( TestPage )controller.showPage( ui, page, mock( PageData.class ) ).getPage();
    TestPage testPage2 = ( TestPage )controller.showPage( ui, page, mock( PageData.class ) ).getPage();

    assertTrue( getTestPage( controller, root1 ).wasCreated() );
    assertTrue( testPage1.wasCreated() );
    assertTrue( testPage2.wasCreated() );
  }

  @Test
  public void testShowPageActivatesPage() {
    PageDescriptor root1 = createRootPage( "foo" );
    PageDescriptor page2 = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );
    PageData data = mock( PageData.class );

    TestPage page = ( TestPage )controller.showPage( ui, page2, data ).getPage();

    assertTrue( getTestPage( controller, root1 ).wasDeactivated() );
    assertTrue( page.wasActivated() );
    assertSame( data, controller.getCurrentData() );
  }

  @Test
  public void testShowPageActivatesPageWithDelegatedPageData() {
    PageDescriptor root1 = createRootPage( "foo" );
    PageDescriptor page2 = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );
    PageData data = mock( PageData.class );

    TestPage page = ( TestPage )controller.showPage( ui, page2, data ).getPage();

    assertTrue( getTestPage( controller, root1 ).wasDeactivated() );
    assertTrue( page.wasActivated() );
    assertSame( data, controller.getCurrentData() );
  }

  @Test
  public void testShowPageActivatesRemotePage() {
    createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );

    controller.showPage( ui, page, mock( PageData.class ) );

    ArgumentCaptor<PageRenderer> captor = ArgumentCaptor.forClass( PageRenderer.class );
    verify( remoteUI, times( 2 ) ).activate( captor.capture() );
    assertSame( page, captor.getAllValues().get( 1 ).getDescriptor() );
  }

  @Test
  public void testShowPageMakesControlVisible() {
    createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );

    controller.showPage( ui, page, mock( PageData.class ) );

    verify( layout, times( 2 ) ).setOnTopControl( any( Control.class ) );
  }

  @Test
  public void testShowPageNotifiesListener() {
    PageDescriptor root1 = createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );

    TestPage testPage = ( TestPage )controller.showPage( ui, page, mock( PageData.class ) ).getPage();

    InOrder order = inOrder( listener );
    order.verify( listener ).before( ui, getTestPage( controller, root1 ), testPage );
    order.verify( listener ).after( ui, getTestPage( controller, root1 ), testPage );
  }

  @Test
  public void testShowPreviousIsFalseOnRoot() {
    createRootPage( "foo" );
    PageDescriptor page = createRootPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );

    controller.showRoot( ui, page, mock( PageData.class ) );
    boolean success = controller.closeCurrentPage( ui );

    assertFalse( success );
  }

  @Test
  public void testShowPreviousCreatesActionsBeforeActive() {
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    PageDescriptor rootPage = createRootPage( "foo" );
    List<ActionDescriptor> actions = new ArrayList<ActionDescriptor>();
    actions.add( descriptor );
    when( rootPage.getActions() ).thenReturn( actions );
    RendererFactory factory = spy( RemoteRendererFactory.getInstance() );
    when( uiDescriptor.getRendererFactory() ).thenReturn( factory );
    PageDescriptor pageDescriptor = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );

    controller.showPage( ui, pageDescriptor, mock( PageData.class ) );
    controller.closeCurrentPage( ui );

    InOrder order = inOrder( factory, remoteUI );
    order.verify( factory ).createActionRenderer( ui, remoteUI, descriptor );
    order.verify( factory ).createActionRenderer( ui, remoteUI, descriptor );
    order.verify( remoteUI ).activate( any( PageRenderer.class ) );
  }

  @Test
  public void testShowPageCreatesActionsBeforeActive() {
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    createRootPage( "foo" );
    List<ActionDescriptor> actions = new ArrayList<ActionDescriptor>();
    actions.add( descriptor );
    RendererFactory factory = spy( RemoteRendererFactory.getInstance() );
    when( uiDescriptor.getRendererFactory() ).thenReturn( factory );
    PageDescriptor pageDescriptor = createPage( "bar" );
    when( pageDescriptor.getActions() ).thenReturn( actions );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );

    controller.showPage( ui, pageDescriptor, mock( PageData.class ) );

    InOrder order = inOrder( factory, remoteUI );
    order.verify( factory ).createActionRenderer( ui, remoteUI, descriptor );
    order.verify( remoteUI ).activate( any( PageRenderer.class ) );
  }

  @Test
  public void testShowRootCreatesActionsBeforeActive() {
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    PageDescriptor rootPage = createRootPage( "foo" );
    List<ActionDescriptor> actions = new ArrayList<ActionDescriptor>();
    actions.add( descriptor );
    RendererFactory factory = spy( RemoteRendererFactory.getInstance() );
    when( uiDescriptor.getRendererFactory() ).thenReturn( factory );
    when( rootPage.getActions() ).thenReturn( actions );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );

    controller.showRoot( ui, rootPage, mock( PageData.class ) );

    InOrder order = inOrder( factory, remoteUI );
    order.verify( factory ).createActionRenderer( ui, remoteUI, descriptor );
    order.verify( factory ).createActionRenderer( ui, remoteUI, descriptor );
    order.verify( remoteUI ).activate( any( PageRenderer.class ) );
  }

  @Test
  public void testShowPreviousIsTrueOnPage() {
    createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );

    controller.showPage( ui, page, mock( PageData.class ) );
    boolean success = controller.closeCurrentPage( ui );

    assertTrue( success );
  }

  @Test
  public void testShowPreviousCallsDeactivateWithOldCurrentPage() {
    createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    when( ui.getPageOperator() ).thenReturn( new PageOperatorImpl( controller, ui ) );
    controller.createRootPages( ui );
    PageRenderer renderer = controller.showPage( ui, page, mock( PageData.class ) );
    TestPage testPage = ( TestPage )renderer.getPage();

    controller.closeCurrentPage( ui );

    Page pageAtDeactivate = testPage.getPageAtDeactivate();
    assertSame( testPage, pageAtDeactivate );
  }

  @Test
  public void testShowPreviousisNotifiesListeners() {
    PageDescriptor rootPage = createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );

    TestPage testPage = ( TestPage )controller.showPage( ui, page, mock( PageData.class ) ).getPage();
    controller.closeCurrentPage( ui );

    InOrder order = inOrder( listener );
    order.verify( listener ).before( ui, testPage, getTestPage( controller, rootPage ) );
    order.verify( listener ).after( ui, testPage, getTestPage( controller, rootPage ) );
  }

  @Test
  public void testShowPreviousMakesControlVisible() {
    createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );

    controller.showPage( ui, page, mock( PageData.class ) );
    controller.closeCurrentPage( ui );

    verify( layout, times( 3 ) ).setOnTopControl( any( Composite.class ) );
  }

  @Test
  public void testShowPreviousDestroysRemotePage() {
    createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );

    controller.showPage( ui, page, mock( PageData.class ) );
    controller.closeCurrentPage( ui );

    verify( remoteObject ).destroy();
  }

  @Test
  public void testShowPreviousDoesNotEffectPagePageData() {
    createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    PageDescriptor page2 = createPage( "bar2" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );
    PageData pagePageData = mock( PageData.class );

    controller.showPage( ui, page, pagePageData );
    controller.showPage( ui, page2, mock( PageData.class ) );
    controller.closeCurrentPage( ui );

    assertSame( pagePageData, controller.getCurrentData() );
  }

  @Test
  public void testShowDeactivatesOldPage() {
    PageDescriptor rootPage = createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );
    PageData data = mock( PageData.class );

    TestPage testPage = ( TestPage )controller.showPage( ui, page, data ).getPage();
    controller.closeCurrentPage( ui );

    assertTrue( testPage.wasDeactivated() );
    assertTrue( getTestPage( controller, rootPage ).wasActivated() );
  }

  @Test
  public void testGetCurrentPage() {
    createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );

    controller.showPage( ui, page, mock( PageData.class ) );

    assertTrue( controller.getCurrentPage() instanceof TestPage );
  }

  @Test
  public void testGetCurrentPageId() {
    createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );
    controller.showPage( ui, page, mock( PageData.class ) );

    String currentPageId = controller.getCurrentPageId();

    assertEquals( "bar", currentPageId );
  }

  @Test
  public void testGetCurrentPageData() {
    createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );
    PageData data = mock( PageData.class );

    controller.showPage( ui, page, data );

    assertSame( data, controller.getCurrentData() );
  }

  @Test
  public void testSetTitle() {
    createRootPage( "foo" );
    PageDescriptor page = createPage( "bar" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );

    PageRenderer remotePage = controller.showPage( ui, page, mock( PageData.class ) );
    controller.setTitle( remotePage.getPage(), "foobar" );

    verify( remoteObject ).set( "title", "foobar" );
  }

  @Test( expected = IllegalStateException.class )
  public void testSetTitleFailsWithNonExistingPage() {
    createRootPage( "foo" );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );

    controller.setTitle( mock( Page.class ), "foobar" );
  }

  @Test
  public void testSetVisibleFindsGlobalAction() {
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    when( descriptor.getId() ).thenReturn( "foo" );
    uiDescriptor.add( descriptor );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createGlobalActions( ui );

    controller.setActionVisible( "foo", true );

    verify( remoteObject ).set( "visibility", true );
  }

  @Test( expected = IllegalStateException.class )
  public void testSetVisibleGlobalActionFailsWithoutAction() {
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    when( descriptor.getId() ).thenReturn( "foo" );
    uiDescriptor.add( descriptor );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createGlobalActions( ui );

    controller.setActionVisible( "foo2", true );
  }

  @Test
  public void testSetEnabledFindsGlobalAction() {
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    when( descriptor.getId() ).thenReturn( "foo" );
    uiDescriptor.add( descriptor );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createGlobalActions( ui );

    controller.setActionEnabled( "foo", true );

    verify( remoteObject ).set( "enabled", true );
  }

  @Test( expected = IllegalStateException.class )
  public void testSetEnabledGlobalActionFailsWithoutAction() {
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    when( descriptor.getId() ).thenReturn( "foo" );
    uiDescriptor.add( descriptor );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createGlobalActions( ui );

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
    uiDescriptor.add( descriptor );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );

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
    uiDescriptor.add( descriptor );
    RemoteUI remoteUI = mock( RemoteUI.class );
    when( remoteUI.getPageParent() ).thenReturn( shell );
    Controller controller = new Controller( remoteUI, uiDescriptor );
    controller.createRootPages( ui );

    controller.setActionEnabled( "foo", true );

    verify( remoteObject ).set( "enabled", true );
  }

  private PageDescriptor createRootPage( String id ) {
    PageDescriptor descriptor = mock( PageDescriptor.class );
    when( descriptor.getId() ).thenReturn( id );
    doReturn( TestPage.class ).when( descriptor ).getPageType();
    doReturn( Boolean.TRUE ).when( descriptor ).isTopLevel();
    uiDescriptor.add( descriptor );
    return descriptor;
  }

  private PageDescriptor createPage( String id ) {
    PageDescriptor descriptor = mock( PageDescriptor.class );
    when( descriptor.getId() ).thenReturn( id );
    doReturn( TestPage.class ).when( descriptor ).getPageType();
    doReturn( Boolean.FALSE ).when( descriptor ).isTopLevel();
    uiDescriptor.add( descriptor );
    return descriptor;
  }

}
