/*******************************************************************************
 * Copyright (c) 2013, 2019 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.internal.Clauses.whenNot;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.EVENT_SWIPE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ITEM;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;

import com.eclipsesource.tabris.widgets.swipe.Swipe;


public class SwipeOperationHandler extends AbstractOperationHandler {

  private final Swipe swipe;
  private int activeClientItem;

  public SwipeOperationHandler( Swipe swipe ) {
    whenNull( swipe ).throwIllegalArgument( "Swipe must not be null" );
    this.swipe = swipe;
    this.activeClientItem = -1;
  }

  @Override
  public void handleNotify( String event, JsonObject properties ) {
    if( EVENT_SWIPE.equals( event ) && !swipe.getControl().isDisposed() ) {
      verifyHasItemProperty( properties );
      activeClientItem = properties.get( PROPERTY_ITEM ).asInt();
      swipe.show( activeClientItem );
    }
  }

  private void verifyHasItemProperty( JsonObject properties ) {
    whenNull( properties ).throwIllegalArgument( "Properties must not be null" );
    whenNull( properties.get( PROPERTY_ITEM ) )
      .throwIllegalArgument( "Properties of " + EVENT_SWIPE + " do not contain an item." );
    whenNot( properties.get( PROPERTY_ITEM ).isNumber() )
      .throwIllegalArgument( "Property item of " + EVENT_SWIPE + " is not an Integer." );
  }

  public int getActiveClientItem() {
    return activeClientItem;
  }

  public void setActiveClientItem( int index ) {
    activeClientItem = index;
  }
}
