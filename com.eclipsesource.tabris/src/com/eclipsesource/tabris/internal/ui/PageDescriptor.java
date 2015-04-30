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
package com.eclipsesource.tabris.internal.ui;

import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.eclipsesource.tabris.ui.ActionConfiguration;
import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.PageStyle;


public class PageDescriptor implements Serializable {

  private final String id;
  private final Class<? extends Page> pageType;
  private final List<ActionDescriptor> actions;
  private boolean topLevel;
  private PageStyle[] style;
  private String title;
  private String backCaption;
  private byte[] image;

  public PageDescriptor( String id, Class<? extends Page> pageType ) {
    this.actions = new ArrayList<ActionDescriptor>();
    this.id = id;
    this.pageType = pageType;
  }

  public String getId() {
    return id;
  }

  public Class<? extends Page> getPageType() {
    return pageType;
  }

  public PageDescriptor addAction( ActionConfiguration configuration ) {
    whenNull( configuration ).throwIllegalArgument( "Action Configuration must not be null" );
    ActionDescriptor descriptor = configuration.getAdapter( ActionDescriptor.class );
    actions.add( descriptor );
    return this;
  }

  public List<ActionDescriptor> getActions() {
    return actions;
  }

  public PageDescriptor setTopLevel( boolean topLevel ) {
    this.topLevel = topLevel;
    return this;
  }

  public boolean isTopLevel() {
    return topLevel;
  }

  public PageDescriptor setTitle( String title ) {
    this.title = title;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public PageDescriptor setBackCaption( String backCaption ) {
    this.backCaption = backCaption;
    return this;
  }

  public String getBackCaption() {
    return backCaption;
  }

  public PageDescriptor setPageStyle( PageStyle... style ) {
    this.style = style;
    return this;
  }

  public PageStyle[] getPageStyle() {
    return style;
  }

  public PageDescriptor setImage( byte[] image ) {
    this.image = image;
    return this;
  }

  public byte[] getImage() {
    return image;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( actions == null ) ? 0 : actions.hashCode() );
    result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
    result = prime * result + ( ( image == null ) ? 0 : image.hashCode() );
    result = prime * result + ( topLevel ? 1231 : 1237 );
    result = prime * result + ( ( pageType == null ) ? 0 : pageType.hashCode() );
    result = prime * result + Arrays.hashCode( style );
    result = prime * result + ( ( title == null ) ? 0 : title.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj ) {
    if( this == obj )
      return true;
    if( obj == null )
      return false;
    if( getClass() != obj.getClass() )
      return false;
    PageDescriptor other = ( PageDescriptor )obj;
    if( actions == null ) {
      if( other.actions != null )
        return false;
    } else if( !actions.equals( other.actions ) )
      return false;
    if( id == null ) {
      if( other.id != null )
        return false;
    } else if( !id.equals( other.id ) )
      return false;
    if( image == null ) {
      if( other.image != null )
        return false;
    } else if( !image.equals( other.image ) )
      return false;
    if( topLevel != other.topLevel )
      return false;
    if( pageType == null ) {
      if( other.pageType != null )
        return false;
    } else if( !pageType.equals( other.pageType ) )
      return false;
    if( !Arrays.equals( style, other.style ) )
      return false;
    if( title == null ) {
      if( other.title != null )
        return false;
    } else if( !title.equals( other.title ) )
      return false;
    return true;
  }

}
