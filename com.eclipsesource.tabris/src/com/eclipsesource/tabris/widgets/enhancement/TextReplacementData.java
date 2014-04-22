/*******************************************************************************
 * Copyright (c) 2013,2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets.enhancement;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.TYPE_TEXT_REPLACEMENT;

import java.io.Serializable;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.widgets.Text;

import com.eclipsesource.tabris.TabrisClient;


/**
 * <p>
 * {@link TextReplacementData} is used to enhance a {@link Text} widget with the ability to create suggestions for the
 * typed letters. E.g. when you define a replacement for the key "foo" with the value "bar" the user gets a suggestion
 * ("bar") when he types in "foo". Usually a {@link TextReplacementData} is used within the
 * {@link TextDecorator#setTextReplacement(TextReplacementData)} method.
 * </p>
 *
 * @see TextDecorator#setTextReplacement(TextReplacementData)
 *
 * @since 1.4
 */
public class TextReplacementData implements Serializable {

  private final JsonObject data;
  private RemoteObject remoteObject;

  public TextReplacementData() {
    Connection connection = RWT.getUISession().getConnection();
    if( RWT.getClient() instanceof TabrisClient ) {
      remoteObject = connection.createRemoteObject( TYPE_TEXT_REPLACEMENT );
    }
    data = new JsonObject();
  }

  /**
   * <p>
   * Create a replacement entry for a specified shortcut. If the user types in the shortcut he will get the replacement
   * displayed"
   * </p>
   *
   * @throws IllegalArgumentException when one of the arguments is <code>null</code> or empty.
   */
  public void put( String shortcut, String replacement ) {
    whenNull( shortcut ).throwIllegalArgument( "shortcut must not be null" );
    when( shortcut.isEmpty() ).throwIllegalArgument( "shortcut must not be empty" );
    whenNull( replacement ).throwIllegalArgument( "replacement must not be null" );
    when( replacement.isEmpty() ).throwIllegalArgument( "replacement must not be empty" );
    data.remove( shortcut );
    data.add( shortcut, replacement );
    updateData();
  }

  /**
   * <p>
   * Returns the defined replacement for the given shortcut.
   * </p>
   *
   * @throws IllegalArgumentException when the shortcut is <code>null</code> or empty.
   */
  public String get( String shortcut ) {
    whenNull( shortcut ).throwIllegalArgument( "shortcut must not be null" );
    when( shortcut.isEmpty() ).throwIllegalArgument( "shortcut must not be empty" );
    JsonValue jsonValue = data.get( shortcut );
    if( jsonValue != null ) {
      return jsonValue.asString();
    }
    return null;
  }

  /**
   * <p>
   * Removes the replacement value for the given shortcut.
   * </p>
   *
   * @throws IllegalArgumentException when the shortcut is <code>null</code> or empty.
   */
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

  String getId() {
    if( remoteObject != null ) {
      return remoteObject.getId();
    }
    return null;
  }

}