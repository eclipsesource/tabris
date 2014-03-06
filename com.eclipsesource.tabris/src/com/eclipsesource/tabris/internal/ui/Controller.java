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

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.eclipsesource.tabris.internal.ZIndexStackLayout;
import com.eclipsesource.tabris.internal.ui.rendering.ActionRenderer;
import com.eclipsesource.tabris.internal.ui.rendering.PageRenderer;
import com.eclipsesource.tabris.internal.ui.rendering.RendererFactory;
import com.eclipsesource.tabris.internal.ui.rendering.UIRenderer;
import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.PageConfiguration;
import com.eclipsesource.tabris.ui.PageData;
import com.eclipsesource.tabris.ui.TransitionListener;
import com.eclipsesource.tabris.ui.UIConfiguration;


public class Controller implements UIUpdater, Serializable {

  private final UIDescriptor uiDescriptor;
  private final Composite pageParent;
  private final Composite actionsParent;
  private final UIRenderer uiRenderer;
  private final List<ActionRenderer> globalActionRenderers;
  private final Map<PageDescriptor, PageRenderer> topLevelPageRenderers;
  private PageFlow currentFlow;
  private UIImpl ui;

  public Controller( UIRenderer uiRenderer, UIDescriptor uiDescriptor ) {
    this.uiRenderer = uiRenderer;
    this.pageParent = uiRenderer.getPageParent();
    this.actionsParent = uiRenderer.getActionsParent();
    this.uiDescriptor = uiDescriptor;
    this.globalActionRenderers = new ArrayList<ActionRenderer>();
    this.topLevelPageRenderers = new HashMap<PageDescriptor, PageRenderer>();
    UpdateUtil.registerUpdater( this );
  }

  public void setUI( UIImpl ui ) {
    this.ui = ui;
  }

  @Override
  public void update( UIConfiguration uiConfiguration ) {
    UIDescriptor descriptor = uiConfiguration.getAdapter( UIDescriptor.class );
    updateGlobalActions( descriptor );
    updateRootPages( descriptor );
    actionsParent.getShell().layout( true, true );
  }

  private void updateGlobalActions( UIDescriptor descriptor ) {
    List<ActionDescriptor> globalActions = descriptor.getGlobalActions();
    for( ActionDescriptor actionDescriptor : globalActions ) {
      if( !actionExist( actionDescriptor ) ) {
        createGlobalAction( ui, actionDescriptor );
      }
    }
    removeDeletedGlobalActions( descriptor );
  }

  private void removeDeletedGlobalActions( UIDescriptor descriptor ) {
    for( ActionRenderer renderer : new ArrayList<ActionRenderer>( globalActionRenderers ) ) {
      if( !actionStillExist( renderer, descriptor ) ) {
        renderer.destroy();
        globalActionRenderers.remove( renderer );
      }
    }
  }

  private boolean actionStillExist( ActionRenderer renderer, UIDescriptor descriptor ) {
    List<ActionDescriptor> globalActions = descriptor.getGlobalActions();
    for( ActionDescriptor actionDescriptor : globalActions ) {
      if( actionDescriptor.getId().equals( renderer.getDescriptor().getId() ) ) {
        return true;
      }
    }
    return false;
  }

  private void updateRootPages( UIDescriptor descriptor ) {
    List<PageDescriptor> rootPages = descriptor.getRootPages();
    createTopLevelPageRenderer( ui, rootPages );
  }

