/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.model;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;

@SuppressWarnings("restriction")
public class CustomVariablesBuilder {

  private final JsonObject jsonObject;
  private int counter;

  public CustomVariablesBuilder() {
    jsonObject = new JsonObject();
    counter = 1;
  }

  public CustomVariablesBuilder addCustomVariable( String key, String value ) {
    validateArgs( key, value );
    jsonObject.add( String.valueOf( counter++ ), new JsonArray().add( key ).add( value ) );
    return this;
  }

  private void validateArgs( String key, String value ) {
    whenNull( key ).throwIllegalArgument( "Key must not be null." );
    when( key.isEmpty() ).throwIllegalArgument( "Key must not be empty." );
    whenNull( value ).throwIllegalArgument( "Value must not be null." );
    when( value.isEmpty() ).throwIllegalArgument( "Value must not be empty." );
  }

  public String getJson() {
    if( jsonObject.isEmpty() ) {
      throw new IllegalStateException( "Custom variables must be added first." );
    }
    return jsonObject.toString();
  }
}
