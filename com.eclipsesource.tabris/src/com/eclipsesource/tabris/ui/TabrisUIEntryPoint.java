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

import static org.eclipse.rap.rwt.internal.service.ContextProvider.getApplicationContext;

import java.io.Serializable;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.rap.rwt.application.EntryPointFactory;
import org.eclipse.rap.rwt.internal.lifecycle.RWTLifeCycle;
import org.eclipse.rap.rwt.widgets.DialogUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.eclipsesource.tabris.TabrisClient;


/**
 * <p>
 * The {@link TabrisUIEntryPoint} class should be used as the EntryPoint for TabrisUI based applications. This is an
 * implementation of the RWT interface {@link EntryPoint} which should be registered within an
 * {@link ApplicationConfiguration}. It has <strong>no</strong> no-argument constructor.
 * This means it can't be registered as a normal{@link EntryPoint}. Instead it should be registered with a
 * {@link EntryPointFactory}. You don't need to implement this factory yourself, just
 * use {@link TabrisUIEntrypointFactory}.
 * </p>
 * <p>
 * The {@link TabrisUIEntryPoint} acts as a convenience class to avoid the need to implement an {@link EntryPoint}.
 * Anyway, if you have the need to create your own display e.g. for using it together with JFace DataBinding, you need
 * to create the {@link TabrisUI} by your own.
 * </p>
 *
 * @see TabrisUI
 * @see TabrisUIEntrypointFactory
 *
 * @since 1.0
 */
@SuppressWarnings("restriction")
public class TabrisUIEntryPoint implements EntryPoint, Serializable {

  private final TabrisUI tabrisUI;

  /**
   * <p>
   * Creates an instance of a TabrisUI {@link EntryPoint}. Should be called within a {@link EntryPointFactory} like
   * {@link TabrisUIEntrypointFactory}.
   * <p>
   *
   * @param configuration the configuration of the UI. Must not be <code>null</code>.
   *
   */
  public TabrisUIEntryPoint( UIConfiguration configuration ) throws IllegalArgumentException {
    this( new TabrisUI( configuration ) );
  }

  TabrisUIEntryPoint( TabrisUI tabrisUI ) {
    this.tabrisUI = tabrisUI;
  }

  @Override
  public int createUI() {
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

  private Shell createShell( Display display ) {
    Shell shell = new Shell( display, SWT.NO_TRIM );
    shell.setMaximized( true );
    return shell;
  }

  /**
   * <p>
   * Will be called when the client accesses the application. The default implementation creates the Tabris UI when
   * the application is accessed from a Tabris Client. When a Browser accesses the application a Dialog will pop up
   * saying that this is an application for mobile devices only. Subclasses may override this method to change the
   * behavior for browsers.<br/>
   * <b>Please note:</b> When overriding this method it's necessary to call
   *                     {@link TabrisUIEntryPoint#createTabrisUI(Shell)}.
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

  protected void createTabrisUI( Shell shell ) {
    tabrisUI.create( shell );
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
