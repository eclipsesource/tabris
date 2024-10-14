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

import static org.eclipse.rap.rwt.internal.lifecycle.DisplayUtil.getLCA;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.service.UISessionImpl;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.test.util.TabrisEnvironment;
import com.eclipsesource.tabris.widgets.PlaybackListener;
import com.eclipsesource.tabris.widgets.Video;

@SuppressWarnings("restriction")
public class VideoRemoteAdapterTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private Display display;
  private Video video;
  private RemoteObject remoteObject;


  @Before
  public void setUp() {
    display = new Display();
    Shell shell = new Shell( display );
    remoteObject = mock( RemoteObject.class );
    Connection connection = mock( Connection.class );
    when( connection.createRemoteObject( anyString() ) ).thenReturn( remoteObject );
    replaceConnection( connection );
    video = new Video( shell, "http://foo/bar" );
  }

  @Test
  public void testRenderControlsVisible_initially() {
    render();

    verify( remoteObject, never() ).set( eq( "controlsVisible" ), anyBoolean() );
  }

  @Test
  public void testRenderControlsVisible_changed() {
    video.setPlayerControlsVisible( true );

    render();

    verify( remoteObject ).set( "controlsVisible", true );
  }

  @Test
  public void testRenderControlsVisible_unchanged() {
    video.setPlayerControlsVisible( true );
    render();
    reset( remoteObject );

    render();

    verify( remoteObject, never() ).set( eq( "controlsVisible" ), anyBoolean() );
  }

  @Test
  public void testRenderControlsVisible_changedBack() {
    video.setPlayerControlsVisible( true );
    video.setPlayerControlsVisible( false );
    render();

    verify( remoteObject, never() ).set( eq( "controlsVisible" ), anyBoolean() );
  }

  @Test
  public void testRenderPlaybackSpeed_initially() {
    render();

    verify( remoteObject, never() ).set( eq( "speed" ), anyFloat() );
  }

  @Test
  public void testRenderPlaybackSpeed_changed() {
    video.setSpeed( 0.8f );

    render();

    verify( remoteObject ).set( "speed", 0.8f );
  }

  @Test
  public void testRenderPlaybackSpeed_unchanged() {
    video.setSpeed( 0.8f );
    render();
    reset( remoteObject );

    render();

    verify( remoteObject, never() ).set( eq( "speed" ), anyFloat() );
  }

  @Test
  public void testRenderPlaybackSpeed_changedBack() {
    video.setSpeed( 0.8f );
    video.setSpeed( Video.PAUSE_SPEED );
    render();

    verify( remoteObject, never() ).set( eq( "speed" ), anyFloat() );
  }

  @Test
  public void testRenderPlaybackListener_initially() {
    render();

    verify( remoteObject, never() ).listen( eq( "Playback" ), anyBoolean() );
  }

  @Test
  public void testRenderPlaybackListener_changed() {
    video.addPlaybackListener( mock( PlaybackListener.class ) );

    render();

    verify( remoteObject ).listen( "Playback", true );
  }

  @Test
  public void testRenderPlaybackListener_unchanged() {
    video.addPlaybackListener( mock( PlaybackListener.class ) );
    render();
    reset( remoteObject );

    render();

    verify( remoteObject, never() ).listen( eq( "Playback" ), anyBoolean() );
  }

  @Test
  public void testRenderPlaybackListener_changedBack() {
    PlaybackListener listener = mock( PlaybackListener.class );
    video.addPlaybackListener( listener );
    video.removePlaybackListener( listener );
    render();

    verify( remoteObject, never() ).listen( eq( "Playback" ), anyBoolean() );
  }

  @Test
  public void testRenderBounds_initially() {
    render();

    verify( remoteObject, never() ).set( eq( "bounds" ), any( JsonArray.class ) );
  }

  @Test
  public void testRenderBounds_changed() throws Exception {
    video.setBounds( 1, 2, 3, 4 );

    render();

    JsonArray expected = new JsonArray().add( 0 ).add( 0 ).add( 3 ).add( 4 );
    verify( remoteObject ).set( eq( "bounds" ), eq( expected ) );
  }

  @Test
  public void testRenderBounds_unchanged() {
    video.setBounds( 1, 2, 3, 4 );
    render();
    reset( remoteObject );

    render();

    verify( remoteObject, never() ).set( eq( "bounds" ), any( JsonArray.class ) );
  }

  @Test
  public void testRenderBounds_changedBack() {
    video.setBounds( 1, 2, 3, 4 );
    video.setBounds( 0, 0, 0, 0 );
    render();

    verify( remoteObject, never() ).set( eq( "bounds" ), any( JsonArray.class ) );
  }

  @Test
  public void testRenderStepToTime() {
    video.stepToTime( 5 );

    render();

    verify( remoteObject ).call( "stepToTime", new JsonObject().add( "time", 5 ) );
  }

  @Test
  public void testRenderStepToTime_onlyOnce() {
    video.stepToTime( 5 );
    video.stepToTime( 10 );

    render();

    verify( remoteObject, times( 1 ) ).call( eq( "stepToTime" ), any( JsonObject.class ) );
  }

  @Test
  public void testRenderStepToTime_suppressesSkipFromCurrent() {
    video.skipFromCurrent( 10 );
    video.stepToTime( 5 );

    render();

    verify( remoteObject, never() ).call( eq( "skip" ), any( JsonObject.class ) );
    verify( remoteObject ).call( "stepToTime", new JsonObject().add( "time", 5 ) );
  }

  @Test
  public void testRenderSkipFromCurrent() {
    video.skipFromCurrent( 5 );

    render();

    verify( remoteObject ).call( "skip", new JsonObject().add( "time", 5 ) );
  }

  @Test
  public void testRenderSkipFromCurrent_onlyOnce() {
    video.skipFromCurrent( 5 );
    video.skipFromCurrent( 10 );

    render();

    verify( remoteObject, times( 1 ) ).call( eq( "skip" ), any( JsonObject.class ) );
  }

  @Test
  public void testRenderSkipFromCurrent_suppressesStepToFrame() {
    video.stepToTime( 5 );
    video.skipFromCurrent( 10 );

    render();

    verify( remoteObject ).call( "skip", new JsonObject().add( "time", 10 ) );
    verify( remoteObject, never() ).call( eq( "stepToTime" ), any( JsonObject.class ) );
  }

  @Test
  public void testRenderClearCache() {
    video.clearCache();

    render();

    verify( remoteObject ).call( "clearCache", null );
  }

  @Test
  public void testRenderClearCache_onlyOnce() {
    video.clearCache();
    video.clearCache();

    render();

    verify( remoteObject, times( 1 ) ).call( "clearCache", null );
  }

  private void render() {
    try {
      getLCA( display ).render( display );
    } catch( IOException shouldNotHappen ) {
      fail( shouldNotHappen.getMessage() );
    }
  }

  private void replaceConnection( Connection connection ) {
    UISessionImpl uiSession = ( UISessionImpl )RWT.getUISession();
    uiSession.setConnection( connection );
  }

}
