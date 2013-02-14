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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.eclipsesource.tabris.internal.ui.InternalPageConfiguration;


/**
 * <p>
 * {@link Page} objects will be instantiated by the framework. Therefore you need to be able to configure such a
 * {@link Page} before it's actually created in a convenient way. This is the single purpose of a
 * {@link PageConfiguration}. It provides methods to define the visual role in a UI e.g. it's image that's used in a
 * menu or if the page should be a top level page. When a {@link Page} contains actions the {@link PageConfiguration}
 * is also the right place to add them.
 * </p>
 * <p>
 * {@link PageConfiguration} objects will be usually created within the {@link UIConfiguration#configure(UI, UIContext)}
 * method and added to the {@link UI}.
 * </p>
 *
 * @see Page
 * @see UIConfiguration
 * @see UI
 *
 * @since 0.11
 */
public class PageConfiguration {

  protected final String id;
  protected final Class<? extends Page> pageType;
  protected String title;
  protected PageStyle[] style;
  protected Image image;
  protected boolean topLevel;
  protected List<ActionConfiguration> actions;

  /**
   * <p>
   * Creates a new {@link PageConfiguration} object with the defined id and a {@link Page} type. The id needs to be
   * unique within the Tabris UI because you will need to use them to browse through the several pages.
   * </p>
   *
   * @param pageId the unique id of the {@link Page}. Must not be empty or <code>null</code>.
   * @param pageType the type of the {@link Page} to be created. Must not be <code>null</code>.
   *
   * @see PageManager#showPage(String)
   */
  public static PageConfiguration newPage( String pageId, Class<? extends Page> pageType ) {
    checkArgumentNotNullAndNotEmpty( pageId, "Page Id" );
    checkArgumentNotNull( pageType, "Type of Page" );
    return new InternalPageConfiguration( pageId, pageType );
  }

  protected PageConfiguration( String id, Class<? extends Page> pageType ) {
    this.id = id;
    this.pageType = pageType;
    this.title = "";
    this.topLevel = false;
    this.style = new PageStyle[] {};
    this.image = null;
    this.actions = new ArrayList<ActionConfiguration>();
  }

  /**
   * <p>
   * Modifies the page to be a top level page or not.
   * </p>
   */
  public PageConfiguration setTopLevel( boolean topLevel ) {
    this.topLevel = topLevel;
    return this;
  }

  /**
   * <p>
   * Defines the title of the page. To modify this state at runtime use {@link PageManager}.
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
   * Defines the image of a page. You will need to have a {@link Display} to create one. Take a look
   * at {@link UIContext#getDisplay()}.
   * </p>
   *
   * @param image the image of the page. Must not be <code>null</code>.
   */
  public PageConfiguration setImage( Image image ) {
    checkArgumentNotNull( image, "Page Image" );
    this.image = image;
    return this;
  }

  /**
   * <p>
   * Adds an action to the page. The action will only be visible on this page.
   * </p>
   *
   * @param configuration the configuration of the the action to add. Must not be <code>null</code>.
   */
  public PageConfiguration addAction( ActionConfiguration configuration ) {
    checkArgumentNotNull( configuration, ActionConfiguration.class.getSimpleName() );
    actions.add( configuration );
    return this;
  }

}
