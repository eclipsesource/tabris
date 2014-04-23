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
import static org.mockito.Mockito.when;

import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.tracking.TrackingEvent;
import com.eclipsesource.tabris.tracking.TrackingEvent.EventType;
import com.eclipsesource.tabris.tracking.TrackingInfo;
import com.eclipsesource.tabris.ui.ActionConfiguration;
import com.eclipsesource.tabris.ui.PageConfiguration;


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
    PageConfiguration config = mock( PageConfiguration.class );
    when( config.getId() ).thenReturn( "foo" );
    when( config.getTitle() ).thenReturn( "title" );
    TrackingEvent event = new TrackingEvent( EventType.PAGE_VIEW, createInfo(), config, 23 );

    tracker.handleEvent( event );

    verify( out ).println( "PAGE_VIEW - foo (title) [appId, model, vendor, osVersion]" );
  }

  @Test
  public void testPrintsAction() {
    ActionConfiguration config = mock( ActionConfiguration.class );
    when( config.getId() ).thenReturn( "foo" );
    when( config.getTitle() ).thenReturn( "title" );
    TrackingEvent event = new TrackingEvent( EventType.ACTION, createInfo(), config, 23 );

    tracker.handleEvent( event );

    verify( out ).println( "ACTION - foo (title) [appId, model, vendor, osVersion]" );
  }

  @Test
  public void testPrintsSearch() {
    ActionConfiguration config = mock( ActionConfiguration.class );
    when( config.getId() ).thenReturn( "foo" );
    when( config.getTitle() ).thenReturn( "title" );
    TrackingEvent event = new TrackingEvent( EventType.SEARCH, createInfo(), config, 23 );

    tracker.handleEvent( event );

    verify( out ).println( "SEARCH - query=query [appId, model, vendor, osVersion]" );
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
