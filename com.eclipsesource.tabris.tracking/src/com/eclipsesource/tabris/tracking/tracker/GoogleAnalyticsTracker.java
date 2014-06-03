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

import static com.eclipsesource.tabris.internal.Clauses.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.eclipsesource.tabris.tracking.Order;
import com.eclipsesource.tabris.tracking.OrderItem;
import com.eclipsesource.tabris.tracking.Tracker;
import com.eclipsesource.tabris.tracking.TrackingEvent;
import com.eclipsesource.tabris.tracking.TrackingEvent.EventType;
import com.eclipsesource.tabris.tracking.TrackingInfo;
import com.eclipsesource.tabris.tracking.internal.analytics.GoogleAnalytics;
import com.eclipsesource.tabris.tracking.internal.analytics.model.AdvancedConfiguration;
import com.eclipsesource.tabris.tracking.internal.analytics.model.AnalyticsConfiguration;
import com.eclipsesource.tabris.tracking.internal.analytics.model.hit.EventHit;
import com.eclipsesource.tabris.tracking.internal.analytics.model.hit.Hit;
import com.eclipsesource.tabris.tracking.internal.analytics.model.hit.ItemHit;
import com.eclipsesource.tabris.tracking.internal.analytics.model.hit.ScreenViewHit;
import com.eclipsesource.tabris.tracking.internal.analytics.model.hit.TransactionHit;
import com.eclipsesource.tabris.tracking.internal.util.UserAgentUtil;
import com.eclipsesource.tabris.ui.Page;


/**
 * <p>
 * The {@link GoogleAnalyticsTracker} submits all {@link TrackingEvent}s to
 * <a href="http://www.google.com/analytics/">Google Analytics</a>. It can be used to track UI events, searches,
 * custom events and ecommerce events.
 * </p>
 * <p>
 * To make use of Google Analytics its features different events will have a different category and label mappings.
 * These are:
 *   <ul>
 *   <li><b>Page Views:</b> Will be mapped to "Screens". The {@link Page} id will be used as "Screen Name".</li>
 *   <li><b>Actions:</b> Will be mapped to "Events". The category for an action is "tabris.ui.action" and its label
 *                       is "execute".</li>
 *   <li><b>Search:</b> Will be mapped to "Events". The category for an action is "tabris.ui.action.search" and its
 *                      label is "search". The search query will be transfered using a custom dimension with the default
 *                      index 1. You can configure the index with
 *                      {@link GoogleAnalyticsTracker#setSearchCustomDimension(int)}</li>
  *   <li><b>Custom Events:</b> Will be mapped to "Events". The category for an action is "tabris.event" and its label
 *                       is "custom".</li>
 *   <li><b>Orders:</b> Will be mapped to "Conversions".</li>
 *   </ul>
 * </p>
 *
 * @since 1.4
 */
@SuppressWarnings("restriction")
public class GoogleAnalyticsTracker implements Tracker {

  static final String LABEL_SEARCH = "search";
  static final String LABEL_EXECUTE = "execute";
  static final String LABEL_EVENT = "custom";
  static final String CATEGORY_SEARCH = "tabris.ui.action.search";
  static final String CATEGORY_ACTION = "tabris.ui.action";
  static final String CATEGORY_EVENT = "tabris.event";

  private final GoogleAnalytics analytics;
  private int searchIndex;

  /**
   * <p>
   * Create a new {@link GoogleAnalyticsTracker} with the trackingId to use. The trackingId needs to be a valid Google
   * Analytics trackingId associated with a mobile application.
   * </p>
   *
   * @param trackingId the Google Analytics tracking id. Must not be <code>null</code> or empty.
   * @param appName the application name to display. Must not be <code>null</code> or empty.
   */
  public GoogleAnalyticsTracker( String trackingId, String appName ) {
    this( new GoogleAnalytics( appName, new AnalyticsConfiguration( "1", trackingId ) ) );
  }

  GoogleAnalyticsTracker( GoogleAnalytics analytics ) {
    this.analytics = analytics;
    this.searchIndex = 1;
  }

  /**
   * <p>
   * Sets the custom dimension index for search queries.
   * </p>
   *
   * @param index the index to use. Must be > 0.
   */
  public void setSearchCustomDimension( int index ) {
    when( index <= 0 ).throwIllegalArgument( "Index must be > 0 but was " + index );
    this.searchIndex = index;
  }

  @Override
  public void handleEvent( TrackingEvent event ) {
    AdvancedConfiguration advancedConfiguration = createAdvancedConfiguration( event );
    List<Hit> hits = createHits( event, advancedConfiguration );
    for( Hit hit : hits ) {
      analytics.track( hit, event.getInfo().getClientId(), advancedConfiguration );
    }
  }

