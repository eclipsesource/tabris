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

import static com.eclipsesource.tabris.internal.VideoLifeCycleAdapter.keyForEnum;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rap.rwt.testfixture.Message;
import org.eclipse.rap.rwt.testfixture.Message.CreateOperation;
import org.eclipse.rap.rwt.testfixture.Message.ListenOperation;
import org.eclipse.rap.rwt.testfixture.Message.SetOperation;
import org.eclipse.rwt.lifecycle.AbstractWidgetLCA;
import org.eclipse.rwt.lifecycle.ILifeCycleAdapter;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.internal.VideoLifeCycleAdapter.PlaybackOptions;
import com.eclipsesource.tabris.test.ControlLCATestUtil;
import com.eclipsesource.tabris.widgets.Video;
import com.eclipsesource.tabris.widgets.Video.PlaybackMode;
import com.eclipsesource.tabris.widgets.Video.PresentationMode;
import com.eclipsesource.tabris.widgets.VideoListener;


public class VideoLifeCycleAdapterTest {
  
  private Video video;
  private Shell parent;
  private VideoListener videoListener;
  private AbstractWidgetLCA lifeCycleAdapter;

  @Before
  public void setUp() {
    Fixture.setUp();
    Display display = new Display();
    parent = new Shell( display );
    video = new Video( "http://test.com", parent );
    videoListener = mock( VideoListener.class );
    lifeCycleAdapter = ( AbstractWidgetLCA )video.getAdapter( ILifeCycleAdapter.class );
    Fixture.fakeNewRequest( display );
  }
  
  @After
  public void tearDown() {
    Fixture.tearDown();
  }
  
  @Test
  public void testControlListeners() throws IOException {
    ControlLCATestUtil.testActivateListener( video );
    ControlLCATestUtil.testFocusListener( video );
    ControlLCATestUtil.testMouseListener( video );
    ControlLCATestUtil.testKeyListener( video );
    ControlLCATestUtil.testTraverseListener( video );
    ControlLCATestUtil.testMenuDetectListener( video );
    ControlLCATestUtil.testHelpListener( video );
  }
  
  @Test
  public void testCreatesWidget() throws IOException {
    lifeCycleAdapter.renderInitialization( video );
    
    CreateOperation createOperation = Fixture.getProtocolMessage().findCreateOperation( video );
    assertEquals( video.getURL().toString(), createOperation.getProperty( VideoLifeCycleAdapter.VIDEO_URL ) );
  }
  
  @Test
  public void testCreatesParent() throws IOException {
    lifeCycleAdapter.renderInitialization( video );
    
    Message message = Fixture.getProtocolMessage();
    CreateOperation operation = message.findCreateOperation( video );
    assertEquals( WidgetUtil.getId( video.getParent() ), operation.getParent() );
  }
  
  @Test
  public void testType() throws IOException {
    lifeCycleAdapter.renderInitialization( video );
    
    Message message = Fixture.getProtocolMessage();
    CreateOperation operation = message.findCreateOperation( video );
    assertEquals( VideoLifeCycleAdapter.TYPE, operation.getType() );
  }
  
  @Test
  public void testFiresPlaybackChange() {
    video.addVideoListener( videoListener );
    Fixture.fakeRequestParam( WidgetUtil.getId( video ) + "." + keyForEnum( PlaybackOptions.PLAYBACK_MODE ), 
                              PlaybackMode.ERROR.name() );
    
    Fixture.executeLifeCycleFromServerThread();
    
    verify( videoListener ).playbackChanged( PlaybackMode.ERROR );
  }
  
  @Test
  public void testFiresPresentationChange() {
    video.addVideoListener( videoListener );
    Fixture.fakeRequestParam( WidgetUtil.getId( video ) + "." + keyForEnum( PlaybackOptions.PRESENTATION_MODE ), 
                              PresentationMode.EMBEDDED.name() );
   
    Fixture.executeLifeCycleFromServerThread();
    
    verify( videoListener ).presentationChanged( PresentationMode.EMBEDDED );
  }
  
  @Test
  public void testFiresPresentationChangeToFullScreen() {
    video.addVideoListener( videoListener );
    Fixture.fakeRequestParam( WidgetUtil.getId( video ) + "." + keyForEnum( PlaybackOptions.PRESENTATION_MODE ), 
                              PresentationMode.FULL_SCREEN.name() );
    
    Fixture.executeLifeCycleFromServerThread();
    
    verify( videoListener ).presentationChanged( PresentationMode.FULL_SCREEN );
  }
  
  @Test
  public void testRendersPlaybackMode() throws IOException {
    Fixture.markInitialized( video.getDisplay() );
    Fixture.markInitialized( video );
    Fixture.preserveWidgets();
    
    video.play();
    lifeCycleAdapter.renderChanges( video );
    
    Message message = Fixture.getProtocolMessage();
    SetOperation operation = message.findSetOperation( video, keyForEnum( PlaybackOptions.PLAYBACK_MODE ) );
    assertEquals( keyForEnum( PlaybackMode.PLAY ), operation.getProperty( keyForEnum( PlaybackOptions.PLAYBACK_MODE ) ) );
  }
  
