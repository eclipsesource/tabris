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
package com.eclipsesource.tabris;

import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.eclipse.rap.rwt.internal.application.ApplicationImpl;

import com.eclipsesource.tabris.internal.bootstrap.ApplicationWrapper;
import com.eclipsesource.tabris.internal.bootstrap.ProxyApplicationConfiguration;
import com.eclipsesource.tabris.internal.bootstrap.ThemePhaseListener;


@SuppressWarnings("restriction")
/**
 * <p>The <code>Bootstrapper</code> needs to be called before registering EntryPoints to make Tabris work.
 * It registers themes and other resources that are needed to make mobile clients work.
 * </p>
 * <p>
 * Typically the bootstrap method is called within the <code>ApplicationConfiguration#configure</code> method:
 * <pre>
 * public class ExampleConfiguration implements ApplicationConfiguration {
 *
 *   public void configure( Application application ) {
 *     Bootstrapper.bootstrap( application );
 *     configuration.addEntryPoint( &quot;/example&quot;, ExampleEntryPoint.class, null );
 *   }
 * }
 * </pre>
 * </p> 
 * 
 * @see ApplicationConfiguration
 * @since 0.6
 */
public class Bootstrapper {
  
  public static final String THEME_ID_IOS = "com.eclipsesource.rap.mobile.theme.ios";
  public static final String THEME_ID_ANDROID = "com.eclipsesource.rap.mobile.theme.android";
  private static final String THEME_PATH_IOS = "theme/ios.css";
  private static final String THEME_PATH_ANDROID = "theme/theme-android-holo.css";
  
  /**
   * <p>
   * Registers themes and other resources needed for the mobile clients. Needs to be called before an EntryPoint is 
   * registered.
   * </p>
   */
  public static void bootstrap( Application application ) {
    ApplicationImpl config = chooseApplication( application );
    config.addPhaseListener( new ThemePhaseListener() );
    registerMobileThemes( config );
  }
  
  private static ApplicationImpl chooseApplication( Application application ) {
    ApplicationImpl app = ( ApplicationImpl )application;
    ApplicationConfiguration configuration = app.getAdapter( ApplicationConfiguration.class );
    if( !( configuration instanceof ProxyApplicationConfiguration ) ) {
      ProxyApplicationConfiguration proxy = new ProxyApplicationConfiguration( configuration );
      app = new ApplicationWrapper( app, proxy );
    }
    return app;
  }
  
  private static void registerMobileThemes( Application config ) {
    config.addStyleSheet( THEME_ID_ANDROID, THEME_PATH_ANDROID );
    config.addStyleSheet( THEME_ID_IOS, THEME_PATH_IOS );
  }

  private Bootstrapper() {
    // prevent instantiation
  }
}
