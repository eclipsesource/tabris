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
package com.eclipsesource.tabris.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.Serializable;

import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.internal.ZIndexStackLayout;
import com.eclipsesource.tabris.internal.ui.TestPage;
import com.eclipsesource.tabris.test.TabrisTestUtil;


public class TabrisUITest {

  private Shell shell;
  private RemoteObject remoteObject;

  @Before
  public void setUp() {
    Fixture.setUp();
    Display display = new Display();
    shell = new Shell( display );
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    remoteObject = TabrisTestUtil.mockRemoteObject();
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( TabrisUI.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullConfiguration() {
    new TabrisUI( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testCreateFailsWithNullConfiguration() {
    TabrisUI tabrisUI = new TabrisUI( mock( UIConfiguration.class ) );

    tabrisUI.create( null );
  }

  @Test
  public void testCreatesRootPages() {
    TabrisUI tabrisUI = new TabrisUI( createConfiguration() );

    tabrisUI.create( shell );

    assertEquals( 2, shell.getChildren().length );
  }

  @Test
  public void testZIndexLayout() {
    TabrisUI tabrisUI = new TabrisUI( createConfiguration() );
    shell.open();

    tabrisUI.create( shell );

    assertTrue( shell.getLayout() instanceof ZIndexStackLayout );
  }

  @Test
  public void testSetsBackgroundOnRemoteUI() {
    TabrisUI tabrisUI = new TabrisUI( createConfiguration() );
    shell.open();

    tabrisUI.create( shell );

    verify( remoteObject ).set( "background", new int[] { 100, 100, 100 } );
  }

  @Test
  public void testSetsForegroundOnRemoteUI() {
    TabrisUI tabrisUI = new TabrisUI( createConfiguration() );
    shell.open();

    tabrisUI.create( shell );

    verify( remoteObject ).set( "foreground", new int[] { 200, 200, 200 } );
  }

  private UIConfiguration createConfiguration() {
    UIConfiguration configuration = new UIConfiguration();
    configuration.addPageConfiguration( new PageConfiguration( "foo1", TestPage.class ).setTopLevel( true ) );
    configuration.addPageConfiguration( new PageConfiguration( "foo2", TestPage.class ).setTopLevel( true ) );
    configuration.setBackground( new RGB( 100, 100, 100 ) );
    configuration.setForeground( new RGB( 200, 200, 200 ) );
    return configuration;
  }

}
