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
package com.eclipsesource.tabris.passepartout.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.eclipsesource.tabris.passepartout.Bounds;


public class UIEnvironmentImplTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullBounds() {
    new UIEnvironmentImpl( null, new Bounds( 0, 0, 0, 0 ), 16 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullReferenceBounds() {
    new UIEnvironmentImpl( new Bounds( 0, 0, 0, 0 ), null, 16 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNegativeFontSize() {
    new UIEnvironmentImpl( new Bounds( 0, 0, 0, 0 ), new Bounds( 0, 0, 0, 0 ), -1 );
  }

  @Test
  public void testHasBounds() {
    Bounds bounds = new Bounds( 0, 0, 0, 0 );
    UIEnvironmentImpl uiEnvironment = new UIEnvironmentImpl( bounds, bounds, 16 );

    Bounds actualBounds = uiEnvironment.getParentBounds();

    assertSame( bounds, actualBounds );
  }

  @Test
  public void testHasReferenceBounds() {
    Bounds bounds = new Bounds( 0, 0, 0, 0 );
    UIEnvironmentImpl uiEnvironment = new UIEnvironmentImpl( bounds, bounds, 16 );

    Bounds actualBounds = uiEnvironment.getReferenceBounds();

    assertSame( bounds, actualBounds );
  }

  @Test
  public void testHasFontSize() {
    UIEnvironmentImpl uiEnvironment = new UIEnvironmentImpl( new Bounds( 0, 0, 0, 0 ), new Bounds( 0, 0, 0, 0 ), 16 );

    int parentFontSize = uiEnvironment.getParentFontSize();

    assertEquals( 16, parentFontSize );
  }
}
