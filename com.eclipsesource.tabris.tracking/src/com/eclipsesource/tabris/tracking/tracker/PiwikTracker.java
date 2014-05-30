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

import java.util.ArrayList;
import java.util.List;

import com.eclipsesource.tabris.tracking.Order;
import com.eclipsesource.tabris.tracking.OrderItem;
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
import com.eclipsesource.tabris.tracking.internal.piwik.model.action.EcommerceAction;
import com.eclipsesource.tabris.tracking.internal.piwik.model.action.SearchAction;
import com.eclipsesource.tabris.tracking.internal.piwik.model.ecommerce.EcommerceItem;
import com.eclipsesource.tabris.tracking.internal.piwik.model.ecommerce.EcommerceItemsBuilder;
import com.eclipsesource.tabris.tracking.internal.util.UserAgentUtil;


/**
 * <p>
 * The {@link PiwikTracker} submits all {@link TrackingEvent}s to <a href="http://piwik.org/">Piwik</a>. It can be
 * used to track UI events, searches, custom events and ecommerce events.
 * </p>
 * <p>
 * To make use of Piwik its features different events will have a different mappings.
 * These are:
 *   <ul>
 *   <li><b>Page Views:</b> Will be mapped to an "Action" with the path prefix "/page".</li>
 *   <li><b>Actions:</b> Will be mapped to an "Action" with the path prefix "/action".</li>
 *   <li><b>Search:</b> Will be mapped to a "Search Action" with the path prefix "/action/search" and the query as
 *                      value.</li>
  *   <li><b>Custom Events:</b>  Will be mapped to an "Action" with the path prefix "/action/custom".</li>
 *   <li><b>Orders:</b> Will be mapped to Piwik's ecommerce tracking.</li>
 *   </ul>
 * </p>
 *
 * @since 1.4
 */
@SuppressWarnings("restriction")
public class PiwikTracker implements Tracker {

  private final Piwik piwik;
  private final String tokenAuth;

  /**
   * <p>
   * Creates a new {@link PiwikTracker}.
   * </p>
   *
   * @param piwikUrl the url of the Piwik installation. Must not be <code>null</code> or empty.
   * @param tokenAuth the token to validate a request. Must not be <code>null</code> or empty.
   * @param siteId the id of the Piwik site configuration to use. Must be > 0.
   */
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
    } else if( event.getType() == EventType.ORDER ) {
      action = createEcommerceAction( event );
    }
    return action;
  }

  private Action createPageViewAction( TrackingEvent event ) {
    String pageId = ( String )event.getDetail();
    return new Action( createHost( event ) + "/page/" + pageId );
  }

  private Action createActionHitAction( TrackingEvent event ) {
    String actionId = ( String )event.getDetail();
    return new Action( createHost( event ) + "/action/" + actionId );
  }

  private Action createSearchAction( TrackingEvent event ) {
    String actionId = ( String )event.getDetail();
    return new SearchAction( createHost( event ) + "/action/search/" + actionId, event.getInfo().getSearchQuery() );
  }

  private Action createEcommerceAction( TrackingEvent event ) {
    Order order = ( Order )event.getDetail();
    EcommerceAction action = new EcommerceAction( createHost( event ) + "/action/ecommerce/" + order.getOrderId(),
                                                  order.getOrderId(),
                                                  order.getTotal() );
    action.setTax( order.getTax() );
    action.setShipping( order.getShipping() );
    if( !order.getItems().isEmpty() ) {
      action.setItems( getOrderItems( order.getItems() ) );
    }
    return action;
  }

  private String getOrderItems( List<OrderItem> list ) {
    List<EcommerceItem> items = createEcommerceItems( list );
    return new EcommerceItemsBuilder( items ).buildJson();
  }

  private List<EcommerceItem> createEcommerceItems( List<OrderItem> list ) {
    List<EcommerceItem> ecommerceItems = new ArrayList<EcommerceItem>();
    for( OrderItem item : list ) {
      ecommerceItems.add( createEcommerceItem( item ) );
    }
    return ecommerceItems;
  }

  private EcommerceItem createEcommerceItem( OrderItem item ) {
    EcommerceItem ecommerceItem = new EcommerceItem( item.getName() );
    if( item.getCategory() != null ) {
      ecommerceItem.setCategory( item.getCategory() );
    }
    if( item.getPrice() != null ) {
      ecommerceItem.setPrice( item.getPrice() );
    }
    ecommerceItem.setQuantity( item.getQuantity() );
    if( item.getSKU() != null ) {
      ecommerceItem.setSku( item.getSKU() );
    }
    return ecommerceItem;
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
