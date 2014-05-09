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
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.DOCUMENT_PATH;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.HIT_TYPE;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValueProvider.getRequestValue;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValues.HIT_PAGEVIEW;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PageViewHitTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullParameter() {
    new PageViewHit( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyParameter() {
    new PageViewHit( "" );
  }

  @Test
  public void testSetsPageViewHit() {
    PageViewHit pageViewHit = new PageViewHit( "foo" );

    String hitType = ( String )pageViewHit.getParameter().get( getRequestKey( HIT_TYPE ) );

    assertEquals( getRequestValue( HIT_PAGEVIEW ), hitType );
  }

  @Test
  public void testSetsDocumentPath() {
    PageViewHit pageViewHit = new PageViewHit( "foo" );

    String documentPath = ( String )pageViewHit.getParameter().get( getRequestKey( DOCUMENT_PATH ) );

    assertEquals( "foo", documentPath );
  }
}
