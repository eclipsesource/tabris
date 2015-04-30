/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.ui;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.rap.rwt.Adaptable;

import com.eclipsesource.tabris.internal.ui.ActionDescriptor;
import com.eclipsesource.tabris.internal.ui.ImageUtil;
import com.eclipsesource.tabris.internal.ui.PageDescriptor;
import com.eclipsesource.tabris.internal.ui.UpdateUtil;


/**
 * <p>
 * {@link Page} objects will be instantiated by the framework. Therefore you need to be able to configure such a
 * {@link Page} in a convenient way before it's actually created. This is the single purpose of a
 * {@link PageConfiguration}. It provides methods to define the visual role in a UI e.g. the page image that's used in a
 * menu or if the page should be a top level page or not. When a {@link Page} contains actions the {@link PageConfiguration}
 * is also the right place to add them represented by an {@link ActionConfiguration}.
 * </p>
 * <p>
 * {@link PageConfiguration} objects will be usually added to a {@link UIConfiguration}.
 * </p>
 *
 * @see Page
 * @see ActionConfiguration
 * @see UIConfiguration
 *
 * @since 0.11
 */
public class PageConfiguration implements Adaptable, Serializable {

  private final String id;
  private final Class<? extends Page> pageType;
  private final List<ActionConfiguration> actions;
  private String title;
  private String backCaption;
  private PageStyle[] style;
  private boolean topLevel;
  private byte[] image;

  /**
   * <p>
   * Creates a new {@link PageConfiguration} object with the defined id and a {@link Page} type. The id needs to be
   * unique within the Tabris UI because you will need to use them to browse through the several pages using the
   * {@link UI}.
   * </p>
   *
   * @param pageId the unique id of the {@link Page}. Must not be empty or <code>null</code>.
   * @param pageType the type of the {@link Page} to be created. Must not be <code>null</code>.
   *
   * @since 1.0
   */
  public PageConfiguration( String pageId, Class<? extends Page> pageType ) {
    whenNull( pageId ).throwIllegalArgument( "Page Id must not be null" );
    when( pageId.isEmpty() ).throwIllegalArgument( "Page Id must not be empty" );
    whenNull( pageType ).throwIllegalArgument( "Type of Page must not be null" );
    this.id = pageId;
    this.pageType = pageType;
    this.title = "";
    this.backCaption = null;
    this.topLevel = false;
    this.style = new PageStyle[] {};
    this.actions = new ArrayList<ActionConfiguration>();
  }

  /**
   * <p>
   * Returns the id for the page.
   * </p>
   *
   * @since 1.4
   */
  public String getId() {
    return id;
  }

  /**
   * <p>
   * Modifies the page to be a top level page or not. A top level page marks the starting point of a flow.
   * </p>
   */
  public PageConfiguration setTopLevel( boolean topLevel ) {
    this.topLevel = topLevel;
    return this;
  }

  /**
   * <p>
   * Returns if the page is a top level page or not.
   * </p>
   *
   * @since 1.4
   */
  public boolean isTopLevel() {
    return topLevel;
  }

  /**
   * <p>
   * Defines the title of the page.
   * </p>
   *
   * @param title the title of the page. Must not be empty or <code>null</code>.
   */
  public PageConfiguration setTitle( String title ) {
    whenNull( title ).throwIllegalArgument( "Page title must not be null" );
    this.title = title;
    return this;
  }

  /**
   * <p>
   * Returns the page title.
   * </p>
   *
   * @since 1.4
   */
  public String getTitle() {
    return title;
  }

  /**
   * <p>
   * Defines the text for the back button.
   * </p>
   *
   * @param backCaption the text for the back button. If {@code null} or empty, the default text is used.
   * @since 1.4.5
   */
  public PageConfiguration setBackCaption( String backCaption ) {
    this.backCaption = backCaption;
    return this;
  }

/**
 * <p>
 * Returns the text for the back button. Default value: {@code null}.
 * </p>
 *
 * @since 1.4.5
 */
  public String getBackCaption() {
    return backCaption;
  }

