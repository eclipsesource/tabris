/*******************************************************************************
 * Copyright (c) 2018 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets;

import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.EVENT_ERROR;
import static com.eclipsesource.tabris.internal.Constants.EVENT_SUCCESS;
import static com.eclipsesource.tabris.internal.Constants.METHOD_START;
import static com.eclipsesource.tabris.internal.Constants.METHOD_STOP;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_DATA;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ERROR_MESSAGE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_FORMAT;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_FORMATS;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PARENT;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_RUNNING;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_SCALE_MODE;
import static com.eclipsesource.tabris.internal.Constants.TYPE_BARCODE_SCANNER;
import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.lifecycle.ProcessActionRunner;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.internal.widgets.compositekit.CompositeOperationHandler;
import org.eclipse.swt.widgets.Composite;


/**
 * <p>
 * The {@link BarcodeScanner} widget can be used to to scan various types of barcodes.
 * </p>
 * <p>
 * <b>Please Note:</b> Scanning is an asynchronous operation. For this reason you need to attach a {@link ScanListener}
 * to the {@link BarcodeScanner} widget to receive scan results.
 * </p>
 * <p>
 * <b>BarcodeScanner is currently not supported on Windows.</b>
 * </p>
 *
 * @since 3.4
 */
@SuppressWarnings("restriction")
public class BarcodeScanner extends Composite {

  public static enum Formats {
    /**
     * UPC-A. Supported on Android and iOS.
     */
    upcA,
    /**
     * UPC-E. Supported on Android and iOS.
     */
    upcE,
    /**
     * Code 39. Supported on Android and iOS.
     */
    code39,
    /**
     * Code 39 Mod 43. Supported on iOS.
     */
    code39Mod43,
    /**
     * Code93. Supported on Android and iOS.
     */
    code93,
    /**
     * Code128. Supported on Android and iOS.
     */
    code128,
    /**
     * EAN-8. Supported on Android and iOS.
     */
    ean8,
    /**
     * EAN-13. Supported on Android and iOS.
     */
    ean13,
    /**
     * PDF417. Supported on Android and iOS.
     */
    pdf417,
    /**
     * QR. Supported on Android and iOS.
     */
    qr,
    /**
     * Aztec. Supported on Android and iOS.
     */
    aztec,
    /**
     * Interleaved 2 of 5. Supported on iOS.
     */
    interleaved2of5,
    /**
     * ITF14. Supported on Android and iOS.
     */
    itf,
    /**
     * DataMatrix. Supported on Android and iOS.
     */
    dataMatrix,
    /**
     * Codabar. Supported on Android.
     */
    codabar
  }

  /**
   * How to scale the camera frame inside the bounds of the <code>BarcodeScanner</code>.
   */
  public static enum ScaleMode {
    /**
     * Shows the full camera frame while potentially leaving blank space on the sides.
     */
    FIT,
    /**
     * Makes the camera frame cover the view bounds while potentially clipping some of the camera frame edges.
     */
    FILL,
  }

  private final RemoteObject remoteObject;
  private ScaleMode scaleMode = ScaleMode.FIT;
  private final List<ScanListener> scanListeners = new ArrayList<ScanListener>();
  private boolean running;

  public BarcodeScanner( Composite parent ) {
    super( parent, SWT.NONE );
    Connection connection = RWT.getUISession().getConnection();
    remoteObject = connection.createRemoteObject( TYPE_BARCODE_SCANNER );
    remoteObject.setHandler( new BarcodeScannerOperationHandler( this ) );
    remoteObject.set( PROPERTY_PARENT, getId( this ) );
  }

  /**
   * Selects the method used for camera frame scaling. The default is </code>ScaleMode.FIT</code>.
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
   * Returns the camera frame scale mode.
   *
   * @return the camera frame scale mode
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
   * <p>
   * Enables the camera and starts scanning for barcodes. When started, the <code>BarcodeScanner</code>
   * continuously notifies the <code>ScanListener</code> as soon as it finds a barcode in its view.
   * </p>
   *
   * @param formats specifies barcode formats to be recognized
   *
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void start( Formats[] formats ) {
    checkWidget();
    if( !running ) {
      JsonObject props = new JsonObject();
      JsonArray jsonFormats = new JsonArray();
      for( Formats format : formats ) {
        jsonFormats.add( format.toString() );
      }
      props.add( PROPERTY_FORMATS, jsonFormats );
      remoteObject.call( METHOD_START, props );
      running = true;
    }
  }

  /**
   * <p>
   * Stops the barcode scanning and disables the camera.
   * </p>
   *
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void stop() {
    checkWidget();
    if( running ) {
      remoteObject.call( METHOD_STOP, null );
      running = false;
    }
  }

  /**
   * <p>
   * Returns true if barcode scanning is running, false otherwise.
   * </p>
   *
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public boolean isRunning() {
    checkWidget();
    return running;
  }

  /**
   * Adds a {@link ScanListener} to receive notifications of barcode scanning results.
   *
   * @param listener the listener to add
   *
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   */
   public void addScanListener( ScanListener listener ) {
     checkWidget();
     whenNull( listener ).throwIllegalArgument( "ScanListener must not be null" );
     scanListeners.add( listener );
   }

  /**
   * Removes a {@link ScanListener}.
   *
   * @param listener the listener to remove
   *
   * @exception SWTException
   * <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   */
   public void removeScanListener( ScanListener listener ) {
     checkWidget();
     whenNull( listener ).throwIllegalArgument( "ScanListener must not be null" );
     scanListeners.remove( listener );
   }

   private class BarcodeScannerOperationHandler extends CompositeOperationHandler {

     public BarcodeScannerOperationHandler( BarcodeScanner scanner ) {
       super( scanner );
     }

     @Override
     public void handleSet( Composite control, JsonObject properties ) {
       super.handleSet( control, properties );
       handleSetRunning( control, properties );
     }

     public void handleSetRunning( Composite control, JsonObject properties ) {
       JsonValue value = properties.get( PROPERTY_RUNNING );
       if( value != null ) {
         running = value.asBoolean();
       }
     }

     @Override
     public void handleNotify( Composite control, String eventName, JsonObject properties ) {
       if( EVENT_SUCCESS.equals( eventName ) ) {
         notifyScanSucceeded( ( BarcodeScanner )control, properties );
       } else if( EVENT_ERROR.equals( eventName )  ) {
         notifyScanFailed( ( BarcodeScanner )control, properties );
       } else {
         super.handleNotify( control, eventName, properties );
       }
     }

     private void notifyScanSucceeded( final BarcodeScanner scanner, final JsonObject properties ) {
       ProcessActionRunner.add( new Runnable() {
         @Override
         public void run() {
           ScanListener[] listeners = scanListeners.toArray( new ScanListener[ 0 ] );
           for( ScanListener listener : listeners ) {
             String format = properties.get( PROPERTY_FORMAT ).asString();
             String data = properties.get( PROPERTY_DATA ).asString();
             listener.scanSucceeded( format, data );
           }
         }
       } );
     }

     private void notifyScanFailed( final BarcodeScanner scanner, final JsonObject properties ) {
       ProcessActionRunner.add( new Runnable() {
         @Override
         public void run() {
           ScanListener[] listeners = scanListeners.toArray( new ScanListener[ 0 ] );
           for( ScanListener listener : listeners ) {
             String error = properties.get( PROPERTY_ERROR_MESSAGE ).asString();
             listener.scanFailed( error );
           }
         }
       } );
     }

   }

}
