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
import java.util.ArrayList;

import org.junit.Test;


public class EcommerceItemsBuilderTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullItemsList() {
    new EcommerceItemsBuilder( null );
  }

  @Test
  public void testSetsItems() {
    ArrayList<EcommerceItem> list = new ArrayList<EcommerceItem>();
    list.add( new EcommerceItem( "foo" ) );

    EcommerceItemsBuilder builder = new EcommerceItemsBuilder( list );

    assertEquals( list, builder.getItems() );
  }

  @Test
  public void testBuildsJson() {
    EcommerceItem item = createItem();
    ArrayList<EcommerceItem> items = new ArrayList<EcommerceItem>();
    items.add( item );

    EcommerceItemsBuilder builder = new EcommerceItemsBuilder( items );

    assertEquals( "[[\"sku\",\"name\",\"category\",30,2]]", builder.buildJson() );
  }

  private EcommerceItem createItem() {
    EcommerceItem item = new EcommerceItem( "name" );
    item.setSku( "sku" );
    item.setCategory( "category" );
    item.setPrice( BigDecimal.valueOf( 30 ) );
    item.setQuantity( 2 );
    return item;
  }

  @Test
  public void testBuildsJsonWithMissingOptionalItemAttributes() {
    EcommerceItem item = new EcommerceItem( "foo" );
    ArrayList<EcommerceItem> items = new ArrayList<EcommerceItem>();
    items.add( item );

    EcommerceItemsBuilder builder = new EcommerceItemsBuilder( items );

    assertEquals( "[[\"\",\"foo\",\"\",0,0]]", builder.buildJson() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyItemsList() {
    new EcommerceItemsBuilder( new ArrayList<EcommerceItem>() );
  }
}
