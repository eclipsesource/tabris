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
 * <p>
 * A <code>VideoListener</code> can be added to a <code>Video</code> object to receive video events like a change of the 
 * <code>PlaybackMode</code> or the <code>Presentationmode</code>
 * </p>
 * 
 * @see Video
 * @since 0.7
 */
public interface VideoListener {
  
  /**
   * <p>
   * Gets called when the <code>PlaybackMode</code> has changed, e.g. from PLAY to PAUSE.
   * </p>
   * 
   * @see PlaybackMode
   */
  void playbackChanged( PlaybackMode newMode );
  
  /**
   * <p>
   * Gets called when the <code>PresentationMode</code> has changed, e.g. from FULLSCREEN to EMBEDDED.
   * </p>
   * 
   * @see PresentationMode
   */
  void presentationChanged( PresentationMode newMode );
  
}
