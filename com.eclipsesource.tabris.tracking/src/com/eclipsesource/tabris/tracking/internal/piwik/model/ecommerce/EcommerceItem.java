/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.model.ecommerce;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import java.math.BigDecimal;


@SuppressWarnings("restriction")
public class EcommerceItem {

  private String sku;
  private final String name;
  private String category;
  private BigDecimal price;
  private int quantity;

  public EcommerceItem( String name ) {
    whenNull( name ).throwIllegalArgument( "Name must not be null." );
    when( name.isEmpty() ).throwIllegalArgument( "Name must not be empty." );
    this.name = name;
  }

  public void setSku( String sku ) {
    whenNull( sku ).throwIllegalArgument( "Sku must not be null." );
    when( sku.isEmpty() ).throwIllegalArgument( "Sku must not be empty." );
    this.sku = sku;
  }

  public String getSku() {
    return sku;
  }

  public String getName() {
    return name;
  }

  public void setCategory( String category ) {
    whenNull( category ).throwIllegalArgument( "Category must not be null." );
    when( category.isEmpty() ).throwIllegalArgument( "Category must not be empty." );
    this.category = category;
  }

  public String getCategory() {
    return category;
  }

  public void setPrice( BigDecimal price ) {
    whenNull( price ).throwIllegalArgument( "Price must not be null." );
    this.price = price;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setQuantity( int quantity ) {
    when( quantity <= 0 ).throwIllegalArgument( "Quantity must be > 0, but was " );
    this.quantity = quantity;
  }

  public int getQuantity() {
    return quantity;
  }
}
