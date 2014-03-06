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


/**
 * <p>
 * A {@link Query} is used to group {@link Condition}s and produce {@link Rule}s. It's main purpose is to provide a
 * fluent API to enable you to create rules in one line e.g.
 *   <pre>
 *   PasseParout.when( condition1 ).and( condition2 ).then( instruction1, instruction2 );
 *   </pre>
 *</p>
 * <p>
 * To create a {@link Query} you need to use the {@link PassePartout#when(Condition)} method.
 * </p>
 *
 * @see Condition
 * @see Instruction
 * @see Rule
 *
 * @noimplement This interface is not intended to be implemented by clients.
 *
 * @since 0.9
 */
public interface Query {

  /**
   * <p>
   * Adds a {@link Condition} to this query. You can use <code>and</code calls to create complex {@link Query}s.
   * </p>
   *
   * @param condition the {@link Condition} to add. Must not be <code>null</code>.
   */
  Query and( Condition condition );

  /**
   * <p>
   * Produces a {@link Rule} out of the add {@link Condition} and the defined {@link Instruction}s. A {@link Rule}
   * needs one instruction minimum but can hold as many {@link Instruction}s you want.
   * </p>
   *
   * @param intstruction the {@link Instruction} to use in the {@link Rule}.
   * @param additionalInstructions optional {@link Instruction}s to add also to the {@link Rule}.
   */
  Rule then( Instruction intstruction, Instruction... additionalInstructions );

}
