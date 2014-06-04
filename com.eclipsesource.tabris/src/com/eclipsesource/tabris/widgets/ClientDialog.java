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
package com.eclipsesource.tabris.widgets;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.EVENT_CLIENT_DIALOG_CLOSE;
import static com.eclipsesource.tabris.internal.Constants.EVENT_SELECTION;
import static com.eclipsesource.tabris.internal.Constants.METHOD_CLOSE;
import static com.eclipsesource.tabris.internal.Constants.METHOD_OPEN;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_BUTTON_CANCEL;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_BUTTON_NEUTRAL;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_BUTTON_OK;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_BUTTON_TYPE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_MESSAGE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_SEVERITY;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_TITLE;
import static com.eclipsesource.tabris.internal.Constants.TYPE_CLIENT_DIALOG;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.lifecycle.ProcessActionRunner;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.eclipsesource.tabris.internal.Constants;


/**
 * <p>
 * A {@link ClientDialog} is a client side dialog. A client uses native dialogs to visualize the {@link ClientDialog}.
 * For this reason dialogs may vary form client to client.
 * </p>
 *
 * @since 1.4
 */
@SuppressWarnings("restriction")
public class ClientDialog implements Serializable {

  /**
   * <p>
   * A client dialog can have three buttons. An OK, CANCEL and a neutral button.
   * </p>
   */
  public static enum ButtonType {

    /**
     * <p>
     * An OK button will be shown where native clients show ok or positive buttons.
     * </p>
     */
    OK,

    /**
     * <p>
     * A CANCEL button will be shown where native clients show cancel or negative buttons.
     * </p>
     */
    CANCEL,

    /**
     * <p>
     * A NEUTRAL button will be shown where native clients show additional or neutral buttons.
     * </p>
     */
    NEUTRAL
  }

  /**
   * <p>
   * A {@link ClientDialog} can have a severity. Some clients interpret the severity by showing a severity icon.
   * </p>
   */
  public static enum Severity {
    WARNING, ERROR
  }

  private final RemoteObject remoteObject;
  private final Map<ButtonType, Listener> buttons;
  private final List<ClientDialogListener> dialogListeners;
  private String title;
  private String message;
  private Severity severity;

  public ClientDialog() {
    this.remoteObject = RWT.getUISession().getConnection().createRemoteObject( TYPE_CLIENT_DIALOG );
    this.remoteObject.setHandler( new DialogOperationHandler() );
    this.buttons = new HashMap<ButtonType, Listener>();
    this.dialogListeners = new ArrayList<ClientDialogListener>();
  }

