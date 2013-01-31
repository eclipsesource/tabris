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

import org.eclipse.swt.widgets.Composite;

import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.UIContext;


public class TestPage implements Page{

  private boolean wasCreated;
  private boolean wasDeactivated;
  private boolean wasActivated;

  @Override
  public void create( Composite parent, UIContext context ) {
    wasCreated = true;
  }

  @Override
  public void activate( UIContext context ) {
    wasActivated = true;
  }

  @Override
  public void deactivate( UIContext context ) {
    wasDeactivated = true;
  }

  public boolean wasCreated() {
    return wasCreated;
  }

  public boolean wasDeactivated() {
    return wasDeactivated;
  }

  public boolean wasActivated() {
    return wasActivated;
  }

}
