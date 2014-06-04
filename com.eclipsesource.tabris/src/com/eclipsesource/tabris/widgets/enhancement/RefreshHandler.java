/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets.enhancement;

import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.EVENT_REFRESH;
import static com.eclipsesource.tabris.internal.Constants.METHOD_DONE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_MESSAGE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_WIDGET;
import static com.eclipsesource.tabris.internal.Constants.TYPE_REFRESH_HANDLER;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.lifecycle.ProcessActionRunner;
import org.eclipse.rap.rwt.internal.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;

import com.eclipsesource.tabris.widgets.RefreshListener;


/**
 * <p>
 * A {@link RefreshHandler} can be added to a {@link Tree} using a {@link TreeDecorator}. You can use the handler to
 * define the refresh behavior by e.g. setting a message or calling the refresh done. Clients will use native refresh
 * facilities e.g. pull down.
 * </p>
 *
 * @see TreeDecorator
 *
 * @since 1.4
 */
@SuppressWarnings("restriction")
public class RefreshHandler {

  private final List<RefreshListener> listeners;
  private final RemoteObject remoteObject;
  private String message;

  public RefreshHandler() {
    listeners = new ArrayList<RefreshListener>();
    remoteObject = RWT.getUISession().getConnection().createRemoteObject( TYPE_REFRESH_HANDLER );
    remoteObject.setHandler( new RefreshHandlerOperator() );
  }

  /**
   * <p>
   * Sets the message the client should show during a refresh.
   * </p>
   *
   * @param message the message to show. Must not be <code>null</code>.
   */
  public void setMessage( String message ) {
    whenNull( message ).throwIllegalArgument( "Message must not be null" );
    remoteObject.set( PROPERTY_MESSAGE, JsonValue.valueOf( message ) );
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  /**
   * <p>
   * Adds a {@link RefreshListener} that will be notified when the user has triggered a refresh. After the refresh
   * is done you need to call {@link RefreshHandler#done()} to hide the refresh UI on the client.
   * </p>
   *
   * @param listener the listener to add. Must not be <code>null</code>.
   */
  public void addRefreshListener( RefreshListener listener ) {
    whenNull( listener ).throwIllegalArgument( "RefreshListener must not be null" );
    listeners.add( listener );
    if( listeners.size() == 1 ) {
      remoteObject.listen( EVENT_REFRESH, true );
    }
  }

  /**
   * <p>
   * Removes a {@link RefreshListener}.
   * </p>
   *
   * @param listener the listener to remove. Must not be <code>null</code>.
   */
  public void removeRefreshListener( RefreshListener listener ) {
    whenNull( listener ).throwIllegalArgument( "RefreshListener must not be null" );
    listeners.remove( listener );
    if( listeners.isEmpty() ) {
      remoteObject.listen( EVENT_REFRESH, false );
    }
  }

  /**
   * <p>
   * Returns the added {@link RefreshListener}s
   * </p>
   */
  public List<RefreshListener> getRefreshListeners() {
    return new ArrayList<RefreshListener>( listeners );
  }

  /**
   * <p>
   * Instructs a client to hide it's refresh UI. Usually this method will be called within a
   * {@link RefreshListener#refresh()} method after the refreshing is done.
   * </p>
   */
  public void done() {
    remoteObject.call( METHOD_DONE, null );
  }

  private class RefreshHandlerOperator extends AbstractOperationHandler {

    @Override
    public void handleNotify( String event, JsonObject properties ) {
      if( EVENT_REFRESH.equals( event ) ) {
        notifyRefreshListeners();
      }
    }
  }

  private void notifyRefreshListeners() {
    ProcessActionRunner.add( new Runnable() {

      @Override
      public void run() {
        List<RefreshListener> refreshListeners = getRefreshListeners();
        for( RefreshListener refreshListener : refreshListeners ) {
          refreshListener.refresh();
        }
      }
    } );
  }

  void hookToWidget( Widget widget ) {
    remoteObject.set( PROPERTY_WIDGET, WidgetUtil.getId( widget ) );
  }

  String getId() {
    return remoteObject.getId();
  }
}
