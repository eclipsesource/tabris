/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.model.action;

import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class ActionTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullActionUrl() {
    new Action( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyActionUrl() {
    new Action( "" );
  }

  @Test
  public void testAddsActionUrlToParameters() {
    Action action = new Action( "foo" );

    assertEquals( "foo", action.getParameters().get( getRequestKey( ACTION_URL ) ) );
  }

  @Test
  public void testParametersAreNotNull() {
    Action action = new Action( "foo" );

    Map<String, Object> parameters = action.getParameters();

    assertNotNull( parameters );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullName() {
    new Action( "foo" ).setName( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyName() {
    new Action( "foo" ).setName( "" );
  }

  @Test
  public void testAddsNameToParameters() {
    Action action = new Action( "foo" );

    action.setName( "foo" );

    assertEquals( "foo", action.getParameters().get( getRequestKey( ACTION_NAME ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCustomVariables() {
    new Action( "foo" ).setCustomVariables( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyCustomVariables() {
    new Action( "foo" ).setCustomVariables( "" );
  }

  @Test
  public void testAddsCustomVariablesToParameters() {
    Action action = new Action( "foo" );

    action.setCustomVariables( "foo" );

    assertEquals( "foo", action.getParameters().get( getRequestKey( ACTION_CUSTOM_VARIABLES ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNegativeGenerationTime() throws Exception {
    new Action( "foo" ).setGenerationTime( -1 );
  }

  @Test
  public void testAddsGenerationTimeToParameters() {
    Action action = new Action( "foo" );

    action.setGenerationTime( 5 );

    assertEquals( Integer.valueOf( 5 ),
                  action.getParameters().get( getRequestKey( ACTION_GENERATION_TIME ) ) );
  }
}
