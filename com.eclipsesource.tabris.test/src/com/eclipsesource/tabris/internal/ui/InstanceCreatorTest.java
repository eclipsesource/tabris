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

import static org.junit.Assert.assertNotNull;

import org.eclipse.swt.widgets.Composite;
import org.junit.Test;

import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.UI;


public class InstanceCreatorTest {

  @Test
  public void testCanCreateInstance() {
    TestClass instance = InstanceCreator.createInstance( TestClass.class );

    assertNotNull( instance );
  }

  @Test( expected = IllegalStateException.class )
  public void testCantCreateInstanceWithoutNoArgumentConstructor() {
    InstanceCreator.createInstance( TestClassWithoutNoArgumentConstructor.class );
  }

  @Test( expected = IllegalStateException.class )
  public void testCantCreateInstanceWithPrivateConstructor() {
    InstanceCreator.createInstance( TestClassWithPrivateConstructor.class );
  }

  public static class TestClass implements Page {

    @Override
    public void createContent( Composite parent, UI ui ) {
    }

    @Override
    public void activate() {
    }

    @Override
    public void deactivate() {
    }

    @Override
    public void destroy() {
    }

  }

  public static class TestClassWithoutNoArgumentConstructor implements Page {

    public TestClassWithoutNoArgumentConstructor( Object arg1 ) {
    }

    @Override
    public void createContent( Composite parent, UI ui ) {
    }

    @Override
    public void activate() {
    }

    @Override
    public void deactivate() {
    }

    @Override
    public void destroy() {
    }

  }

  public static class TestClassWithPrivateConstructor implements Page {

    private TestClassWithPrivateConstructor() {
    }

    @Override
    public void createContent( Composite parent, UI ui ) {
    }

    @Override
    public void activate() {
    }

    @Override
    public void deactivate() {
    }

    @Override
    public void destroy() {
    }

  }
}
