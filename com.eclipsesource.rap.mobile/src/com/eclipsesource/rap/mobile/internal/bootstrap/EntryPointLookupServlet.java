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

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.rwt.internal.application.ApplicationContext;
import org.eclipse.rwt.internal.lifecycle.EntryPointManager;


@SuppressWarnings("restriction")
public class EntryPointLookupServlet extends HttpServlet {
  
  static final String KEY_ENTRYPOINTS = "entrypoints";
  private EntryPointManager manager;

  public EntryPointLookupServlet( ApplicationContext applicationContext ) {
    manager = applicationContext.getEntryPointManager();
  }

  @Override
  protected void doGet( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
    Collection<String> servletPaths = manager.getServletPaths();
    StringBuilder builder = new StringBuilder();
    builder.append( "{'" + KEY_ENTRYPOINTS + "':[" );
    appendPaths( servletPaths, builder );
    builder.append( "]}" );
    resp.setContentType( "application/json" );
    resp.getWriter().write( builder.toString() );
  }

  private void appendPaths( Collection<String> servletPaths, StringBuilder builder ) {
    for( String path : servletPaths ) {
      if( !builder.toString().endsWith( "[" ) ) {
        builder.append( "," );
      } 
      builder.append( "'" + path + "'" );
    }
  }
}
