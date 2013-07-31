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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.ArrayList;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectImpl;
import org.eclipse.rap.rwt.internal.serverpush.ServerPushManager;
import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.tabris.test.TabrisTestUtil;
import com.eclipsesource.tabris.ui.UI;
import com.eclipsesource.tabris.ui.action.ProposalHandler;
import com.eclipsesource.tabris.ui.action.SearchAction;


@SuppressWarnings("restriction")
public class RemoteSearchActionTest {

  private RemoteObjectImpl remoteObject;
  private ActionDescriptor actionDescriptor;
  private UI ui;

  @Before
  public void setUp() {
    Fixture.setUp();
    Display display = new Display();
    remoteObject = ( RemoteObjectImpl )TabrisTestUtil.mockRemoteObject();
    ui = mock( UI.class );
    when( ui.getDisplay() ).thenReturn( display );
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
  public void testCalslExecuteOnSelectionEvent() {
    SearchAction action = mock( SearchAction.class );
    when( actionDescriptor.getAction() ).thenReturn( action );
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, actionDescriptor, "foo" );
    when( remoteObject.getHandler() ).thenReturn( remoteAction );

    TabrisTestUtil.dispatchNotify( remoteObject, "Selection", new JsonObject() );

    verify( action ).execute( ui );
  }

  @Test
  public void testCallsSearchOnSearchEvent() {
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, actionDescriptor, "foo" );
    SearchAction action = mock( SearchAction.class );
    when( remoteObject.getHandler() ).thenReturn( remoteAction );
    when( actionDescriptor.getAction() ).thenReturn( action );
    JsonObject properties = new JsonObject();
    properties.add( "query", "bar" );

    TabrisTestUtil.dispatchNotify( remoteObject, "Search", properties );

    verify( action ).search( "bar" );
  }

  @Test
  public void testCallsGetProposlsOnModifyEvent() {
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, actionDescriptor, "foo" );
    SearchAction action = mock( SearchAction.class );
    when( remoteObject.getHandler() ).thenReturn( remoteAction );
    when( actionDescriptor.getAction() ).thenReturn( action );
    JsonObject properties = new JsonObject();
    properties.add( "query", "bar" );

    TabrisTestUtil.dispatchNotify( remoteObject, "Modify", properties );

    verify( action ).modified( eq( "bar" ), any( ProposalHandler.class ) );
  }

  @Test
  public void testSendsProposals() {
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, actionDescriptor, "foo" );
    SearchAction action = mock( SearchAction.class );
    when( remoteObject.getHandler() ).thenReturn( remoteAction );
    when( actionDescriptor.getAction() ).thenReturn( action );
    ArrayList<String> proposals = new ArrayList<String>();
    proposals.add( "foo" );
    proposals.add( "bar" );
    JsonObject properties = new JsonObject();
    properties.add( "query", "bar" );
    TabrisTestUtil.dispatchNotify( remoteObject, "Modify", properties );
    ArgumentCaptor<ProposalHandler> captor = ArgumentCaptor.forClass( ProposalHandler.class );
    verify( action ).modified( eq( "bar" ), captor.capture() );

    captor.getValue().setProposals( proposals );

    verify( remoteObject ).set( "proposals", new JsonArray().add( "foo" ).add( "bar" ) );
  }

  @Test
  public void testSendsNullProposalsAsEmptyArray() {
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, actionDescriptor, "foo" );
    SearchAction action = mock( SearchAction.class );
    when( remoteObject.getHandler() ).thenReturn( remoteAction );
    when( actionDescriptor.getAction() ).thenReturn( action );
    JsonObject properties = new JsonObject();
    properties.add( "query", "bar" );
    TabrisTestUtil.dispatchNotify( remoteObject, "Modify", properties );
    ArgumentCaptor<ProposalHandler> captor = ArgumentCaptor.forClass( ProposalHandler.class );
    verify( action ).modified( eq( "bar" ), captor.capture() );

    captor.getValue().setProposals( new ArrayList<String>() );

    verify( remoteObject ).set( "proposals", new JsonArray() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullProposals() {
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, actionDescriptor, "foo" );
    SearchAction action = spy( new TestSearchAction() );
    when( remoteObject.getHandler() ).thenReturn( remoteAction );
    when( actionDescriptor.getAction() ).thenReturn( action );
    JsonObject properties = new JsonObject();
    properties.add( "query", "bar" );
    TabrisTestUtil.dispatchNotify( remoteObject, "Modify", properties );
    ArgumentCaptor<ProposalHandler> captor = ArgumentCaptor.forClass( ProposalHandler.class );
    verify( action ).modified( eq( "bar" ), captor.capture() );

    captor.getValue().setProposals( null );
  }

  @Test
  public void testActivatesServerPushSessionOnActivate() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, actionDescriptor, "foo" );
    SearchAction action = spy( new TestSearchAction() );
    when( remoteObject.getHandler() ).thenReturn( remoteAction );
    when( actionDescriptor.getAction() ).thenReturn( action );

    remoteAction.handleCall( "activate", null );

    assertTrue( ServerPushManager.getInstance().isServerPushActive() );
  }

  @Test
  public void testDeactivatesServerPushSessionOnDeactivate() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, actionDescriptor, "foo" );
    SearchAction action = spy( new TestSearchAction() );
    when( remoteObject.getHandler() ).thenReturn( remoteAction );
    when( actionDescriptor.getAction() ).thenReturn( action );
    remoteAction.handleCall( "activate", null );

    remoteAction.handleCall( "deactivate", null );

    assertFalse( ServerPushManager.getInstance().isServerPushActive() );
  }
}
