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
package com.eclipsesource.tabris.passepartout.internal.condition;

import static com.eclipsesource.tabris.passepartout.test.PassPartoutTestUtil.createEnvironment;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.eclipsesource.tabris.passepartout.Bounds;


public class AlwaysTrueContidtionTest {

  @Test
  public void testisAlwaysTrue() {
    AlwaysTrueContidtion contidtion = new AlwaysTrueContidtion();

    assertTrue( contidtion.compliesWith( null ) );
    assertTrue( contidtion.compliesWith( createEnvironment() ) );
    assertTrue( contidtion.compliesWith( createEnvironment( new Bounds( 100, 100, 100, 100 ) ) ) );
  }
}
