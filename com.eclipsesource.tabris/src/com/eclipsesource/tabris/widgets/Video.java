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

import static com.eclipsesource.tabris.internal.Clauses.when;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.rap.rwt.internal.lifecycle.WidgetLifeCycleAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.eclipsesource.tabris.internal.VideoLifeCycleAdapter;
import com.eclipsesource.tabris.internal.VideoLifeCycleAdapter.PlaybackOptions;


/**
 * <p>
 * A <code>Video</code> can be used to play videos from an URL on a mobile device. It provides facilities to control
 * the video from the server side and to receive notification when the client controls will be triggered.
 * </p>
 *
 * @see PlaybackListener
 * @see PresentationListener
 *
 * @since 0.7
 */
@SuppressWarnings("restriction")
public class Video extends Composite {

  /**
   * <p>
   * The current playback state of the video player.
   * This may also change as a result of the user interacting with the video player controls.
   * </p>
   *
   * @since 1.0
   */
  public static enum Playback {
    /**
     * <p>
     * Video playback is ready.
     * </p>
     */
    READY,

    /**
     * <p>
     * Video playback is currently under way.
     * </p>
     */
    PLAY,

    /**
     * <p>
     * Video playback is currently paused.
     * </p>
     */
    PAUSE,

    /**
     * <p>
     * Video playback is currently stopped.
     * </p>
     */
    STOP,

    /**
     * <p>
     * Video playback is temporarily interrupted, perhaps because the buffer ran out of content.
     * </p>
     */
    INTERRUPT,

    /**
     * <p>
     * The player is currently seeking towards the end of the video.
     * </p>
     */
    FAST_FORWARD,

    /**
     * <p>
     * The player is currently seeking towards the beginning of the video.
     * </p>
     */
    FAST_BACKWARD,

    /**
     * <p>
     * The player has reached the end of the video and finished playing.
     * </p>
     */
    DONE,

    /**
     * <p>
     * An error has occurred like: buffering of data has stalled.
     * </p>
     */
    ERROR
  }

  /**
   * <p>
   * The current presentation of the video player.
   * This may also change as a result of the user interacting with the video player controls.
   * </p>
   *
   * @since 1.0
   */
  public static enum Presentation {
    /**
     * <p>
     * The player has entered or should enter full-screen.
     * ,/p>
     */
    FULL_SCREEN,

    /**
     * <p>
     * The player has exited or should exit full-screen.
     * </p>
     */
    EMBEDDED
  }

  private static final float PLAY_SPEED = 1;
  private static final float HALT_SPEED = 0;

  private final List<PlaybackListener> playbackListeners = new ArrayList<PlaybackListener>();
  private final List<PresentationListener> presentationListeners = new ArrayList<PresentationListener>();
  private final Map<PlaybackOptions, Object> playbackOptions = new HashMap<PlaybackOptions, Object>();

  private URL videoUrl;
  private int cacheSizeMb;
  private boolean wantsToClearCache;

  /**
   * Creates a new Video object.
   *
   * @param videoUrl a String with the url of the video to play; never null
   * @param parent the parent Composite; never null
   * @throws IllegalArgumentException when the passed URL string is not a valid
   *           url.
   */
  public Video( String videoUrl, Composite parent ) throws IllegalArgumentException {
    this(videoUrl, 0, parent);
  }

  /**
   * <p>
   * Creates a new Video object.
   * </p>
   *
   * @param videoUrl a String with the url of the video to play; never null
   * @param cacheSizeMb the maximum allowed size of the client-side video cache
   *          in megabytes (0 or greater).
   * @param parent the parent Composite; never null
   * @throws IllegalArgumentException when the passed URl string is not a valid
   *           url.
   * @since 1.4.8
   */
  public Video( String videoUrl, int cacheSizeMb, Composite parent ) throws IllegalArgumentException {
    super( parent, SWT.NONE );
    initiateDefaultValues();
    setCacheSize( cacheSizeMb );
    assignUrl( videoUrl );
  }

  private void initiateDefaultValues() {
    playbackOptions.put( PlaybackOptions.PLAYBACK, Playback.PAUSE );
    playbackOptions.put( PlaybackOptions.PRESENTATION, Presentation.EMBEDDED );
    playbackOptions.put( PlaybackOptions.SPEED, Float.valueOf( HALT_SPEED ) );
    playbackOptions.put( PlaybackOptions.CONTROLS_VISIBLE, Boolean.valueOf( true ) );
    playbackOptions.put( PlaybackOptions.REPEAT, Boolean.valueOf( false ) );
  }

  private void setCacheSize( int cacheSizeMb ) {
    if (cacheSizeMb < 0) {
      throw new IllegalArgumentException( String.valueOf( cacheSizeMb ) );
    }
    this.cacheSizeMb = cacheSizeMb;
  }

