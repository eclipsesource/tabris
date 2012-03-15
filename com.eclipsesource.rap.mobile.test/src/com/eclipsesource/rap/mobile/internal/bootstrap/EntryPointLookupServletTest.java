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
package com.eclipsesource.rap.mobile.internal.bootstrap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.rwt.internal.application.ApplicationContext;
import org.eclipse.rwt.internal.lifecycle.EntryPointManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@SuppressWarnings("restriction")
@RunWith( MockitoJUnitRunner.class )
public class EntryPointLookupServletTest {
  
  private EntryPointLookupServlet lookupServlet;
  @Mock private EntryPointManager manager;

  @Before
  public void setUp() {
    ApplicationContext applicationContext = mock( ApplicationContext.class );
    when( applicationContext.getEntryPointManager() ).thenReturn( manager );
    lookupServlet = new EntryPointLookupServlet( applicationContext );
  }
  
  @Test
  public void testGet() throws ServletException, IOException, JSONException {
    Collection<String> paths = createPathList();
    when( manager.getServletPaths() ).thenReturn( paths );
    HttpServletResponse resp = mock( HttpServletResponse.class );
    PrintWriter writer = mock( PrintWriter.class );
    when( resp.getWriter() ).thenReturn( writer );
    HttpServletRequest req = mock( HttpServletRequest.class );
    
    lookupServlet.doGet( req, resp );
    
    JSONArray points = getEntryPointArray( writer );
    assertEquals( "/test", points.getString( 0 ) );
    assertEquals( "/test2", points.getString( 1 ) );
    assertEquals( 2, points.length() );
  }

  private Collection<String> createPathList() {
    Collection<String> paths = new ArrayList<String>();
    paths.add( "/test" );
    paths.add( "/test2" );
    return paths;
  }

  private JSONArray getEntryPointArray( PrintWriter writer ) throws JSONException {
    ArgumentCaptor<String> captor = ArgumentCaptor.forClass( String.class );
    verify( writer ).write( captor.capture() );
    JSONObject object = new JSONObject( captor.getValue() );
    JSONArray points = object.getJSONArray( EntryPointLookupServlet.KEY_ENTRYPOINTS );
    return points;
  }
}
