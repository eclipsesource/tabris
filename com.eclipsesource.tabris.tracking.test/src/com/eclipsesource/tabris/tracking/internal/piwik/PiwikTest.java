/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik;

import static com.github.restdriver.clientdriver.RestClientDriver.giveEmptyResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;

import java.util.UUID;
import java.util.regex.Pattern;

import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.tracking.internal.piwik.model.AdvancedConfiguration;
import com.eclipsesource.tabris.tracking.internal.piwik.model.PiwikConfiguration;
import com.eclipsesource.tabris.tracking.internal.piwik.model.VisitorInformation;
import com.eclipsesource.tabris.tracking.internal.piwik.model.action.Action;
import com.github.restdriver.clientdriver.ClientDriverRule;

public class PiwikTest {

  @Rule
  public ClientDriverRule driver = new ClientDriverRule();

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullPiwikUrl() {
    new Piwik( null, new PiwikConfiguration( "piwikUrl", 2 ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyPiwikUrl() {
    new Piwik( "", new PiwikConfiguration( "piwikUrl", 2 ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullPiwikConfiguration() {
    new Piwik( "piwikUrl", null );
  }

  @Test( expected = IllegalStateException.class )
  public void testSendsRequestFailsWithNon200Response() {
    driver.addExpectation( onRequestTo( "/" ).withAnyParams(), giveEmptyResponse().withStatus( 404 ) );

    Piwik piwik = new Piwik( driver.getBaseUrl(), new PiwikConfiguration( "foo", 2 ) );

    piwik.track( new Action( "actionUrl" ) );
  }

  @Test
  public void testSendsRequestWithActionOnly() throws Exception {
    driver.addExpectation( onRequestTo( "/" ).withParam( "idsite", 2 )
      .withParam( "rec", 1 )
      .withParam( "apiv", "apiVersion" )
      .withParam( "rand", compileUuidPattern() )
      .withParam( "url", "actionUrl" ), giveEmptyResponse().withStatus( 200 ) );

    Piwik piwik = new Piwik( driver.getBaseUrl(), new PiwikConfiguration( "apiVersion", 2 ) );

    piwik.track( new Action( "actionUrl" ) );
  }

  @Test
  public void testSendsRequestWithVisitorInformation() throws Exception {
    driver.addExpectation( onRequestTo( "/" ).withParam( "idsite", 2 )
      .withParam( "rec", 1 )
      .withParam( "apiv", "apiVersion" )
      .withParam( "rand", compileUuidPattern() )
      .withParam( "url", "actionUrl" )
      .withParam( "res", "1280x1024" ), giveEmptyResponse().withStatus( 200 ) );

    Piwik piwik = new Piwik( driver.getBaseUrl(), new PiwikConfiguration( "apiVersion", 2 ) );

    piwik.track( new Action( "actionUrl" ),
                 new VisitorInformation().setScreenResolution( "1280x1024" ) );
  }

  @Test
  public void testSendsRequestWithAdvancedConfiguration() throws Exception {
    String fakeToken = UUID.randomUUID().toString().replace( "-", "" );
    driver.addExpectation( onRequestTo( "/" ).withParam( "idsite", 2 )
      .withParam( "rec", 1 )
      .withParam( "apiv", "apiVersion" )
      .withParam( "rand", compileUuidPattern() )
      .withParam( "url", "actionUrl" )
      .withParam( "token_auth", fakeToken )
      .withParam( "city", "Karlsruhe" ), giveEmptyResponse().withStatus( 200 ) );

    Piwik piwik = new Piwik( driver.getBaseUrl(), new PiwikConfiguration( "apiVersion", 2 ) );

    piwik.track( new Action( "actionUrl" ),
                 new AdvancedConfiguration( fakeToken ).setCityOverride( "Karlsruhe" ) );
  }

  private Pattern compileUuidPattern() {
    return Pattern.compile( "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}" );
  }

}
