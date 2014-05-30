/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.model.action;

import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ACTION_GOAL_ID;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ECOMMERCE_ORDER_DISCOUNT;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ECOMMERCE_ORDER_ID;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ECOMMERCE_ORDER_ITEMS;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ECOMMERCE_ORDER_SHIPPING;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ECOMMERCE_ORDER_SUBTOTAL;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ECOMMERCE_ORDER_TAX;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ECOMMERCE_ORDER_TOTAL;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;


public class EcommerceActionTest {

  @Test
  public void testAddsIdGoal0ToParameters() {
    EcommerceAction ecommerceAction = new EcommerceAction( "foo", "bar", new BigDecimal( 1 ) );

    assertEquals( Integer.valueOf( 0 ), ecommerceAction.getParameter().get( getRequestKey( ACTION_GOAL_ID ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullId() {
    new EcommerceAction( "foo", null, new BigDecimal( 1 ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyId() {
    new EcommerceAction( "foo", "", new BigDecimal( 1 ) );
  }

  @Test
  public void testStoresOrderId() {
    EcommerceAction ecommerceAction = new EcommerceAction( "foo", "bar", new BigDecimal( 1 ) );

    assertEquals( "bar", ecommerceAction.getParameter().get( getRequestKey( ECOMMERCE_ORDER_ID ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullTotal() {
    new EcommerceAction( "foo", "bar", null );
  }

  @Test
  public void testStoresRevenue() {
    EcommerceAction ecommerceAction = new EcommerceAction( "foo", "bar", new BigDecimal( 5 ) );

    assertEquals( BigDecimal.valueOf( 5 ),
                  ecommerceAction.getParameter().get( getRequestKey( ECOMMERCE_ORDER_TOTAL ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullSubtotal() {
    new EcommerceAction( "foo", "bar", new BigDecimal( 1 ) ).setSubtotal( null );
  }

  @Test
  public void testStoresSubtotal() {
    EcommerceAction ecommerceAction = new EcommerceAction( "foo", "bar", new BigDecimal( 1 ) );

    ecommerceAction.setSubtotal( BigDecimal.valueOf( 5 ) );

    assertEquals( BigDecimal.valueOf( 5 ),
                  ecommerceAction.getParameter().get( getRequestKey( ECOMMERCE_ORDER_SUBTOTAL ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullTax() {
    new EcommerceAction( "foo", "bar", new BigDecimal( 1 ) ).setTax( null );
  }

  @Test
  public void testStoresTax() {
    EcommerceAction ecommerceAction = new EcommerceAction( "foo", "bar", new BigDecimal( 1 ) );

    ecommerceAction.setTax( BigDecimal.valueOf( 5 ) );

    assertEquals( BigDecimal.valueOf( 5 ), ecommerceAction.getParameter().get( getRequestKey( ECOMMERCE_ORDER_TAX ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullShipping() {
    new EcommerceAction( "foo", "bar", new BigDecimal( 1 ) ).setShipping( null );
  }

  @Test
  public void testStoresShipping() {
    EcommerceAction ecommerceAction = new EcommerceAction( "foo", "bar", new BigDecimal( 1 ) );

    ecommerceAction.setShipping( BigDecimal.valueOf( 5 ) );

    assertEquals( BigDecimal.valueOf( 5 ),
                  ecommerceAction.getParameter().get( getRequestKey( ECOMMERCE_ORDER_SHIPPING ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullDiscount() {
    new EcommerceAction( "foo", "bar", new BigDecimal( 1 ) ).setDiscount( null );
  }

  @Test
  public void testStoresDiscount() {
    EcommerceAction ecommerceAction = new EcommerceAction( "foo", "bar", new BigDecimal( 1 ) );

    ecommerceAction.setDiscount( BigDecimal.valueOf( 5 ) );

    assertEquals( BigDecimal.valueOf( 5 ),
                  ecommerceAction.getParameter().get( getRequestKey( ECOMMERCE_ORDER_DISCOUNT ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullItemsJson() {
    new EcommerceAction( "foo", "bar", new BigDecimal( 1 ) ).setItems( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyItemsJson() {
    new EcommerceAction( "foo", "bar", new BigDecimal( 1 ) ).setItems( "" );
  }

  @Test
  public void testStoresItemsJson() {
    EcommerceAction ecommerceAction = new EcommerceAction( "foo", "bar", new BigDecimal( 1 ) );

    ecommerceAction.setItems( "bar" );

    assertEquals( "bar", ecommerceAction.getParameter().get( getRequestKey( ECOMMERCE_ORDER_ITEMS ) ) );
  }
}
