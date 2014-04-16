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
package com.eclipsesource.tabris.tracking.internal.analytics.model;

import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.TRACKING_ID;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.VERSION;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

public class AnalyticsConfigurationTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullVersion() {
    new AnalyticsConfiguration( null, "bar" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyVersion() {
    new AnalyticsConfiguration( "", "bar" );
  }

  @Test
  public void testSetsVersion() {
    AnalyticsConfiguration configuration = new AnalyticsConfiguration( "foo", "bar" );

    Map<String, Object> parameter = configuration.getParameter();

    assertEquals( "foo", parameter.get( getRequestKey( VERSION ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullTrackingId() {
    new AnalyticsConfiguration( "foo", null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyTrackingId() {
    new AnalyticsConfiguration( "foo", "" );
  }

  @Test
  public void testSetsTrackingId() {
    AnalyticsConfiguration configuration = new AnalyticsConfiguration( "foo", "baz" );

    Map<String, Object> parameter = configuration.getParameter();

    assertEquals( "baz", parameter.get( getRequestKey( TRACKING_ID ) ) );
  }

}
