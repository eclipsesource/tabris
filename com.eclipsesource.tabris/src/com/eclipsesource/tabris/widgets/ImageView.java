/*******************************************************************************
 * Copyright (c) 2017, 2018 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_HEIGHT;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_IMAGE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_MAX;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_MIN;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PARENT;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_SCALE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_SCALE_MODE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_SRC;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_TINT_COLOR;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_VALUE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_WIDTH;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ZOOM_ENABLED;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ZOOM_LEVEL;
import static com.eclipsesource.tabris.internal.Constants.TYPE_IMAGE_VIEW;
import static org.eclipse.rap.rwt.remote.JsonMapping.toJson;
import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.lifecycle.RemoteAdapter;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.graphics.ImageFactory;
import org.eclipse.swt.internal.widgets.WidgetRemoteAdapter;
import org.eclipse.swt.internal.widgets.compositekit.CompositeOperationHandler;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;


/**
 * <p>
 * A widget to display an image.
 * </p>
 * <p>
 * The {@link ImageView} is a stand alone {@link Widget}. It needs to be part of your layout.
 * </p>
 *
 * @since 3.4
 */
@SuppressWarnings("restriction")
public class ImageView extends Composite {

  public static enum ScaleMode {
    /**
     * Will scale the image proportionally to fit into the view, possible leaving some empty space at the edges.
     * That is, the image will be displayed as large as possible while being fully contained in the view.
     */
    FIT,
    /**
     * Will scale the image proportionally to fill the entire view, possibly cutting off parts of the image.
     * That is, the image will be displayed as small as possible while covering the entire view.
     */
    FILL,
    /**
     * Will scale down the image to fit into the view if it is too large, but it won’t scale up a smaller image.
     */
    AUTO,
    /**
     * Will resize the image to the actual bounds of the image view.
     */
    STRETCH,
    /**
     * Will not resize the image at all. The image will be displayed in its original size.
     */
    NONE;
 }

  private Image image;
  private float scale = 1;
  private ScaleMode scaleMode = ScaleMode.AUTO;
  private Color tintColor;
  private boolean zoomEnabled;
  private float minZoomLevel = 1f;
  private float maxZoomLevel = 3f;
  private float zoomLevel = 1f;
  private final RemoteObject remoteObject;
  private boolean renderImageScheduled;
  private boolean renderZoomLevelScheduled;

  /**
   * <p>
   * Creates a new {@link ImageView} with the provided parent.
   * </p>
   *
   * @param parent the parent to use. Must not be <code>null</code>.
   */
  public ImageView( Composite parent ) {
    super( parent, SWT.NONE );
    Connection connection = RWT.getUISession().getConnection();
    remoteObject = connection.createRemoteObject( TYPE_IMAGE_VIEW );
    remoteObject.set( PROPERTY_PARENT, getId( this ) );
    remoteObject.setHandler( new ImageViewOperationHandler( this ) );
  }

