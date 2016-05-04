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

import static com.eclipsesource.tabris.test.util.MessageUtil.hasOperation;
import static com.eclipsesource.tabris.test.util.MessageUtil.isParentOfCreate;
import static com.eclipsesource.tabris.test.util.MessageUtil.OperationType.CALL;
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

import com.eclipsesource.tabris.internal.VideoLifeCycleAdapter.VideoOperationHandler;
import com.eclipsesource.tabris.test.util.ControlLCATestUtil;
import com.eclipsesource.tabris.test.util.MessageUtil;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;
import com.eclipsesource.tabris.widgets.PlaybackListener;
import com.eclipsesource.tabris.widgets.Video;
import com.eclipsesource.tabris.widgets.Video.PlayerState;


@SuppressWarnings("restriction")
public class VideoLifeCycleAdapterTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private Video video;
  private Shell parent;
  private PlaybackListener playbackListener;
  private AbstractWidgetLCA lifeCycleAdapter;

  @Before
  public void setUp() {
    Display display = new Display();
    parent = new Shell( display );
    video = new Video( parent, "http://test.com" );
    playbackListener = mock( PlaybackListener.class );
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
  public void testFiresPlayerState() {
    environment.getRemoteObject().setHandler( new VideoOperationHandler( video ) );
    video.addPlaybackListener( playbackListener );

    JsonObject parameters = new JsonObject();
    parameters.add( Constants.PROPERTY_STATE, PlayerState.READY.name() );
    environment.dispatchNotify( Constants.EVENT_PLAYBACK, parameters );

    verify( playbackListener ).playbackChanged( PlayerState.READY );
    assertFalse( hasOperation( WidgetUtil.getId( video ), SET, null ) );
  }

  @Test
  public void testFiresSetSpeed() {
    environment.getRemoteObject().setHandler( new VideoOperationHandler( video ) );
    assertEquals( 0.0f, video.getSpeed(), 0.0f );

    JsonObject parameters = new JsonObject();
    parameters.add( Constants.PROPERTY_PLAYBACK_SPEED, 2.0f );
    environment.dispatchSet( parameters );

    assertEquals( 2.0f, video.getSpeed(), 0.0f );
  }

  @Test
  public void testRendersSetSpeed() throws IOException {
    lifeCycleAdapter.preserveValues( video );

    video.setSpeed( Video.PLAY_SPEED );
    lifeCycleAdapter.renderChanges( video );

    JsonObject properties = MessageUtil.getOperationProperties( WidgetUtil.getId( video ), SET, null );
    assertEquals( Video.PLAY_SPEED,
                  properties.get( Constants.PROPERTY_PLAYBACK_SPEED ).asFloat(),
                  0 );
  }

  @Test
  public void testRendersControlsVisible() throws IOException {
    lifeCycleAdapter.preserveValues( video );

    video.setPlayerControlsVisible( true );
    lifeCycleAdapter.renderChanges( video );

    JsonObject properties = MessageUtil.getOperationProperties( WidgetUtil.getId( video ), SET, null );
    assertTrue( properties.get( Constants.PROPERTY_PLAYER_CONTROLS_VISIBLE ).asBoolean() );
  }

  @Test
  public void testRendersStepToTime() throws IOException {
    lifeCycleAdapter.preserveValues( video );

    video.stepToTime( 23.0f );
    lifeCycleAdapter.renderChanges( video );

    assertTrue( MessageUtil.hasOperation( WidgetUtil.getId( video ),
                                          CALL,
                                          Constants.METHOD_STEP_TO_TIME ) );
  }
  
  @Test
  public void testRendersSkipFromCurrent() throws IOException {
    lifeCycleAdapter.preserveValues( video );

    video.skipFromCurrent( 10.0f );
    lifeCycleAdapter.renderChanges( video );

    JsonObject properties = MessageUtil
      .getOperationProperties( WidgetUtil.getId( video ),
                               CALL,
                               Constants.METHOD_SKIP_FROM_CURRENT );
    assertEquals( 10.0f, properties.get( Constants.PROPERTY_TIME ).asFloat(), 0.0f );
  }

  @Test
  public void testRendersClearCache() throws IOException {
    lifeCycleAdapter.preserveValues( video );
    video.clearCache();
    lifeCycleAdapter.renderChanges( video );

    JsonObject properties = MessageUtil
      .getOperationProperties( WidgetUtil.getId( video ), CALL, Constants.METHOD_CLEAR_CACHE );
    assertTrue( properties.isEmpty() );
  }

  @Test
  public void testRendersPlaybackListener() throws IOException {
    lifeCycleAdapter.preserveValues( video );

    video.addPlaybackListener( playbackListener );
    lifeCycleAdapter.renderChanges( video );

    JsonObject properties = MessageUtil.getOperationProperties( WidgetUtil.getId( video ), LISTEN, null );
    assertNotNull( properties.get( Constants.EVENT_PLAYBACK ) );
  }

}
