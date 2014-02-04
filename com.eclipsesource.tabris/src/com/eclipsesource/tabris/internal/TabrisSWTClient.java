/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;

import static org.eclipse.rap.rwt.SingletonUtil.getSessionInstance;

import org.eclipse.rap.rwt.client.service.BrowserNavigation;
import org.eclipse.rap.rwt.client.service.ClientService;
import org.eclipse.rap.rwt.internal.client.BrowserNavigationImpl;

import com.eclipsesource.tabris.TabrisClient;

@SuppressWarnings("restriction")
public class TabrisSWTClient implements TabrisClient {

  public TabrisSWTClient() {
    getService( TableItemHeightService.class );
    getService( BrowserNavigation.class );
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends ClientService> T getService( Class<T> type ) {
    T result = null;
    if( type == TableItemHeightService.class ) {
      result = ( T )getSessionInstance( TableItemHeightService.class );
    } else if( type == BrowserNavigation.class ) {
      result = ( T )getSessionInstance( BrowserNavigationImpl.class );
    }
    return result;
  }
}
