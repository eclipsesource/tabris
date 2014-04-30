/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.request;

import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ACTION_URL;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.API_VERSION;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.RANDOM;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.REC;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.SITE_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.eclipsesource.tabris.tracking.internal.piwik.model.PiwikConfiguration;
import com.eclipsesource.tabris.tracking.internal.piwik.model.Requestable;
import com.eclipsesource.tabris.tracking.internal.piwik.model.action.Action;

public class RequestAssemblerTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullConfiguration() {
    new RequestAssembler( null, new Action( "foo" ), new TestRequestable( "baz", "bar" ) );

  }

  @Test
  public void testAddsParametersFromConfiguration() {
    RequestAssembler assembler = new RequestAssembler( new PiwikConfiguration( "apiVersion", 2 ),
                                                       new Action( "actionUrl" ),
                                                       new TestRequestable( "baz", "bac" ) );

    assertEquals( "apiVersion", assembler.assemble().get( getRequestKey( API_VERSION ) ) );
    assertEquals( Integer.valueOf( 2 ), assembler.assemble().get( getRequestKey( SITE_ID ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullAction() {
    new RequestAssembler( new PiwikConfiguration( "apiVersion", 2 ),
                          null,
                          new TestRequestable( "foo", "bar" ) );
  }

  @Test
  public void testSetsActionAsParameter() {
    RequestAssembler assembler = new RequestAssembler( new PiwikConfiguration( "apiVersion", 2 ),
                                                       new Action( "actionUrl" ),
                                                       new TestRequestable( "foo", "bar" ) );

    assertEquals( "actionUrl", assembler.assemble().get( getRequestKey( ACTION_URL ) ) );
  }

  @Test
  public void testSetsUuidForRandAsParameter() {
    RequestAssembler assembler = new RequestAssembler( new PiwikConfiguration( "apiVersion", 2 ),
                                                       new Action( "actionUrl" ),
                                                       new TestRequestable( "foo", "bar" ) );

    String clientId = ( String )assembler.assemble().get( getRequestKey( RANDOM ) );

    assertTrue( clientId.length() == 36 );
  }

  @Test
  public void testSetsRecAsParameter() {
    RequestAssembler assembler = new RequestAssembler( new PiwikConfiguration( "apiVersion", 2 ),
                                                       new Action( "actionUrl" ),
                                                       new TestRequestable( "foo", "bar" ) );

    assertEquals( Integer.valueOf( 1 ), assembler.assemble().get( getRequestKey( REC ) ) );
  }

  @Test
  public void testAddsParameterFromRequestables() {
    TestRequestable requestable1 = new TestRequestable( "foo", "bar" );
    TestRequestable requestable2 = new TestRequestable( "foo2", "bar2" );
    RequestAssembler assembler = new RequestAssembler( new PiwikConfiguration( "apiVersion", 2 ),
                                                       new Action( "actionUrl" ),
                                                       requestable1,
                                                       requestable2 );

    Map<String, Object> parameter = assembler.assemble();

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
    public Map<String, Object> getParameters() {
      return parameter;
    }

  }
}
