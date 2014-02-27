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

import com.eclipsesource.tabris.passepartout.Instruction;
import com.eclipsesource.tabris.passepartout.Rule;
import com.eclipsesource.tabris.passepartout.UIEnvironment;


public class InstructionExtractor {

  private final QueryVerifier verifier;

  public InstructionExtractor( UIEnvironment environment ) {
    whenNull( environment ).throwIllegalArgument( "Environment must not be null" );
    this.verifier = new QueryVerifier( environment );
  }

  public List<Instruction> extract( List<Rule> rules ) {
    List<Instruction> instructions = new ArrayList<Instruction>();
    for( Rule rule : rules ) {
      if( verifier.complies( rule ) ) {
        instructions.addAll( rule.getInstructions() );
      }
    }
    return instructions;
  }

}