  /**
   * <p>
   * Defines the style of a page. A page can have multiple styles.
   * </p>
   */
  public PageConfiguration setStyle( PageStyle... style ) {
    this.style = style;
    return this;
  }

  /**
   * <p>
   * Returns the page style as list.
   * </p>
   *
   * @since 1.4
   */
  public List<PageStyle> getStyle() {
    return Arrays.asList( style );
  }

  /**
   * <p>
   * Defines the image of a page that is used in menus and so on.
   * </p>
   *
   * @param image the image of the page. Must not be <code>null</code>.
   *
   * @since 1.0
   */
  public PageConfiguration setImage( InputStream image ) {
    whenNull( image ).throwIllegalArgument( "Page image must not be null" );
    this.image = ImageUtil.getBytes( image );
    return this;
  }

  /**
   * <p>
   * Returns the page image bytes.
   * </p>
   *
   * @since 1.4
   */
  public byte[] getImage() {
    return image;
  }

  /**
   * <p>
   * Adds an action to the page. The action becomes visible when this page will be visible.
   * </p>
   *
   * @param configuration the configuration of the the action to add. Must not be <code>null</code>.
   *
   * @since 1.0
   */
  public PageConfiguration addActionConfiguration( ActionConfiguration configuration ) {
    whenNull( configuration ).throwIllegalArgument( "Action configuration must not be null" );
    actions.add( configuration );
    UpdateUtil.firePageUpdate( this );
    return this;
  }

  /**
   * <p>
   * Removes an {@link ActionConfiguration} for the specified id. Can be used to manipulate a page during runtime.
   * </p>
   *
   * @throws IllegalStateException when no {@link ActionConfiguration} exist for the specified id.
   * @throws IllegalArgumentException when the id is null or empty.
   *
   * @since 1.2
   */
  public PageConfiguration removeActionConfiguration( String actionConfigurationId ) {
    whenNull( actionConfigurationId ).throwIllegalArgument( "actionConfigurationId most not be null" );
    when( actionConfigurationId.isEmpty() ).throwIllegalArgument( "actionConfigurationId most not be empty" );
    ActionConfiguration configuration = getActionConfiguration( actionConfigurationId );
    whenNull( configuration ).throwIllegalState( "ActionConfiguration for id " + actionConfigurationId + " does not exist" );
    actions.remove( configuration );
    UpdateUtil.firePageUpdate( this );
    return this;
  }

  /**
   * <p>
   * Returns the {@link ActionConfiguration} for the specified id or <code>null</code> if non existent.
   * </p>
   *
   * @throws IllegalArgumentException when the id is null or empty.
   *
   * @since 1.4
   */
  public ActionConfiguration getActionConfiguration( String actionConfigurationId ) {
    whenNull( actionConfigurationId ).throwIllegalArgument( "actionConfigurationId most not be null" );
    when( actionConfigurationId.isEmpty() ).throwIllegalArgument( "actionConfigurationId most not be empty" );
    for( ActionConfiguration action : actions ) {
      if( action.getAdapter( ActionDescriptor.class ).getId().equals( actionConfigurationId ) ) {
        return action;
      }
    }
    return null;
  }

  /**
   * @since 1.0
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T> T getAdapter( Class<T> adapter ) {
    if( adapter == PageDescriptor.class ) {
      return ( T )createDescriptor();
    }
    return null;
  }

  private PageDescriptor createDescriptor() {
    PageDescriptor pageDescriptor = new PageDescriptor( id, pageType )
                                      .setTitle( title )
                                      .setBackCaption( backCaption )
                                      .setImage( image )
                                      .setTopLevel( topLevel )
                                      .setPageStyle( style );
    for( ActionConfiguration configuration : actions ) {
      pageDescriptor.addAction( configuration );
    }
    return pageDescriptor;
  }

}
