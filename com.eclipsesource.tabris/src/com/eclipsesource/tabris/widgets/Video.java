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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.rap.rwt.lifecycle.ILifeCycleAdapter;
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
 * @see VideoListener
 * @since 0.7
 */
public class Video extends Composite {
  
  /**
   * <p>
   * The current playback state of the video player.
   * This may also change as a result of the user interacting with the video player controls.
   * </p>
   */
  public enum PlaybackMode {
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
   * The current presentation mode of the video player.
   * This may also change as a result of the user interacting with the video player controls.
   * </p>
   */
  public enum PresentationMode {
    /**
     * <p>
     * The player has entered or should enter full-screen mode.
     * ,/p>
     */
    FULL_SCREEN,

    /**
     * <p>
     * The player has exited or should exit full-screen mode.
     * </p>
     */
    EMBEDDED
  }

  private static final float PLAY_SPEED = 1;
  private static final float HALT_SPEED = 0;
  
  private URL videoUrl;
  private final List<VideoListener> listeners = new ArrayList<VideoListener>();
  private final Map<PlaybackOptions, Object> playbackOptions = new HashMap<PlaybackOptions, Object>();
  
  /**
   * <p>
   * Creates a new Video object.
   * </p>
   * 
   * @throws IllegalArgumentException when the passed URl string is not a valid url.
   */
  public Video( String videoUrl, Composite parent ) throws IllegalArgumentException {
    super( parent, SWT.NONE );
    initiateDefaultValues();
    assignUrl( videoUrl );
  }
  
  private void initiateDefaultValues() {
    playbackOptions.put( PlaybackOptions.PLAYBACK_MODE, PlaybackMode.PAUSE );
    playbackOptions.put( PlaybackOptions.PRESENTATION_MODE, PresentationMode.EMBEDDED );
    playbackOptions.put( PlaybackOptions.SPEED, Float.valueOf( HALT_SPEED ) );
    playbackOptions.put( PlaybackOptions.CONTROLS_VISIBLE, Boolean.valueOf( true ) );
    playbackOptions.put( PlaybackOptions.REPEAT, Boolean.valueOf( false ) );
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
    PlaybackMode oldMode = ( PlaybackMode )playbackOptions.get( PlaybackOptions.PLAYBACK_MODE );
    playbackOptions.put( PlaybackOptions.PLAYBACK_MODE, PlaybackMode.PLAY );
    firePlaybackChanged( oldMode, PlaybackMode.PLAY );
  }
  
  /**
   * <p>
   * Pauses the video playback on the client device.
   * </p>
   */
  public void pause() {
    playbackOptions.put( PlaybackOptions.SPEED, Float.valueOf( HALT_SPEED ) );
    PlaybackMode oldMode = ( PlaybackMode )playbackOptions.get( PlaybackOptions.PLAYBACK_MODE );
    playbackOptions.put( PlaybackOptions.PLAYBACK_MODE, PlaybackMode.PAUSE );
    firePlaybackChanged( oldMode, PlaybackMode.PAUSE );
  }
  
  /**
   * <p>
   * Stops the video playback on the client device.
   * </p>
   */
  public void stop() {
    playbackOptions.put( PlaybackOptions.SPEED, Float.valueOf( HALT_SPEED ) );
    PlaybackMode oldMode = ( PlaybackMode )playbackOptions.get( PlaybackOptions.PLAYBACK_MODE );
    playbackOptions.put( PlaybackOptions.PLAYBACK_MODE, PlaybackMode.STOP );
    firePlaybackChanged( oldMode, PlaybackMode.STOP );
  }
  
