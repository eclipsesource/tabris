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
package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.internal.Constants.EVENT_PLAYBACK;
import static com.eclipsesource.tabris.internal.Constants.METHOD_CLEAR_CACHE;
import static com.eclipsesource.tabris.internal.Constants.METHOD_SKIP_FROM_CURRENT;
import static com.eclipsesource.tabris.internal.Constants.METHOD_STEP_TO_TIME;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_CACHE_SIZE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PARENT;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PLAYBACK_SPEED;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PLAYER_CONTROLS_VISIBLE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_STATE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_TIME;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_URL;
import static com.eclipsesource.tabris.internal.Constants.TYPE_VIDEO;
import static org.eclipse.rap.rwt.internal.lifecycle.WidgetLCAUtil.preserveListener;
import static org.eclipse.rap.rwt.internal.lifecycle.WidgetLCAUtil.preserveProperty;
import static org.eclipse.rap.rwt.internal.lifecycle.WidgetLCAUtil.renderListener;
import static org.eclipse.rap.rwt.internal.lifecycle.WidgetLCAUtil.renderProperty;

import java.io.IOException;
import java.io.Serializable;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.internal.lifecycle.AbstractWidgetLCA;
import org.eclipse.rap.rwt.internal.lifecycle.ControlLCAUtil;
import org.eclipse.rap.rwt.internal.lifecycle.ProcessActionRunner;
import org.eclipse.rap.rwt.internal.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.internal.protocol.RemoteObjectFactory;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.internal.widgets.compositekit.CompositeOperationHandler;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

import com.eclipsesource.tabris.widgets.Video;
import com.eclipsesource.tabris.widgets.Video.PlayerState;
import com.eclipsesource.tabris.widgets.Video.VideoStateAdapter;

@SuppressWarnings("restriction")
public class VideoLifeCycleAdapter extends AbstractWidgetLCA implements Serializable {

  @Override
  public void preserveValues( Widget widget ) {
    ControlLCAUtil.preserveValues( ( Control )widget );
    Video video = ( Video )widget;
    preserveProperty( video, PROPERTY_PLAYBACK_SPEED, String.valueOf( video.getSpeed() ) );
    preserveProperty( video, PROPERTY_PLAYER_CONTROLS_VISIBLE, video.hasPlayerControlsVisible() );
    VideoStateAdapter adapter = video.getAdapter( VideoStateAdapter.class );
    preserveListener( video, EVENT_PLAYBACK, adapter.hasPlaybackListener() );
  }

  @Override
  public void renderChanges( Widget widget ) throws IOException {
    ControlLCAUtil.renderChanges( ( Control )widget );
    Video video = ( Video )widget;
    VideoStateAdapter adapter = video.getAdapter( VideoStateAdapter.class );
    renderListener( video, EVENT_PLAYBACK, adapter.hasPlaybackListener(), false );
    if( adapter.isSkipFromCurrentRequested() ) {
      JsonObject parameters = new JsonObject();
      parameters.add( PROPERTY_TIME, adapter.getSkipFromCurrent() );
      RemoteObject remoteObject = RemoteObjectFactory.getRemoteObject( video );
      remoteObject.call( METHOD_SKIP_FROM_CURRENT, parameters );
      adapter.resetSkipFromCurrent();
    }
    if( adapter.isStepToTimeRequested() ) {
      JsonObject parameters = new JsonObject();
      parameters.add( PROPERTY_TIME, adapter.getStepToTime() );
      RemoteObject remoteObject = RemoteObjectFactory.getRemoteObject( video );
      remoteObject.call( METHOD_STEP_TO_TIME, parameters );
      adapter.resetStepToTime();
    }
    if( adapter.isClearCacheRequested() ) {
      RemoteObject remoteObject = RemoteObjectFactory.getRemoteObject( video );
      remoteObject.call( METHOD_CLEAR_CACHE, null );
      adapter.resetClearCache();
    }
    renderProperty( video,
                    PROPERTY_PLAYER_CONTROLS_VISIBLE,
                    video.hasPlayerControlsVisible(),
                    false );
    renderProperty( video,
                    PROPERTY_PLAYBACK_SPEED,
                    Double.valueOf( video.getSpeed() ),
                    Double.valueOf( Video.PAUSE_SPEED ) );
  }

  @Override
  public void renderInitialization( Widget widget ) throws IOException {
    Video video = ( Video )widget;
    RemoteObject remoteObject = RemoteObjectFactory.createRemoteObject( video, TYPE_VIDEO );
    remoteObject.setHandler( new VideoOperationHandler( video ) );
    remoteObject.set( PROPERTY_PARENT, WidgetUtil.getId( video.getParent() ) );
    remoteObject.set( PROPERTY_CACHE_SIZE, video.getCacheSize() );
    remoteObject.set( PROPERTY_URL, video.getURL().toString() );
  }

  public static class VideoOperationHandler extends CompositeOperationHandler {

    public VideoOperationHandler( Composite composite ) {
      super( composite );
    }

    @Override
    public void handleNotify( final Composite control, String eventName, JsonObject properties ) {
      if( eventName.equals( EVENT_PLAYBACK ) ) {
        String state = properties.get( PROPERTY_STATE ).asString().toUpperCase();
        final PlayerState playerState = PlayerState.valueOf( state );
        ProcessActionRunner.add( new Runnable() {
          @Override
          public void run() {
            control.getAdapter( VideoStateAdapter.class ).notifyPlaybackListeners( playerState );
          }
        } );
      } else {
        super.handleNotify( control, eventName, properties );
      }
    }

    @Override
    public void handleSet( Composite control, JsonObject properties ) {
      JsonValue value = properties.get( PROPERTY_PLAYBACK_SPEED );
      if( value != null ) {
        ((Video) control).setSpeed( value.asFloat() );
      }
      super.handleSet( control, properties );
    }
  }

}