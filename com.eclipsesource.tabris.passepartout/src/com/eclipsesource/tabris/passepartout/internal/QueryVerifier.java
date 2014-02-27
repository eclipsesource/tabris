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

import java.util.List;

import com.eclipsesource.tabris.passepartout.Condition;
import com.eclipsesource.tabris.passepartout.Query;
import com.eclipsesource.tabris.passepartout.Rule;
import com.eclipsesource.tabris.passepartout.UIEnvironment;


public class QueryVerifier {

  private final UIEnvironment environment;

  public QueryVerifier( UIEnvironment environment ) {
    whenNull( environment ).throwIllegalArgument( "UIEnvironment must not be null" );
    this.environment = environment;
  }

  public boolean complies( Query query ) {
    whenNull( query ).throwIllegalArgument( "Query must not be null" );
    List<Condition> conditions = ( ( QueryImpl )query ).getConditions();
    return compliesWith( conditions );
  }

  public boolean complies( Rule rule ) {
    whenNull( rule ).throwIllegalArgument( "Rule must not be null" );
    return compliesWith( rule.getConditions() );
  }

  private boolean compliesWith( List<Condition> conditions ) {
    boolean complies = true;
    for( Condition condition : conditions ) {
      complies &= condition.compliesWith( environment );
    }
    return complies;
  }
}
