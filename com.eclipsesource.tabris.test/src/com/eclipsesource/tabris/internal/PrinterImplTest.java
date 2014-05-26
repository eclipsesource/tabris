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
package com.eclipsesource.tabris.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.Serializable;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import com.eclipsesource.tabris.print.PrintError;
import com.eclipsesource.tabris.print.PrintListener;
import com.eclipsesource.tabris.print.PrintOptions;
import com.eclipsesource.tabris.print.Printer;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;


public class PrinterImplTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( PrinterImpl.class ) );
  }

  @Test
  public void testPrintListenerIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( PrintListener.class ) );
  }

  @Test
  public void testSetsNoInitialPrintOptionsWithDefaultOptions() {
    RemoteObject remoteObject = environment.getServiceObject();

    new PrinterImpl();

    verify( remoteObject, never() ).set( eq( "url" ), any( JsonValue.class ) );
    verify( remoteObject, never() ).set( eq( "printer" ), any( JsonValue.class ) );
    verify( remoteObject, never() ).set( eq( "jobName" ), any( JsonValue.class ) );
    verify( remoteObject, never() ).set( eq( "showPageRange" ), any( JsonValue.class ) );
    verify( remoteObject, never() ).set( eq( "showNumberOfCopies" ), any( JsonValue.class ) );
    verify( remoteObject, never() ).set( eq( "outputType" ), any( JsonValue.class ) );
    verify( remoteObject, never() ).set( eq( "quality" ), any( JsonValue.class ) );
    verify( remoteObject, never() ).set( eq( "duplex" ), any( JsonValue.class ) );
  }

  @Test
  public void testSendsPrintWithPrintCall() {
    RemoteObject remoteObject = environment.getServiceObject();
    Printer print = new PrinterImpl();

    print.print( "http://localhost/file.pdf", createOptions() );

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( remoteObject ).call( eq( "print" ), captor.capture() );
    assertEquals( "http://localhost/file.pdf", captor.getValue().get( "url" ).asString() );
    assertEquals( "A Printer ID", captor.getValue().get( "printer" ).asString() );
    assertEquals( "A Job Name", captor.getValue().get( "jobName" ).asString() );
    assertTrue( captor.getValue().get( "showPageRange" ).asBoolean() );
    assertTrue( captor.getValue().get( "showNumberOfCopies" ).asBoolean() );
    assertEquals( "normal", captor.getValue().get( "quality" ).asString() );
    assertEquals( "color", captor.getValue().get( "outputType" ).asString() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testPrintFailsWithNullOptions() {
    Printer print = new PrinterImpl();

    print.print( "foo", null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testPrintFailsWithNullUrl() {
    Printer print = new PrinterImpl();

    print.print( null, new PrintOptions() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testPrintFailsWithEmptyUrl() {
    Printer print = new PrinterImpl();

    print.print( "", new PrintOptions() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testAddListenerFailsWithNullListener() {
    Printer print = new PrinterImpl();

    print.addPrintListener( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testRemoveListenerFailsWithNullListener() {
    Printer print= new PrinterImpl();

    print.removePrintListener( null );
  }

  @Test
  public void testDelegatesError() {
    PrinterImpl print = new PrinterImpl();
    PrintListener listener = mock( PrintListener.class );
    print.addPrintListener( listener );
    JsonObject properties = new JsonObject();
    properties.add( "printer", "" );
    properties.add( "jobName", "" );
    properties.add( "message", "" );

    print.print( "http://localhost/file.pdf", createOptions() );
    environment.dispatchNotifyOnServiceObject( "Error", properties );

    verify( listener ).printFailed( any( PrintError.class ) );
  }

  @Test
  public void testDelegatesErrorWithoutProperties() {
    PrinterImpl print = new PrinterImpl();
    PrintListener listener = mock( PrintListener.class );
    print.addPrintListener( listener );

    print.print( "http://localhost/file.pdf", createOptions() );
    environment.dispatchNotifyOnServiceObject( "Error", new JsonObject() );

    verify( listener ).printFailed( any( PrintError.class ) );
  }

  @Test
  public void testDelegatesErrorToAllListeners() {
    PrinterImpl print = new PrinterImpl();
    PrintListener listener1 = mock( PrintListener.class );
    PrintListener listener2 = mock( PrintListener.class );
    print.addPrintListener( listener1 );
    print.addPrintListener( listener2 );
    JsonObject properties = new JsonObject();
    properties.add( "printer", "" );
    properties.add( "jobName", "" );
    properties.add( "message", "" );

    print.print( "http://localhost/file.pdf", createOptions() );
    environment.dispatchNotifyOnServiceObject( "Error", properties );

    InOrder order = inOrder( listener1, listener2 );
    order.verify( listener1 ).printFailed( any( PrintError.class ) );
    order.verify( listener2 ).printFailed( any( PrintError.class ) );
  }

  @Test
  public void testDelegatesCancel() {
    PrinterImpl print = new PrinterImpl();
    PrintListener listener = mock( PrintListener.class );
    print.addPrintListener( listener );
    JsonObject properties = new JsonObject();
    properties.add( "printer", "" );
    properties.add( "jobName", "" );

    print.print( "http://localhost/file.pdf", createOptions() );
    environment.dispatchNotifyOnServiceObject( "Cancel", properties );

    verify( listener ).printCanceled( any( String.class ), any( String.class ) );
  }

  @Test
  public void testDelegatesCancelWithoutProperties() {
    PrinterImpl print = new PrinterImpl();
    PrintListener listener = mock( PrintListener.class );
    print.addPrintListener( listener );

    print.print( "http://localhost/file.pdf", createOptions() );
    environment.dispatchNotifyOnServiceObject( "Cancel", new JsonObject() );

    verify( listener ).printCanceled( null, null );
  }

  @Test
  public void testDelegatesCancelToAllListeners() {
    PrinterImpl print = new PrinterImpl();
    PrintListener listener1 = mock( PrintListener.class );
    PrintListener listener2 = mock( PrintListener.class );
    print.addPrintListener( listener1 );
    print.addPrintListener( listener2 );
    JsonObject properties = new JsonObject();
    properties.add( "printer", "" );
    properties.add( "jobName", "" );

    print.print( "http://localhost/file.pdf", createOptions() );
    environment.dispatchNotifyOnServiceObject( "Cancel", properties );

    InOrder order = inOrder( listener1, listener2 );
    order.verify( listener1 ).printCanceled( any( String.class ), any( String.class ) );
    order.verify( listener2 ).printCanceled( any( String.class ), any( String.class ) );
  }

  @Test
  public void testDelegatesSuccess() {
    PrinterImpl print = new PrinterImpl();
    PrintListener listener = mock( PrintListener.class );
    print.addPrintListener( listener );
    JsonObject properties = new JsonObject();
    properties.add( "printer", "" );
    properties.add( "jobName", "" );

    print.print( "http://localhost/file.pdf", createOptions() );
    environment.dispatchNotifyOnServiceObject( "Success", properties );

    verify( listener ).printSucceeded( any( String.class ), any( String.class ) );
  }

  @Test
  public void testDelegatesSuccessWithoutProperties() {
    PrinterImpl print = new PrinterImpl();
    PrintListener listener = mock( PrintListener.class );
    print.addPrintListener( listener );

    print.print( "http://localhost/file.pdf", createOptions() );
    environment.dispatchNotifyOnServiceObject( "Success", new JsonObject() );

    verify( listener ).printSucceeded( null, null );
  }

  @Test
  public void testDelegatesSuccessToAllListeners() {
    PrinterImpl print = new PrinterImpl();
    PrintListener listener1 = mock( PrintListener.class );
    PrintListener listener2 = mock( PrintListener.class );
    print.addPrintListener( listener1 );
    print.addPrintListener( listener2 );
    JsonObject properties = new JsonObject();
    properties.add( "printer", "" );
    properties.add( "jobName", "" );

    print.print( "http://localhost/file.pdf", createOptions() );
    environment.dispatchNotifyOnServiceObject( "Success", properties );

    InOrder order = inOrder( listener1, listener2 );
    order.verify( listener1 ).printSucceeded( any( String.class ), any( String.class ) );
    order.verify( listener2 ).printSucceeded( any( String.class ), any( String.class ) );
  }

  private PrintOptions createOptions() {
    PrintOptions options = new PrintOptions();
    options.setPrinter( "A Printer ID" );
    options.setJobName( "A Job Name" );
    return options;
  }

}