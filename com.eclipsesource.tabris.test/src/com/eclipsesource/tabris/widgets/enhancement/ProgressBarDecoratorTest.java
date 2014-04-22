/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets.enhancement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ProgressBarDecoratorTest {

  private ProgressBar progressBar;

  @Before
  public void setUp() {
    Fixture.setUp();
    Shell shell = new Shell( new Display() );
    progressBar = new ProgressBar( shell, SWT.INDETERMINATE );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testSetsSpinningIndicator() {
    ProgressBarDecorator decorator = new ProgressBarDecorator( progressBar );

    decorator.useSpinningIndicator();

    assertEquals( progressBar.getData( "spinningIndicator" ), Boolean.TRUE );
  }

  @Test
  public void testSetSpinningIndicatorReturnsDecorator() {
    ProgressBarDecorator decorator = new ProgressBarDecorator( progressBar );

    ProgressBarDecorator actualDecorator = decorator.useSpinningIndicator();

    assertSame( decorator, actualDecorator );
  }
}
