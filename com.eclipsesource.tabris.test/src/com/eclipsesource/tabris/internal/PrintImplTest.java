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

import static com.eclipsesource.tabris.test.TabrisTestUtil.dispatchNotify;
import static com.eclipsesource.tabris.test.TabrisTestUtil.mockServiceObject;
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
import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import com.eclipsesource.tabris.print.Print;
import com.eclipsesource.tabris.print.PrintError;
import com.eclipsesource.tabris.print.PrintListener;
import com.eclipsesource.tabris.print.PrintOptions;
import com.eclipsesource.tabris.test.RWTRunner;
import com.eclipsesource.tabris.test.TabrisTestUtil;


@RunWith( RWTRunner.class )
public class PrintImplTest {

  @Before
  public void setUp() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    new Display();
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( PrintImpl.class ) );
  }

  @Test
  public void testPrintListenerIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( PrintListener.class ) );
  }

  @Test
  public void testSetsNoInitialPrintOptionsWithDefaultOptions() {
    RemoteObject remoteObject = mockServiceObject();

    new PrintImpl();

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
    RemoteObject remoteObject = mockServiceObject();
    Print print = new PrintImpl();

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
    Print print = new PrintImpl();

    print.print( "foo", null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testPrintFailsWithNullUrl() {
    Print print = new PrintImpl();

    print.print( null, new PrintOptions() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testPrintFailsWithEmptyUrl() {
    Print print = new PrintImpl();

    print.print( "", new PrintOptions() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testAddListenerFailsWithNullListener() {
    Print print = new PrintImpl();

    print.addPrintListener( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testRemoveListenerFailsWithNullListener() {
    Print print= new PrintImpl();

    print.removePrintListener( null );
  }

  @Test
  public void testDelegatesError() {
    PrintImpl print = new PrintImpl();
    PrintListener listener = mock( PrintListener.class );
    print.addPrintListener( listener );
    JsonObject properties = new JsonObject();
    properties.add( "printer", "" );
    properties.add( "jobName", "" );
    properties.add( "message", "" );

    print.print( "http://localhost/file.pdf", createOptions() );
    dispatchNotify( print.getRemoteObject(), "Error", properties );

    verify( listener ).printFailed( any( PrintError.class ) );
  }

  @Test
  public void testDelegatesErrorWithoutProperties() {
    PrintImpl print = new PrintImpl();
    PrintListener listener = mock( PrintListener.class );
    print.addPrintListener( listener );

    print.print( "http://localhost/file.pdf", createOptions() );
    dispatchNotify( print.getRemoteObject(), "Error",  new JsonObject() );

    verify( listener ).printFailed( any( PrintError.class ) );
  }

  @Test
  public void testDelegatesErrorToAllListeners() {
    PrintImpl print = new PrintImpl();
    PrintListener listener1 = mock( PrintListener.class );
    PrintListener listener2 = mock( PrintListener.class );
    print.addPrintListener( listener1 );
    print.addPrintListener( listener2 );
    JsonObject properties = new JsonObject();
    properties.add( "printer", "" );
    properties.add( "jobName", "" );
    properties.add( "message", "" );

    print.print( "http://localhost/file.pdf", createOptions() );
    TabrisTestUtil.dispatchNotify( print.getRemoteObject(), "Error", properties );

    InOrder order = inOrder( listener1, listener2 );
    order.verify( listener1 ).printFailed( any( PrintError.class ) );
    order.verify( listener2 ).printFailed( any( PrintError.class ) );
  }

  @Test
  public void testDelegatesCancel() {
    PrintImpl print = new PrintImpl();
    PrintListener listener = mock( PrintListener.class );
    print.addPrintListener( listener );
    JsonObject properties = new JsonObject();
    properties.add( "printer", "" );
    properties.add( "jobName", "" );

    print.print( "http://localhost/file.pdf", createOptions() );
    dispatchNotify( print.getRemoteObject(), "Cancel", properties );

    verify( listener ).printCanceled( any( String.class ), any( String.class ) );
  }

  @Test
  public void testDelegatesCancelWithoutProperties() {
    PrintImpl print = new PrintImpl();
    PrintListener listener = mock( PrintListener.class );
    print.addPrintListener( listener );

    print.print( "http://localhost/file.pdf", createOptions() );
    TabrisTestUtil.dispatchNotify( print.getRemoteObject(), "Cancel", new JsonObject() );

    verify( listener ).printCanceled( null, null );
  }

  @Test
  public void testDelegatesCancelToAllListeners() {
    PrintImpl print = new PrintImpl();
    PrintListener listener1 = mock( PrintListener.class );
    PrintListener listener2 = mock( PrintListener.class );
    print.addPrintListener( listener1 );
    print.addPrintListener( listener2 );
    JsonObject properties = new JsonObject();
    properties.add( "printer", "" );
    properties.add( "jobName", "" );

    print.print( "http://localhost/file.pdf", createOptions() );
    dispatchNotify( print.getRemoteObject(), "Cancel", properties );

    InOrder order = inOrder( listener1, listener2 );
    order.verify( listener1 ).printCanceled( any( String.class ), any( String.class ) );
    order.verify( listener2 ).printCanceled( any( String.class ), any( String.class ) );
  }

  @Test
  public void testDelegatesSuccess() {
    PrintImpl print = new PrintImpl();
    PrintListener listener = mock( PrintListener.class );
    print.addPrintListener( listener );
    JsonObject properties = new JsonObject();
    properties.add( "printer", "" );
    properties.add( "jobName", "" );

    print.print( "http://localhost/file.pdf", createOptions() );
    dispatchNotify( print.getRemoteObject(), "Success", properties );

    verify( listener ).printSucceeded( any( String.class ), any( String.class ) );
  }

  @Test
  public void testDelegatesSuccessWithoutProperties() {
    PrintImpl print = new PrintImpl();
    PrintListener listener = mock( PrintListener.class );
    print.addPrintListener( listener );

    print.print( "http://localhost/file.pdf", createOptions() );
    dispatchNotify( print.getRemoteObject(), "Success", new JsonObject() );

    verify( listener ).printSucceeded( null, null );
  }

  @Test
  public void testDelegatesSuccessToAllListeners() {
    PrintImpl print = new PrintImpl();
    PrintListener listener1 = mock( PrintListener.class );
    PrintListener listener2 = mock( PrintListener.class );
    print.addPrintListener( listener1 );
    print.addPrintListener( listener2 );
    JsonObject properties = new JsonObject();
    properties.add( "printer", "" );
    properties.add( "jobName", "" );

    print.print( "http://localhost/file.pdf", createOptions() );
    dispatchNotify( print.getRemoteObject(), "Success", properties );

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