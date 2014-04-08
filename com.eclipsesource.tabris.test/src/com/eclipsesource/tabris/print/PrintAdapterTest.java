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

import org.junit.Test;


public class PrintAdapterTest {

  @Test
  public void testSucceededDoesNotFail() {
    PrintAdapter printAdapter = new PrintAdapter();

    printAdapter.printSucceeded( "foo", "foo" );
  }

  @Test
  public void testCanceledDoesNotFail() {
    PrintAdapter printAdapter = new PrintAdapter();

    printAdapter.printCanceled( "foo", "foo" );
  }

  @Test
  public void testErrorDoesNotFail() {
    PrintAdapter printAdapter = new PrintAdapter();

    printAdapter.printFailed( new PrintError( "foo", "foo", "foo" ) );
  }
}
