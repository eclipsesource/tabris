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
import static com.eclipsesource.tabris.internal.VersionCheck.TABRIS_SERVER_VERSION;
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
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rap.rwt.testfixture.TestRequest;
import org.eclipse.rap.rwt.testfixture.internal.engine.ThemeManagerHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.TabrisClient;
import com.eclipsesource.tabris.test.RWTEnvironment;


@SuppressWarnings("restriction")
public class TabrisClientProviderTest {

  @Rule
  public RWTEnvironment environment = new RWTEnvironment();

  private static final String CURRENT_THEME_ID = "org.eclipse.rap.theme.current";

  private TabrisClientProvider provider;

  @Before
  public void setUp() {
    provider = new TabrisClientProvider();
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
    new TabrisClientProvider( "" );
  }

  @Test
  public void testAcceptForAndroid() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_ANDROID );

    assertTrue( provider.accept( request ) );
  }

  @Test
  public void testAcceptForIOS() {
    TestRequest request = ( TestRequest )RWT.getRequest();
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
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_IOS );

    provider.accept( request );

    String currentTheme = ( String )RWT.getUISession().getAttribute( CURRENT_THEME_ID );
    assertEquals( Constants.THEME_ID_IOS, currentTheme );
  }

  @Test
  public void testUsesIOS6Theme() throws IOException {
    registerTheme( Constants.THEME_ID_IOS6 );
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_IOS + " OS 6.1.2" );

    provider.accept( request );

    String currentTheme = ( String )RWT.getUISession().getAttribute( CURRENT_THEME_ID );
    assertEquals( Constants.THEME_ID_IOS6, currentTheme );
  }

  @Test
  public void testUsesAndroidTheme() throws IOException {
    registerTheme( Constants.THEME_ID_ANDROID );
    TestRequest request = ( TestRequest )RWT.getRequest();
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
    ThemeManagerHelper.resetThemeManager();
    ThemeManager themeManager = applicationContext.getThemeManager();
    themeManager.registerTheme( theme );
  }

  @Test
  public void testSetsServerIdAsHeaderForAndroid() {
    provider = new TabrisClientProvider( "foo" );
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_ANDROID );

    provider.accept( request );

    String header = RWT.getResponse().getHeader( "com.eclipsesource.tabris.server.id" );
    assertEquals( "foo", header );
  }

  @Test
  public void testSetsServerIdAsHeaderForIOS() {
    provider = new TabrisClientProvider( "foo" );
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_IOS );

    provider.accept( request );

    String header = RWT.getResponse().getHeader( "com.eclipsesource.tabris.server.id" );
    assertEquals( "foo", header );
  }

  @Test
  public void testSetsServerIdAsHeaderForAndroidOnlyIfSet() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_ANDROID );

    provider.accept( request );

    String header = RWT.getResponse().getHeader( "com.eclipsesource.tabris.server.id" );
    assertNull( header );
  }

  @Test
  public void testSetsServerIdAsHeaderForIOSOnlyIfSet() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_IOS );

    provider.accept( request );

    String header = RWT.getResponse().getHeader( "com.eclipsesource.tabris.server.id" );
    assertNull( header );
  }

  @Test
  public void testSetsNoServerIdAsHeaderForWeb() {
    provider = new TabrisClientProvider( "foo" );
    TestRequest request = ( TestRequest )RWT.getRequest();

    provider.accept( request );

    String header = RWT.getResponse().getHeader( "com.eclipsesource.tabris.server.id" );
    assertNull( header );
  }

  @Test
  public void testSets412ForIncompatibleVersion() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_IOS + "/42.1.0 (foo)" );

    provider.accept( request );

    int status = RWT.getResponse().getStatus();
    assertEquals( 412, status );
  }

  @Test
  public void testSetsErrorHeadAttribute() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_IOS + "/42.1.0 (foo)" );

    provider.accept( request );

    JsonObject head = Fixture.getProtocolMessage().getHead();
    String error = head.get( "error" ).asString();
    assertEquals( "Incompatible Tabris Versions:\nClient 42.1 vs. Server " + TABRIS_SERVER_VERSION, error );
  }

  @Test
  public void testRespectsSystemPropertyForVersionCheck() {
    System.setProperty( PROPERTY_VERSION_CHECK, "false" );
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_IOS + "/42.1.0 (foo)" );

    provider.accept( request );

    int status = RWT.getResponse().getStatus();
    assertNotEquals( 412, status );
  }

}
