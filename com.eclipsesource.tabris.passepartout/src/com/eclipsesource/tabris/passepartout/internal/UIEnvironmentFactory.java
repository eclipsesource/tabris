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

import static com.eclipsesource.tabris.passepartout.internal.Clauses.when;
import static com.eclipsesource.tabris.passepartout.internal.Clauses.whenNull;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

import com.eclipsesource.tabris.passepartout.Bounds;
import com.eclipsesource.tabris.passepartout.UIEnvironment;


public class UIEnvironmentFactory {

  public static UIEnvironment createEnvironment( Widget widget ) {
    whenNull( widget ).throwIllegalArgument( "Widget must not be null" );
    when( widget.isDisposed() ).throwIllegalState( "Widget already disposed" );
    return new UIEnvironmentImpl( getParentBounds( widget ), getReferenceBounds( widget ), getParentFontSize( widget ) );
  }

  private static Bounds getParentBounds( Widget widget ) {
    if( widget instanceof Control ) {
      Composite parent = ( ( Control )widget ).getParent();
      if( parent != null ) {
        return BoundsUtil.getBounds( parent.getBounds() );
      }
      return BoundsUtil.getBounds( widget.getDisplay().getBounds() );
    }
    return BoundsUtil.getBounds( widget.getDisplay().getShells()[ 0 ].getBounds() );
  }

  private static Bounds getReferenceBounds( Widget widget ) {
    if( widget instanceof Control ) {
      if( widget instanceof Shell ) {
        return BoundsUtil.getBounds( widget.getDisplay().getBounds() );
      }
      return BoundsUtil.getBounds( ( ( Control )widget ).getShell().getBounds() );
    }
    return BoundsUtil.getBounds( widget.getDisplay().getBounds() );
  }

  private static int getParentFontSize( Widget widget ) {
    if( widget instanceof Control ) {
      Composite parent = ( ( Control )widget ).getParent();
      if( parent != null ) {
        return parent.getFont().getFontData()[ 0 ].getHeight();
      }
    }
    return Display.getCurrent().getSystemFont().getFontData()[ 0 ].getHeight();
  }

  private UIEnvironmentFactory() {
    // prevent instantiation
  }
}
