/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal;

import java.io.Serializable;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.eclipsesource.tabris.ClientStore;
import com.eclipsesource.tabris.app.App;
import com.eclipsesource.tabris.device.ClientDevice;
import com.eclipsesource.tabris.internal.Constants;
import com.eclipsesource.tabris.tracking.TrackingInfo;


@SuppressWarnings("restriction")
public class TrackingInfoFactory implements Serializable {

  static final String PROPERTY_ID = "com.eclipsesource.tabris.tracking.id";

  public static TrackingInfo createInfo( Display display ) {
    TrackingInfo trackingInfo = new TrackingInfo();
    trackingInfo.setClientId( getClientId() );
    setClientDeviceInfo( trackingInfo );
    setAppInfo( trackingInfo );
    setDisplayInfo( display, trackingInfo );
    setClientIpInfo( trackingInfo );
    trackingInfo.setUserAgent( RWT.getRequest().getHeader( Constants.USER_AGENT ) );
    return trackingInfo;
  }

  private static void setAppInfo( TrackingInfo trackingInfo ) {
    App app = RWT.getClient().getService( App.class );
    if( app != null ) {
      trackingInfo.setTabrisVersion( app.getTabrisVersion() );
      trackingInfo.setAppId( app.getId() );
      trackingInfo.setAppVersion( app.getVersion() );
    }
  }

  private static void setClientDeviceInfo( TrackingInfo trackingInfo ) {
    ClientDevice device = RWT.getClient().getService( ClientDevice.class );
    if( device != null ) {
      trackingInfo.setDeviceModel( device.getModel() );
      trackingInfo.setDeviceOsVersion( device.getOSVersion() );
      trackingInfo.setDeviceVendor( device.getVendor() );
      trackingInfo.setScaleFactor( device.getScaleFactor() );
      trackingInfo.setPlatform( device.getPlatform() );
      trackingInfo.setClientLocale( device.getLocale() );
      trackingInfo.setClientTimezoneOffset( device.getTimezoneOffset() );
    }
  }

  private static void setDisplayInfo( Display display, TrackingInfo trackingInfo ) {
    Rectangle bounds = display.getBounds();
    trackingInfo.setScreenResolution( new Point( bounds.width, bounds.height ) );
  }

  private static String getClientId() {
    ClientStore clientStore = RWT.getClient().getService( ClientStore.class );
    if( clientStore != null ) {
      String id = clientStore.get( PROPERTY_ID );
      if( id == null ) {
        id = UUID.randomUUID().toString();
        clientStore.add( PROPERTY_ID, id );
      }
      return id;
    }
    return UUID.randomUUID().toString();
  }

  private static void setClientIpInfo( TrackingInfo info ) {
    HttpServletRequest request = RWT.getRequest();
    String ip = request.getHeader( "X-Forwarded-For" );
    if( ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase( ip ) ) {
      ip = request.getHeader( "Proxy-Client-IP" );
    }
    if( ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase( ip ) ) {
      ip = request.getHeader( "WL-Proxy-Client-IP" );
    }
    if( ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase( ip ) ) {
      ip = request.getHeader( "HTTP_CLIENT_IP" );
    }
    if( ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase( ip ) ) {
      ip = request.getHeader( "HTTP_X_FORWARDED_FOR" );
    }
    if( ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase( ip ) ) {
      ip = request.getRemoteAddr();
    }
    info.setClientIp( ip );
  }

  private TrackingInfoFactory() {
    // prevent instantiation
  }
}
