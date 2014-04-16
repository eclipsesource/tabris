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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;

import com.eclipsesource.tabris.tracking.internal.analytics.model.hit.Hit;
import com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys;
import com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeyProvider;


public class HitTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullType() {
    new Hit( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyType() {
    new Hit( "" );
  }

  @Test
  public void testParameterAreNotNull() {
    Hit hit = new Hit( "foo" );

    Map<String, Object> parameter = hit.getParameter();

    assertNotNull( parameter );
  }

  @Test
  public void testAddsTypeToParameter() {
    Hit hit = new Hit( "foo" );

    Map<String, Object> parameter = hit.getParameter();

    Object type = parameter.get( RequestKeyProvider.getRequestKey( RequestKeys.HIT_TYPE ) );
    assertEquals( "foo", type );
  }
}
