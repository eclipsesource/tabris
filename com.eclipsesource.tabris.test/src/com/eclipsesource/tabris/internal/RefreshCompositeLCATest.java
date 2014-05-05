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

import static com.eclipsesource.tabris.test.util.MessageUtil.getOperationProperties;
import static com.eclipsesource.tabris.test.util.MessageUtil.hasCreateOperation;
import static com.eclipsesource.tabris.test.util.MessageUtil.hasOperation;
import static com.eclipsesource.tabris.test.util.MessageUtil.isParentOfCreate;
import static com.eclipsesource.tabris.test.util.MessageUtil.OperationType.CALL;
import static com.eclipsesource.tabris.test.util.MessageUtil.OperationType.CREATE;
import static com.eclipsesource.tabris.test.util.MessageUtil.OperationType.LISTEN;
import static com.eclipsesource.tabris.test.util.MessageUtil.OperationType.SET;
import static org.eclipse.rap.rwt.lifecycle.WidgetUtil.getId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectRegistry;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.remote.OperationHandler;
import org.eclipse.rap.rwt.scripting.ClientListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.test.util.ControlLCATestUtil;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;
import com.eclipsesource.tabris.widgets.RefreshComposite;
import com.eclipsesource.tabris.widgets.RefreshListener;


@SuppressWarnings("restriction")
public class RefreshCompositeLCATest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private Display display;
  private Shell shell;
  private RefreshCompositeLCA lca;
  private RefreshComposite composite;

  @Before
  public void setUp() {
    display = new Display();
    shell = new Shell( display );
    lca = new RefreshCompositeLCA();
    composite = new RefreshComposite( shell, SWT.BORDER );
  }

  @Test
  public void testControlListeners() throws IOException {
    ControlLCATestUtil.testActivateListener( composite );
    ControlLCATestUtil.testFocusListener( composite );
    ControlLCATestUtil.testMouseListener( composite );
    ControlLCATestUtil.testKeyListener( composite );
    ControlLCATestUtil.testTraverseListener( composite );
    ControlLCATestUtil.testMenuDetectListener( composite );
    ControlLCATestUtil.testHelpListener( composite );
  }

  @Test
  public void testRenderCreate() throws IOException {
    lca.renderInitialization( composite );

    assertTrue( hasCreateOperation( "tabris.widgets.RefreshComposite" ) );
  }

  @Test
  public void testRenderInitializationSetsOperationHandler() throws IOException {
    String id = getId( composite );
    lca.renderInitialization( composite );

    OperationHandler handler = RemoteObjectRegistry.getInstance().get( id ).getHandler();
    assertTrue( handler instanceof RefreshCompositeOperationHandler );
  }

  @Test
  public void testRenderInitializationRendersMessage() throws IOException {
    composite.setMessage( "foo" );

    lca.renderInitialization( composite );

    JsonObject properties = getOperationProperties( WidgetUtil.getId( composite ), CREATE, null );
    assertEquals( "foo", properties.get( "message" ).asString() );
  }

  @Test
  public void testRenderInitializationRendersRefreshListen() throws IOException {
    composite.addRefreshListener( mock( RefreshListener.class ) );

    lca.renderInitialization( composite );

    JsonObject properties = getOperationProperties( WidgetUtil.getId( composite ), LISTEN, null );
    assertTrue( properties.get( "Refresh" ).asBoolean() );
  }

  @Test
  public void testRenderParent() throws IOException {
    lca.renderInitialization( composite );

    assertTrue( isParentOfCreate( "tabris.widgets.RefreshComposite", getId( composite.getParent() ) ) );
  }

  @Test
  public void testRenderListenToRefreshToRefresh() {
    composite.addRefreshListener( mock( RefreshListener.class ) );

    lca.renderListenToRefresh( composite );

    JsonObject properties = getOperationProperties( WidgetUtil.getId( composite ), LISTEN, null );
    assertTrue( properties.get( "Refresh" ).asBoolean() );
  }

  @Test
  public void testRenderListenToRefreshToRefreshUnchanged() {
    composite.addRefreshListener( mock( RefreshListener.class ) );

    lca.preserveValues( composite );
    lca.renderClientArea( composite );

    assertFalse( hasOperation( WidgetUtil.getId( composite ), LISTEN, null ) );
  }

  @Test
  public void testRenderMessage() {
    composite.setMessage( "foo" );

    lca.renderMessage( composite );

    JsonObject properties = getOperationProperties( WidgetUtil.getId( composite ), SET, null );
    assertEquals( "foo", properties.get( "message" ).asString() );
  }

  @Test
  public void testRenderMessageUnchanged() throws IOException {
    composite.setMessage( "foo" );

    lca.renderInitialization( composite );
    lca.preserveValues( composite );
    lca.renderClientArea( composite );

    assertFalse( hasOperation( WidgetUtil.getId( composite ), SET, null ) );
  }

  @Test
  public void testRenderDone() {
    composite.done();

    lca.renderDone( composite );

    JsonObject properties = getOperationProperties( WidgetUtil.getId( composite ), CALL, "done" );
    assertTrue( properties.isEmpty() );
  }

  @Test
  public void testRenderDoneUnchanged() {
    composite.done();

    lca.preserveValues( composite );
    lca.renderDone( composite );

    assertFalse( hasOperation( WidgetUtil.getId( composite ), CALL, "done" ) );
  }

  @Test
  public void testRenderClientArea() {
    composite.setSize( 110, 120 );

    lca.renderClientArea( composite );

    Rectangle clientArea = composite.getClientArea();
    JsonObject properties = getOperationProperties( WidgetUtil.getId( composite ), SET, null );
    assertEquals( clientArea, toRectangle( properties.get( "clientArea" ) ) );
  }

  @Test
  public void testRenderClientAreaSizeZero() {
    composite.setSize( 0, 0 );

    lca.renderClientArea( composite );

    Rectangle clientArea = new Rectangle( 0, 0, 0, 0 );
    JsonObject properties = getOperationProperties( WidgetUtil.getId( composite ), SET, null );
    assertEquals( clientArea, toRectangle( properties.get( "clientArea" ) ) );
  }

  @Test
  public void testRenderClientAreaSizeUnchanged() throws IOException {
    composite.setSize( 110, 120 );

    lca.renderInitialization( composite );
    lca.preserveValues( composite );
    lca.renderClientArea( composite );

    assertFalse( hasOperation( WidgetUtil.getId( composite ), SET, null ) );
  }

  @Test
  public void testRenderChangesRendersClientListener() throws IOException {
    composite.addListener( SWT.MouseDown, new ClientListener( "" ) );

    lca.renderChanges( composite );

    JsonObject properties = getOperationProperties( WidgetUtil.getId( composite ), CALL, "addListener" );
    assertNotNull( properties );
  }

  private Rectangle toRectangle( Object property ) {
    JsonArray jsonArray = ( JsonArray )property;
    Rectangle result = new Rectangle( jsonArray.get( 0 ).asInt(),
                                      jsonArray.get( 1 ).asInt(),
                                      jsonArray.get( 2 ).asInt(),
                                      jsonArray.get( 3 ).asInt() );
    return result;
  }

}
