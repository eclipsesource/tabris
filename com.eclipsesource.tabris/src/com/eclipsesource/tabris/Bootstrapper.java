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

import static com.eclipsesource.tabris.internal.Constants.THEME_ID_ANDROID;
import static com.eclipsesource.tabris.internal.Constants.THEME_ID_IOS;
import static com.eclipsesource.tabris.internal.Constants.THEME_PATH_ANDROID;
import static com.eclipsesource.tabris.internal.Constants.THEME_PATH_IOS;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.internal.application.ApplicationContextImpl;
import org.eclipse.rap.rwt.internal.application.ApplicationImpl;
import org.eclipse.rap.rwt.service.ResourceLoader;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.eclipsesource.tabris.internal.ItemDataRenderer;
import com.eclipsesource.tabris.internal.TabrisClientProvider;
import com.eclipsesource.tabris.internal.bootstrap.HttpServiceTracker;


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

  private final ApplicationImpl application;
  private HttpServiceTracker httpServiceTracker;

  /**
   * @since 0.9
   */
  public Bootstrapper( Application application ) throws IllegalArgumentException {
    if( !( application instanceof ApplicationImpl ) ) {
      throw new IllegalArgumentException( "Application needs to be an ApplicationImpl" );
    }
    this.application = ( ApplicationImpl )application;
  }

  /**
   * <p>
   * Registers themes and other resources needed for the mobile clients. Needs to be called before an EntryPoint is
   * registered.
   * </p>
   *
   * @since 0.9
   */
  public void bootstrap() {
    application.addClientProvider( new TabrisClientProvider() );
    application.addPhaseListener( new ItemDataRenderer() );
    registerMobileThemes();
  }

  private static class ResourceLoaderImpl implements ResourceLoader {

    @Override
    public InputStream getResourceAsStream( String resourceName ) throws IOException {
      return Bootstrapper.class.getClassLoader().getResourceAsStream( resourceName );
    }
  }

  private void registerMobileThemes() {
    ResourceLoaderImpl resourceLoader = new ResourceLoaderImpl();
    application.addStyleSheet( THEME_ID_ANDROID, THEME_PATH_ANDROID, resourceLoader );
    application.addStyleSheet( THEME_ID_IOS, THEME_PATH_IOS, resourceLoader );
  }

  /**
   * <p>
   * Registers a servlet that returns all entrypoint registered within the passed <code>Application</code>. The
   * registered servlet can be reached by making a GET request to <code>http(s)://server:port/${path}</code>. The
   * call returns a JSON object that contains all entrypoints.
   * </p>
   *
   * @param bundle The bundle is used to call the OSGi HttpService to register the servlet.
   * @param path The path to register the lookup. Needs to start with a "/".
   *
   * @since 0.9
   */
  public void registerEntryPointLookup( Bundle bundle, String path ) {
    ApplicationContextImpl appContext = application.getApplicationContext();
    BundleContext bundleContext = bundle.getBundleContext();
    httpServiceTracker = new HttpServiceTracker( bundleContext, appContext, path );
    httpServiceTracker.open();
  }

  HttpServiceTracker getHttpServiceTracker() {
    return httpServiceTracker;
  }

}
