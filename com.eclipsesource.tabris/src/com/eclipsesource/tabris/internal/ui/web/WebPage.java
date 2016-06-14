/*******************************************************************************
 * Copyright (c) 2013, 2016 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal.ui.web;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.JavaScriptExecutor;
import org.eclipse.rap.rwt.internal.util.Entities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.eclipsesource.tabris.internal.ui.ActionDescriptor;
import com.eclipsesource.tabris.internal.ui.InstanceCreator;
import com.eclipsesource.tabris.internal.ui.PageDescriptor;
import com.eclipsesource.tabris.internal.ui.rendering.ActionRenderer;
import com.eclipsesource.tabris.internal.ui.rendering.PageRenderer;
import com.eclipsesource.tabris.internal.ui.rendering.RendererFactory;
import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.PageData;
import com.eclipsesource.tabris.ui.UI;


@SuppressWarnings("restriction")
public class WebPage implements PageRenderer {

  private final UI ui;
  private final WebUI uiRenderer;
  private final PageData data;
  private final Page page;
  private final List<ActionRenderer> remoteActions;
  private PageDescriptor descriptor;
  private Control control;
  private String title;

  public WebPage( UI ui, WebUI uiRenderer, PageDescriptor descriptor, PageData data ) {
    this.ui = ui;
    this.descriptor = descriptor;
    this.uiRenderer = uiRenderer;
    this.data = data;
    this.page = InstanceCreator.createInstance( descriptor.getPageType() );
    this.remoteActions = new ArrayList<ActionRenderer>();
    this.uiRenderer.pageCreated( this );
    title = descriptor.getTitle();
  }

  @Override
  public void update( PageDescriptor descriptor, RendererFactory rendererFactory, Composite uiParent ) {
    this.descriptor = descriptor;
    createActions( rendererFactory, uiParent );
    removeOldActions();
  }

  private void removeOldActions() {
    for( ActionRenderer renderer : new ArrayList<ActionRenderer>( remoteActions ) ) {
      if( !existInPage( renderer ) ) {
        renderer.destroy();
        remoteActions.remove( renderer );
      }
    }
  }

  private boolean existInPage( ActionRenderer renderer ) {
    List<ActionDescriptor> actions = descriptor.getActions();
    for( ActionDescriptor actionDescriptor : actions ) {
      if( actionDescriptor.getId().equals( renderer.getDescriptor().getId() ) ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void createActions( RendererFactory rendererFactory, Composite uiParent ) {
    List<ActionDescriptor> actions = descriptor.getActions();
    for( ActionDescriptor actionDescriptor : actions ) {
      if( !actionExist( actionDescriptor ) ) {
        createAction( rendererFactory, uiParent, actionDescriptor );
      }
    }
  }

  private boolean actionExist( ActionDescriptor newAction ) {
    for( ActionRenderer renderer : remoteActions ) {
      if( renderer.getDescriptor().getId().equals( newAction.getId() ) ) {
        return true;
      }
    }
    return false;
  }

  private void createAction( RendererFactory rendererFactory,
                             Composite uiParent,
                             ActionDescriptor actionDescriptor )
  {
    ActionRenderer renderer = rendererFactory.createActionRenderer( ui, uiRenderer, actionDescriptor );
    remoteActions.add( renderer );
    renderer.createUi( uiParent );
  }

  @Override
  public void setTitle( String title ) {
    this.title = title;
    setBrowserTitle();
    uiRenderer.updatePageSwitcher( descriptor, title );
  }

  @Override
  public void createControl( Composite parent ) {
    if( control == null ) {
      Composite container = new Composite( parent, SWT.NONE );
      container.setLayout( new FillLayout() );
      page.createContent( container, ui );
      control = container;
    }
  }

  @Override
  public Control getControl() {
    return control;
  }

  @Override
  public List<ActionRenderer> getActionRenderers() {
    return remoteActions;
  }

  @Override
  public void destroyActions() {
    for( ActionRenderer action : remoteActions ) {
      action.destroy();
    }
    remoteActions.clear();
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
  public PageData getData() {
    return data;
  }

  @Override
  public void destroy() {
    page.destroy();
    if( control != null ) {
      control.dispose();
      control = null;
    }
    uiRenderer.pageDestroyed( this );
  }

  void pageActivated() {
    setBrowserTitle();
  }

  private void setBrowserTitle() {
    JavaScriptExecutor executor = RWT.getClient().getService( JavaScriptExecutor.class );
    executor.execute( "document.title = \"" + Entities.HTML40.escape( title ) + "\";" );
  }

}
