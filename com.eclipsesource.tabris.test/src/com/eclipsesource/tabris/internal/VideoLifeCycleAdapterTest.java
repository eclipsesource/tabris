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
import static com.eclipsesource.tabris.test.util.MessageUtil.hasOperation;
import static com.eclipsesource.tabris.test.util.MessageUtil.isParentOfCreate;
import static com.eclipsesource.tabris.test.util.MessageUtil.OperationType.CREATE;
import static com.eclipsesource.tabris.test.util.MessageUtil.OperationType.LISTEN;
import static com.eclipsesource.tabris.test.util.MessageUtil.OperationType.SET;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.Serializable;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.internal.lifecycle.AbstractWidgetLCA;
import org.eclipse.rap.rwt.internal.lifecycle.WidgetLifeCycleAdapter;
import org.eclipse.rap.rwt.internal.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.internal.VideoLifeCycleAdapter.PlaybackOptions;
import com.eclipsesource.tabris.internal.VideoLifeCycleAdapter.VideoOperationHandler;
import com.eclipsesource.tabris.test.util.ControlLCATestUtil;
import com.eclipsesource.tabris.test.util.MessageUtil;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;
import com.eclipsesource.tabris.widgets.PlaybackListener;
import com.eclipsesource.tabris.widgets.PresentationListener;
import com.eclipsesource.tabris.widgets.Video;
import com.eclipsesource.tabris.widgets.Video.Playback;
import com.eclipsesource.tabris.widgets.Video.Presentation;