  /**
   * <p>
   * Sets the title of a {@link ClientDialog}. The title may be displayed more emphasized as normal text.
   * </p>
   *
   * @param title the title to show. Must not be <code>null</code>.
   */
  public void setTitle( String title ) {
    whenNull( title ).throwIllegalArgument( "title must not be null" );
    this.remoteObject.set( PROPERTY_TITLE, title );
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  /**
   * <p>
   * Sets the message of a {@link ClientDialog}. Usually the message will be displayed as a body of the native dialog.
   * </p>
   *
   * @param message the message to display. Must not be <code>null</code>.
   */
  public ClientDialog setMessage( String message ) {
    whenNull( message ).throwIllegalArgument( "message must not be null" );
    this.message = message;
    this.remoteObject.set( PROPERTY_MESSAGE, message );
    return this;
  }

  public String getMessage() {
    return message;
  }

  /**
   * <p>
   * Sets the {@link Severity} of a {@link ClientDialog}.
   * </p>
   *
   * @param severity the {@link Severity} to use. Must not be <code>null</code>.
   */
  public ClientDialog setSeverity( Severity severity ) {
    whenNull( severity ).throwIllegalArgument( "severity must not be null" );
    this.severity = severity;
    remoteObject.set( PROPERTY_SEVERITY, severity.toString() );
    return this;
  }

  public Severity getSeverity() {
    return severity;
  }

  /**
   * <p>
   * Sets a {@link ClientDialog} button with the specified {@link ButtonType}. The client is responsible to position
   * the button on its right place.
   * </p>
   *
   * @param type the {@link ButtonType} of the button. Must not be <code>null</code>.
   * @param text the text of the button. Must not be <code>null</code> or empty.
   */
  public ClientDialog setButton( ButtonType type, String text ) {
    return setButton( type, text, null );
  }

  /**
   * <p>
   * Sets a {@link ClientDialog} button with the specified {@link ButtonType}. The client is responsible to position
   * the button on its right place. When the button was pressed the {@link Listener} will be notified.
   * </p>
   *
   * @param type the {@link ButtonType} of the button. Must not be <code>null</code>.
   * @param text the text of the button. Must not be <code>null</code> or empty.
   * @param listener the listener to notify when the button was pressed. May be <code>null</code>.
   */
  public ClientDialog setButton( ButtonType type, String text, Listener listener ) {
    whenNull( type ).throwIllegalArgument( "type must not be null" );
    whenNull( text ).throwIllegalArgument( "text must not be null" );
    when( text.isEmpty() ).throwIllegalArgument( "text must not be empty" );
    remoteObject.set( createButtonKey( type ), text );
    addListener( type, listener );
    return this;
  }

  private String createButtonKey( ButtonType type ) {
    switch( type ) {
      case OK:
        return PROPERTY_BUTTON_OK;
      case CANCEL:
        return PROPERTY_BUTTON_CANCEL;
      case NEUTRAL:
        return PROPERTY_BUTTON_NEUTRAL;
      default:
        return null;
    }
  }

  private void addListener( ButtonType type, Listener listener ) {
    if( listener != null ) {
      if( buttons.isEmpty() ) {
        remoteObject.listen( EVENT_SELECTION, true );
      }
      buttons.put( type, listener );
    }
  }

  /**
   * <p>
   * Instructs a client to open the {@link ClientDialog}.
   * </p>
   */
  public void open() {
    remoteObject.call( METHOD_OPEN, null );
    notifyOpenListeners();
  }

  private void notifyOpenListeners() {
    final List<ClientDialogListener> listeners = new ArrayList<ClientDialogListener>( dialogListeners );
    ProcessActionRunner.add( new Runnable() {

      @Override
      public void run() {
        for( ClientDialogListener listener : listeners ) {
          listener.open();
        }
      }
    } );
  }

  /**
   * <p>
   * Instructs a client to close a {@link ClientDialog}.
   * </p>
   */
  public void close() {
    remoteObject.call( METHOD_CLOSE, null );
  }

  /**
   * <p>
   * Adds a {@link ClientDialogListener} that will be notified when a dialog was opened or closed.
   * </p>
   *
   * @param listener the listener to add. Must not be <code>null</code>.
   */
  public void addClientDialogListener( ClientDialogListener listener ) {
    whenNull( listener ).throwIllegalArgument( "listener must not be null" );
    if( dialogListeners.isEmpty() ) {
      remoteObject.listen( EVENT_CLIENT_DIALOG_CLOSE, true );
    }
    dialogListeners.add( listener );
  }

  /**
   * <p>
   * Removes a {@link ClientDialogListener}.
   * </p>
   *
   * @param listener the listener to add. Must not be <code>null</code>.
   */
  public void removeClientDialogListener( ClientDialogListener listener ) {
    whenNull( listener ).throwIllegalArgument( "listener must not be null" );
    dialogListeners.remove( listener );
    if( dialogListeners.isEmpty() ) {
      remoteObject.listen( EVENT_CLIENT_DIALOG_CLOSE, false );
    }
  }

  private class DialogOperationHandler extends AbstractOperationHandler {

    @Override
    public void handleNotify( String event, JsonObject properties ) {
      if( event.equals( Constants.EVENT_SELECTION ) ) {
        dispatchSelectionEvent( properties );
      } else if( event.equals( EVENT_CLIENT_DIALOG_CLOSE ) ) {
        dispatchCloseEvent();
      }
    }

  }

  private void dispatchSelectionEvent( JsonObject properties ) {
    String type = properties.get( PROPERTY_BUTTON_TYPE ).asString();
    Listener listener = buttons.get( getButtonType( type ) );
    if( listener != null ) {
      notifySelectionListener( listener );
    }
  }

  private ButtonType getButtonType( String type ) {
    if( type.equals( PROPERTY_BUTTON_OK ) ) {
      return ButtonType.OK;
    } else if( type.equals( PROPERTY_BUTTON_CANCEL ) ) {
      return ButtonType.CANCEL;
    } else if( type.equals( PROPERTY_BUTTON_NEUTRAL ) ) {
      return ButtonType.NEUTRAL;
    }
    return null;
  }

  private void notifySelectionListener( final Listener listener ) {
    final Display display = Display.getCurrent();
    ProcessActionRunner.add( new Runnable() {

      @Override
      public void run() {
        Event event = new Event();
        event.display = display;
        listener.handleEvent( event );
      }
    } );
  }

  private void dispatchCloseEvent() {
    final List<ClientDialogListener> listeners = new ArrayList<ClientDialogListener>( dialogListeners );
    ProcessActionRunner.add( new Runnable() {

      @Override
      public void run() {
        for( ClientDialogListener listener : listeners ) {
          listener.close();
        }
      }
    } );
  }

  RemoteObject getRemoteObject() {
    return remoteObject;
  }
}
