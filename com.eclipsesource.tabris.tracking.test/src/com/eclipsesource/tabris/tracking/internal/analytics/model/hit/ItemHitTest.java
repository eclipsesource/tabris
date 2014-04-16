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
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.ITEM_CATEGORY;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.ITEM_CODE;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.ITEM_NAME;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.ITEM_PRICE;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.ITEM_QUANTITY;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.TRANSACTION_ID;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValueProvider.getRequestValue;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValues.HIT_ITEM;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

public class ItemHitTest {

  @Test
  public void testSetsAppViewHit() {
    ItemHit itemHit = new ItemHit( "foo", "bar" );

    String hitType = ( String )itemHit.getParameter().get( getRequestKey( HIT_TYPE ) );

    assertEquals( getRequestValue( HIT_ITEM ), hitType );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullItemName() {
    new ItemHit( null, "foo" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyItemName() {
    new ItemHit( "", "foo" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullTransactionId() {
    new ItemHit( "foo", null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyTransactionId() {
    new ItemHit( "foo", null );
  }

  @Test
  public void testSetsItemName() {
    ItemHit itemHit = new ItemHit( "foo", "bar" );

    Map<String, Object> parameter = itemHit.getParameter();

    assertEquals( "foo", parameter.get( getRequestKey( ITEM_NAME ) ) );
  }

  @Test
  public void testSetsTransactionId() {
    ItemHit itemHit = new ItemHit( "foo", "bar" );

    Map<String, Object> parameter = itemHit.getParameter();

    assertEquals( "bar", parameter.get( getRequestKey( TRANSACTION_ID ) ) );
  }

  @Test
  public void testSetsItemPrice() {
    ItemHit itemHit = new ItemHit( "foo", "bar" ).setPrice( 20 );

    Map<String, Object> parameter = itemHit.getParameter();

    assertEquals( Double.valueOf( 20 ), parameter.get( getRequestKey( ITEM_PRICE ) ) );
  }

  @Test
  public void testSetsItemQuantity() {
    ItemHit itemHit = new ItemHit( "foo", "bar" ).setQuantity( 20 );

    Map<String, Object> parameter = itemHit.getParameter();

    assertEquals( Double.valueOf( 20 ), parameter.get( getRequestKey( ITEM_QUANTITY ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullItemCode() {
    new ItemHit( "foo", "bar" ).setCode( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyItemCode() {
    new ItemHit( "foo", "bar" ).setCode( "" );
  }

  @Test
  public void testSetsItemCode() {
    ItemHit itemHit = new ItemHit( "foo", "bar" ).setCode( "baz" );

    Map<String, Object> parameter = itemHit.getParameter();

    assertEquals( "baz", parameter.get( getRequestKey( ITEM_CODE ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullItemCategory() {
    new ItemHit( "foo", "bar" ).setCategory( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyItemCategory() {
    new ItemHit( "foo", "bar" ).setCategory( "" );
  }

  @Test
  public void testSetsItemCategory() {
    ItemHit itemHit = new ItemHit( "foo", "bar" ).setCategory( "baz" );

    Map<String, Object> parameter = itemHit.getParameter();

    assertEquals( "baz", parameter.get( getRequestKey( ITEM_CATEGORY ) ) );
  }
}
