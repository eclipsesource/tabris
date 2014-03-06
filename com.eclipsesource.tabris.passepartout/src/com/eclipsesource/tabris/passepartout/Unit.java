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

import java.math.BigDecimal;


/**
 * <p>
 * When using CSS you have a whole bunch of units to define borders and sizes. These are pixel, percentages, ems and
 * many more. In Java we only have primitives that can't be used for every unit. Thus the {@link Unit} type is an
 * abstraction for units that will be used within Passe-Partout.
 * </p>
 * <p>
 * By default Passe-Partout knows 3 units. These are pixel, percentage and the em. These units can be created using
 * the <code>px</code>, <code>em</code> and <code>percentage</code> methods in the {@link PassePartout} class.
 * </p>
 *
 * @noimplement This interface is not intended to be implemented by clients.
 *
 * @since 0.9
 */
public interface Unit {

  /**
   * <p>
   * Returns the value of a {@link Unit} as a {@link BigDecimal}. It's up to the caller to interpret this value
   * correctly.
   * </p>
   */
  BigDecimal getValue();

}
