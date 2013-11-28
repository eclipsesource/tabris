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

import static com.eclipsesource.tabris.internal.Constants.METHOD_OPEN;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_MESSAGE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_QUERY;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
import com.eclipsesource.tabris.ui.action.Proposal;
import com.eclipsesource.tabris.ui.action.ProposalHandler;
import com.eclipsesource.tabris.ui.action.SearchAction;


@SuppressWarnings("restriction")
public class RemoteSearchActionTest {

  private RemoteObjectImpl remoteObject;
  private ActionDescriptor actionDescriptor;
  private UI ui;
  private RemoteUI uiRenderer;

  @Before
  public void setUp() {
    Fixture.setUp();
    Display display = new Display();
    remoteObject = ( RemoteObjectImpl )TabrisTestUtil.mockRemoteObject();
    ui = mock( UI.class );
    uiRenderer = mock( RemoteUI.class );
    when( uiRenderer.getRemoteUIId() ).thenReturn( "foo" );
    when( ui.getDisplay() ).thenReturn( display );
    actionDescriptor = mock( ActionDescriptor.class );
    when( actionDescriptor.getId() ).thenReturn( "foo" );
    InputStream image = RemoteActionTest.class.getResourceAsStream( "testImage.png" );
    when( actionDescriptor.getImage() ).thenReturn( ImageUtil.getBytes( image ) );
    when( actionDescriptor.getTitle() ).thenReturn( "bar" );
    TestSearchAction action = new TestSearchAction();
    when( actionDescriptor.getAction() ).thenReturn( action );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testRegistersItselfAsChangeListener() {
    SearchAction action = spy( new TestSearchAction() );
    when( actionDescriptor.getAction() ).thenReturn( action );

    RemoteSearchAction renderer = new RemoteSearchAction( ui, uiRenderer, actionDescriptor );

    PropertyChangeNotifier adapter = action.getAdapter( PropertyChangeNotifier.class );
    assertSame( adapter.getPropertyChangeHandler(), renderer );
  }

  @Test
  public void testCallsExecuteOnSelectionEvent() {
    SearchAction action = spy( new TestSearchAction() );
    when( actionDescriptor.getAction() ).thenReturn( action );
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, uiRenderer, actionDescriptor );
    when( remoteObject.getHandler() ).thenReturn( remoteAction );

    TabrisTestUtil.dispatchNotify( remoteObject, "Selection", new JsonObject() );

    verify( action ).execute( ui );
  }

  @Test
  public void testCallsSearchOnSearchEvent() {
    SearchAction action = spy( new TestSearchAction() );
    when( actionDescriptor.getAction() ).thenReturn( action );
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, uiRenderer, actionDescriptor );
    when( remoteObject.getHandler() ).thenReturn( remoteAction );
    JsonObject properties = new JsonObject();
    properties.add( "query", "bar" );

    TabrisTestUtil.dispatchNotify( remoteObject, "Search", properties );

    verify( action ).search( "bar" );
  }

  @Test
  public void testCallsGetProposalsOnModifyEvent() {
    SearchAction action = spy( new TestSearchAction() );
    when( actionDescriptor.getAction() ).thenReturn( action );
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, uiRenderer, actionDescriptor );
    when( remoteObject.getHandler() ).thenReturn( remoteAction );
    JsonObject properties = new JsonObject();
    properties.add( "query", "bar" );

    TabrisTestUtil.dispatchNotify( remoteObject, "Modify", properties );

    verify( action ).modified( eq( "bar" ), any( ProposalHandler.class ) );
  }

  @Test
  public void testSendsProposals() {
    SearchAction action = spy( new TestSearchAction() );
    when( actionDescriptor.getAction() ).thenReturn( action );
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, uiRenderer, actionDescriptor );
    when( remoteObject.getHandler() ).thenReturn( remoteAction );
    List<Proposal> proposals = new ArrayList<Proposal>();
    proposals.add( new Proposal( "foo" ) );
    proposals.add( new Proposal( "bar" ) );
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
    SearchAction action = spy( new TestSearchAction() );
    when( actionDescriptor.getAction() ).thenReturn( action );
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, uiRenderer, actionDescriptor );
    when( remoteObject.getHandler() ).thenReturn( remoteAction );
    JsonObject properties = new JsonObject();
    properties.add( "query", "bar" );
    TabrisTestUtil.dispatchNotify( remoteObject, "Modify", properties );
    ArgumentCaptor<ProposalHandler> captor = ArgumentCaptor.forClass( ProposalHandler.class );
    verify( action ).modified( eq( "bar" ), captor.capture() );

    captor.getValue().setProposals( new ArrayList<Proposal>() );

    verify( remoteObject ).set( "proposals", new JsonArray() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullProposals() {
    SearchAction action = spy( new TestSearchAction() );
    when( actionDescriptor.getAction() ).thenReturn( action );
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, uiRenderer, actionDescriptor );
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
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, uiRenderer, actionDescriptor );
    SearchAction action = spy( new TestSearchAction() );
    when( remoteObject.getHandler() ).thenReturn( remoteAction );
    when( actionDescriptor.getAction() ).thenReturn( action );

    remoteAction.handleCall( "activate", null );

    assertTrue( ServerPushManager.getInstance().isServerPushActive() );
  }

  @Test
  public void testDeactivatesServerPushSessionOnDeactivate() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, uiRenderer, actionDescriptor );
    when( remoteObject.getHandler() ).thenReturn( remoteAction );
    remoteAction.handleCall( "activate", null );

    remoteAction.handleCall( "deactivate", null );

    assertFalse( ServerPushManager.getInstance().isServerPushActive() );
  }

  @Test
  public void testCallsOpenOnPropertyChange() {
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, uiRenderer, actionDescriptor );
    when( remoteObject.getHandler() ).thenReturn( remoteAction );

    remoteAction.propertyChanged( METHOD_OPEN, null );

    verify( remoteObject ).call( "open", null );
  }

  @Test
  public void testCallsExecuteBeforeOpenOnPropertyChange() {
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, uiRenderer, actionDescriptor );
    when( remoteObject.getHandler() ).thenReturn( remoteAction );

    remoteAction.propertyChanged( METHOD_OPEN, null );

    assertTrue( ( ( TestSearchAction ) actionDescriptor.getAction() ).wasExecuted() );
  }

  @Test
  public void testSetsQueryOnPropertyChange() {
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, uiRenderer, actionDescriptor );
    when( remoteObject.getHandler() ).thenReturn( remoteAction );

    remoteAction.propertyChanged( PROPERTY_QUERY, "foo" );

    verify( remoteObject ).set( "query", "foo" );
  }

  @Test
  public void testSetsMessageOnPropertyChange() {
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, uiRenderer, actionDescriptor );
    when( remoteObject.getHandler() ).thenReturn( remoteAction );

    remoteAction.propertyChanged( PROPERTY_MESSAGE, "foo" );

    verify( remoteObject ).set( "message", "foo" );
  }
}
