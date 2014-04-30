/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.model;

import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.API_VERSION;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.SITE_ID;
import static org.junit.Assert.*;

import org.junit.Test;

public class PiwikConfigurationTest {

  @Test
  public void testParametersAreNotNull() {
    assertNotNull( new PiwikConfiguration( "foo", 2 ).getParameters() );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailsWithNullVersion() {
    new PiwikConfiguration( null, 2 );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailsWithEmptyVersion() {
    new PiwikConfiguration( "", 2 );
  }

  @Test
  public void testAddsApiVersionToParameters() throws Exception {
    PiwikConfiguration configuration = new PiwikConfiguration( "foo", 2 );
    assertEquals( "foo", configuration.getParameters().get( getRequestKey( API_VERSION ) ) );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailsWithNegativeSiteId() throws Exception {
    new PiwikConfiguration( "foo", -1 );
  }

  @Test
  public void testAddsSiteIdToParameters() throws Exception {
    PiwikConfiguration configuration = new PiwikConfiguration( "foo", 2 );
    assertEquals( Integer.valueOf( 2 ), configuration.getParameters().get( getRequestKey( SITE_ID ) ) );
  }
}
