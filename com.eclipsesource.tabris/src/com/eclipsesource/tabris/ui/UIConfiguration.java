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

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.rwt.Adaptable;
import org.eclipse.swt.graphics.RGB;

import com.eclipsesource.tabris.internal.ui.ActionDescriptor;
import com.eclipsesource.tabris.internal.ui.ImageUtil;
import com.eclipsesource.tabris.internal.ui.PageDescriptor;
import com.eclipsesource.tabris.internal.ui.UIDescriptor;
import com.eclipsesource.tabris.internal.ui.UpdateUtil;


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
  private final List<PageConfiguration> pageConfigurations;
  private final List<ActionConfiguration> actionConfigurations;
  private RGB background;
  private RGB foreground;
  private byte[] image;

  public UIConfiguration() {
    this.uiDescriptor = new UIDescriptor();
    this.pageConfigurations = new ArrayList<PageConfiguration>();
    this.actionConfigurations = new ArrayList<ActionConfiguration>();
  }

  /**
   * <p>
   * Adds a page represented by a {@link PageConfiguration} object.
   * </p>
   *
   * @since 1.0
   */
  public UIConfiguration addPageConfiguration( PageConfiguration configuration ) {
    whenNull( configuration ).throwIllegalArgument( "Page Configuration must not be null" );
    pageConfigurations.add( configuration );
    PageDescriptor descriptor = configuration.getAdapter( PageDescriptor.class );
    uiDescriptor.add( descriptor );
    UpdateUtil.fireUiUpdate( this );
    return this;
  }

  /**
   * <p>
   * Returns a {@link PageConfiguration} for the passed in id or <code>null</code> if no {@link PageConfiguration}
   * exist for this id. The {@link PageConfiguration} can be used to manipulate a page during runtime e.g.
   * adding {@link ActionConfiguration}s.
   * </p>
   *
   * @return The {@link PageConfiguration} for the specified id or <code>null</code> when the configuration does not
   * exist.
   *
   * @throws IllegalArgumentException if the pageId is <code>null</code> or empty.
   *
   * @since 1.2
   */
  public PageConfiguration getPageConfiguration( String pageId ) {
    whenNull( pageId ).throwIllegalArgument( "Page Id must not be null" );
    when( pageId.isEmpty() ).throwIllegalArgument( "Page Id must not be empty" );
    for( PageConfiguration configuration : pageConfigurations ) {
      if( configuration.getAdapter( PageDescriptor.class ).getId().equals( pageId ) ) {
        return configuration;
      }
    }
    return null;
  }

  /**
   * <p>
   * Removes a {@link PageConfiguration} for the specified id. Can be used to manipulate a ui during runtime.
   * </p>
   *
   * @throws IllegalStateException when the page to remove is active or in the current page chain.
   * @throws IllegalArgumentException when the id is <code>null</code> or empty.
   *
   * @since 1.2
   */
  public UIConfiguration removePageConfiguration( String pageId ) {
    whenNull( pageId ).throwIllegalArgument( "Page Id must not be null" );
    when( pageId.isEmpty() ).throwIllegalArgument( "Page Id must not be empty" );
    for( PageConfiguration configuration : new ArrayList<PageConfiguration>( pageConfigurations ) ) {
      if( configuration.getAdapter( PageDescriptor.class ).getId().equals( pageId ) ) {
        UpdateUtil.firePageRemove( configuration );
        pageConfigurations.remove( configuration );
        uiDescriptor.removePageDescriptor( pageId );
      }
    }
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
    whenNull( configuration ).throwIllegalArgument( "Action Configuration must not be null" );
    actionConfigurations.add( configuration );
    ActionDescriptor descriptor = configuration.getAdapter( ActionDescriptor.class );
    uiDescriptor.add( descriptor );
    UpdateUtil.fireUiUpdate( this );
    return this;
  }

  /**
   * <p>
   * Removes an {@link ActionConfiguration} for the specified id. Can be used to manipulate a page during runtime.
   * </p>
   *
   * @throws IllegalStateException when no {@link ActionConfiguration} exist for the specified id.
   * @throws IllegalArgumentException when the id is <code>null</code> or empty.
   *
   * @since 1.2
   */
  public UIConfiguration removeActionConfiguration( String actionConfigurationId ) {
    whenNull( actionConfigurationId ).throwIllegalArgument( "actionConfigurationId most not be null" );
    when( actionConfigurationId.isEmpty() ).throwIllegalArgument( "actionConfigurationId most not be empty" );
    ActionConfiguration configuration = getActionConfiguration( actionConfigurationId );
    whenNull( configuration ).throwIllegalState( "ActionConfiguration for id " + actionConfigurationId + " does not exist" );
    uiDescriptor.removeAction( actionConfigurationId );
    actionConfigurations.remove( configuration );
    UpdateUtil.fireUiUpdate( this );
    return this;
  }

  private ActionConfiguration getActionConfiguration( String actionConfigurationId ) {
    for( ActionConfiguration action : actionConfigurations ) {
      if( action.getAdapter( ActionDescriptor.class ).getId().equals( actionConfigurationId ) ) {
        return action;
      }
    }
    return null;
  }

  /**
   * <p>
   * Sets the foreground color for navigation controls and the area where those controls are located on.
   * </p>
   *
   * @since 1.0
   */
  public UIConfiguration setForeground( RGB foreground ) {
    whenNull( foreground ).throwIllegalArgument( "Foreground must not be null" );
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
    whenNull( background ).throwIllegalArgument( "Background must not be null" );
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
   * Places a UI icon to the platform typical place.
   * </p>
   *
   * @since 1.2
   */
  public UIConfiguration setImage( InputStream image ) {
    whenNull( image ).throwIllegalArgument( "Action Image must not be null" );
    this.image = ImageUtil.getBytes( image );
    return this;
  }

  /**
   * <p>
   * Returns the bytes of the UI icon or <code>null</code>.
   * </p>
   *
   * @since 1.2
   */
  public byte[] getImage() {
    return image;
  }

  /**
   * <p>
   * Adds a {@link TransitionListener} that notifies you when a user browses from one page to another.
   * </p>
   *
   * @since 1.0
   */
  public UIConfiguration addTransitionListener( TransitionListener listener ) {
    whenNull( listener ).throwIllegalArgument( "Listener must not be null" );
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
    whenNull( listener ).throwIllegalArgument( "Listener must not be null" );
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
