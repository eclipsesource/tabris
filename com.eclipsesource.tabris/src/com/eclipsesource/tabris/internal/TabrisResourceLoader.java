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

import static com.eclipsesource.tabris.internal.Constants.INDEX_JSON;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.rap.rwt.internal.application.ApplicationContextImpl;
import org.eclipse.rap.rwt.internal.lifecycle.EntryPointManager;
import org.eclipse.rap.rwt.internal.theme.JsonArray;
import org.eclipse.rap.rwt.internal.theme.JsonObject;
import org.eclipse.rap.rwt.service.ResourceLoader;


@SuppressWarnings("restriction")
public class TabrisResourceLoader implements ResourceLoader, Serializable {

  static final String KEY_ENTRYPOINTS = "entrypoints";
  static final String KEY_PATH = "path";
  private final EntryPointManager entryPointManager;

  public TabrisResourceLoader( ApplicationContextImpl applicationContext ) {
    entryPointManager = applicationContext.getEntryPointManager();
  }

  @Override
  public InputStream getResourceAsStream( String resourceName ) throws IOException {
    if( INDEX_JSON.equals( resourceName ) ) {
      return getEntryPoints();
    }
    return null;
  }

  private InputStream getEntryPoints() {
    Collection<String> servletPaths = entryPointManager.getServletPaths();
    JsonObject jsonObject = createMessageObject( servletPaths );
    String json = jsonObject.toString();
    try {
      return new ByteArrayInputStream( json.getBytes( "utf-8" ) );
    } catch( UnsupportedEncodingException uee ) {
      throw new IllegalStateException( uee );
    }
  }

  private JsonObject createMessageObject( Collection<String> servletPaths ) {
    JsonObject jsonObject = new JsonObject();
    JsonArray array = new JsonArray();
    appendPaths( ( List<String> )servletPaths, array );
    jsonObject.append( KEY_ENTRYPOINTS, array );
    return jsonObject;
  }

  private void appendPaths( List<String> servletPaths, JsonArray array ) {
    Collections.sort( servletPaths );
    for( String path : servletPaths ) {
      JsonObject object = new JsonObject();
      object.append( KEY_PATH, ".." + path );
      array.append( object );
    }
  }

}
