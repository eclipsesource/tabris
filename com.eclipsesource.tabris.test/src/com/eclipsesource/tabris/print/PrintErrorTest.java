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
package com.eclipsesource.tabris.print;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;


public class PrintErrorTest {

  @Test
  public void testSetsPrinter() {
    PrintError error = new PrintError( "printer", "job", "message" );

    String printer = error.getPrinter();

    assertEquals( "printer", printer );
  }

  @Test
  public void testPrinterCanBeNull() {
    PrintError error = new PrintError( null, "job", "message" );

    String printer = error.getPrinter();

    assertNull( printer );
  }

  @Test
  public void testSetsJob() {
    PrintError error = new PrintError( "printer", "job", "message" );

    String jobName = error.getJobName();

    assertEquals( "job", jobName );
  }

  @Test
  public void testJobCanBeNull() {
    PrintError error = new PrintError( "printer", null, "message" );

    String jobName = error.getJobName();

    assertNull( jobName );
  }

  @Test
  public void testSetsMessage() {
    PrintError error = new PrintError( "printer", "job", "message" );

    String message = error.getMessage();

    assertEquals( "message", message );
  }

  @Test
  public void testMessageCanBeNull() {
    PrintError error = new PrintError( "printer", "job", null );

    String message = error.getMessage();

    assertNull( message );
  }
}
