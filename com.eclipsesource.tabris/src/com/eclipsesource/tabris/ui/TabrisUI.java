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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Shell;

import com.eclipsesource.tabris.internal.ZIndexStackLayout;
import com.eclipsesource.tabris.internal.ui.Controller;
import com.eclipsesource.tabris.internal.ui.RemoteUI;
import com.eclipsesource.tabris.internal.ui.UIDescriptor;
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
 * @see TabrisUIEntryPoint
 *
 * @since 1.0
 */
@SuppressWarnings("restriction")
public class TabrisUI {

  private final UIConfiguration configuration;

  /**
   * @since 1.0
   */
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
    Controller controller = new Controller( shell, remoteUI, configuration.getAdapter( UIDescriptor.class ) );
    UIImpl ui = prepareUi( shell, remoteUI, configuration, controller );
    configure( configuration, ui );
    setUiColors( shell, remoteUI, configuration );
    prepareController( controller, ui );
  }

  private void prepareShell( Shell shell ) {
    shell.setMaximized( true );
  }

  private UIImpl prepareUi( Shell shell, RemoteUI remoteUI, UIConfiguration configuration, Controller controller ) {
    UIImpl ui = new UIImpl( shell.getDisplay(), controller, configuration );
    remoteUI.setUi( ui );
    remoteUI.setController( controller );
    return ui;
  }

  private void configure( UIConfiguration configuration, UIImpl ui ) {
    ui.markInitialized();
  }

  private void setUiColors( Shell shell, RemoteUI remoteUI, UIConfiguration configuration ) {
    RGB background = configuration.getBackground();
    if( background != null ) {
      remoteUI.setBackground( new Color( shell.getDisplay(), background ) );
    }
    RGB foreground = configuration.getForeground();
    if( foreground != null ) {
      remoteUI.setForeground( new Color( shell.getDisplay(), foreground ) );
    }
  }

  private void prepareController( Controller controller, UIImpl ui ) {
    controller.createGlobalActions( ui );
    controller.createRootPages( ui );
  }
}
