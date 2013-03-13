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
package com.eclipsesource.tabris.widgets;

import com.eclipsesource.tabris.widgets.Video.Playback;


/**
 * <p>
 * A {@link PlaybackListener} can be added to a {@link Video} object to receive video events like a play or stop.
 * </p>
 *
 * @see Video
 *
 * @since 1.0
 */
public interface PlaybackListener {

  /**
   * <p>
   * Gets called when the {@link Playback} has changed, e.g. from PLAY to PAUSE.
   * </p>
   *
   * @see Playback
   */
  void playbackChanged( Playback newPlayback );

}
