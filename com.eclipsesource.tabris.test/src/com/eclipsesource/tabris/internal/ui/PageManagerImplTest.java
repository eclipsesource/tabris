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

import com.eclipsesource.tabris.Store;
import com.eclipsesource.tabris.ui.Page;


public class PageManagerImplTest {

  private Display display;
  private Controller controller;

  @Before
  public void setUp() {
    Fixture.setUp();
    display = new Display();
    Shell shell = new Shell( display );
    shell.setLayout( new StackLayout() );
    controller = new Controller( shell, mock( RemoteUI.class ), mock( DescriptorHolder.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullController() {
    new PageManagerImpl( null, mock( UIContextImpl.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullContext() {
    new PageManagerImpl( mock( Controller.class ), null );
  }

  @Test
  public void testGetsCurrentPage() {
    Controller controller = mock( Controller.class );
    when( controller.getCurrentPage() ).thenReturn( mock( Page.class ) );
    UIContextImpl context = new UIContextImpl( display, controller, mock( UIImpl.class ) );

    context.markInitialized();
    context.getPageManager().getPage();

    verify( controller ).getCurrentPage();
  }

  @Test
  public void testGetsPageStore() {
    Controller controller = mock( Controller.class );
    when( controller.getCurrentStore() ).thenReturn( mock( Store.class ) );
    UIContextImpl context = new UIContextImpl( display, controller, mock( UIImpl.class ) );

    context.markInitialized();
    Store store = context.getPageManager().getPageStore();

    verify( controller ).getCurrentStore();
    assertNotNull( store );
  }

  @Test
  public void testShowsPreviousPage() {
    Controller controller = mock( Controller.class );
    UIContextImpl context = new UIContextImpl( display, controller, mock( UIImpl.class ) );

    context.markInitialized();
    context.getPageManager().showPreviousPage();

    verify( controller ).showPreviousPage( eq( context ) );
  }

  @Test( expected = IllegalStateException.class )
  public void testShowPageFailsWithNonExistingPage() {
    UIImpl ui = mock( UIImpl.class );
    when( ui.getDescriptorHolder() ).thenReturn( mock( DescriptorHolder.class ) );
    UIContextImpl context = new UIContextImpl( display, controller, ui );
    context.markInitialized();

    context.getPageManager().showPage( "foo" );
  }

  @Test
  public void testShowsRootPageWithController() {
    Controller controller = mock( Controller.class );
    UIImpl ui = mock( UIImpl.class );
    DescriptorHolder contentHolder = new DescriptorHolder();
    PageDescriptor page = mock( PageDescriptor.class );
    when( page.getId() ).thenReturn( "foo" );
    doReturn( Boolean.TRUE ).when( page ).isTopLevel();
    contentHolder.add( page );
    when( ui.getDescriptorHolder() ).thenReturn( contentHolder );
    UIContextImpl context = new UIContextImpl( display, controller, ui );

    context.markInitialized();
    context.getPageManager().showPage( "foo" );

    verify( controller ).show( eq( context ), eq( page ), any( Store.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testShowsRootPageFailsWithNullStore() {
    Controller controller = mock( Controller.class );
    UIImpl ui = mock( UIImpl.class );
    DescriptorHolder contentHolder = new DescriptorHolder();
    PageDescriptor page = mock( PageDescriptor.class );
    when( page.getId() ).thenReturn( "foo" );
    doReturn( Boolean.TRUE ).when( page ).isTopLevel();
    contentHolder.add( page );
    when( ui.getDescriptorHolder() ).thenReturn( contentHolder );
    UIContextImpl context = new UIContextImpl( display, controller, ui );
    context.markInitialized();

    context.getPageManager().showPage( "foo", null );
  }

  @Test
  public void testShowsRootPageWithControllerAndStore() {
    Controller controller = mock( Controller.class );
    UIImpl ui = mock( UIImpl.class );
    DescriptorHolder contentHolder = new DescriptorHolder();
    PageDescriptor page = mock( PageDescriptor.class );
    when( page.getId() ).thenReturn( "foo" );
    doReturn( Boolean.TRUE ).when( page ).isTopLevel();
    contentHolder.add( page );
    when( ui.getDescriptorHolder() ).thenReturn( contentHolder );
    UIContextImpl context = new UIContextImpl( display, controller, ui );
    context.markInitialized();
    Store store = mock( Store.class );

    context.getPageManager().showPage( "foo", store );

    verify( controller ).show( context, page, store );
  }

  @Test
  public void testShowsPageWithController() {
    Controller controller = mock( Controller.class );
    UIImpl ui = mock( UIImpl.class );
    UIContextImpl context = new UIContextImpl( display, controller, ui );
    DescriptorHolder contentHolder = new DescriptorHolder();
    PageDescriptor page = mock( PageDescriptor.class );
    when( page.getId() ).thenReturn( "foo" );
    doReturn( Boolean.FALSE ).when( page ).isTopLevel();
    contentHolder.add( page );
    when( ui.getDescriptorHolder() ).thenReturn( contentHolder );

    context.markInitialized();
    context.getPageManager().showPage( "foo" );

    verify( controller ).show( eq( context ), eq( page ), any( Store.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testShowsPageFailsWithNullStore() {
    Controller controller = mock( Controller.class );
    UIImpl ui = mock( UIImpl.class );
    UIContextImpl context = new UIContextImpl( display, controller, ui );
    DescriptorHolder contentHolder = new DescriptorHolder();
    PageDescriptor page = mock( PageDescriptor.class );
    when( page.getId() ).thenReturn( "foo" );
    doReturn( Boolean.FALSE ).when( page ).isTopLevel();
    contentHolder.add( page );
    when( ui.getDescriptorHolder() ).thenReturn( contentHolder );
    context.markInitialized();

    context.getPageManager().showPage( "foo", null );
  }

  @Test
  public void testShowsPageWithControllerAndStore() {
    Controller controller = mock( Controller.class );
    UIImpl ui = mock( UIImpl.class );
    UIContextImpl context = new UIContextImpl( display, controller, ui );
    DescriptorHolder contentHolder = new DescriptorHolder();
    PageDescriptor page = mock( PageDescriptor.class );
    when( page.getId() ).thenReturn( "foo" );
    doReturn( Boolean.FALSE ).when( page ).isTopLevel();
    contentHolder.add( page );
    when( ui.getDescriptorHolder() ).thenReturn( contentHolder );
    Store store = mock( Store.class );
    context.markInitialized();

    context.getPageManager().showPage( "foo", store );

    verify( controller ).show( context, page, store );
  }

}
