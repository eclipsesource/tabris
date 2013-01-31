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

import com.eclipsesource.tabris.ui.Action;
import com.eclipsesource.tabris.ui.UIContext;


public class TestAction implements Action {

  private boolean wasExecuted;

  @Override
  public void execute( UIContext context ) {
    wasExecuted = true;
  }

  public boolean wasExecuted() {
    return wasExecuted;
  }
}
