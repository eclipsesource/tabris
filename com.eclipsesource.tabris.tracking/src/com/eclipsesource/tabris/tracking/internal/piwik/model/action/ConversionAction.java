/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.model.action;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ACTION_GOAL_ID;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ACTION_GOAL_REVENUE;

@SuppressWarnings("restriction")
public class ConversionAction extends Action {

  public ConversionAction( String actionUrl, int goalId ) {
    super( actionUrl );
    when( goalId < 1 ).throwIllegalArgument( "GoalId must be > 0, but was " + goalId );
    addParameter( getRequestKey( ACTION_GOAL_ID ), Integer.valueOf( goalId ) );
  }

  public ConversionAction setRevenue( double revenue ) {
    when( revenue < 1 ).throwIllegalArgument( "Revenue must be > 0, but was " + revenue );
    addParameter( getRequestKey( ACTION_GOAL_REVENUE ), Double.valueOf( revenue ) );
    return this;
  }
}
