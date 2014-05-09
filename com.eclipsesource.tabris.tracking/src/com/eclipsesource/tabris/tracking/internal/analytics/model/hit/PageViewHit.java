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
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.DOCUMENT_PATH;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValueProvider.getRequestValue;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValues.HIT_PAGEVIEW;


@SuppressWarnings("restriction")
public class PageViewHit extends Hit {

  public PageViewHit( String documentPath ) {
    super( getRequestValue( HIT_PAGEVIEW ) );
    whenNull( documentPath ).throwIllegalArgument( "DocumentPath must not be null." );
    when( documentPath.isEmpty() ).throwIllegalArgument( "DocumentPath must not be empty." );
    addParameter( getRequestKey( DOCUMENT_PATH ), documentPath );
  }
}
