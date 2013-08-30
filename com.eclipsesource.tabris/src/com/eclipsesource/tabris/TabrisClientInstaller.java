/*******************************************************************************
 * Copyright (c) 2012,2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris;

import static com.eclipsesource.tabris.internal.Constants.INDEX_JSON;
import static com.eclipsesource.tabris.internal.Constants.THEME_ID_ANDROID;
import static com.eclipsesource.tabris.internal.Constants.THEME_ID_IOS;
import static com.eclipsesource.tabris.internal.Constants.THEME_PATH_ANDROID;
import static com.eclipsesource.tabris.internal.Constants.THEME_PATH_IOS;
import static com.eclipsesource.tabris.internal.Constants.THEME_PATH_WEB;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.client.Client;
import org.eclipse.rap.rwt.internal.application.ApplicationImpl;
import org.eclipse.rap.rwt.service.ResourceLoader;

import com.eclipsesource.tabris.internal.TabrisClientProvider;
import com.eclipsesource.tabris.internal.TabrisResourceLoader;


/**
 * <p>
 * Installs a Tabris specific client to provide Tabris specific client services. An instance of the {@link Client} can
 * be obtained using {@link RWT#getClient()}.
 * </p>
 *
 * @since 1.0
 */
@SuppressWarnings("restriction")
public class TabrisClientInstaller {

  private static class ResourceLoaderImpl implements ResourceLoader {

    @Override
    public InputStream getResourceAsStream( String resourceName ) throws IOException {
      return TabrisClientInstaller.class.getClassLoader().getResourceAsStream( resourceName );
    }
  }

  /**
   * <p>
   * Registers themes and other resources needed for the Tabris clients.
   * </p>
   *
   * @since 1.0
   */
  public static void install( Application application ) {
    ApplicationImpl applicationImpl = ( ApplicationImpl )application;
    applicationImpl.addClientProvider( new TabrisClientProvider() );
    registerCompatibilityThemes( application );
    registerResourceLoader( applicationImpl );
  }

  private static void registerCompatibilityThemes( Application application ) {
    ResourceLoaderImpl resourceLoader = new ResourceLoaderImpl();
    application.addStyleSheet( THEME_ID_ANDROID, THEME_PATH_ANDROID, resourceLoader );
    application.addStyleSheet( THEME_ID_IOS, THEME_PATH_IOS, resourceLoader );
    application.addStyleSheet( RWT.DEFAULT_THEME_ID, THEME_PATH_WEB, resourceLoader );
  }

  private static void registerResourceLoader( ApplicationImpl application ) {
    TabrisResourceLoader tabrisLoader = new TabrisResourceLoader( application.getApplicationContext() );
    application.getApplicationContext().getResourceRegistry().add( INDEX_JSON, tabrisLoader );
  }

  private TabrisClientInstaller() {
    // prevent instantiation
  }
}
