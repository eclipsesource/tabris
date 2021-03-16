/*******************************************************************************
 * Copyright (c) 2021 EclipseSource and others.
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
import static com.eclipsesource.tabris.internal.Constants.EVENT_ERROR;
import static com.eclipsesource.tabris.internal.Constants.EVENT_SUCCESS;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PADDING;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PAGE_BACKGROUND;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PAGE_ELEVATION;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PARENT;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_SPACING;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_URL;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ZOOM_ENABLED;
import static com.eclipsesource.tabris.internal.Constants.TYPE_PDF_VIEW;
import static org.eclipse.rap.rwt.remote.JsonMapping.toJson;
import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.lifecycle.ProcessActionRunner;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.theme.BoxDimensions;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.internal.widgets.compositekit.CompositeOperationHandler;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;

/**
 * <p>
 * A widget to display a PDF document.
 * </p>
 * <p>
 * The {@link PdfView} is a stand alone {@link Widget}. It needs to be part of your layout.
 * </p>
 *
 * @since 3.17
 */
@SuppressWarnings("restriction")
public class PdfView extends Composite {

  private final RemoteObject remoteObject;
  private BoxDimensions padding;
  private int spacing;
  private int pageElevation;
  private Color pageBackground;
  private boolean zoomEnabled;
  private String url = "";
  private final List<LoadListener> loadListeners = new ArrayList<LoadListener>();

  /**
   * Creates a new PdfView object.
   *
   * @param parent the parent Composite, never null
   * @since 3.17
   */
  public PdfView( Composite parent ) {
    super( parent, SWT.NONE );
    Connection connection = RWT.getUISession().getConnection();
    remoteObject = connection.createRemoteObject( TYPE_PDF_VIEW );
    remoteObject.set( PROPERTY_PARENT, getId( this ) );
    remoteObject.setHandler( new PdfViewOperationHandler( this ) );
  }

  /**
   * Setting the URL of the document.
   *
   * @param url the document URL
   *
   * @exception IllegalArgumentException
   * <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if the argument is empty string or null</li>
   * </ul>
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setUrl( String url ) {
    checkWidget();
    when( url == null || url.isEmpty() ).throwIllegalArgument( "URL must not be blank." );
    if( !this.url.equals( url ) ) {
      this.url = url;
      remoteObject.set( PROPERTY_URL, url );
    }
  }

  /**
   * Returns the document URL.
   *
   * @return the document URL
   *
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public String getUrl() {
    checkWidget();
    return url;
  }

  /**
   * Setting this property determines the intensity of the shadow page on the widget background.
   * On iOS the shadow is either visible or not (property set to 0), but always looks the same.
   *
   * @exception IllegalArgumentException
   * <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if the page elevation is negative</li>
   * </ul>
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setPageElevation( int pageElevation ) {
    checkWidget();
    when( pageElevation < 0 ).throwIllegalArgument( "PageElevation must be positive number or zero" );
    if( pageElevation != this.pageElevation) {
      this.pageElevation = pageElevation;
      remoteObject.set( PROPERTY_PAGE_ELEVATION, pageElevation );
    }
  }

  /**
   * Returns page elevation.
   *
   * @return the page elevation
   *
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getPageElevation() {
    checkWidget();
    return pageElevation;
  }

  /**
   * Sets additional space to add inside the widgets bounds.
   *
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setViewPadding( int top, int right, int bottom, int left ) {
    checkWidget();
    BoxDimensions padding = new BoxDimensions( top, right, bottom, left );
    if( !padding.equals( this.padding ) ) {
      this.padding = padding;
      remoteObject.set( PROPERTY_PADDING, new JsonArray().add( top ).add( right ).add( bottom ).add( left ) );
    }
  }

  /**
   * Returns view padding
   *
   * @return the view padding
   *
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public BoxDimensions getViewPadding() {
    checkWidget();
    return padding;
  }

  /**
   * Sets vertical spacing between the individual pages.
   * The default is specific to the device and can not be changed on iOS.
   *
   * @exception IllegalArgumentException
   * <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if the vertical spacing is zero or negative</li>
   * </ul>
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setSpacing( int spacing ) {
    checkWidget();
    when( spacing <= 0 ).throwIllegalArgument( "Spacing must be positive number" );
    if( spacing != this.spacing) {
      this.spacing = spacing;
      remoteObject.set( PROPERTY_SPACING, spacing );
    }
  }

  /**
   * Returns vertical spacing between the individual pages.
   *
   * @return the spacing
   *
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getSpacing() {
    checkWidget();
    return spacing;
  }

  /**
   * Sets a color visible in the transparent areas of a page.
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
  public void setPageBackground( Color pageBackground ) {
    checkWidget();
    whenNull( pageBackground ).throwIllegalArgument( "pageBackground must not be null" );
    when( pageBackground.isDisposed() ).throwIllegalArgument( "pageBackground is disposed" );
    if( !pageBackground.equals( this.pageBackground ) ) {
      this.pageBackground = pageBackground;
      remoteObject.set( PROPERTY_PAGE_BACKGROUND, toJson( pageBackground ) );
    }
  }

  /**
   * Returns the receiver's page background color.
   *
   * @return the receiver's page background color
   *
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public Color getPageBackground() {
    checkWidget();
    return pageBackground;
  }

  /**
   * Enables the pinch-to-zoom gesture. On iOS this will scale the entire document,
   * while on Android each page is scaled individually.
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
   * Adds a {@link LoadListener} to receive notifications of pdf loading.
   *
   * @param listener the listener to add
   *
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
   public void addLoadListener( LoadListener listener ) {
     checkWidget();
     whenNull( listener ).throwIllegalArgument( "LoadListener must not be null" );
     loadListeners.add( listener );
   }

  /**
   * Removes a {@link LoadListener}.
   *
   * @param listener the listener to remove
   *
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
   public void removeLoadListener( LoadListener listener ) {
     checkWidget();
     whenNull( listener ).throwIllegalArgument( "LoadListener must not be null" );
     loadListeners.remove( listener );
   }

  private class PdfViewOperationHandler extends CompositeOperationHandler {

    public PdfViewOperationHandler( PdfView control ) {
      super( control );
    }

    @Override
    public void handleNotify( Composite control, String eventName, JsonObject properties ) {
      if( EVENT_SUCCESS.equals( eventName ) ) {
        notifyLoadSucceeded();
      } else if( EVENT_ERROR.equals( eventName )  ) {
        notifyLoadFailed();
      } else {
        super.handleNotify( control, eventName, properties );
      }
    }

    private void notifyLoadSucceeded() {
      ProcessActionRunner.add( new Runnable() {
        @Override
        public void run() {
          LoadListener[] listeners = loadListeners.toArray( new LoadListener[ 0 ] );
          for( LoadListener listener : listeners ) {
            listener.loadSucceeded();
          }
        }
      } );
    }

    private void notifyLoadFailed() {
      ProcessActionRunner.add( new Runnable() {
        @Override
        public void run() {
          LoadListener[] listeners = loadListeners.toArray( new LoadListener[ 0 ] );
          for( LoadListener listener : listeners ) {
            listener.loadFailed();
          }
        }
      } );
    }

  }

}
