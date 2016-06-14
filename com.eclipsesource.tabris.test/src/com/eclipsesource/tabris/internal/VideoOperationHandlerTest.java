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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InOrder;

import com.eclipsesource.tabris.test.util.TabrisEnvironment;
import com.eclipsesource.tabris.widgets.PlaybackListener;
import com.eclipsesource.tabris.widgets.Video;
import com.eclipsesource.tabris.widgets.Video.PlayerState;

@SuppressWarnings("restriction")
public class VideoOperationHandlerTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private Video video;
  private VideoOperationHandler operationHandler;

  @Before
  public void setUp() {
    Shell shell = new Shell( new Display() );
    video = new Video( shell, "http://foo/bar" );
    operationHandler = new VideoOperationHandler( video );
  }

  @Test
  public void testNotifiesSinglePlaybackListener() {
    PlaybackListener listener = mock( PlaybackListener.class );
    video.addPlaybackListener( listener );

    operationHandler.handleNotify( "Playback", new JsonObject().add( "state", "finished" ) );

    verify( listener ).playbackChanged( PlayerState.FINISHED );
  }

  @Test
  public void testNotifiesMultiplePlaybackListener() {
    PlaybackListener listener1 = mock( PlaybackListener.class );
    PlaybackListener listener2 = mock( PlaybackListener.class );
    video.addPlaybackListener( listener1 );
    video.addPlaybackListener( listener2 );

    operationHandler.handleNotify( "Playback", new JsonObject().add( "state", "finished" ) );

    InOrder order = inOrder( listener1, listener2 );
    order.verify( listener1 ).playbackChanged( PlayerState.FINISHED );
    order.verify( listener2 ).playbackChanged( PlayerState.FINISHED );
  }

  @Test
  public void testDoesNotNotifyPlaybackListenerForOtherEvent() {
    PlaybackListener listener = mock( PlaybackListener.class );
    video.addPlaybackListener( listener );

    operationHandler.handleNotify( "FocusOut", null );

    verify( listener, never() ).playbackChanged( any( PlayerState.class) );
  }

  @Test
  public void testSetsSpeed() {
    operationHandler.handleSet( video, new JsonObject().add( "speed", 0.8f ) );

    assertEquals( 0.8f, video.getSpeed(), 0.0f );
  }

}
