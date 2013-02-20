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
import java.util.List;

import org.eclipse.swt.graphics.Color;

import com.eclipsesource.tabris.ui.ActionConfiguration;
import com.eclipsesource.tabris.ui.PageConfiguration;
import com.eclipsesource.tabris.ui.TransitionListener;
import com.eclipsesource.tabris.ui.UI;


public class UIImpl implements UI {

  private final DescriptorHolder descriptorHolder;
  private final List<TransitionListener> transitionListeners;
  private final RemoteUI remoteUI;

  public UIImpl( RemoteUI remoteUI ) {
    checkArgumentNotNull( remoteUI, RemoteUI.class.getSimpleName() );
    this.remoteUI = remoteUI;
    this.descriptorHolder = new DescriptorHolder();
    this.transitionListeners = new ArrayList<TransitionListener>();
  }

  public DescriptorHolder getDescriptorHolder() {
    return descriptorHolder;
  }

  @Override
  public UI addPage( PageConfiguration configuration ) {
    checkArgumentNotNull( configuration, "Page Configuration" );
    PageDescriptor descriptor = ( ( InternalPageConfiguration )configuration ).createDescriptor();
    descriptorHolder.add( descriptor );
    return this;
  }

  @Override
  public UI addAction( ActionConfiguration configuration ) {
    checkArgumentNotNull( configuration, "Action Configuration" );
    ActionDescriptor descriptor = ( ( InternalActionConfiguration )configuration ).createDescriptor();
    descriptorHolder.add( descriptor );
    return this;
  }

  @Override
  public void setForeground( Color foreground ) {
    checkArgumentNotNull( foreground, "Foreground" );
    remoteUI.setForeground( foreground );
  }

  @Override
  public void setBackground( Color background ) {
    checkArgumentNotNull( background, "Background" );
    remoteUI.setBackground( background );
  }

  @Override
  public UI addTransitionListener( TransitionListener listener ) {
    checkArgumentNotNull( listener, TransitionListener.class.getSimpleName() );
    transitionListeners.add( listener );
    return this;
  }

  @Override
  public UI removeTransitionListener( TransitionListener listener ) {
    checkArgumentNotNull( listener, TransitionListener.class.getSimpleName() );
    transitionListeners.remove( listener );
    return this;
  }

  List<TransitionListener> getTransitionListeners() {
    return transitionListeners;
  }

}
