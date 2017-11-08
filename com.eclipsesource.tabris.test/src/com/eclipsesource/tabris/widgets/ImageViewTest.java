/*******************************************************************************
 * Copyright (c) 2017 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets;

import static com.eclipsesource.tabris.internal.Constants.PROPERTY_HEIGHT;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_IMAGE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PARENT;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_SCALE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_SCALE_MODE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_TINT_COLOR;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_WIDTH;
import static org.eclipse.rap.rwt.internal.lifecycle.DisplayUtil.getLCA;
import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.service.UISessionImpl;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.widgets.compositekit.CompositeOperationHandler;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.test.util.TabrisEnvironment;

@SuppressWarnings("restriction")
public class ImageViewTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private Display display;
  private ImageView view;
  private Connection connection;
  private RemoteObject remoteObject;

  @Before
  public void setUp() {
    display = new Display();
    Shell parent = new Shell( display );
    remoteObject = mock( RemoteObject.class );
    connection = mock( Connection.class );
    when( connection.createRemoteObject( anyString() ) ).thenReturn( remoteObject );
    replaceConnection( connection );
    view = new ImageView( parent );
  }

  @Test
  public void testConstructor_createsRemoteObject() {
    verify( connection ).createRemoteObject( "tabris.widgets.ImageView" );
  }

  @Test
  public void testConstructor_setsParent() {
    verify( remoteObject ).set( PROPERTY_PARENT, getId( view ) );
  }

  @Test
  public void testConstructor_setsHandler() {
    verify( remoteObject ).setHandler( any( CompositeOperationHandler.class ) );
  }

  @Test
  public void testSetImage() {
    Image image = getImage();

    view.setImage( image );

    assertSame( image, view.getImage() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetImage_nullArgument() {
    view.setImage( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetImage_disposedArgument() {
    Image image = getImage();
    image.dispose();

    view.setImage( image );
  }

  @Test
  public void testRenderImage_withoutScale() {
    Image image = getImage();
    view.setImage( image );

    render();

    JsonObject expected = new JsonObject()
      .add( "src", "rwt-resources/generated/1c8a9d7b.png" )
      .add( PROPERTY_WIDTH, 36 )
      .add( PROPERTY_HEIGHT, 36 );
    verify( remoteObject ).set( eq( PROPERTY_IMAGE ), eq( expected ) );
  }

  @Test
  public void testRenderImage_withScale() {
    Image image = getImage();
    view.setImage( image );
    view.setScale( 2 );

    render();

    JsonObject expected = new JsonObject()
      .add( "src", "rwt-resources/generated/1c8a9d7b.png" )
      .add( PROPERTY_SCALE, 2 );
    verify( remoteObject ).set( eq( PROPERTY_IMAGE ), eq( expected ) );
  }

  @Test
  public void testSetScale() throws Exception {
    view.setScale( 2 );

    assertEquals( 2, view.getScale(), 0 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetScale_negativeArgument() {
    view.setScale( -2 );
  }

  @Test
  public void testSetScaleMode() {
    view.setScaleMode( ImageView.ScaleMode.FILL );

    assertEquals( ImageView.ScaleMode.FILL, view.getScaleMode() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetScaleMode_nullArgument() {
    view.setScaleMode( null );
  }

  @Test
  public void testRenderScaleMode() {
    view.setScaleMode( ImageView.ScaleMode.FILL );

    verify( remoteObject ).set( PROPERTY_SCALE_MODE, "fill" );
  }

  @Test
  public void testSetTintColor() {
    Color tintColor = new Color( display, 255, 0, 0 );

    view.setTintColor( tintColor );

    assertEquals( tintColor, view.getTintColor() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetTintColor_nullArgument() {
    view.setTintColor( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetTintColor_disposedArgument() {
    Color tintColor = new Color( display, 255, 0, 0 );
    tintColor.dispose();

    view.setTintColor( tintColor );
  }

  @Test
  public void testRenderTintColor() {
    Color tintColor = new Color( display, 255, 0, 0 );

    view.setTintColor( tintColor );

    JsonArray expected = new JsonArray().add( 255 ).add( 0 ).add( 0 ).add( 255 );
    verify( remoteObject ).set( PROPERTY_TINT_COLOR, expected );
  }

  @Test
  public void testComputeSize_withImage() {
    Image image = getImage();

    view.setImage( image );

    assertEquals( new Point( 36, 36 ), view.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
  }

  @Test
  public void testComputeSize_withImageAndScale() {
    Image image = getImage();

    view.setImage( image );
    view.setScale( 2 );

    assertEquals( new Point( 18, 18 ), view.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
  }

  @Test
  public void testComputeSize_withoutImage() {
    assertEquals( new Point( 0, 0 ), view.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
  }

  @Test
  public void testComputeSize_withHints() {
    assertEquals( new Point( 10, 20 ), view.computeSize( 10, 20 ) );
  }

  @Test
  public void testComputeSize_withHHintOnly() {
    view.setImage( getImage() );

    assertEquals( new Point( 12, 12 ), view.computeSize( 12, SWT.DEFAULT ) );
  }

  @Test
  public void testComputeSize_withHHintWithoutImage() {
    assertEquals( new Point( 12, 0 ), view.computeSize( 12, SWT.DEFAULT ) );
  }

  @Test
  public void testComputeSize_withVHintOnly() {
    view.setImage( getImage() );

    assertEquals( new Point( 12, 12 ), view.computeSize( SWT.DEFAULT, 12 ) );
  }

  @Test
  public void testComputeSize_withVHintOnlyWithoutImage() {
    assertEquals( new Point( 0, 12 ), view.computeSize( SWT.DEFAULT, 12 ) );
  }

  private void render() {
    try {
      getLCA( display ).render( display );
    } catch( IOException shouldNotHappen ) {
      fail( shouldNotHappen.getMessage() );
    }
  }

  private void replaceConnection( Connection connection ) {
    UISessionImpl uiSession = ( UISessionImpl )RWT.getUISession();
    uiSession.setConnection( connection );
  }

  private Image getImage() {
    InputStream resourceStream = getClass().getResourceAsStream( "tabris.png" );
    return new Image( display, resourceStream );
  }

}
