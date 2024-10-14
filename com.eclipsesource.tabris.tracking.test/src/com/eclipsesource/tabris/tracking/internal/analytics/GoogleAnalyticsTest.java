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
package com.eclipsesource.tabris.tracking.internal.analytics;

import static com.github.restdriver.clientdriver.RestClientDriver.giveEmptyResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.tracking.internal.analytics.model.AdvancedConfiguration;
import com.eclipsesource.tabris.tracking.internal.analytics.model.AnalyticsConfiguration;
import com.eclipsesource.tabris.tracking.internal.analytics.model.hit.ScreenViewHit;
import com.github.restdriver.clientdriver.ClientDriverRule;

public class GoogleAnalyticsTest {

  @Rule
  public ClientDriverRule driver = new ClientDriverRule();

  @Test
  public void testHasAnalyticsURL() {
    String url = GoogleAnalytics.ANALYTICS_URL;

    assertEquals( url, "http://www.google-analytics.com/collect" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullAnalyticsConfiguration() {
    new GoogleAnalytics( "foo", null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullAppName() {
    new GoogleAnalytics( null, new AnalyticsConfiguration( "foo", "baz" ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyAppName() {
    new GoogleAnalytics( "", new AnalyticsConfiguration( "foo", "baz" ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSendFailsWithNullHit() {
    GoogleAnalytics analytics = new GoogleAnalytics( "foo", new AnalyticsConfiguration( "foo", "bar" ) );

    analytics.track( null, "id" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSendFailsWithNullAdvancedConfiguration() {
    GoogleAnalytics analytics = new GoogleAnalytics( "foo", new AnalyticsConfiguration( "foo", "bar" ) );

    analytics.track( new ScreenViewHit( "foo" ), "id", null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSendFailsWithNullClientIdWithoutConfig() {
    GoogleAnalytics analytics = new GoogleAnalytics( "foo", new AnalyticsConfiguration( "foo", "bar" ) );

    analytics.track( new ScreenViewHit( "foo" ), null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSendFailsWithNullClientIdAndConfig() {
    GoogleAnalytics analytics = new GoogleAnalytics( "foo", new AnalyticsConfiguration( "foo", "bar" ) );

    analytics.track( new ScreenViewHit( "foo" ), null, new AdvancedConfiguration() );
  }

  @Ignore
  @Test( expected = IllegalStateException.class )
  public void testSendsRequestFailsWithNon200Response() {
    driver.addExpectation( onRequestTo( "/" ).withAnyParams(), giveEmptyResponse().withStatus( 404 ) );

    AnalyticsConfiguration configuration = new AnalyticsConfiguration( "1", "baz" );
    GoogleAnalytics analytics = new GoogleAnalytics( driver.getBaseUrl(), "foo", configuration );

    analytics.track( new ScreenViewHit( "screenName" ), "baz", mock( AdvancedConfiguration.class ) );
  }

  @Ignore
  @Test
  public void testSendsRequestWithConfigurationParameter() {
    driver.addExpectation( onRequestTo( "/" ).withParam( "v", "1" )
                                                .withParam( "tid", "bar" )
                                                .withParam( "cid", "baz" )
                                                .withParam( "t", "screenview" )
                                                .withParam( "cd", "screenName" )
                                                .withParam( "an", "foo" ),
                           giveEmptyResponse().withStatus( 200 ) );

    AnalyticsConfiguration configuration = new AnalyticsConfiguration( "1", "bar" );
    GoogleAnalytics analytics = new GoogleAnalytics( driver.getBaseUrl(), "foo", configuration );

    analytics.track( new ScreenViewHit( "screenName" ), "baz", mock( AdvancedConfiguration.class ) );
  }

  @Ignore
  @Test
  public void testSendsRequestWithAdvancedConfigurationParameter() {
    driver.addExpectation( onRequestTo( "/" ).withParam( "v", "1" )
                                                .withParam( "tid", "bar" )
                                                .withParam( "cid", "baz" )
                                                .withParam( "t", "screenview" )
                                                .withParam( "cd", "screenName" )
                                                .withParam( "uip", "62.34.56.7" )
                                                .withParam( "an", "foo" ),
                           giveEmptyResponse().withStatus( 200 ) );

    AnalyticsConfiguration configuration = new AnalyticsConfiguration( "1", "bar" );
    GoogleAnalytics analytics = new GoogleAnalytics( driver.getBaseUrl(), "foo", configuration );
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration();
    advancedConfiguration.setIpOverride( "62.34.56.7" );

    analytics.track( new ScreenViewHit( "screenName" ), "baz", advancedConfiguration );
  }
}
