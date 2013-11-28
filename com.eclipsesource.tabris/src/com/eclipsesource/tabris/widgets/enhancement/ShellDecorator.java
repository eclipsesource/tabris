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

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.OVERLAY_COLOR;
import static com.eclipsesource.tabris.internal.WidgetsUtil.setData;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Shell;


/**
 * @since 1.2
 */
public class ShellDecorator extends WidgetDecorator<ShellDecorator> {

  private final Shell shell;

  ShellDecorator( Shell shell ) {
    super( shell );
    this.shell = shell;
  }

  /**
   * <p>
   * Specifies the overlay color of modal shells. When a modal shell is not fullscreen it has a transparent area between
   * the display and the shell bounds. This area can be colored using the
   * {@link ShellDecorator#setOverlayColor(Color, int)} method.
   * </p>
   *
   * @param color the color to use for the gap between shell and display bounds. Must not be <code>null</code>.
   * @param alpha the alpha value of the color.
   */
  public ShellDecorator setOverlayColor( Color color, int alpha ) {
    whenNull( color ).throwIllegalArgument( "Color must not be null" );
    when( alpha < 0 || alpha > 255 ).throwIllegalArgument( "Alpha must be >= 0 and <= 255 but was " + alpha );
    RGB rgb = color.getRGB();
    JsonArray overlay = new JsonArray().add( rgb.red ).add( rgb.green ).add( rgb.blue ).add( alpha );
    setData( shell, OVERLAY_COLOR, overlay );
    return this;
  }
}