  private boolean actionExist( ActionDescriptor actionDescriptor ) {
    for( ActionRenderer renderer : globalActionRenderers ) {
      if( renderer.getDescriptor().getId().equals( actionDescriptor.getId() ) ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void update( PageConfiguration pageConfiguration ) {
    PageDescriptor descriptor = pageConfiguration.getAdapter( PageDescriptor.class );
    List<PageRenderer> allPages = getAllPages();
    for( PageRenderer renderer : allPages ) {
      if( renderer.getDescriptor().getId().equals( descriptor.getId() ) ) {
        renderer.update( descriptor, uiDescriptor.getRendererFactory(), actionsParent );
      }
    }
    actionsParent.getShell().layout( true, true );
  }

  @Override
  public void remove( PageConfiguration pageConfiguration ) {
    PageDescriptor descriptor = pageConfiguration.getAdapter( PageDescriptor.class );
    verifyPageIsNoInCurrentFlow( descriptor );
    destroyPage( descriptor );
  }

  private void verifyPageIsNoInCurrentFlow( PageDescriptor descriptor ) {
    List<PageRenderer> allRenderers = currentFlow.getAllRenderers();
    for( PageRenderer pageRenderer : allRenderers ) {
      if( pageRenderer.getDescriptor().getId().equals( descriptor.getId() ) ) {
        throw new IllegalStateException( "Can not remove page from current chain" );
      }
    }
  }

  private void destroyPage( PageDescriptor descriptor ) {
    for( Entry<PageDescriptor, PageRenderer> entry : topLevelPageRenderers.entrySet() ) {
      if( entry.getKey().getId().equals( descriptor.getId() ) ) {
        entry.getValue().destroy();
      }
    }
  }

  public void createRootPages( UIImpl ui ) {
    List<PageDescriptor> pageDescriptors = uiDescriptor.getRootPages();
    when( pageDescriptors.isEmpty() ).throwIllegalState( "No TopLevel Pages found." );
    createTopLevelPageRenderer( ui, pageDescriptors );
    showRoot( ui, pageDescriptors.get( 0 ), new PageData() );
  }

  private void createTopLevelPageRenderer( UIImpl ui, List<PageDescriptor> pages ) {
    for( PageDescriptor descriptor : pages ) {
      if( !exist( descriptor ) ) {
        RendererFactory rendererFactory = uiDescriptor.getRendererFactory();
        PageRenderer renderer = rendererFactory.createPageRenderer( ui, uiRenderer, descriptor, new PageData() );
        topLevelPageRenderers.put( descriptor, renderer );
        renderer.createControl( pageParent );
      }
    }
  }

  private boolean exist( PageDescriptor descriptor ) {
    for( Entry<PageDescriptor, PageRenderer> entry : topLevelPageRenderers.entrySet() ) {
      if( entry.getKey().getId().equals( descriptor.getId() ) ) {
        return true;
      }
    }
    return false;
  }

  public void createGlobalActions( UIImpl ui ) {
    List<ActionDescriptor> actionDescriptors = uiDescriptor.getGlobalActions();
    for( ActionDescriptor actionDescriptor : actionDescriptors ) {
      createGlobalAction( ui, actionDescriptor );
    }
  }

  private void createGlobalAction( UIImpl ui, ActionDescriptor actionDescriptor ) {
    RendererFactory rendererFactory = uiDescriptor.getRendererFactory();
    ActionRenderer renderer = rendererFactory.createActionRenderer( ui, uiRenderer, actionDescriptor );
    renderer.createUi( actionsParent );
    globalActionRenderers.add( renderer );
  }

  void show( UIImpl ui, PageDescriptor newPageDescriptor, PageData data ) {
    if( newPageDescriptor.isTopLevel() ) {
      showRoot( ui, newPageDescriptor, data );
    } else {
      showPage( ui, newPageDescriptor, data );
    }
  }

  void showRoot( UIImpl ui, PageDescriptor newPageDescriptor, PageData data ) {
    PageRenderer oldRoot = null;
    PageRenderer newRoot = topLevelPageRenderers.get( newPageDescriptor );
    newRoot.getData().addData( data );
    if( currentFlow != null ) {
      oldRoot = cleanupOldRoot( ui, newRoot );
    }
    initializeNewRoot( ui, oldRoot, newRoot );
  }

  private PageRenderer cleanupOldRoot( UIImpl ui, PageRenderer root ) {
    PageRenderer oldRoot = currentFlow.getCurrentRenderer();
    fireTransitionBeforeEvent( ui, oldRoot, root );
    oldRoot.destroyActions();
    oldRoot.getPage().deactivate();
    currentFlow.destroy();
    return oldRoot;
  }

  private void initializeNewRoot( UIImpl ui, PageRenderer oldRoot, PageRenderer newRoot ) {
    currentFlow = new PageFlow( newRoot );
    newRoot.createActions( uiDescriptor.getRendererFactory(), actionsParent );
    uiRenderer.activate( newRoot );
    newRoot.getPage().activate();
    makeControlVisible( currentFlow.getCurrentRenderer().getControl() );
    fireTransitionAfterEvent( ui, oldRoot, newRoot );
  }

  PageRenderer showPage( UIImpl ui, PageDescriptor newPage, PageData data ) {
    PageRenderer oldPageRenderer = cleanupOldPageRenderer( ui );
    return initializeNewPage( ui, newPage, oldPageRenderer, data );
  }

  private PageRenderer cleanupOldPageRenderer( UIImpl ui ) {
    PageRenderer oldPageRenderer = currentFlow.getCurrentRenderer();
    oldPageRenderer.destroyActions();
    oldPageRenderer.getPage().deactivate();
    return oldPageRenderer;
  }

  private PageRenderer initializeNewPage( UIImpl ui,
                                          PageDescriptor newPage,
                                          PageRenderer oldPageRenderer,
                                          PageData data )
  {
    RendererFactory rendererFactory = uiDescriptor.getRendererFactory();
    PageRenderer newPageRenderer = rendererFactory.createPageRenderer( ui, uiRenderer, newPage, data  );
    fireTransitionBeforeEvent( ui, oldPageRenderer, newPageRenderer );
    currentFlow.add( newPageRenderer );
    newPageRenderer.createActions( uiDescriptor.getRendererFactory(), actionsParent );
    newPageRenderer.createControl( pageParent );
    uiRenderer.activate( newPageRenderer );
    newPageRenderer.getPage().activate();
    makeControlVisible( newPageRenderer.getControl() );
    fireTransitionAfterEvent( ui, oldPageRenderer, newPageRenderer );
    return newPageRenderer;
  }

  boolean closeCurrentPage( UIImpl ui ) {
    if( currentFlow != null && currentFlow.getPreviousRenderer() != null ) {
      restorePreviousPage( ui, currentFlow.getPreviousRenderer() );
      return true;
    }
    return false;
  }

  private void restorePreviousPage( UIImpl ui, PageRenderer previousPageRenderer ) {
    fireTransitionBeforeEvent( ui, currentFlow.getCurrentRenderer(), previousPageRenderer );
    PageRenderer removedPage = cleanUpCurrentPage( ui );
    initializePreviousPage( ui, previousPageRenderer );
    fireTransitionAfterEvent( ui, removedPage, previousPageRenderer );
  }

  private PageRenderer cleanUpCurrentPage( UIImpl ui ) {
    PageRenderer removedPage = currentFlow.getCurrentRenderer();
    removedPage.getPage().deactivate();
    removedPage.destroy();
    removedPage.destroyActions();
    currentFlow.pop();
    return removedPage;
  }

  private void initializePreviousPage( UIImpl ui, PageRenderer previousPage ) {
    previousPage.createActions( uiDescriptor.getRendererFactory(), actionsParent );
    uiRenderer.activate( previousPage );
    previousPage.getPage().activate();
    makeControlVisible( previousPage.getControl() );
  }

  private void makeControlVisible( Control control ) {
    ZIndexStackLayout stack = ( ZIndexStackLayout )pageParent.getLayout();
    stack.setOnTopControl( control );
    pageParent.layout( true );
  }

  public void setTitle( Page page, String title ) {
    if( currentFlow != null ) {
      PageRenderer rendererToModify = null;
      List<PageRenderer> allPageRenderes = currentFlow.getAllRenderers();
      for( PageRenderer renderer : allPageRenderes ) {
        if( renderer.getPage().equals( page ) ) {
          rendererToModify = renderer;
          break;
        }
      }
      setPageTitle( rendererToModify, title );
    }
  }

  private void setPageTitle( PageRenderer remotePage, String title ) {
    if( remotePage != null ) {
      remotePage.setTitle( title );
    } else {
      throw new IllegalStateException( "Page does not exist." );
    }
  }

  public Page getCurrentPage() {
    if( currentFlow != null ) {
      return currentFlow.getCurrentRenderer().getPage();
    }
    return null;
  }

  public String getCurrentPageId() {
    if( currentFlow != null ) {
      return currentFlow.getCurrentRenderer().getDescriptor().getId();
    }
    return null;
  }

  public PageData getCurrentData() {
    if( currentFlow != null ) {
      return currentFlow.getCurrentRenderer().getData();
    }
    return null;
  }

  List<PageRenderer> getAllPages() {
    List<PageRenderer> pages = new ArrayList<PageRenderer>( topLevelPageRenderers.values() );
    if( currentFlow != null ) {
      pages.addAll( currentFlow.getAllRenderers() );
    }
    return pages;
  }

  public void setActionEnabled( String id, boolean enabled ) {
    ActionRenderer action = findRemoteAction( id );
    action.setEnabled( enabled );
  }

  public void setActionVisible( String id, boolean visible ) {
    ActionRenderer action = findRemoteAction( id );
    action.setVisible( visible );
  }

  ActionRenderer findRemoteAction( String id ) {
    ActionRenderer result = findActionInGlobalActions( id );
    if( result == null ) {
      result = findActionInPageActions( id );
    }
    whenNull( result ).throwIllegalState( "Action with id " + id + " does not exist." );
    return result;
  }

  boolean hasAction( String id ) {
    ActionRenderer result = findActionInGlobalActions( id );
    if( result == null ) {
      result = findActionInPageActions( id );
    }
    return result != null;
  }

  private ActionRenderer findActionInGlobalActions( String id ) {
    for( ActionRenderer action : globalActionRenderers ) {
      if( action.getDescriptor().getId().equals( id ) ) {
        return action;
      }
    }
    return null;
  }

  private ActionRenderer findActionInPageActions( String id ) {
    if( currentFlow != null ) {
      return findActionInCurrentFlow( id );
    }
    return null;
  }

  private ActionRenderer findActionInCurrentFlow( String id ) {
    List<PageRenderer> allPageRenderers = currentFlow.getAllRenderers();
    for( PageRenderer pageRenderer : allPageRenderers ) {
      List<ActionRenderer> actionRenderers = pageRenderer.getActionRenderers();
      for( ActionRenderer actionRenderer : actionRenderers ) {
        if( actionRenderer.getDescriptor().getId().equals( id ) ) {
          return actionRenderer;
        }
      }
    }
    return null;
  }

  void fireTransitionBeforeEvent( UIImpl ui, PageRenderer from, PageRenderer to ) {
    UIDescriptor uiDescriptor = ui.getConfiguration().getAdapter( UIDescriptor.class );
    List<TransitionListener> listeners = new ArrayList<TransitionListener>( uiDescriptor.getTransitionListeners() );
    for( TransitionListener listener : listeners ) {
      Page oldPage = from != null ? from.getPage() : null;
      Page newPage = to != null ? to.getPage() : null;
      listener.before( ui, oldPage, newPage );
    }
  }

  void fireTransitionAfterEvent( UIImpl ui, PageRenderer from, PageRenderer to ) {
    UIDescriptor uiDescriptor = ui.getConfiguration().getAdapter( UIDescriptor.class );
    List<TransitionListener> listeners = new ArrayList<TransitionListener>( uiDescriptor.getTransitionListeners() );
    for( TransitionListener listener : listeners ) {
      Page oldPage = from != null ? from.getPage() : null;
      Page newPage = to != null ? to.getPage() : null;
      listener.after( ui, oldPage, newPage );
    }
  }

  Map<PageDescriptor, PageRenderer> getRootPages() {
    return topLevelPageRenderers;
  }

}
