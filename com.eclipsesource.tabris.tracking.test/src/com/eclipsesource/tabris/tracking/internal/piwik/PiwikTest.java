/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik;

import static com.eclipsesource.tabris.tracking.internal.piwik.model.AdvancedConfigurationTest.FAKE_TOKEN;
import static com.github.restdriver.clientdriver.RestClientDriver.giveEmptyResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;

import java.util.regex.Pattern;

import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.tracking.internal.piwik.model.AdvancedConfiguration;
import com.eclipsesource.tabris.tracking.internal.piwik.model.PiwikConfiguration;
import com.eclipsesource.tabris.tracking.internal.piwik.model.VisitorInformation;
import com.eclipsesource.tabris.tracking.internal.piwik.model.action.Action;
import com.github.restdriver.clientdriver.ClientDriverRule;

public class PiwikTest {

  private static final Pattern RANDOM = Pattern.compile( "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}" );

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

  @Test
  public void testSendsRequestWithVisitorInformation() throws Exception {
    driver.addExpectation( onRequestTo( "/" ).withParam( "idsite", 2 )
      .withParam( "rec", 1 )
      .withParam( "apiv", "apiVersion" )
      .withParam( "rand", RANDOM )
      .withParam( "url", "actionUrl" )
      .withParam( "token_auth", FAKE_TOKEN )
      .withParam( "res", "1280x1024" ), giveEmptyResponse().withStatus( 200 ) );

    Piwik piwik = new Piwik( driver.getBaseUrl(), new PiwikConfiguration( "apiVersion", 2 ) );

    piwik.track( new Action( "actionUrl" ),
                 new VisitorInformation().setScreenResolution( "1280x1024" ),
                 new AdvancedConfiguration( FAKE_TOKEN ) );
  }

}
