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

import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PLAYBACK;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PRESENTATION;
import static com.eclipsesource.tabris.internal.VideoLifeCycleAdapter.keyForEnum;
import static org.eclipse.rap.rwt.testfixture.Fixture.fakeNotifyOperation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.rwt.lifecycle.AbstractWidgetLCA;
import org.eclipse.rap.rwt.lifecycle.WidgetLifeCycleAdapter;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rap.rwt.testfixture.Message;
import org.eclipse.rap.rwt.testfixture.Message.CreateOperation;
import org.eclipse.rap.rwt.testfixture.Message.ListenOperation;
import org.eclipse.rap.rwt.testfixture.Message.SetOperation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.eclipsesource.tabris.internal.VideoLifeCycleAdapter.PlaybackOptions;
import com.eclipsesource.tabris.test.ControlLCATestUtil;
import com.eclipsesource.tabris.widgets.PlaybackListener;
import com.eclipsesource.tabris.widgets.PresentationListener;
import com.eclipsesource.tabris.widgets.Video;
import com.eclipsesource.tabris.widgets.Video.Playback;
import com.eclipsesource.tabris.widgets.Video.Presentation;


@RunWith( MockitoJUnitRunner.class )
public class VideoLifeCycleAdapterTest {

  private Video video;
  private Shell parent;
  @Mock private PlaybackListener playbackListener;
  @Mock private PresentationListener presentationListener;
  private AbstractWidgetLCA lifeCycleAdapter;

