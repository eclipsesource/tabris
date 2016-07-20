/*******************************************************************************
 * Copyright (c) 2012, 2016 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.INDEX_JSON;
import static com.eclipsesource.tabris.internal.Constants.THEME_ID_ANDROID;
import static com.eclipsesource.tabris.internal.Constants.THEME_ID_IOS;
import static com.eclipsesource.tabris.internal.Constants.THEME_ID_IOS6;
import static com.eclipsesource.tabris.internal.Constants.THEME_ID_SWT;
import static com.eclipsesource.tabris.internal.Constants.THEME_ID_WINDOWS;
import static com.eclipsesource.tabris.internal.Constants.THEME_PATH_ANDROID;
import static com.eclipsesource.tabris.internal.Constants.THEME_PATH_IOS;
import static com.eclipsesource.tabris.internal.Constants.THEME_PATH_IOS6;
import static com.eclipsesource.tabris.internal.Constants.THEME_PATH_SWT;
import static com.eclipsesource.tabris.internal.Constants.THEME_PATH_WEB;
import static com.eclipsesource.tabris.internal.Constants.THEME_PATH_WINDOWS;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.client.Client;
import org.eclipse.rap.rwt.internal.application.ApplicationImpl;
import org.eclipse.rap.rwt.service.ResourceLoader;

import com.eclipsesource.tabris.internal.DefaultVersionCheck;
import com.eclipsesource.tabris.internal.TabrisClientProvider;
import com.eclipsesource.tabris.internal.TabrisResourceLoader;
import com.eclipsesource.tabris.internal.TabrisSWTClientProvider;


/**
 * <p>
 * Installs a Tabris specific client to provide Tabris specific client services. An instance of the {@link Client} can
 * be obtained using {@link RWT#getClient()}.
 * </p>
 *
 * @see TabrisClient
 *
 * @since 1.0
 */
@SuppressWarnings("restriction")
public class TabrisClientInstaller {

  static VersionCheck VERSION_CHECK = new DefaultVersionCheck();

  private static class ResourceLoaderImpl implements ResourceLoader {

    @Override
    public InputStream getResourceAsStream( String resourceName ) throws IOException {
      return TabrisClientInstaller.class.getClassLoader().getResourceAsStream( resourceName );
    }
  }

  /**
   * <p>
   * Registers themes and other resources needed for the Tabris clients with no required serverId.
   * </p>
   *
   * @since 1.0
   */
  public static void install( Application application ) {
    TabrisClientInstaller.install( application, null );
  }

  /**
   * <p>
   * Registers themes and other resources needed for the Tabris clients. Also sets the server id that a client
   * can use to verify a server connection.
   * </p>
   *
   * @param serverId the id for the server. May be <code>null</code> but not empty.
   *
   * @since 1.3
   */
  public static void install( Application application, String serverId ) {
    verifyServerId( serverId );
    ApplicationImpl applicationImpl = ( ApplicationImpl )application;
    applicationImpl.addClientProvider( new TabrisClientProvider( VERSION_CHECK, serverId ) );
    applicationImpl.addClientProvider( new TabrisSWTClientProvider() );
    registerCompatibilityThemes( application );
    registerResourceLoader( applicationImpl );
  }

  private static void verifyServerId( String serverId ) {
    if( serverId != null ) {
      when( serverId.isEmpty() ).throwIllegalArgument( "ServerId must not be empty" );
    }
  }

  private static void registerCompatibilityThemes( Application application ) {
    ResourceLoaderImpl resourceLoader = new ResourceLoaderImpl();
    application.addStyleSheet( THEME_ID_ANDROID, THEME_PATH_ANDROID, resourceLoader );
    application.addStyleSheet( THEME_ID_SWT, THEME_PATH_SWT, resourceLoader );
    application.addStyleSheet( THEME_ID_IOS6, THEME_PATH_IOS6, resourceLoader );
    application.addStyleSheet( THEME_ID_IOS, THEME_PATH_IOS, resourceLoader );
    application.addStyleSheet( THEME_ID_WINDOWS, THEME_PATH_WINDOWS, resourceLoader );
    application.addStyleSheet( RWT.DEFAULT_THEME_ID, THEME_PATH_WEB, resourceLoader );
  }

  private static void registerResourceLoader( ApplicationImpl application ) {
    TabrisResourceLoader tabrisLoader = new TabrisResourceLoader( application.getApplicationContext() );
    application.getApplicationContext().getResourceRegistry().add( INDEX_JSON, tabrisLoader );
  }

  /**
   * <p>
   * Sets the {@link VersionCheck} for the {@link Application} to install.
   * </p>
   *
   * @param versionCheck the {@link VersionCheck} to use. Must not be <code>null</code>.
   *
   * @since 1.4
   */
  public static void setVersionCheck( VersionCheck versionCheck ) {
    whenNull( versionCheck ).throwIllegalArgument( "versionCheck must not be null" );
    VERSION_CHECK = versionCheck;
  }

  private TabrisClientInstaller() {
    // prevent instantiation
  }
}
