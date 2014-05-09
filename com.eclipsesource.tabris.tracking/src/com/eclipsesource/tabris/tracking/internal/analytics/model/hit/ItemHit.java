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
package com.eclipsesource.tabris.tracking.internal.analytics.model.hit;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.ITEM_CATEGORY;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.ITEM_CODE;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.ITEM_NAME;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.ITEM_PRICE;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.ITEM_QUANTITY;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.TRANSACTION_ID;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValueProvider.getRequestValue;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValues.HIT_ITEM;


@SuppressWarnings("restriction")
public class ItemHit extends Hit {

  public ItemHit( String name, String transactionId ) {
    super( getRequestValue( HIT_ITEM ) );
    validateArguments( name, transactionId );
    addParameter( getRequestKey( ITEM_NAME ), name );
    addParameter( getRequestKey( TRANSACTION_ID ), transactionId );
  }

  private void validateArguments( String name, String transactionId ) {
    whenNull( name ).throwIllegalArgument( "Name must not be null." );
    when( name.isEmpty() ).throwIllegalArgument( "Name must not be empty." );
    whenNull( transactionId ).throwIllegalArgument( "TransactionId must not be null." );
    when( transactionId.isEmpty() ).throwIllegalArgument( "TransactionId must not be empty." );
  }

  public ItemHit setPrice( double price ) {
    addParameter( getRequestKey( ITEM_PRICE ), Double.valueOf( price ) );
    return this;
  }

  public ItemHit setQuantity( double quantity ) {
    addParameter( getRequestKey( ITEM_QUANTITY ), Double.valueOf( quantity ) );
    return this;
  }

  public ItemHit setCode( String code ) {
    whenNull( code ).throwIllegalArgument( "Code must not be null." );
    when( code.isEmpty() ).throwIllegalArgument( "Code must not be empty." );
    addParameter( getRequestKey( ITEM_CODE ), code );
    return this;
  }

  public ItemHit setCategory( String category ) {
    whenNull( category ).throwIllegalArgument( "Category must not be null." );
    when( category.isEmpty() ).throwIllegalArgument( "Category must not be empty." );
    addParameter( getRequestKey( ITEM_CATEGORY ), category );
    return this;
  }

}
