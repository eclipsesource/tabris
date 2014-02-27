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

import static com.eclipsesource.tabris.passepartout.internal.Clauses.whenNull;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import com.eclipsesource.tabris.passepartout.Bounds;


public class BoundsUtil {

  public static Bounds getBounds( Rectangle rectangle ) {
    whenNull( rectangle ).throwIllegalArgument( "Rectangle must not be null" );
    return new Bounds( rectangle.x, rectangle.y, rectangle.width, rectangle.height );
  }

  public static Bounds getBounds( Point point ) {
    whenNull( point ).throwIllegalArgument( "Point must not be null" );
    return new Bounds( 0, 0, point.x, point.y );
  }

  public static Rectangle getRectangle( Bounds bounds ) {
    whenNull( bounds ).throwIllegalArgument( "Bounds must not be null" );
    return new Rectangle( bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight() );
  }

  private BoundsUtil() {
    // prevent instantiation
  }
}
