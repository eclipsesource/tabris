/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.model;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.IS_BOT;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.NEW_VISIT;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.TOKEN_AUTH;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_CITY_OVERRIDE;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_COUNTRY_OVERRIDE;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_DATETIME_OVERRIDE;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_ID_ENFORCED;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_IP_OVERRIDE;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_LATITUDE_OVERRIDE;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_LONGITUDE_OVERRIDE;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_REGION_OVERRIDE;

import java.util.HashMap;
import java.util.Map;

import com.eclipsesource.tabris.tracking.internal.Requestable;


@SuppressWarnings("restriction")
public class AdvancedConfiguration implements Requestable {

  private final Map<String, Object> parameters;

  public AdvancedConfiguration( String tokenAuth ) {
    whenNull( tokenAuth ).throwIllegalArgument( "TokenAuth must not be null." );
    when( tokenAuth.length() != 32 ).throwIllegalArgument( "TokenAuth must be a String of 32 characters." );
    parameters = new HashMap<String, Object>();
    addParameter( getRequestKey( TOKEN_AUTH ), tokenAuth );
  }

  protected void addParameter( String key, Object value ) {
    parameters.put( key, value );
  }

  @Override
  public Map<String, Object> getParameter() {
    return parameters;
  }

  public AdvancedConfiguration setVisitorIpOverride( String visitorIpOverride ) {
    whenNull( visitorIpOverride ).throwIllegalArgument( "VisitorIpOverride must not be null." );
    when( visitorIpOverride.isEmpty() ).throwIllegalArgument( "VisitorIpOverride must not be empty." );
    addParameter( getRequestKey( VISITOR_IP_OVERRIDE ), visitorIpOverride );
    return this;
  }

  public AdvancedConfiguration setDatetimeOverride( String datetimeOverride ) {
    whenNull( datetimeOverride ).throwIllegalArgument( "Datetime must not be null." );
    when( datetimeOverride.isEmpty() ).throwIllegalArgument( "Datetime must not be empty." );
    addParameter( getRequestKey( VISITOR_DATETIME_OVERRIDE ), datetimeOverride );
    return this;
  }

  public AdvancedConfiguration setVisitorIdEnforced( String visitorIdEnforced ) {
    whenNull( visitorIdEnforced ).throwIllegalArgument( "VisitorIdEnforced must not be null." );
    when( visitorIdEnforced.isEmpty() ).throwIllegalArgument( "VisitorIdEnforced must not be empty." );
    addParameter( getRequestKey( VISITOR_ID_ENFORCED ), visitorIdEnforced );
    return this;
  }

  public AdvancedConfiguration setNewVisit( boolean newVisit ) {
    addParameter( getRequestKey( NEW_VISIT ), Boolean.valueOf( newVisit ) );
    return this;
  }

  public AdvancedConfiguration setCountryOverride( String visitorCountryOverride ) {
    whenNull( visitorCountryOverride ).throwIllegalArgument( "CountryOverride must not be null." );
    when( visitorCountryOverride.isEmpty() ).throwIllegalArgument( "CountryOverride must not be empty." );
    addParameter( getRequestKey( VISITOR_COUNTRY_OVERRIDE ), visitorCountryOverride );
    return this;
  }

  public AdvancedConfiguration setRegionOverride( String regionOverride ) {
    whenNull( regionOverride ).throwIllegalArgument( "RegionOverride must not be null." );
    when( regionOverride.isEmpty() ).throwIllegalArgument( "RegionOverride must not be empty." );
    addParameter( getRequestKey( VISITOR_REGION_OVERRIDE ), regionOverride );
    return this;
  }

  public AdvancedConfiguration setCityOverride( String cityOverride ) {
    whenNull( cityOverride ).throwIllegalArgument( "CityOverride must not be null." );
    when( cityOverride.isEmpty() ).throwIllegalArgument( "CityOverride must not be empty." );
    addParameter( getRequestKey( VISITOR_CITY_OVERRIDE ), cityOverride );
    return this;
  }

  public AdvancedConfiguration setLatitudeOverride( String latitudeOverride ) {
    whenNull( latitudeOverride ).throwIllegalArgument( "LatitudeOverride must not be null." );
    when( latitudeOverride.isEmpty() ).throwIllegalArgument( "LatitudeOverride must not be empty." );
    addParameter( getRequestKey( VISITOR_LATITUDE_OVERRIDE ), latitudeOverride );
    return this;
  }

  public AdvancedConfiguration setLongitudeOverride( String longitudeOverride ) {
    whenNull( longitudeOverride ).throwIllegalArgument( "LongitudeOverride must not be null." );
    when( longitudeOverride.isEmpty() ).throwIllegalArgument( "LongitudeOverride must not be empty." );
    addParameter( getRequestKey( VISITOR_LONGITUDE_OVERRIDE ), longitudeOverride );
    return this;
  }

  public AdvancedConfiguration setIsBot( boolean isBot ) {
    addParameter( getRequestKey( IS_BOT ), Boolean.valueOf( isBot ) );
    return this;
  }
}
