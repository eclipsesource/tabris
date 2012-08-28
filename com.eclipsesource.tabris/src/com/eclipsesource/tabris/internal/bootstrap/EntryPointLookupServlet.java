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
package com.eclipsesource.tabris.internal.bootstrap;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.rap.rwt.internal.application.ApplicationContext;
import org.eclipse.rap.rwt.internal.lifecycle.EntryPointManager;
import org.eclipse.rap.rwt.internal.theme.JsonArray;
import org.eclipse.rap.rwt.internal.theme.JsonObject;


@SuppressWarnings("restriction")
public class EntryPointLookupServlet extends HttpServlet {
  
  static final String KEY_ENTRYPOINTS = "entrypoints";
  static final String KEY_PATH = "path";
  private transient EntryPointManager manager;
  private static final long serialVersionUID = 1L;  

  public EntryPointLookupServlet( ApplicationContext applicationContext ) {
    manager = applicationContext.getEntryPointManager();
  }

  @Override
  protected void doGet( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
    Collection<String> servletPaths = manager.getServletPaths();
    JsonObject jsonObject = createMessageObject( servletPaths );
    resp.setContentType( "application/json" );
    resp.getWriter().write( jsonObject.toString() );
  }

  private JsonObject createMessageObject( Collection<String> servletPaths ) {
    JsonObject jsonObject = new JsonObject();
    JsonArray array = new JsonArray();
    appendPaths( servletPaths, array );
    jsonObject.append( KEY_ENTRYPOINTS, array );
    return jsonObject;
  }

  private void appendPaths( Collection<String> servletPaths, JsonArray array ) {
    for( String path : servletPaths ) {
      JsonObject object = new JsonObject();
      object.append( KEY_PATH, path );
      array.append( object );
    }
  }
}
