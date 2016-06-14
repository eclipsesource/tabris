/*******************************************************************************
 * Copyright (c) 2016 EclipseSource and others.
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
import static com.eclipsesource.tabris.internal.Constants.METHOD_DONE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_MESSAGE;

import org.eclipse.rap.rwt.remote.RemoteObject;

import com.eclipsesource.tabris.widgets.RefreshComposite;


public class RefreshCompositeRemoteAdapter extends TabrisRemoteAdapter<RefreshComposite> {

  private static final int MESSAGE = 1;
  private static final int REFRESH_LISTENER = 2;

  private final RemoteObject remoteObject;
  private final Runnable renderRunnable;
  private transient String message;
  private transient boolean refreshListener;
  private transient boolean done;

  public RefreshCompositeRemoteAdapter( RefreshComposite composite, RemoteObject remoteObject ) {
    super( composite );
    this.remoteObject = remoteObject;
    renderRunnable = createRenderRunnable();
  }

  private Runnable createRenderRunnable() {
    return new Runnable() {
      @Override
      public void run() {
        renderMessage();
        renderRefreshListener();
        renderDone();
        clear();
      }
    };
  }

  public void preserveMessage( String message ) {
    if( !hasPreserved( MESSAGE ) ) {
      markPreserved( MESSAGE );
      this.message = message;
      scheduleRender( renderRunnable );
    }
  }

  private void renderMessage() {
    if( hasPreserved( MESSAGE ) ) {
      String actual = control.getMessage();
      if( changed( actual, message ) ) {
        remoteObject.set( PROPERTY_MESSAGE, actual );
      }
    }
  }

  public void preserveRefreshListener( boolean refreshListener ) {
    if( !hasPreserved( REFRESH_LISTENER ) ) {
      markPreserved( REFRESH_LISTENER );
      this.refreshListener = refreshListener;
      scheduleRender( renderRunnable );
    }
  }

  private void renderRefreshListener() {
    if( hasPreserved( REFRESH_LISTENER ) ) {
      boolean actual = !control.getRefreshListeners().isEmpty();
      if( changed( actual, refreshListener )) {
        remoteObject.listen( EVENT_REFRESH, actual );
      }
    }
  }

  public void setDone( boolean reset ) {
    this.done = reset;
    scheduleRender( renderRunnable );
  }

  private void renderDone() {
    if( done ) {
      remoteObject.call( METHOD_DONE, null );
    }
  }

  @Override
  protected void clear() {
    super.clear();
    done = false;
  }

}
