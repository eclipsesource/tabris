/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

public class SwipeLayout extends Layout {

  private Control onTopControl;

  public void setOnTopControl( Control control ) {
    onTopControl = control;
  }

  public Control getOnTopControl() {
    return onTopControl;
  }

  @Override
  protected Point computeSize( Composite composite, int wHint, int hHint, boolean flushCache ) {
    Control children[] = composite.getChildren();
    int maxWidth = 0;
    int maxHeight = 0;
    for( int i = 0; i < children.length; i++ ) {
      Point size = children[ i ].computeSize( wHint, hHint, flushCache );
      maxWidth = Math.max( size.x, maxWidth );
      maxHeight = Math.max( size.y, maxHeight );
    }
    int width = maxWidth;
    int height = maxHeight;
    if( wHint != SWT.DEFAULT ) {
      width = wHint;
    }
    if( hHint != SWT.DEFAULT ) {
      height = hHint;
    }
    return new Point( width, height );
  }

  @Override
  protected boolean flushCache( Control control ) {
    return true;
  }

  @Override
  protected void layout( Composite composite, boolean flushCache ) {
    Control children[] = composite.getChildren();
    Rectangle rect = composite.getClientArea();
    for( int i = 0; i < children.length; i++ ) {
      children[ i ].setBounds( rect );
      if( children[ i ] == onTopControl ) {
        onTopControl.moveAbove( null );
      }
    }
  }
}
