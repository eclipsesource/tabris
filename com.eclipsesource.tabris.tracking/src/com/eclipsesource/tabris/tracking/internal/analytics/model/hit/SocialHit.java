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
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.SOCIAL_ACTION;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.SOCIAL_ACTION_TARGET;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.SOCIAL_NETWORK;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValueProvider.getRequestValue;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValues.HIT_SOCIAL;


@SuppressWarnings("restriction")
public class SocialHit extends Hit {

  public SocialHit( String network, String action, String actionTarget ) {
    super( getRequestValue( HIT_SOCIAL ) );
    validateArguments( network, action, actionTarget );
    addRequiredParameter( network, action, actionTarget );
  }

  private void addRequiredParameter( String socialNetwork, String socialAction, String socialActionTarget ) {
    addParameter( getRequestKey( SOCIAL_NETWORK ), socialNetwork );
    addParameter( getRequestKey( SOCIAL_ACTION ), socialAction );
    addParameter( getRequestKey( SOCIAL_ACTION_TARGET ), socialActionTarget );
  }

  private void validateArguments( String network, String action, String actionTarget ) {
    whenNull( network ).throwIllegalArgument( "Network must not be null." );
    when( network.isEmpty() ).throwIllegalArgument( "Network must not be empty." );
    whenNull( action ).throwIllegalArgument( "Action must not be null." );
    when( action.isEmpty() ).throwIllegalArgument( "Action must not be empty." );
    whenNull( actionTarget ).throwIllegalArgument( "ActionTarget must not be null." );
    when( actionTarget.isEmpty() ).throwIllegalArgument( "ActionTarget must not be empty." );
  }
}
