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

import com.eclipsesource.tabris.internal.ui.InternalPageConfiguration;


/**
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

  public PageConfiguration setTopLevel( boolean topLevel ) {
    this.topLevel = topLevel;
    return this;
  }

  public PageConfiguration setTitle( String title ) {
    checkArgumentNotNull( title, "Page Title" );
    this.title = title;
    return this;
  }

  public PageConfiguration setStyle( PageStyle... style ) {
    this.style = style;
    return this;
  }

  public PageConfiguration setImage( Image image ) {
    checkArgumentNotNull( image, "Page Image" );
    this.image = image;
    return this;
  }

  public PageConfiguration addAction( ActionConfiguration configuration ) {
    checkArgumentNotNull( configuration, ActionConfiguration.class.getSimpleName() );
    actions.add( configuration );
    return this;
  }

}
