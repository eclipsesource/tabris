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

import java.util.ArrayList;
import java.util.List;


public class SwipeItemIndexer {

  private int currentIndex;
  private int range;
  private int oldIndex;
  private boolean dirty;

  public SwipeItemIndexer() {
    range = 1;
    reset();
  }

  public void reset() {
    currentIndex = -1;
    oldIndex = -1;
  }

  public void setRange( int range ) {
    if( range < 0 ) {
      throw new IllegalArgumentException( "Range must be 0 or positive." );
    }
    this.range = range;
  }

  public int getRange() {
    return range;
  }

  public void setCurrent( int index ) {
    dirty = true;
    verifyNewIndex( index );
    oldIndex = currentIndex;
    currentIndex = index;
  }

  private void verifyNewIndex( int newIndex ) {
    if( newIndex < 0 ) {
      throw new IllegalArgumentException( "Index must be positive" );
    }
  }

  public int getOld() {
    return oldIndex;
  }

  public int getCurrent() {
   return currentIndex;
  }

  public int[] getPrevious() {
    int[] result = getEmptyRange();
    if( range > 0 ) {
      int[] newRange = getRange( currentIndex );
      result = computePreviousIndexes( newRange );
    }
    return result;
  }

  private int[] computePreviousIndexes( int[] newRange ) {
    int[] result = new int[ range ];
    if( currentIndex > 0 || oldIndex > 0) {
      if( currentIndex >= oldIndex || isAJump() ) {
        result = computeFollowUps( newRange );
      } else {
        result = computeInvertedFollowUps( newRange );
      }
    } else {
      result = getEmptyRange();
    }
    return result;
  }

  public int[] getNext() {
    int[] result = getEmptyRange();
    if( range > 0 ) {
      int[] newRange = getRange( currentIndex );
      result = computeNextIndexes( newRange );
    }
    return result;
  }

  private int[] getRange( int index ) {
    List<Integer> rangeItems = new ArrayList<Integer>();
    for( int i = ( index - range ); i <= ( index + range ); i++ ) {
      if( i >= 0 ) {
        rangeItems.add( Integer.valueOf( i ) );
      }
    }
    return getAsArray( rangeItems );
  }

  private int[] computeNextIndexes( int[] newRange ) {
    int[] result = new int[ range + 1 ];
    if( currentIndex > oldIndex || isAJump() ) {
      result = computeInvertedFollowUps( newRange );
    } else {
      if( currentIndex > 0 ) {
        result = computeFollowUps( newRange );
      } else {
        result = getEmptyRange();
      }
    }
    return result;
  }

  private boolean isAJump() {
    return ( currentIndex - oldIndex ) > 1 || ( oldIndex - currentIndex ) > 1;
  }

  private int[] computeInvertedFollowUps( int[] indexes ) {
    List<Integer> result = new ArrayList<Integer>();
    for( int i = 0; i < indexes.length; i++ ) {
      if( indexes[ i ] > currentIndex ) {
        result.add( Integer.valueOf( indexes[ i ] ) );
      }
    }
    return getAsArray( result );
  }

  private int[] computeFollowUps( int[] indexes ) {
    List<Integer> result = new ArrayList<Integer>();
    for( int i = 0; i < indexes.length; i++ ) {
      if( indexes[ i ] < currentIndex ) {
        result.add( Integer.valueOf( indexes[ i ] ) );
      }
    }
    return getAsArray( result );
  }

  public int[] popOutOfRangeIndexes() {
    int[] result = getEmptyRange();
    if( dirty && oldIndex != -1 ) {
      result = computeOutOfRangeIndexes();
      dirty = false;
    }
    return result;
  }

  private int[] getEmptyRange() {
    return new int[] {};
  }

  private int[] computeOutOfRangeIndexes() {
    int[] newRange = getRange( currentIndex );
    int[] oldRange = getRange( oldIndex );
    return computeDelta( oldRange, newRange );
  }

  private int[] computeDelta( int[] oldRange, int[] newRange ) {
    List<Integer> delta = new ArrayList<Integer>();
    List<Integer> oldIndexes = getAsList( oldRange );
    List<Integer> newIndexes = getAsList( newRange );
    for( Integer index : oldIndexes ) {
      if( !newIndexes.contains( index ) ) {
        delta.add( index );
      }
    }
    return getAsArray( delta );
  }

  public static List<Integer> getAsList( int[] indexes ) {
    ArrayList<Integer> result = new ArrayList<Integer>();
    for( int index : indexes ) {
      result.add( Integer.valueOf( index ) );
    }
    return result;
  }

  public static int[] getAsArray( List<Integer> indexes ) {
    int result[] = new int[ indexes.size() ];
    for( int i = 0; i < indexes.size(); i++ ) {
      Integer index = indexes.get( i );
      result[ i ] = index.intValue();
    }
    return result;
  }

}