  @Before
  public void setUp() {
    Fixture.setUp();
    Display display = new Display();
    parent = new Shell( display );
    video = new Video( "http://test.com", parent );
    new Button( parent, SWT.PUSH );
    lifeCycleAdapter = ( AbstractWidgetLCA )video.getAdapter( WidgetLifeCycleAdapter.class );
    Fixture.fakeNewRequest();
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( VideoLifeCycleAdapter.class ) );
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
    assertEquals( video.getURL().toString(), createOperation.getProperty( Constants.PROPERTY_URL ) );
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
    assertEquals( Constants.TYPE_VIDEO, operation.getType() );
  }

  @Test
  public void testFiresPlaybackChange() {
    video.addPlaybackListener( playbackListener );
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put( PROPERTY_PLAYBACK, Playback.ERROR.name() );
    fakeNotifyOperation( getId(), Constants.EVENT_PLAYBACK, parameters );

    Fixture.executeLifeCycleFromServerThread();

    verify( playbackListener ).playbackChanged( Playback.ERROR );
  }

  @Test
  public void testRendersPlaybackReadyOnce() {
    video.addPlaybackListener( playbackListener );
    Fixture.executeLifeCycleFromServerThread();
    Fixture.fakeNewRequest();
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put( PROPERTY_PLAYBACK, Playback.READY.name() );
    fakeNotifyOperation( getId(), Constants.EVENT_PLAYBACK, parameters );
    Fixture.executeLifeCycleFromServerThread();

    verify( playbackListener ).playbackChanged( Playback.READY );
    Message message = Fixture.getProtocolMessage();
    assertNull( message.findSetOperation( video, keyForEnum( PlaybackOptions.PLAYBACK ) ) );
  }

  @Test
  public void testRendersPlaybackPlayOnce() {
    video.addPlaybackListener( playbackListener );
    Fixture.executeLifeCycleFromServerThread();
    Fixture.fakeNewRequest();
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put( PROPERTY_PLAYBACK, Playback.PLAY.name() );
    fakeNotifyOperation( getId(), Constants.EVENT_PLAYBACK, parameters );
    Fixture.executeLifeCycleFromServerThread();

    verify( playbackListener ).playbackChanged( Playback.PLAY );
    Message message = Fixture.getProtocolMessage();
    assertNull( message.findSetOperation( video, keyForEnum( PlaybackOptions.PLAYBACK ) ) );
  }

  @Test
  public void testFiresPresentationChange() {
    video.addPresentationListener( presentationListener );
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put( PROPERTY_PRESENTATION, Presentation.FULL_SCREEN.name() );
    fakeNotifyOperation( getId(), Constants.EVENT_PRESENTATION, parameters );

    Fixture.executeLifeCycleFromServerThread();

    verify( presentationListener ).presentationChanged( Presentation.FULL_SCREEN );
  }

  @Test
  public void testFiresPresentationChangeToFullScreen() {
    video.addPresentationListener( presentationListener );
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put( PROPERTY_PRESENTATION, Presentation.FULL_SCREEN.name() );
    fakeNotifyOperation( getId(), Constants.EVENT_PRESENTATION, parameters );

    Fixture.executeLifeCycleFromServerThread();

    verify( presentationListener ).presentationChanged( Presentation.FULL_SCREEN );
  }

  @Test
  public void testFiresPresentationChangeToFullScreenOnce() {
    video.addPresentationListener( presentationListener );
    Fixture.executeLifeCycleFromServerThread();
    Fixture.fakeNewRequest();
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put( PROPERTY_PRESENTATION, Presentation.FULL_SCREEN.name() );
    fakeNotifyOperation( getId(), Constants.EVENT_PRESENTATION, parameters );

    Fixture.executeLifeCycleFromServerThread();

    verify( presentationListener ).presentationChanged( Presentation.FULL_SCREEN );
    Message message = Fixture.getProtocolMessage();
    assertNull( message.findSetOperation( video, keyForEnum( PlaybackOptions.PRESENTATION ) ) );
  }

  @Test
  public void testRendersPlaybackMode() throws IOException {
    Fixture.markInitialized( video.getDisplay() );
    Fixture.markInitialized( video );
    Fixture.preserveWidgets();

    video.play();
    lifeCycleAdapter.renderChanges( video );

    Message message = Fixture.getProtocolMessage();
    SetOperation operation = message.findSetOperation( video, keyForEnum( PlaybackOptions.PLAYBACK ) );
    assertEquals( keyForEnum( Playback.PLAY ), operation.getProperty( keyForEnum( PlaybackOptions.PLAYBACK ) ) );
  }

  @Test
  public void testRendersPresentationMode() throws IOException {
    Fixture.markInitialized( video.getDisplay() );
    Fixture.markInitialized( video );
    Fixture.preserveWidgets();

    video.setFullscreen( true );
    lifeCycleAdapter.renderChanges( video );

    Message message = Fixture.getProtocolMessage();
    SetOperation operation = message.findSetOperation( video, keyForEnum( PlaybackOptions.PRESENTATION ) );
    assertEquals( keyForEnum( Presentation.FULL_SCREEN ),
                  operation.getProperty( keyForEnum( PlaybackOptions.PRESENTATION ) ) );
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
  public void testRendersPlaybackListener() throws IOException {
    Fixture.markInitialized( video.getDisplay() );
    Fixture.markInitialized( video );
    Fixture.preserveWidgets();

    video.addPlaybackListener( playbackListener );
    lifeCycleAdapter.renderChanges( video );

    Message message = Fixture.getProtocolMessage();
    ListenOperation listenOperation = message.findListenOperation( video, Constants.EVENT_PLAYBACK );
    assertNotNull( listenOperation );
  }

  @Test
  public void testRendersPresentationListener() throws IOException {
    Fixture.markInitialized( video.getDisplay() );
    Fixture.markInitialized( video );
    Fixture.preserveWidgets();

    video.addPresentationListener( presentationListener );
    lifeCycleAdapter.renderChanges( video );

    Message message = Fixture.getProtocolMessage();
    ListenOperation listenOperation = message.findListenOperation( video, Constants.EVENT_PRESENTATION );
    assertNotNull( listenOperation );
  }

  private String getId() {
    return WidgetUtil.getId( video );
  }

}
