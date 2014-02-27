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
package com.eclipsesource.tabris.passepartout.internal.unit;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;


public class EmTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullEm() {
    new Em( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithZeroEm() {
    new Em( BigDecimal.ZERO );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNegativeEm() {
    new Em( BigDecimal.valueOf( -1 ) );
  }

  @Test
  public void testHasValue() {
    Em em = new Em( BigDecimal.ONE );

    BigDecimal value = em.getValue();

    assertEquals( BigDecimal.ONE, value );
  }
}
