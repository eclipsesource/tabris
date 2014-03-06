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

import java.util.ArrayList;
import java.util.List;

import com.eclipsesource.tabris.passepartout.Condition;
import com.eclipsesource.tabris.passepartout.Instruction;
import com.eclipsesource.tabris.passepartout.Query;
import com.eclipsesource.tabris.passepartout.Rule;


public class QueryImpl implements Query {

  private final List<Condition> conditions;

  public QueryImpl( Condition condition ) {
    whenNull( condition ).throwIllegalArgument( "Condition must not be null" );
    this.conditions = new ArrayList<Condition>();
    this.conditions.add( condition );
  }

  @Override
  public Rule then( Instruction instruction, Instruction... additionalInstructions ) {
    whenNull( instruction ).throwIllegalArgument( "Instruction must not be null" );
    List<Instruction> actualInstructions = new ArrayList<Instruction>();
    actualInstructions.add( instruction );
    for( Instruction actualInstruction : additionalInstructions ) {
      actualInstructions.add( actualInstruction );
    }
    return new RuleImpl( conditions, actualInstructions );
  }

  @Override
  public Query and( Condition condition ) {
    whenNull( condition ).throwIllegalArgument( "Condition must not be null" );
    conditions.add( condition );
    return this;
  }

  public List<Condition> getConditions() {
    return new ArrayList<Condition>( conditions );
  }

}
