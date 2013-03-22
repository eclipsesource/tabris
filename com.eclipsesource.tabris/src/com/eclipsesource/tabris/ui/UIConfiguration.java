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

import org.eclipse.rap.rwt.Adaptable;
import org.eclipse.swt.graphics.RGB;

import com.eclipsesource.tabris.internal.ui.ActionDescriptor;
import com.eclipsesource.tabris.internal.ui.PageDescriptor;
import com.eclipsesource.tabris.internal.ui.UIDescriptor;


/**
 * <p>
 * For a Tabris UI one {@link UIConfiguration} object exists. Think about it as the glue between you pages and actions.
 * </p>
 *
 * @see PageConfiguration
 * @see Page
 * @see ActionConfiguration
 * @see Action
 * @see TransitionListener
 *
 * @since 1.0
 */
public class UIConfiguration implements Adaptable {

  private final UIDescriptor uiDescriptor;
  private RGB background;
  private RGB foreground;

  public UIConfiguration() {
    this.uiDescriptor = new UIDescriptor();
  }

  /**
   * <p>
   * Adds a page represented by a {@link PageConfiguration} object.
   * </p>
   *
   * @since 1.0
   */
  public UIConfiguration addPageConfiguration( PageConfiguration configuration ) {
    checkArgumentNotNull( configuration, "Page Configuration" );
    PageDescriptor descriptor = configuration.getAdapter( PageDescriptor.class );
    uiDescriptor.add( descriptor );
    return this;
  }

  /**
   * <p>
   * Adds an action represented by an {@link ActionConfiguration} object.
   * </p>
   *
   * @since 1.0
   */
  public UIConfiguration addActionConfiguration( ActionConfiguration configuration ) {
    checkArgumentNotNull( configuration, "Action Configuration" );
    ActionDescriptor descriptor = configuration.getAdapter( ActionDescriptor.class );
    uiDescriptor.add( descriptor );
    return this;
  }

  /**
   * <p>
   * Sets the foreground color for navigation controls and the area where those controls are located on.
   * </p>
   *
   * @since 1.0
   */
  public UIConfiguration setForeground( RGB foreground ) {
    checkArgumentNotNull( foreground, "Foreground" );
    this.foreground = foreground;
    return this;
  }

  /**
   * <p>
   * Sets the foreground color for navigation controls and the area where those controls are located on.
   * </p>
   *
   * @since 1.0
   */
  public UIConfiguration setForeground( int red, int green, int blue ) {
    return setForeground( new RGB( red, green, blue ) );
  }

  public RGB getForeground() {
    return foreground;
  }

  /**
   * <p>
   * Sets the background color for navigation controls and the area where those controls are located on.
   * </p>
   *
   * @since 1.0
   */
  public UIConfiguration setBackground( RGB background ) {
    checkArgumentNotNull( background, "Background" );
    this.background = background;
    return this;
  }

  /**
   * <p>
   * Sets the background color for navigation controls and the area where those controls are located on.
   * </p>
   *
   * @since 1.0
   */
  public UIConfiguration setBackground( int red, int green, int blue ) {
    return setBackground( new RGB( red, green, blue ) );
  }

  public RGB getBackground() {
    return background;
  }

  /**
   * <p>
   * Adds a {@link TransitionListener} that notifies you when a user browses from one page to another.
   * </p>
   *
   * @since 1.0
   */
  public UIConfiguration addTransitionListener( TransitionListener listener ) {
    checkArgumentNotNull( listener, TransitionListener.class.getSimpleName() );
    uiDescriptor.addTransitionListener( listener );
    return this;
  }

  /**
   * <p>
   * Removes a {@link TransitionListener}.
   * </p>
   *
   * @since 1.0
   */
  public UIConfiguration removeTransitionListener( TransitionListener listener ) {
    checkArgumentNotNull( listener, TransitionListener.class.getSimpleName() );
    uiDescriptor.removeTransitionListener( listener );
    return this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getAdapter( Class<T> adapter ) {
    if( adapter == UIDescriptor.class ) {
      return ( T )uiDescriptor;
    }
    return null;
  }

}
