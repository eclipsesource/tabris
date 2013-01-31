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

import static com.eclipsesource.tabris.internal.Preconditions.checkArgumentNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.eclipsesource.tabris.Store;
import com.eclipsesource.tabris.internal.ZIndexStackLayout;
import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.TransitionListener;


public class Controller {

  private final DescriptorHolder content;
  private final Shell shell;
  private final List<RemoteAction> globalActions;
  private final Map<PageDescriptor, RemotePage> rootPages;
  private final RemoteUI remoteUI;
  private PageFlow currentFlow;

  public Controller( Shell shell, RemoteUI remoteUI, DescriptorHolder content ) {
    this.shell = shell;
    this.remoteUI = remoteUI;
    this.content = content;
    this.globalActions = new ArrayList<RemoteAction>();
    this.rootPages = new HashMap<PageDescriptor, RemotePage>();
  }

  public void createRootPages( UIContextImpl context ) {
    List<PageDescriptor> pages = content.getRootPages();
    verifyRootPages( pages );
    createRootRemotePages( context, pages );
    showRoot( context, pages.get( 0 ), new Store() );
  }

  private void createRootRemotePages( UIContextImpl context, List<PageDescriptor> pages ) {
    for( PageDescriptor descriptor : pages ) {
      RemotePage remotePage = new RemotePage( context, descriptor, remoteUI.getRemoteUIId(), new Store() );
      rootPages.put( descriptor, remotePage );
      remotePage.createControl( shell );
    }
  }

  public void createGlobalActions( UIContextImpl context ) {
    List<ActionDescriptor> actions = content.getGlobalActions();
    for( ActionDescriptor actionDescriptor : actions ) {
      globalActions.add( new RemoteAction( context, actionDescriptor, remoteUI.getRemoteUIId() ) );
    }
  }

  private void verifyRootPages( List<PageDescriptor> pages ) {
    if( pages.isEmpty() ) {
      throw new IllegalStateException( "No TopLevel Pages found." );
    }
  }

  void show( UIContextImpl context, PageDescriptor newPage, Store store ) {
    if( newPage.isTopLevel() ) {
      showRoot( context, newPage, store );
    } else {
      showPage( context, newPage, store );
    }
  }

  void showRoot( UIContextImpl context, PageDescriptor newPage, Store store ) {
    RemotePage oldRoot = null;
    RemotePage newRoot = rootPages.get( newPage );
    newRoot.getStore().addStore( store );
    if( currentFlow != null ) {
      oldRoot = cleanupOldRoot( context, newRoot );
    }
    initializeNewRoot( context, oldRoot, newRoot );
  }

  private RemotePage cleanupOldRoot( UIContextImpl context, RemotePage root ) {
    RemotePage oldRoot = currentFlow.getCurrentPage();
    fireTransitionBeforeEvent( context, oldRoot, root );
    oldRoot.destroyActions();
    oldRoot.getPage().deactivate( context );
    currentFlow.destroy();
    return oldRoot;
  }

  private void initializeNewRoot( UIContextImpl context, RemotePage oldRoot, RemotePage newRoot ) {
    currentFlow = new PageFlow( newRoot );
    remoteUI.activate( newRoot.getRemotePageId() );
    newRoot.createActions();
    newRoot.getPage().activate( context );
    makeControlVisible( currentFlow.getCurrentPage().getControl() );
    fireTransitionAfterEvent( context, oldRoot, newRoot );
  }

  RemotePage showPage( UIContextImpl context, PageDescriptor newPage, Store store ) {
    RemotePage oldRemotePage = cleanupOldPage( context );
    return initializeNewPage( context, newPage, oldRemotePage, store );
  }

  private RemotePage cleanupOldPage( UIContextImpl context ) {
    RemotePage oldPage = currentFlow.getCurrentPage();
    oldPage.destroyActions();
    oldPage.getPage().deactivate( context );
    return oldPage;
  }

  private RemotePage initializeNewPage( UIContextImpl context,
                                        PageDescriptor newPage,
                                        RemotePage oldRemotePage,
                                        Store store )
  {
    RemotePage newRemotePage = new RemotePage( context, newPage, remoteUI.getRemoteUIId(), store );
    fireTransitionBeforeEvent( context, oldRemotePage, newRemotePage );
    currentFlow.add( newRemotePage );
    newRemotePage.createControl( shell );
    newRemotePage.createActions();
    remoteUI.activate( newRemotePage.getRemotePageId() );
    newRemotePage.getPage().activate( context );
    makeControlVisible( newRemotePage.getControl() );
    fireTransitionAfterEvent( context, oldRemotePage, newRemotePage );
    return newRemotePage;
  }