  private void assignUrl( String videoUrl ) {
    try {
      this.videoUrl = new URL( videoUrl );
    } catch( MalformedURLException mfURLe ) {
      throw new IllegalArgumentException( videoUrl + " is not a valid url", mfURLe );
    }
  }

  /**
   * <p>
   * Returns the url of the video to play.
   * </p>
   */
  public URL getURL() {
    return videoUrl;
  }

  /**
   * <p>
   * Starts the video playback on the client device.
   * </p>
   */
  public void play() {
    playbackOptions.put( PlaybackOptions.SPEED, Float.valueOf( PLAY_SPEED ) );
    Playback oldMode = ( Playback )playbackOptions.get( PlaybackOptions.PLAYBACK );
    playbackOptions.put( PlaybackOptions.PLAYBACK, Playback.PLAY );
    firePlayback( oldMode, Playback.PLAY );
  }

  /**
   * <p>
   * Pauses the video playback on the client device.
   * </p>
   */
  public void pause() {
    playbackOptions.put( PlaybackOptions.SPEED, Float.valueOf( HALT_SPEED ) );
    Playback oldMode = ( Playback )playbackOptions.get( PlaybackOptions.PLAYBACK );
    playbackOptions.put( PlaybackOptions.PLAYBACK, Playback.PAUSE );
    firePlayback( oldMode, Playback.PAUSE );
  }

  /**
   * <p>
   * Stops the video playback on the client device.
   * </p>
   */
  public void stop() {
    playbackOptions.put( PlaybackOptions.SPEED, Float.valueOf( HALT_SPEED ) );
    Playback oldMode = ( Playback )playbackOptions.get( PlaybackOptions.PLAYBACK );
    playbackOptions.put( PlaybackOptions.PLAYBACK, Playback.STOP );
    firePlayback( oldMode, Playback.STOP );
  }

  /**
   * <p>
   * Fast forwards the video playback on the client device.
   * </p>
   */
  public void fastForward( float speed ) {
    when( speed <= 1 ).throwIllegalArgument( "Speed has to be > 1 for a fast forward but was " + speed );
    playbackOptions.put( PlaybackOptions.SPEED, Float.valueOf( speed ) );
    Playback oldMode = ( Playback )playbackOptions.get( PlaybackOptions.PLAYBACK );
    playbackOptions.put( PlaybackOptions.PLAYBACK, Playback.FAST_FORWARD );
    firePlayback( oldMode, Playback.FAST_FORWARD );
  }

  /**
   * <p>
   * Fast backwards the video playback on the client device.
   * </p>
   */
  public void fastBackward( float speed ) {
    when( speed >= -1 ).throwIllegalArgument( "Speed has to be < -1 for a fast backward but was " + speed );
    playbackOptions.put( PlaybackOptions.SPEED, Float.valueOf( speed ) );
    Playback oldMode = ( Playback )playbackOptions.get( PlaybackOptions.PLAYBACK );
    playbackOptions.put( PlaybackOptions.PLAYBACK, Playback.FAST_BACKWARD );
    firePlayback( oldMode, Playback.FAST_BACKWARD );
  }

  /**
   * <p>
   * returns the current speed of the video playback. 1.0 mean normal speed.
   * </p>
   */
  public float getSpeed() {
    return ( ( Float )playbackOptions.get( PlaybackOptions.SPEED ) ).floatValue();
  }

  /**
   * <p>
   * Steps to a specific point in time of the video to play.
   * </p>
   *
   * @param seconds the time to step to in seconds.
   */
  public void stepToTime( int seconds ) {
    playbackOptions.put( PlaybackOptions.HEAD_POSITION, Integer.valueOf( seconds ) );
  }

  /**
   * <p>
   * Sets the repeat value of the video playback on the client device.
   * </p>
   */
  public void setRepeat( boolean repeat ) {
    playbackOptions.put( PlaybackOptions.REPEAT, Boolean.valueOf( repeat ) );
  }

  /**
   * <p>
   * Returns if the video will be repeated.
   * </p>
   */
  public boolean hasRepeat() {
    return ( ( Boolean )playbackOptions.get( PlaybackOptions.REPEAT ) ).booleanValue();
  }

  /**
   * <p>
   * Sets full screen playback on the client device.
   * </p>
   */
  public void setFullscreen( boolean fullScreenEnabled ) {
    Presentation presentationMode = fullScreenEnabled ? Presentation.FULL_SCREEN : Presentation.EMBEDDED;
    Presentation oldMode = ( Presentation )playbackOptions.get( PlaybackOptions.PRESENTATION );
    playbackOptions.put( PlaybackOptions.PRESENTATION, presentationMode );
    firePresentation( oldMode, presentationMode );
  }

