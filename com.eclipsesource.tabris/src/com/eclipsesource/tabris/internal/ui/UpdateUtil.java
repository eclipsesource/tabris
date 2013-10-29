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

import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.service.ContextProvider;

import com.eclipsesource.tabris.ui.PageConfiguration;
import com.eclipsesource.tabris.ui.UIConfiguration;


@SuppressWarnings("restriction")
public class UpdateUtil {

  static final String UPDATER_PROPERTY = "com.eclipsesource.tabris.ui.UIUpdater";

  public static void registerUpdater( UIUpdater updater ) {
    whenNull( updater ).throwIllegalArgument( "UIUpdater must not be null" );
    RWT.getUISession().setAttribute( UPDATER_PROPERTY, updater );
  }

  public static void firePageUpdate( PageConfiguration pageConfiguration ) {
    whenNull( pageConfiguration ).throwIllegalArgument( "PageConfiguration must not be null" );
    UIUpdater updater = getUpdater();
    if( updater != null ) {
      updater.update( pageConfiguration );
    }
  }

  public static void fireUiUpdate( UIConfiguration uiConfiguration ) {
    whenNull( uiConfiguration ).throwIllegalArgument( "UIConfiguration must not be null" );
    UIUpdater updater = getUpdater();
    if( updater != null ) {
      updater.update( uiConfiguration );
    }
  }

  static UIUpdater getUpdater() {
    UIUpdater result = null;
    if( ContextProvider.hasContext() ) {
      Object updater = RWT.getUISession().getAttribute( UPDATER_PROPERTY );
      if( updater != null && updater instanceof UIUpdater ) {
        result = ( UIUpdater )updater;
      }
    }
    return result;
  }

  private UpdateUtil() {
    // prevent instantiation
  }

}
