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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.HashMap;

import org.eclipse.rap.rwt.internal.remote.RemoteObjectImpl;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.tabris.test.TabrisTestUtil;
import com.eclipsesource.tabris.ui.Action;
import com.eclipsesource.tabris.ui.UI;


@SuppressWarnings("restriction")
public class RemoteActionTest {

  private RemoteObjectImpl remoteObject;
  private ActionDescriptor actionDescriptor;
  private UI ui;

  @Before
  public void setUp() {
    Fixture.setUp();
    new Display();
    remoteObject = ( RemoteObjectImpl )TabrisTestUtil.mockRemoteObject();
    ui = mock( UI.class );
    actionDescriptor = mock( ActionDescriptor.class );
    when( actionDescriptor.getAction() ).thenReturn( new TestAction() );
    when( actionDescriptor.getId() ).thenReturn( "foo" );
    InputStream image = RemoteActionTest.class.getResourceAsStream( "testImage.png" );
    when( actionDescriptor.getImage() ).thenReturn( ImageUtil.getBytes( image ) );
    when( actionDescriptor.getTitle() ).thenReturn( "bar" );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testSetsInitialAttributes() {
    new RemoteAction( ui, actionDescriptor, "foo" );

    verify( remoteObject ).set( "parent", "foo" );
    ArgumentCaptor<Object[]> captor = ArgumentCaptor.forClass( Object[].class );
    verify( remoteObject ).set( eq( "image" ), captor.capture() );
    assertTrue( captor.getValue()[ 0 ] instanceof String );
    assertEquals( Integer.valueOf( 49 ), captor.getValue()[ 1 ] );
    assertEquals( Integer.valueOf( 43 ), captor.getValue()[ 2 ] );
    verify( remoteObject ).set( "title", "bar" );
    verify( remoteObject ).set( "visibility", false );
    verify( remoteObject ).set( "enabled", false );
  }

  @Test
  public void testDoesNotSetVisibleAndEnableDefault() {
    doReturn( Boolean.TRUE ).when( actionDescriptor ).isEnabled();
    doReturn( Boolean.TRUE ).when( actionDescriptor ).isVisible();

    new RemoteAction( ui, actionDescriptor, "foo" );

    verify( remoteObject, never() ).set( "visibility", true );
    verify( remoteObject, never() ).set( "enabled", true );
  }

  @Test
  public void testSetsInitialAttributesWithoutImage() {
    when( actionDescriptor.getImage() ).thenReturn( null );
    new RemoteAction( ui, actionDescriptor, "foo" );

    verify( remoteObject, never() ).set( eq( "image" ), anyString() );
  }

  @Test
  public void testGetsDescriptor() {
    RemoteAction remoteAction = new RemoteAction( ui, actionDescriptor, "foo" );

    ActionDescriptor actualDescriptor = remoteAction.getDescriptor();

    assertSame( actionDescriptor, actualDescriptor );
  }

  @Test
  public void testSetsVisible() {
    RemoteAction remoteAction = new RemoteAction( ui, actionDescriptor, "foo" );

    remoteAction.setVisible( true );

    verify( remoteObject ).set( "visibility", true );
  }

  @Test
  public void testSetEnabled() {
    RemoteAction remoteAction = new RemoteAction( ui, actionDescriptor, "foo" );

    remoteAction.setEnabled( true );

    verify( remoteObject ).set( "enabled", true );
  }

  @Test
  public void testDestroy() {
    RemoteAction remoteAction = new RemoteAction( ui, actionDescriptor, "foo" );

    remoteAction.destroy();

    verify( remoteObject ).destroy();
  }

  @Test
  public void testCallsExecuteOnEvent() {
    RemoteAction remoteAction = new RemoteAction( ui, actionDescriptor, "foo" );
    Action action = mock( Action.class );
    when( remoteObject.getHandler() ).thenReturn( remoteAction );
    when( actionDescriptor.getAction() ).thenReturn( action );

    Fixture.dispatchNotify( remoteObject, "Selection", new HashMap<String, Object>() );

    verify( action ).execute( ui );
  }
}
