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
package com.eclipsesource.tabris.tracking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;


public class OrderTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullId() {
    new Order( null, BigDecimal.ONE );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyId() {
    new Order( "", BigDecimal.ONE );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullTotal() {
    new Order( "foo", null );
  }

  @Test
  public void testIsEqual() {
    Order order = new Order( "foo", BigDecimal.ONE );
    Order order2 = new Order( "foo", BigDecimal.ONE );

    assertEquals( order, order2 );
  }

  @Test
  public void testHasId() {
    Order order = new Order( "foo", BigDecimal.ONE );

    String id = order.getOrderId();

    assertEquals( "foo", id );
  }

  @Test
  public void testHasTotal() {
    Order order = new Order( "foo", BigDecimal.ONE );

    BigDecimal total = order.getRevenue();

    assertEquals( BigDecimal.ONE, total );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNullShipment() {
    Order order = new Order( "foo", BigDecimal.ONE );

    order.setShipping( null );
  }

  @Test
  public void testHasDefaultShipment() {
    Order order = new Order( "foo", BigDecimal.ONE );

    BigDecimal shipment = order.getShipping();

    assertEquals( BigDecimal.valueOf( 0 ), shipment );
  }

  @Test
  public void testHasShipment() {
    Order order = new Order( "foo", BigDecimal.ONE );

    order.setShipping( BigDecimal.valueOf( 2 ) );
    BigDecimal shipment = order.getShipping();

    assertEquals( BigDecimal.valueOf( 2 ), shipment );
  }

  @Test
  public void testSetShipmentReturnsOrder() {
    Order order = new Order( "foo", BigDecimal.ONE );

    Order actualOrder = order.setShipping( BigDecimal.valueOf( 2 ) );

    assertSame( order, actualOrder );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNullTax() {
    Order order = new Order( "foo", BigDecimal.ONE );

    order.setTax( null );
  }

  @Test
  public void testHasDefaultTax() {
    Order order = new Order( "foo", BigDecimal.ONE );

    BigDecimal tax = order.getTax();

    assertEquals( BigDecimal.valueOf( 0 ), tax );
  }

  @Test
  public void testHasTax() {
    Order order = new Order( "foo", BigDecimal.ONE );

    order.setTax( BigDecimal.valueOf( 3 ) );
    BigDecimal tax = order.getTax();

    assertEquals( BigDecimal.valueOf( 3 ), tax );
  }

  @Test
  public void testSetTaxReturnsOrder() {
    Order order = new Order( "foo", BigDecimal.ONE );

    Order actualOrder = order.setTax( BigDecimal.valueOf( 3 ) );

    assertSame( order, actualOrder );
  }

  @Test
  public void testAddsItem() {
    Order order = new Order( "foo", BigDecimal.ONE );
    OrderItem item = new OrderItem( "bar" );

    order.addItem( item );

    List<OrderItem> items = order.getItems();
    assertSame( items.get( 0 ), item );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToAddNullItem() {
    Order order = new Order( "foo", BigDecimal.ONE );

    order.addItem( null );
  }

  @Test
  public void testRemovesItem() {
    Order order = new Order( "foo", BigDecimal.ONE );
    OrderItem item = new OrderItem( "bar" );
    order.addItem( item );

    order.removeItem( item );

    List<OrderItem> items = order.getItems();
    assertTrue( items.isEmpty() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToRemoveNullItem() {
    Order order = new Order( "foo", BigDecimal.ONE );
    OrderItem item = new OrderItem( "bar" );
    order.addItem( item );

    order.removeItem( null );
  }

  @Test
  public void testItemsAreSafeCopy() {
    Order order = new Order( "foo", BigDecimal.ONE );
    OrderItem item = new OrderItem( "bar" );

    List<OrderItem> items = order.getItems();
    order.addItem( item );

    List<OrderItem> items2 = order.getItems();
    assertSame( items2.get( 0 ), item );
    assertTrue( items.isEmpty() );
  }
}
