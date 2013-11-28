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
package com.eclipsesource.tabris.internal.ui;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;


public class PropertyChangeNotifier {

  private PropertyChangeHandler handler;

  public void setPropertyChangeHandler( PropertyChangeHandler handler ) {
    whenNull( handler ).throwIllegalArgument( "PropertyChangeHandler must not be null" );
    this.handler = handler;
  }

  public PropertyChangeHandler getPropertyChangeHandler() {
    return handler;
  }

  public void firePropertyChange( String key, Object value ) {
    whenNull( key ).throwIllegalArgument( "key must not be null" );
    when( key.isEmpty() ).throwIllegalArgument( "key must not be empty" );
    whenNull( handler ).throwIllegalState( "PropertyChangeHandler not set" );
    handler.propertyChanged( key, value );
  }
}
