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

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.rap.rwt.internal.protocol.IClientObject;

import com.eclipsesource.tabris.internal.AbstractObjectSynchronizer;
import com.eclipsesource.tabris.xcallbackurl.XCallback;
import com.eclipsesource.tabris.xcallbackurl.XCallbackConfiguration;
import com.eclipsesource.tabris.xcallbackurl.XCallbackURL;


@SuppressWarnings("restriction")
public class XCallbackURLSynchronizer extends AbstractObjectSynchronizer<XCallbackURL> {

  static final String CALLBACK_ID = "tabris.xCallbackUrl";
  static final String X_CALLBACK_RESULT = "xCallbackResult";
  static final String RESULT_SUCCESS = "success";
  static final String RESULT_CANCEL = "cancel";
  static final String RESULT_ERROR = "error";
  static final String ERROR_CODE_PROPERTY = "xCallbackErrorCode";
  static final String ERROR_MESSAGE_PROPERTY = "xCallbackErrorMessage";
  static final String CALL_PROPERTY = "wants-to-call";
  static final String CALL_METHOD = "call";
  static final String ACTION_PARAMETERS = "actionParameters";
  static final String ACTION = "action";
  static final String X_SOURCE = "x-source";
  static final String TARGET_APP = "targetApp";

  public XCallbackURLSynchronizer( XCallbackURL xCallbackUrl ) {
    super( xCallbackUrl );
  }

  @Override
  protected void renderInitialization( IClientObject clientObject, XCallbackURL callback ) {
    XCallbackConfiguration configruation = callback.getConfigruation();
    clientObject.create( CALLBACK_ID );
    clientObject.set( TARGET_APP, configruation.getTargetApp() );
    clientObject.set( X_SOURCE, configruation.getXSource() );
    clientObject.set( ACTION, configruation.getAction() );
    String[] parameters = getJoinedParameters( configruation );
    clientObject.set( ACTION_PARAMETERS, parameters );
    syncCallMethod( clientObject, callback );
  }

  private String[] getJoinedParameters( XCallbackConfiguration configruation ) {
    Map<String, String> actionParameters = configruation.getActionParameters();
    Set<Entry<String, String>> entrySet = actionParameters.entrySet();
    String[] parameters = new String[ entrySet.size() ];
    int count = 0;
    for( Entry<String, String> entry : entrySet ) {
      parameters[ count ] = entry.getKey() + "=" + entry.getValue();
      count++;
    }
    return parameters;
  }

  private void syncCallMethod( IClientObject clientObject, XCallbackURL callback ) {
    XCallbackSyncAdapter syncAdapter = callback.getAdapter( XCallbackSyncAdapter.class );
    if( syncAdapter.wantsToCall() ) {
      clientObject.call( CALL_METHOD, null );
      syncAdapter.setWantsToCall( false );
    }
  }

  @Override
  protected void readData( XCallbackURL object ) { 
    // do nothing. Read is handled in processAction
  }

  @Override
  protected void preserveValues( XCallbackURL callback ) {
    XCallbackSyncAdapter syncAdapter = callback.getAdapter( XCallbackSyncAdapter.class );
    preserveProperty( CALL_PROPERTY, syncAdapter.wantsToCall() );
  }

  @Override
  protected void processAction( XCallbackURL callback ) {
    String xCallbackResult = readPropertyValue( X_CALLBACK_RESULT );
    if( xCallbackResult != null ) {
      XCallbackSyncAdapter syncAdapter = callback.getAdapter( XCallbackSyncAdapter.class );
      XCallback xCallback = syncAdapter.getCallback();
      if( xCallback != null ) {
        dispatchCallbackResult( xCallbackResult, xCallback );
        syncAdapter.setCallback( null );
      }
    }
  }

  private void dispatchCallbackResult( String xCallbackResult, XCallback xCallback ) {
    if( xCallbackResult.equals( RESULT_SUCCESS ) ) {
      xCallback.onSuccess();
    } else if( xCallbackResult.equals( RESULT_CANCEL ) ) {
      xCallback.onCancel();
    } else if( xCallbackResult.equals( RESULT_ERROR ) ) {
      String errorMsg = readPropertyValue( ERROR_MESSAGE_PROPERTY );
      String errorCode = readPropertyValue( ERROR_CODE_PROPERTY );
      xCallback.onError( Integer.parseInt( errorCode ), errorMsg );
    }
  }

  @Override
  protected void renderChanges( XCallbackURL callback ) {
    XCallbackSyncAdapter syncAdapter = callback.getAdapter( XCallbackSyncAdapter.class );
    if( hasPropertyChanged( CALL_PROPERTY, Boolean.valueOf( syncAdapter.wantsToCall() ) ) ) {
      getClientObject().call( CALL_METHOD, null );
      syncAdapter.setWantsToCall( false );
    }
  }


}
