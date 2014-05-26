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

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("restriction")
public class Order {

  private final List<OrderItem> items;
  private final String orderId;
  private final BigDecimal total;
  private BigDecimal shipping;
  private BigDecimal tax;

  public Order( String orderId, BigDecimal total ) {
    whenNull( orderId ).throwIllegalArgument( "OrderId must not be null" );
    when( orderId.isEmpty() ).throwIllegalArgument( "OrderId must not be empty" );
    whenNull( total ).throwIllegalArgument( "Total must not be null" );
    this.items = new ArrayList<OrderItem>();
    this.orderId = orderId;
    this.total = total;
    this.shipping = ZERO;
    this.tax = ZERO;
  }

  public String getOrderId() {
    return orderId;
  }

  public BigDecimal getRevenue() {
    return total;
  }

  public Order setShipping( BigDecimal shipping ) {
    whenNull( shipping ).throwIllegalArgument( "Shipping must not be null" );
    this.shipping = shipping;
    return this;
  }

  public BigDecimal getShipping() {
    return shipping;
  }

  public Order setTax( BigDecimal tax ) {
    whenNull( tax ).throwIllegalArgument( "Tax must not be null" );
    this.tax = tax;
    return this;
  }

  public BigDecimal getTax() {
    return tax;
  }

  public void addItem( OrderItem item ) {
    whenNull( item ).throwIllegalArgument( "OrderItem must not be null" );
    items.add( item );
  }

  public void removeItem( OrderItem item ) {
    whenNull( item ).throwIllegalArgument( "OrderItem must not be null" );
    items.remove( item );
  }

  public List<OrderItem> getItems() {
    return new ArrayList<OrderItem>( items );
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( items == null ) ? 0 : items.hashCode() );
    result = prime * result + ( ( orderId == null ) ? 0 : orderId.hashCode() );
    result = prime * result + ( ( shipping == null ) ? 0 : shipping.hashCode() );
    result = prime * result + ( ( tax == null ) ? 0 : tax.hashCode() );
    result = prime * result + ( ( total == null ) ? 0 : total.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj ) {
    if( this == obj ) {
      return true;
    }
    if( obj == null ) {
      return false;
    }
    if( getClass() != obj.getClass() ) {
      return false;
    }
    Order other = ( Order )obj;
    if( items == null ) {
      if( other.items != null ) {
        return false;
      }
    } else if( !items.equals( other.items ) ) {
      return false;
    }
    if( orderId == null ) {
      if( other.orderId != null ) {
        return false;
      }
    } else if( !orderId.equals( other.orderId ) ) {
      return false;
    }
    if( shipping == null ) {
      if( other.shipping != null ) {
        return false;
      }
    } else if( !shipping.equals( other.shipping ) ) {
      return false;
    }
    if( tax == null ) {
      if( other.tax != null ) {
        return false;
      }
    } else if( !tax.equals( other.tax ) ) {
      return false;
    }
    if( total == null ) {
      if( other.total != null ) {
        return false;
      }
    } else if( !total.equals( other.total ) ) {
      return false;
    }
    return true;
  }

}
