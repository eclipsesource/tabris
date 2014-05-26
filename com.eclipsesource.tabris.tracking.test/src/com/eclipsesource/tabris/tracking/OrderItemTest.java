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

import java.math.BigDecimal;

import org.junit.Test;


public class OrderItemTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullName() {
    new OrderItem( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyName() {
    new OrderItem( "" );
  }

  @Test
  public void testIsEqual() {
    OrderItem item = new OrderItem( "foo" );
    OrderItem item2 = new OrderItem( "foo" );

    assertEquals( item, item2 );
  }

  @Test
  public void testHasName() {
    OrderItem item = new OrderItem( "foo" );

    String name = item.getName();

    assertEquals( "foo", name );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNullPrice() {
    OrderItem item = new OrderItem( "foo" );

    item.setPrice( null );
  }

  @Test
  public void testSetsPrice() {
    OrderItem item = new OrderItem( "foo" );

    item.setPrice( BigDecimal.valueOf( 23 ) );

    BigDecimal price = item.getPrice();
    assertEquals( BigDecimal.valueOf( 23 ), price );
  }

  @Test
  public void testSetPriceReturnsItem() {
    OrderItem item = new OrderItem( "foo" );

    OrderItem actualItem = item.setPrice( BigDecimal.valueOf( 23 ) );

    assertSame( item, actualItem );
  }

  @Test
  public void testHasDefaultQuantity() {
    OrderItem item = new OrderItem( "foo" );

    int quantity = item.getQuantity();

    assertEquals( 1, quantity );
  }

  @Test
  public void testSetsQuantity() {
    OrderItem item = new OrderItem( "foo" );

    item.setQuantity( 22 );

    int quantity = item.getQuantity();
    assertEquals( 22, quantity );
  }

  @Test
  public void testSetQuantityReturnsItem() {
    OrderItem item = new OrderItem( "foo" );

    OrderItem actualItem = item.setQuantity( 22 );

    assertSame( item, actualItem );
  }

  @Test
  public void testSetsSKU() {
    OrderItem item = new OrderItem( "foo" );

    item.setSKU( "sku" );

    String sku = item.getSKU();
    assertSame( sku, "sku" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullSKU() {
    OrderItem item = new OrderItem( "foo" );

    item.setSKU( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptySKU() {
    OrderItem item = new OrderItem( "foo" );

    item.setSKU( "" );
  }

  @Test
  public void testSetSKUReturnsItem() {
    OrderItem item = new OrderItem( "foo" );

    OrderItem actualItem = item.setSKU( "sku" );

    assertSame( item, actualItem );
  }

  @Test
  public void testSetsCategory() {
    OrderItem item = new OrderItem( "foo" );

    item.setCategory( "bar" );

    String category = item.getCategory();
    assertEquals( category, "bar" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCategory() {
    OrderItem item = new OrderItem( "foo" );

    item.setCategory( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyCategory() {
    OrderItem item = new OrderItem( "foo" );

    item.setCategory( "" );
  }

  @Test
  public void testSetCategoryReturnsItem() {
    OrderItem item = new OrderItem( "foo" );

    OrderItem actualItem = item.setCategory( "bar" );

    assertSame( item, actualItem );
  }
}
