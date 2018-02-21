/*******************************************************************************
 * Copyright (c) 2018 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets;

import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.service.UISessionImpl;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.test.util.TabrisEnvironment;

@SuppressWarnings("restriction")
public class BarcodeScannerTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private Shell shell;
  private BarcodeScanner scanner;
  private Connection connection;
  private RemoteObject remoteObject;

  @Before
  public void setUp() {
    shell = new Shell( new Display() );
    remoteObject = mock( RemoteObject.class );
    connection = mock( Connection.class );
    when( connection.createRemoteObject( anyString() ) ).thenReturn( remoteObject );
    replaceConnection( connection );
    scanner = new BarcodeScanner( shell );
  }

  private void replaceConnection( Connection connection ) {
    UISessionImpl uiSession = ( UISessionImpl )RWT.getUISession();
    uiSession.setConnection( connection );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullParent() {
    new BarcodeScanner( null );
  }

  @Test
  public void testContructor_createsRemoteObjectWithCorrectType() {
    verify( connection ).createRemoteObject( eq( "tabris.widgets.BarcodeScanner" ) );
  }

  @Test
  public void testContructor_setsParent() {
    verify( remoteObject ).set( "parent", getId( scanner ) );
  }

  @Test
  public void testSetScaleMode() {
    scanner.setScaleMode( BarcodeScanner.ScaleMode.FILL );

    assertEquals( BarcodeScanner.ScaleMode.FILL, scanner.getScaleMode() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetScaleMode_nullArgument() {
    scanner.setScaleMode( null );
  }

  @Test
  public void testRenderScaleMode() {
    scanner.setScaleMode( BarcodeScanner.ScaleMode.FILL );

    verify( remoteObject ).set( "scaleMode", "fill" );
  }

  @Test
  public void testRenderStart() {
    scanner.start( new BarcodeScanner.Formats[] { BarcodeScanner.Formats.qr } );

    verify( remoteObject ).call( "start", new JsonObject().set( "formats", new JsonArray().add( "qr" ) ) );
  }

  @Test
  public void testRenderStart_onlyOnce() {
    scanner.start( new BarcodeScanner.Formats[] { BarcodeScanner.Formats.qr } );
    scanner.start( new BarcodeScanner.Formats[] { BarcodeScanner.Formats.qr } );

    verify( remoteObject, times( 1 ) ).call( "start", new JsonObject().set( "formats", new JsonArray().add( "qr" ) ) );
  }

  @Test
  public void testRenderStop() {
    scanner.start( new BarcodeScanner.Formats[] { BarcodeScanner.Formats.qr } );
    reset( remoteObject );

    scanner.stop();

    verify( remoteObject ).call( "stop", null );
  }

  @Test
  public void testRenderStop_onlyOnce() {
    scanner.start( new BarcodeScanner.Formats[] { BarcodeScanner.Formats.qr } );
    reset( remoteObject );

    scanner.stop();
    scanner.stop();

    verify( remoteObject, times( 1 ) ).call( "stop", null );
  }

  @Test
  public void testIsRunning_returnsFalseInitially() throws Exception {
    assertFalse( scanner.isRunning() );
  }

  @Test
  public void testIsRunning_returnsTrueAfterStart() throws Exception {
    scanner.start( new BarcodeScanner.Formats[] { BarcodeScanner.Formats.qr } );

    assertTrue( scanner.isRunning() );
  }

  @Test
  public void testIsRunning_returnsTrueAfterStop() throws Exception {
    scanner.start( new BarcodeScanner.Formats[] { BarcodeScanner.Formats.qr } );

    scanner.stop();

    assertFalse( scanner.isRunning() );
  }

}
