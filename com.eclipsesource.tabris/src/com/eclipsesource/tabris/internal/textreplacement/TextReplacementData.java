/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal.textreplacement;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.TEXT_REPLACEMENT;

import java.io.Serializable;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.Adaptable;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.widgets.Text;

import com.eclipsesource.tabris.TabrisClient;
import com.eclipsesource.tabris.internal.WidgetsUtil;

public class TextReplacementData implements Adaptable, Serializable {

  public static void enableTextReplacement( Text text, TextReplacementData data ) {
    String id = data.getAdapter( String.class );
    if( id != null ) {
      WidgetsUtil.setData( text, TEXT_REPLACEMENT, id );
    }
  }

  private static final String REMOTE_TYPE = "tabris.TextReplacement";

  private RemoteObject remoteObject;
  private final JsonObject data;

  public TextReplacementData() {
    Connection connection = RWT.getUISession().getConnection();
    if( RWT.getClient() instanceof TabrisClient ) {
      remoteObject = connection.createRemoteObject( REMOTE_TYPE );
    }
    data = new JsonObject();
  }

  public void put( String shortcut, String replacement ) {
    whenNull( shortcut ).throwIllegalArgument( "shortcut must not be null" );
    when( shortcut.isEmpty() ).throwIllegalArgument( "shortcut must not be empty" );
    whenNull( replacement ).throwIllegalArgument( "replacement must not be null" );
    when( replacement.isEmpty() ).throwIllegalArgument( "replacement must not be empty" );
    data.remove( shortcut );
    data.add( shortcut, replacement );
    updateData();
  }

  public String get( String shortcut ) {
    whenNull( shortcut ).throwIllegalArgument( "shortcut must not be null" );
    when( shortcut.isEmpty() ).throwIllegalArgument( "shortcut must not be empty" );
    JsonValue jsonValue = data.get( shortcut );
    if( jsonValue != null ) {
      return jsonValue.asString();
    }
    return null;
  }

  public void remove( String shortcut ) {
    whenNull( shortcut ).throwIllegalArgument( "shortcut must not be null" );
    when( shortcut.isEmpty() ).throwIllegalArgument( "shortcut must not be empty" );
    data.remove( shortcut );
    updateData();
  }

  private void updateData() {
    if( remoteObject != null ) {
      remoteObject.set( "texts", data );
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getAdapter( Class<T> adapter ) {
    if( adapter == String.class ) {
      if( remoteObject != null ) {
        return ( T )remoteObject.getId();
      }
    }
    return null;
  }

}