  boolean showPreviousPage( UIContextImpl context ) {
    if( currentFlow != null && currentFlow.getPreviousPage() != null ) {
      restorePreviousPage( context, currentFlow.getPreviousPage() );
      return true;
    }
    return false;
  }

  private void restorePreviousPage( UIContextImpl context, RemotePage previousPage ) {
    RemotePage removedPage = cleanUpCurrentPage( context );
    fireTransitionBeforeEvent( context, removedPage, previousPage );
    initializePreviousPage( context, previousPage );
    fireTransitionAfterEvent( context, removedPage, previousPage );
  }

  private RemotePage cleanUpCurrentPage( UIContextImpl context ) {
    RemotePage removedPage = currentFlow.pop();
    removedPage.destroy();
    removedPage.destroyActions();
    removedPage.getPage().deactivate( context );
    return removedPage;
  }

  private void initializePreviousPage( UIContextImpl context, RemotePage previousPage ) {
    remoteUI.activate( previousPage.getRemotePageId() );
    previousPage.createActions();
    previousPage.getPage().activate( context );
    makeControlVisible( previousPage.getControl() );
  }

  private void makeControlVisible( Control control ) {
    ZIndexStackLayout stack = ( ZIndexStackLayout )shell.getLayout();
    stack.setOnTopControl( control );
    shell.layout( true );
  }

  public void setCurrentTitle( String title ) {
    if( currentFlow != null ) {
      currentFlow.getCurrentPage().setTitle( title );
    }
  }

  public Page getCurrentPage() {
    if( currentFlow != null ) {
      return currentFlow.getCurrentPage().getPage();
    }
    return null;
  }

  public Store getCurrentStore() {
    if( currentFlow != null ) {
      return currentFlow.getCurrentPage().getStore();
    }
    return null;
  }

  public String getPageId( String remotePageId ) {
    List<RemotePage> pages = getAllPages();
    for( RemotePage remotePage : pages ) {
      if( remotePage.getRemotePageId().equals( remotePageId ) ) {
        return remotePage.getDescriptor().getId();
      }
    }
    throw new IllegalStateException( "RemotePage with id " + remotePageId + " does not exist." );
  }

  private List<RemotePage> getAllPages() {
    List<RemotePage> pages = new ArrayList<RemotePage>( rootPages.values() );
    if( currentFlow != null ) {
      pages.addAll( currentFlow.getAllPages() );
    }
    return pages;
  }

  public void setActionEnabled( String id, boolean enabled ) {
    RemoteAction action = findRemoteAction( id );
    action.setEnabled( enabled );
  }

  public void setActionVisible( String id, boolean visible ) {
    RemoteAction action = findRemoteAction( id );
    action.setVisible( visible );
  }

  private RemoteAction findRemoteAction( String id ) {
    RemoteAction result = findActionInGlobalActions( id );
    if( result == null ) {
      result = findActionInPageActions( id );
    }
    checkArgumentNotNull( result, "Action with id " + id + " does not exist." );
    return result;
  }

  private RemoteAction findActionInGlobalActions( String id ) {
    for( RemoteAction action : globalActions ) {
      if( action.getDescriptor().getId().equals( id ) ) {
        return action;
      }
    }
    return null;
  }

  private RemoteAction findActionInPageActions( String id ) {
    if( currentFlow != null ) {
      return findActionInCurrentFlow( id );
    }
    return null;
  }

  private RemoteAction findActionInCurrentFlow( String id ) {
    List<RemotePage> allPages = currentFlow.getAllPages();
    for( RemotePage page : allPages ) {
      List<RemoteAction> actions = page.getActions();
      for( RemoteAction remoteAction : actions ) {
        if( remoteAction.getDescriptor().getId().equals( id ) ) {
          return remoteAction;
        }
      }
    }
    return null;
  }

  void fireTransitionBeforeEvent( UIContextImpl context, RemotePage from, RemotePage to ) {
    List<TransitionListener> listeners = context.getUI().getTransitionListeners();
    for( TransitionListener listener : listeners ) {
      Page oldPage = from != null ? from.getPage() : null;
      Page newPage = to != null ? to.getPage() : null;
      listener.before( context, oldPage, newPage );
    }
  }

  void fireTransitionAfterEvent( UIContextImpl context, RemotePage from, RemotePage to ) {
    List<TransitionListener> listeners = context.getUI().getTransitionListeners();
    for( TransitionListener listener : listeners ) {
      Page oldPage = from != null ? from.getPage() : null;
      Page newPage = to != null ? to.getPage() : null;
      listener.after( context, oldPage, newPage );
    }
  }

  Map<PageDescriptor, RemotePage> getRootPages() {
    return rootPages;
  }

}
