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

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.test.util.TabrisEnvironment;


public class ImageInstructionTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  @Before
  public void setUp() {
    new Display();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullImage() {
    new ImageInstruction( null );
  }

  @Test
  public void testHasImage() {
    Image image = environment.getTestImage();
    ImageInstruction instruction = new ImageInstruction( image );

    Image actualimage = instruction.getImage();

    assertSame( image, actualimage );
  }
}
