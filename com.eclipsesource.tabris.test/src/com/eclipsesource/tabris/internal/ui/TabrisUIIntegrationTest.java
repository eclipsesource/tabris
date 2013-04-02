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

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.internal.ZIndexStackLayout;
import com.eclipsesource.tabris.ui.AbstractPage;
import com.eclipsesource.tabris.ui.ActionConfiguration;
import com.eclipsesource.tabris.ui.PageConfiguration;
import com.eclipsesource.tabris.ui.PageData;
import com.eclipsesource.tabris.ui.UIConfiguration;


public class TabrisUIIntegrationTest {

  @Before
  public void setUp() {
    Fixture.setUp();
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testCanEnableActionInPageCreate() {
    UIImpl ui = createUI( ActionEnablingTestPage.class );

    ui.getPageOperator().openPage( "foo" );
  }

  @Test
  public void testCanSetActionVisibleInPageCreate() {
    UIImpl ui = createUI( ActionVisibleChangingTestPage.class );

    ui.getPageOperator().openPage( "foo" );
  }

  private UIImpl createUI( Class<? extends AbstractPage> pageType) {
    UIConfiguration configuration = new UIConfiguration();
    Shell shell = new Shell( new Display() );
    shell.setLayout( new ZIndexStackLayout() );
    ActionConfiguration actionConfiguration = new ActionConfiguration( "bar", TestAction.class );
    configuration.addPageConfiguration( new PageConfiguration( "fooRoot", TestPage.class ).setTopLevel( true ) );
    configuration.addPageConfiguration( new PageConfiguration( "foo", pageType )
      .addActionConfiguration( actionConfiguration ) );
    RemoteUI remoteUI = new RemoteUI( shell );
    Controller controller = new Controller( shell, remoteUI, configuration.getAdapter( UIDescriptor.class ) );
    UIImpl ui = new UIImpl( shell.getDisplay(), controller, configuration );
    remoteUI.setController( controller );
    remoteUI.setUi( ui );
    ui.markInitialized();
    controller.createGlobalActions( ui );
    controller.createRootPages( ui );
    return ui;
  }

  public static class ActionEnablingTestPage extends AbstractPage {

    @Override
    public void createContent( Composite parent, PageData data ) {
      setActionEnabled( "bar", false );
    }

  }

  public static class ActionVisibleChangingTestPage extends AbstractPage {

    @Override
    public void createContent( Composite parent, PageData data ) {
      setActionVisible( "bar", false );
    }

  }
}
