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
import org.eclipse.rap.rwt.application.EntryPoint;
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
 * @since 0.11
 */
@SuppressWarnings("restriction")
public class TabrisUI implements EntryPoint {

  private final UIConfiguration configuration;

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

  protected void createTabrisUI( Shell shell ) {
    RemoteUI remoteUI = new RemoteUI( shell );
    shell.setLayout( new ZIndexStackLayout() );
    UIImpl ui = new UIImpl();
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
