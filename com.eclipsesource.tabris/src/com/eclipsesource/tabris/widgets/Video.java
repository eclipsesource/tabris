/*******************************************************************************
 * Copyright (c) 2012, 2016 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets;

import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_CACHE_SIZE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PARENT;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_URL;
import static com.eclipsesource.tabris.internal.Constants.TYPE_VIDEO;
import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import com.eclipsesource.tabris.internal.VideoOperationHandler;
import com.eclipsesource.tabris.internal.VideoRemoteAdapter;

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

  private final List<PlaybackListener> playbackListeners = new ArrayList<PlaybackListener>();

  private int cacheSizeMb;
  private URL videoUrl;
  private float speed = PAUSE_SPEED;
  private boolean controlsVisible;
  private final RemoteObject remoteObject;
  private final VideoRemoteAdapter remoteAdapter;

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
    setCacheSize( cacheSizeMb );
    setVideoUrl( videoUrl );
    Connection connection = RWT.getUISession().getConnection();
    remoteObject = connection.createRemoteObject( TYPE_VIDEO );
    remoteObject.setHandler( new VideoOperationHandler( this ) );
    remoteObject.set( PROPERTY_PARENT, getId( this ) );
    remoteObject.set( PROPERTY_CACHE_SIZE, getCacheSize() );
    remoteObject.set( PROPERTY_URL, getURL().toString() );
    remoteAdapter = new VideoRemoteAdapter( this, remoteObject );
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
    remoteAdapter.setClearCache( true );
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
    if( this.speed != speed ) {
      remoteAdapter.preservePlaybackSpeed( this.speed );
      this.speed = speed;
    }
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
    if( this.controlsVisible != controlsVisible ) {
      remoteAdapter.preserveControlsVisible( this.controlsVisible );
      this.controlsVisible = controlsVisible;
    }
  }

  /**
   * Adds a {@link PlaybackListener} to receive notifications of video events
   * like play or stop.
   *
   * @since 1.0
   */
   public void addPlaybackListener( PlaybackListener listener ) {
     whenNull( listener ).throwIllegalArgument( "PlaybackListener must not be null" );
     remoteAdapter.preservePlaybackListener( hasPlaybackListener() );
     playbackListeners.add( listener );
   }

  /**
   * Removes a {@link PlaybackListener}.
   *
   * @since 1.0
   */
   public void removePlaybackListener( PlaybackListener listener ) {
     whenNull( listener ).throwIllegalArgument( "PlaybackListener must not be null" );
     remoteAdapter.preservePlaybackListener( hasPlaybackListener() );
     playbackListeners.remove( listener );
   }

   /**
    * <p>
    * Returns the added {@link PlaybackListener}s
    * </p>
    *
    * @since 1.4.9
    */
   public List<PlaybackListener> getPlaybackListeners() {
     return new ArrayList<PlaybackListener>( playbackListeners );
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
    remoteAdapter.setStepToTime( seconds );
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
    remoteAdapter.setSkipFromCurrent( seconds );
  }

  @Override
  public void setBounds( Rectangle bounds ) {
    remoteAdapter.preserveBounds( getBounds() );
    super.setBounds( bounds );
  }

  private boolean hasPlaybackListener() {
    return !playbackListeners.isEmpty();
  }

}
