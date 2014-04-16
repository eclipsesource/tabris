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
package com.eclipsesource.tabris.tracking.internal.analytics.model.hit;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.DNS_TIME;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.PAGE_DOWNLOAD_TIME;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.PAGE_LOAD_TIME;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.REDIRECT_RESPONSE_TIME;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.SERVER_RESPONSE_TIME;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.TCP_CONNECT_TIME;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.USER_TIMING_CATEGORY;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.USER_TIMING_LABEL;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.USER_TIMING_TIME;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.USER_TIMING_VARIABLE_NAME;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValueProvider.getRequestValue;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValues.HIT_TIMING;


@SuppressWarnings("restriction")
public class TimingHit extends Hit {

  public TimingHit() {
    super( getRequestValue( HIT_TIMING ) );
  }

  public TimingHit setUserTimingCategory( String userTimingCategory ) {
    whenNull( userTimingCategory ).throwIllegalArgument( "UserTimingCategory must not be null." );
    when( userTimingCategory.isEmpty() ).throwIllegalArgument( "UserTimingCategory must not be empty." );
    addParameter( getRequestKey( USER_TIMING_CATEGORY ), userTimingCategory );
    return this;
  }

  public TimingHit setUserTimingVariableName( String userTimingVariableName ) {
    whenNull( userTimingVariableName ).throwIllegalArgument( "UserTimingVariableName must not be null." );
    when( userTimingVariableName.isEmpty() ).throwIllegalArgument( "UserTimingVariableName must not be empty." );
    addParameter( getRequestKey( USER_TIMING_VARIABLE_NAME ), userTimingVariableName );
    return this;
  }

  public TimingHit setUserTimingTime( int userTimingTime ) {
    addParameter( getRequestKey( USER_TIMING_TIME ), Integer.valueOf( userTimingTime ) );
    return this;
  }

  public TimingHit setPageLoadTime( int pageLoadTime ) {
    addParameter( getRequestKey( PAGE_LOAD_TIME ), Integer.valueOf( pageLoadTime ) );
    return this;
  }

  public TimingHit setDnsTime( int dnsTime ) {
    addParameter( getRequestKey( DNS_TIME ), Integer.valueOf( dnsTime ) );
    return this;
  }

  public TimingHit setUserTimingLabel( String userTimingLabel ) {
    whenNull( userTimingLabel ).throwIllegalArgument( "UserTimingLabel must not be null." );
    when( userTimingLabel.isEmpty() ).throwIllegalArgument( "UserTimingLabel must not be empty." );
    addParameter( getRequestKey( USER_TIMING_LABEL ), userTimingLabel );
    return this;
  }

  public TimingHit setPageDownloadTime( int pageDownloadTime ) {
    addParameter( getRequestKey( PAGE_DOWNLOAD_TIME ), Integer.valueOf( pageDownloadTime ) );
    return this;
  }

  public TimingHit setRedirectResponseTime( int redirectResponseTime ) {
    addParameter( getRequestKey( REDIRECT_RESPONSE_TIME ), Integer.valueOf( redirectResponseTime ) );
    return this;
  }

  public TimingHit setTcpConnectTime( int tcpConnectTime ) {
    addParameter( getRequestKey( TCP_CONNECT_TIME ), Integer.valueOf( tcpConnectTime ) );
    return this;
  }

  public TimingHit setServerResponseTime( int serverResponseTime ) {
    addParameter( getRequestKey( SERVER_RESPONSE_TIME ), Integer.valueOf( serverResponseTime ) );
    return this;
  }

}
