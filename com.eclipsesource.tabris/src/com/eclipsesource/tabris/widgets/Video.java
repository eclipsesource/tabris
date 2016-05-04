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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.rwt.internal.lifecycle.WidgetLifeCycleAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.eclipsesource.tabris.internal.VideoLifeCycleAdapter;

/**
 * <p>
 * A <code>Video</code> can be used to play videos from an URL on a mobile
 * device. It provides facilities to control the video from the server side and
 * to receive notification when the client controls will be triggered.
 * </p>
 *
 * @see PlaybackListener
 * @since 0.7
 */
@SuppressWarnings("restriction")
public class Video extends Composite {

  /**
   * <p>
   * The current playback state of the video player. This may also change as a
   * result of the user interacting with the video player controls.
   * </p>
   *
   * @since 1.4.9
   */
   public static enum PlayerState {
     /**
      * Player state is unknown. This is the initial state.
      */
     UNKNOWN,
     /**
      * Player is ready to play.
      */
     READY,
     /**
      * Video playback has stalled temporarily. It will resume once
      * more data is loaded.
      */
     STALLED,
     /**
      * Video playback has failed.
      */
     FAILED,
     /**
      * Video playback has finished.
      */
     FINISHED;
  }

  /**
   * Speed for normal playback ({@value}).
   *
   * @see #setSpeed(float)
   * @since 1.4.9
   */
  public static final float PLAY_SPEED = 1.0f;
  /**
   * Speed for pausing the playback ({@value}).
   *
   * @see #setSpeed(float)
   * @since 1.4.9
   */
  public static final float PAUSE_SPEED = 0.0f;
  private static final float DISABLED = Float.NEGATIVE_INFINITY;

  private final List<PlaybackListener> playbackListeners = new ArrayList<PlaybackListener>();

  private int cacheSizeMb;
  private URL videoUrl;
  private float speed;
  private boolean clearCache;
  private boolean controlsVisible;
  private float stepToTime;
  private float skipFromCurrent;

  /**
   * Creates a new Video object.
   *
   * @param parent the parent Composite; never null
   * @param videoUrl a String with the url of the video to play; never null
   * @throws IllegalArgumentException when the passed URL string is not a valid
   *           url.
   * @since 1.4.9
   */
  public Video( Composite parent, String videoUrl ) throws IllegalArgumentException {
    this( parent, videoUrl, 0 );
  }

  /**
   * Creates a new Video object.
   *
   * @param videoUrl a String with the url of the video to play; never null
   * @param cacheSizeMb the maximum allowed size of the client-side video cache
   *          in megabytes (0 or greater).
   * @param parent the parent Composite; never null
   * @throws IllegalArgumentException when the passed URl string is not a valid
   *           url.
   * @since 1.4.9
   */
  public Video( Composite parent, String videoUrl, int cacheSizeMb )
    throws IllegalArgumentException
  {
    super( parent, SWT.NONE );
    initiateDefaultValues();
    setCacheSize( cacheSizeMb );
    setVideoUrl( videoUrl );
  }

  private void initiateDefaultValues() {
    this.speed = PAUSE_SPEED;
    this.clearCache = false;
    this.controlsVisible = false;
    this.stepToTime = DISABLED;
    this.skipFromCurrent = DISABLED;
  }

  private void setCacheSize( int cacheSizeMb ) {
    if( cacheSizeMb < 0 ) {
      throw new IllegalArgumentException( String.valueOf( cacheSizeMb ) );
    }
    this.cacheSizeMb = cacheSizeMb;
  }

  private void setVideoUrl( String videoUrl ) {
    try {
      this.videoUrl = new URL( videoUrl );
    } catch( MalformedURLException mfURLe ) {
      throw new IllegalArgumentException( videoUrl + " is not a valid url", mfURLe );
    }
  }

  /**
   * Instructs the device to remove all cached content from the video cache.
   * <p>
   * The call is asynchronous, the files are <b>not</b> removed immediately upon
   * returning.
   *
   * @since 1.4.9
   */
  public void clearCache() {
    clearCache = true;
  }

  /**
   * Returns the maximum allowed video cache size in megabytes. The default is
   * 0.
   *
   * @return cache size in megabytes (0 or greater).
   * @since 1.4.9
   */
  public int getCacheSize() {
    return cacheSizeMb;
  }

