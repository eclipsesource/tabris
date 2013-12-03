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
package com.eclipsesource.tabris.internal;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;


public class ScrollingCompositeUtil {

  public static boolean isRevealed( Point origin, Rectangle clientArea, Rectangle controlBounds ) {
    boolean verticalVisible = isVerticallyVisible( origin, clientArea, controlBounds );
    boolean horizontalVisible = isHorizontallyVisible( origin, clientArea, controlBounds );
    return horizontalVisible && verticalVisible;
  }

  private static boolean isVerticallyVisible( Point origin, Rectangle clientArea, Rectangle controlBounds ) {
    boolean isTopIn = origin.y <= controlBounds.y;
    boolean isBottomIn = origin.y + clientArea.height >= controlBounds.y + controlBounds.height;
    boolean isFullyTopIn = origin.y <= controlBounds.y - controlBounds.height;
    boolean isFullyBottomIn = origin.y + clientArea.height >= controlBounds.y + controlBounds.height;
    return ( isTopIn && isBottomIn ) || ( isFullyTopIn && isFullyBottomIn );
  }

  private static boolean isHorizontallyVisible( Point origin, Rectangle clientArea, Rectangle controlBounds ) {
    boolean isLeftIn = origin.x <= controlBounds.x;
    boolean isRightIn = origin.x + clientArea.width >= controlBounds.x + controlBounds.width;
    boolean isFullyLeftIn = origin.x <= controlBounds.x - controlBounds.width;
    boolean isFullyRightIn = origin.x + clientArea.width >= controlBounds.x + controlBounds.width;
    return ( isLeftIn && isRightIn ) || ( isFullyLeftIn && isFullyRightIn );
  }

  private ScrollingCompositeUtil() {
    // prevent instantiation
  }
}
