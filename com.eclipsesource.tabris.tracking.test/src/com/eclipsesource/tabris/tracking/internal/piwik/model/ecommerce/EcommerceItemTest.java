/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.model.ecommerce;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;


public class EcommerceItemTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullName() {
    new EcommerceItem( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyName() {
    new EcommerceItem( "" );
  }

  @Test
  public void testStoresName() {
    EcommerceItem ecommerceItem = new EcommerceItem( "foo" );

    assertEquals( "foo", ecommerceItem.getName() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullSku() {
    new EcommerceItem( "foo" ).setSku( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptySku() {
    new EcommerceItem( "foo" ).setSku( "" );
  }

  @Test
  public void testStoresSku() {
    EcommerceItem ecommerceItem = new EcommerceItem( "foo" );

    ecommerceItem.setSku( "foo" );

    assertEquals( "foo", ecommerceItem.getSku() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCategory() {
    new EcommerceItem( "foo" ).setCategory( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyCategory() {
    new EcommerceItem( "foo" ).setCategory( "" );
  }

  @Test
  public void testStoresCategory() {
    EcommerceItem ecommerceItem = new EcommerceItem( "foo" );

    ecommerceItem.setCategory( "foo" );

    assertEquals( "foo", ecommerceItem.getCategory() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullPrice() {
    new EcommerceItem( "foo" ).setPrice( null );
  }

  @Test
  public void testStoresPrice() {
    EcommerceItem ecommerceItem = new EcommerceItem( "foo" );

    ecommerceItem.setPrice( BigDecimal.valueOf( 20 ) );

    assertEquals( BigDecimal.valueOf( 20 ), ecommerceItem.getPrice() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNegativeQuantity() {
    new EcommerceItem( "foo" ).setQuantity( -1 );
  }

  @Test
  public void testStoresQuantity() {
    EcommerceItem ecommerceItem = new EcommerceItem( "foo" );

    ecommerceItem.setQuantity( 20 );

    assertEquals( Integer.valueOf( 20 ), Integer.valueOf( ecommerceItem.getQuantity() ) );
  }
}
