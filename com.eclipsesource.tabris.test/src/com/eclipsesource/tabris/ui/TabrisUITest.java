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

import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.internal.ZIndexStackLayout;
import com.eclipsesource.tabris.internal.ui.TestPage;


public class TabrisUITest {

  private Shell shell;

  @Before
  public void setUp() {
    Fixture.setUp();
    Display display = new Display();
    shell = new Shell( display );
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
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
    UIConfiguration configuration = new ExampleConfig();
    TabrisUI tabrisUI = new TabrisUI( configuration );

    tabrisUI.create( shell );

    assertEquals( 2, shell.getChildren().length );
  }

  @Test
  public void testZIndexLayout() {
    UIConfiguration configuration = new ExampleConfig();
    TabrisUI tabrisUI = new TabrisUI( configuration );
    shell.open();

    tabrisUI.create( shell );

    assertTrue( shell.getLayout() instanceof ZIndexStackLayout );
  }

  private static class ExampleConfig implements UIConfiguration {

    @Override
    public void configure( UI ui, UIContext context ) {
      ui.addPage( PageConfiguration.newPage( "foo1", TestPage.class ).setTopLevel( true ) );
      ui.addPage( PageConfiguration.newPage( "foo2", TestPage.class ).setTopLevel( true ) );
    }

  }
}
