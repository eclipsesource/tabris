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
package com.eclipsesource.tabris.tracking.tracker;

import com.eclipsesource.tabris.tracking.Tracker;
import com.eclipsesource.tabris.tracking.TrackingEvent;
import com.eclipsesource.tabris.tracking.TrackingEvent.EventType;
import com.eclipsesource.tabris.tracking.TrackingInfo;
import com.eclipsesource.tabris.tracking.internal.analytics.GoogleAnalytics;
import com.eclipsesource.tabris.tracking.internal.analytics.model.AdvancedConfiguration;
import com.eclipsesource.tabris.tracking.internal.analytics.model.AnalyticsConfiguration;
import com.eclipsesource.tabris.tracking.internal.analytics.model.hit.AppViewHit;
import com.eclipsesource.tabris.tracking.internal.analytics.model.hit.EventHit;
import com.eclipsesource.tabris.tracking.internal.analytics.model.hit.Hit;
import com.eclipsesource.tabris.tracking.internal.util.UserAgentUtil;
import com.eclipsesource.tabris.ui.ActionConfiguration;
import com.eclipsesource.tabris.ui.PageConfiguration;


/**
 * @since 1.4
 */
public class GoogleAnalyticsTracker implements Tracker {

  static final String LABEL_SEARCH = "search";
  static final String LABEL_EXECUTE = "execute";
  static final String CATEGORY_SEARCH = "tabris.ui.action.search";
  static final String CATEGORY_ACTION = "tabris.ui.action";

  private final GoogleAnalytics analytics;

  public GoogleAnalyticsTracker( String trackingId, String appName ) {
    this( new GoogleAnalytics( appName, new AnalyticsConfiguration( "1", trackingId ) ) );
  }

  GoogleAnalyticsTracker( GoogleAnalytics analytics ) {
    this.analytics = analytics;
  }

  @Override
  public void handleEvent( TrackingEvent event ) {
    AdvancedConfiguration advancedConfiguration = createAdvancedConfiguration( event );
    Hit hit = createHit( event, advancedConfiguration );
    analytics.track( hit, event.getInfo().getClientId(), advancedConfiguration );
  }

  private Hit createHit( TrackingEvent event, AdvancedConfiguration advancedConfiguration ) {
    Hit result = null;
    if( event.getType() == EventType.PAGE_VIEW ) {
      result = createAppViewHit( event );
    } else if( event.getType() == EventType.ACTION ) {
      result = createActionHit( event );
    } else if( event.getType() == EventType.SEARCH ) {
      result = createSearchHit( event, advancedConfiguration );
    }
    return result;
  }

  private Hit createAppViewHit( TrackingEvent event ) {
    PageConfiguration pageConfiguration = ( PageConfiguration )event.getDetail();
    return new AppViewHit( pageConfiguration.getId() );
  }

  private Hit createActionHit( TrackingEvent event ) {
    ActionConfiguration actionConfiguration = ( ActionConfiguration )event.getDetail();
    EventHit eventHit = new EventHit();
    eventHit.setCategory( CATEGORY_ACTION );
    eventHit.setAction( LABEL_EXECUTE );
    eventHit.setLabel( actionConfiguration.getId() );
    return eventHit;
  }

  private Hit createSearchHit( TrackingEvent event, AdvancedConfiguration advancedConfiguration ) {
    ActionConfiguration actionConfiguration = ( ActionConfiguration )event.getDetail();
    EventHit eventHit = new EventHit();
    eventHit.setCategory( CATEGORY_SEARCH );
    eventHit.setAction( LABEL_SEARCH );
    eventHit.setLabel( actionConfiguration.getId() );
    advancedConfiguration.setCustomDimension( 1, event.getInfo().getSearchQuery() );
    return eventHit;
  }

  private AdvancedConfiguration createAdvancedConfiguration( TrackingEvent event ) {
    AdvancedConfiguration configuration = new AdvancedConfiguration();
    TrackingInfo info = event.getInfo();
    String screenResolution = info.getScreenResolution().x + "x" + info.getScreenResolution().y;
    configuration.setScreenResolution( screenResolution );
    configuration.setAppVersion( info.getAppVersion() );
    configuration.setIpOverride( info.getClientIp() );
    configuration.setUserAgentOverride( UserAgentUtil.getProvider( info.getPlatform() ).getUserAgent( info ) );
    configuration.setUserLanguage( info.getClientLocale().getLanguage() + "-" + info.getClientLocale().getCountry() );
    configuration.setViewportSize( screenResolution );
    return configuration;
  }

}

