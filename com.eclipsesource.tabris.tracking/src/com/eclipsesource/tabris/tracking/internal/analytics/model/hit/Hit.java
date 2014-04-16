/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.analytics.model.hit;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.HIT_TYPE;

import java.util.HashMap;
import java.util.Map;

import com.eclipsesource.tabris.tracking.internal.analytics.model.Requestable;


@SuppressWarnings("restriction")
public class Hit implements Requestable {

  private final Map<String, Object> parameter;

  public Hit( String type ) {
    whenNull( type ).throwIllegalArgument( "Type must not be null." );
    when( type.isEmpty() ).throwIllegalArgument( "Type must not be empty." );
    this.parameter = new HashMap<String, Object>();
    addParameter( getRequestKey( HIT_TYPE ), type );
  }

  protected void addParameter( String key, Object value) {
    parameter.put( key, value );
  }

  @Override
  public Map<String, Object> getParameter() {
    return parameter;
  }
}
