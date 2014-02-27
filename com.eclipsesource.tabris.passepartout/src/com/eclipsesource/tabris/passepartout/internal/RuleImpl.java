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
import com.eclipsesource.tabris.passepartout.Rule;


public class RuleImpl implements Rule {

  private final List<Instruction> instructions;
  private final List<Condition> conditions;

  public RuleImpl( List<Condition> conditions, List<Instruction> instructions ) {
    whenNull( conditions ).throwIllegalArgument( "Conditions must not be null" );
    whenNull( instructions ).throwIllegalArgument( "Instruction must not be null" );
    this.instructions = new ArrayList<Instruction>( instructions );
    this.conditions = new ArrayList<Condition>( conditions );
  }

  @Override
  public List<Condition> getConditions() {
    return new ArrayList<Condition>( conditions );
  }

  @Override
  public List<Instruction> getInstructions() {
    return new ArrayList<Instruction>( instructions );
  }

}
