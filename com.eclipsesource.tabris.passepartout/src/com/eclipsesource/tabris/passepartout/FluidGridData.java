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
package com.eclipsesource.tabris.passepartout;

import static com.eclipsesource.tabris.passepartout.PassePartout.columns;
import static com.eclipsesource.tabris.passepartout.PassePartout.when;
import static com.eclipsesource.tabris.passepartout.internal.Clauses.whenNull;

import java.util.ArrayList;
import java.util.List;

import com.eclipsesource.tabris.passepartout.internal.condition.AlwaysTrueContidtion;


public class FluidGridData {

  private final List<Rule> rules;

  public FluidGridData() {
    this.rules = new ArrayList<Rule>();
    addRule( when( new AlwaysTrueContidtion() ).then( columns( 1 ) ) );
  }

  public FluidGridData addRule( Rule rule ) {
    whenNull( rule ).throwIllegalArgument( "Rule must not be null" );
    rules.add( rule );
    return this;
  }

  public List<Rule> getRules() {
    return new ArrayList<Rule>( rules );
  }
}
