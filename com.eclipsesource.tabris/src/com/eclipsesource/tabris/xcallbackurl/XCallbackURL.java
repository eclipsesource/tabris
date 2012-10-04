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
package com.eclipsesource.tabris.xcallbackurl;

import org.eclipse.rap.rwt.Adaptable;
import org.eclipse.rap.rwt.internal.protocol.ClientObjectAdapter;
import org.eclipse.rap.rwt.internal.protocol.IClientObjectAdapter;

import com.eclipsesource.tabris.xcallbackurl.internal.XCallbackSyncAdapter;
import com.eclipsesource.tabris.xcallbackurl.internal.XCallbackURLSynchronizer;


/**
 * @since 0.8
 */
@SuppressWarnings( "restriction" )
public class XCallbackURL implements Adaptable {
  
  private final XCallbackConfiguration configuration;
  private final ClientObjectAdapter clientObjectAdapter;
  private final XCallbackSyncAdapter xCallbackSyncAdapter;

  public XCallbackURL( XCallbackConfiguration configuration ) {
    checkConfiguration( configuration );
    this.configuration = configuration;
    this.clientObjectAdapter = new ClientObjectAdapter( "x" );
    this.xCallbackSyncAdapter = new XCallbackSyncAdapter();
    new XCallbackURLSynchronizer( this );
  }
  
  private void checkConfiguration( XCallbackConfiguration configuration ) {
    if( configuration == null ) {
      throw new IllegalArgumentException( "XCallbackConfiguration must not be null" );
    }
  }

  /**
   * one way.
   */
  public void call() {
    xCallbackSyncAdapter.setWantsToCall( true );
    xCallbackSyncAdapter.setCallback( null );
  }

  /**
   * two way.
   */
  public void call( XCallback callback ) {
    xCallbackSyncAdapter.setWantsToCall( true );
    xCallbackSyncAdapter.setCallback( callback );
  }
  
  public XCallbackConfiguration getConfigruation() {
    return configuration;
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public <T> T getAdapter( Class<T> adapter ) {
    T result = null;
    if( adapter == IClientObjectAdapter.class ) {
      result = ( T )clientObjectAdapter;
    } else if( adapter == XCallbackSyncAdapter.class ) {
      result = ( T )xCallbackSyncAdapter;
    }
    return result;
  }
  
}
