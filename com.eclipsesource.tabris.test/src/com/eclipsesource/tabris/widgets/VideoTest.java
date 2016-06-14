/*******************************************************************************
 * Copyright (c) 2012, 2016 EclipseSource and others.
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

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.test.util.TabrisEnvironment;


public class VideoTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private Shell parent;
  private Video video;

  @Before
  public void setUp() {
    Display display = new Display();
    parent = new Shell( display );
    video = new Video( parent, "http://localhost/video.mp4" );
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
  public void testHasntPlaybackListenerDefault() {
    assertTrue( video.getPlaybackListeners().isEmpty() );
  }

  @Test
  public void testHasPlaybackListener() {
    video.addPlaybackListener( mock( PlaybackListener.class ) );

    assertEquals( 1, video.getPlaybackListeners().size() );
  }

  @Test
  public void testHasPlaybackListenerWithAddAndRemove() {
    video = new Video( parent, "http://localhost/video.mp4" );

    PlaybackListener listener = mock( PlaybackListener.class );
    video.addPlaybackListener( listener );
    video.removePlaybackListener( listener );

    assertTrue( video.getPlaybackListeners().isEmpty() );
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

}
