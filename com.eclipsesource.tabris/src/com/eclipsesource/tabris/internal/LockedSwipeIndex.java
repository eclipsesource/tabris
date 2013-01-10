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

class LockedSwipeIndex {

  private final int direction;
  private final int index;

  public LockedSwipeIndex( int direction, int index ) {
    this.direction = direction;
    this.index = index;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + direction;
    result = prime * result + index;
    return result;
  }

  @Override
  public boolean equals( Object obj ) {
    if( this == obj )
      return true;
    if( obj == null )
      return false;
    if( getClass() != obj.getClass() )
      return false;
    LockedSwipeIndex other = ( LockedSwipeIndex )obj;
    if( direction != other.direction )
      return false;
    if( index != other.index )
      return false;
    return true;
  }

}