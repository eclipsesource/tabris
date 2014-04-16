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
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.EVENT_ACTION;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.EVENT_CATEGORY;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.EVENT_LABEL;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.EVENT_VALUE;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.HIT_TYPE;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValueProvider.getRequestValue;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValues.HIT_EVENT;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EventHitTest {

  @Test
  public void testSetsEventHit() {
    EventHit eventHit = new EventHit();

    String hitType = ( String )eventHit.getParameter().get( getRequestKey( HIT_TYPE ) );

    assertEquals( getRequestValue( HIT_EVENT ), hitType );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullEventCategory() {
    new EventHit().setCategory( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyEventCategory() {
    new EventHit().setCategory( "" );
  }

  @Test
  public void testSetsEventCategory() {
    EventHit eventHit = new EventHit().setCategory( "foo" );

    String eventCategory = ( String )eventHit.getParameter().get( getRequestKey( EVENT_CATEGORY ) );

    assertEquals( "foo", eventCategory );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullEventAction() {
    new EventHit().setAction( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyEventAction() {
    new EventHit().setAction( "" );
  }

  @Test
  public void testSetsEventAction() {
    EventHit eventHit = new EventHit().setAction( "foo" );

    String eventCategory = ( String )eventHit.getParameter().get( getRequestKey( EVENT_ACTION ) );

    assertEquals( "foo", eventCategory );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullEventLabel() {
    new EventHit().setLabel( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyEventLabel() {
    new EventHit().setLabel( "" );
  }

  @Test
  public void testSetsEventLabel() {
    EventHit eventHit = new EventHit().setLabel( "foo" );

    String eventCategory = ( String )eventHit.getParameter().get( getRequestKey( EVENT_LABEL ) );

    assertEquals( "foo", eventCategory );
  }

  @Test
  public void testSetsEventValue() {
    EventHit eventHit = new EventHit().setValue( 2 );

    Integer eventValue = ( Integer )eventHit.getParameter().get( getRequestKey( EVENT_VALUE ) );

    assertEquals( Integer.valueOf( 2 ), eventValue );
  }

}
