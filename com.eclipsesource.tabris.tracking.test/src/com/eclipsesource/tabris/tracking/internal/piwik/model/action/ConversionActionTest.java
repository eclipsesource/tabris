/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.model.action;

import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ACTION_GOAL_ID;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ACTION_GOAL_REVENUE;
import static org.junit.Assert.*;

import org.junit.Test;

public class ConversionActionTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNegativeGoalId() throws Exception {
    new ConversionAction( "foo", -1 );
  }

  @Test
  public void testAddsGoalIdToParameters() throws Exception {
    ConversionAction action = new ConversionAction( "foo", 5 );

    assertEquals( Integer.valueOf( 5 ), action.getParameters().get( getRequestKey( ACTION_GOAL_ID ) ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNegativeRevenue() {
    new ConversionAction( "foo", 5 ).setRevenue( -1 );
  }
  
  @Test
  public void testAddsRevenueToParameters() {
    ConversionAction action = new ConversionAction( "foo", 5 ).setRevenue( 3 );
    
    assertEquals( Double.valueOf( 3 ), action.getParameters().get( getRequestKey( ACTION_GOAL_REVENUE ) ) );
  }
}
