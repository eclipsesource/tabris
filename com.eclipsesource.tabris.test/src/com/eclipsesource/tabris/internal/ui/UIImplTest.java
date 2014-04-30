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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.test.RWTEnvironment;
import com.eclipsesource.tabris.ui.Action;
import com.eclipsesource.tabris.ui.ActionOperator;
import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.PageOperator;
import com.eclipsesource.tabris.ui.UIConfiguration;


public class UIImplTest {

  @Rule
  public RWTEnvironment environment = new RWTEnvironment();

  private Display display;
  private Controller controller;

  @Before
  public void setUp() {
    display = new Display();
    Shell shell = new Shell( display );
    shell.setLayout( new StackLayout() );
    RemoteUI uiRenderer = mock( RemoteUI.class );
    when( uiRenderer.getPageParent() ).thenReturn( shell );
    controller = new Controller( uiRenderer, mock( UIDescriptor.class ) );
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( UIImpl.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullDisplay() {
    new UIImpl( null, controller, mock( UIConfiguration.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullUI() {
    new UIImpl( display, controller, null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullShell() {
    new UIImpl( display, null, mock( UIConfiguration.class ) );
  }

  @Test
  public void testGetsDisplay() {
    UIImpl ui = new UIImpl( display, controller, mock( UIConfiguration.class ) );

    assertSame( display, ui.getDisplay() );
  }

  @Test
  public void testGetsUI() {
    UIConfiguration configuration = mock( UIConfiguration.class );
    UIImpl ui = new UIImpl( display, controller, configuration );

    assertSame( configuration, ui.getConfiguration() );
  }

  @Test
  public void testGetsActionOperatorIsNullWhenNotInitialized() {
    UIImpl ui = new UIImpl( display, controller, mock( UIConfiguration.class ) );

    assertNull( ui.getActionOperator() );
  }

  @Test
  public void testGetsPageOperatorIsNullWhenNotInitialized() {
    UIImpl ui = new UIImpl( display, controller, mock( UIConfiguration.class ) );

    assertNull( ui.getPageOperator() );
  }

  @Test
  public void testGetsActionOperatorAfterInitialized() {
    UIImpl ui = new UIImpl( display, controller, mock( UIConfiguration.class ) );
    ui.markInitialized();

    assertNotNull( ui.getActionOperator() );
  }

  @Test
  public void testGetsPageOperatorAfterInitialized() {
    UIImpl ui = new UIImpl( display, controller, mock( UIConfiguration.class ) );
    ui.markInitialized();

    assertNotNull( ui.getPageOperator() );
  }

  @Test
  public void testGetsActionOperatorDoesNotCreateANewInstance() {
    UIImpl ui = new UIImpl( display, controller, mock( UIConfiguration.class ) );
    ui.markInitialized();

    ActionOperator operator1 = ui.getActionOperator();
    ActionOperator operator2 = ui.getActionOperator();

    assertSame( operator1, operator2 );
  }

  @Test
  public void testGetsPageOperatorDoesNotCreateANewInstance() {
    UIImpl ui = new UIImpl( display, controller, mock( UIConfiguration.class ) );
    ui.markInitialized();

    PageOperator operator1 = ui.getPageOperator();
    PageOperator operator2 = ui.getPageOperator();

    assertSame( operator1, operator2 );
  }

  @Test
  public void testGetsPageConfigurationFromController() {
    Controller controller = mock( Controller.class );
    UIImpl ui = new UIImpl( display, controller, mock( UIConfiguration.class ) );
    Page page = mock( Page.class );

    ui.getPageConfiguration( page );

    verify( controller ).getPageConfiguration( page );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testGetPageConfigurationFailsWithNullPage() {
    Controller controller = mock( Controller.class );
    UIImpl ui = new UIImpl( display, controller, mock( UIConfiguration.class ) );

    ui.getPageConfiguration( null );
  }

  @Test
  public void testGetsActionConfigurationFromController() {
    Controller controller = mock( Controller.class );
    UIImpl ui = new UIImpl( display, controller, mock( UIConfiguration.class ) );
    Action action = mock( Action.class );

    ui.getActionConfiguration( action );

    verify( controller ).getActionConfiguration( action );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testGetActionConfigurationFailsWithNullAction() {
    Controller controller = mock( Controller.class );
    UIImpl ui = new UIImpl( display, controller, mock( UIConfiguration.class ) );

    ui.getActionConfiguration( null );
  }

}
