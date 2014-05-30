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
import java.math.RoundingMode;
import java.util.List;

import org.eclipse.rap.json.JsonArray;


@SuppressWarnings("restriction")
public class EcommerceItemsBuilder {

  private final List<EcommerceItem> items;

  public EcommerceItemsBuilder( List<EcommerceItem> items ) {
    whenNull( items ).throwIllegalArgument( "Items must not be null." );
    when( items.size() == 0 ).throwIllegalArgument( "Items list must not be empty." );
    this.items = items;
  }

  protected List<EcommerceItem> getItems() {
    return items;
  }

  public String buildJson() {
    JsonArray jsonArray = new JsonArray();
    for( EcommerceItem item : items ) {
      JsonArray itemJsonArray = new JsonArray();
      itemJsonArray.add( item.getSku() != null ? item.getSku() : "" );
      itemJsonArray.add( item.getName() );
      itemJsonArray.add( item.getCategory() != null ? item.getCategory() : "" );
      itemJsonArray.add( toDouble( item.getPrice() != null ? item.getPrice() : new BigDecimal( 0 ) ) );
      itemJsonArray.add( item.getQuantity() );
      jsonArray.add( itemJsonArray );
    }
    return jsonArray.toString();
  }

  private double toDouble( BigDecimal amount ) {
    return amount.setScale( 2, RoundingMode.HALF_UP ).doubleValue();
  }
}
