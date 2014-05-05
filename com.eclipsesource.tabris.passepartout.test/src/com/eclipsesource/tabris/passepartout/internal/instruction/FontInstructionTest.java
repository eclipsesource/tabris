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

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.test.util.TabrisEnvironment;


public class FontInstructionTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private Display display;

  @Before
  public void setUp() {
    display = new Display();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullFont() {
    new FontInstruction( null );
  }

  @Test
  public void testHasFont() {
    Font font = display.getSystemFont();
    FontInstruction instruction = new FontInstruction( font );

    Font actualFont = instruction.getFont();

    assertSame( font, actualFont );
  }
}