  @Test
  public void testRendersPresentationMode() throws IOException {
    Fixture.markInitialized( video.getDisplay() );
    Fixture.markInitialized( video );
    Fixture.preserveWidgets();
    
    video.setFullscreen( true );
    lifeCycleAdapter.renderChanges( video );
    
    Message message = Fixture.getProtocolMessage();
    SetOperation operation = message.findSetOperation( video, keyForEnum( PlaybackOptions.PRESENTATION_MODE ) );
    assertEquals( keyForEnum( PresentationMode.FULL_SCREEN ), 
                  operation.getProperty( keyForEnum( PlaybackOptions.PRESENTATION_MODE ) ) );
  }
  
  @Test
  public void testRendersSpeedForward() throws IOException {
    Fixture.markInitialized( video.getDisplay() );
    Fixture.markInitialized( video );
    Fixture.preserveWidgets();
    
    video.fastForward( 2 );
    lifeCycleAdapter.renderChanges( video );
    
    Message message = Fixture.getProtocolMessage();
    SetOperation operation = message.findSetOperation( video, keyForEnum( PlaybackOptions.SPEED ) );
    assertEquals( Double.valueOf( 2 ), 
                  operation.getProperty( keyForEnum( PlaybackOptions.SPEED ) ) );
  }
  
  @Test
  public void testRendersSpeedBackward() throws IOException {
    Fixture.markInitialized( video.getDisplay() );
    Fixture.markInitialized( video );
    Fixture.preserveWidgets();
    
    video.fastBackward( -2 );
    lifeCycleAdapter.renderChanges( video );
    
    Message message = Fixture.getProtocolMessage();
    SetOperation operation = message.findSetOperation( video, keyForEnum( PlaybackOptions.SPEED ) );
    assertEquals( Double.valueOf( -2 ), 
                  operation.getProperty( keyForEnum( PlaybackOptions.SPEED ) ) );
  }
  
  @Test
  public void testRendersRepeat() throws IOException {
    Fixture.markInitialized( video.getDisplay() );
    Fixture.markInitialized( video );
    Fixture.preserveWidgets();
    
    video.setRepeat( true );
    lifeCycleAdapter.renderChanges( video );
    
    Message message = Fixture.getProtocolMessage();
    SetOperation operation = message.findSetOperation( video, keyForEnum( PlaybackOptions.REPEAT ) );
    assertEquals( Boolean.TRUE, 
                  operation.getProperty( keyForEnum( PlaybackOptions.REPEAT ) ) );
  }
  
  @Test
  public void testRendersAutoPlay() throws IOException {
    Fixture.markInitialized( video.getDisplay() );
    Fixture.markInitialized( video );
    Fixture.preserveWidgets();
    
    video.setAutoPlay( true );
    lifeCycleAdapter.renderChanges( video );
    
    Message message = Fixture.getProtocolMessage();
    SetOperation operation = message.findSetOperation( video, keyForEnum( PlaybackOptions.AUTOPLAY ) );
    assertEquals( Boolean.TRUE, 
                  operation.getProperty( keyForEnum( PlaybackOptions.AUTOPLAY ) ) );
  }
  
  @Test
  public void testRendersControlsVisible() throws IOException {
    Fixture.markInitialized( video.getDisplay() );
    Fixture.markInitialized( video );
    Fixture.preserveWidgets();
    
    video.setPlayerControlsVisible( false );
    lifeCycleAdapter.renderChanges( video );
    
    Message message = Fixture.getProtocolMessage();
    SetOperation operation = message.findSetOperation( video, keyForEnum( PlaybackOptions.CONTROLS_VISIBLE ) );
    assertEquals( Boolean.FALSE, 
                  operation.getProperty( keyForEnum( PlaybackOptions.CONTROLS_VISIBLE ) ) );
  }
  
  @Test
  public void testRendersHeadPointer() throws IOException {
    Fixture.markInitialized( video.getDisplay() );
    Fixture.markInitialized( video );
    Fixture.preserveWidgets();
    
    video.stepToTime( 23 );
    lifeCycleAdapter.renderChanges( video );
    
    Message message = Fixture.getProtocolMessage();
    SetOperation operation = message.findSetOperation( video, keyForEnum( PlaybackOptions.HEAD_POSITION ) );
    assertEquals( Integer.valueOf( 23 ), 
                  operation.getProperty( keyForEnum( PlaybackOptions.HEAD_POSITION ) ) );
  }
  
  @Test
  public void testRendersVideoListener() throws IOException {
    Fixture.markInitialized( video.getDisplay() );
    Fixture.markInitialized( video );
    Fixture.preserveWidgets();
    
    video.addVideoListener( mock( VideoListener.class ) );
    lifeCycleAdapter.renderChanges( video );
    
    Message message = Fixture.getProtocolMessage();
    ListenOperation listenOperation = message.findListenOperation( video, VideoLifeCycleAdapter.VIDEO_LISTENER_PROPERTY );
    assertNotNull( listenOperation );
  }
  
  @Test
  public void testDestroy() throws IOException {
    lifeCycleAdapter.renderDispose( video );
    
    Message message = Fixture.getProtocolMessage();
    assertNotNull( message.findDestroyOperation( video ) );
  }
  
}