  /**
   * Returns the url of the video to play.
   */
  public URL getURL() {
    return videoUrl;
  }

  /**
   * Returns the current speed of the video playback.
   *
   * @see #PLAY_SPEED
   * @see #PAUSE_SPEED
   * @since 1.4.9
   */
  public float getSpeed() {
    return speed;
  }

  /**
   * Set the playback speed. A positive value of 1.0 plays forward at normal speed.
   * A negative value plays backward. A value of 0.0 pauses.
   *
   * @see #PLAY_SPEED
   * @see #PAUSE_SPEED
   * @since 1.4.9
   */
  public void setSpeed( float speed ) {
    this.speed = speed;
  }

  /**
   * Returns true, if video controls are visible on the client.
   */
  public boolean hasPlayerControlsVisible() {
    return controlsVisible;
  }

  /**
   * Controls if video controls should be visible on the client or not.
   */
  public void setPlayerControlsVisible( boolean controlsVisible ) {
    this.controlsVisible = controlsVisible;
  }

  /**
   * Adds a {@link PlaybackListener} to receive notifications of video events
   * like play or stop.
   *
   * @since 1.0
   */
   public void addPlaybackListener( PlaybackListener listener ) {
     playbackListeners.add( listener );
   }

  /**
   * Removes a {@link PlaybackListener}.
   *
   * @since 1.0
   */
   public void removePlaybackListener( PlaybackListener listener ) {
     playbackListeners.remove( listener );
   }

  /**
   * Steps to a specific point in time of the video to play.
   *
   * @param seconds Seconds before decimal point; milliseconds after decimal
   *          point. If the argument is too large, the play position will be
   *          moved to the end of the video. If the argument is too small, the
   *          play position will be moved to the start of the video.
   * @since 1.4.9
   */
  public void stepToTime( float seconds ) {
    this.stepToTime = Math.max( 0, seconds );
    this.skipFromCurrent = DISABLED;
  }

  /**
   * Skip forward or backward from the current playback position. Will be
   * affected by latency.
   *
   * @param seconds Seconds before the decimal point; milliseconds after the
   *          decimal point. A positive value skips forward; a negative value
   *          skips backward. If the argument is too large, the play position
   *          will be moved to the end of the video. If the argument is too
   *          small, the play position will be moved to the start of the video.
   * @since 1.4.9
   */
  public void skipFromCurrent( float seconds ) {
    this.stepToTime = DISABLED;
    this.skipFromCurrent = seconds;
  }

  @Override
  @SuppressWarnings({ "unchecked", "deprecation" })
  public <T> T getAdapter( Class<T> adapter ) {
    T result;
    if( adapter == WidgetLifeCycleAdapter.class
        || adapter == org.eclipse.rap.rwt.lifecycle.WidgetLifeCycleAdapter.class )
    {
      result = ( T )new VideoLifeCycleAdapter();
    } else if( adapter == VideoStateAdapter.class ) {
      result = ( T )new VideoStateAdapter();
    } else {
      result = super.getAdapter( adapter );
    }
    return result;
  }

  /**  Internal API. */
  public class VideoStateAdapter {

    public boolean hasPlaybackListener() {
      return !playbackListeners.isEmpty();
    }

    public void notifyPlaybackListeners( PlayerState state ) {
      List<PlaybackListener> listeners = new ArrayList<PlaybackListener>( playbackListeners );
      for( PlaybackListener listener : listeners ) {
        listener.playbackChanged( state );
      }
    }

    public boolean isClearCacheRequested() {
      return clearCache;
    }

    public void resetClearCache() {
      clearCache = false;
    }

    public boolean isSkipFromCurrentRequested() {
      return skipFromCurrent != DISABLED;
    }

    public float getSkipFromCurrent() {
      return skipFromCurrent;
    }

    public void resetSkipFromCurrent() {
      skipFromCurrent = DISABLED;
    }

    public boolean isStepToTimeRequested() {
      return stepToTime != DISABLED;
    }

    public float getStepToTime() {
      return stepToTime;
    }

    public void resetStepToTime() {
      stepToTime = DISABLED;
    }
  }
}
