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
package com.eclipsesource.tabris.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class SwipeLayoutTest {
  
  private Shell shell;

  @Before
  public void setUp() {
    Fixture.setUp();
    shell = new Shell( new Display() );
    shell.setSize( 50, 50 );
    shell.setLayout( new FillLayout() );
  }
  
  @After
  public void tearDown() {
    Fixture.tearDown();
  }
  
  @Test
  public void testUsesWholeClientAreaForLayout() {
    Composite parent = new Composite( shell, SWT.NONE );
    parent.setLayout( new SwipeLayout() );
    Composite child = new Composite( parent, SWT.NONE );
    shell.layout( true, true );
    
    Point size = child.computeSize( SWT.DEFAULT, SWT.DEFAULT );
    
    assertEquals( 64, size.x );
    assertEquals( 64, size.y );
  }
  
  @Test
  public void testUsesHints() {
    Composite parent = new Composite( shell, SWT.NONE );
    parent.setLayout( new SwipeLayout() );
    Composite child = new Composite( parent, SWT.NONE );
    shell.layout( true, true );
    
    Point size = child.computeSize( 100, 100 );
    
    assertEquals( 100, size.x );
    assertEquals( 100, size.y );
  }
  
  @Test
  public void testModifiesZIndexOnLayout() {
    Composite parent = new Composite( shell, SWT.NONE );
    SwipeLayout layout = new SwipeLayout();
    parent.setLayout( layout );
    new Composite( parent, SWT.NONE );
    Composite child2 = new Composite( parent, SWT.NONE );
    layout.setOnTopControl( child2 );
    
    shell.layout( true, true );
    
    assertSame( child2, parent.getChildren()[ 0 ] );
  }
  
  @Test
  public void testModifiesZIndexOnLayoutWhenTopControlChangedTwice() {
    Composite parent = new Composite( shell, SWT.NONE );
    SwipeLayout layout = new SwipeLayout();
    parent.setLayout( layout );
    Composite child1 = new Composite( parent, SWT.NONE );
    Composite child2 = new Composite( parent, SWT.NONE );
    
    layout.setOnTopControl( child1 );
    layout.setOnTopControl( child2 );
    layout.setOnTopControl( child1 );
    
    shell.layout( true, true );
    
    assertSame( child1, parent.getChildren()[ 0 ] );
  }
  
  @Test
  public void testGetOnTopControl() {
    SwipeLayout swipeLayout = new SwipeLayout();
    Control control = mock( Control.class );
    
    swipeLayout.setOnTopControl( control);
    
    assertEquals( control, swipeLayout.getOnTopControl() );
  }
}
