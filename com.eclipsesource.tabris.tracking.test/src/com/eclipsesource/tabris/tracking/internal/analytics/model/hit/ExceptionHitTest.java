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
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.EXCEPTION_DESCRIPTION;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.EXCEPTION_FATAL;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.HIT_TYPE;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValueProvider.getRequestValue;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValues.HIT_EXCEPTION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

public class ExceptionHitTest {

  @Test
  public void testSetsExceptionHit() {
    ExceptionHit exceptionHit = new ExceptionHit( "foo" );

    String hitType = ( String )exceptionHit.getParameter().get( getRequestKey( HIT_TYPE ) );

    assertEquals( getRequestValue( HIT_EXCEPTION ), hitType );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullExceptionDescription() {
    new ExceptionHit( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyExceptionDescription() {
    new ExceptionHit( "" );
  }

  @Test
  public void testSavesExceptionDescription() {
    ExceptionHit exceptionHit = new ExceptionHit( "foo" );

    Map<String, Object> parameter = exceptionHit.getParameter();

    assertEquals( "foo", parameter.get( getRequestKey( EXCEPTION_DESCRIPTION ) ) );
  }

  @Test
  public void testSavesExceptionFatal() {
    ExceptionHit exceptionHit = new ExceptionHit( "foo" ).setFatal( true );

    Map<String, Object> parameter = exceptionHit.getParameter();

    assertTrue( ( ( Boolean )parameter.get( getRequestKey( EXCEPTION_FATAL ) ) ).booleanValue() );
  }

}
