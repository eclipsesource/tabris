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

import org.eclipse.swt.widgets.Display;

import com.eclipsesource.tabris.Store;
import com.eclipsesource.tabris.ui.ActionManager;
import com.eclipsesource.tabris.ui.PageManager;
import com.eclipsesource.tabris.ui.UIContext;


public class UIContextImpl implements UIContext {

  private final Display display;
  private final UIImpl ui;
  private final Store store;
  private final ActionManagerImpl actionManager;
  private final PageManagerImpl pageManager;
  private boolean initialized;

  public UIContextImpl( Display display, Controller controller, UIImpl ui ) {
    checkArgumentNotNull( display, Display.class.getSimpleName() );
    checkArgumentNotNull( controller, Controller.class.getSimpleName() );
    checkArgumentNotNull( ui, UIImpl.class.getSimpleName() );
    this.display = display;
    this.ui = ui;
    this.store = new Store();
    this.actionManager = new ActionManagerImpl( controller );
    this.pageManager = new PageManagerImpl( controller, this );
  }

  @Override
  public Display getDisplay() {
    return display;
  }

  @Override
  public PageManager getPageManager() {
    if( initialized ) {
      return pageManager;
    }
    return null;
  }

  @Override
  public ActionManager getActionManager() {
    if( initialized ) {
      return actionManager;
    }
    return null;
  }

  @Override
  public Store getGlobalStore() {
    return store;
  }

  UIImpl getUI() {
    return ui;
  }

  public void markInitialized() {
    initialized = true;
  }
}
