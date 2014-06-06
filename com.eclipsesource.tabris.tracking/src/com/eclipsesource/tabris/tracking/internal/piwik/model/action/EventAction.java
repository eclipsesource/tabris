/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.model.action;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.EVENT_ACTION;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.EVENT_CATEGORY;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.EVENT_NAME;


@SuppressWarnings("restriction")
public class EventAction extends Action {

  public EventAction( String actionUrl ) {
    super( actionUrl );
  }

  public EventAction setCategory( String category ) {
    whenNull( category ).throwIllegalArgument( "Category must not be null." );
    when( category.isEmpty() ).throwIllegalArgument( "Category must not be empty." );
    addParameter( getRequestKey( EVENT_CATEGORY ), category );
    return this;
  }

  public EventAction setAction( String action ) {
    whenNull( action ).throwIllegalArgument( "Action must not be null." );
    when( action.isEmpty() ).throwIllegalArgument( "Action must not be empty." );
    addParameter( getRequestKey( EVENT_ACTION ), action );
    return this;
  }

  public EventAction setEventName( String name ) {
    whenNull( name ).throwIllegalArgument( "Name must not be null." );
    when( name.isEmpty() ).throwIllegalArgument( "Name must not be empty." );
    addParameter( getRequestKey( EVENT_NAME ), name );
    return this;
  }
}
