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
package com.eclipsesource.tabris.ui;

import static com.eclipsesource.tabris.internal.Preconditions.checkArgumentNotNull;
import static com.eclipsesource.tabris.internal.Preconditions.checkArgumentNotNullAndNotEmpty;

import org.eclipse.swt.graphics.Image;

import com.eclipsesource.tabris.internal.ui.InternalActionConfiguration;


/**
 * @since 0.11
 */
public class ActionConfiguration {

  protected final String actionId;
  protected final Class<? extends Action> actionType;
  protected String title;
  protected Image image;
  protected Prominence[] prominence;
  protected boolean enabled;
  protected boolean visible;

  public static ActionConfiguration newAction( String actionId, Class<? extends Action> actionType ) {
    checkArgumentNotNullAndNotEmpty( actionId, "Action Id" );
    checkArgumentNotNull( actionType, "Type of Action" );
    return new InternalActionConfiguration( actionId, actionType );
  }

  protected ActionConfiguration( String actionId, Class<? extends Action> actionType ) {
    this.actionId = actionId;
    this.actionType = actionType;
    this.title = "";
    this.image = null;
    this.prominence = new Prominence[] {};
    this.enabled = true;
    this.visible = true;
  }

  public ActionConfiguration setTitle( String title ) {
    checkArgumentNotNull( title, "Action Title" );
    this.title = title;
    return this;
  }

  public ActionConfiguration setVisible( boolean visible ) {
    this.visible = visible;
    return this;
  }

  public ActionConfiguration setEnabled( boolean enabled ) {
    this.enabled = enabled;
    return this;
  }

  public ActionConfiguration setImage( Image image ) {
    checkArgumentNotNull( image, "Action Image" );
    this.image = image;
    return this;
  }

  public ActionConfiguration setProminence( Prominence... prominence ) {
    this.prominence = prominence;
    return this;
  }
}