  /**
   * Sets the receiver's image to the argument, which must not be <code>null</code>.
   *
   * @param image the image to display on the receiver
   *
   * @exception IllegalArgumentException
   * <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if the image is <code>null</code> or has been disposed</li>
   * </ul>
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setImage( Image image ) {
    checkWidget();
    whenNull( image ).throwIllegalArgument( "Image must not be null" );
    when( image.isDisposed() ).throwIllegalArgument( "Image is disposed" );
    this.image = image;
    scheduleRenderImage();
  }

  /**
   * Returns the receiver's image if it has one, or <code>null</code> if it does not.
   *
   * @return the receiver's image
   *
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public Image getImage() {
    checkWidget();
    return image;
  }

  /**
   * Sets image scale factor. The default is 1.
   *
   * @exception IllegalArgumentException
   * <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if the scale factor is zero or negative</li>
   * </ul>
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setScale( float scale ) {
    checkWidget();
    when( scale <= 0 ).throwIllegalArgument( "Scale must be positive number" );
    this.scale = scale;
    scheduleRenderImage();
  }

  /**
   * Returns image scale factor.
   *
   * @return the image scale factor
   *
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public float getScale() {
    checkWidget();
    return scale;
  }

  /**
   * Selects the method used for image scaling. The default is <code>ScaleMode.AUTO</code>.
   *
   * @param scaleMode the scale mode to use, must not be <code>null</code>
   *
   * @exception IllegalArgumentException
   * <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if the scale mode is <code>null</code></li>
   * </ul>
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setScaleMode( ScaleMode scaleMode ) {
    checkWidget();
    whenNull( scaleMode ).throwIllegalArgument( "ScaleMode must not be null" );
    if( !this.scaleMode.equals( scaleMode ) ) {
      this.scaleMode = scaleMode;
      remoteObject.set( PROPERTY_SCALE_MODE, scaleMode.toString().toLowerCase() );
    }
  }

  /**
   * Returns the receiver's scale mode.
   *
   * @return the receiver's scale mode
   *
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public ScaleMode getScaleMode() {
    checkWidget();
    return scaleMode;
  }

  /**
   * Sets a color to change the image appearance.
   * All opaque parts of the image will be tinted with the given color.
   *
   * @exception IllegalArgumentException
   * <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if the color is <code>null</code> or has been disposed</li>
   * </ul>
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setTintColor( Color tintColor ) {
    checkWidget();
    whenNull( tintColor ).throwIllegalArgument( "tintColor must not be null" );
    when( tintColor.isDisposed() ).throwIllegalArgument( "tintColor is disposed" );
    if( !tintColor.equals( this.tintColor ) ) {
      this.tintColor = tintColor;
      remoteObject.set( PROPERTY_TINT_COLOR, toJson( tintColor ) );
    }
  }

  /**
   * Returns the receiver's tint color.
   *
   * @return the receiver's tint color
   *
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public Color getTintColor() {
    checkWidget();
    return tintColor;
  }

  /**
   * Enables the pinch-to-zoom gesture and makes the properties <code>zoomLevel</code>,
   * <code>minZoomLevel</code> and <code>maxZoomLevel</code> available.
   *
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setZoomEnabled( boolean zoomEnabled ) {
    checkWidget();
    if( this.zoomEnabled != zoomEnabled) {
      this.zoomEnabled = zoomEnabled;
      remoteObject.set( PROPERTY_ZOOM_ENABLED, zoomEnabled );
    }
  }

  /**
   * Returns true if image zoom is enabled, false otherwise.
   *
   * @return true if image zoom is enabled
   *
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public boolean getZoomEnabled() {
    checkWidget();
    return zoomEnabled;
  }

  /**
   * Sets the amount that the image is zoomed in or out.
   * The default position without any zooming has the value 1.0.
   *
   * @exception IllegalArgumentException
   * <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if the zoomLevel is invalid</li>
   * </ul>
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setZoomLevel( float zoomLevel ) {
    checkWidget();
    when( !zoomEnabled ).throwIllegalArgument( "zoomLevel can not be set when zoomEnabled is false" );
    when( zoomLevel < minZoomLevel ).throwIllegalArgument( "zoomLevel can not be smaller than minZoomLevel" );
    when( zoomLevel > maxZoomLevel ).throwIllegalArgument( "zoomLevel can not be larger than maxZoomLevel" );
    this.zoomLevel = zoomLevel;
    scheduleRenderZoomLevel();
  }

  /**
   * Returns image zoom level.
   *
   * @return the image zoom level
   *
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public float getZoomLevel() {
    checkWidget();
    return zoomLevel;
  }

  /**
   * Sets the lowest amount the image can be zoomed out to.
   * Setting the <code>minZoomLevel</code> to a level larger than the current <code>zoomLevel</code>
   * changes the <code>zoomLevel</code> to be the same as the new <code>minZoomLevel</code>.
   *
   * @exception IllegalArgumentException
   * <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if the minZoomLevel is invalid</li>
   * </ul>
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setMinZoomLevel( float minZoomLevel ) {
    checkWidget();
    when( !zoomEnabled ).throwIllegalArgument( "minZoomLevel can not be set when zoomEnabled is false" );
    when( minZoomLevel > maxZoomLevel ).throwIllegalArgument( "minZoomLevel can not be larger than maxZoomLevel" );
    this.minZoomLevel = minZoomLevel;
    if( minZoomLevel > zoomLevel ) {
      zoomLevel = minZoomLevel;
    }
    scheduleRenderZoomLevel();
  }

  /**
   * Returns image minimum zoom level.
   *
   * @return the image minimum zoom level
   *
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public float getMinZoomLevel() {
    checkWidget();
    return minZoomLevel;
  }

  /**
   * Sets the highest amount the image can be zoomed out to.
   * Setting the <code>maxZoomLevel</code> to a level smaller than the current <code>zoomLevel</code>
   * changes the <code>zoomLevel</code> to be the same as the new <code>maxZoomLevel</code>.
   *
   * @exception IllegalArgumentException
   * <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if the maxZoomLevel is invalid</li>
   * </ul>
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setMaxZoomLevel( float maxZoomLevel ) {
    checkWidget();
    when( !zoomEnabled ).throwIllegalArgument( "maxZoomLevel can not be set when zoomEnabled is false" );
    when( maxZoomLevel < minZoomLevel ).throwIllegalArgument( "maxZoomLevel can not be smaller than minZoomLevel" );
    this.maxZoomLevel = maxZoomLevel;
    if( maxZoomLevel < zoomLevel ) {
      zoomLevel = maxZoomLevel;
    }
    scheduleRenderZoomLevel();
  }

  /**
   * Returns image maximum zoom level.
   *
   * @return the image maximum zoom level
   *
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public float getMaxZoomLevel() {
    checkWidget();
    return maxZoomLevel;
  }

  @Override
  public Point computeSize( int wHint, int hHint, boolean changed ) {
    checkWidget();
    Point size = new Point( 0, 0 );
    if( image != null ) {
      Rectangle bounds = image.getBounds();
      size.x = ( int )( bounds.width / scale );
      size.y = ( int )( bounds.height / scale );
    }
    if( wHint != SWT.DEFAULT && hHint != SWT.DEFAULT ) {
      size.x = wHint;
      size.y = hHint;
    } else if( wHint != SWT.DEFAULT && hHint == SWT.DEFAULT ) {
      float ratio = size.x / wHint;
      size.x = wHint;
      size.y = ( int )( size.y / ratio );
    } else if( wHint == SWT.DEFAULT && hHint != SWT.DEFAULT ) {
      float ratio = size.y / hHint;
      size.x = ( int )( size.x / ratio );
      size.y = hHint;
    }
    return size;
  }

  private void scheduleRenderImage() {
    if( !renderImageScheduled && image != null ) {
      renderImageScheduled = true;
      getRemoteAdapter( this ).addRenderRunnable( new Runnable() {
        @Override
        public void run() {
          remoteObject.set( PROPERTY_IMAGE, createImagePropetries() );
          renderImageScheduled = false;
        }
      } );
    }
  }

  private void scheduleRenderZoomLevel() {
    if( !renderZoomLevelScheduled ) {
      renderZoomLevelScheduled = true;
      getRemoteAdapter( this ).addRenderRunnable( new Runnable() {
        @Override
        public void run() {
          remoteObject.set( PROPERTY_ZOOM_LEVEL, createZoomLevelPropetries() );
          renderZoomLevelScheduled = false;
        }
      } );
    }
  }

  private JsonObject createImagePropetries() {
    JsonObject props = new JsonObject();
    String imagePath = ImageFactory.getImagePath( image );
    Rectangle bounds = image.getBounds();
    props.set( PROPERTY_SRC, imagePath );
    if( scale == 1 ) {
      props.set( PROPERTY_WIDTH, bounds.width );
      props.set( PROPERTY_HEIGHT, bounds.height );
    } else {
      props.set( PROPERTY_SCALE, scale );
    }
    return props;
  }

  private JsonObject createZoomLevelPropetries() {
    JsonObject props = new JsonObject();
    props.set( PROPERTY_MIN, minZoomLevel );
    props.set( PROPERTY_VALUE, zoomLevel );
    props.set( PROPERTY_MAX, maxZoomLevel );
    return props;
  }

  protected static WidgetRemoteAdapter getRemoteAdapter( Composite composite ) {
    return ( WidgetRemoteAdapter )composite.getAdapter( RemoteAdapter.class );
  }

  private class ImageViewOperationHandler extends CompositeOperationHandler {

    public ImageViewOperationHandler( ImageView control ) {
      super( control );
    }

    @Override
    public void handleSet( Composite control, JsonObject properties ) {
      super.handleSet( control, properties );
      JsonValue value = properties.get( PROPERTY_ZOOM_LEVEL );
      if( value != null ) {
        zoomLevel = value.asFloat();
      }
    }

  }

}
