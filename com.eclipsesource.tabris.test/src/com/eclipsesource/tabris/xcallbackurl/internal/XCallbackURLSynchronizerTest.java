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
package com.eclipsesource.tabris.xcallbackurl.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.protocol.IClientObjectAdapter;
import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rap.rwt.testfixture.Message;
import org.eclipse.rap.rwt.testfixture.Message.CreateOperation;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.xcallbackurl.XCallback;
import com.eclipsesource.tabris.xcallbackurl.XCallbackConfiguration;
import com.eclipsesource.tabris.xcallbackurl.XCallbackURL;


@SuppressWarnings("restriction")
public class XCallbackURLSynchronizerTest {
  
  private XCallbackURLSynchronizer xCallbackURLSynchronizer;
  private XCallbackURL xCallbackUrl;

  @Before
  public void setUp() {
    Fixture.setUp();
    XCallbackConfiguration configuration = new XCallbackConfiguration( "target", "action", "source" );
    configuration.addActionParameter( "foo", "bar" );
    xCallbackUrl = new XCallbackURL( configuration );
    xCallbackURLSynchronizer = new XCallbackURLSynchronizer( xCallbackUrl );
  }
  
  @After
  public void tearDown() {
    Fixture.tearDown();
  }
  
  @Test
  public void testRenderInitialization() throws JSONException {
    String id = xCallbackUrl.getAdapter( IClientObjectAdapter.class ).getId();
    
    xCallbackURLSynchronizer.renderInitialization( xCallbackURLSynchronizer.getClientObject(), xCallbackUrl );
    
    Message message = Fixture.getProtocolMessage();
    CreateOperation createOperation = message.findCreateOperation( id );
    assertEquals( XCallbackURLSynchronizer.CALLBACK_ID, createOperation.getType() );
    assertEquals( "target", createOperation.getProperty( XCallbackURLSynchronizer.TARGET_APP ) );
    assertEquals( "action", createOperation.getProperty( XCallbackURLSynchronizer.ACTION ) );
    assertEquals( "source", createOperation.getProperty( XCallbackURLSynchronizer.X_SOURCE ) );
    JSONArray parameters = ( JSONArray )createOperation.getProperty( XCallbackURLSynchronizer.ACTION_PARAMETERS );
    assertEquals( 1, parameters.length() );
    assertEquals( "foo=bar", parameters.get( 0 ) );
  }
  
  @Test
  public void testRenderInitializationWithCall() {
    xCallbackUrl.call();
    String id = xCallbackUrl.getAdapter( IClientObjectAdapter.class ).getId();
    
    xCallbackURLSynchronizer.renderInitialization( xCallbackURLSynchronizer.getClientObject(), xCallbackUrl );
    
    Message message = Fixture.getProtocolMessage();
    assertNotNull( message.findCallOperation( id, XCallbackURLSynchronizer.CALL_METHOD ) );
    XCallbackSyncAdapter adapter = xCallbackUrl.getAdapter( XCallbackSyncAdapter.class );
    assertFalse( adapter.wantsToCall() );
  }
  
  @Test
  public void testPreservesCall() throws Throwable {
    xCallbackUrl.call();
    String id = xCallbackUrl.getAdapter( IClientObjectAdapter.class ).getId();
    Fixture.fakePhase( PhaseId.READ_DATA );
    
    xCallbackURLSynchronizer.preserveValues( xCallbackUrl );

    Object property = RWT.getServiceStore().getAttribute( id + "." + XCallbackURLSynchronizer.CALL_PROPERTY );
    assertTrue( ( ( Boolean )property ).booleanValue() ); 
  }
  
  @Test
  public void testProcessActionWithoutCallbackDoesNotThrowException() {
    xCallbackUrl.call();
    String id = xCallbackUrl.getAdapter( IClientObjectAdapter.class ).getId();
    
    Fixture.fakeRequestParam( id + "." + XCallbackURLSynchronizer.X_CALLBACK_RESULT, XCallbackURLSynchronizer.RESULT_SUCCESS );
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    xCallbackURLSynchronizer.processAction( xCallbackUrl );
  }
  
  @Test
  public void testProcessActionWithoutPropertyDoesNotThrowException() {
    XCallback xCallback = mock( XCallback.class );
    xCallbackUrl.call( xCallback );
    
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    xCallbackURLSynchronizer.processAction( xCallbackUrl );
    
    verify( xCallback, never() ).onSuccess();
    verify( xCallback, never() ).onCancel();
    verify( xCallback, never() ).onError( anyInt(), anyString() );
  }
  
