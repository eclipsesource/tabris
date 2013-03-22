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

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.PageStore;
import com.eclipsesource.tabris.ui.UIConfiguration;


public class PageOperatorImplTest {

  private Display display;
  private Controller controller;

  @Before
  public void setUp() {
    Fixture.setUp();
    display = new Display();
    Shell shell = new Shell( display );
    shell.setLayout( new StackLayout() );
    controller = new Controller( shell, mock( RemoteUI.class ), mock( UIDescriptor.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullController() {
    new PageOperatorImpl( null, mock( UIImpl.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullUI() {
    new PageOperatorImpl( mock( Controller.class ), null );
  }

  @Test
  public void testGetsCurrentPage() {
    Controller controller = mock( Controller.class );
    when( controller.getCurrentPage() ).thenReturn( mock( Page.class ) );
    UIImpl ui = new UIImpl( display, controller, mock( UIConfiguration.class ) );

    ui.markInitialized();
    ui.getPageOperator().getCurrentPage();

    verify( controller ).getCurrentPage();
  }

  @Test
  public void testGetsPagePageStore() {
    Controller controller = mock( Controller.class );
    when( controller.getCurrentStore() ).thenReturn( mock( PageStore.class ) );
    UIImpl ui = new UIImpl( display, controller, mock( UIConfiguration.class ) );

    ui.markInitialized();
    PageStore store = ui.getPageOperator().getCurrentPageStore();

    verify( controller ).getCurrentStore();
    assertNotNull( store );
  }

  @Test
  public void testClosesCurrentPage() {
    Controller controller = mock( Controller.class );
    UIImpl ui = new UIImpl( display, controller, mock( UIConfiguration.class ) );
    doReturn( Boolean.TRUE ).when( controller ).closeCurrentPage( ui );

    ui.markInitialized();
    ui.getPageOperator().closeCurrentPage();

    verify( controller ).closeCurrentPage( eq( ui ) );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsClosingTopLevelPage() {
    Controller controller = mock( Controller.class );
    UIConfiguration configuration = mock( UIConfiguration.class );
    UIDescriptor contentHolder = new UIDescriptor();
    PageDescriptor page = mock( PageDescriptor.class );
    when( page.getId() ).thenReturn( "foo" );
    doReturn( Boolean.TRUE ).when( page ).isTopLevel();
    contentHolder.add( page );
    when( configuration.getAdapter( UIDescriptor.class ) ).thenReturn( contentHolder );
    UIImpl ui = new UIImpl( display, controller, configuration );
    ui.markInitialized();
    ui.getPageOperator().openPage( "foo" );

    ui.getPageOperator().closeCurrentPage();
  }

  @Test( expected = IllegalStateException.class )
    public void testOpenPageFailsWithNonExistingPage() {
      UIConfiguration configuration = mock( UIConfiguration.class );
      when( configuration.getAdapter( UIDescriptor.class ) ).thenReturn( mock( UIDescriptor.class ) );
      UIImpl ui = new UIImpl( display, controller, configuration );
      ui.markInitialized();

      ui.getPageOperator().openPage( "foo" );
    }

  @Test
  public void testShowsRootPageWithController() {
    Controller controller = mock( Controller.class );
    UIConfiguration configuration = mock( UIConfiguration.class );
    UIDescriptor contentHolder = new UIDescriptor();
    PageDescriptor page = mock( PageDescriptor.class );
    when( page.getId() ).thenReturn( "foo" );
    doReturn( Boolean.TRUE ).when( page ).isTopLevel();
    contentHolder.add( page );
    when( configuration.getAdapter( UIDescriptor.class ) ).thenReturn( contentHolder );
    UIImpl ui = new UIImpl( display, controller, configuration );

    ui.markInitialized();
    ui.getPageOperator().openPage( "foo" );

    verify( controller ).show( eq( ui ), eq( page ), any( PageStore.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testShowsRootPageFailsWithNullPageStore() {
    Controller controller = mock( Controller.class );
    UIConfiguration configuration = mock( UIConfiguration.class );
    UIDescriptor contentHolder = new UIDescriptor();
    PageDescriptor page = mock( PageDescriptor.class );
    when( page.getId() ).thenReturn( "foo" );
    doReturn( Boolean.TRUE ).when( page ).isTopLevel();
    contentHolder.add( page );
    when( configuration.getAdapter( UIDescriptor.class ) ).thenReturn( contentHolder );
    UIImpl ui = new UIImpl( display, controller, configuration );
    ui.markInitialized();

    ui.getPageOperator().openPage( "foo", null );
  }

  @Test
  public void testShowsRootPageWithControllerAndPageStore() {
    Controller controller = mock( Controller.class );
    UIConfiguration configuration = mock( UIConfiguration.class );
    UIDescriptor contentHolder = new UIDescriptor();
    PageDescriptor page = mock( PageDescriptor.class );
    when( page.getId() ).thenReturn( "foo" );
    doReturn( Boolean.TRUE ).when( page ).isTopLevel();
    contentHolder.add( page );
    when( configuration.getAdapter( UIDescriptor.class ) ).thenReturn( contentHolder );
    UIImpl ui = new UIImpl( display, controller, configuration );
    ui.markInitialized();
    PageStore store = mock( PageStore.class );

    ui.getPageOperator().openPage( "foo", store );

    verify( controller ).show( ui, page, store );
  }

  @Test
  public void testShowsPageWithController() {
    Controller controller = mock( Controller.class );
    UIConfiguration configuration = mock( UIConfiguration.class );
    UIImpl ui = new UIImpl( display, controller, configuration );
    UIDescriptor contentHolder = new UIDescriptor();
    PageDescriptor page = mock( PageDescriptor.class );
    when( page.getId() ).thenReturn( "foo" );
    doReturn( Boolean.FALSE ).when( page ).isTopLevel();
    contentHolder.add( page );
    when( configuration.getAdapter( UIDescriptor.class ) ).thenReturn( contentHolder );

    ui.markInitialized();
    ui.getPageOperator().openPage( "foo" );

    verify( controller ).show( eq( ui ), eq( page ), any( PageStore.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testShowsPageFailsWithNullPageStore() {
    Controller controller = mock( Controller.class );
    UIConfiguration configuration = mock( UIConfiguration.class );
    UIImpl ui = new UIImpl( display, controller, configuration );
    UIDescriptor contentHolder = new UIDescriptor();
    PageDescriptor page = mock( PageDescriptor.class );
    when( page.getId() ).thenReturn( "foo" );
    doReturn( Boolean.FALSE ).when( page ).isTopLevel();
    contentHolder.add( page );
    when( configuration.getAdapter( UIDescriptor.class ) ).thenReturn( contentHolder );
    ui.markInitialized();

    ui.getPageOperator().openPage( "foo", null );
  }

  @Test
  public void testShowsPageWithControllerAndPageStore() {
    Controller controller = mock( Controller.class );
    UIConfiguration configuration = mock( UIConfiguration.class );
    UIImpl ui = new UIImpl( display, controller, configuration );
    UIDescriptor contentHolder = new UIDescriptor();
    PageDescriptor page = mock( PageDescriptor.class );
    when( page.getId() ).thenReturn( "foo" );
    doReturn( Boolean.FALSE ).when( page ).isTopLevel();
    contentHolder.add( page );
    when( configuration.getAdapter( UIDescriptor.class ) ).thenReturn( contentHolder );
    PageStore store = mock( PageStore.class );
    ui.markInitialized();

    ui.getPageOperator().openPage( "foo", store );

    verify( controller ).show( ui, page, store );
  }

  @Test( expected = IllegalArgumentException.class )
    public void testSetCurrentPageTitleFailsWithNullPage() {
      Controller controller = mock( Controller.class );
      when( controller.getCurrentPage() ).thenReturn( mock( Page.class ) );
      UIImpl ui = new UIImpl( display, controller, mock( UIConfiguration.class ) );
      ui.markInitialized();

      ui.getPageOperator().setCurrentPageTitle( null, "foo" );
    }

  @Test( expected = IllegalArgumentException.class )
    public void testSetCurrentPageTitleFailsWithNullTitle() {
      Controller controller = mock( Controller.class );
      when( controller.getCurrentPage() ).thenReturn( mock( Page.class ) );
      UIImpl ui = new UIImpl( display, controller, mock( UIConfiguration.class ) );
      ui.markInitialized();

      ui.getPageOperator().setCurrentPageTitle( mock( Page.class ), null );
    }

  @Test
    public void testSetCurrentPageTitleDelegatesToController() {
      Controller controller = mock( Controller.class );
      Page page = mock( Page.class );
      when( controller.getCurrentPage() ).thenReturn( page );
      UIImpl ui = new UIImpl( display, controller, mock( UIConfiguration.class ) );
      ui.markInitialized();

      ui.getPageOperator().setCurrentPageTitle( page, "foo" );

      verify( controller ).setTitle( page, "foo" );
    }

}
