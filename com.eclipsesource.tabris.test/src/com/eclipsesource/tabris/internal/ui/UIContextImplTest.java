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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.Store;
import com.eclipsesource.tabris.ui.ActionManager;
import com.eclipsesource.tabris.ui.PageManager;


public class UIContextImplTest {

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

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullDisplay() {
    new UIContextImpl( null, controller, mock( UIImpl.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullUI() {
    new UIContextImpl( display, controller, null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullShell() {
    new UIContextImpl( display, null, mock( UIImpl.class ) );
  }

  @Test
  public void testGetsDisplay() {
    UIContextImpl context = new UIContextImpl( display, controller, mock( UIImpl.class ) );

    assertSame( display, context.getDisplay() );
  }

  @Test
  public void testGetsUI() {
    UIImpl ui = mock( UIImpl.class );
    UIContextImpl context = new UIContextImpl( display, controller, ui );

    assertSame( ui, context.getUI() );
  }

  @Test
  public void testGetStore() {
    UIContextImpl context = new UIContextImpl( display, controller, mock( UIImpl.class ) );

    Store store1 = context.getGlobalStore();
    Store store2 = context.getGlobalStore();

    assertNotNull( store1 );
    assertSame( store1, store2 );
  }

  @Test
  public void testGetsActionManagerIsNullWhenNotInitialized() {
    UIContextImpl context = new UIContextImpl( display, controller, mock( UIImpl.class ) );

    assertNull( context.getActionManager() );
  }

  @Test
  public void testGetsPageManagerIsNullWhenNotInitialized() {
    UIContextImpl context = new UIContextImpl( display, controller, mock( UIImpl.class ) );

    assertNull( context.getPageManager() );
  }

  @Test
  public void testGetsActionManagerAfterInitialized() {
    UIContextImpl context = new UIContextImpl( display, controller, mock( UIImpl.class ) );
    context.markInitialized();

    assertNotNull( context.getActionManager() );
  }

  @Test
  public void testGetsPageManagerAfterInitialized() {
    UIContextImpl context = new UIContextImpl( display, controller, mock( UIImpl.class ) );
    context.markInitialized();

    assertNotNull( context.getPageManager() );
  }

  @Test
  public void testGetsActionManagerDoesNotCreateANewInstance() {
    UIContextImpl context = new UIContextImpl( display, controller, mock( UIImpl.class ) );
    context.markInitialized();

    ActionManager manager1 = context.getActionManager();
    ActionManager manager2 = context.getActionManager();

    assertSame( manager1, manager2 );
  }

  @Test
  public void testGetsPageManagerDoesNotCreateANewInstance() {
    UIContextImpl context = new UIContextImpl( display, controller, mock( UIImpl.class ) );
    context.markInitialized();

    PageManager manager1 = context.getPageManager();
    PageManager manager2 = context.getPageManager();

    assertSame( manager1, manager2 );
  }

}
