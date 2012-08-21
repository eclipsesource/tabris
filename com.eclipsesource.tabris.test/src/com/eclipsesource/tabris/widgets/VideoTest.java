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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rwt.lifecycle.ILifeCycleAdapter;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import com.eclipsesource.tabris.internal.VideoLifeCycleAdapter;
import com.eclipsesource.tabris.internal.VideoLifeCycleAdapter.PlaybackOptions;
import com.eclipsesource.tabris.widgets.Video.PlaybackAdapter;
import com.eclipsesource.tabris.widgets.Video.PlaybackMode;
import com.eclipsesource.tabris.widgets.Video.PresentationMode;


public class VideoTest {
  
  private Video video;
  private Shell parent;
  private VideoListener videoListener;

  @Before
  public void setUp() {
    Fixture.setUp();
    Display display = new Display();
    parent = new Shell( display );
    video = new Video( "http://test.com", parent );
    videoListener = mock( VideoListener.class );
    video.addVideoListener( videoListener );
  }
  
  @After
  public void tearDown() {
    Fixture.tearDown();
  }
  
  @Test
  public void testGetUrl() throws MalformedURLException {
    assertEquals( new URL( "http://test.com" ), video.getURL() );
  }
  
  @Test
  public void testGetVideoAdapter() {
    ILifeCycleAdapter adapter = video.getAdapter( ILifeCycleAdapter.class );
    
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
    assertEquals( PlaybackMode.PAUSE, video.getPlaybackMode() );
  }
  
  @Test
  public void testPlay() {
    video.play();
    
    assertEquals( PlaybackMode.PLAY, video.getPlaybackMode() );
  }
  
  @Test
  public void testPause() {
    video.pause();
    
    assertEquals( PlaybackMode.PAUSE, video.getPlaybackMode() );
  }
  
  @Test
  public void testStop() {
    video.stop();
    
    assertEquals( PlaybackMode.STOP, video.getPlaybackMode() );
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
    assertEquals( PlaybackMode.FAST_FORWARD, video.getPlaybackMode() );
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
    assertEquals( PlaybackMode.FAST_BACKWARD, video.getPlaybackMode() );
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
    assertEquals( PresentationMode.EMBEDDED, video.getPresentationMode() );
  }
  
  @Test
  public void testFullScreenPresentationMode() {
    video.setFullscreen( true );
    
    assertEquals( PresentationMode.FULL_SCREEN, video.getPresentationMode() );
  }
  
