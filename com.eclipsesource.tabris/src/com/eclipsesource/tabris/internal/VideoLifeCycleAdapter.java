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

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.rwt.internal.protocol.ClientObjectFactory;
import org.eclipse.rwt.internal.protocol.IClientObject;
import org.eclipse.rwt.lifecycle.AbstractWidgetLCA;
import org.eclipse.rwt.lifecycle.ControlLCAUtil;
import org.eclipse.rwt.lifecycle.WidgetLCAUtil;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

import com.eclipsesource.tabris.widgets.Video;
import com.eclipsesource.tabris.widgets.Video.PlaybackAdapter;
import com.eclipsesource.tabris.widgets.Video.PlaybackMode;
import com.eclipsesource.tabris.widgets.Video.PresentationMode;


@SuppressWarnings("restriction")
public class VideoLifeCycleAdapter extends AbstractWidgetLCA {
  
  public enum PlaybackOptions {
    SPEED, REPEAT, AUTOPLAY, CONTROLS_VISIBLE, PLAYBACK_MODE, PRESENTATION_MODE, HEAD_POSITION
  }

  static final String TYPE = "tabris.widgets.Video";
  static final String PARENT = "parent";
  static final String VIDEO_URL = "videoURL";

  @Override
  public void readData( Widget widget ) {
    readPlaybackMode( widget );
    readPresentationMode( widget );
  }

  private void readPlaybackMode( Widget widget ) {
    String playbackMode = WidgetLCAUtil.readPropertyValue( widget, keyForEnum( PlaybackOptions.PLAYBACK_MODE ) );
    if( playbackMode != null ) {
      PlaybackMode newMode = PlaybackMode.valueOf( playbackMode );
      Video video = ( Video )widget;
      video.getAdapter( PlaybackAdapter.class ).setPlaybackMode( newMode );
    }
  }

  private void readPresentationMode( Widget widget ) {
    String presentationMode = WidgetLCAUtil.readPropertyValue( widget, keyForEnum( PlaybackOptions.PRESENTATION_MODE ) );
    if( presentationMode != null ) {
      PresentationMode newMode = PresentationMode.valueOf( presentationMode );
      Video video = ( Video )widget;
      if( newMode == PresentationMode.EMBEDDED ) {
        video.setFullscreen( false );
      } else {
        video.setFullscreen( true );
      }
    }
  }

  @Override
  public void preserveValues( Widget widget ) {
    ControlLCAUtil.preserveValues( ( Control )widget );
    Video video = ( Video ) widget;
    PlaybackAdapter adapter = video.getAdapter( PlaybackAdapter.class );
    Map<PlaybackOptions, Object> options = adapter.getOptions();
    for( Entry<PlaybackOptions, Object> entry : options.entrySet() ) {
      WidgetLCAUtil.preserveProperty( video, keyForEnum( entry.getKey() ), jsonizeValue( entry ) );
    }
  }

  @Override
  public void renderChanges( Widget widget ) throws IOException {
    ControlLCAUtil.renderChanges( ( Control )widget );
    Video video = ( Video ) widget;
    PlaybackAdapter adapter = video.getAdapter( PlaybackAdapter.class );
    Map<PlaybackOptions, Object> options = adapter.getOptions();
    for( Entry<PlaybackOptions, Object> entry : options.entrySet() ) {
      WidgetLCAUtil.renderProperty( widget, keyForEnum( entry.getKey() ), jsonizeValue( entry ), null );
    }
  }

  @Override
  public void renderInitialization( Widget widget ) throws IOException {
    Video video = ( Video ) widget;
    IClientObject clientObject = ClientObjectFactory.getClientObject( video );
    clientObject.create( TYPE );
    clientObject.set( PARENT, WidgetUtil.getId( video.getParent() ) );
    clientObject.set( VIDEO_URL, video.getURL().toString() );
  }

  @Override
  public void renderDispose( Widget widget ) throws IOException {
    ClientObjectFactory.getClientObject( widget ).destroy();
  }

  private static Object jsonizeValue( Entry<PlaybackOptions, Object> entry ) {
    Object value = entry.getValue();
    if( value instanceof PlaybackMode || value instanceof PresentationMode ) {
      value = keyForEnum( ( ( Enum )value ) );
    }
    return value;
  }
  
  static String keyForEnum( Enum<?> type ) {
    return type.name().toLowerCase();
  }
}