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
package com.eclipsesource.tabris.internal.ui;

import static com.eclipsesource.tabris.internal.Constants.EVENT_SELECTION;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ENABLED;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_IMAGE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PARENT;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PLACEMENT_PRIORITY;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_TITLE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_VISIBILITY;

import java.io.ByteArrayInputStream;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.graphics.ImageFactory;
import org.eclipse.swt.widgets.Composite;

import com.eclipsesource.tabris.internal.ui.rendering.ActionRenderer;
import com.eclipsesource.tabris.ui.Action;
import com.eclipsesource.tabris.ui.ActionListener;
import com.eclipsesource.tabris.ui.PlacementPriority;
import com.eclipsesource.tabris.ui.UI;


@SuppressWarnings("restriction")
public class RemoteAction extends AbstractOperationHandler implements ActionRenderer {

  private final RemoteObject remoteObject;
  private final ActionDescriptor descriptor;
  private final UI ui;

  public RemoteAction( UI ui, RemoteUI uiRenderer, ActionDescriptor descriptor ) {
    this.ui = ui;
    this.descriptor = descriptor;
    this.remoteObject = RWT.getUISession().getConnection().createRemoteObject( getType() );
    this.remoteObject.setHandler( this );
    setAttributes( uiRenderer.getRemoteUIId() );
  }

  protected String getType() {
    return "tabris.Action";
  }

  private void setAttributes( String parentId ) {
    remoteObject.set( PROPERTY_PARENT, parentId );
    remoteObject.set( PROPERTY_TITLE, descriptor.getTitle() );
    setImage();
    setDefaultEnabled();
    setDefaultVisible();
    setPlacementPriority();
  }

  private void setImage() {
    Image image = createImage( descriptor.getImage() );
    if( image != null ) {
      Rectangle bounds = image.getBounds();
      JsonArray imageData = new JsonArray();
      imageData.add( ImageFactory.getImagePath( image ) );
      imageData.add( bounds.width );
      imageData.add( bounds.height );
      remoteObject.set( PROPERTY_IMAGE, imageData );
    }
  }

  private Image createImage( byte[] bytes ) {
    if( bytes != null ) {
      return new Image( ui.getDisplay(), new ByteArrayInputStream( bytes ) );
    }
    return null;
  }

  private void setDefaultEnabled() {
    boolean enabled = descriptor.isEnabled();
    if( !enabled ) {
      setEnabled( enabled );
    }
  }

  private void setDefaultVisible() {
    boolean visible = descriptor.isVisible();
    if( !visible ) {
      setVisible( visible );
    }
  }

  private void setPlacementPriority() {
    PlacementPriority placementPriority = descriptor.getPlacementPriority();
    if( placementPriority != null ) {
      remoteObject.set( PROPERTY_PLACEMENT_PRIORITY, placementPriority.name() );
    }
  }

  @Override
  public void setEnabled( boolean enabled ) {
    remoteObject.set( PROPERTY_ENABLED, enabled );
  }

  @Override
  public void setVisible( boolean visible ) {
    remoteObject.set( PROPERTY_VISIBILITY, visible );
  }

  @Override
  public void handleNotify( String event, JsonObject properties ) {
    if( event.equals( EVENT_SELECTION ) ) {
      Action action = descriptor.getAction();
      action.execute( ui );
      notifyActionListeners( action );
    }
  }

  private void notifyActionListeners( Action action ) {
    UIDescriptor uiDescriptor = ui.getConfiguration().getAdapter( UIDescriptor.class );
    for( ActionListener listener : uiDescriptor.getActionListeners() ) {
      listener.executed( ui, action );
    }
  }

  @Override
  public ActionDescriptor getDescriptor() {
    return descriptor;
  }

  @Override
  public void destroy() {
    remoteObject.destroy();
  }

  public RemoteObject getRemoteObject() {
    return remoteObject;
  }

  @Override
  public UI getUI() {
    return ui;
  }

  @Override
  public void createUi( Composite uiParent ) {
    // remote actions do not need an UI
  }
}
