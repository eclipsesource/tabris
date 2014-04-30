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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.Serializable;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.internal.lifecycle.AbstractWidgetLCA;
import org.eclipse.rap.rwt.internal.lifecycle.WidgetLifeCycleAdapter;
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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.internal.VideoLifeCycleAdapter.PlaybackOptions;
import com.eclipsesource.tabris.test.ControlLCATestUtil;
import com.eclipsesource.tabris.test.RWTEnvironment;
import com.eclipsesource.tabris.widgets.PlaybackListener;
import com.eclipsesource.tabris.widgets.PresentationListener;
import com.eclipsesource.tabris.widgets.Video;
import com.eclipsesource.tabris.widgets.Video.Playback;
import com.eclipsesource.tabris.widgets.Video.Presentation;


@SuppressWarnings("restriction")
public class VideoLifeCycleAdapterTest {

  @Rule
  public RWTEnvironment environment = new RWTEnvironment();

  private Video video;
  private Shell parent;
  private PlaybackListener playbackListener;
  private PresentationListener presentationListener;
  private AbstractWidgetLCA lifeCycleAdapter;

  @Before
  public void setUp() {
    Display display = new Display();
    parent = new Shell( display );
    video = new Video( "http://test.com", parent );
    playbackListener = mock( PlaybackListener.class );
    presentationListener = mock( PresentationListener.class );
    new Button( parent, SWT.PUSH );
    lifeCycleAdapter = ( AbstractWidgetLCA )video.getAdapter( WidgetLifeCycleAdapter.class );
    Fixture.fakeNewRequest();
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
    assertEquals( video.getURL().toString(), createOperation.getProperty( Constants.PROPERTY_URL ).asString() );
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
    JsonObject parameters = new JsonObject();
    parameters.add( PROPERTY_PLAYBACK, Playback.ERROR.name() );
    fakeNotifyOperation( getId(), Constants.EVENT_PLAYBACK, parameters );

    Fixture.executeLifeCycleFromServerThread();

    verify( playbackListener ).playbackChanged( Playback.ERROR );
  }

  @Test
  public void testRendersPlaybackReadyOnce() {
    video.addPlaybackListener( playbackListener );
    Fixture.executeLifeCycleFromServerThread();
    Fixture.fakeNewRequest();
    JsonObject parameters = new JsonObject();
    parameters.add( PROPERTY_PLAYBACK, Playback.READY.name() );

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
    JsonObject parameters = new JsonObject();
    parameters.add( PROPERTY_PLAYBACK, Playback.PLAY.name() );
    fakeNotifyOperation( getId(), Constants.EVENT_PLAYBACK, parameters );
    Fixture.executeLifeCycleFromServerThread();

    verify( playbackListener ).playbackChanged( Playback.PLAY );
    Message message = Fixture.getProtocolMessage();
    assertNull( message.findSetOperation( video, keyForEnum( PlaybackOptions.PLAYBACK ) ) );
  }

  @Test
  public void testFiresPresentationChange() {
    video.addPresentationListener( presentationListener );
    JsonObject parameters = new JsonObject();
    parameters.add( PROPERTY_PRESENTATION, Presentation.FULL_SCREEN.name() );
    fakeNotifyOperation( getId(), Constants.EVENT_PRESENTATION, parameters );

    Fixture.executeLifeCycleFromServerThread();

    verify( presentationListener ).presentationChanged( Presentation.FULL_SCREEN );
  }

  @Test
  public void testFiresPresentationChangeToFullScreen() {
    video.addPresentationListener( presentationListener );
    JsonObject parameters = new JsonObject();
    parameters.add( PROPERTY_PRESENTATION, Presentation.FULL_SCREEN.name() );
    fakeNotifyOperation( getId(), Constants.EVENT_PRESENTATION, parameters );

    Fixture.executeLifeCycleFromServerThread();

    verify( presentationListener ).presentationChanged( Presentation.FULL_SCREEN );
  }

  @Test
  public void testFiresPresentationChangeToFullScreenOnce() {
    video.addPresentationListener( presentationListener );
    Fixture.executeLifeCycleFromServerThread();
    Fixture.fakeNewRequest();
    JsonObject parameters = new JsonObject();
    parameters.add( PROPERTY_PRESENTATION, Presentation.FULL_SCREEN.name() );
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
    assertEquals( keyForEnum( Playback.PLAY ), operation.getProperty( keyForEnum( PlaybackOptions.PLAYBACK ) ).asString() );
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
                  operation.getProperty( keyForEnum( PlaybackOptions.PRESENTATION ) ).asString() );
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
    assertEquals( 2, operation.getProperty( keyForEnum( PlaybackOptions.SPEED ) ).asDouble(), 0 );
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
    assertEquals( -2, operation.getProperty( keyForEnum( PlaybackOptions.SPEED ) ).asDouble(), 0 );
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
    assertTrue( operation.getProperty( keyForEnum( PlaybackOptions.REPEAT ) ).asBoolean() );
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
    assertFalse( operation.getProperty( keyForEnum( PlaybackOptions.CONTROLS_VISIBLE ) ).asBoolean() );
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
    assertEquals( 23, operation.getProperty( keyForEnum( PlaybackOptions.HEAD_POSITION ) ).asInt() );
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
