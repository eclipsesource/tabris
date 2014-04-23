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

import static org.eclipse.rap.rwt.lifecycle.WidgetUtil.getId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectRegistry;
import org.eclipse.rap.rwt.remote.OperationHandler;
import org.eclipse.rap.rwt.scripting.ClientListener;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rap.rwt.testfixture.Message;
import org.eclipse.rap.rwt.testfixture.Message.CreateOperation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.test.ControlLCATestUtil;
import com.eclipsesource.tabris.widgets.RefreshComposite;
import com.eclipsesource.tabris.widgets.RefreshListener;


@SuppressWarnings("restriction")
public class RefreshCompositeLCATest {

  private Display display;
  private Shell shell;
  private RefreshCompositeLCA lca;
  private RefreshComposite composite;

  @Before
  public void setUp() {
    Fixture.setUp();
    display = new Display();
    shell = new Shell( display );
    lca = new RefreshCompositeLCA();
    Fixture.fakeNewRequest();
    composite = new RefreshComposite( shell, SWT.BORDER );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
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

    Message message = Fixture.getProtocolMessage();
    CreateOperation operation = message.findCreateOperation( composite );
    assertEquals( "tabris.widgets.RefreshComposite", operation.getType() );
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

    Message message = Fixture.getProtocolMessage();
    assertEquals( "foo", message.findCreateProperty( composite, "message" ).asString() );
  }

  @Test
  public void testRenderInitializationRendersRefreshListen() throws IOException {
    composite.addRefreshListener( mock( RefreshListener.class ) );

    lca.renderInitialization( composite );

    Message message = Fixture.getProtocolMessage();
    assertTrue( message.findListenProperty( composite, "Refresh" ).asBoolean() );
  }

  @Test
  public void testRenderParent() throws IOException {
    lca.renderInitialization( composite );

    Message message = Fixture.getProtocolMessage();
    CreateOperation operation = message.findCreateOperation( composite );
    assertEquals( getId( composite.getParent() ), operation.getParent() );
  }

  @Test
  public void testRenderListenToRefreshToRefresh() {
    composite.addRefreshListener( mock( RefreshListener.class ) );

    lca.renderListenToRefresh( composite );

    Message message = Fixture.getProtocolMessage();
    assertTrue( message.findListenProperty( composite, "Refresh" ).asBoolean() );
  }

  @Test
  public void testRenderListenToRefreshToRefreshUnchanged() {
    Fixture.markInitialized( composite );
    composite.addRefreshListener( mock( RefreshListener.class ) );

    lca.preserveValues( composite );
    lca.renderClientArea( composite );

    Message message = Fixture.getProtocolMessage();
    assertNull( message.findListenOperation( composite, "Refresh" ) );
  }

  @Test
  public void testRenderMessage() {
    composite.setMessage( "foo" );

    lca.renderMessage( composite );

    Message message = Fixture.getProtocolMessage();
    assertEquals( "foo", message.findSetProperty( composite, "message" ).asString() );
  }

  @Test
  public void testRenderMessageUnchanged() {
    Fixture.markInitialized( composite );
    composite.setMessage( "foo" );

    lca.preserveValues( composite );
    lca.renderClientArea( composite );

    Message message = Fixture.getProtocolMessage();
    assertNull( message.findSetOperation( composite, "message" ) );
  }

  @Test
  public void testRenderReset() {
    composite.reset();

    lca.renderReset( composite );

    Message message = Fixture.getProtocolMessage();
    assertTrue( message.findSetProperty( composite, "reset" ).asBoolean() );
  }

  @Test
  public void testRenderResetUnchanged() {
    Fixture.markInitialized( composite );
    composite.reset();

    lca.preserveValues( composite );
    lca.renderReset( composite );

    Message message = Fixture.getProtocolMessage();
    assertNull( message.findSetOperation( composite, "reset" ) );
  }

  @Test
  public void testRenderClientArea() {
    composite.setSize( 110, 120 );

    lca.renderClientArea( composite );

    Message message = Fixture.getProtocolMessage();
    Rectangle clientArea = composite.getClientArea();
    assertEquals( clientArea, toRectangle( message.findSetProperty( composite, "clientArea" ) ) );
  }

  @Test
  public void testRenderClientAreaSizeZero() {
    composite.setSize( 0, 0 );

    lca.renderClientArea( composite );

    Message message = Fixture.getProtocolMessage();
    Rectangle clientArea = new Rectangle( 0, 0, 0, 0 );
    assertEquals( clientArea, toRectangle( message.findSetProperty( composite, "clientArea" ) ) );
  }

  @Test
  public void testRenderClientAreaSizeUnchanged() {
    Fixture.markInitialized( composite );
    composite.setSize( 110, 120 );

    lca.preserveValues( composite );
    lca.renderClientArea( composite );

    Message message = Fixture.getProtocolMessage();
    assertNull( message.findSetOperation( composite, "clientArea" ) );
  }

  @Test
  public void testRenderChangesRendersClientListener() throws IOException {
    composite.addListener( SWT.MouseDown, new ClientListener( "" ) );

    lca.renderChanges( composite );

    Message message = Fixture.getProtocolMessage();
    assertNotNull( message.findCallOperation( composite, "addListener" ) );
  }

  private Rectangle toRectangle( Object property ) {
    JsonArray jsonArray = ( JsonArray )property;
    Rectangle result = new Rectangle(
                                     jsonArray.get( 0 ).asInt(),
                                     jsonArray.get( 1 ).asInt(),
                                     jsonArray.get( 2 ).asInt(),
                                     jsonArray.get( 3 ).asInt()
        );
    return result;
  }

}
