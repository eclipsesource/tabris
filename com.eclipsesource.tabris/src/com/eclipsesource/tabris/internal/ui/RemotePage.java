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

import static com.eclipsesource.tabris.internal.Constants.PROPERTY_CONTROL;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_IMAGE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PARENT;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_STYLE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_TITLE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_TOP_LEVEL;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectImpl;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.graphics.ImageFactory;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.eclipsesource.tabris.Store;
import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.PageStyle;
import com.eclipsesource.tabris.ui.UIContext;


@SuppressWarnings("restriction")
public class RemotePage {

  private final PageDescriptor descriptor;
  private final RemoteObjectImpl remoteObject;
  private final List<RemoteAction> remoteActions;
  private final UIContext context;
  private final String parentId;
  private final Page page;
  private final Store store;
  private Control control;

  public RemotePage( UIContext context, PageDescriptor descriptor, String parentId, Store store ) {
    this.context = context;
    this.parentId = parentId;
    this.store = store;
    this.remoteObject = ( RemoteObjectImpl )RWT.getUISession().getConnection().createRemoteObject( "tabris.Page" );
    this.descriptor = descriptor;
    this.page = InstanceCreator.createInstance( descriptor.getPageType() );
    this.remoteActions = new ArrayList<RemoteAction>();
    setTitle( descriptor.getTitle() );
    setAttributes();
  }

  public String getRemotePageId() {
    return remoteObject.getId();
  }

  private void setAttributes() {
    remoteObject.set( PROPERTY_PARENT, parentId );
    remoteObject.set( PROPERTY_TOP_LEVEL, descriptor.isTopLevel() );
    if( descriptor.getPageStyle() != null && descriptor.getPageStyle().length > 0 ) {
      remoteObject.set( PROPERTY_STYLE, createPageStyleParameter( descriptor.getPageStyle() ) );
    }
    setImage();
  }

  private String[] createPageStyleParameter( PageStyle[] pageStyle ) {
    List<String> parameters = new ArrayList<String>();
    for( PageStyle style : pageStyle ) {
      parameters.add( style.toString() );
    }
    String[] result = new String[ parameters.size() ];
    parameters.toArray( result );
    return result;
  }

  private void setImage() {
    Image image = descriptor.getImage();
    if( image != null ) {
      Rectangle bounds = image.getBounds();
      Object[] imageData = new Object[] { ImageFactory.getImagePath( descriptor.getImage() ),
                                          Integer.valueOf( bounds.width ),
                                          Integer.valueOf( bounds.height ) };
      remoteObject.set( PROPERTY_IMAGE, imageData );
    }
  }

  public void createActions() {
    List<ActionDescriptor> actions = descriptor.getActions();
    for( ActionDescriptor actionDescriptor : actions ) {
      remoteActions.add( new RemoteAction( context, actionDescriptor, parentId ) );
    }
  }

  public void setTitle( String title ) {
    remoteObject.set( PROPERTY_TITLE, title );
  }

  public List<RemoteAction> getActions() {
    return remoteActions;
  }

  public PageDescriptor getDescriptor() {
    return descriptor;
  }

  public Page getPage() {
    return page;
  }

  public void createControl( Composite parent ) {
    if( control == null ) {
      Composite container = new Composite( parent, SWT.NONE );
      container.setLayout( new FillLayout() );
      page.create( container, context );
      control = container;
      remoteObject.set( PROPERTY_CONTROL, WidgetUtil.getId( control ) );
    }
  }

  public Control getControl() {
    return control;
  }

  public Store getStore() {
    return store;
  }

  public void destroy() {
    control.dispose();
    remoteObject.destroy();
  }

  public void destroyActions() {
    for( RemoteAction action : remoteActions ) {
      action.destroy();
    }
    remoteActions.clear();
  }
}
