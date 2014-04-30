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
import org.eclipse.rap.rwt.internal.serverpush.ServerPushManager;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.tabris.test.RWTEnvironment;
import com.eclipsesource.tabris.ui.UI;
import com.eclipsesource.tabris.ui.UIConfiguration;
import com.eclipsesource.tabris.ui.action.Proposal;
import com.eclipsesource.tabris.ui.action.ProposalHandler;
import com.eclipsesource.tabris.ui.action.SearchAction;
import com.eclipsesource.tabris.ui.action.SearchActionListener;


@SuppressWarnings("restriction")
public class RemoteSearchActionTest {

  @Rule
  public RWTEnvironment environment = new RWTEnvironment();

  private RemoteObject remoteObject;
  private ActionDescriptor actionDescriptor;
  private UI ui;
  private RemoteUI uiRenderer;

  @Before
  public void setUp() {
    Display display = new Display();
    remoteObject = environment.getRemoteObject();
    ui = mock( UI.class );
    mockUI( mock( SearchActionListener.class ) );
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
    new RemoteSearchAction( ui, uiRenderer, actionDescriptor );

    environment.dispatchNotify( "Selection", new JsonObject() );

    verify( action ).execute( ui );
  }

  @Test
  public void testCallsSearchOnSearchEvent() {
    SearchAction action = spy( new TestSearchAction() );
    when( actionDescriptor.getAction() ).thenReturn( action );
    new RemoteSearchAction( ui, uiRenderer, actionDescriptor );
    JsonObject properties = new JsonObject();
    properties.add( "query", "bar" );

    environment.dispatchNotify( "Search", properties );

    verify( action ).search( "bar" );
  }

  @Test
  public void testNotifiesListenerOnSearchEvent() {
    SearchActionListener listener = mock( SearchActionListener.class );
    mockUI( listener );
    SearchAction action = spy( new TestSearchAction() );
    when( actionDescriptor.getAction() ).thenReturn( action );
    new RemoteSearchAction( ui, uiRenderer, actionDescriptor );
    JsonObject properties = new JsonObject();
    properties.add( "query", "bar" );

    environment.dispatchNotify( "Search", properties );

    verify( listener ).searched( ui, action, "bar" );
  }

  @Test
  public void testCallsGetProposalsOnModifyEvent() {
    SearchAction action = spy( new TestSearchAction() );
    when( actionDescriptor.getAction() ).thenReturn( action );
    new RemoteSearchAction( ui, uiRenderer, actionDescriptor );
    JsonObject properties = new JsonObject();
    properties.add( "query", "bar" );

    environment.dispatchNotify( "Modify", properties );

    verify( action ).modified( eq( "bar" ), any( ProposalHandler.class ) );
  }

  @Test
  public void testNotifiesListenerOnModifyEvent() {
    SearchActionListener listener = mock( SearchActionListener.class );
    mockUI( listener );
    SearchAction action = spy( new TestSearchAction() );
    when( actionDescriptor.getAction() ).thenReturn( action );
    new RemoteSearchAction( ui, uiRenderer, actionDescriptor );
    JsonObject properties = new JsonObject();
    properties.add( "query", "bar" );

    environment.dispatchNotify( "Modify", properties );

    verify( listener ).modified( ui, action, "bar" );
  }

  @Test
  public void testSendsProposals() {
    SearchAction action = spy( new TestSearchAction() );
    when( actionDescriptor.getAction() ).thenReturn( action );
    new RemoteSearchAction( ui, uiRenderer, actionDescriptor );
    List<Proposal> proposals = new ArrayList<Proposal>();
    proposals.add( new Proposal( "foo" ) );
    proposals.add( new Proposal( "bar" ) );
    JsonObject properties = new JsonObject();
    properties.add( "query", "bar" );
    environment.dispatchNotify( "Modify", properties );
    ArgumentCaptor<ProposalHandler> captor = ArgumentCaptor.forClass( ProposalHandler.class );
    verify( action ).modified( eq( "bar" ), captor.capture() );

    captor.getValue().setProposals( proposals );

    verify( remoteObject ).set( "proposals", new JsonArray().add( "foo" ).add( "bar" ) );
  }

  @Test
  public void testSendsNullProposalsAsEmptyArray() {
    SearchAction action = spy( new TestSearchAction() );
    when( actionDescriptor.getAction() ).thenReturn( action );
    new RemoteSearchAction( ui, uiRenderer, actionDescriptor );
    JsonObject properties = new JsonObject();
    properties.add( "query", "bar" );
    environment.dispatchNotify( "Modify", properties );
    ArgumentCaptor<ProposalHandler> captor = ArgumentCaptor.forClass( ProposalHandler.class );
    verify( action ).modified( eq( "bar" ), captor.capture() );

    captor.getValue().setProposals( new ArrayList<Proposal>() );

    verify( remoteObject ).set( "proposals", new JsonArray() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullProposals() {
    SearchAction action = spy( new TestSearchAction() );
    when( actionDescriptor.getAction() ).thenReturn( action );
    new RemoteSearchAction( ui, uiRenderer, actionDescriptor );
    when( actionDescriptor.getAction() ).thenReturn( action );
    JsonObject properties = new JsonObject();
    properties.add( "query", "bar" );
    environment.dispatchNotify( "Modify", properties );
    ArgumentCaptor<ProposalHandler> captor = ArgumentCaptor.forClass( ProposalHandler.class );
    verify( action ).modified( eq( "bar" ), captor.capture() );

    captor.getValue().setProposals( null );
  }

  @Test
  public void testActivatesServerPushSessionOnActivate() {
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, uiRenderer, actionDescriptor );
    SearchAction action = spy( new TestSearchAction() );
    when( actionDescriptor.getAction() ).thenReturn( action );

    remoteAction.handleCall( "activate", null );

    assertTrue( ServerPushManager.getInstance().isServerPushActive() );
  }

  @Test
  public void testDeactivatesServerPushSessionOnDeactivate() {
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, uiRenderer, actionDescriptor );
    remoteAction.handleCall( "activate", null );

    remoteAction.handleCall( "deactivate", null );

    assertFalse( ServerPushManager.getInstance().isServerPushActive() );
  }

  @Test
  public void testCallsOpenOnPropertyChange() {
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, uiRenderer, actionDescriptor );

    remoteAction.propertyChanged( METHOD_OPEN, null );

    verify( remoteObject ).call( "open", null );
  }

  @Test
  public void testCallsExecuteBeforeOpenOnPropertyChange() {
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, uiRenderer, actionDescriptor );

    remoteAction.propertyChanged( METHOD_OPEN, null );

    assertTrue( ( ( TestSearchAction ) actionDescriptor.getAction() ).wasExecuted() );
  }

  @Test
  public void testSetsQueryOnPropertyChange() {
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, uiRenderer, actionDescriptor );

    remoteAction.propertyChanged( PROPERTY_QUERY, "foo" );

    verify( remoteObject ).set( "query", "foo" );
  }

  @Test
  public void testSetsMessageOnPropertyChange() {
    RemoteSearchAction remoteAction = new RemoteSearchAction( ui, uiRenderer, actionDescriptor );

    remoteAction.propertyChanged( PROPERTY_MESSAGE, "foo" );

    verify( remoteObject ).set( "message", "foo" );
  }

  private void mockUI( SearchActionListener listener ) {
    UIConfiguration configuration = new UIConfiguration();
    configuration.addActionListener( listener );
    when( ui.getConfiguration() ).thenReturn( configuration );
  }
}
