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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.eclipse.rap.rwt.internal.lifecycle.WidgetLifeCycleAdapter;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InOrder;

import com.eclipsesource.tabris.internal.VideoLifeCycleAdapter;
import com.eclipsesource.tabris.internal.VideoLifeCycleAdapter.PlaybackOptions;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;
import com.eclipsesource.tabris.widgets.Video.Playback;
import com.eclipsesource.tabris.widgets.Video.PlaybackAdapter;
import com.eclipsesource.tabris.widgets.Video.Presentation;


@SuppressWarnings("restriction")
public class VideoTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private Video video;
  private Shell parent;
  private PlaybackListener playbackListener;
  private PresentationListener presentationListener;

  @Before
  public void setUp() {
    Display display = new Display();
    parent = new Shell( display );
    video = new Video( "http://test.com", parent );
    playbackListener = mock( PlaybackListener.class );
    video.addPlaybackListener( playbackListener );
    presentationListener = mock( PresentationListener.class );
    video.addPresentationListener( presentationListener );
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( Video.class ) );
  }

  @Test
  public void testPlaybackListenerIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( PlaybackListener.class ) );
  }

  @Test
  public void testPresentationListenerIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( PresentationListener.class ) );
  }

  @Test
  public void testGetUrl() throws MalformedURLException {
    assertEquals( new URL( "http://test.com" ).toString(), video.getURL().toString() );
  }

  @Test
  public void testGetVideoAdapter() {
    WidgetLifeCycleAdapter adapter = video.getAdapter( WidgetLifeCycleAdapter.class );

    assertTrue( adapter instanceof VideoLifeCycleAdapter );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testMallformedUrl() {
    new Video( "foo.bar", parent );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testNullParent() {
    new Video( "http://test.com", null );
  }

  @Test
  public void testDefaultPlaybackMode() {
    assertEquals( Playback.PAUSE, video.getPlayback() );
  }

  @Test
  public void testPlay() {
    video.play();

    assertEquals( Playback.PLAY, video.getPlayback() );
  }

  @Test
  public void testPause() {
    video.pause();

    assertEquals( Playback.PAUSE, video.getPlayback() );
  }

  @Test
  public void testStop() {
    video.stop();

    assertEquals( Playback.STOP, video.getPlayback() );
  }

  @Test
  public void testDefaultSpeed() {
    assertEquals( 0, video.getSpeed(), 0 );
  }

  @Test
  public void testDefaultPlaySpeed() {
    video.play();

    assertEquals( 1, video.getSpeed(), 0 );
  }

  @Test
  public void testDefaultStopSpeed() {
    video.play();
    video.stop();

    assertEquals( 0, video.getSpeed(), 0 );
  }

  @Test
  public void testDefaultPauseSpeed() {
    video.play();
    video.pause();

    assertEquals( 0, video.getSpeed(), 0 );
  }

  @Test
  public void testFastForward() {
    video.fastForward( 2.5F );

    assertEquals( 2.5, video.getSpeed(), 0 );
    assertEquals( Playback.FAST_FORWARD, video.getPlayback() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFastForwardWithToSmallValue() {
    video.fastForward( 0.4F );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFastForwardWithNegativeValue() {
    video.fastForward( -0.4F );
  }

  @Test
  public void testFastBackward() {
    video.fastBackward( -2.5F );

    assertEquals( -2.5, video.getSpeed(), 0 );
    assertEquals( Playback.FAST_BACKWARD, video.getPlayback() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFastBackwardWithToSmallValue() {
    video.fastBackward( -0.4F );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFastBackwardWithNegativeValue() {
    video.fastBackward( 0.4F );
  }

  @Test
  public void testDefaultPresentationMode() {
    assertEquals( Presentation.EMBEDDED, video.getPresentation() );
  }

  @Test
  public void testFullScreenPresentationMode() {
    video.setFullscreen( true );

    assertEquals( Presentation.FULL_SCREEN, video.getPresentation() );
  }

  @Test
  public void testEmbeddedPresentationMode() {
    video.setFullscreen( true );
    video.setFullscreen( false );

    assertEquals( Presentation.EMBEDDED, video.getPresentation() );
  }

  @Test
  public void testSetPlayerControlsVisisbleDefault() {
    assertTrue( video.hasPlayerControlsVisible() );
  }

  @Test
  public void testSetPlayerControlsVisisble() {
    video.setPlayerControlsVisible( false );

    assertFalse( video.hasPlayerControlsVisible() );
  }

  @Test
  public void testSetPlayerControlsVisisbleAfterInvisible() {
    video.setPlayerControlsVisible( false );
    video.setPlayerControlsVisible( true );

    assertTrue( video.hasPlayerControlsVisible() );
  }

  @Test
  public void testDefaultRepeat() {
    assertFalse( video.hasRepeat() );
  }

  @Test
  public void testRepeat() {
    video.setRepeat( true );

    assertTrue( video.hasRepeat() );
  }

  @Test
  public void testRepeatAfterUnrepeat() {
    video.setRepeat( true );
    video.setRepeat( false );

    assertFalse( video.hasRepeat() );
  }

  @Test
  public void testStepTo() {
    video.stepToTime( 20 );

    Map<PlaybackOptions, Object> options = video.getAdapter( PlaybackAdapter.class ).getOptions();
    assertEquals( Integer.valueOf( 20 ), options.get( VideoLifeCycleAdapter.PlaybackOptions.HEAD_POSITION ) );
  }

  @Test
  public void testStepToDefault() {
    Map<PlaybackOptions, Object> options = video.getAdapter( PlaybackAdapter.class ).getOptions();
    assertNull( options.get( VideoLifeCycleAdapter.PlaybackOptions.HEAD_POSITION ) );
  }

  @Test
  public void testAddPlaybackListener() {
    PlaybackListener listener = mock( PlaybackListener.class );
    PlaybackListener listener2 = mock( PlaybackListener.class );
    video.addPlaybackListener( listener );
    video.addPlaybackListener( listener2 );

    video.play();

    verify( listener ).playbackChanged( Playback.PLAY );
    verify( listener2 ).playbackChanged( Playback.PLAY );
  }

  @Test
  public void testRemovePlaybackListener() {
    PlaybackListener listener = mock( PlaybackListener.class );
    PlaybackListener listener2 = mock( PlaybackListener.class );
    video.addPlaybackListener( listener );
    video.addPlaybackListener( listener2 );
    video.removePlaybackListener( listener );

    video.play();

    verify( listener, never() ).playbackChanged( Playback.PLAY );
    verify( listener2 ).playbackChanged( Playback.PLAY );
  }

  @Test
  public void testAddPresentationListener() {
    PresentationListener listener = mock( PresentationListener.class );
    PresentationListener listener2 = mock( PresentationListener.class );
    video.addPresentationListener( listener );
    video.addPresentationListener( listener2 );

    video.setFullscreen( true );

    verify( listener ).presentationChanged( Presentation.FULL_SCREEN );
    verify( listener2 ).presentationChanged( Presentation.FULL_SCREEN );
  }

  @Test
  public void testRemovePresentationListener() {
    PresentationListener listener = mock( PresentationListener.class );
    PresentationListener listener2 = mock( PresentationListener.class );
    video.addPresentationListener( listener );
    video.addPresentationListener( listener2 );
    video.removePresentationListener( listener );

    video.setFullscreen( true );

    verify( listener, never() ).presentationChanged( Presentation.FULL_SCREEN );
    verify( listener2 ).presentationChanged( Presentation.FULL_SCREEN );
  }

  @Test
  public void testPlaysThrowsEvent() {
    video.play();

    verify( playbackListener ).playbackChanged( Playback.PLAY );
  }

  @Test
  public void testPauseThrowsEvent() {
    video.stop();
    video.pause();

    InOrder order = inOrder( playbackListener );
    order.verify( playbackListener ).playbackChanged( Playback.STOP );
    order.verify( playbackListener ).playbackChanged( Playback.PAUSE );
  }

  @Test
  public void testStopThrowsEvent() {
    video.stop();

    verify( playbackListener ).playbackChanged( Playback.STOP );
  }

  @Test
  public void testFastForwardThrowsEvent() {
    video.fastForward( 2 );

    verify( playbackListener ).playbackChanged( Playback.FAST_FORWARD );
  }

  @Test
  public void testFastBackwardThrowsEvent() {
    video.fastBackward( -2 );

    verify( playbackListener ).playbackChanged( Playback.FAST_BACKWARD );
  }

  @Test
  public void testSetFullscreenThrowsEvent() {
    video.setFullscreen( true );

    verify( presentationListener ).presentationChanged( Presentation.FULL_SCREEN );
  }

  @Test
  public void testSetFullscreenWithEmbeddedThrowsEvent() {
    video.setFullscreen( true );
    video.setFullscreen( false );

    InOrder order = inOrder( presentationListener );
    order.verify( presentationListener ).presentationChanged( Presentation.FULL_SCREEN );
    order.verify( presentationListener ).presentationChanged( Presentation.EMBEDDED );
  }

  @Test
  public void testFiresPresentationEventOnlyOnce() {
    video.setFullscreen( true );
    video.setFullscreen( true );

    verify( presentationListener ).presentationChanged( Presentation.FULL_SCREEN );
  }

  @Test
  public void testFiresPlaybackEventOnlyOnce() {
    video.play();
    video.play();

    verify( playbackListener ).playbackChanged( Playback.PLAY );
  }

  @Test
  public void testPlaybackAdapterPlaybackModeChange() {
    PlaybackAdapter adapter = video.getAdapter( PlaybackAdapter.class );

    adapter.firePlaybackChange( Playback.ERROR );

    verify( playbackListener ).playbackChanged( Playback.ERROR );
  }

  @Test
  public void testPlaybackAdapterPresentationModeChange() {
    PlaybackAdapter adapter = video.getAdapter( PlaybackAdapter.class );

    adapter.firePresentationChange( Presentation.EMBEDDED );

    verify( presentationListener ).presentationChanged( Presentation.EMBEDDED );
  }

  @Test
  public void testHasntPlaybackListenerDefault() {
    video = new Video( "http://test.com", parent );
    PlaybackAdapter adapter = video.getAdapter( PlaybackAdapter.class );

    assertFalse( adapter.hasPlaybackListener() );
  }

  @Test
  public void testHasntPresentationListenerDefault() {
    video = new Video( "http://test.com", parent );
    PlaybackAdapter adapter = video.getAdapter( PlaybackAdapter.class );

    assertFalse( adapter.hasPresentationListener() );
  }

  @Test
  public void testHasPlaybackListener() {
    video = new Video( "http://test.com", parent );
    video.addPlaybackListener( mock( PlaybackListener.class ) );
    PlaybackAdapter adapter = video.getAdapter( PlaybackAdapter.class );

    assertTrue( adapter.hasPlaybackListener() );
  }

  @Test
  public void testHasPresentationListener() {
    video = new Video( "http://test.com", parent );
    video.addPresentationListener( mock( PresentationListener.class ) );
    PlaybackAdapter adapter = video.getAdapter( PlaybackAdapter.class );

    assertTrue( adapter.hasPresentationListener() );
  }

  @Test
  public void testHasPlaybackListenerWithAddAndRemove() {
    video = new Video( "http://test.com", parent );
    PlaybackListener listener = mock( PlaybackListener.class );
    PlaybackAdapter adapter = video.getAdapter( PlaybackAdapter.class );

    video.addPlaybackListener( listener );
    video.removePlaybackListener( listener );

    assertFalse( adapter.hasPlaybackListener() );
  }

  @Test
  public void testHasPresentationListenerWithAddAndRemove() {
    video = new Video( "http://test.com", parent );
    PresentationListener listener = mock( PresentationListener.class );
    PlaybackAdapter adapter = video.getAdapter( PlaybackAdapter.class );

    video.addPresentationListener( listener );
    video.removePresentationListener( listener );

    assertFalse( adapter.hasPresentationListener() );
  }

}
