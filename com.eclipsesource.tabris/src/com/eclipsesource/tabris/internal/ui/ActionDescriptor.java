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
  private final String title;
  private final boolean visible;
  private final boolean enabled;
  private final byte[] image;
  private final PlacementPriority placementPriority;

  public ActionDescriptor( String id,
                           Action action,
                           String title,
                           byte[] image,
                           boolean visible,
                           boolean enabled,
                           PlacementPriority placementPriority )
  {
    this.id = id;
    this.action = action;
    this.title = title;
    this.image = image;
    this.enabled = enabled;
    this.visible = visible;
    this.placementPriority = placementPriority;
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

  public byte[] getImage() {
    return image;
  }

  public boolean isVisible() {
    return visible;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public PlacementPriority getPlacementPriority() {
    return placementPriority;
  }

}
