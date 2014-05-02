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
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.CONTENT_DESCRIPTION;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.HIT_TYPE;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValueProvider.getRequestValue;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValues.HIT_SCREENVIEW;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

public class ScreenViewHitTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullScreenName() {
    new ScreenViewHit( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyScreenName() {
    new ScreenViewHit( "" );
  }

  @Test
  public void testSetsScreenViewHit() {
    ScreenViewHit screenViewHit = new ScreenViewHit( "foo" );

    Object hitType = screenViewHit.getParameter().get( getRequestKey( HIT_TYPE ) );

    assertEquals( getRequestValue( HIT_SCREENVIEW ), hitType );
  }

  @Test
  public void testSetsScreenName() {
    ScreenViewHit hit = new ScreenViewHit( "foo" );

    Map<String, Object> parameter = hit.getParameter();

    assertEquals( "foo", parameter.get( getRequestKey( CONTENT_DESCRIPTION ) ) );
  }
}
