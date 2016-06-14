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
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PLAYBACK_SPEED;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_STATE;

import java.util.List;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.internal.lifecycle.ProcessActionRunner;
import org.eclipse.swt.internal.widgets.compositekit.CompositeOperationHandler;
import org.eclipse.swt.widgets.Composite;

import com.eclipsesource.tabris.widgets.PlaybackListener;
import com.eclipsesource.tabris.widgets.Video;
import com.eclipsesource.tabris.widgets.Video.PlayerState;


@SuppressWarnings("restriction")
public class VideoOperationHandler extends CompositeOperationHandler {

  public VideoOperationHandler( Video control ) {
    super( control );
  }

  @Override
  public void handleSet( Composite control, JsonObject properties ) {
    JsonValue value = properties.get( PROPERTY_PLAYBACK_SPEED );
    if( value != null ) {
      ( ( Video )control ).setSpeed( value.asFloat() );
    }
    super.handleSet( control, properties );
  }

  @Override
  public void handleNotify( Composite control, String eventName, JsonObject properties ) {
    if( EVENT_PLAYBACK.equals( eventName ) ) {
      notifyPlaybackListeners( ( Video )control, properties );
    } else {
      super.handleNotify( control, eventName, properties );
    }
  }

  private void notifyPlaybackListeners( final Video video, JsonObject properties ) {
    String state = properties.get( PROPERTY_STATE ).asString().toUpperCase();
    final PlayerState playerState = PlayerState.valueOf( PlayerState.class, state );
    ProcessActionRunner.add( new Runnable() {

      @Override
      public void run() {
        List<PlaybackListener> playbackListeners = video.getPlaybackListeners();
        for( PlaybackListener playbackListener : playbackListeners ) {
          playbackListener.playbackChanged( playerState );
        }
      }
    } );
  }

}