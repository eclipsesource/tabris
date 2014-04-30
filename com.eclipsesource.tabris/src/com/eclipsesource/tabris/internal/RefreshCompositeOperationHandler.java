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
package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.internal.Constants.EVENT_REFRESH;

import java.util.List;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.internal.lifecycle.ProcessActionRunner;
import org.eclipse.swt.internal.widgets.compositekit.CompositeOperationHandler;
import org.eclipse.swt.widgets.Composite;

import com.eclipsesource.tabris.widgets.RefreshComposite;
import com.eclipsesource.tabris.widgets.RefreshListener;


@SuppressWarnings("restriction")
public class RefreshCompositeOperationHandler extends CompositeOperationHandler {

  public RefreshCompositeOperationHandler( RefreshComposite control ) {
    super( control );
  }

  @Override
  public void handleNotify( Composite control, String eventName, JsonObject properties ) {
    if( EVENT_REFRESH.equals( eventName ) ) {
      notifyRefreshListeners( ( RefreshComposite )control );
    } else {
      super.handleNotify( control, eventName, properties );
    }
  }

  private void notifyRefreshListeners( final RefreshComposite composite ) {
    ProcessActionRunner.add( new Runnable() {

      @Override
      public void run() {
        List<RefreshListener> refreshListeners = composite.getRefreshListeners();
        for( RefreshListener refreshListener : refreshListeners ) {
          refreshListener.refresh();
        }
      }
    } );
  }

}