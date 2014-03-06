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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;

import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.PageOperator;
import com.eclipsesource.tabris.ui.UI;


public class TestPage implements Page{

  private UI ui;
  private boolean wasCreated;
  private boolean wasDeactivated;
  private boolean wasActivated;
  private boolean wasDestroyed;
  private Page pageAtDeactivate;
  private final List<String> callStack;

  public TestPage() {
    callStack = new ArrayList<String>();
  }

  @Override
  public void createContent( Composite parent, UI ui ) {
    callStack.add( "create" );
    this.ui = ui;
    wasCreated = true;
    parent.addDisposeListener( new DisposeListener() {

      @Override
      public void widgetDisposed( DisposeEvent event ) {
        callStack.add( "dispose" );
      }
    } );
  }

  @Override
  public void activate() {
    callStack.add( "activate" );
    wasActivated = true;
  }

  @Override
  public void deactivate() {
    callStack.add( "deactivate" );
    PageOperator pageOperator = ui.getPageOperator();
    if( pageOperator != null ) {
      pageAtDeactivate = pageOperator.getCurrentPage();
    }
    wasDeactivated = true;
  }

  public Page getPageAtDeactivate() {
    return pageAtDeactivate;
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

  @Override
  public void destroy() {
    callStack.add( "destroy" );
    wasDestroyed = true;
  }

  public boolean wasDestroyed() {
    return wasDestroyed;
  }

  public List<String> getCallStack() {
    return callStack;
  }

}
