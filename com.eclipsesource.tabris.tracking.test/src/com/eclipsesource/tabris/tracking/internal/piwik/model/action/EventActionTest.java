/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.model.action;

import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.EVENT_ACTION;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.EVENT_CATEGORY;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.EVENT_NAME;
import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class EventActionTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCategory() {
    EventAction eventAction = new EventAction( "foo" );

    eventAction.setCategory( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyCategory() {
    EventAction eventAction = new EventAction( "foo" );

    eventAction.setCategory( "" );
  }

  @Test
  public void testAddsCategoryToParameter() throws Exception {
    EventAction eventAction = new EventAction( "foo" ).setCategory( "baz" );

    assertEquals( "baz", eventAction.getParameter().get( getRequestKey( EVENT_CATEGORY ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullAction() {
    EventAction eventAction = new EventAction( "foo" );

    eventAction.setAction( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyAction() {
    EventAction eventAction = new EventAction( "foo" );

    eventAction.setAction( "" );
  }

  @Test
  public void testAddsActionToParameter() throws Exception {
    EventAction eventAction = new EventAction( "foo" ).setAction( "baz" );

    assertEquals( "baz", eventAction.getParameter().get( getRequestKey( EVENT_ACTION ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullName() {
    EventAction eventAction = new EventAction( "foo" );

    eventAction.setEventName( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyName() {
    EventAction eventAction = new EventAction( "foo" );

    eventAction.setEventName( "" );
  }

  @Test
  public void testAddsNameToParameter() throws Exception {
    EventAction eventAction = new EventAction( "foo" ).setEventName( "baz" );

    assertEquals( "baz", eventAction.getParameter().get( getRequestKey( EVENT_NAME ) ) );
  }
}
