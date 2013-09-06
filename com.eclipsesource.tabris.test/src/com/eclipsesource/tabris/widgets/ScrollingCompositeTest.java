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
package com.eclipsesource.tabris.widgets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ScrollingCompositeTest {

  private Shell shell;

  @Before
  public void setUp() {
    Fixture.setUp();
    Display display = new Display();
    shell = new Shell( display );
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullParent() {
    new ScrollingComposite( null, SWT.H_SCROLL );
  }

  @Test
  public void testUsesScrolledCompositeAsParent() {
    ScrollingComposite composite = new ScrollingComposite( shell, SWT.H_SCROLL );

    Composite parent = composite.getParent();

    assertTrue( parent instanceof ScrolledComposite );
  }

  @Test
  public void testExpandsHorizontal() {
    ScrollingComposite composite = new ScrollingComposite( shell, SWT.H_SCROLL );
    ScrolledComposite scrolledComposite = ( ScrolledComposite )composite.getParent();

    boolean expandHorizontal = scrolledComposite.getExpandHorizontal();

    assertTrue( expandHorizontal );
  }

  @Test
  public void testExpandsVertical() {
    ScrollingComposite composite = new ScrollingComposite( shell, SWT.H_SCROLL );
    ScrolledComposite scrolledComposite = ( ScrolledComposite )composite.getParent();

    boolean expandVertical = scrolledComposite.getExpandVertical();

    assertTrue( expandVertical );
  }

  @Test
  public void testDelegatesLayoutData() {
    ScrollingComposite composite = new ScrollingComposite( shell, SWT.H_SCROLL );
    ScrolledComposite scrolledComposite = ( ScrolledComposite )composite.getParent();
    GridData data = new GridData();

    composite.setLayoutData( data );

    assertSame( data, scrolledComposite.getLayoutData() );
  }

  @Test
  public void testSetsVerticalAndHorizontalMinSize() {
    ScrollingComposite composite = new ScrollingComposite( shell, SWT.H_SCROLL | SWT.V_SCROLL );
    ScrolledComposite scrolledComposite = ( ScrolledComposite )composite.getParent();
    scrolledComposite.setBounds( 0, 0, 200, 300 );
    Point size = composite.computeSize( SWT.DEFAULT, SWT.DEFAULT );

    int minWidth = scrolledComposite.getMinWidth();
    int minHeight = scrolledComposite.getMinHeight();

    assertEquals( size.x, minWidth );
    assertEquals( size.y, minHeight );
  }

  @Test
  public void testSetsHorizontalMinSize() {
    ScrollingComposite composite = new ScrollingComposite( shell, SWT.H_SCROLL );
    ScrolledComposite scrolledComposite = ( ScrolledComposite )composite.getParent();
    scrolledComposite.setBounds( 0, 0, 200, 300 );
    Point size = composite.computeSize( SWT.DEFAULT, SWT.DEFAULT );

    int minWidth = scrolledComposite.getMinWidth();
    int minHeight = scrolledComposite.getMinHeight();

    assertEquals( size.x, minWidth );
    assertEquals( 0, minHeight );
  }

  @Test
  public void testSetsHorizontalMinSizeOnResize() {
    ScrollingComposite composite = new ScrollingComposite( shell, SWT.H_SCROLL );
    ScrolledComposite scrolledComposite = ( ScrolledComposite )composite.getParent();
    scrolledComposite.setSize( 100, 100 );
    Point size = composite.computeSize( SWT.DEFAULT, SWT.DEFAULT );

    int minWidth = scrolledComposite.getMinWidth();
    int minHeight = scrolledComposite.getMinHeight();

    assertEquals( size.x, minWidth );
    assertEquals( 0, minHeight );
  }

  @Test
  public void testSetsVerticalMinSize() {
    ScrollingComposite composite = new ScrollingComposite( shell, SWT.V_SCROLL );
    ScrolledComposite scrolledComposite = ( ScrolledComposite )composite.getParent();
    scrolledComposite.setBounds( 0, 0, 200, 300 );
    Point size = composite.computeSize( SWT.DEFAULT, SWT.DEFAULT );

    int minWidth = scrolledComposite.getMinWidth();
    int minHeight = scrolledComposite.getMinHeight();

    assertEquals( 200, minWidth );
    assertEquals( size.y, minHeight );
  }

  @Test
  public void testSetsVerticalMinSizeOnResize() {
    ScrollingComposite composite = new ScrollingComposite( shell, SWT.V_SCROLL );
    ScrolledComposite scrolledComposite = ( ScrolledComposite )composite.getParent();
    scrolledComposite.setSize( 100, 100 );
    Point size = composite.computeSize( SWT.DEFAULT, SWT.DEFAULT );

    int minWidth = scrolledComposite.getMinWidth();
    int minHeight = scrolledComposite.getMinHeight();

    assertEquals( 100, minWidth );
    assertEquals( size.y, minHeight );
  }

  @Test
  public void testRevealsItem() {
    shell.setSize( 100, 100 );
    shell.setLayout( new FillLayout() );
    ScrollingComposite composite = new ScrollingComposite( shell, SWT.V_SCROLL );
    composite.setLayout( new RowLayout( SWT.VERTICAL ) );
    createChildren( composite );
    Control child = composite.getChildren()[ 80 ];

    composite.reveal( child );

    assertTrue( composite.isRevealed( child ) );
  }

  @Test
  public void testIsNotRevealedByDefault() {
    shell.setSize( 100, 100 );
    shell.setLayout( new FillLayout() );
    ScrollingComposite composite = new ScrollingComposite( shell, SWT.V_SCROLL );
    composite.setLayout( new RowLayout( SWT.VERTICAL ) );
    createChildren( composite );
    Control child = composite.getChildren()[ 80 ];
    shell.layout( true, true );

    boolean revealed = composite.isRevealed( child );

    assertFalse( revealed );
  }

  private void createChildren( ScrollingComposite composite ) {
    for( int i = 0; i < 100; i++ ) {
      Label label = new Label( composite, SWT.NONE );
      label.setText( "FooBar" );
    }
  }

  @Test( expected = IllegalArgumentException.class )
  public void testRevealFailsWithNullControl() {
    ScrollingComposite composite = new ScrollingComposite( shell, SWT.V_SCROLL );

    composite.reveal( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testIsRevealFailsWithNullControl() {
    ScrollingComposite composite = new ScrollingComposite( shell, SWT.V_SCROLL );

    composite.isRevealed( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testRevealFailsWithNonChild() {
    ScrollingComposite composite = new ScrollingComposite( shell, SWT.V_SCROLL );
    Composite control = new Composite( shell, SWT.NONE );

    composite.reveal( control );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testIsRevealFailsWithNonChild() {
    ScrollingComposite composite = new ScrollingComposite( shell, SWT.V_SCROLL );
    Composite control = new Composite( shell, SWT.NONE );

    composite.isRevealed( control );
  }

  @Test
  public void testDisposesScrolledComposite() {
    ScrollingComposite composite = new ScrollingComposite( shell, SWT.V_SCROLL );
    ScrolledComposite scrolledComposite = ( ScrolledComposite )composite.getParent();

    composite.dispose();

    assertTrue( composite.isDisposed() );
    assertTrue( scrolledComposite.isDisposed() );
  }

  @Test
  public void testDoubleDisposingDoesNotFail() {
    ScrollingComposite composite = new ScrollingComposite( shell, SWT.V_SCROLL );

    composite.dispose();
    composite.dispose();
  }

  @Test
  public void testComputeSizeIsNeverNull() {
    ScrollingComposite composite = new ScrollingComposite( shell, SWT.NONE );

    Point size = composite.computeSize( SWT.DEFAULT, SWT.DEFAULT );

    assertNotNull( size );
  }
}
