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

import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import java.io.Serializable;

import org.eclipse.swt.widgets.Display;

import com.eclipsesource.tabris.ui.Action;
import com.eclipsesource.tabris.ui.ActionConfiguration;
import com.eclipsesource.tabris.ui.ActionOperator;
import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.PageConfiguration;
import com.eclipsesource.tabris.ui.PageOperator;
import com.eclipsesource.tabris.ui.UI;
import com.eclipsesource.tabris.ui.UIConfiguration;


public class UIImpl implements UI, Serializable {

  private final Display display;
  private final Controller controller;
  private final UIConfiguration configuration;
  private final ActionOperatorImpl actionOperator;
  private final PageOperatorImpl pageOperator;
  private boolean initialized;

  public UIImpl( Display display, Controller controller, UIConfiguration configuration ) {
    this.controller = controller;
    whenNull( display ).throwIllegalArgument( "Display must not be null" );
    whenNull( controller ).throwIllegalArgument( "Controller must not be null" );
    whenNull( configuration ).throwIllegalArgument( "Configuration must not be null" );
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

  @Override
  public UIConfiguration getConfiguration() {
    return configuration;
  }

  @Override
  public PageConfiguration getPageConfiguration( Page page ) {
    whenNull( page ).throwIllegalArgument( "Page must not be null" );
    return controller.getPageConfiguration( page );
  }

  @Override
  public ActionConfiguration getActionConfiguration( Action action ) {
    whenNull( action ).throwIllegalArgument( "Action must not be null" );
    return controller.getActionConfiguration( action );
  }

  public void markInitialized() {
    initialized = true;
  }
}
