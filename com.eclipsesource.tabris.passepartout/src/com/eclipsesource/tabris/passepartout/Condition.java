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
 * A {@link Condition} defines a boolean condition that can be valid or invalid. {@link Condition}s are used in
 * {@link Query}s to define {@link Rule}s.
 * </p>
 * <p>
 * By default Passe-Partout comes with a set of implemented {@link Condition}s that can be created using the
 * {@link PassePartout} class. In rare cases you need to implement your own {@link Condition} to define even more
 * detailed queries.
 * </p>
 *
 * @see PassePartout
 *
 * @since 0.9
 */
public interface Condition {

  /**
   * <p>
   * The {@link Condition#compliesWith(UIEnvironment)} method tells the caller if the {@link Condition} that implements
   * the method is valid or not. For this reason a parameter of the type {@link UIEnvironment} will be passed in that
   * you can use for computation.
   * </p>
   *
   * @param environment describes the UI environment. Is never <code>null</code>.
   */
  boolean compliesWith( UIEnvironment environment );

}
