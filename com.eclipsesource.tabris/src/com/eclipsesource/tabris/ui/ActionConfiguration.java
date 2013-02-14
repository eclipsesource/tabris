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
import org.eclipse.swt.widgets.Display;

import com.eclipsesource.tabris.internal.ui.InternalActionConfiguration;


/**
 * <p>
 * {@link Action} objects will be instantiated by the framework. Therefore you need to be able to configure such a
 * {@link Action} before it's actually created in a convenient way. This is the single purpose of a
 * {@link ActionConfiguration}. It provides methods to define the visual role in a UI e.g. it's image or it's text.
 * </p>
 * <p>
 * {@link ActionConfiguration} objects will be usually created within the
 * {@link UIConfiguration#configure(UI, UIContext)} method and added to the {@link UI}.
 * </p>
 *
 * @see Action
 * @see UIConfiguration
 * @see UI
 *
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

  /**
   * <p>
   * Creates a new {@link ActionConfiguration} object with the defined id and a {@link Action} type. The id needs to be
   * unique within the Tabris UI.
   * </p>
   *
   * @param actionId the unique id of the {@link Action}. Must not be empty or <code>null</code>.
   * @param actionType the type of the {@link Action} to be created. Must not be <code>null</code>.
   *
   * @see PageManager#showPage(String)
   */
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

  /**
   * <p>
   * Defines the title of the action.
   * </p>
   *
   * @param title the title of the action. Must not be empty or <code>null</code>.
   */
  public ActionConfiguration setTitle( String title ) {
    checkArgumentNotNull( title, "Action Title" );
    this.title = title;
    return this;
  }

  /**
   * <p>
   * Defines the initial visibility of the action. To modify this state at runtime use {@link ActionManager}.
   * </p>
   */
  public ActionConfiguration setVisible( boolean visible ) {
    this.visible = visible;
    return this;
  }

  /**
   * <p>
   * Defines the initial enabled state of the action. To modify this state at runtime use {@link ActionManager}.
   * </p>
   */
  public ActionConfiguration setEnabled( boolean enabled ) {
    this.enabled = enabled;
    return this;
  }

  /**
   * <p>
   * Defines the image of an action. You will need to have a {@link Display} to create one. Take a look
   * at {@link UIContext#getDisplay()}.
   * </p>
   *
   * @param image the image of the action. Must not be <code>null</code>.
   */
  public ActionConfiguration setImage( Image image ) {
    checkArgumentNotNull( image, "Action Image" );
    this.image = image;
    return this;
  }

  /**
   * <p>
   * Sets the {@link Prominence} of an action. One action can have multiple prominences.
   * </p>
   */
  public ActionConfiguration setProminence( Prominence... prominence ) {
    this.prominence = prominence;
    return this;
  }
}
