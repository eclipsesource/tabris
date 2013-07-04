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
package com.eclipsesource.tabris.xcallbackurl;

import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.EVENT_ON_CANCEL;
import static com.eclipsesource.tabris.internal.Constants.EVENT_ON_ERROR;
import static com.eclipsesource.tabris.internal.Constants.EVENT_ON_SUCCESS;
import static com.eclipsesource.tabris.internal.Constants.METHOD_CALL;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ACTION_PARAMETERS;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ERROR_CODE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ERROR_MESSAGE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PARAMETERS;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_TARGET_ACTION;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_TARGET_SCHEME;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_XCANCEL_NAME;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_XERROR_NAME;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_XSOURCE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_XSOURCE_NAME;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_XSUCCESS_NAME;
import static com.eclipsesource.tabris.internal.Constants.TYPE_XCALLBACK;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;


/**
 * see http://x-callback-url.com/specifications/
 * @since 1.1
 */
public class XCallback implements Serializable {

  private final RemoteObject remoteObject;
  private final List<XCallbackListener> listeners;

  public XCallback( XCallbackConfiguration configuration ) {
    whenNull( configuration ).throwIllegalArgument( "Configuration must not be null" );
    this.listeners = new ArrayList<XCallbackListener>();
    this.remoteObject = RWT.getUISession().getConnection().createRemoteObject( TYPE_XCALLBACK );
    remoteObject.setHandler( createOperationHandler() );
    sendConfiguration( configuration );
  }

  private AbstractOperationHandler createOperationHandler() {
    return new AbstractOperationHandler() {

      @Override
      public void handleNotify( String event, JsonObject properties ) {
        if( event.equals( EVENT_ON_SUCCESS ) ) {
          dispatchOnSuccess( getParameter( properties ) );
        } else if( event.equals( EVENT_ON_ERROR ) ) {
          dispatchOnError( properties.get( PROPERTY_ERROR_CODE ).asString(),
                           properties.get( PROPERTY_ERROR_MESSAGE ).asString() );
        } else if( event.equals( EVENT_ON_CANCEL ) ) {
          dispatchOnCancel();
        }
      }
    };
  }

  private Map<String, String> getParameter( JsonObject properties ) {
    Map<String, String> parameter = new HashMap<String, String>();
    if( properties != null && properties.get( PROPERTY_PARAMETERS ) != null ) {
      JsonObject object = properties.get( PROPERTY_PARAMETERS ).asObject();
      List<String> names = object.names();
      for( String name : names ) {
        parameter.put( name, object.get( name ).asString() );
      }
    }
    return parameter;
  }

  private void sendConfiguration( XCallbackConfiguration configuration ) {
    remoteObject.set( PROPERTY_TARGET_SCHEME, configuration.getTargetScheme() );
    remoteObject.set( PROPERTY_TARGET_ACTION, configuration.getTargetAction() );
    sendOptionalParameter( configuration );
  }

  private void sendOptionalParameter( XCallbackConfiguration configuration ) {
    sendXSource( configuration );
    sendActionParameter( configuration );
    sendXSourceName( configuration );
    sendXSuccessName( configuration );
    sendXErrorName( configuration );
    sendXCancelName( configuration );
  }

  private void sendXSource( XCallbackConfiguration configuration ) {
    String xSource = configuration.getXSource();
    if( xSource != null ) {
      remoteObject.set( PROPERTY_XSOURCE, xSource );
    }
  }

  private void sendActionParameter( XCallbackConfiguration configuration ) {
    Map<String, String> actionParameters = configuration.getActionParameters();
    if( actionParameters != null && !actionParameters.isEmpty() ) {
      JsonObject xActionParameter = new JsonObject();
      for( Entry<String, String> actionParameter : actionParameters.entrySet() ) {
        xActionParameter.add( actionParameter.getKey(), actionParameter.getValue() );
      }
      remoteObject.set( PROPERTY_ACTION_PARAMETERS, xActionParameter );
    }
  }

  private void sendXSourceName( XCallbackConfiguration configuration ) {
    String xSourceName = configuration.getXSourceName();
    if( xSourceName != null ) {
      remoteObject.set( PROPERTY_XSOURCE_NAME, xSourceName );
    }
  }

  private void sendXSuccessName( XCallbackConfiguration configuration ) {
    String xSuccessName = configuration.getXSuccessName();
    if( xSuccessName != null ) {
      remoteObject.set( PROPERTY_XSUCCESS_NAME, xSuccessName );
    }
  }

  private void sendXErrorName( XCallbackConfiguration configuration ) {
    String xErrorName = configuration.getXErrorName();
    if( xErrorName != null ) {
      remoteObject.set( PROPERTY_XERROR_NAME, xErrorName );
    }
  }

  private void sendXCancelName( XCallbackConfiguration configuration ) {
    String xCancelName = configuration.getXCancelName();
    if( xCancelName != null ) {
      remoteObject.set( PROPERTY_XCANCEL_NAME, xCancelName );
    }
  }

  private void dispatchOnSuccess( Map<String, String> parameter ) {
    for( XCallbackListener listener : listeners ) {
      listener.onSuccess( parameter );
    }
  }

  private void dispatchOnError( String errorCode, String errorMessage ) {
    for( XCallbackListener listener : listeners ) {
      listener.onError( errorCode, errorMessage );
    }
  }

  private void dispatchOnCancel() {
    for( XCallbackListener listener : listeners ) {
      listener.onCancel();
    }
  }

  public void addXCallbackListener( XCallbackListener listener ) {
    whenNull( listener ).throwIllegalArgument( "Listener must not be null" );
    listeners.add( listener );
  }

  public void removeXCallbackListener( XCallbackListener listener ) {
    whenNull( listener ).throwIllegalArgument( "Listener must not be null" );
    listeners.remove( listener );
  }

  public void call() {
    remoteObject.call( METHOD_CALL, null );
  }

  RemoteObject getRemoteObject() {
    return remoteObject;
  }

}