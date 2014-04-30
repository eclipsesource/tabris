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
package com.eclipsesource.tabris.widgets.enhancement;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry;
import com.eclipsesource.tabris.test.RWTEnvironment;


public class ShellDecoratorTest {

  @Rule
  public RWTEnvironment environment = new RWTEnvironment();

  private Shell shell;
  private ShellDecorator decorator;
  private Display display;

  @Before
  public void setUp() {
    display = new Display();
    shell = mock( Shell.class );
    decorator = Widgets.onShell( shell );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNegativeAlpha() {
    Color color = new Color( display, 233, 244, 255 );

    decorator.setOverlayColor( color, -34 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithToBigAlpha() {
    Color color = new Color( display, 233, 244, 255 );

    decorator.setOverlayColor( color, 256 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullColor() {
    decorator.setOverlayColor( null, 254 );
  }

  @Test
  public void testSetsOverlay() {
    Color color = new Color( display, 233, 244, 255 );
    decorator.setOverlayColor( color, 34 );

    JsonArray expectedColor = new JsonArray().add( 233 ).add( 244 ).add( 255 ).add( 34 );
    verify( shell ).setData( WhiteListEntry.OVERLAY_COLOR.getKey(), expectedColor );
  }

}
