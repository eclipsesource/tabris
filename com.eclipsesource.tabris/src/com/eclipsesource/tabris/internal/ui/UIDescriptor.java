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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.rwt.RWT;

import com.eclipsesource.tabris.internal.TabrisClient;
import com.eclipsesource.tabris.internal.ui.rendering.RendererFactory;
import com.eclipsesource.tabris.internal.ui.web.WebRendererFactory;
import com.eclipsesource.tabris.ui.TransitionListener;


public class UIDescriptor implements Serializable {

  private final List<PageDescriptor> pageDescriptors;
  private final List<ActionDescriptor> actionDescriptors;
  private final List<TransitionListener> transitionListeners;

  public UIDescriptor() {
    pageDescriptors = new ArrayList<PageDescriptor>();
    actionDescriptors = new ArrayList<ActionDescriptor>();
    transitionListeners = new ArrayList<TransitionListener>();
  }

  public void add( PageDescriptor descriptor ) {
    verifyPageDescriptorIsUnique( descriptor );
    pageDescriptors.add( descriptor );
  }

  private void verifyPageDescriptorIsUnique( PageDescriptor descriptor ) {
    for( PageDescriptor pageDescriptor : pageDescriptors ) {
      if( pageDescriptor.getId().equals( descriptor.getId() ) ) {
        throw new IllegalStateException( "Page with id " + descriptor.getId() + " allready exist." );
      }
    }
  }

  public PageDescriptor getPageDescriptor( String id ) {
    for( PageDescriptor pageDescriptor : pageDescriptors ) {
      if( pageDescriptor.getId().equals( id ) ) {
        return pageDescriptor;
      }
    }
    return null;
  }

  public void add( ActionDescriptor descriptor ) {
    verifyActionDescriptorIsUnique( descriptor );
    actionDescriptors.add( descriptor );
  }

  private void verifyActionDescriptorIsUnique( ActionDescriptor descriptor ) {
    for( ActionDescriptor actionDescriptor : actionDescriptors ) {
      if( actionDescriptor.getId().equals( descriptor.getId() ) ) {
        throw new IllegalStateException( "Action with id " + descriptor.getId() + " allready exist." );
      }
    }
  }

  public ActionDescriptor getActionDescriptor( String id ) {
    for( ActionDescriptor actionDescriptor : actionDescriptors ) {
      if( actionDescriptor.getId().equals( id ) ) {
        return actionDescriptor;
      }
    }
    return null;
  }

  public List<PageDescriptor> getRootPages() {
    List<PageDescriptor> result = new ArrayList<PageDescriptor>();
    for( PageDescriptor pageDescriptor : pageDescriptors ) {
      if( pageDescriptor.isTopLevel() ) {
        result.add( pageDescriptor );
      }
    }
    return result;
  }

  public List<ActionDescriptor> getGlobalActions() {
    return actionDescriptors;
  }

  public void addTransitionListener( TransitionListener listener ) {
    transitionListeners.add( listener );
  }

  public void removeTransitionListener( TransitionListener listener ) {
    transitionListeners.remove( listener );
  }

  public List<TransitionListener> getTransitionListeners() {
    return transitionListeners;
  }

  public RendererFactory getRendererFactory() {
    if( RWT.getClient() instanceof TabrisClient ) {
      return RemoteRendererFactory.getInstance();
    }
    return WebRendererFactory.getInstance();
  }
}
