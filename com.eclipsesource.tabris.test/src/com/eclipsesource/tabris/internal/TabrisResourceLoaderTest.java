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
package com.eclipsesource.tabris.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletContext;

import org.eclipse.rap.rwt.internal.application.ApplicationContextImpl;
import org.eclipse.rap.rwt.internal.lifecycle.EntryPointManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@SuppressWarnings("restriction")
@RunWith( MockitoJUnitRunner.class )
public class TabrisResourceLoaderTest {

  @Mock private EntryPointManager manager;
  @Mock private ServletContext context;
  private ApplicationContextImpl applicationContext;

  @Before
  public void setUp() {
    applicationContext = mock( ApplicationContextImpl.class );
    when( applicationContext.getEntryPointManager() ).thenReturn( manager );
    when( applicationContext.getServletContext() ).thenReturn( context );
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( TabrisResourceLoader.class ) );
  }

  @Test
  public void testGetWithoutIndexJson() throws IOException {
    TabrisResourceLoader loader = new TabrisResourceLoader( applicationContext );
    Collection<String> paths = createPathList();
    when( manager.getServletPaths() ).thenReturn( paths );

    InputStream stream = loader.getResourceAsStream( "index.json2" );

    assertNull( stream );
  }

  @Test
  public void testGetEntryPointsWithIndexJson() throws IOException, JSONException {
    TabrisResourceLoader loader = new TabrisResourceLoader( applicationContext );
    Collection<String> paths = createPathList();
    when( manager.getServletPaths() ).thenReturn( paths );

    InputStream stream = loader.getResourceAsStream( "index.json" );

    JSONArray points = getEntryPointArray( stream );
    assertEquals( "../test", points.getJSONObject( 0 ).getString( TabrisResourceLoader.KEY_PATH ) );
    assertEquals( "../test2", points.getJSONObject( 1 ).getString( TabrisResourceLoader.KEY_PATH ) );
    assertEquals( 2, points.length() );
  }

  @Test
  public void testSortsEntryPoints() throws IOException, JSONException {
    TabrisResourceLoader loader = new TabrisResourceLoader( applicationContext );
    Collection<String> paths = createPathList();
    paths.add( "/a" );
    when( manager.getServletPaths() ).thenReturn( paths );

    InputStream stream = loader.getResourceAsStream( "index.json" );

    JSONArray points = getEntryPointArray( stream );
    assertEquals( "../a", points.getJSONObject( 0 ).getString( TabrisResourceLoader.KEY_PATH ) );
    assertEquals( "../test", points.getJSONObject( 1 ).getString( TabrisResourceLoader.KEY_PATH ) );
    assertEquals( "../test2", points.getJSONObject( 2 ).getString( TabrisResourceLoader.KEY_PATH ) );
  }

  private Collection<String> createPathList() {
    Collection<String> paths = new ArrayList<String>();
    paths.add( "/test" );
    paths.add( "/test2" );
    return paths;
  }

  private JSONArray getEntryPointArray( InputStream stream ) throws JSONException, IOException {
    JSONObject object = new JSONObject( getJson( stream ) );
    JSONArray points = object.getJSONArray( TabrisResourceLoader.KEY_ENTRYPOINTS );
    return points;
  }

  private String getJson( InputStream stream ) throws IOException {
    InputStreamReader is = new InputStreamReader( stream );
    StringBuilder stringBuilder = new StringBuilder();
    BufferedReader reader = new BufferedReader( is );
    String read = reader.readLine();
    while( read != null ) {
      stringBuilder.append( read );
      read = reader.readLine();
    }
    return stringBuilder.toString();
  }

}
