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
package com.eclipsesource.tabris.passepartout.internal;

import static com.eclipsesource.tabris.passepartout.internal.Clauses.whenNull;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public class RelayoutListener implements Listener {

  private final Composite composite;

  public RelayoutListener( Composite composite ) {
    whenNull( composite ).throwIllegalArgument( "Composite must not be null" );
    this.composite = composite;
  }

  @Override
  public void handleEvent( Event event ) {
    if( !composite.isDisposed() ) {
      composite.layout( true, true );
    }
  }
}