  @Test
  public void testEmbeddedPresentationMode() {
    video.setFullscreen( true );
    video.setFullscreen( false );
    
    assertEquals( PresentationMode.EMBEDDED, video.getPresentationMode() );
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
  public void testDefaultAutoPlay() {
    assertFalse( video.hasAutoPlay() );
  }
  
  @Test
  public void testAutoPlay() {
    video.setAutoPlay( true );
    
    assertTrue( video.hasAutoPlay() );
  }
  
  @Test
  public void testAutoPlayAfterUnAutoPlay() {
    video.setAutoPlay( true );
    video.setAutoPlay( false );
    
    assertFalse( video.hasAutoPlay() );
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
  public void testAddVideoListener() {
    VideoListener listener = mock( VideoListener.class );
    VideoListener listener2 = mock( VideoListener.class );
    video.addVideoListener( listener );
    video.addVideoListener( listener2 );
    
    video.play();
    
    verify( listener ).playbackChanged( PlaybackMode.PLAY );
    verify( listener2 ).playbackChanged( PlaybackMode.PLAY );
  }
  
  @Test
  public void testRemoveVideoListener() {
    VideoListener listener = mock( VideoListener.class );
    VideoListener listener2 = mock( VideoListener.class );
    video.addVideoListener( listener );
    video.addVideoListener( listener2 );
    video.removeVideoListener( listener );
    
    video.play();
    
    verify( listener, never() ).playbackChanged( PlaybackMode.PLAY );
    verify( listener2 ).playbackChanged( PlaybackMode.PLAY );
  }
  
  @Test
  public void testPlaysThrowsEvent() {
    video.play();
    
    verify( videoListener ).playbackChanged( PlaybackMode.PLAY );
  }
  
  @Test
  public void testPauseThrowsEvent() {
    video.stop();
    video.pause();
    
    InOrder order = inOrder( videoListener );
    order.verify( videoListener ).playbackChanged( PlaybackMode.STOP );
    order.verify( videoListener ).playbackChanged( PlaybackMode.PAUSE );
  }
  
  @Test
  public void testStopThrowsEvent() {
    video.stop();
    
    verify( videoListener ).playbackChanged( PlaybackMode.STOP );
  }
  
  @Test
  public void testFastForwardThrowsEvent() {
    video.fastForward( 2 );
    
    verify( videoListener ).playbackChanged( PlaybackMode.FAST_FORWARD );
  }
  
  @Test
  public void testFastBackwardThrowsEvent() {
    video.fastBackward( -2 );
    
    verify( videoListener ).playbackChanged( PlaybackMode.FAST_BACKWARD );
  }
  
  @Test
  public void testSetFullscreenThrowsEvent() {
    video.setFullscreen( true );
    
    verify( videoListener ).presentationChanged( PresentationMode.FULL_SCREEN );
  }
  
  @Test
  public void testSetFullscreenWithEmbeddedThrowsEvent() {
    video.setFullscreen( true );
    video.setFullscreen( false );
    
    InOrder order = inOrder( videoListener );
    order.verify( videoListener ).presentationChanged( PresentationMode.FULL_SCREEN );
    order.verify( videoListener ).presentationChanged( PresentationMode.EMBEDDED );
  }
  
  @Test
  public void testFiresPresentationEventOnlyOnce() {
    video.setFullscreen( true );
    video.setFullscreen( true );
    
    verify( videoListener ).presentationChanged( PresentationMode.FULL_SCREEN );
  }
  
  @Test
  public void testFiresPlaybackEventOnlyOnce() {
    video.play();
    video.play();
    
    verify( videoListener ).playbackChanged( PlaybackMode.PLAY );
  }
  
  @Test
  public void testPlaybackAdapterPlaybackModeChange() {
    PlaybackAdapter adapter = video.getAdapter( PlaybackAdapter.class );
    
    adapter.firePlaybackChange( PlaybackMode.ERROR );
    
    verify( videoListener ).playbackChanged( PlaybackMode.ERROR );
  }
  
  @Test
  public void testPlaybackAdapterPresentationModeChange() {
    PlaybackAdapter adapter = video.getAdapter( PlaybackAdapter.class );
    
    adapter.firePresentationChange( PresentationMode.EMBEDDED );
    
    verify( videoListener ).presentationChanged( PresentationMode.EMBEDDED );
  }
  
  @Test
  public void testHasListenerDefault() {
    video = new Video( "http://test.com", parent );
    PlaybackAdapter adapter = video.getAdapter( PlaybackAdapter.class );
    
    assertFalse( adapter.hasVideoListener() );
  }
  
  @Test
  public void testHasListener() {
    video = new Video( "http://test.com", parent );
    video.addVideoListener( mock( VideoListener.class ) );
    PlaybackAdapter adapter = video.getAdapter( PlaybackAdapter.class );
    
    assertTrue( adapter.hasVideoListener() );
  }
  
  @Test
  public void testHasListenerWithAddAndRemove() {
    video = new Video( "http://test.com", parent );
    VideoListener listener = mock( VideoListener.class );
    video.addVideoListener( listener );
    video.removeVideoListener( listener );
    PlaybackAdapter adapter = video.getAdapter( PlaybackAdapter.class );
    
    assertFalse( adapter.hasVideoListener() );
  }
  
}
