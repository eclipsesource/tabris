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
package com.eclipsesource.tabris.passepartout.internal;

import static com.eclipsesource.tabris.passepartout.internal.Clauses.whenNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.eclipsesource.tabris.passepartout.Query;
import com.eclipsesource.tabris.passepartout.QueryListener;
import com.eclipsesource.tabris.passepartout.UIEnvironment;


public class QueryNotifier {

  private final Map<Query, QueryListener> queryListeners;
  private final Map<Query, QueryListener> activeQueryListeners;

  public QueryNotifier() {
    queryListeners = new HashMap<Query, QueryListener>();
    activeQueryListeners = new HashMap<Query, QueryListener>();
  }

  public void addQueryListener( Query query, QueryListener listener ) {
    whenNull( query ).throwIllegalArgument( "Query must not be null" );
    whenNull( listener ).throwIllegalArgument( "QueryListener must not be null" );
    queryListeners.put( query, listener );
  }

  public void removeQueryListener( Query query ) {
    whenNull( query ).throwIllegalArgument( "Query must not be null" );
    queryListeners.remove( query );
    activeQueryListeners.remove( query );
  }

  public void notifyListeners( UIEnvironment environment ) {
    whenNull( environment ).throwIllegalArgument( "UIEnvironment must not be null" );
    QueryVerifier verifier = new QueryVerifier( environment );
    for( Entry<Query, QueryListener> entry : queryListeners.entrySet() ) {
      Query query = entry.getKey();
      sortOutInvalidQueries( verifier );
      if( verifier.complies( query ) && !activeQueryListeners.containsKey( query ) ) {
        activeQueryListeners.put( query, entry.getValue() );
        entry.getValue().activated( query );
      }
    }
  }

  private void sortOutInvalidQueries( QueryVerifier verifier ) {
    Map<Query, QueryListener> oldListeners = new HashMap<Query, QueryListener>( activeQueryListeners );
    for( Entry<Query, QueryListener> entry : oldListeners.entrySet() ) {
      if( !verifier.complies( entry.getKey() ) ) {
        activeQueryListeners.remove( entry.getKey() );
        entry.getValue().deactivated( entry.getKey() );
      }
    }
  }
}
