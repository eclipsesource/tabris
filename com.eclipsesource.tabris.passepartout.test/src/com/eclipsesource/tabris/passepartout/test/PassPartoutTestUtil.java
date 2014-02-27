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
package com.eclipsesource.tabris.passepartout.test;

import static org.mockito.Mockito.mock;

import com.eclipsesource.tabris.passepartout.Bounds;
import com.eclipsesource.tabris.passepartout.FluidGridConfiguration;
import com.eclipsesource.tabris.passepartout.LayoutMode;
import com.eclipsesource.tabris.passepartout.UIEnvironment;
import com.eclipsesource.tabris.passepartout.internal.UIEnvironmentImpl;


public class PassPartoutTestUtil {

  public static UIEnvironment createEnvironment() {
    return createEnvironment( new Bounds( 0, 0, 0, 0 ) );
  }

  public static UIEnvironment createEnvironment( Bounds bounds ) {
    return new UIEnvironmentImpl( bounds, mock( Bounds.class ), 16 );
  }

  public static FluidGridConfiguration createConfig() {
    return new FluidGridConfiguration( LayoutMode.AUTO, 720, 1872 );
  }

  private PassPartoutTestUtil() {
    // prevent instantiation
  }
}