  /**
   * <p>
   * Fast forwards the video playback on the client device.
   * </p>
   */
  public void fastForward( float speed ) {
    if( speed <= 1 ) {
      throw new IllegalArgumentException( "Speed has to be > 1 for a fast fotward. But was " + speed );
    }
    playbackOptions.put( PlaybackOptions.SPEED, Float.valueOf( speed ) );
    PlaybackMode oldMode = ( PlaybackMode )playbackOptions.get( PlaybackOptions.PLAYBACK_MODE );
    playbackOptions.put( PlaybackOptions.PLAYBACK_MODE, PlaybackMode.FAST_FORWARD );
    firePlaybackChanged( oldMode, PlaybackMode.FAST_FORWARD );
  }
  
  /**
   * <p>
   * Fast backwards the video playback on the client device.
   * </p>
   */
  public void fastBackward( float speed ) {
    if( speed >= -1 ) {
      throw new IllegalArgumentException( "Speed has to be < 1 for a fast backward. But was " + speed );
    }
    playbackOptions.put( PlaybackOptions.SPEED, Float.valueOf( speed ) );
    PlaybackMode oldMode = ( PlaybackMode )playbackOptions.get( PlaybackOptions.PLAYBACK_MODE );
    playbackOptions.put( PlaybackOptions.PLAYBACK_MODE, PlaybackMode.FAST_BACKWARD );
    firePlaybackChanged( oldMode, PlaybackMode.FAST_BACKWARD );
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
    PresentationMode presentationMode = fullScreenEnabled ? PresentationMode.FULL_SCREEN : PresentationMode.EMBEDDED;
    PresentationMode oldMode = ( PresentationMode )playbackOptions.get( PlaybackOptions.PRESENTATION_MODE );
    playbackOptions.put( PlaybackOptions.PRESENTATION_MODE, presentationMode );
    firePresentationChanged( oldMode, presentationMode );
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
   * @see PlaybackMode
   */
  public PlaybackMode getPlaybackMode() {
    return ( PlaybackMode )playbackOptions.get( PlaybackOptions.PLAYBACK_MODE );
  }
  
  /**
   * <p>
   * Returns the current <code>PresentationMode</code>.
   * </p>
   * 
   * @see PresentationMode
   */
  public PresentationMode getPresentationMode() {
    return ( PresentationMode )playbackOptions.get( PlaybackOptions.PRESENTATION_MODE );
  }
  
  /**
   * <p>
   * Adds a <code>VideoListener</code> to receive notifications of video events like play or stop.
   * </p>
   */
  public void addVideoListener( VideoListener listener ) {
    listeners.add( listener );
  }
  
  /**
   * <p>
   * Removes a <code>VideoListener</code>
   * </p>
   */
  public void removeVideoListener( VideoListener listener ) {
    listeners.remove( listener );
  }
  
  private void firePlaybackChanged( PlaybackMode oldMode, PlaybackMode newMode ) {
    if( oldMode != newMode ) {
      for( VideoListener listener : listeners ) {
        listener.playbackChanged( newMode );
      }
    }
  }
  
  private void firePresentationChanged( PresentationMode oldMode, PresentationMode newMode ) {
    if( oldMode != newMode ) {
      for( VideoListener listener : listeners ) {
        listener.presentationChanged( newMode );
      }
    }
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public <T> T getAdapter( Class<T> adapter ) {
    T result;
    if( adapter == ILifeCycleAdapter.class ) {
      result = ( T )new VideoLifeCycleAdapter();
    } else if( adapter == PlaybackAdapter.class ) {
      result = ( T )new PlaybackAdapter();
    } else {
      result = super.getAdapter( adapter );
    }
    return result;
  }
  
  public class PlaybackAdapter {
    
    public void setPlaybackMode( PlaybackMode mode ) {
      playbackOptions.put( PlaybackOptions.PLAYBACK_MODE, mode );
    }
    
    public void firePlaybackChange( PlaybackMode mode ) {
      firePlaybackChanged( null, mode );
    }
    
    public void firePresentationChange( PresentationMode mode ) {
      firePresentationChanged( null, mode );
    }
    
    public Map<PlaybackOptions, Object> getOptions() {
      return playbackOptions;
    }

    public boolean hasVideoListener() {
      return !listeners.isEmpty();
    }
    
  }
  
}
