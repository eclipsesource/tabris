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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.rap.rwt.internal.lifecycle.WidgetLifeCycleAdapter;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.internal.VideoLifeCycleAdapter;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;
import com.eclipsesource.tabris.widgets.Video.PlayerState;
import com.eclipsesource.tabris.widgets.Video.VideoStateAdapter;


@SuppressWarnings("restriction")
public class VideoTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private static final float DISABLED = Float.NEGATIVE_INFINITY;
  private Shell parent;
  private Video video;
  private VideoStateAdapter adapter;

  @Before
  public void setUp() {
    Display display = new Display();
    parent = new Shell( display );
    video = new Video( parent, "http://localhost/video.mp4" );
    adapter = video.getAdapter( VideoStateAdapter.class );
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
  public void testGetUrl() throws MalformedURLException {
    assertEquals( new URL( "http://localhost/video.mp4" ).toString(), video.getURL().toString() );
  }

  @Test
  public void testGetVideoAdapter() {
    WidgetLifeCycleAdapter adapter = video.getAdapter( WidgetLifeCycleAdapter.class );

    assertTrue( adapter instanceof VideoLifeCycleAdapter );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testMallformedUrl() {
    new Video( parent, "foo.bar" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testNullParent() {
    new Video( null, "http://localhost/video.mp4" );
  }

  @Test
  public void testDefaultSpeed() {
    assertEquals( 0.0f, video.getSpeed(), 0.0f );
  }

  @Test
  public void testSetSpeed() {
    video.setSpeed( Video.PLAY_SPEED );

    assertEquals( Video.PLAY_SPEED, video.getSpeed(), 0.0f );
  }

  @Test
  public void testSetPlayerControlsVisisbleDefault() {
    assertFalse( video.hasPlayerControlsVisible() );
  }

  @Test
  public void testSetPlayerControlsVisisble() {
    video.setPlayerControlsVisible( true );

    assertTrue( video.hasPlayerControlsVisible() );
  }

  @Test
  public void testSetPlayerControlsVisibleAfterInvisible() {
    video.setPlayerControlsVisible( false );
    video.setPlayerControlsVisible( true );

    assertTrue( video.hasPlayerControlsVisible() );
  }

  @Test
  public void testStepToTime() {
    video.stepToTime( 20.0f );

    assertEquals( 20.0f, adapter.getStepToTime(), 0.0f );
  }

  @Test
  public void testStepToTimeWithNegativeValue() {
    video.stepToTime( -1.0f );

    assertEquals( 0.0f, adapter.getStepToTime(), 0.0f );
  }

  @Test
  public void testStepToTimeDefault() {
    assertEquals( DISABLED, adapter.getStepToTime(), 0.0f );
  }

  @Test
  public void testStepToTimeAfterSkipFromCurrent() {
    video.skipFromCurrent( 10.0f );
    video.stepToTime( 5.0f );

    assertEquals( 5.0f, adapter.getStepToTime(), 0.0f );
    assertEquals( DISABLED, adapter.getSkipFromCurrent(), 0.0f );
  }

  @Test
  public void testSkipFromCurrentDefault() {
    assertEquals( DISABLED, adapter.getStepToTime(), 0.0f );
  }

  @Test
  public void testSkipFromCurrentAfterStepToTime() {
    video.stepToTime( 25.0f );
    video.skipFromCurrent( -10.0f );

    assertEquals( DISABLED, adapter.getStepToTime(), 0.0f );
    assertEquals( -10.0f, adapter.getSkipFromCurrent(), 0.0f );
  }

  @Test
  public void testAddPlaybackListener() {
    PlaybackListener listener = mock( PlaybackListener.class );
    PlaybackListener listener2 = mock( PlaybackListener.class );
    video.addPlaybackListener( listener );
    video.addPlaybackListener( listener2 );

    adapter.notifyPlaybackListeners( PlayerState.READY );

    verify( listener ).playbackChanged( PlayerState.READY );
    verify( listener2 ).playbackChanged( PlayerState.READY );
  }

  @Test
  public void testRemovePlaybackListener() {
    PlaybackListener listener = mock( PlaybackListener.class );
    PlaybackListener listener2 = mock( PlaybackListener.class );
    video.addPlaybackListener( listener );
    video.addPlaybackListener( listener2 );
    video.removePlaybackListener( listener );

    adapter.notifyPlaybackListeners( PlayerState.READY );

    verify( listener, never() ).playbackChanged( PlayerState.READY );
    verify( listener2 ).playbackChanged( PlayerState.READY );
  }

  @Test
  public void testHasntPlaybackListenerDefault() {
    assertFalse( adapter.hasPlaybackListener() );
  }

  @Test
  public void testHasPlaybackListener() {
    video.addPlaybackListener( mock( PlaybackListener.class ) );

    assertTrue( adapter.hasPlaybackListener() );
  }

  @Test
  public void testHasPlaybackListenerWithAddAndRemove() {
    video = new Video( parent, "http://localhost/video.mp4" );

    PlaybackListener listener = mock( PlaybackListener.class );
    video.addPlaybackListener( listener );
    video.removePlaybackListener( listener );

    VideoStateAdapter adapter = video.getAdapter( VideoStateAdapter.class );
    assertFalse( adapter.hasPlaybackListener() );
  }

  @Test
  public void testCacheSizeDefaultsToZero() {
    video = new Video( parent, "http://localhost/video.mp4" );

    assertEquals( 0, video.getCacheSize() );
  }

  @Test
  public void testGetCacheSize() {
    video = new Video( parent, "http://localhost/video.mp4", 100 );

    assertEquals( 100, video.getCacheSize() );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetCacheSize_IllegalValue() {
    video = new Video( parent, "http://localhost/video.mp4", -1 );
  }

  @Test
  public void testClearCache() {
    assertFalse( adapter.isClearCacheRequested() );

    video.clearCache();

    assertTrue( adapter.isClearCacheRequested() );
  }

  @Test
  public void testResetClearCache() {
    video.clearCache();

    assertTrue( adapter.isClearCacheRequested() );

    adapter.resetClearCache();

    assertFalse( adapter.isClearCacheRequested() );
  }

  @Test
  public void testStepToTimeRequested() {
    assertFalse( adapter.isStepToTimeRequested() );

    video.stepToTime( 60.0f );

    assertTrue( adapter.isStepToTimeRequested() );
  }

  @Test
  public void testResetStepToTime() {
    video.stepToTime( 60.0f );

    assertTrue( adapter.isStepToTimeRequested() );

    adapter.resetStepToTime();

    assertFalse( adapter.isStepToTimeRequested() );
  }

  @Test
  public void testSkipFromCurrentRequested() {
    assertFalse( adapter.isSkipFromCurrentRequested() );

    video.skipFromCurrent( -5.0f );

    assertTrue( adapter.isSkipFromCurrentRequested() );
  }

  @Test
  public void testResetSkipFromCurrent() {
    video.skipFromCurrent( 10.0f );

    assertTrue( adapter.isSkipFromCurrentRequested() );

    adapter.resetSkipFromCurrent();

    assertFalse( adapter.isSkipFromCurrentRequested() );
  }
}
