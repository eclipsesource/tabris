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

import java.util.List;

import org.junit.Test;


public class OrderTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullId() {
    new Order( null, 1 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyId() {
    new Order( "", 1 );
  }

  @Test
  public void testIsEqual() {
    Order order = new Order( "foo", 1 );
    Order order2 = new Order( "foo", 1 );

    assertEquals( order, order2 );
  }

  @Test
  public void testHasId() {
    Order order = new Order( "foo", 1 );

    String id = order.getOrderId();

    assertEquals( "foo", id );
  }

  @Test
  public void testHasRevenue() {
    Order order = new Order( "foo", 1 );

    double revenue = order.getRevenue();

    assertEquals( 1, revenue, 0 );
  }

  @Test
  public void testHasShipment() {
    Order order = new Order( "foo", 1 );

    order.setShipping( 2 );
    double shipment = order.getShipping();

    assertEquals( 2, shipment, 0 );
  }

  @Test
  public void testSetShipmentReturnsOrder() {
    Order order = new Order( "foo", 1 );

    Order actualOrder = order.setShipping( 2 );

    assertSame( order, actualOrder );
  }

  @Test
  public void testHasTax() {
    Order order = new Order( "foo", 1 );

    order.setTax( 3 );
    double tax = order.getTax();

    assertEquals( 3, tax, 0 );
  }

  @Test
  public void testSetTaxReturnsOrder() {
    Order order = new Order( "foo", 1 );

    Order actualOrder = order.setTax( 3 );

    assertSame( order, actualOrder );
  }

  @Test
  public void testAddsItem() {
    Order order = new Order( "foo", 1 );
    OrderItem item = new OrderItem( "bar" );

    order.addItem( item );

    List<OrderItem> items = order.getItems();
    assertSame( items.get( 0 ), item );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToAddNullItem() {
    Order order = new Order( "foo", 1 );

    order.addItem( null );
  }

  @Test
  public void testRemovesItem() {
    Order order = new Order( "foo", 1 );
    OrderItem item = new OrderItem( "bar" );
    order.addItem( item );

    order.removeItem( item );

    List<OrderItem> items = order.getItems();
    assertTrue( items.isEmpty() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToRemoveNullItem() {
    Order order = new Order( "foo", 1 );
    OrderItem item = new OrderItem( "bar" );
    order.addItem( item );

    order.removeItem( null );
  }

  @Test
  public void testItemsAreSafeCopy() {
    Order order = new Order( "foo", 1 );
    OrderItem item = new OrderItem( "bar" );

    List<OrderItem> items = order.getItems();
    order.addItem( item );

    List<OrderItem> items2 = order.getItems();
    assertSame( items2.get( 0 ), item );
    assertTrue( items.isEmpty() );
  }
}
