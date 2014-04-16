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
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.HIT_TYPE;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.SOCIAL_ACTION;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.SOCIAL_ACTION_TARGET;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.SOCIAL_NETWORK;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValueProvider.getRequestValue;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValues.HIT_SOCIAL;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

public class SocialHitTest {

  @Test
  public void testSetsSocialHitType() {
    SocialHit socialHit = new SocialHit( "foo", "bar", "baz" );

    String hitType = ( String )socialHit.getParameter().get( getRequestKey( HIT_TYPE ) );

    assertEquals( getRequestValue( HIT_SOCIAL ), hitType );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullSocialNetowork() {
    new SocialHit( null, "foo", "bar" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptySocialNetowork() {
    new SocialHit( "", "foo", "bar" );
  }

  @Test
  public void testSetsSocialNetwork() {
    SocialHit itemHit = new SocialHit( "foo", "bar", "baz" );

    Map<String, Object> parameter = itemHit.getParameter();

    assertEquals( "foo", parameter.get( getRequestKey( SOCIAL_NETWORK ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullSocialAction() {
    new SocialHit( "foo", null, "bar" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptySocialAction() {
    new SocialHit( "foo", "", "bar" );
  }

  @Test
  public void testSetsSocialAction() {
    SocialHit itemHit = new SocialHit( "foo", "bar", "baz" );

    Map<String, Object> parameter = itemHit.getParameter();

    assertEquals( "bar", parameter.get( getRequestKey( SOCIAL_ACTION ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullSocialActionTarget() {
    new SocialHit( "foo", "bar", null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptySocialActionTarget() {
    new SocialHit( "foo", "bar", "" );
  }

  @Test
  public void testSetsSocialActionTarget() {
    SocialHit itemHit = new SocialHit( "foo", "bar", "baz" );

    Map<String, Object> parameter = itemHit.getParameter();

    assertEquals( "baz", parameter.get( getRequestKey( SOCIAL_ACTION_TARGET ) ) );
  }
}
