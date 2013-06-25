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

import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import java.io.Serializable;

import org.eclipse.rap.rwt.Adaptable;
import org.eclipse.swt.graphics.RGB;

import com.eclipsesource.tabris.internal.ui.ActionDescriptor;
import com.eclipsesource.tabris.internal.ui.PageDescriptor;
import com.eclipsesource.tabris.internal.ui.UIDescriptor;


/**
 * <p>
 * A Tabris UI consists of {@link Page} and {@link Action} objects that are loosely coupled. The point where these are
 * hooked together is a {@link UIConfiguration}.
 * </p>
 * <p>
 * Two types of pages exist. These are top level pages and normal pages. A top level page marks the beginning of an
 * application flow. E.g. when you developing a book browsing app the top level page may be your shelf from where you
 * can browse the books. Of course in such an app you will need to browse from a book to another book without the way
 * back to the shelf. This means the book page needs to be a normal pages that can be chained together.</br>
 * This is what normal pages are, the second type of pages. They can appear everywhere in the application. The depth
 * level of such a page is not defined.
 * </p>
 * <p>
 * To add pages you need to add a {@link PageConfiguration} to a {@link UIConfiguration}. In the
 * {@link PageConfiguration} you can define the type of the page.
 * </p>
 * <p>
 * Actions also exist in two types. The first type are global actions. Global actions are visible in the whole
 * application regardless on which page you are currently on. The other type of actions are page actions. These
 * actions are having the same lifecycle as a Page and they are also only visible when the related page is visible.
 * </p>
 * <p>
 * You can add Actions similar as Pages. To add a global {@link Action} you need to add it directly to a
 * {@link UIConfiguration}. To add a page action you need to add it to a {@link PageConfiguration}.
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
public class UIConfiguration implements Adaptable, Serializable {

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
    whenNull( configuration ).thenIllegalArgument( "Page Configuration must not be null" );
    PageDescriptor descriptor = configuration.getAdapter( PageDescriptor.class );
    uiDescriptor.add( descriptor );
    return this;
  }

  /**
   * <p>
   * Adds a global action represented by an {@link ActionConfiguration} object.
   * </p>
   *
   * @since 1.0
   */
  public UIConfiguration addActionConfiguration( ActionConfiguration configuration ) {
    whenNull( configuration ).thenIllegalArgument( "Action Configuration must not be null" );
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
    whenNull( foreground ).thenIllegalArgument( "Foreground must not be null" );
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
    whenNull( background ).thenIllegalArgument( "Background must not be null" );
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

  /**
   * <p>
   * Returns the background color for navigation controls and the area where those controls are located on.
   * </p>
   *
   * @since 1.0
   */
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
    whenNull( listener ).thenIllegalArgument( "Listener must not be null" );
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
    whenNull( listener ).thenIllegalArgument( "Listener must not be null" );
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
