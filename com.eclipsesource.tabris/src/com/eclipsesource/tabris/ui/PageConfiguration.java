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

import static com.eclipsesource.tabris.internal.Preconditions.checkArgumentNotNull;
import static com.eclipsesource.tabris.internal.Preconditions.checkArgumentNotNullAndNotEmpty;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.rwt.Adaptable;

import com.eclipsesource.tabris.internal.ui.PageDescriptor;


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
public class PageConfiguration implements Adaptable {

  protected final String id;
  protected final Class<? extends Page> pageType;
  protected String title;
  protected PageStyle[] style;
  protected boolean topLevel;
  protected List<ActionConfiguration> actions;
  private InputStream image;

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
    checkArgumentNotNullAndNotEmpty( pageId, "Page Id" );
    checkArgumentNotNull( pageType, "Type of Page" );
    this.id = pageId;
    this.pageType = pageType;
    this.title = "";
    this.topLevel = false;
    this.style = new PageStyle[] {};
    this.actions = new ArrayList<ActionConfiguration>();
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
   * Defines the title of the page.
   * </p>
   *
   * @param title the title of the page. Must not be empty or <code>null</code>.
   */
  public PageConfiguration setTitle( String title ) {
    checkArgumentNotNull( title, "Page Title" );
    this.title = title;
    return this;
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
   * Defines the image of a page that is used in menus and so on.
   * </p>
   *
   * @param image the image of the page. Must not be <code>null</code>.
   *
   * @since 1.0
   */
  public PageConfiguration setImage( InputStream image ) {
    checkArgumentNotNull( image, "Page Image" );
    this.image = image;
    return this;
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
    checkArgumentNotNull( configuration, ActionConfiguration.class.getSimpleName() );
    actions.add( configuration );
    return this;
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
    PageDescriptor pageDescriptor = new PageDescriptor( id, pageType, title, image, topLevel, style );
    for( ActionConfiguration configuration : actions ) {
      pageDescriptor.addAction( configuration );
    }
    return pageDescriptor;
  }

}
