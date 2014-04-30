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
package com.eclipsesource.tabris.internal;

import java.io.IOException;

import org.eclipse.rap.rwt.internal.lifecycle.AbstractWidgetLCA;
import org.eclipse.rap.rwt.internal.protocol.RemoteObjectFactory;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.internal.widgets.canvaskit.CanvasLCA;
import org.eclipse.swt.widgets.Widget;

import com.eclipsesource.tabris.widgets.ClientCanvas;


@SuppressWarnings("restriction")
public class ClientCanvasLCA extends AbstractWidgetLCA {

  private final AbstractWidgetLCA delegate;

  public ClientCanvasLCA() {
    this( new CanvasLCA() );
  }

  ClientCanvasLCA( AbstractWidgetLCA delegate ) {
    this.delegate = delegate;
  }

  @Override
  public void preserveValues( Widget widget ) {
    delegate.preserveValues( widget );
  }

  @Override
  public void renderInitialization( Widget widget ) throws IOException {
    delegate.renderInitialization( widget );
    RemoteObject remoteObject = RemoteObjectFactory.getRemoteObject( widget );
    remoteObject.setHandler( new ClientCanvasOperator( ( ClientCanvas )widget ) );
  }

  @Override
  public void renderChanges( Widget widget ) throws IOException {
    delegate.renderChanges( widget );
  }
}
