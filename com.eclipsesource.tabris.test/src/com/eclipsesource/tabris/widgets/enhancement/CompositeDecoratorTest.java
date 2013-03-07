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
package com.eclipsesource.tabris.widgets.enhancement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class CompositeDecoratorTest {

  private Composite composite;
  private CompositeDecorator decorator;

  @Before
  public void setUp() {
    Fixture.setUp();
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Shell shell = new Shell( new Display() );
    composite = new Composite( shell, SWT.NONE );
    composite.setLayout( new GridLayout() );
    decorator = new CompositeDecorator( composite );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testAddGroupedListenerFailsWithNullListener() {
    decorator.addGroupedListener( SWT.Selection, null );
  }

  @Test( expected = IllegalStateException.class )
  public void testAddGroupedListenerFailsWithFormLayout() {
    composite.setLayout( new FormLayout() );

    decorator.addGroupedListener( SWT.Selection, mock( Listener.class ) );
  }

  @Test( expected = IllegalStateException.class )
  public void testAddGroupedListenerFailsWithFillLayout() {
    composite.setLayout( new FillLayout() );

    decorator.addGroupedListener( SWT.Selection, mock( Listener.class ) );
  }

  @Test( expected = IllegalStateException.class )
  public void testAddGroupedListenerFailsWithNullLayout() {
    composite.setLayout( null );

    decorator.addGroupedListener( SWT.Selection, mock( Listener.class ) );
  }

  @Test
  public void testAddGroupedListenerAddsFacadeComposite() {
    decorator.addGroupedListener( SWT.Selection, mock( Listener.class ) );

    Control facade = composite.getChildren()[ 0 ];
    String compositeId = WidgetUtil.getId( composite );
    Object data = facade.getData( "groupedEventComposite" );
    assertEquals( compositeId, data );
  }

  @Test
  public void testAddMultipleGroupedListenersAddsFacadeCompositeOnlyOnce() {
    decorator.addGroupedListener( SWT.Selection, mock( Listener.class ) );
    decorator.addGroupedListener( SWT.Selection, mock( Listener.class ) );

    assertEquals( 1, composite.getChildren().length );
  }

  @Test
  public void testAddGroupedListenerAddsListenerToFacade() {
    Listener listener = mock( Listener.class );

    decorator.addGroupedListener( SWT.Selection, listener );

    Control facade = composite.getChildren()[ 0 ];
    Listener[] listeners = facade.getListeners( SWT.Selection );
    assertEquals( 1, listeners.length );
    assertSame( listener, listeners[ 0 ] );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testRemoveGroupedListenerFailsWithNullListener() {
    decorator.removeGroupedListener( SWT.Selection, null );
  }

  @Test
  public void testRemoveGroupedListenerDoesNotCreateFacade() {
    decorator.removeGroupedListener( SWT.Selection, mock( Listener.class ) );

    assertEquals( 0, composite.getChildren().length );
  }

  @Test
  public void testRemoveGroupedListenerRemovesListenerFromFacade() {
    Listener listener = mock( Listener.class );
    decorator.addGroupedListener( SWT.Selection, listener );

    decorator.removeGroupedListener( SWT.Selection, listener );

    Control facade = composite.getChildren()[ 0 ];
    Listener[] listeners = facade.getListeners( SWT.Selection );
    assertEquals( 0, listeners.length );
  }

  @Test
  public void testFacadeAdoptsBoundsFromComposite() {
    composite.setBounds( 100, 100, 200, 200 );
    decorator.addGroupedListener( SWT.Selection, mock( Listener.class ) );
    Control facade = composite.getChildren()[ 0 ];

    Rectangle bounds = facade.getBounds();
    assertEquals( 0, bounds.x );
    assertEquals( 0, bounds.y );
    assertEquals( 200, bounds.width );
    assertEquals( 200, bounds.height );
  }

  @Test
  public void testFacadeAdoptsBoundsFromCompositeOnResize() {
    composite.setBounds( 100, 100, 200, 200 );
    decorator.addGroupedListener( SWT.Selection, mock( Listener.class ) );
    Control facade = composite.getChildren()[ 0 ];

    composite.setBounds( 200, 200, 300, 300 );

    Rectangle bounds = facade.getBounds();
    assertEquals( 0, bounds.x );
    assertEquals( 0, bounds.y );
    assertEquals( 300, bounds.width );
    assertEquals( 300, bounds.height );
  }

  @Test
  public void testDisposingCompositeAlsoDisposesFacade() {
    decorator.addGroupedListener( SWT.Selection, mock( Listener.class ) );
    Control facade = composite.getChildren()[ 0 ];

    composite.dispose();

    assertTrue( facade.isDisposed() );
  }

  @Test
  public void testExcludesFacadeFromLayoutingWithGridLayout() {
    decorator.addGroupedListener( SWT.Selection, mock( Listener.class ) );

    Control facade = composite.getChildren()[ 0 ];
    GridData layoutData = ( GridData )facade.getLayoutData();
    assertTrue( layoutData.exclude );
  }

  @Test
  public void testFacadeHasCustomVariantForTheming() {
    decorator.addGroupedListener( SWT.Selection, mock( Listener.class ) );
    Control facade = composite.getChildren()[ 0 ];

    Object variant = facade.getData( RWT.CUSTOM_VARIANT );

    assertEquals( "groupedEventComposite", variant );
  }

  @Test
  public void testExcludesFacadeFromLayoutingWithRowLayout() {
    composite.setLayout( new RowLayout() );

    decorator.addGroupedListener( SWT.Selection, mock( Listener.class ) );

    Control facade = composite.getChildren()[ 0 ];
    RowData layoutData = ( RowData )facade.getLayoutData();
    assertTrue( layoutData.exclude );
  }

  @Test
  public void testAddsFacadeAllwaysOnTop() {
    new Button( composite, SWT.PUSH );

    decorator.addGroupedListener( SWT.Selection, mock( Listener.class ) );

    Control facade = composite.getChildren()[ 0 ];
    assertTrue( facade instanceof Composite );
  }

  @Test
  public void testLayoutsFacadeAllwaysOnTop() {
    decorator.addGroupedListener( SWT.Selection, mock( Listener.class ) );

    new Button( composite, SWT.PUSH );

    Control facade = composite.getChildren()[ 0 ];
    assertTrue( facade instanceof Composite );
  }

}
