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

import static com.eclipsesource.tabris.internal.Constants.EVENT_SWIPE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ITEM;
import static com.eclipsesource.tabris.internal.Preconditions.checkArgumentNotNull;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;

import com.eclipsesource.tabris.widgets.swipe.Swipe;


public class SwipeOperationHandler extends AbstractOperationHandler {

  private final Swipe swipe;

  public SwipeOperationHandler( Swipe swipe ) {
    checkArgumentNotNull( swipe, "Swipe" );
    this.swipe = swipe;
  }

  @Override
  public void handleNotify( String event, JsonObject properties ) {
    if( EVENT_SWIPE.equals( event ) ) {
      verifyHasItemProperty( properties );
      int itemIndex = properties.get( PROPERTY_ITEM ).asInt();
      swipe.show( itemIndex );
    }
  }

  private void verifyHasItemProperty( JsonObject properties ) {
    checkArgumentNotNull( properties, "Properties" );
    if( properties.get( PROPERTY_ITEM ) == null ) {
      throw new IllegalArgumentException( "Properties of " + EVENT_SWIPE + " do not contian an item." );
    }
    if( !properties.get( PROPERTY_ITEM ).isNumber() ) {
      throw new IllegalArgumentException( "Property item of " + EVENT_SWIPE + " is not an Integer." );
    }
  }
}
