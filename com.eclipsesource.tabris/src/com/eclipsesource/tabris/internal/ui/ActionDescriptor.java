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

import com.eclipsesource.tabris.ui.Action;
import com.eclipsesource.tabris.ui.PlacementPriority;


public class ActionDescriptor implements Serializable {

  private final String id;
  private final Action action;
  private String title;
  private boolean visible;
  private boolean enabled;
  private byte[] image;
  private PlacementPriority placementPriority;

  public ActionDescriptor( String id, Action action ) {
    this.id = id;
    this.action = action;
  }

  public String getId() {
    return id;
  }

  public Action getAction() {
    return action;
  }

  public String getTitle() {
    return title;
  }

  public ActionDescriptor setTitle( String title ) {
    this.title = title;
    return this;
  }

  public byte[] getImage() {
    return image;
  }

  public ActionDescriptor setImage( byte[] image ) {
    this.image = image;
    return this;
  }

  public boolean isVisible() {
    return visible;
  }

  public ActionDescriptor setVisible( boolean visible ) {
    this.visible = visible;
    return this;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public ActionDescriptor setEnabled( boolean enabled ) {
    this.enabled = enabled;
    return this;
  }

  public PlacementPriority getPlacementPriority() {
    return placementPriority;
  }

  public ActionDescriptor setPlacementPrority( PlacementPriority placemenPriority ) {
    this.placementPriority = placemenPriority;
    return this;
  }

}
