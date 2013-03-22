/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.ui;


/**
 * <p>
 * The single purpose of the {@link ActionOperator} is to modify and get the state of {@link Action} objects at runtime.
 * this means you can influence the visibility or the enabled state of an {@link Action}.
 * </p>
 *
 * @since 1.0
 */
public interface ActionOperator {

  /**
   * <p>
   * Sets the enabled state of an {@link Action}.
   * </p>
   *
   * @param id the id of the {@link Action}. Must not be empty or <code>null</code>.
   *
   * @throws IllegalStateException when no {@link Action} for the given id exist.
   */
  void setActionEnabled( String id, boolean enabled ) throws IllegalStateException;

  /**
   * <p>
   * Gets the enabled state of an {@link Action}.
   * </p>
   *
   * @param id the id of the {@link Action}. Must not be empty or <code>null</code>.
   */
  boolean isActionEnabled( String id );

  /**
   * <p>
   * Sets the visibility of an {@link Action}.
   * </p>
   *
   * @param id the id of the {@link Action}. Must not be empty or <code>null</code>.
   *
   * @throws IllegalStateException when no {@link Action} for the given id exist.
   */
  void setActionVisible( String id, boolean visible ) throws IllegalStateException;

  /**
   * <p>
   * Gets the visibility of an {@link Action}.
   * </p>
   *
   * @param id the id of the {@link Action}. Must not be empty or <code>null</code>.
   */
  boolean isActionVisible( String id );

}
