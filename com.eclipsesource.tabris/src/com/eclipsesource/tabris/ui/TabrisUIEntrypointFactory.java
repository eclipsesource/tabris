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

import static com.eclipsesource.tabris.internal.Preconditions.checkArgumentNotNull;

import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.rap.rwt.application.EntryPointFactory;


/**
 * <p>
 * A {@link TabrisUIEntryPoint} does not have a <strong>no</string> no-argument constructor. Therefore it needs to be
 * added to an {@link ApplicationConfiguration} with an {@link EntryPointFactory}. You don't need to implement this
 * factory yourself. The {@link TabrisUIEntrypointFactory} can be used to accomplish this task.
 * </p>
 *
 * @see TabrisUIEntryPoint
 * @see ApplicationConfiguration
 *
 * @since 1.0
 */
public class TabrisUIEntrypointFactory implements EntryPointFactory {

  private final UIConfiguration configuration;

  /**
   * <p>
   * Creates an instance of {@link TabrisUIEntrypointFactory} with the given configuration.
   * </p>
   *
   * @param configuration the configuration for the UI. Must not be <code>null</code>.
   */
  public TabrisUIEntrypointFactory( UIConfiguration configuration ) {
    checkArgumentNotNull( configuration, UIConfiguration.class.getName() );
    this.configuration = configuration;
  }

  @Override
  public EntryPoint create() {
    return new TabrisUIEntryPoint( configuration );
  }
}
