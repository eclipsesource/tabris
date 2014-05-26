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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.PrintStream;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.tracking.Order;
import com.eclipsesource.tabris.tracking.TrackingEvent;
import com.eclipsesource.tabris.tracking.TrackingEvent.EventType;
import com.eclipsesource.tabris.tracking.TrackingInfo;


public class ConsoleTrackerTest {

  private PrintStream out;
  private ConsoleTracker tracker;

  @Before
  public void setUp() {
    out = mock( PrintStream.class );
    System.setOut( out );
    tracker = new ConsoleTracker();
  }

  @Test
  public void testPrintsPageView() {
    TrackingEvent event = new TrackingEvent( EventType.PAGE_VIEW, createInfo(), "foo", 23 );

    tracker.handleEvent( event );

    verify( out ).println( "PAGE_VIEW - foo [appId, model, vendor, osVersion]" );
  }

  @Test
  public void testPrintsAction() {
    TrackingEvent event = new TrackingEvent( EventType.ACTION, createInfo(), "foo", 23 );

    tracker.handleEvent( event );

    verify( out ).println( "ACTION - foo [appId, model, vendor, osVersion]" );
  }

  @Test
  public void testPrintsSearch() {
    TrackingEvent event = new TrackingEvent( EventType.SEARCH, createInfo(), "foo", 23 );

    tracker.handleEvent( event );

    verify( out ).println( "SEARCH - query=query [appId, model, vendor, osVersion]" );
  }

  @Test
  public void testPrintsOrder() {
    Order order = new Order( "foo", BigDecimal.ONE );
    TrackingEvent event = new TrackingEvent( EventType.ORDER, createInfo(), order, 23 );

    tracker.handleEvent( event );

    verify( out ).println( "ORDER - " + order.getOrderId() + " (1, 0, 0) [appId, model, vendor, osVersion]" );
  }

  @Test
  public void testPrintsEvent() {
    TrackingEvent event = new TrackingEvent( EventType.EVENT, createInfo(), "foo", 23 );

    tracker.handleEvent( event );

    verify( out ).println( "EVENT - foo [appId, model, vendor, osVersion]" );
  }

  private TrackingInfo createInfo() {
    TrackingInfo info = new TrackingInfo();
    info.setAppId( "appId" );
    info.setDeviceOsVersion( "osVersion" );
    info.setDeviceVendor( "vendor" );
    info.setDeviceModel( "model" );
    info.setSearchQuery( "query" );
    return info;
  }
}
