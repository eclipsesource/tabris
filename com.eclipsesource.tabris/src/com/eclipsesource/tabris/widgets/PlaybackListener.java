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

import java.io.Serializable;

import com.eclipsesource.tabris.widgets.Video.PlayerState;

/**
 * <p>
 * A {@link PlaybackListener} can be added to a {@link Video} object to receive video events.
 * </p>
 *
 * @see Video
 *
 * @since 1.0
 */
public interface PlaybackListener extends Serializable {

  /**
   * <p>
   * Gets called when the {@link PlayerState} has changed.
   * </p>
   *
   * @see PlayerState
   * @since 1.4.9
   */
  void playbackChanged( PlayerState state );

}
