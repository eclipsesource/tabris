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

import java.util.List;


/**
 * <p>
 * A {@link Rule} can be compared to a CSS media query and it's content. In CSS media queries you define a condition and
 * a block that will be active when this query becomes active. With a {@link Rule} it's the same. You define
 * {@link Condition}s and instead of a block you will define {@link Instruction}s.
 * </p>
 * <p>
 * A {@link Rule} is used everywhere in Passe-Partout. You work with them when using {@link FluidGridLayout},
 * {@link QueryListener} or responsive {@link Resource}s.
 * </p>
 *
 * @see Condition
 * @see Instruction
 *
 * @noimplement This interface is not intended to be implemented by clients.
 *
 * @since 0.9
 */
public interface Rule {

  /**
   * <p>
   * Returns the list of {@link Condition}s that are defined in this {@link Rule}.
   * </p>
   */
  List<Condition> getConditions();

  /**
   * <p>
   * Returns the list of {@link Instruction}s that are defined in this {@link Rule}.
   * </p>
   */
  List<Instruction> getInstructions();

}
