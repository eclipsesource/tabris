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
import static org.eclipse.rap.rwt.internal.service.ContextProvider.getContext;

import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.rap.rwt.internal.theme.JsonValue;
import org.eclipse.swt.widgets.Shell;

import com.eclipsesource.tabris.internal.ZIndexStackLayout;
import com.eclipsesource.tabris.internal.ui.Controller;
import com.eclipsesource.tabris.internal.ui.RemoteUI;
import com.eclipsesource.tabris.internal.ui.UIContextImpl;
import com.eclipsesource.tabris.internal.ui.UIImpl;


/**
 * <p>
 * The {@link TabrisUI} forms the core of the Tabris UI framework. It acts as the starting point for a UI.
 * The whole UI creation will be triggered from within the {@link TabrisUI#create(Shell)} method.
 * </p>
 * <p>
 * Usually you don't need to create with this class directly. Consider using the {@link TabrisUIEntryPoint} instead.
 * </p>
 *
 * @see UIConfiguration
 * @see TabrisUIEntryPoint
 *
 * @since 1.0
 */
@SuppressWarnings("restriction")
public class TabrisUI {

  private final UIConfiguration configuration;

  public TabrisUI( UIConfiguration configuration ) {
    checkArgumentNotNull( configuration, UIConfiguration.class.getSimpleName() );
    getContext().getProtocolWriter().appendHead( "tabris.UI", JsonValue.valueOf( true ) );
    this.configuration = configuration;
  }

  /**
   * <p>
   * Will create the Tabris UI. Needs to be called within the creation of the UI e.g. in an
   * {@link EntryPoint#createUI()} method.
   * </p>
   *
   * @since 1.0
   */
  public void create( Shell shell ) {
    checkArgumentNotNull( shell, Shell.class.getSimpleName() );
    prepareShell( shell );
    RemoteUI remoteUI = new RemoteUI( shell );
    shell.setLayout( new ZIndexStackLayout() );
    UIImpl ui = new UIImpl( remoteUI );
    Controller controller = new Controller( shell, remoteUI, ui.getDescriptorHolder() );
    UIContextImpl context = prepareContext( shell, remoteUI, ui, controller );
    configure( ui, context );
    prepareController( controller, context );
  }

  private void prepareShell( Shell shell ) {
    shell.setMaximized( true );
  }

  private UIContextImpl prepareContext( Shell shell, RemoteUI remoteUI, UIImpl ui, Controller controller ) {
    UIContextImpl context = new UIContextImpl( shell.getDisplay(), controller, ui );
    remoteUI.setContext( context );
    remoteUI.setController( controller );
    return context;
  }

  private void configure( UIImpl ui, UIContextImpl context ) {
    configuration.configure( ui, context );
    context.markInitialized();
  }

  private void prepareController( Controller controller, UIContextImpl context ) {
    controller.createGlobalActions( context );
    controller.createRootPages( context );
  }
}
