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

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.eclipse.rap.rwt.internal.remote.RemoteObjectImpl;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectRegistry;
import org.eclipse.rap.rwt.lifecycle.AbstractWidgetLCA;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.remote.OperationHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.eclipsesource.tabris.test.RWTRunner;
import com.eclipsesource.tabris.widgets.ClientCanvas;


@SuppressWarnings("restriction")
@RunWith( RWTRunner.class )
public class ClientCanvasLCATest {

  private ClientCanvas clientCanvas;

  @Before
  public void setUp() {
    Display display = new Display();
    Shell shell = new Shell( display );
    clientCanvas = new ClientCanvas( shell, SWT.NONE );
  }

  @Test
  public void testDispatchesRenderInitializationToDelegate() throws IOException {
    AbstractWidgetLCA delegate = mock( AbstractWidgetLCA.class );
    ClientCanvasLCA lca = new ClientCanvasLCA( delegate );

    lca.renderInitialization( clientCanvas );

    verify( delegate ).renderInitialization( clientCanvas );
  }

  @Test
  public void testRenderInitializationExchangesOperationHandler() throws IOException {
    ClientCanvasLCA lca = new ClientCanvasLCA();

    lca.renderInitialization( clientCanvas );

    RemoteObjectImpl remoteObject = RemoteObjectRegistry.getInstance().get( WidgetUtil.getId( clientCanvas ) );
    OperationHandler handler = remoteObject.getHandler();
    assertTrue( handler instanceof ClientCanvasOperator );
  }

  @Test
  public void testDispatchesPreserveValuesToDelegate() {
    AbstractWidgetLCA delegate = mock( AbstractWidgetLCA.class );
    ClientCanvasLCA lca = new ClientCanvasLCA( delegate );

    lca.preserveValues( clientCanvas );

    verify( delegate ).preserveValues( clientCanvas );
  }

  @Test
  public void testDispatchesRenderChangesToDelegate() throws IOException {
    AbstractWidgetLCA delegate = mock( AbstractWidgetLCA.class );
    ClientCanvasLCA lca = new ClientCanvasLCA( delegate );

    lca.renderChanges( clientCanvas );

    verify( delegate ).renderChanges( clientCanvas );
  }
}
