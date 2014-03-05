/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.app;

import org.eclipse.rap.rwt.client.service.ClientService;


/**
 * <p>
 * The {@link App} client service represents the App that is calling the Tabris server. Sometimes its necessary to
 * configure the App or to get notified when something happens within the App e.g. the App was sent to the background.
 * The {@link App} provides facilities to accomplish these tasks.
 * </p>
 *
 * @noimplement This interface is not intended to be implemented by clients.
 *
 * @since 0.10
 */
public interface App extends ClientService {

  /**
   * <p>
   * Registers an event listener that will be notified when an event of the specified {@link EventType} was fired
   * within the App.
   * </p>
   *
   * @param type the {@link EventType} of interest
   * @param listener the listener that gets called when an event was fired within the App.
   *
   * @see AppListener
   */
  void addEventListener( EventType type, AppListener listener );

  /**
   * <p>
   * Removes an event listener.
   * </p>
   */
  void removeEventListener( EventType type, AppListener listener );

  /**
   * <p>
   * On some devices a back navigation is possible using some kind of back button. When adding a
   * {@link BackNavigationListener} the listener will be notified when the user presses the back button.
   * </p>
   *
   * @see BackNavigationListener
   */
  void addBackNavigationListener( BackNavigationListener listener );

  /**
   * <p>
   * Removes a {@link BackNavigationListener}.
   * </p>
   */
  void removeBackNavigationListener( BackNavigationListener listener );

  /**
   * <p>
   * With the {@link App#addEventListener(EventType, AppListener)} method you can register a listener for the
   * {@link EventType#INACTIVE}. Inactive means a period with no user interactions. The
   * {@link App#startInactivityTimer(int)} specifies the time that will be used to detect inactivity and starts the
   * inactivity countdown.
   * </p>
   *
   * @param inactivityTime the inactivity time in ms.
   *
   * @since 1.1
   */
  void startInactivityTimer( int inactivityTime );

  /**
   * <p>
   * Stops the inactivity countdown.
   * </p>
   *
   * @since 1.1
   */
  void stopInactivityTimer();

  /**
   * <p>
   * Instructs the client to enable or disable screen protection. Each client can interpret screen protection
   * differently e.g. showing a black screen.
   * </p>
   *
   * @since 1.1
   */
  void setScreenProtection( boolean protect );

  /**
   * <p>
   * Returns whether the screen is protected or not.
   * </p>
   *
   * @since 1.1
   */
  boolean hasScreenProtection();

  /**
   * <p>
   * Instructs the client to set the given number >= 0 as badge on the application icon.
   * Set to {see SWT.NONE} to hide.
   * Not all clients support badges on the application icon.
   * </p>
   *
   * @since 1.2
   */
  void setBadgeNumber( int badgeNumber );

  /**
   * <p>
   * Returns the number which should be set in the application icon badge.
   * </p>
   *
   * @since 1.2
   */
  int getBadgeNumber();

  /**
   * <p>
   * Returns the Tabris version used in the client application.
   * </p>
   *
   * @since 1.3
   */
  String getTabrisVersion();

  /**
   * <p>
   * Returns the ID of the client application.
   * </p>
   *
   * @since 1.3
   */
  String getId();

  /**
   * <p>
   * Returns the version of the client application.
   * </p>
   *
   * @since 1.3
   */
  String getVersion();

}