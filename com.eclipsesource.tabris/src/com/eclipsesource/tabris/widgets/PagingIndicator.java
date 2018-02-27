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
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;

import com.eclipsesource.tabris.ui.TabrisUI;
import com.eclipsesource.tabris.widgets.swipe.Swipe;

/**
 * <p>
 * When you build your app with a paging concept using {@link Swipe}, {@link TabrisUI} or any homebrew solution it’s
 * sometimes required that you need to indicate the paging progress. This is often done using colored bullets. With the
 * {@link PagingIndicator} you can create such bullets. It allows you to define the number of bullets, the active one
 * and the look and feel.
 * </p>
 * <p>
 * The {@link PagingIndicator} is a stand alone {@link Widget}. It needs to be part of your layout.
 * </p>
 *
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

  /**
   * <p>
   * Creates a new {@link PagingIndicator} with the provided parent. Default values are:
   * </p>
   * <ul>
   *   <li>bullet count = 1</li>
   *   <li>active bullet index = 0</li>
   *   <li>spacing between bullets = 9px</li>
   *   <li>bullet diameter = 7px</li>
   *   <li>active color = blue</li>
   *   <li>inactive color = gray</li>
   * </ul>
   *
   * @param parent the parent to use. Must not be <code>null</code>.
   */
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
    addPaintListener();
  }

  private void addPaintListener() {
    indicatorCanvas.addPaintListener( new PaintListener() {

      @Override
      public void paintControl( PaintEvent event ) {
        drawIndicators( event.gc );
        getParent().layout( true, true );
      }
    } );
  }

  /**
   * <p>
   * Sets the number of the bullets to show.
   * </p>
   *
   * @param count the number of bullets. Must be &gt;= 0.
   */
  public void setCount( int count ) {
    when( count < 0 ).throwIllegalArgument( "Count must be positive but was " + count );
    this.count = count;
    update();
  }

  /**
   * <p>
   * Returns the number of bullets to show.
   * </p>
   */
  public int getCount() {
    return count;
  }

  /**
   * <p>
   * Marks the bullet with the defined index as active.
   * </p>
   *
   * @param index the index of the bullet to become active. Must be &gt;= 0.
   *
   * @throws IllegalStateException when the defined index does not exist.
   */
  public void setActive( int index ) {
    when( index < 0 ).throwIllegalArgument( "Index must be positive but was " + index );
    when( index > ( count - 1 ) ).throwIllegalState( "Index " + index + " does not exist. Count is " + count + "." );
    this.activeIndex = index;
    update();
  }

  /**
   * <p>
   * Returns the index of the active bullet.
   * </p>
   */
  public int getActive() {
    return activeIndex;
  }

  /**
   * <p>
   * Sets the color of the active bullet.
   * </p>
   *
   * @param activeColor the color to use for active bullets. Must not be <code>null</code>.
   */
  public void setActiveColor( Color activeColor ) {
    whenNull( activeColor ).throwIllegalArgument( "Active color must not be null" );
    this.activeColor = activeColor;
    update();
  }

  /**
   * <p>
   * Returns the active color.
   * </p>
   */
  public Color getActiveColor() {
    return activeColor;
  }

  /**
   * <p>
   * Sets the color of the inactive bullets.
   * </p>
   *
   * @param inactiveColor the color to use for inactive bullets. Must not be <code>null</code>.
   */
  public void setInactiveColor( Color inactiveColor ) {
    whenNull( inactiveColor ).throwIllegalArgument( "Inactive color must not be null" );
    this.inactiveColor = inactiveColor;
    update();
  }

  /**
   * <p>
   * Returns the inactive color.
   * </p>
   */
  public Color getInactiveColor() {
    return inactiveColor;
  }

  /**
   * <p>
   * Sets the spacing used between bullets.
   * </p>
   *
   * @param spacing the spacing to use in pixel. Must be &gt;= 0.
   */
  public void setSpacing( int spacing ) {
    when( spacing < 0  ).throwIllegalArgument( "Spacing must be positive but was " + spacing );
    this.spacing = spacing;
    update();
  }

  /**
   * <p>
   * Returns the spacing used between bullets.
   * </p>
   */
  public int getSpacing() {
    return spacing;
  }

  /**
   * <p>
   * Sets the diameter of the bullets.
   * </p>
   *
   * @param diameter the diameter to use for the bullets. Must be &gt;= 0.
   */
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
    drawIndicators( new GC( indicatorCanvas ) );
  }

  private void drawIndicators( GC gc ) {
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