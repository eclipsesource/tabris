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
import static com.eclipsesource.tabris.internal.JsonUtil.createJsonArray;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.json.JsonArray;
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

import com.eclipsesource.tabris.internal.ui.rendering.ActionRenderer;
import com.eclipsesource.tabris.internal.ui.rendering.PageRenderer;
import com.eclipsesource.tabris.internal.ui.rendering.RendererFactory;
import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.PageData;
import com.eclipsesource.tabris.ui.PageStyle;
import com.eclipsesource.tabris.ui.UI;


@SuppressWarnings("restriction")
public class RemotePage implements Serializable, PageRenderer {

  private final PageDescriptor descriptor;
  private final RemoteObjectImpl remoteObject;
  private final List<ActionRenderer> remoteActions;
  private final UI ui;
  private final RemoteUI uiRenderer;
  private final PageData data;
  private final Page page;
  private Control control;

  public RemotePage( UI ui, RemoteUI uiRenderer, PageDescriptor descriptor, PageData data ) {
    this.ui = ui;
    this.uiRenderer = uiRenderer;
    this.data = data;
    this.remoteObject = ( RemoteObjectImpl )RWT.getUISession().getConnection().createRemoteObject( "tabris.Page" );
    this.descriptor = descriptor;
    this.page = InstanceCreator.createInstance( descriptor.getPageType() );
    this.remoteActions = new ArrayList<ActionRenderer>();
    setTitle( descriptor.getTitle() );
    setAttributes();
  }

  String getId() {
    return remoteObject.getId();
  }

  private void setAttributes() {
    remoteObject.set( PROPERTY_PARENT, uiRenderer.getRemoteUIId() );
    remoteObject.set( PROPERTY_TOP_LEVEL, descriptor.isTopLevel() );
    if( descriptor.getPageStyle() != null && descriptor.getPageStyle().length > 0 ) {
      remoteObject.set( PROPERTY_STYLE, createPageStyleParameter( descriptor.getPageStyle() ) );
    }
    setImage();
  }

  private JsonArray createPageStyleParameter( PageStyle[] pageStyle ) {
    List<String> parameters = new ArrayList<String>();
    for( PageStyle style : pageStyle ) {
      parameters.add( style.toString() );
    }
    String[] result = new String[ parameters.size() ];
    parameters.toArray( result );
    return createJsonArray( result );
  }

  private void setImage() {
    Image image = createImage( descriptor.getImage() );
    if( image != null ) {
      Rectangle bounds = image.getBounds();
      JsonArray imageData = new JsonArray();
      imageData.add( ImageFactory.getImagePath( image ) );
      imageData.add( bounds.width );
      imageData.add( bounds.height );
      remoteObject.set( PROPERTY_IMAGE, imageData );
    }
  }

  private Image createImage( byte[] bytes ) {
    if( bytes != null ) {
      return new Image( ui.getDisplay(), new ByteArrayInputStream( bytes ) );
    }
    return null;
  }

  @Override
  public void createActions( RendererFactory rendererFactory, Composite uiParent ) {
    List<ActionDescriptor> actions = descriptor.getActions();
    for( ActionDescriptor actionDescriptor : actions ) {
      ActionRenderer renderer = rendererFactory.createActionRenderer( ui, uiRenderer, actionDescriptor );
      remoteActions.add( renderer );
      renderer.createUi( uiParent );
    }
  }

  @Override
  public void setTitle( String title ) {
    remoteObject.set( PROPERTY_TITLE, title );
  }

  @Override
  public List<ActionRenderer> getActionRenderers() {
    return remoteActions;
  }

  @Override
  public PageDescriptor getDescriptor() {
    return descriptor;
  }

  @Override
  public Page getPage() {
    return page;
  }

  @Override
  public void createControl( Composite parent ) {
    if( control == null ) {
      Composite container = new Composite( parent, SWT.NONE );
      container.setLayout( new FillLayout() );
      page.createContent( container, ui );
      control = container;
      remoteObject.set( PROPERTY_CONTROL, WidgetUtil.getId( control ) );
    }
  }

  @Override
  public Control getControl() {
    return control;
  }

  @Override
  public PageData getData() {
    return data;
  }

  @Override
  public void destroy() {
    page.destroy();
    control.dispose();
    remoteObject.destroy();
  }

  @Override
  public void destroyActions() {
    for( ActionRenderer action : remoteActions ) {
      action.destroy();
    }
    remoteActions.clear();
  }
}
