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
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.TRANSACTION_AFFILIATION;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.TRANSACTION_ID;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.TRANSACTION_REVENUE;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.TRANSACTION_SHIPPING;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.TRANSACTION_TAX;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValueProvider.getRequestValue;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValues.HIT_TRANSACTION;


@SuppressWarnings("restriction")
public class TransactionHit extends Hit {

  public TransactionHit( String transactionId ) {
    super( getRequestValue( HIT_TRANSACTION ) );
    whenNull( transactionId ).throwIllegalArgument( "TransactionId must not be null" );
    when( transactionId.isEmpty() ).throwIllegalArgument( "TransactionId must not be empty" );
    addParameter( getRequestKey( TRANSACTION_ID ), transactionId );
  }

  public TransactionHit setAffiliation( String affiliation ) {
    whenNull( affiliation ).throwIllegalArgument( "Affiliation must not be null." );
    when( affiliation.isEmpty() ).throwIllegalArgument( "Affiliation must not be empty." );
    addParameter( getRequestKey( TRANSACTION_AFFILIATION ), affiliation );
    return this;
  }

  public TransactionHit setRevenue( double revenue ) {
    addParameter( getRequestKey( TRANSACTION_REVENUE ), Double.valueOf( revenue ) );
    return this;
  }

  public TransactionHit setShipping( double shipping ) {
    addParameter( getRequestKey( TRANSACTION_SHIPPING ), Double.valueOf( shipping ) );
    return this;
  }

  public TransactionHit setTax( double tax ) {
    addParameter( getRequestKey( TRANSACTION_TAX ), Double.valueOf( tax ) );
    return this;
  }
}
