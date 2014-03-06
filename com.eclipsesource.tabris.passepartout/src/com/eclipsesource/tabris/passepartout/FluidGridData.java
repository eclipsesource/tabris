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


/**
 * <p>
 * The {@link FluidGridData} is the layout data type you need to use for the {@link FluidGridLayout}.
 * A {@link FluidGridData} object is basically a set of {@link Rule}s. This set defines how your control should be
 * layouted. Within a {@link Rule} you need to define the {@link Condition}s and the {@link Instruction}s to use.
 * </p>
 * <p>
 * If such {@link Condition}s will become valid the {@link FluidGridLayout} grabs the defined {@link Instruction}s out
 * of the {@link FluidGridData} and applies them.
 * <p>
 *
 * @see FluidGridLayout
 * @see Rule
 * @see Condition
 * @see Instruction
 *
 * @since 0.9
 */
public class FluidGridData {

  private final List<Rule> rules;

  public FluidGridData() {
    this.rules = new ArrayList<Rule>();
    addRule( when( new AlwaysTrueContidtion() ).then( columns( 1 ) ) );
  }

  /**
   * <p>
   * Adds a {@link Rule}.
   * </p>
   *
   * @param rule the {@link Rule} to add. Must not be <code>null</code>.
   */
  public FluidGridData addRule( Rule rule ) {
    whenNull( rule ).throwIllegalArgument( "Rule must not be null" );
    rules.add( rule );
    return this;
  }

  /**
   * <p>
   * Returns all {@link Rule}s defined in this {@link FluidGridData}.
   * </p>
   */
  public List<Rule> getRules() {
    return new ArrayList<Rule>( rules );
  }
}
