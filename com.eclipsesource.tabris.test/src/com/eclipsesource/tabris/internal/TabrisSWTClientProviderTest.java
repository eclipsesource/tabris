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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.application.ApplicationContextImpl;
import org.eclipse.rap.rwt.internal.service.ContextProvider;
import org.eclipse.rap.rwt.internal.theme.Theme;
import org.eclipse.rap.rwt.internal.theme.ThemeManager;
import org.eclipse.rap.rwt.internal.theme.css.CssFileReader;
import org.eclipse.rap.rwt.internal.theme.css.StyleSheet;
import org.eclipse.rap.rwt.service.ResourceLoader;
import org.eclipse.rap.rwt.testfixture.TestRequest;
import org.eclipse.rap.rwt.testfixture.internal.engine.ThemeManagerHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.eclipsesource.tabris.test.RWTRunner;


@SuppressWarnings("restriction")
@RunWith( RWTRunner.class )
public class TabrisSWTClientProviderTest {

  private static final String CURRENT_THEME_ID = "org.eclipse.rap.theme.current";

  private TabrisSWTClientProvider provider;

  @Before
  public void setUp() {
    provider = new TabrisSWTClientProvider();
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( TabrisSWTClientProvider.class ) );
  }

  @Test
  public void testAcceptForSWT() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_SWT );

    assertTrue( provider.accept( request ) );
  }

  @Test
  public void testDoesNotAcceptForWeb() {
    assertFalse( provider.accept( RWT.getRequest() ) );
  }

  @Test
  public void testReturnsTabrisClient() {
    assertTrue( provider.getClient() instanceof TabrisSWTClient );
  }

  @Test
  public void testUsesSWTTheme() throws IOException {
    registerTheme( Constants.THEME_ID_SWT );
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_SWT );

    provider.accept( request );

    String currentTheme = ( String )RWT.getUISession().getAttribute( CURRENT_THEME_ID );
    assertEquals( Constants.THEME_ID_SWT, currentTheme );
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
}
