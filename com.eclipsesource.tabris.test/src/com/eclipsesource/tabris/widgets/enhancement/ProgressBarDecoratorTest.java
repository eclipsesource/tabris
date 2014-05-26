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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.test.util.TabrisEnvironment;


public class ProgressBarDecoratorTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private ProgressBar progressBar;

  @Before
  public void setUp() {
    Shell shell = new Shell( new Display() );
    progressBar = new ProgressBar( shell, SWT.INDETERMINATE );
  }

  @Test
  public void testSetsSpinningIndicatorTrue() {
    ProgressBarDecorator decorator = new ProgressBarDecorator( progressBar );

    decorator.useSpinningIndicator( true );

    assertEquals( progressBar.getData( "spinningIndicator" ), Boolean.TRUE );
  }

  @Test
  public void testSetsSpinningIndicatorFalse() {
    ProgressBarDecorator decorator = new ProgressBarDecorator( progressBar );

    decorator.useSpinningIndicator( false );

    assertEquals( progressBar.getData( "spinningIndicator" ), Boolean.FALSE );
  }

  @Test
  public void testSetSpinningIndicatorReturnsDecorator() {
    ProgressBarDecorator decorator = new ProgressBarDecorator( progressBar );

    ProgressBarDecorator actualDecorator = decorator.useSpinningIndicator( true );

    assertSame( decorator, actualDecorator );
  }
}
