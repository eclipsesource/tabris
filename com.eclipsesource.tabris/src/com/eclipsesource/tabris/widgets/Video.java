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

import org.eclipse.rwt.lifecycle.ILifeCycleAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.eclipsesource.tabris.internal.VideoLifeCycleAdapter;
import com.eclipsesource.tabris.internal.VideoLifeCycleAdapter.PlaybackOptions;


/**
 * @since 0.6
 */
public class Video extends Composite {
  
  public enum PlaybackMode {
    PLAY, PAUSE, STOP, INTERUPT, FAST_FORWARD, FAST_BACKWARD, USER_EXIT, DONE, ERROR
  }
  public enum PresentationMode {
    FULL_SCREEN, EMBEDDED
  }

  private static final float PLAY_SPEED = 1;
  private static final float HALT_SPEED = 0;
  
  private URL videoUrl;
  private List<VideoListener> listeners = new ArrayList<VideoListener>();
  private Map<PlaybackOptions, Object> playbackOptions = new HashMap<PlaybackOptions, Object>();
  
  public Video( String videoUrl, Composite parent ) {
    super( parent, SWT.NONE );
    initiateDefaultValues();
    assignUrl( videoUrl );
  }
  
  private void initiateDefaultValues() {
    playbackOptions.put( PlaybackOptions.PLAYBACK_MODE, PlaybackMode.STOP );
    playbackOptions.put( PlaybackOptions.PRESENTATION_MODE, PresentationMode.EMBEDDED );
    playbackOptions.put( PlaybackOptions.SPEED, Float.valueOf( HALT_SPEED ) );
    playbackOptions.put( PlaybackOptions.CONTROLS_VISIBLE, Boolean.valueOf( true ) );
    playbackOptions.put( PlaybackOptions.REPEAT, Boolean.valueOf( false ) );
    playbackOptions.put( PlaybackOptions.AUTOPLAY, Boolean.valueOf( false ) );
  }

  private void assignUrl( String videoUrl ) {
    try {
      this.videoUrl = new URL( videoUrl );
    } catch( MalformedURLException mfURLe ) {
      throw new IllegalArgumentException( videoUrl + " is not a valid url", mfURLe );
    }
  }
  
  public URL getURL() {
    return videoUrl;
  }

  public void play() {
    playbackOptions.put( PlaybackOptions.SPEED, Float.valueOf( PLAY_SPEED ) );
    playbackOptions.put( PlaybackOptions.PLAYBACK_MODE, PlaybackMode.PLAY );
    firePlaybackChanged( PlaybackMode.PLAY );
  }
  
  public void pause() {
    playbackOptions.put( PlaybackOptions.SPEED, Float.valueOf( HALT_SPEED ) );
    playbackOptions.put( PlaybackOptions.PLAYBACK_MODE, PlaybackMode.PAUSE );
    firePlaybackChanged( PlaybackMode.PAUSE );
  }
  
  public void stop() {
    playbackOptions.put( PlaybackOptions.SPEED, Float.valueOf( HALT_SPEED ) );
    playbackOptions.put( PlaybackOptions.PLAYBACK_MODE, PlaybackMode.STOP );
    firePlaybackChanged( PlaybackMode.STOP );
  }
  
  public void fastForward( float speed ) {
    if( speed <= 1 ) {
      throw new IllegalArgumentException( "Speed has to be > 1 for a fast fotward. But was " + speed );
    }
    playbackOptions.put( PlaybackOptions.SPEED, Float.valueOf( speed ) );
    playbackOptions.put( PlaybackOptions.PLAYBACK_MODE, PlaybackMode.FAST_FORWARD );
    firePlaybackChanged( PlaybackMode.FAST_FORWARD );
  }
  
  public void fastBackward( float speed ) {
    if( speed >= -1 ) {
      throw new IllegalArgumentException( "Speed has to be < 1 for a fast backward. But was " + speed );
    }
    playbackOptions.put( PlaybackOptions.SPEED, Float.valueOf( speed ) );
    playbackOptions.put( PlaybackOptions.PLAYBACK_MODE, PlaybackMode.FAST_BACKWARD );
    firePlaybackChanged( PlaybackMode.FAST_BACKWARD );
  }
  
  public float getSpeed() {
    return ( ( Float )playbackOptions.get( PlaybackOptions.SPEED ) ).floatValue();
  }
  
  public void stepToTime( int seconds ) {
    playbackOptions.put( PlaybackOptions.HEAD_POSITION, Integer.valueOf( seconds ) );
  }
  
  public void setRepeat( boolean repeat ) {
    playbackOptions.put( PlaybackOptions.REPEAT, Boolean.valueOf( repeat ) );
  }
  
  public boolean hasRepeat() {
    return ( ( Boolean )playbackOptions.get( PlaybackOptions.REPEAT ) ).booleanValue();
  }
  
  public void setAutoPlay( boolean autoPlay ) {
    playbackOptions.put( PlaybackOptions.AUTOPLAY, Boolean.valueOf( autoPlay ) );
  }
  
  public boolean hasAutoPlay() {
    return ( ( Boolean )playbackOptions.get( PlaybackOptions.AUTOPLAY ) ).booleanValue();
  }
  
  public void setFullscreen( boolean fullScreenEnabled ) {
    PresentationMode presentationMode = fullScreenEnabled ? PresentationMode.FULL_SCREEN : PresentationMode.EMBEDDED;
    playbackOptions.put( PlaybackOptions.PRESENTATION_MODE, presentationMode );
    firePresentationChanged( presentationMode );
  }
  
  public void setPlayerControlsVisible( boolean visibility ) {
    playbackOptions.put( PlaybackOptions.CONTROLS_VISIBLE, Boolean.valueOf( visibility ) );
  }
  
  public boolean hasPlayerControlsVisible() {
    return ( ( Boolean )playbackOptions.get( PlaybackOptions.CONTROLS_VISIBLE ) ).booleanValue();
  }
  
  public PlaybackMode getPlaybackMode() {
    return ( PlaybackMode )playbackOptions.get( PlaybackOptions.PLAYBACK_MODE );
  }
  
  public PresentationMode getPresentationMode() {
    return ( PresentationMode )playbackOptions.get( PlaybackOptions.PRESENTATION_MODE );
  }
  
  public void addVideoListener( VideoListener listener ) {
    listeners.add( listener );
  }
  
  public void removeVideoListener( VideoListener listener ) {
    listeners.remove( listener );
  }
  
  private void firePlaybackChanged( PlaybackMode newMode ) {
    for( VideoListener listener : listeners ) {
      listener.playbackChanged( newMode );
    }
  }
  
  private void firePresentationChanged( PresentationMode newMode ) {
    for( VideoListener listener : listeners ) {
      listener.presentationChanged( newMode );
    }
  }
  
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
      firePlaybackChanged( mode );
    }
    
    public Map<PlaybackOptions, Object> getOptions() {
      return playbackOptions;
    }
    
  }
  
}
