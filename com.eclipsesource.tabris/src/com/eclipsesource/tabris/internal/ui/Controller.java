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
import static com.eclipsesource.tabris.internal.ui.RemoteActionFactory.createRemoteAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.eclipsesource.tabris.internal.ZIndexStackLayout;
import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.PageData;
import com.eclipsesource.tabris.ui.TransitionListener;


public class Controller implements Serializable {

  private final UIDescriptor uiDescriptor;
  private final Shell shell;
  private final List<RemoteAction> globalActions;
  private final Map<PageDescriptor, RemotePage> rootPages;
  private final RemoteUI remoteUI;
  private PageFlow currentFlow;

  public Controller( Shell shell, RemoteUI remoteUI, UIDescriptor uiDescriptor ) {
    this.shell = shell;
    this.remoteUI = remoteUI;
    this.uiDescriptor = uiDescriptor;
    this.globalActions = new ArrayList<RemoteAction>();
    this.rootPages = new HashMap<PageDescriptor, RemotePage>();
  }

  public void createRootPages( UIImpl ui ) {
    List<PageDescriptor> pages = uiDescriptor.getRootPages();
    when( pages.isEmpty() ).throwIllegalState( "No TopLevel Pages found." );
    createRootRemotePages( ui, pages );
    showRoot( ui, pages.get( 0 ), new PageData() );
  }

  private void createRootRemotePages( UIImpl ui, List<PageDescriptor> pages ) {
    for( PageDescriptor descriptor : pages ) {
      RemotePage remotePage = new RemotePage( ui, descriptor, remoteUI.getRemoteUIId(), new PageData() );
      rootPages.put( descriptor, remotePage );
      remotePage.createControl( shell );
    }
  }

  public void createGlobalActions( UIImpl ui ) {
    List<ActionDescriptor> actions = uiDescriptor.getGlobalActions();
    for( ActionDescriptor actionDescriptor : actions ) {
      globalActions.add( createRemoteAction( ui, actionDescriptor, remoteUI.getRemoteUIId() ) );
    }
  }

  void show( UIImpl ui, PageDescriptor newPage, PageData data ) {
    if( newPage.isTopLevel() ) {
      showRoot( ui, newPage, data );
    } else {
      showPage( ui, newPage, data );
    }
  }

  void showRoot( UIImpl ui, PageDescriptor newPage, PageData data ) {
    RemotePage oldRoot = null;
    RemotePage newRoot = rootPages.get( newPage );
    newRoot.getData().addData( data );
    if( currentFlow != null ) {
      oldRoot = cleanupOldRoot( ui, newRoot );
    }
    initializeNewRoot( ui, oldRoot, newRoot );
  }

  private RemotePage cleanupOldRoot( UIImpl ui, RemotePage root ) {
    RemotePage oldRoot = currentFlow.getCurrentPage();
    fireTransitionBeforeEvent( ui, oldRoot, root );
    oldRoot.destroyActions();
    oldRoot.getPage().deactivate();
    currentFlow.destroy();
    return oldRoot;
  }

  private void initializeNewRoot( UIImpl ui, RemotePage oldRoot, RemotePage newRoot ) {
    currentFlow = new PageFlow( newRoot );
    remoteUI.activate( newRoot.getRemotePageId() );
    newRoot.createActions();
    newRoot.getPage().activate();
    makeControlVisible( currentFlow.getCurrentPage().getControl() );
    fireTransitionAfterEvent( ui, oldRoot, newRoot );
  }

  RemotePage showPage( UIImpl ui, PageDescriptor newPage, PageData data ) {
    RemotePage oldRemotePage = cleanupOldPage( ui );
    return initializeNewPage( ui, newPage, oldRemotePage, data );
  }

  private RemotePage cleanupOldPage( UIImpl ui ) {
    RemotePage oldPage = currentFlow.getCurrentPage();
    oldPage.destroyActions();
    oldPage.getPage().deactivate();
    return oldPage;
  }

