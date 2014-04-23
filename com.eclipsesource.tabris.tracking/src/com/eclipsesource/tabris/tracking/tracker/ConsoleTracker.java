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

import java.util.Date;

import com.eclipsesource.tabris.tracking.Tracker;
import com.eclipsesource.tabris.tracking.TrackingEvent;
import com.eclipsesource.tabris.tracking.TrackingEvent.EventType;
import com.eclipsesource.tabris.tracking.TrackingInfo;
import com.eclipsesource.tabris.ui.ActionConfiguration;
import com.eclipsesource.tabris.ui.PageConfiguration;


/**
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
    builder.append( new Date( event.getTimestamp() ) );
    builder.append( ": " );
    builder.append( event.getType() + " - " );
  }

  private void appendEventDetail( StringBuilder builder, TrackingEvent event ) {
    if( event.getType() == EventType.PAGE_VIEW ) {
      appendPageView( builder, event );
    } else if( event.getType() == EventType.ACTION ) {
      appendAction( builder, event );
    } else if( event.getType() == EventType.SEARCH ) {
      appendSearch( builder, event );
    }
  }

  private void appendPageView( StringBuilder builder, TrackingEvent event ) {
    PageConfiguration configuration = ( PageConfiguration )event.getDetail();
    builder.append( configuration.getId() );
    builder.append( " (" );
    builder.append( configuration.getTitle() );
    builder.append( ")" );
  }

  private void appendAction( StringBuilder builder, TrackingEvent event ) {
    ActionConfiguration configuration = ( ActionConfiguration )event.getDetail();
    builder.append( configuration.getId() );
    builder.append( " (" );
    builder.append( configuration.getTitle() );
    builder.append( ")" );
  }

  private void appendSearch( StringBuilder builder, TrackingEvent event ) {
    builder.append( "query=" );
    builder.append( event.getInfo().getSearchQuery() );
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
