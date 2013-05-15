/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rap.rwt.testfixture.Message;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;


public class ControlLCATestUtil {

  public static void testActivateListener( Control control ) throws IOException {
    Fixture.markInitialized( control.getDisplay() );
    Fixture.markInitialized( control );
    testRenderAddListener( control, SWT.Activate );
    testRenderRemoveListener( control, SWT.Activate );
    testRenderListenerUnchanged( control, SWT.Activate );
    testRenderAddListener( control, SWT.Deactivate );
    testRenderRemoveListener( control, SWT.Deactivate );
    testRenderListenerUnchanged( control, SWT.Deactivate );
  }

  public static void testFocusListener( Control control ) throws IOException {
    Fixture.markInitialized( control.getDisplay() );
    Fixture.markInitialized( control );
    testRenderAddListener( control, SWT.FocusIn );
    testRenderRemoveListener( control, SWT.FocusIn );
    testRenderListenerUnchanged( control, SWT.FocusIn );
    testRenderAddListener( control, SWT.FocusOut );
    testRenderRemoveListener( control, SWT.FocusOut );
    testRenderListenerUnchanged( control, SWT.FocusOut );
  }

  public static void testMouseListener( Control control ) throws IOException {
    Fixture.markInitialized( control.getDisplay() );
    Fixture.markInitialized( control );
    testRenderAddListener( control, SWT.MouseDown );
    testRenderRemoveListener( control, SWT.MouseDown );
    testRenderListenerUnchanged( control, SWT.MouseDown );
    testRenderAddListener( control, SWT.MouseDoubleClick );
    testRenderRemoveListener( control, SWT.MouseDoubleClick );
    testRenderListenerUnchanged( control, SWT.MouseDoubleClick );
    testRenderAddListener( control, SWT.MouseUp );
    testRenderRemoveListener( control, SWT.MouseUp );
    testRenderListenerUnchanged( control, SWT.MouseUp );
  }

  public static void testKeyListener( Control control ) throws IOException {
    Fixture.markInitialized( control.getDisplay() );
    Fixture.markInitialized( control );
    testRenderAddListener( control, SWT.KeyDown );
    testRenderRemoveListener( control, SWT.KeyDown );
    testRenderListenerUnchanged( control, SWT.KeyDown );
    testRenderAddListener( control, SWT.KeyUp );
    testRenderRemoveListener( control, SWT.KeyUp );
    testRenderListenerUnchanged( control, SWT.KeyUp );
  }

  public static void testTraverseListener( Control control ) throws IOException {
    Fixture.markInitialized( control.getDisplay() );
    Fixture.markInitialized( control );
    testRenderAddListener( control, SWT.Traverse );
    testRenderRemoveListener( control, SWT.Traverse );
    testRenderListenerUnchanged( control, SWT.Traverse );
  }

  public static void testMenuDetectListener( Control control ) throws IOException {
    Fixture.markInitialized( control.getDisplay() );
    Fixture.markInitialized( control );
    testRenderAddListener( control, SWT.MenuDetect );
    testRenderRemoveListener( control, SWT.MenuDetect );
    testRenderListenerUnchanged( control, SWT.MenuDetect );
  }

  public static void testHelpListener( Control control ) throws IOException {
    Fixture.markInitialized( control.getDisplay() );
    Fixture.markInitialized( control );
    testRenderAddListener( control, SWT.Help );
    testRenderRemoveListener( control, SWT.Help );
    testRenderListenerUnchanged( control, SWT.Help );
  }

  private static void testRenderAddListener( Control control, int eventType )
    throws IOException
  {
    Listener listener = mock( Listener.class );
    Fixture.fakeNewRequest();
    Fixture.preserveWidgets();

    control.addListener( eventType, listener );
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    String listenerName = getListenerName( eventType );
    assertTrue( message.findListenProperty( control, listenerName ).asBoolean() );

    control.removeListener( eventType, listener );
  }

  private static void testRenderRemoveListener( Control control, int eventType )
    throws IOException
  {
    Listener listener = mock( Listener.class );
    control.addListener( eventType, listener );
    Fixture.fakeNewRequest();
    Fixture.preserveWidgets();

    control.removeListener( eventType, listener );
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    String listenerName = getListenerName( eventType );
    assertFalse( message.findListenProperty( control, listenerName ).asBoolean() );
  }

  private static void testRenderListenerUnchanged( Control control, int eventType )
    throws IOException
  {
    Listener listener = mock( Listener.class );
    Fixture.fakeNewRequest();
    Fixture.preserveWidgets();

    control.addListener( eventType, listener );
    Fixture.preserveWidgets();
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    String listenerName = getListenerName( eventType );
    assertNull( message.findListenOperation( control, listenerName ) );

    control.removeListener( eventType, listener );
  }

  private static String getListenerName( int eventType ) {
    String result = "None";
    switch( eventType ) {
      case SWT.MouseDown:
        result = "MouseDown";
        break;
      case SWT.MouseDoubleClick:
        result = "MouseDoubleClick";
        break;
      case SWT.MouseUp:
        result = "MouseUp";
        break;
      case SWT.FocusIn:
        result = "FocusIn";
        break;
      case SWT.FocusOut:
        result = "FocusOut";
        break;
      case SWT.Activate:
        result = "Activate";
        break;
      case SWT.Deactivate:
        result = "Deactivate";
        break;
      case SWT.Help:
        result = "Help";
        break;
      case SWT.MenuDetect:
        result = "MenuDetect";
        break;
      case SWT.KeyDown:
        result = "KeyDown";
        break;
      case SWT.KeyUp:
        // [if] Note: we are sending only KeyDown event from the client
        result = "KeyDown";
        break;
      case SWT.Traverse:
        result = "Traverse";
        break;
    }
    return result;
  }
}
