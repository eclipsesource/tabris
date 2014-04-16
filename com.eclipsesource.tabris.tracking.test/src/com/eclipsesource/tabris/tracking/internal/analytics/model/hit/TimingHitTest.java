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
package com.eclipsesource.tabris.tracking.internal.analytics.model.hit;

import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.DNS_TIME;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.HIT_TYPE;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.PAGE_DOWNLOAD_TIME;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.PAGE_LOAD_TIME;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.REDIRECT_RESPONSE_TIME;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.SERVER_RESPONSE_TIME;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.TCP_CONNECT_TIME;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.USER_TIMING_CATEGORY;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.USER_TIMING_LABEL;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.USER_TIMING_TIME;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.USER_TIMING_VARIABLE_NAME;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValueProvider.getRequestValue;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValues.HIT_TIMING;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

public class TimingHitTest {

  @Test
  public void testTimingHitSavesTimingHitType() {
    TimingHit timingHit = new TimingHit();

    String hitType = ( String )timingHit.getParameter().get( getRequestKey( HIT_TYPE ) );

    assertEquals( getRequestValue( HIT_TIMING ), hitType );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullUserTimingCategory() {
    new TimingHit().setUserTimingCategory( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyUserTimingCategory() {
    new TimingHit().setUserTimingCategory( "" );
  }

  @Test
  public void testSetsUserTimingCategory() {
    TimingHit eventHit = new TimingHit().setUserTimingCategory( "foo" );

    String userTimingCategory = ( String )eventHit.getParameter().get( getRequestKey( USER_TIMING_CATEGORY ) );

    assertEquals( "foo", userTimingCategory );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullUserTimingVariableName() {
    new TimingHit().setUserTimingVariableName( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyUserTimingVariableName() {
    new TimingHit().setUserTimingVariableName( "" );
  }

  @Test
  public void testSetsUserTimingVariableName() {
    TimingHit eventHit = new TimingHit().setUserTimingVariableName( "foo" );

    String userTimingVariableName = ( String )eventHit.getParameter().get( getRequestKey( USER_TIMING_VARIABLE_NAME ) );

    assertEquals( "foo", userTimingVariableName );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullUserTimingTime() {
    new TimingHit().setUserTimingVariableName( null );
  }

  @Test
  public void testSetsUserTimingTime() {
    TimingHit eventHit = new TimingHit().setUserTimingTime( 2 );

    Map<String, Object> parameter = eventHit.getParameter();

    assertEquals( Integer.valueOf( 2 ), parameter.get( getRequestKey( USER_TIMING_TIME ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullUserTimingLabel() {
    new TimingHit().setUserTimingLabel( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyUserTimingLabel() {
    new TimingHit().setUserTimingLabel( "" );
  }

  @Test
  public void testSetsUserTimingLabel() {
    TimingHit eventHit = new TimingHit().setUserTimingLabel( "foo" );

    String userTimingLabel = ( String )eventHit.getParameter().get( getRequestKey( USER_TIMING_LABEL ) );

    assertEquals( "foo", userTimingLabel );
  }

  @Test
  public void testSetsPageLoadTime() {
    TimingHit eventHit = new TimingHit().setPageLoadTime( 2 );

    Map<String, Object> parameter = eventHit.getParameter();

    assertEquals( Integer.valueOf( 2 ), parameter.get( getRequestKey( PAGE_LOAD_TIME ) ) );
  }

  @Test
  public void testSetsDnsTime() {
    TimingHit eventHit = new TimingHit().setDnsTime( 2 );

    Map<String, Object> parameter = eventHit.getParameter();

    assertEquals( Integer.valueOf( 2 ), parameter.get( getRequestKey( DNS_TIME ) ) );
  }

  @Test
  public void testSetsPageDownloadTime() {
    TimingHit eventHit = new TimingHit().setPageDownloadTime( 2 );

    Map<String, Object> parameter = eventHit.getParameter();

    assertEquals( Integer.valueOf( 2 ), parameter.get( getRequestKey( PAGE_DOWNLOAD_TIME ) ) );
  }

  @Test
  public void testSetsRedirectResponseTime() {
    TimingHit eventHit = new TimingHit().setRedirectResponseTime( 2 );

    Map<String, Object> parameter = eventHit.getParameter();

    assertEquals( Integer.valueOf( 2 ), parameter.get( getRequestKey( REDIRECT_RESPONSE_TIME ) ) );
  }

  @Test
  public void testSetsTcpConnectTime() {
    TimingHit eventHit = new TimingHit().setTcpConnectTime( 2 );

    Map<String, Object> parameter = eventHit.getParameter();

    assertEquals( Integer.valueOf( 2 ), parameter.get( getRequestKey( TCP_CONNECT_TIME ) ) );
  }

  @Test
  public void testSetsServerResponseTime() {
    TimingHit eventHit = new TimingHit().setServerResponseTime( 2 );

    Map<String, Object> parameter = eventHit.getParameter();

    assertEquals( Integer.valueOf( 2 ), parameter.get( getRequestKey( SERVER_RESPONSE_TIME ) ) );
  }

}
