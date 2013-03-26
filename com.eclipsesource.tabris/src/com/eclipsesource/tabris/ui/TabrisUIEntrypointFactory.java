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

import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.rap.rwt.application.EntryPointFactory;

import com.eclipsesource.tabris.internal.Preconditions;


/**
 * @since 1.0
 */
public class TabrisUIEntrypointFactory implements EntryPointFactory {

  private final UIConfiguration configuration;

  public TabrisUIEntrypointFactory( UIConfiguration configuration ) {
    Preconditions.checkArgumentNotNull( configuration, UIConfiguration.class.getName() );
    this.configuration = configuration;
  }

  @Override
  public EntryPoint create() {
    return new TabrisUIEntryPoint( configuration );
  }
}
