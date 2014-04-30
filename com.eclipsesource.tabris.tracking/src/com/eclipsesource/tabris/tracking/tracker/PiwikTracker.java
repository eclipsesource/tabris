/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.tracker;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import com.eclipsesource.tabris.tracking.Tracker;
import com.eclipsesource.tabris.tracking.TrackingEvent;
import com.eclipsesource.tabris.tracking.TrackingEvent.EventType;
import com.eclipsesource.tabris.tracking.TrackingInfo;
import com.eclipsesource.tabris.tracking.internal.piwik.Piwik;
import com.eclipsesource.tabris.tracking.internal.piwik.model.AdvancedConfiguration;
import com.eclipsesource.tabris.tracking.internal.piwik.model.CustomVariablesBuilder;
import com.eclipsesource.tabris.tracking.internal.piwik.model.PiwikConfiguration;
import com.eclipsesource.tabris.tracking.internal.piwik.model.VisitorInformation;
import com.eclipsesource.tabris.tracking.internal.piwik.model.action.Action;
import com.eclipsesource.tabris.tracking.internal.piwik.model.action.SearchAction;
import com.eclipsesource.tabris.tracking.internal.util.UserAgentUtil;
import com.eclipsesource.tabris.ui.ActionConfiguration;
import com.eclipsesource.tabris.ui.PageConfiguration;


@SuppressWarnings("restriction")
public class PiwikTracker implements Tracker {

  private final Piwik piwik;
  private final String tokenAuth;

  public PiwikTracker( String piwikUrl, String tokenAuth, int siteId ) {
    this( new Piwik( piwikUrl, new PiwikConfiguration( "1", siteId ) ), tokenAuth );
  }

  PiwikTracker( Piwik piwik, String tokenAuth ) {
    whenNull( tokenAuth ).throwIllegalArgument( "TokenAuth must not be null." );
    when( tokenAuth.isEmpty() ).throwIllegalArgument( "TokenAuth must not be empty." );
    this.piwik = piwik;
    this.tokenAuth = tokenAuth;
  }

  @Override
  public void handleEvent( TrackingEvent event ) {
    TrackingInfo info = event.getInfo();
    AdvancedConfiguration advancedConfiguration = createAdvancedConfiguration( info );
    VisitorInformation visitorInformation = createVisitorInformation( info );
    Action action = createAction( event );
    piwik.track( action, visitorInformation, advancedConfiguration );
  }

  private Action createAction( TrackingEvent event ) {
    Action action = null;
    if( event.getType() == EventType.PAGE_VIEW ) {
      action = createPageViewAction( event );
    } else if( event.getType() == EventType.ACTION ) {
      action = createActionHitAction( event );
    } else if( event.getType() == EventType.SEARCH ) {
      action = createSearchAction( event );
    }
    return action;
  }

  private Action createPageViewAction( TrackingEvent event ) {
    PageConfiguration pageConfiguration = ( PageConfiguration )event.getDetail();
    return new Action( createHost( event ) + "/page/" + pageConfiguration.getId() );
  }

  private Action createActionHitAction( TrackingEvent event ) {
    ActionConfiguration actionConfiguration = ( ActionConfiguration )event.getDetail();
    return new Action( createHost( event ) + "/action/" + actionConfiguration.getId() );
  }

  private Action createSearchAction( TrackingEvent event ) {
    ActionConfiguration actionConfiguration = ( ActionConfiguration )event.getDetail();
    return new SearchAction( createHost( event ) + "/action/search/" + actionConfiguration.getId(),
                             event.getInfo().getSearchQuery() );
  }

  private String createHost( TrackingEvent event ) {
    return "http://" + event.getInfo().getAppId();
  }

  private VisitorInformation createVisitorInformation( TrackingInfo info ) {
    VisitorInformation visitorInformation = new VisitorInformation();
    String screenResolution = info.getScreenResolution().x + "x" + info.getScreenResolution().y;
    visitorInformation.setScreenResolution( screenResolution );
    visitorInformation.setUserAgentOverride( UserAgentUtil.getProvider( info.getPlatform() ).getUserAgent( info ) );
    visitorInformation.setCustomVariables( createCustomVariables( info ) );
    visitorInformation.setLanguageOverride( info.getClientLocale().getLanguage() );
    visitorInformation.setId( info.getClientId() );
    return visitorInformation;
  }

  private String createCustomVariables( TrackingInfo info ) {
    CustomVariablesBuilder builder = new CustomVariablesBuilder();
    builder.addCustomVariable( "Application Version", info.getAppVersion() );
    builder.addCustomVariable( "Locale", info.getClientLocale().getLanguage() + "-" + info.getClientLocale().getCountry() );
    builder.addCustomVariable( "Device Model", info.getDeviceModel() );
    builder.addCustomVariable( "Device OS Version", info.getDeviceOsVersion() );
    builder.addCustomVariable( "Device Vendor", info.getDeviceVendor() );
    builder.addCustomVariable( "Tabris Version", info.getTabrisVersion() );
    return builder.getJson();
  }

  private AdvancedConfiguration createAdvancedConfiguration( TrackingInfo info ) {
    AdvancedConfiguration configuration = new AdvancedConfiguration( tokenAuth );
    configuration.setVisitorIpOverride( info.getClientIp() );
    return configuration;
  }
}
