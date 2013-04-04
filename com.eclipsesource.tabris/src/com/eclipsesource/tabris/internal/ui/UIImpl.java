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
package com.eclipsesource.tabris.internal.ui;

import static com.eclipsesource.tabris.internal.Preconditions.checkArgumentNotNull;

import java.io.Serializable;

import org.eclipse.swt.widgets.Display;

import com.eclipsesource.tabris.ui.ActionOperator;
import com.eclipsesource.tabris.ui.PageOperator;
import com.eclipsesource.tabris.ui.UI;
import com.eclipsesource.tabris.ui.UIConfiguration;


public class UIImpl implements UI, Serializable {

  private final Display display;
  private final UIConfiguration configuration;
  private final ActionOperatorImpl actionOperator;
  private final PageOperatorImpl pageOperator;
  private boolean initialized;

  public UIImpl( Display display, Controller controller, UIConfiguration configuration ) {
    checkArgumentNotNull( display, Display.class.getSimpleName() );
    checkArgumentNotNull( controller, Controller.class.getSimpleName() );
    checkArgumentNotNull( configuration, UIConfiguration.class.getSimpleName() );
    this.display = display;
    this.configuration = configuration;
    this.actionOperator = new ActionOperatorImpl( controller );
    this.pageOperator = new PageOperatorImpl( controller, this );
  }

  @Override
  public Display getDisplay() {
    return display;
  }

  @Override
  public PageOperator getPageOperator() {
    if( initialized ) {
      return pageOperator;
    }
    return null;
  }

  @Override
  public ActionOperator getActionOperator() {
    if( initialized ) {
      return actionOperator;
    }
    return null;
  }

  UIConfiguration getConfiguration() {
    return configuration;
  }

  public void markInitialized() {
    initialized = true;
  }
}