  /**
   * <p>
   * Controls if video controls should be visible on the client or not.
   * </p>
   */
  public void setPlayerControlsVisible( boolean visibility ) {
    playbackOptions.put( PlaybackOptions.CONTROLS_VISIBLE, Boolean.valueOf( visibility ) );
  }

  /**
   * <p>
   * returns if video controls are visible on the client or not.
   * </p>
   */
  public boolean hasPlayerControlsVisible() {
    return ( ( Boolean )playbackOptions.get( PlaybackOptions.CONTROLS_VISIBLE ) ).booleanValue();
  }

  /**
   * <p>
   * Returns the current <code>PlaybackMode</code>
   * </p>
   *
   * @see Playback
   *
   * @since 1.0
   */
  public Playback getPlayback() {
    return ( Playback )playbackOptions.get( PlaybackOptions.PLAYBACK );
  }

  /**
   * <p>
   * Returns the current {@link Presentation}.
   * </p>
   *
   * @see Presentation
   *
   * @since 1.0
   */
  public Presentation getPresentation() {
    return ( Presentation )playbackOptions.get( PlaybackOptions.PRESENTATION );
  }

  /**
   * <p>
   * Adds a {@link PlaybackListener} to receive notifications of video events like play or stop.
   * </p>
   *
   * @since 1.0
   */
  public void addPlaybackListener( PlaybackListener listener ) {
    playbackListeners.add( listener );
  }

  /**
   * <p>
   * Removes a {@link PlaybackListener}.
   * </p>
   *
   * @since 1.0
   */
  public void removePlaybackListener( PlaybackListener listener ) {
    playbackListeners.remove( listener );
  }

  /**
   * <p>
   * Adds a {@link PresentationListener} to receive notifications of presentation events like entered fullscrion and
   * so on.
   * </p>
   *
   * @since 1.0
   */
  public void addPresentationListener( PresentationListener listener ) {
    presentationListeners.add( listener );
  }

  /**
   * <p>
   * Removes a {@link PresentationListener}.
   * </p>
   *
   * @since 1.0
   */
  public void removePresentationListener( PresentationListener listener ) {
    presentationListeners.remove( listener );
  }

  /**
   * Returns the maximum allowed video cache size in megabytes. The default is 0.
   *
   * @return cache size in megabytes (0 or greater).
   * @since 1.4.8
   */
  public int getCacheSize() {
    return cacheSizeMb;
  }

  /**
   * Instructs the device to remove all cached content from the video cache.
   * <p>
   * The call is asynchronous, the files are <b>not</b> removed immediately upon returning.
   *
   * @since 1.4.8
   */
  public void clearCache() {
    wantsToClearCache = true;
  }

  private void firePlayback( Playback oldMode, Playback newMode ) {
    if( oldMode != newMode ) {
      List<PlaybackListener> listeners = new ArrayList<PlaybackListener>( playbackListeners );
      for( PlaybackListener listener : listeners ) {
        listener.playbackChanged( newMode );
      }
    }
  }

  private void firePresentation( Presentation oldMode, Presentation newMode ) {
    if( oldMode != newMode ) {
      List<PresentationListener> listeners = new ArrayList<PresentationListener>( presentationListeners );
      for( PresentationListener listener : listeners ) {
        listener.presentationChanged( newMode );
      }
    }
  }

  @Override
  @SuppressWarnings({ "unchecked", "deprecation" })
  public <T> T getAdapter( Class<T> adapter ) {
    T result;
    if( adapter == WidgetLifeCycleAdapter.class || adapter == org.eclipse.rap.rwt.lifecycle.WidgetLifeCycleAdapter.class ) {
      result = ( T )new VideoLifeCycleAdapter();
    } else if( adapter == PlaybackAdapter.class ) {
      result = ( T )new PlaybackAdapter();
    } else {
      result = super.getAdapter( adapter );
    }
    return result;
  }

  public class PlaybackAdapter {

    public void setPlaybackMode( Playback playback ) {
      playbackOptions.put( PlaybackOptions.PLAYBACK, playback );
    }

    public void firePlaybackChange( Playback playback ) {
      firePlayback( null, playback );
    }

    public void firePresentationChange( Presentation mode ) {
      firePresentation( null, mode );
    }

    public Map<PlaybackOptions, Object> getOptions() {
      return playbackOptions;
    }

    public boolean hasPlaybackListener() {
      return !playbackListeners.isEmpty();
    }

    public boolean hasPresentationListener() {
      return !presentationListeners.isEmpty();
    }

    public boolean isClearCacheRequested() {
      return wantsToClearCache;
    }

    public void resetClearCache() {
      wantsToClearCache = false;
    }
  }
}
