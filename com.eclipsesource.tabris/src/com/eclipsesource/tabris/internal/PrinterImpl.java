/*******************************************************************************
 * Copyright (c) 2014, 2018 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.EVENT_CANCEL;
import static com.eclipsesource.tabris.internal.Constants.EVENT_ERROR;
import static com.eclipsesource.tabris.internal.Constants.EVENT_SUCCESS;
import static com.eclipsesource.tabris.internal.Constants.METHOD_PRINT;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_JOB_NAME;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_MESSAGE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_URL;
import static com.eclipsesource.tabris.internal.Constants.TYPE_PRINTER;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.lifecycle.ProcessActionRunner;
import org.eclipse.rap.rwt.internal.remote.ConnectionImpl;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;

import com.eclipsesource.tabris.print.PrintError;
import com.eclipsesource.tabris.print.PrintListener;
import com.eclipsesource.tabris.print.Printer;


@SuppressWarnings("restriction")
public class PrinterImpl extends AbstractOperationHandler implements Printer {

  private final RemoteObject remoteObject;
  private final List<PrintListener> printListeners;

  public PrinterImpl() {
    remoteObject = ( ( ConnectionImpl )RWT.getUISession().getConnection() ).createServiceObject( TYPE_PRINTER );
    remoteObject.setHandler( this );
    printListeners = new ArrayList<PrintListener>();
  }

  @Override
  public void print( String url, String jobName ) {
    whenNull( url ).throwIllegalArgument( "URL must not be null." );
    when( url.isEmpty() ).throwIllegalArgument( "URL must not be empty" );
    whenNull( jobName ).throwIllegalArgument( "Job name must not be null." );
    when( jobName.isEmpty() ).throwIllegalArgument( "Job name must not be empty" );
    JsonObject properties = new JsonObject()
      .add( PROPERTY_URL, url)
      .add( PROPERTY_JOB_NAME, jobName );
    remoteObject.call( METHOD_PRINT, properties );
  }

  @Override
  public void handleNotify( String event, JsonObject properties ) {
    if( EVENT_SUCCESS.equals( event ) ) {
      notifyListenersWithPrintSucceeded( properties );
    } else if( EVENT_CANCEL.equals( event ) ) {
      notifyListenersWithPrintCancelled( properties );
    } else if( EVENT_ERROR.equals( event ) ) {
      notifyListenersWithError( getPrintError( properties ) );
    }
  }

  private void notifyListenersWithPrintSucceeded( final JsonObject properties ) {
    ProcessActionRunner.add( new Runnable() {

      @Override
      public void run() {
        doNotifyListenersWithPrintSucceeded( properties );
      }

    } );
  }

  private void doNotifyListenersWithPrintSucceeded( final JsonObject properties ) {
    List<PrintListener> listeners = new ArrayList<PrintListener>( printListeners );
    String jobName = getAsString( properties, PROPERTY_JOB_NAME );
    for( PrintListener listener : listeners ) {
      listener.printSucceeded( jobName );
    }
  }

  private void notifyListenersWithPrintCancelled( final JsonObject properties ) {
    ProcessActionRunner.add( new Runnable() {

      @Override
      public void run() {
        doNotifyListenersWithPrintCancelled( properties );
      }

    } );
  }

  private void doNotifyListenersWithPrintCancelled( final JsonObject properties ) {
    List<PrintListener> listeners = new ArrayList<PrintListener>( printListeners );
    String jobName = getAsString( properties, PROPERTY_JOB_NAME );
    for( PrintListener listener : listeners ) {
      listener.printCanceled( jobName );
    }
  }

  private void notifyListenersWithError( final PrintError error ) {
    ProcessActionRunner.add( new Runnable() {

      @Override
      public void run() {
        doNotifyListenersWithError( error );
      }

    } );
  }

  private void doNotifyListenersWithError( final PrintError error ) {
    List<PrintListener> listeners = new ArrayList<PrintListener>( printListeners );
    for( PrintListener listener : listeners ) {
      listener.printFailed( error );
    }
  }

  private PrintError getPrintError( JsonObject properties ) {
    String jobName = getAsString( properties, PROPERTY_JOB_NAME );
    String message = getAsString( properties, PROPERTY_MESSAGE );
    return new PrintError( jobName, message );
  }

  private String getAsString( JsonObject properties, String property ) {
    JsonValue jsonValue = properties.get( property );
    if ( jsonValue != null ) {
      return jsonValue.asString();
    }
    return null;
  }

  RemoteObject getRemoteObject() {
    return remoteObject;
  }

  @Override
  public void addPrintListener( PrintListener listener ) {
    whenNull( listener ).throwIllegalArgument( "Listener must not be null" );
    printListeners.add( listener );
  }

  @Override
  public void removePrintListener( PrintListener listener ) {
    whenNull( listener ).throwIllegalArgument( "Listener must not be null" );
    printListeners.remove( listener );
  }

}