  @Test
  public void testOnSuccess() {
    XCallback xCallback = mock( XCallback.class );
    xCallbackUrl.call( xCallback );
    String id = xCallbackUrl.getAdapter( IClientObjectAdapter.class ).getId();

    Fixture.fakeRequestParam( id + "." + XCallbackURLSynchronizer.X_CALLBACK_RESULT, XCallbackURLSynchronizer.RESULT_SUCCESS );
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    xCallbackURLSynchronizer.processAction( xCallbackUrl );
    
    verify( xCallback ).onSuccess();
    testResetsCallback();
  }
  
  @Test
  public void testOnCancel() {
    XCallback xCallback = mock( XCallback.class );
    xCallbackUrl.call( xCallback );
    String id = xCallbackUrl.getAdapter( IClientObjectAdapter.class ).getId();
    
    Fixture.fakeRequestParam( id + "." + XCallbackURLSynchronizer.X_CALLBACK_RESULT, XCallbackURLSynchronizer.RESULT_CANCEL );
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    xCallbackURLSynchronizer.processAction( xCallbackUrl );
    
    verify( xCallback ).onCancel();
    testResetsCallback();
  }
  
  @Test
  public void testOnError() {
    XCallback xCallback = mock( XCallback.class );
    xCallbackUrl.call( xCallback );
    String id = xCallbackUrl.getAdapter( IClientObjectAdapter.class ).getId();
    
    Fixture.fakeRequestParam( id + "." + XCallbackURLSynchronizer.X_CALLBACK_RESULT, XCallbackURLSynchronizer.RESULT_ERROR );
    Fixture.fakeRequestParam( id + "." + XCallbackURLSynchronizer.ERROR_CODE_PROPERTY, "404" );
    Fixture.fakeRequestParam( id + "." + XCallbackURLSynchronizer.ERROR_MESSAGE_PROPERTY, "Not Found" );
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    xCallbackURLSynchronizer.processAction( xCallbackUrl );
    
    verify( xCallback ).onError( eq( 404 ), eq( "Not Found" ) );
    testResetsCallback();
  }

  private void testResetsCallback() {
    XCallbackSyncAdapter adapter = xCallbackUrl.getAdapter( XCallbackSyncAdapter.class );
    assertNull( adapter.getCallback() );
  }
  
  @Test
  public void testRenderChangesWithCall() {
    Fixture.fakePhase( PhaseId.PREPARE_UI_ROOT );
    String id = xCallbackUrl.getAdapter( IClientObjectAdapter.class ).getId();
    xCallbackURLSynchronizer.renderInitialization( xCallbackURLSynchronizer.getClientObject(), xCallbackUrl );
    Fixture.fakePhase( PhaseId.READ_DATA );
    xCallbackURLSynchronizer.preserveValues( xCallbackUrl );
    xCallbackUrl.call();
    
    Fixture.fakePhase( PhaseId.RENDER );
    xCallbackURLSynchronizer.renderChanges( xCallbackUrl );
    
    Message message = Fixture.getProtocolMessage();
    assertNotNull( message.findCallOperation( id, XCallbackURLSynchronizer.CALL_METHOD ) );
  }
  
  @Test
  public void testRenderChangesWithoutCall() {
    Fixture.fakePhase( PhaseId.PREPARE_UI_ROOT );
    String id = xCallbackUrl.getAdapter( IClientObjectAdapter.class ).getId();
    xCallbackURLSynchronizer.renderInitialization( xCallbackURLSynchronizer.getClientObject(), xCallbackUrl );
    Fixture.fakePhase( PhaseId.READ_DATA );
    xCallbackURLSynchronizer.preserveValues( xCallbackUrl );
    
    Fixture.fakePhase( PhaseId.RENDER );
    xCallbackURLSynchronizer.renderChanges( xCallbackUrl );
    
    Message message = Fixture.getProtocolMessage();
    assertNull( message.findCallOperation( id, XCallbackURLSynchronizer.CALL_METHOD ) );
  }
  
  @Test
  public void testReadDataDoesNotThrowException() {
    xCallbackURLSynchronizer.readData( xCallbackUrl );
  }
}
