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
  private int previousIndex;
  private int nextIndex;
  private final List<Integer> stashes;
  
  public SwipeItemIndexer() {
    stashes = new ArrayList<Integer>();
    previousIndex = -1;
    currentIndex = -1;
    nextIndex = -1;
  }

  public void setCurrent( int index ) {
    verifyNewIndex( index );
    if( isInRange( index ) ) {
      setCurrentInRange( index );
    } else {
      setCurrentOutOfRange( index );
    }
  }

  private void setCurrentInRange( int index ) {
    previousIndex = computePreviousIndex( index );
    nextIndex = computeNextIndex( index );
    currentIndex = index;
    stashBorderIndex();
  }

  private void setCurrentOutOfRange( int index ) {
    stashOldValues();
    previousIndex = index - 1;
    nextIndex = index + 1;
    currentIndex = index;
  }
  
  private void stashBorderIndex() {
    stashes.clear();
    if( nextIndex > previousIndex ) {
      if( previousIndex > 0 ) {
        stashes.add( Integer.valueOf( previousIndex - 1 ) );
      }
    } else {
      stashes.add( Integer.valueOf( previousIndex + 1 ) );
    }
  }

  private void verifyNewIndex( int newIndex ) {
    if( newIndex < 0 ) {
      throw new IllegalArgumentException( "Index must be positive" );
    }
  }

  private void stashOldValues() {
    stashes.clear();
    stashes.add( Integer.valueOf( previousIndex ) );
    stashes.add( Integer.valueOf( currentIndex ) );
    stashes.add( Integer.valueOf( nextIndex ) );
  }

  private boolean isInRange( int newIndex ) {
    return newIndex <= ( currentIndex + 1 ) && newIndex >= ( currentIndex - 1 ) || isDefaultState();
  }

  private boolean isDefaultState() {
    return previousIndex == -1 && currentIndex == -1 && nextIndex == -1;
  }

  private int computePreviousIndex( int newIndex ) {
    int result = newIndex - 1;
    if( currentIndex > newIndex ) {
      result = newIndex + 1;
    } 
    return result;
  }
  
  private int computeNextIndex( int newIndex ) {
    int result = newIndex + 1;
    if( currentIndex > newIndex ) {
      result = newIndex - 1;
    } 
    return result;
  }
  
  public int getCurrent() {
   return currentIndex; 
  }
  
  public int getPrevious() {
    return previousIndex; 
  }
  
  public int getNext() {
    return nextIndex; 
  }
  
  public int[] popOutOfRangeIndexes() {
    int[] result = new int[ stashes.size() ];
    for( int i = 0; i < result.length; i++ ) {
      result[ i ] = stashes.get( i ).intValue();
    }
    stashes.clear();
    return result;
  }
  
}
