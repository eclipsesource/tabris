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
package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.ClientDevice.Platform.ANDROID;
import static com.eclipsesource.tabris.ClientDevice.Platform.IOS;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.Client;
import org.eclipse.rap.rwt.internal.client.ClientProvider;
import org.eclipse.rap.rwt.internal.theme.ThemeUtil;

import com.eclipsesource.tabris.Bootstrapper;
import com.eclipsesource.tabris.ClientDevice;
import com.eclipsesource.tabris.ClientDevice.Platform;
import com.eclipsesource.tabris.TabrisClient;


@SuppressWarnings("restriction")
public class TabrisClientProvider implements ClientProvider {

  @Override
  public boolean accept( HttpServletRequest request ) {
    ClientDevice clientDevice = ClientDevice.getCurrent();
    Platform platform = clientDevice.getPlatform();
    boolean result = platform == ANDROID || platform == IOS;
    if( result ) {
      setThemeForPlatform( platform );
      extractClientInfo( request );
    }
    return result;
  }

  private void extractClientInfo( HttpServletRequest request ) {
  }

  private void setThemeForPlatform( Platform platform ) {
    if( platform == IOS ) {
      ThemeUtil.setCurrentThemeId( RWT.getUISession(), Bootstrapper.THEME_ID_IOS );
    } else if( platform == ANDROID ) {
      ThemeUtil.setCurrentThemeId( RWT.getUISession(), Bootstrapper.THEME_ID_ANDROID );
    }
  }

  @Override
  public Client getClient() {
    return new TabrisClient();
  }
}
