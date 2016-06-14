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

import static com.eclipsesource.tabris.internal.Constants.EVENT_PLAYBACK;
import static com.eclipsesource.tabris.internal.Constants.METHOD_CLEAR_CACHE;
import static com.eclipsesource.tabris.internal.Constants.METHOD_SKIP_FROM_CURRENT;
import static com.eclipsesource.tabris.internal.Constants.METHOD_STEP_TO_TIME;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_BOUNDS;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PLAYBACK_SPEED;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PLAYER_CONTROLS_VISIBLE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_TIME;
import static org.eclipse.rap.rwt.remote.JsonMapping.toJson;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.graphics.Rectangle;

import com.eclipsesource.tabris.widgets.Video;


public class VideoRemoteAdapter extends TabrisRemoteAdapter<Video> {

  private static final float DISABLED = Float.NEGATIVE_INFINITY;

  private static final int PLAYER_CONTROLS_VISIBLE = 1;
  private static final int PLAYBACK_SPEED = 2;
  private static final int BOUNDS = 3;
  private static final int PLAYBACK_LISTENER = 4;

  private final RemoteObject remoteObject;
  private final Runnable renderRunnable;
  private transient boolean controlsVisible;
  private transient float playbackSpeed;
  private transient Rectangle bounds;
  private transient boolean playbackListener;
  private transient float stepToTime = DISABLED;
  private transient float skipFromCurrent = DISABLED;
  private transient boolean clearCache;

  public VideoRemoteAdapter( Video video, RemoteObject remoteObject ) {
    super( video );
    this.remoteObject = remoteObject;
    renderRunnable = createRenderRunnable();
  }

  private Runnable createRenderRunnable() {
    return new Runnable() {
      @Override
      public void run() {
        renderControlsVisible();
        renderPlaybackSpeed();
        renderBounds();
        renderPlaybackListener();
        renderStepToTime();
        renderSkipFromCurrent();
        renderClearCache();
        clear();
      }
    };
  }

  public void preserveControlsVisible( boolean visible ) {
    if( !hasPreserved( PLAYER_CONTROLS_VISIBLE ) ) {
      markPreserved( PLAYER_CONTROLS_VISIBLE );
      controlsVisible = visible;
      scheduleRender( renderRunnable );
    }
  }

  private void renderControlsVisible() {
    if( hasPreserved( PLAYER_CONTROLS_VISIBLE ) ) {
      boolean actual = control.hasPlayerControlsVisible();
      if( changed( actual, controlsVisible ) ) {
        remoteObject.set( PROPERTY_PLAYER_CONTROLS_VISIBLE, actual );
      }
    }
  }

  public void preservePlaybackSpeed( float speed ) {
    if( !hasPreserved( PLAYBACK_SPEED ) ) {
      markPreserved( PLAYBACK_SPEED );
      playbackSpeed = speed;
      scheduleRender( renderRunnable );
    }
  }

  private void renderPlaybackSpeed() {
    if( hasPreserved( PLAYBACK_SPEED ) ) {
      float actual = control.getSpeed();
      if( actual != playbackSpeed ) {
        remoteObject.set( PROPERTY_PLAYBACK_SPEED, actual );
      }
    }
  }

  public void preserveBounds( Rectangle bounds ) {
    if( !hasPreserved( BOUNDS ) ) {
      markPreserved( BOUNDS );
      this.bounds = bounds;
      scheduleRender( renderRunnable );
    }
  }

  private void renderBounds() {
    if( hasPreserved( BOUNDS ) ) {
      Rectangle actual = control.getBounds();
      if( changed( actual, bounds ) ) {
        actual.x = 0;
        actual.y = 0;
        remoteObject.set( PROPERTY_BOUNDS, toJson( actual ) );
      }
    }
  }

  public void preservePlaybackListener( boolean playbackListener ) {
    if( !hasPreserved( PLAYBACK_LISTENER ) ) {
      markPreserved( PLAYBACK_LISTENER );
      this.playbackListener = playbackListener;
      scheduleRender( renderRunnable );
    }
  }

  private void renderPlaybackListener() {
    if( hasPreserved( PLAYBACK_LISTENER ) ) {
      boolean actual = !control.getPlaybackListeners().isEmpty();
      if( changed( actual, playbackListener )) {
        remoteObject.listen( EVENT_PLAYBACK, actual );
      }
    }
  }

  public void setStepToTime( float seconds ) {
    stepToTime = Math.max( 0, seconds );
    skipFromCurrent = DISABLED;
    scheduleRender( renderRunnable );
  }

  private void renderStepToTime() {
    if( stepToTime != DISABLED ) {
      JsonObject value = new JsonObject().add( PROPERTY_TIME, stepToTime );
      remoteObject.call( METHOD_STEP_TO_TIME, value );
    }
  }

  public void setSkipFromCurrent( float seconds ) {
    stepToTime = DISABLED;
    skipFromCurrent = seconds;
    scheduleRender( renderRunnable );
  }

  private void renderSkipFromCurrent() {
    if( skipFromCurrent != DISABLED ) {
      JsonObject value = new JsonObject().add( PROPERTY_TIME, skipFromCurrent );
      remoteObject.call( METHOD_SKIP_FROM_CURRENT, value );
    }
  }

  public void setClearCache( boolean clear ) {
    clearCache = clear;
    scheduleRender( renderRunnable );
  }

  private void renderClearCache() {
    if( clearCache ) {
      remoteObject.call( METHOD_CLEAR_CACHE, null );
    }
  }

  @Override
  protected void clear() {
    super.clear();
    stepToTime = DISABLED;
    skipFromCurrent = DISABLED;
    clearCache = false;
  }

}