@SuppressWarnings("restriction")
public class VideoLifeCycleAdapterTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

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
    environment.newRequest();
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

    assertTrue( MessageUtil.hasCreateOperation( "tabris.widgets.Video" ) );
    JsonObject properties = MessageUtil.getOperationProperties( WidgetUtil.getId( video ), CREATE, null );
    assertEquals( video.getURL().toString(), properties.get( Constants.PROPERTY_URL ).asString() );
  }

  @Test
  public void testCreatesParent() throws IOException {
    lifeCycleAdapter.renderInitialization( video );

    assertTrue( isParentOfCreate( "tabris.widgets.Video", WidgetUtil.getId( video.getParent() ) ) );
  }

  @Test
  public void testFiresPlaybackChange() {
    environment.getRemoteObject().setHandler( new VideoOperationHandler( video ) );
    video.addPlaybackListener( playbackListener );
    JsonObject parameters = new JsonObject();
    parameters.add( PROPERTY_PLAYBACK, Playback.ERROR.name() );

    environment.dispatchNotify( Constants.EVENT_PLAYBACK, parameters );

    verify( playbackListener ).playbackChanged( Playback.ERROR );
  }

  @Test
  public void testRendersPlaybackReadyOnce() {
    environment.getRemoteObject().setHandler( new VideoOperationHandler( video ) );
    video.addPlaybackListener( playbackListener );
    environment.newRequest();
    JsonObject parameters = new JsonObject();
    parameters.add( PROPERTY_PLAYBACK, Playback.READY.name() );

    environment.dispatchNotify( Constants.EVENT_PLAYBACK, parameters );

    verify( playbackListener ).playbackChanged( Playback.READY );
    assertFalse( hasOperation( WidgetUtil.getId( video ), SET, null ) );
  }

  @Test
  public void testRendersPlaybackPlayOnce() {
    environment.getRemoteObject().setHandler( new VideoOperationHandler( video ) );
    video.addPlaybackListener( playbackListener );
    environment.newRequest();
    JsonObject parameters = new JsonObject();
    parameters.add( PROPERTY_PLAYBACK, Playback.PLAY.name() );

    environment.dispatchNotify( Constants.EVENT_PLAYBACK, parameters );

    verify( playbackListener ).playbackChanged( Playback.PLAY );
    assertFalse( hasOperation( WidgetUtil.getId( video ), SET, null ) );
  }

  @Test
  public void testFiresPresentationChange() {
    environment.getRemoteObject().setHandler( new VideoOperationHandler( video ) );
    video.addPresentationListener( presentationListener );
    JsonObject parameters = new JsonObject();
    parameters.add( PROPERTY_PRESENTATION, Presentation.FULL_SCREEN.name() );

    environment.dispatchNotify( Constants.EVENT_PRESENTATION, parameters );

    verify( presentationListener ).presentationChanged( Presentation.FULL_SCREEN );
  }

  @Test
  public void testFiresPresentationChangeToFullScreen() {
    environment.getRemoteObject().setHandler( new VideoOperationHandler( video ) );
    video.addPresentationListener( presentationListener );
    JsonObject parameters = new JsonObject();
    parameters.add( PROPERTY_PRESENTATION, Presentation.FULL_SCREEN.name() );

    environment.dispatchNotify( Constants.EVENT_PRESENTATION, parameters );

    verify( presentationListener ).presentationChanged( Presentation.FULL_SCREEN );
  }

  @Test
  public void testFiresPresentationChangeToFullScreenOnce() {
    environment.getRemoteObject().setHandler( new VideoOperationHandler( video ) );
    video.addPresentationListener( presentationListener );
    environment.newRequest();
    JsonObject parameters = new JsonObject();
    parameters.add( PROPERTY_PRESENTATION, Presentation.FULL_SCREEN.name() );

    environment.dispatchNotify( Constants.EVENT_PRESENTATION, parameters );

    verify( presentationListener ).presentationChanged( Presentation.FULL_SCREEN );
    assertFalse( hasOperation( WidgetUtil.getId( video ), SET, null ) );
  }

  @Test
  public void testRendersPlaybackMode() throws IOException {
    lifeCycleAdapter.preserveValues( video );

    video.play();
    lifeCycleAdapter.renderChanges( video );

    JsonObject properties = MessageUtil.getOperationProperties( WidgetUtil.getId( video ), SET, null );
    assertEquals( keyForEnum( Playback.PLAY ), properties.get( keyForEnum( PlaybackOptions.PLAYBACK ) ).asString() );
  }

  @Test
  public void testRendersPresentationMode() throws IOException {
    lifeCycleAdapter.preserveValues( video );

    video.setFullscreen( true );
    lifeCycleAdapter.renderChanges( video );

    JsonObject properties = MessageUtil.getOperationProperties( WidgetUtil.getId( video ), SET, null );
    assertEquals( keyForEnum( Presentation.FULL_SCREEN ),
                  properties.get( keyForEnum( PlaybackOptions.PRESENTATION ) ).asString() );
  }

  @Test
  public void testRendersSpeedForward() throws IOException {
    lifeCycleAdapter.preserveValues( video );

    video.fastForward( 2 );
    lifeCycleAdapter.renderChanges( video );

    JsonObject properties = MessageUtil.getOperationProperties( WidgetUtil.getId( video ), SET, null );
    assertEquals( 2, properties.get( keyForEnum( PlaybackOptions.SPEED ) ).asDouble(), 0 );
  }

  @Test
  public void testRendersSpeedBackward() throws IOException {
    lifeCycleAdapter.preserveValues( video );

    video.fastBackward( -2 );
    lifeCycleAdapter.renderChanges( video );

    JsonObject properties = MessageUtil.getOperationProperties( WidgetUtil.getId( video ), SET, null );
    assertEquals( -2, properties.get( keyForEnum( PlaybackOptions.SPEED ) ).asDouble(), 0 );
  }

  @Test
  public void testRendersRepeat() throws IOException {
    lifeCycleAdapter.preserveValues( video );

    video.setRepeat( true );
    lifeCycleAdapter.renderChanges( video );

    JsonObject properties = MessageUtil.getOperationProperties( WidgetUtil.getId( video ), SET, null );
    assertTrue( properties.get( keyForEnum( PlaybackOptions.REPEAT ) ).asBoolean() );
  }

  @Test
  public void testRendersControlsVisible() throws IOException {
    lifeCycleAdapter.preserveValues( video );

    video.setPlayerControlsVisible( false );
    lifeCycleAdapter.renderChanges( video );

    JsonObject properties = MessageUtil.getOperationProperties( WidgetUtil.getId( video ), SET, null );
    assertFalse( properties.get( keyForEnum( PlaybackOptions.CONTROLS_VISIBLE ) ).asBoolean() );
  }

  @Test
  public void testRendersHeadPointer() throws IOException {
    lifeCycleAdapter.preserveValues( video );

    video.stepToTime( 23 );
    lifeCycleAdapter.renderChanges( video );

    JsonObject properties = MessageUtil.getOperationProperties( WidgetUtil.getId( video ), SET, null );
    assertEquals( 23, properties.get( keyForEnum( PlaybackOptions.HEAD_POSITION ) ).asInt() );
  }

  @Test
  public void testRendersPlaybackListener() throws IOException {
    lifeCycleAdapter.preserveValues( video );

    video.addPlaybackListener( playbackListener );
    lifeCycleAdapter.renderChanges( video );

    JsonObject properties = MessageUtil.getOperationProperties( WidgetUtil.getId( video ), LISTEN, null );
    assertNotNull( properties.get( Constants.EVENT_PLAYBACK ) );
  }

  @Test
  public void testRendersPresentationListener() throws IOException {
    lifeCycleAdapter.preserveValues( video );

    video.addPresentationListener( presentationListener );
    lifeCycleAdapter.renderChanges( video );

    JsonObject properties = MessageUtil.getOperationProperties( WidgetUtil.getId( video ), LISTEN, null );
    assertNotNull( properties.get( Constants.EVENT_PRESENTATION ) );
  }

}
