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

import static com.eclipsesource.tabris.internal.Constants.PROPERTY_VERSION_CHECK;
import static com.eclipsesource.tabris.internal.VersionChecker.TABRIS_SERVER_VERSION;
import static com.eclipsesource.tabris.test.util.MessageUtil.getHead;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.application.ApplicationContextImpl;
import org.eclipse.rap.rwt.internal.service.ContextProvider;
import org.eclipse.rap.rwt.internal.theme.Theme;
import org.eclipse.rap.rwt.internal.theme.ThemeManager;
import org.eclipse.rap.rwt.internal.theme.css.CssFileReader;
import org.eclipse.rap.rwt.internal.theme.css.StyleSheet;
import org.eclipse.rap.rwt.service.ResourceLoader;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.TabrisClient;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;
import com.eclipsesource.tabris.test.util.TabrisRequest;


@SuppressWarnings("restriction")
public class TabrisClientProviderTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private static final String CURRENT_THEME_ID = "org.eclipse.rap.theme.current";

  private TabrisClientProvider provider;

  @Before
  public void setUp() {
    provider = new TabrisClientProvider( new DefaultVersionCheck() );
  }

  @After
  public void tearDown() {
    System.getProperties().remove( Constants.PROPERTY_VERSION_CHECK );
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( TabrisClientProvider.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyServerId() {
    new TabrisClientProvider( new DefaultVersionCheck(), "" );
  }

  @Test
  public void testAcceptForAndroid() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_ANDROID );

    assertTrue( provider.accept( request ) );
  }

  @Test
  public void testAcceptForIOS() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_IOS );

    assertTrue( provider.accept( request ) );
  }

  @Test
  public void testDoesNotAcceptForWeb() {
    assertFalse( provider.accept( RWT.getRequest() ) );
  }

  @Test
  public void testReturnsTabrisClient() {
    assertTrue( provider.getClient() instanceof TabrisClient );
  }

  @Test
  public void testUsesIOSTheme() throws IOException {
    registerTheme( Constants.THEME_ID_IOS );
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_IOS );

    provider.accept( request );

    String currentTheme = ( String )RWT.getUISession().getAttribute( CURRENT_THEME_ID );
    assertEquals( Constants.THEME_ID_IOS, currentTheme );
  }

  @Test
  public void testUsesIOS6Theme() throws IOException {
    registerTheme( Constants.THEME_ID_IOS6 );
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_IOS + " OS 6.1.2" );

    provider.accept( request );

    String currentTheme = ( String )RWT.getUISession().getAttribute( CURRENT_THEME_ID );
    assertEquals( Constants.THEME_ID_IOS6, currentTheme );
  }

  @Test
  public void testUsesAndroidTheme() throws IOException {
    registerTheme( Constants.THEME_ID_ANDROID );
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_ANDROID );

    provider.accept( request );

    String currentTheme = ( String )RWT.getUISession().getAttribute( CURRENT_THEME_ID );
    assertEquals( Constants.THEME_ID_ANDROID, currentTheme );
  }

  private void registerTheme( String themeId ) throws IOException {
    ResourceLoader resourceLoader = new ResourceLoader() {

      @Override
      public InputStream getResourceAsStream( String resourceName ) throws IOException {
        return new ByteArrayInputStream( "".getBytes() );
      }
    };
    StyleSheet styleSheet = CssFileReader.readStyleSheet( "", resourceLoader );
    Theme theme = new Theme( themeId, "unknown", styleSheet );
    ApplicationContextImpl applicationContext = ContextProvider.getContext().getApplicationContext();
    environment.resetThemes();
    ThemeManager themeManager = applicationContext.getThemeManager();
    themeManager.registerTheme( theme );
  }

  @Test
  public void testSetsServerIdAsHeaderForAndroid() {
    provider = new TabrisClientProvider( new DefaultVersionCheck(), "foo" );
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_ANDROID );

    provider.accept( request );

    String header = RWT.getResponse().getHeader( "com.eclipsesource.tabris.server.id" );
    assertEquals( "foo", header );
  }

  @Test
  public void testSetsServerIdAsHeaderForIOS() {
    provider = new TabrisClientProvider( new DefaultVersionCheck(), "foo" );
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_IOS );

    provider.accept( request );

    String header = RWT.getResponse().getHeader( "com.eclipsesource.tabris.server.id" );
    assertEquals( "foo", header );
  }

  @Test
  public void testSetsServerIdAsHeaderForAndroidOnlyIfSet() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_ANDROID );

    provider.accept( request );

    String header = RWT.getResponse().getHeader( "com.eclipsesource.tabris.server.id" );
    assertNull( header );
  }

  @Test
  public void testSetsServerIdAsHeaderForIOSOnlyIfSet() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_IOS );

    provider.accept( request );

    String header = RWT.getResponse().getHeader( "com.eclipsesource.tabris.server.id" );
    assertNull( header );
  }

  @Test
  public void testSetsNoServerIdAsHeaderForWeb() {
    provider = new TabrisClientProvider( new DefaultVersionCheck(), "foo" );
    TabrisRequest request = environment.getRequest();

    provider.accept( request );

    String header = RWT.getResponse().getHeader( "com.eclipsesource.tabris.server.id" );
    assertNull( header );
  }

  @Test
  public void testSets412ForIncompatibleVersion() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_IOS + "/42.1.0 (foo)" );

    provider.accept( request );

    int status = RWT.getResponse().getStatus();
    assertEquals( 412, status );
  }

  @Test
  public void testSetsErrorHeadAttribute() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_IOS + "/42.1.0 (foo)" );

    provider.accept( request );

    JsonObject head = getHead();
    String error = head.get( "error" ).asString();
    String serverVersion = TABRIS_SERVER_VERSION.substring( 0, TABRIS_SERVER_VERSION.length() - 2 );
    assertEquals( "Incompatible Tabris Versions:\nClient 42.1 vs. Server " + serverVersion, error );
  }

  @Test
  public void testRespectsSystemPropertyForVersionCheck() {
    System.setProperty( PROPERTY_VERSION_CHECK, "false" );
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_IOS + "/42.1.0 (foo)" );

    provider.accept( request );

    int status = RWT.getResponse().getStatus();
    assertNotEquals( 412, status );
  }

}
