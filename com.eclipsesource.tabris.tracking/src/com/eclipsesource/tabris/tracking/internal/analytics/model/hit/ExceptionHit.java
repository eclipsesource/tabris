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
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.EXCEPTION_DESCRIPTION;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.EXCEPTION_FATAL;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValueProvider.getRequestValue;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValues.HIT_EXCEPTION;


@SuppressWarnings("restriction")
public class ExceptionHit extends Hit {

  public ExceptionHit( String description ) {
    super( getRequestValue( HIT_EXCEPTION ) );
    whenNull( description ).throwIllegalArgument( "Description must not be null." );
    when( description.isEmpty() ).throwIllegalArgument( "Description must not be empty." );
    addParameter( getRequestKey( EXCEPTION_DESCRIPTION ), description );
  }

  public ExceptionHit setFatal( boolean fatal ) {
    addParameter( getRequestKey( EXCEPTION_FATAL ), Boolean.valueOf( fatal ) );
    return this;
  }

}
