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
 * An {@link UIEnvironment} describes the environment of a UI component. E.g. if the component has a parent you can
 * retrieve the parent's bounds. If it has no parent the {@link UIEnvironment} will always use the next higher UI part.
 * So, you don't need to take care of using the right bounds because this is encapsulated in the {@link UIEnvironment}.
 * </p>
 * <p>
 * The {@link UIEnvironment} will be used when you implement your {@link Condition}.
 * </p>
 *
 * @see Condition
 *
 * @noimplement This interface is not intended to be implemented by clients.
 *
 * @since 0.9
 */
public interface UIEnvironment {

  /**
   * <p>
   * Returns the bounds of the parent UI element. If there is no parent it returns the bounds form the next higher
   * element.
   * </p>
   */
  Bounds getParentBounds();

  /**
   * <p>
   * Reference bounds are used when dealing with percentages. To calculate a percentage you always need a base to
   * compute against. These are the reference bounds.
   * </p>
   */
  Bounds getReferenceBounds();

  /**
   * <p>
   * Returns the parent font size. Mostly use to compute an em. If the element has no parent the font size of the
   * next higher element will be used.
   * </p>
   */
  int getParentFontSize();
}
