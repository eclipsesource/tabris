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
import static com.eclipsesource.tabris.internal.Constants.EVENT_PRESENTATION;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_CACHE_SIZE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PARENT;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PLAYBACK;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PRESENTATION;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_URL;
import static com.eclipsesource.tabris.internal.Constants.TYPE_VIDEO;
import static org.eclipse.rap.rwt.internal.lifecycle.WidgetLCAUtil.preserveListener;
import static org.eclipse.rap.rwt.internal.lifecycle.WidgetLCAUtil.preserveProperty;
import static org.eclipse.rap.rwt.internal.lifecycle.WidgetLCAUtil.renderListener;
import static org.eclipse.rap.rwt.internal.lifecycle.WidgetLCAUtil.renderProperty;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.rap.json.JsonObject;
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
import com.eclipsesource.tabris.widgets.Video.Playback;
import com.eclipsesource.tabris.widgets.Video.PlaybackAdapter;
import com.eclipsesource.tabris.widgets.Video.Presentation;


@SuppressWarnings("restriction")
public class VideoLifeCycleAdapter extends AbstractWidgetLCA implements Serializable {

  public static enum PlaybackOptions {
    SPEED, REPEAT, CONTROLS_VISIBLE, PLAYBACK, PRESENTATION, HEAD_POSITION
  }

  @Override
  public void preserveValues( Widget widget ) {
    ControlLCAUtil.preserveValues( ( Control )widget );
    Video video = ( Video ) widget;
    PlaybackAdapter adapter = video.getAdapter( PlaybackAdapter.class );
    Map<PlaybackOptions, Object> options = adapter.getOptions();
    for( Entry<PlaybackOptions, Object> entry : options.entrySet() ) {
      preserveProperty( video, keyForEnum( entry.getKey() ), jsonizeValue( entry ) );
    }
    preserveListener( video, EVENT_PLAYBACK, adapter.hasPlaybackListener() );
    preserveListener( video, EVENT_PRESENTATION, adapter.hasPresentationListener() );
  }

  @Override
  public void renderChanges( Widget widget ) throws IOException {
    ControlLCAUtil.renderChanges( ( Control )widget );
    Video video = ( Video ) widget;
    PlaybackAdapter adapter = video.getAdapter( PlaybackAdapter.class );
    Map<PlaybackOptions, Object> options = adapter.getOptions();
    for( Entry<PlaybackOptions, Object> entry : options.entrySet() ) {
      renderProperty( widget, keyForEnum( entry.getKey() ), jsonizeValue( entry ), null );
    }
    renderListener( video, EVENT_PLAYBACK, adapter.hasPlaybackListener(), false );
    renderListener( video, EVENT_PRESENTATION, adapter.hasPresentationListener(), false );
    if( adapter.isClearCacheRequested() ) {
      adapter.resetClearCache();
      RemoteObject remoteObject = RemoteObjectFactory.getRemoteObject( video );
      remoteObject.call( "clearCache", null );
    }
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
    public void handleNotify( Composite control, String eventName, JsonObject properties ) {
      if( eventName.equals( EVENT_PLAYBACK ) ) {
        handlePlaybackMode( control, properties );
      } else if( eventName.equals( EVENT_PRESENTATION ) ) {
        handlePresentationMode( control, properties );
      } else {
        super.handleNotify( control, eventName, properties );
      }
    }

    private void handlePlaybackMode( Widget widget, JsonObject properties ) {
      String playbackMode = properties.get( PROPERTY_PLAYBACK ).asString();
      Playback newMode = Playback.valueOf( playbackMode.toUpperCase() );
      Video video = ( Video )widget;
      video.getAdapter( PlaybackAdapter.class ).setPlaybackMode( newMode );
      notifyListenersAboutPlaybackModeChange( newMode, video );
    }

    private void notifyListenersAboutPlaybackModeChange( final Playback newMode, final Video video ) {
      ProcessActionRunner.add( new Runnable() {
        @Override
        public void run() {
          video.getAdapter( PlaybackAdapter.class ).firePlaybackChange( newMode );
        }
      } );
    }

    private void handlePresentationMode( Widget widget, JsonObject properties ) {
      String presentationMode = properties.get( PROPERTY_PRESENTATION ).asString();
      Presentation newMode = Presentation.valueOf( presentationMode.toUpperCase() );
      Video video = ( Video )widget;
      video.getAdapter( PlaybackAdapter.class ).getOptions().put( PlaybackOptions.PRESENTATION, newMode );
      notifyListenersAboutPresentationModeChange( newMode, video );
    }

    private void notifyListenersAboutPresentationModeChange( final Presentation newMode, final Video video ) {
      ProcessActionRunner.add( new Runnable() {
        @Override
        public void run() {
          video.getAdapter( PlaybackAdapter.class ).firePresentationChange( newMode );
        }
      } );
    }
  }

  private static Object jsonizeValue( Entry<PlaybackOptions, Object> entry ) {
    Object value = entry.getValue();
    if( value instanceof Playback || value instanceof Presentation ) {
      value = keyForEnum( ( ( Enum )value ) );
    }
    return value;
  }

  static String keyForEnum( Enum<?> type ) {
    return type.name().toLowerCase();
  }
}