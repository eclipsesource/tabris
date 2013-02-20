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
import static org.eclipse.rap.rwt.internal.service.ContextProvider.getApplicationContext;
import static org.eclipse.rap.rwt.internal.service.ContextProvider.getContext;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.rap.rwt.application.EntryPointFactory;
import org.eclipse.rap.rwt.internal.lifecycle.RWTLifeCycle;
import org.eclipse.rap.rwt.internal.theme.JsonValue;
import org.eclipse.rap.rwt.widgets.DialogUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.eclipsesource.tabris.TabrisClient;
import com.eclipsesource.tabris.internal.ZIndexStackLayout;
import com.eclipsesource.tabris.internal.ui.Controller;
import com.eclipsesource.tabris.internal.ui.RemoteUI;
import com.eclipsesource.tabris.internal.ui.UIContextImpl;
import com.eclipsesource.tabris.internal.ui.UIImpl;


/**
 * <p>
 * The {@link TabrisUI} class should be used as the EntryPoint for TabrisUI based applications. As you may have seen
 * it's an implementation of the RWT interface {@link EntryPoint}. This means it should be registered within an
 * {@link ApplicationConfiguration}. It has no no-argument constructor. This means it can't be registered as a normal
 * {@link EntryPoint}. Instead it should be registered with a {@link EntryPointFactory}.
 * </p>
 * <p>
 * For more details on what's a Tabris UI please see {@link UIConfiguration}.
 * </p>
 *
 * @see ApplicationConfiguration
 * @see EntryPoint
 * @see EntryPointFactory
 * @see UIConfiguration
 *
 * @since 0.11
 */
@SuppressWarnings("restriction")
public class TabrisUI implements EntryPoint {

  private final UIConfiguration configuration;

  /**
   * <p>
   * Creates an instance of a TabrisUI {@link EntryPoint}. Should be called within a {@link EntryPointFactory}.
   * <p>
   *
   * @param configuration the configuration of the UI. Must not be <code>null</code>.
   *
   * @see UIConfiguration
   */
  public TabrisUI( UIConfiguration configuration ) throws IllegalArgumentException {
    checkArgumentNotNull( configuration, UIConfiguration.class.getSimpleName() );
    this.configuration = configuration;
  }

  @Override
  public int createUI() {
    getContext().getProtocolWriter().appendHead( "tabris.UI", JsonValue.valueOf( true ) );
    Display display = new Display();
    Shell shell = createShell( display );
    createContent( shell );
    shell.layout( true );
    shell.open();
    if( getApplicationContext().getLifeCycleFactory().getLifeCycle() instanceof RWTLifeCycle ) {
      readAndDispatch( display, shell );
    }
    return 0;
  }

  /**
   * <p>
   * Will be called when the client accesses the application. The default implementation creates the Tabrsi UI when
   * the application is accessed from a Tabris Client. When a Browser accesses the application a Dialog will pop up
   * saying that this is an application for mobile devices only. Subclasses may override this method to change the
   * behavior for browsers.<br/>
   * <b>Please note:</b> When overriding this method it's necessary to call {@link TabrisUI#createTabrisUI(Shell)}
   *                     within it for Tabris Clients.
   * </p>
   */
  protected void createContent( Shell shell ) {
    if( RWT.getClient() instanceof TabrisClient ) {
      createTabrisUI( shell );
    } else {
      MessageBox messageBox = new MessageBox( shell, SWT.ICON_WARNING );
      messageBox.setText( "Application not available in the Browser" );
      messageBox.setMessage( "This Application is made for mobile clients only" );
      DialogUtil.open( messageBox, null );
    }
  }

  /**
   * <p>
   * Will create the Tabris UI. Needs to be called from within {@link TabrisUI#createContent(Shell)}.
   * </p>
   */
  protected void createTabrisUI( Shell shell ) {
    RemoteUI remoteUI = new RemoteUI( shell );
    shell.setLayout( new ZIndexStackLayout() );
    UIImpl ui = new UIImpl( remoteUI );
    Controller controller = new Controller( shell, remoteUI, ui.getDescriptorHolder() );
    UIContextImpl context = new UIContextImpl( shell.getDisplay(), controller, ui );
    remoteUI.setContext( context );
    remoteUI.setController( controller );
    configuration.configure( ui, context );
    context.markInitialized();
    controller.createGlobalActions( context );
    controller.createRootPages( context );
  }

  private Shell createShell( Display display ) {
    Shell shell = new Shell( display, SWT.NO_TRIM );
    shell.setMaximized( true );
    return shell;
  }

  private void readAndDispatch( Display display, Shell shell ) {
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    display.dispose();
  }

}
