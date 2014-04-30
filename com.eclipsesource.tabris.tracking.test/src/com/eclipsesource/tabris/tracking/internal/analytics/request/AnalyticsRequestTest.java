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
package com.eclipsesource.tabris.tracking.internal.analytics.request;

import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.APP_NAME;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.CLIENT_ID;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.eclipsesource.tabris.tracking.internal.Requestable;
import com.eclipsesource.tabris.tracking.internal.analytics.model.hit.AppViewHit;

public class AnalyticsRequestTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullAppName() {
    new AnalyticsRequest( null, "foo", new AppViewHit( "qaz" ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyAppName() {
    new AnalyticsRequest( "", "foo", new AppViewHit( "qaz" ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullClientId() {
    new AnalyticsRequest( "foo", null, new AppViewHit( "qaz" ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyClientId() {
    new AnalyticsRequest( "foo", "", new AppViewHit( "qaz" ) );
  }

  @Test
  public void testSetsAppName() {
    AnalyticsRequest request = new AnalyticsRequest( "appName", "foo", new TestRequestable( "foo", "bar" ) );

    String appName = ( String )request.assemble().get( getRequestKey( APP_NAME ) );

    assertEquals( "appName", appName );
  }

  @Test
  public void testSetsClientIdAsParameter() {
    AnalyticsRequest request = new AnalyticsRequest( "appName", "foo", new TestRequestable( "foo", "bar" ) );

    String clientId = ( String )request.assemble().get( getRequestKey( CLIENT_ID ) );

    assertEquals( "foo", clientId );
  }

  @Test
  public void testAddsParameterFromRequestables() {
    TestRequestable requestable1 = new TestRequestable( "foo", "bar" );
    TestRequestable requestable2 = new TestRequestable( "foo2", "bar2" );
    AnalyticsRequest request = new AnalyticsRequest( "foo", "foo", requestable1, requestable2 );

    Map<String, Object> parameter = request.assemble();

    assertEquals( "bar", parameter.get( "foo" ) );
    assertEquals( "bar2", parameter.get( "foo2" ) );
  }

  private static class TestRequestable implements Requestable {

    private final HashMap<String, Object> parameter;

    public TestRequestable( String key, String value ) {
      this.parameter = new HashMap<String, Object>();
      parameter.put( key, value );
    }

    @Override
    public Map<String, Object> getParameter() {
      return parameter;
    }

  }

}
