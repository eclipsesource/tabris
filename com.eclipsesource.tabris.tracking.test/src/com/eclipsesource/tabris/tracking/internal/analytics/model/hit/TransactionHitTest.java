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

import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.HIT_TYPE;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.TRANSACTION_AFFILIATION;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.TRANSACTION_ID;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.TRANSACTION_REVENUE;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.TRANSACTION_SHIPPING;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.TRANSACTION_TAX;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValueProvider.getRequestValue;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValues.HIT_TRANSACTION;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

public class TransactionHitTest {

  @Test
  public void testSetsAppViewHit() {
    TransactionHit transactionHit = new TransactionHit( "foo" );

    String hitType = ( String )transactionHit.getParameter().get( getRequestKey( HIT_TYPE ) );

    assertEquals( getRequestValue( HIT_TRANSACTION ), hitType );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullTransactionId() {
    new TransactionHit( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyTransactionId() {
    new TransactionHit( "" );
  }

  @Test
  public void testSetsTransactionId() {
    TransactionHit transactionHit = new TransactionHit( "foo" );

    String transactionId = ( String )transactionHit.getParameter().get( getRequestKey( TRANSACTION_ID ) );

    assertEquals( "foo", transactionId );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullTransactionAffiliation() {
    new TransactionHit( "foo" ).setAffiliation( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyTransactionAffiliation() {
    new TransactionHit( "foo" ).setAffiliation( "" );
  }

  @Test
  public void testSetsTransactionAffiliation() {
    TransactionHit transactionHit = new TransactionHit( "foo" ).setAffiliation( "bar" );

    String transactionAffiliation = ( String )transactionHit.getParameter()
                                                            .get( getRequestKey( TRANSACTION_AFFILIATION ) );

    assertEquals( "bar", transactionAffiliation );
  }

  @Test
  public void testSetsTransactionRevenue() {
    TransactionHit transactionHit = new TransactionHit( "foo" ).setRevenue( 20 );

    Map<String, Object> parameter = transactionHit.getParameter();

    assertEquals( Double.valueOf( 20 ), parameter.get( getRequestKey( TRANSACTION_REVENUE ) ) );
  }

  @Test
  public void testSetsTransactionShipping() {
    TransactionHit transactionHit = new TransactionHit( "foo" ).setShipping( 20 );

    Map<String, Object> parameter = transactionHit.getParameter();

    assertEquals( Double.valueOf( 20 ), parameter.get( getRequestKey( TRANSACTION_SHIPPING ) ) );
  }

  @Test
  public void testSetsTransactionTax() {
    TransactionHit transactionHit = new TransactionHit( "foo" ).setTax( 20 );

    Map<String, Object> parameter = transactionHit.getParameter();

    assertEquals( Double.valueOf( 20 ), parameter.get( getRequestKey( TRANSACTION_TAX ) ) );
  }
}
