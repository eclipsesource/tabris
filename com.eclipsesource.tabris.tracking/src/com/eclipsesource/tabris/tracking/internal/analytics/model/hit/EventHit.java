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

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.EVENT_ACTION;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.EVENT_CATEGORY;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.EVENT_LABEL;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.EVENT_VALUE;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValueProvider.getRequestValue;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValues.HIT_EVENT;


@SuppressWarnings("restriction")
public class EventHit extends Hit {

  public EventHit() {
    super( getRequestValue( HIT_EVENT ) );
  }

  public EventHit setCategory( String category ) {
    whenNull( category ).throwIllegalArgument( "Category must not be null!" );
    when( category.isEmpty() ).throwIllegalArgument( "Category must not be empty!" );
    addParameter( getRequestKey( EVENT_CATEGORY ), category );
    return this;
  }

  public EventHit setAction( String action ) {
    whenNull( action ).throwIllegalArgument( "Action must not be null!" );
    when( action.isEmpty() ).throwIllegalArgument( "Action must not be empty!" );
    addParameter( getRequestKey( EVENT_ACTION ), action );
    return this;
  }

  public EventHit setLabel( String label ) {
    whenNull( label ).throwIllegalArgument( "Label must not be null!" );
    when( label.isEmpty() ).throwIllegalArgument( "Label must not be empty!" );
    addParameter( getRequestKey( EVENT_LABEL ), label );
    return this;
  }

  public EventHit setValue( int value ) {
    addParameter( getRequestKey( EVENT_VALUE ), Integer.valueOf( value ) );
    return this;
  }

}
