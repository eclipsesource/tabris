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

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("restriction")
public class Order {

  private final List<OrderItem> items;
  private final String orderId;
  private final double revenue;
  private double shipping;
  private double tax;

  public Order( String orderId, double revenue ) {
    whenNull( orderId ).throwIllegalArgument( "OrderId must not be null" );
    when( orderId.isEmpty() ).throwIllegalArgument( "OrderId must not be empty" );
    this.items = new ArrayList<OrderItem>();
    this.orderId = orderId;
    this.revenue = revenue;
  }

  public String getOrderId() {
    return orderId;
  }

  public double getRevenue() {
    return revenue;
  }

  public Order setShipping( double shipping ) {
    this.shipping = shipping;
    return this;
  }

  public double getShipping() {
    return shipping;
  }

  public Order setTax( double tax ) {
    this.tax = tax;
    return this;
  }

  public double getTax() {
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
    long temp;
    temp = Double.doubleToLongBits( revenue );
    result = prime * result + ( int )( temp ^ ( temp >>> 32 ) );
    temp = Double.doubleToLongBits( shipping );
    result = prime * result + ( int )( temp ^ ( temp >>> 32 ) );
    temp = Double.doubleToLongBits( tax );
    result = prime * result + ( int )( temp ^ ( temp >>> 32 ) );
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
    if( Double.doubleToLongBits( revenue ) != Double.doubleToLongBits( other.revenue ) ) {
      return false;
    }
    if( Double.doubleToLongBits( shipping ) != Double.doubleToLongBits( other.shipping ) ) {
      return false;
    }
    if( Double.doubleToLongBits( tax ) != Double.doubleToLongBits( other.tax ) ) {
      return false;
    }
    return true;
  }

}
