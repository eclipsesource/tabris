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
package com.eclipsesource.tabris.passepartout.internal.instruction;

import static org.junit.Assert.assertSame;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class BackgroundInstructionTest {

  @Before
  public void setUp() {
    Fixture.setUp();
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullColor() {
    new BackgroundInstruction( null );
  }

  @Test
  public void testHasColor() {
    Display display = new Display();
    Color color = display.getSystemColor( SWT.COLOR_BLACK );
    BackgroundInstruction instruction = new BackgroundInstruction( color );

    Color actualColor = instruction.getColor();

    assertSame( color, actualColor );
  }
}
