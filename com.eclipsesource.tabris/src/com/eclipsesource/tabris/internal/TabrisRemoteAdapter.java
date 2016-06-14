/*******************************************************************************
 * Copyright (c) 2016 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;

import org.eclipse.rap.rwt.internal.lifecycle.RemoteAdapter;
import org.eclipse.swt.internal.SerializableCompatibility;
import org.eclipse.swt.internal.widgets.WidgetRemoteAdapter;
import org.eclipse.swt.widgets.Composite;

@SuppressWarnings("restriction")
public class TabrisRemoteAdapter<T extends Composite> implements SerializableCompatibility {

  protected final T control;
  private transient int preserved;
  private transient boolean renderScheduled;

  public TabrisRemoteAdapter( T composite ) {
    this.control = composite;
  }

  protected void markPreserved( int index ) {
    preserved |= ( 1 << index );
  }

  protected boolean hasPreserved( int index ) {
    return ( preserved & ( 1 << index ) ) != 0;
  }

  protected void clear() {
    preserved = 0;
    renderScheduled = false;
  }

  protected void scheduleRender( Runnable runnable ) {
    if( !renderScheduled ) {
      getRemoteAdapter( control ).addRenderRunnable( runnable );
      renderScheduled = true;
    }
  }

  protected boolean changed( boolean actualValue, boolean preservedValue ) {
    return actualValue != preservedValue;
  }

  protected boolean changed( Object actualValue, Object preservedValue ) {
    return !equals( actualValue, preservedValue );
  }

  private static boolean equals( Object o1, Object o2 ) {
    return o1 == o2 || o1 != null && o1.equals( o2 );
  }

  protected static WidgetRemoteAdapter getRemoteAdapter( Composite composite ) {
    return ( WidgetRemoteAdapter )composite.getAdapter( RemoteAdapter.class );
  }

}
