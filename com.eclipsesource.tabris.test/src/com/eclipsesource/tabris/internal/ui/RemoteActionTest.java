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

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectImpl;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.tabris.test.RWTRunner;
import com.eclipsesource.tabris.test.TabrisTestUtil;
import com.eclipsesource.tabris.ui.Action;
import com.eclipsesource.tabris.ui.PlacementPriority;
import com.eclipsesource.tabris.ui.UI;


@SuppressWarnings("restriction")
@RunWith( RWTRunner.class )
public class RemoteActionTest {

  private RemoteObjectImpl remoteObject;
  private ActionDescriptor actionDescriptor;
  private UI ui;
  private RemoteUI uiRenderer;

  @Before
  public void setUp() {
    new Display();
    remoteObject = ( RemoteObjectImpl )TabrisTestUtil.mockRemoteObject();
    ui = mock( UI.class );
    uiRenderer = mock( RemoteUI.class );
    when( uiRenderer.getRemoteUIId() ).thenReturn( "foo" );
    actionDescriptor = mock( ActionDescriptor.class );
    when( actionDescriptor.getAction() ).thenReturn( new TestAction() );
    when( actionDescriptor.getId() ).thenReturn( "foo" );
    InputStream image = RemoteActionTest.class.getResourceAsStream( "testImage.png" );
    when( actionDescriptor.getImage() ).thenReturn( ImageUtil.getBytes( image ) );
    when( actionDescriptor.getTitle() ).thenReturn( "bar" );
    when( actionDescriptor.getPlacementPriority() ).thenReturn( PlacementPriority.HIGH );
  }

  @Test
  public void testSetsInitialAttributes() {
    new RemoteAction( ui, uiRenderer, actionDescriptor );

    verify( remoteObject ).set( "parent", "foo" );
    ArgumentCaptor<JsonArray> captor = ArgumentCaptor.forClass( JsonArray.class );
    verify( remoteObject ).set( eq( "image" ), captor.capture() );
    assertTrue( captor.getValue().get( 0 ).isString() );
    assertEquals( 49, captor.getValue().get( 1 ).asInt() );
    assertEquals( 43, captor.getValue().get( 2 ).asInt() );
    verify( remoteObject ).set( "title", "bar" );
    verify( remoteObject ).set( "visibility", false );
    verify( remoteObject ).set( "enabled", false );
  }

  @Test
  public void testDoesNotSetVisibleAndEnableDefault() {
    doReturn( Boolean.TRUE ).when( actionDescriptor ).isEnabled();
    doReturn( Boolean.TRUE ).when( actionDescriptor ).isVisible();

    new RemoteAction( ui, uiRenderer, actionDescriptor );

    verify( remoteObject, never() ).set( "visibility", true );
    verify( remoteObject, never() ).set( "enabled", true );
  }

  @Test
  public void testSetsInitialAttributesWithoutImage() {
    when( actionDescriptor.getImage() ).thenReturn( null );
    new RemoteAction( ui, uiRenderer, actionDescriptor );

    verify( remoteObject, never() ).set( eq( "image" ), anyString() );
  }

  @Test
  public void testGetsDescriptor() {
    RemoteAction remoteAction = new RemoteAction( ui, uiRenderer, actionDescriptor );

    ActionDescriptor actualDescriptor = remoteAction.getDescriptor();

    assertSame( actionDescriptor, actualDescriptor );
  }

  @Test
  public void testGetsUI() {
    RemoteAction remoteAction = new RemoteAction( ui, uiRenderer, actionDescriptor );

    UI actualUi = remoteAction.getUI();

    assertSame( ui, actualUi );
  }

  @Test
  public void testSetsVisible() {
    RemoteAction remoteAction = new RemoteAction( ui, uiRenderer, actionDescriptor );

    remoteAction.setVisible( true );

    verify( remoteObject ).set( "visibility", true );
  }

  @Test
  public void testSetsEnabled() {
    RemoteAction remoteAction = new RemoteAction( ui, uiRenderer, actionDescriptor );

    remoteAction.setEnabled( true );

    verify( remoteObject ).set( "enabled", true );
  }

  @Test
  public void testSetsPlacementPriority() {
    new RemoteAction( ui, uiRenderer, actionDescriptor );

    verify( remoteObject ).set( "placementPriority", "HIGH" );
  }

  @Test
  public void testDestroy() {
    RemoteAction remoteAction = new RemoteAction( ui, uiRenderer, actionDescriptor );

    remoteAction.destroy();

    verify( remoteObject ).destroy();
  }

  @Test
  public void testGetRemotObject() {
    RemoteAction remoteAction = new RemoteAction( ui, uiRenderer, actionDescriptor );

    RemoteObject actualRemoteObject = remoteAction.getRemoteObject();

    assertSame( remoteObject, actualRemoteObject );
  }

  @Test
  public void testCallsExecuteOnEvent() {
    RemoteAction remoteAction = new RemoteAction( ui, uiRenderer, actionDescriptor );
    Action action = mock( Action.class );
    when( remoteObject.getHandler() ).thenReturn( remoteAction );
    when( actionDescriptor.getAction() ).thenReturn( action );

    TabrisTestUtil.dispatchNotify( remoteObject, "Selection", new JsonObject() );

    verify( action ).execute( ui );
  }
}
