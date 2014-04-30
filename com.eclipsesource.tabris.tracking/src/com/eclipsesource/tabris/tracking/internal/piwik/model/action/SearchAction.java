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
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ACTION_SEARCH;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ACTION_SEARCH_CATEGORY;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ACTION_SEARCH_COUNT;


@SuppressWarnings("restriction")
public class SearchAction extends Action {

  public SearchAction( String actionUrl, String searchTerms ) {
    super( actionUrl );
    whenNull( searchTerms ).throwIllegalArgument( "SearchTerms must not be null." );
    when( searchTerms.isEmpty() ).throwIllegalArgument( "SearchTerms must not be empty." );
    addParameter( getRequestKey( ACTION_SEARCH ), searchTerms );
  }

  public SearchAction setCategory( String category ) {
    whenNull( category ).throwIllegalArgument( "Category must not be null." );
    when( category.isEmpty() ).throwIllegalArgument( "Category must not be empty." );
    addParameter( getRequestKey( ACTION_SEARCH_CATEGORY ), category );
    return this;
  }

  public SearchAction setCount( String count ) {
    whenNull( count ).throwIllegalArgument( "Count must not be null." );
    when( count.isEmpty() ).throwIllegalArgument( "Count must not be empty." );
    addParameter( getRequestKey( ACTION_SEARCH_COUNT ), count );
    return this;
  }
}
