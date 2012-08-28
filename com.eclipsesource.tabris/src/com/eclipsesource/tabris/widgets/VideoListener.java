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
package com.eclipsesource.tabris.widgets;

import com.eclipsesource.tabris.widgets.Video.PlaybackMode;
import com.eclipsesource.tabris.widgets.Video.PresentationMode;


/**
 * @since 0.7
 */
public interface VideoListener {
  
  void playbackChanged( PlaybackMode newMode );
  
  void presentationChanged( PresentationMode newMode );
  
}
