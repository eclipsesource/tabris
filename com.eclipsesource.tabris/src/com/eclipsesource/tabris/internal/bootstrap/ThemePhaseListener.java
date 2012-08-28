/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal.bootstrap;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.theme.ThemeUtil;
import org.eclipse.rap.rwt.lifecycle.PhaseEvent;
import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.lifecycle.PhaseListener;

import com.eclipsesource.tabris.Bootstrapper;
import com.eclipsesource.tabris.internal.Constants;


@SuppressWarnings("restriction")
public class ThemePhaseListener implements PhaseListener {

  public void beforePhase( PhaseEvent event ) {
    String userAgent = RWT.getRequest().getHeader( Constants.USER_AGENT );
    setCurrentTheme( userAgent );
  }

  private void setCurrentTheme( String userAgent ) {
    if( userAgent.contains( Constants.ID_IOS ) ) {
      ThemeUtil.setCurrentThemeId( Bootstrapper.THEME_ID_IOS );
    } else if( userAgent.contains( Constants.ID_ANDROID ) ) {
      ThemeUtil.setCurrentThemeId( Bootstrapper.THEME_ID_ANDROID );
    }
  }

  public void afterPhase( PhaseEvent event ) {
    // do nothing
  }

  public PhaseId getPhaseId() {
    return PhaseId.PREPARE_UI_ROOT;
  }
}
