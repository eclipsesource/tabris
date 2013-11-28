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
package com.eclipsesource.tabris.internal.ui.web;

import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.CUSTOM_VARIANT_TABRIS_UI;
import static com.eclipsesource.tabris.internal.ui.ImageUtil.getImage;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.eclipsesource.tabris.internal.ui.ActionDescriptor;
import com.eclipsesource.tabris.internal.ui.rendering.ActionRenderer;
import com.eclipsesource.tabris.ui.UI;


public class WebAction implements ActionRenderer {

  private final UI ui;
  private final WebUI uiRenderer;
  private final ActionDescriptor descriptor;
  private Button control;

  public WebAction( UI ui, WebUI uiRenderer, ActionDescriptor descriptor ) {
    this.ui = ui;
    this.uiRenderer = uiRenderer;
    this.descriptor = descriptor;
  }

  @Override
  public void createUi( Composite uiParent ) {
    if( control == null ) {
      control = new Button( uiParent, SWT.PUSH );
      control.setData( RWT.CUSTOM_VARIANT, CUSTOM_VARIANT_TABRIS_UI );
      control.setEnabled( descriptor.isEnabled() );
      control.setVisible( descriptor.isVisible() );
      control.setToolTipText( descriptor.getTitle() );
      control.setImage( getImage( control.getDisplay(), descriptor.getImage() ) );
      control.addListener( SWT.Selection, new ActionSelectionListener() );
    }
  }

  @Override
  public void setEnabled( boolean enabled ) {
    whenNull( control ).throwIllegalState( "UI is not created" );
    control.setEnabled( enabled );
  }

  @Override
  public void setVisible( boolean visible ) {
    whenNull( control ).throwIllegalState( "UI is not created" );
    control.setLayoutData( createRowData( SWT.DEFAULT, SWT.DEFAULT, visible ) );
    control.setVisible( visible );
    layoutWebUI();
  }

  @Override
  public void destroy() {
    if( control != null ) {
      control.dispose();
      control = null;
    }
  }

  @Override
  public ActionDescriptor getDescriptor() {
    return descriptor;
  }

  @Override
  public UI getUI() {
    return ui;
  }

  Button getControl() {
    return control;
  }

  protected void layoutWebUI() {
    uiRenderer.layout();
  }

  protected void actionExecuted() {
    // might be implemented by subclasses
  }

  protected RowData createRowData( int width, int height, boolean visible ) {
    RowData data = new RowData( width, height );
    data.exclude = !visible;
    return data;
  }

  private final class ActionSelectionListener implements Listener {
    @Override
    public void handleEvent( Event event ) {
      descriptor.getAction().execute( ui );
      actionExecuted();
    }
  }

}
