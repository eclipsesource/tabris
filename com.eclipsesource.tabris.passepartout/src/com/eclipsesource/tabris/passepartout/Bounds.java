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
package com.eclipsesource.tabris.passepartout;


/**
 * <p>
 * {@link Bounds} are basically an abstraction for an area in a UI. Such an area always has an origin point and
 * width + height.
 * </p>
 *
 * @since 0.9
 */
public class Bounds {

  private final int x;
  private final int y;
  private final int width;
  private final int height;

  /**
   * <p>
   * Creates a new {@link Bounds} object with the defined values.
   * </p>
   *
   *  @param x the x coordinate of the origin point.
   *  @param y the y coordinate of the origin point.
   *  @param width the width of the defined area.
   *  @param height the height of the defined area.
   */
  public Bounds( int x, int y, int width, int height ) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  /**
   * <p>
   * Returns the x coordinate of the origin point.
   * </p>
   */
  public int getX() {
    return x;
  }

  /**
   * <p>
   * Returns the y coordinate of the origin point.
   * </p>
   */
  public int getY() {
    return y;
  }

  /**
   * <p>
   * Returns the width of the defined area.
   * </p>
   */
  public int getWidth() {
    return width;
  }

  /**
   * <p>
   * Returns the height of the defined area.
   * </p>
   */
  public int getHeight() {
    return height;
  }

  @Override
  public String toString() {
    return "Bounds {" + x + ", " + y + ", " + width + ", " + height + "}";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + height;
    result = prime * result + width;
    result = prime * result + x;
    result = prime * result + y;
    return result;
  }

  @Override
  public boolean equals( Object obj ) {
    if( this == obj ) {
      return true;
    }
    if( obj == null ) {
      return false;
    }
    if( getClass() != obj.getClass() ) {
      return false;
    }
    Bounds other = ( Bounds )obj;
    if( height != other.height ) {
      return false;
    }
    if( width != other.width ) {
      return false;
    }
    if( x != other.x ) {
      return false;
    }
    if( y != other.y ) {
      return false;
    }
    return true;
  }

}