  private List<Hit> createHits( TrackingEvent event, AdvancedConfiguration advancedConfiguration ) {
    List<Hit> result = new ArrayList<Hit>();
    if( event.getType() == EventType.PAGE_VIEW ) {
      result.add( createScreenViewHit( event ) );
    } else if( event.getType() == EventType.ACTION ) {
      result.add( createActionHit( event ) );
    } else if( event.getType() == EventType.SEARCH ) {
      result.add( createSearchHit( event, advancedConfiguration ) );
    } else if( event.getType() == EventType.ORDER ) {
      result.add( createTransactionHit( event, advancedConfiguration ) );
      result.addAll( createItemHits( event, advancedConfiguration ) );
    } else if( event.getType() == EventType.EVENT ) {
      result.add( createEventHit( event ) );
    }
    return result;
  }

  private Hit createScreenViewHit( TrackingEvent event ) {
    String pageId = ( String )event.getDetail();
    return new ScreenViewHit( pageId );
  }

  private Hit createActionHit( TrackingEvent event ) {
    String actionId = ( String )event.getDetail();
    EventHit eventHit = new EventHit();
    eventHit.setCategory( CATEGORY_ACTION );
    eventHit.setAction( LABEL_EXECUTE );
    eventHit.setLabel( actionId );
    return eventHit;
  }

  private Hit createSearchHit( TrackingEvent event, AdvancedConfiguration advancedConfiguration ) {
    String actionId = ( String )event.getDetail();
    EventHit eventHit = new EventHit();
    eventHit.setCategory( CATEGORY_SEARCH );
    eventHit.setAction( LABEL_SEARCH );
    eventHit.setLabel( actionId );
    advancedConfiguration.setCustomDimension( searchIndex, event.getInfo().getSearchQuery() );
    return eventHit;
  }

  private Hit createTransactionHit( TrackingEvent event, AdvancedConfiguration advancedConfiguration ) {
    Order order = ( Order )event.getDetail();
    TransactionHit hit = new TransactionHit( order.getOrderId() );
    hit.setRevenue( toDouble( order.getTotal() ) );
    hit.setShipping( toDouble( order.getShipping() ) );
    hit.setTax( toDouble( order.getTax() ) );
    return hit;
  }

  private List<Hit> createItemHits( TrackingEvent event, AdvancedConfiguration advancedConfiguration ) {
    Order order = ( Order )event.getDetail();
    List<Hit> itemHits = new ArrayList<Hit>();
    for( OrderItem item : order.getItems() ) {
      itemHits.add( createItemHit( order.getOrderId(), item ) );
    }
    return itemHits;
  }

  private Hit createItemHit( String orderId, OrderItem item ) {
    ItemHit itemHit = new ItemHit( item.getName(), orderId );
    if( item.getCategory() != null ) {
      itemHit.setCategory( item.getCategory() );
    }
    if( item.getSKU() != null ) {
      itemHit.setCode( item.getSKU() );
    }
    itemHit.setPrice( toDouble( item.getPrice() ) );
    itemHit.setQuantity( item.getQuantity() );
    return itemHit;
  }

  private Hit createEventHit( TrackingEvent event ) {
    String eventId = ( String )event.getDetail();
    EventHit eventHit = new EventHit();
    eventHit.setCategory( CATEGORY_EVENT );
    eventHit.setAction( LABEL_EVENT );
    eventHit.setLabel( eventId );
    return eventHit;
  }

  private AdvancedConfiguration createAdvancedConfiguration( TrackingEvent event ) {
    AdvancedConfiguration configuration = new AdvancedConfiguration();
    TrackingInfo info = event.getInfo();
    String screenResolution = info.getScreenResolution().x + "x" + info.getScreenResolution().y;
    configuration.setScreenResolution( screenResolution );
    configuration.setAppId( info.getAppId() );
    configuration.setAppVersion( info.getAppVersion() );
    configuration.setIpOverride( info.getClientIp() );
    configuration.setUserAgentOverride( UserAgentUtil.getProvider( info.getPlatform() ).getUserAgent( info ) );
    configuration.setUserLanguage( info.getClientLocale().getLanguage() + "-" + info.getClientLocale().getCountry() );
    configuration.setViewportSize( screenResolution );
    return configuration;
  }

  private double toDouble( BigDecimal amount ) {
    return amount.setScale( 2, RoundingMode.HALF_UP ).doubleValue();
  }

}

