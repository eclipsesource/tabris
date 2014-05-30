/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.model.action;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ACTION_GOAL_ID;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ECOMMERCE_ORDER_DISCOUNT;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ECOMMERCE_ORDER_ID;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ECOMMERCE_ORDER_ITEMS;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ECOMMERCE_ORDER_SHIPPING;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ECOMMERCE_ORDER_SUBTOTAL;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ECOMMERCE_ORDER_TAX;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ECOMMERCE_ORDER_TOTAL;

import java.math.BigDecimal;


@SuppressWarnings("restriction")
public class EcommerceAction extends Action {

  public EcommerceAction( String actionUrl, String orderId, BigDecimal total ) {
    super( actionUrl );
    whenNull( orderId ).throwIllegalArgument( "OrderId must not be null." );
    when( orderId.isEmpty() ).throwIllegalArgument( "OrderId must not be empty." );
    whenNull( total ).throwIllegalArgument( "Total must not be null." );
    addParameter( getRequestKey( ACTION_GOAL_ID ), Integer.valueOf( 0 ) );
    addParameter( getRequestKey( ECOMMERCE_ORDER_ID ), orderId );
    addParameter( getRequestKey( ECOMMERCE_ORDER_TOTAL ), total );
  }

  public EcommerceAction setSubtotal( BigDecimal subtotal ) {
    whenNull( subtotal ).throwIllegalArgument( "Subtotal must not be null" );
    addParameter( getRequestKey( ECOMMERCE_ORDER_SUBTOTAL ), subtotal );
    return this;
  }

  public EcommerceAction setTax( BigDecimal tax ) {
    whenNull( tax ).throwIllegalArgument( "Tax must not be null." );
    addParameter( getRequestKey( ECOMMERCE_ORDER_TAX ), tax );
    return this;
  }

  public EcommerceAction setShipping( BigDecimal shipping ) {
    whenNull( shipping ).throwIllegalArgument( "Shipping must not be null" );
    addParameter( getRequestKey( ECOMMERCE_ORDER_SHIPPING ), shipping );
    return this;
  }

  public EcommerceAction setDiscount( BigDecimal discount ) {
    whenNull( discount ).throwIllegalArgument( "Discount must not be null" );
    addParameter( getRequestKey( ECOMMERCE_ORDER_DISCOUNT ), discount );
    return this;
  }

  public EcommerceAction setItems( String items ) {
    whenNull( items ).throwIllegalArgument( "items must not be null." );
    when( items.isEmpty() ).throwIllegalArgument( "items must not be empty." );
    addParameter( getRequestKey( ECOMMERCE_ORDER_ITEMS ), items );
    return this;
  }
}