  private RemotePage initializeNewPage( UIImpl ui,
                                        PageDescriptor newPage,
                                        RemotePage oldRemotePage,
                                        PageData data )
  {
    RemotePage newRemotePage = new RemotePage( ui, newPage, remoteUI.getRemoteUIId(), data );
    fireTransitionBeforeEvent( ui, oldRemotePage, newRemotePage );
    currentFlow.add( newRemotePage );
    newRemotePage.createActions();
    newRemotePage.createControl( shell );
    remoteUI.activate( newRemotePage.getRemotePageId() );
    newRemotePage.getPage().activate();
    makeControlVisible( newRemotePage.getControl() );
    fireTransitionAfterEvent( ui, oldRemotePage, newRemotePage );
    return newRemotePage;
  }

  boolean closeCurrentPage( UIImpl ui ) {
    if( currentFlow != null && currentFlow.getPreviousPage() != null ) {
      restorePreviousPage( ui, currentFlow.getPreviousPage() );
      return true;
    }
    return false;
  }

  private void restorePreviousPage( UIImpl ui, RemotePage previousPage ) {
    RemotePage removedPage = cleanUpCurrentPage( ui );
    fireTransitionBeforeEvent( ui, removedPage, previousPage );
    initializePreviousPage( ui, previousPage );
    fireTransitionAfterEvent( ui, removedPage, previousPage );
  }

  private RemotePage cleanUpCurrentPage( UIImpl ui ) {
    RemotePage removedPage = currentFlow.pop();
    removedPage.destroy();
    removedPage.destroyActions();
    removedPage.getPage().deactivate();
    return removedPage;
  }

  private void initializePreviousPage( UIImpl ui, RemotePage previousPage ) {
    remoteUI.activate( previousPage.getRemotePageId() );
    previousPage.createActions();
    previousPage.getPage().activate();
    makeControlVisible( previousPage.getControl() );
  }

  private void makeControlVisible( Control control ) {
    ZIndexStackLayout stack = ( ZIndexStackLayout )shell.getLayout();
    stack.setOnTopControl( control );
    shell.layout( true );
  }

  public void setTitle( Page page, String title ) {
    if( currentFlow != null ) {
      RemotePage remotePageToModify = null;
      List<RemotePage> allPages = currentFlow.getAllPages();
      for( RemotePage remotePage : allPages ) {
        if( remotePage.getPage().equals( page ) ) {
          remotePageToModify = remotePage;
          break;
        }
      }
      setPageTitle( remotePageToModify, title );
    }
  }

  private void setPageTitle( RemotePage remotePage, String title ) {
    if( remotePage != null ) {
      remotePage.setTitle( title );
    } else {
      throw new IllegalStateException( "Page does not exist." );
    }
  }

  public Page getCurrentPage() {
    if( currentFlow != null ) {
      return currentFlow.getCurrentPage().getPage();
    }
    return null;
  }

  public PageData getCurrentData() {
    if( currentFlow != null ) {
      return currentFlow.getCurrentPage().getData();
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

  RemoteAction findRemoteAction( String id ) {
    RemoteAction result = findActionInGlobalActions( id );
    if( result == null ) {
      result = findActionInPageActions( id );
    }
    whenNull( result ).throwIllegalState( "Action with id " + id + " does not exist." );
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

  void fireTransitionBeforeEvent( UIImpl ui, RemotePage from, RemotePage to ) {
    UIDescriptor uiDescriptor = ui.getConfiguration().getAdapter( UIDescriptor.class );
    List<TransitionListener> listeners = new ArrayList<TransitionListener>( uiDescriptor.getTransitionListeners() );
    for( TransitionListener listener : listeners ) {
      Page oldPage = from != null ? from.getPage() : null;
      Page newPage = to != null ? to.getPage() : null;
      listener.before( ui, oldPage, newPage );
    }
  }

  void fireTransitionAfterEvent( UIImpl ui, RemotePage from, RemotePage to ) {
    UIDescriptor uiDescriptor = ui.getConfiguration().getAdapter( UIDescriptor.class );
    List<TransitionListener> listeners = new ArrayList<TransitionListener>( uiDescriptor.getTransitionListeners() );
    for( TransitionListener listener : listeners ) {
      Page oldPage = from != null ? from.getPage() : null;
      Page newPage = to != null ? to.getPage() : null;
      listener.after( ui, oldPage, newPage );
    }
  }

  Map<PageDescriptor, RemotePage> getRootPages() {
    return rootPages;
  }

}
