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
package com.eclipsesource.tabris.tracking.tracker;

import com.eclipsesource.tabris.tracking.Order;
import com.eclipsesource.tabris.tracking.Tracker;
import com.eclipsesource.tabris.tracking.TrackingEvent;
import com.eclipsesource.tabris.tracking.TrackingEvent.EventType;
import com.eclipsesource.tabris.tracking.TrackingInfo;


/**
 * <p>
 * The {@link ConsoleTracker} is a simple example implementation of a {@link Tracker}. It prints every event tracked
 * to the console. You may use the {@link ConsoleTracker} as a template to implement your own {@link Tracker}.
 * </p>
 * @since 1.4
 */
public class ConsoleTracker implements Tracker {

  @Override
  public void handleEvent( TrackingEvent event ) {
    System.out.println( getEventString( event ) );
  }

  private String getEventString( TrackingEvent event ) {
    StringBuilder builder = new StringBuilder();
    appendEventPrefix( builder, event );
    appendEventDetail( builder, event );
    appendEventInfo( builder, event );
    return builder.toString();
  }

  private void appendEventPrefix( StringBuilder builder, TrackingEvent event ) {
    builder.append( event.getType() + " - " );
  }

  private void appendEventDetail( StringBuilder builder, TrackingEvent event ) {
    if( event.getType() == EventType.PAGE_VIEW ) {
      appendPageView( builder, event );
    } else if( event.getType() == EventType.ACTION ) {
      appendAction( builder, event );
    } else if( event.getType() == EventType.SEARCH ) {
      appendSearch( builder, event );
    } else if( event.getType() == EventType.ORDER ) {
      appendOrder( builder, event );
    } else if( event.getType() == EventType.EVENT ) {
      appendEvent( builder, event );
    }
  }

  private void appendPageView( StringBuilder builder, TrackingEvent event ) {
    builder.append( ( String )event.getDetail() );
  }

  private void appendAction( StringBuilder builder, TrackingEvent event ) {
    builder.append( ( String )event.getDetail() );
  }

  private void appendSearch( StringBuilder builder, TrackingEvent event ) {
    builder.append( "query=" );
    builder.append( event.getInfo().getSearchQuery() );
  }

  private void appendOrder( StringBuilder builder, TrackingEvent event ) {
    Order info = ( Order )event.getDetail();
    builder.append( info.getOrderId() );
    builder.append( " (" );
    builder.append( info.getTotal() + ", " + info.getShipping() + ", " + info.getTax() );
    builder.append( ")" );
  }

  private void appendEvent( StringBuilder builder, TrackingEvent event ) {
    builder.append( ( String )event.getDetail() );
  }

  private void appendEventInfo( StringBuilder builder, TrackingEvent event ) {
    TrackingInfo info = event.getInfo();
    builder.append( " [" );
    builder.append( info.getAppId() + ", " );
    builder.append( info.getDeviceModel() + ", " );
    builder.append( info.getDeviceVendor() + ", " );
    builder.append( info.getDeviceOsVersion() );
    builder.append( "]" );
  }

}
