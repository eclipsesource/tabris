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
package com.eclipsesource.tabris.widgets;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * @since 1.3
 */
public class PagingIndicator extends Composite {

  private Canvas indicatorCanvas;
  private Color inactiveColor;
  private Color activeColor;
  private int count;
  private int activeIndex;
  private int spacing;
  private int diameter;

  public PagingIndicator( Composite parent ) {
    super( parent, SWT.NONE );
    initializeIndicator( parent );
    createCanvas();
  }

  private void initializeIndicator( Composite parent ) {
    setInternalLayout();
    count = 1;
    activeIndex = 0;
    spacing = 9;
    diameter = 7;
    inactiveColor = parent.getDisplay().getSystemColor( SWT.COLOR_GRAY );
    activeColor = new Color( parent.getDisplay(), 0, 122, 255 );
  }

  private void setInternalLayout() {
    GridLayout gridLayout = new GridLayout();
    gridLayout.marginHeight = 0;
    gridLayout.marginWidth = 0;
    gridLayout.verticalSpacing = 0;
    gridLayout.horizontalSpacing = 0;
    setLayout( gridLayout );
  }

  private void createCanvas() {
    indicatorCanvas = new Canvas( this, SWT.NONE );
    indicatorCanvas.setLayoutData( new GridData( SWT.CENTER, SWT.CENTER, true, true ) );
  }

  public void setCount( int count ) {
    when( count < 0 ).throwIllegalArgument( "Count must be positive but was " + count );
    this.count = count;
    update();
  }

  public int getCount() {
    return count;
  }

  public void setActive( int index ) {
    when( index < 0 ).throwIllegalArgument( "Index must be positive but was " + index );
    when( index > ( count - 1 ) ).throwIllegalState( "Index " + index + " does not exist. Count is " + count + "." );
    this.activeIndex = index;
    update();
  }

  public int getActive() {
    return activeIndex;
  }

  public void setActiveColor( Color activeColor ) {
    whenNull( activeColor ).throwIllegalArgument( "Active color must not be null" );
    this.activeColor = activeColor;
    update();
  }

  public Color getActiveColor() {
    return activeColor;
  }

  public void setInactiveColor( Color inactiveColor ) {
    whenNull( inactiveColor ).throwIllegalArgument( "Inactive color must not be null" );
    this.inactiveColor = inactiveColor;
    update();
  }

  public Color getInactiveColor() {
    return inactiveColor;
  }

  public void setSpacing( int spacing ) {
    when( spacing < 0  ).throwIllegalArgument( "Spacing must be positive but was " + spacing );
    this.spacing = spacing;
    update();
  }

  public int getSpacing() {
    return spacing;
  }

  public void setDiameter( int diameter ) {
    when( diameter < 0  ).throwIllegalArgument( "Diameter must be positive but was " + diameter );
    this.diameter = diameter;
    update();
  }

  public int getDiameter() {
    return diameter;
  }

  @Override
  public void update() {
    super.update();
    updateSize();
    drawIndicators();
    getParent().layout( true, true );
  }

  private void updateSize() {
    int width = ( getCount() * diameter ) + ( Math.max( 0, getCount() - 1 ) * spacing );
    indicatorCanvas.setSize( width, diameter );
    GridData gd = ( GridData )indicatorCanvas.getLayoutData();
    gd.heightHint = diameter;
    gd.widthHint = width;
  }

  private void drawIndicators() {
    GC gc = new GC( indicatorCanvas );
    for( int i = 0; i < getCount(); i++ ) {
      if( i == getActive() ) {
        gc.setBackground( getActiveColor() );
      } else {
        gc.setBackground( getInactiveColor() );
      }
      int x = ( getDiameter() + getSpacing() ) * i;
      gc.fillOval( x, 0, getDiameter(), getDiameter() );
    }
    gc.dispose();
  }

  Canvas getCanvas() {
    return indicatorCanvas;
  }

}