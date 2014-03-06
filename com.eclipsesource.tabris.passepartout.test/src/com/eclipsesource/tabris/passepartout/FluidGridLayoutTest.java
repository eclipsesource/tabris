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
package com.eclipsesource.tabris.passepartout;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.widgets.IDisplayAdapter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.passepartout.internal.RelayoutListener;
import com.eclipsesource.tabris.passepartout.internal.condition.AlwaysTrueContidtion;


@SuppressWarnings("restriction")
public class FluidGridLayoutTest {

  private Shell shell;

  @Before
  public void setUp() {
    Fixture.setUp();
    shell = new Shell( new Display() );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullMode() {
    new FluidGridLayout( null );
  }

  @Test
  public void testAttachesRelayoutListener() {
    FluidGridLayout layout = new FluidGridLayout( new FluidGridConfiguration( LayoutMode.AUTO, 100, 200 ) );

    layout.layout( shell, true );

    int listenerCount = countRelayoutListeners();
    assertEquals( listenerCount, 1 );
  }

  @Test
  public void testAttachesNoRelayoutListenerWithoutAutoLayout() {
    FluidGridLayout layout = new FluidGridLayout( new FluidGridConfiguration( LayoutMode.NONE, 100, 200 ) );

    layout.layout( shell, true );

    int listenerCount = countRelayoutListeners();
    assertEquals( listenerCount, 0 );
  }

  @Test
  public void testAttachesRelayoutListenerOnce() {
    FluidGridLayout layout = new FluidGridLayout( new FluidGridConfiguration( LayoutMode.AUTO, 100, 200 ) );

    layout.layout( shell, true );
    layout.layout( shell, true );

    int listenerCount = countRelayoutListeners();
    assertEquals( listenerCount, 1 );
  }

  private int countRelayoutListeners() {
    Listener[] listeners = shell.getListeners( SWT.Resize );
    int listenerCount = 0;
    for( Listener listener : listeners ) {
      if( listener instanceof RelayoutListener ) {
        listenerCount++;
      }
    }
    return listenerCount;
  }

  @Test
  public void testRespectsWidthHintWhenComputeSize() {
    Composite composite = new Composite( shell, SWT.NONE );
    FluidGridLayout layout = new FluidGridLayout( new FluidGridConfiguration( LayoutMode.NONE, 100, 200 ) );

    Point size = layout.computeSize( composite, 100, SWT.DEFAULT, true );

    assertEquals( 100, size.x );
  }

  @Test
  public void testRespectsHeightHintWhenComputeSize() {
    Composite composite = new Composite( shell, SWT.NONE );
    FluidGridLayout layout = new FluidGridLayout( new FluidGridConfiguration( LayoutMode.NONE, 100, 200 ) );

    Point size = layout.computeSize( composite, SWT.DEFAULT, 100, true );

    assertEquals( 100, size.y );
  }

  @Test
  public void testRespectsHintsWhenComputeSize() {
    Composite composite = new Composite( shell, SWT.NONE );
    FluidGridLayout layout = new FluidGridLayout( new FluidGridConfiguration( LayoutMode.NONE, 100, 200 ) );

    Point size = layout.computeSize( composite, 50, 100, true );

    assertEquals( 50, size.x );
    assertEquals( 100, size.y );
  }

  @Test
  public void testUsesDisplayBoundsWhenComputeSizeAndParentIsNull() {
    FluidGridLayout layout = new FluidGridLayout( new FluidGridConfiguration( LayoutMode.NONE, 100, 200 ) );

    Point size = layout.computeSize( shell, SWT.DEFAULT, SWT.DEFAULT, true );

    assertEquals( shell.getDisplay().getBounds().width, size.x );
    assertEquals( shell.getDisplay().getBounds().height, size.y );
  }

  @Test
  public void testUsesParentBoundsWhenComputeSize() {
    shell.setSize( 500, 500 );
    Composite composite = new Composite( shell, SWT.NONE );
    FluidGridLayout layout = new FluidGridLayout( new FluidGridConfiguration( LayoutMode.NONE, 100, 200 ) );

    Point size = layout.computeSize( composite, SWT.DEFAULT, SWT.DEFAULT, true );

    assertEquals( 500, size.x );
    assertEquals( 500, size.y );
  }

  @Test
  public void testLayoutsChildren() {
    setDisplayBounds();
    Composite child1 = new Composite( shell, SWT.NONE );
    Composite child2 = new Composite( shell, SWT.NONE );
    FluidGridLayout layout = PassePartout.createFluidGrid();

    layout.layout( shell, true );

    assertEquals( child1.getBounds().x, 0 );
    assertEquals( child1.getBounds().y, 0 );
    assertEquals( child1.getBounds().width, 125 );
    assertEquals( child1.getBounds().height, 64 );
    assertEquals( child2.getBounds().x, 125 );
    assertEquals( child2.getBounds().y, 0 );
    assertEquals( child2.getBounds().width, 125 );
    assertEquals( child2.getBounds().height, 64 );
  }

  private void setDisplayBounds() {
    Object adapter = shell.getDisplay().getAdapter( IDisplayAdapter.class );
    IDisplayAdapter displayAdapter = ( IDisplayAdapter )adapter;
    Rectangle expectedBounds = new Rectangle( 0, 0, 500, 500 );
    displayAdapter.setBounds( expectedBounds );
  }

  @Test
  public void testNotifiesQueryListener() {
    QueryListener listener = mock( QueryListener.class );
    FluidGridLayout layout = new FluidGridLayout( new FluidGridConfiguration( LayoutMode.NONE, 100, 200 ) );
    Query query = PassePartout.when( new AlwaysTrueContidtion() );
    layout.addQueryListener( query, listener );

    layout.layout( shell, false );

    verify( listener ).activated( query );
  }

  @Test
  public void testDoesNotNotifyRemovedQueryListener() {
    QueryListener listener = mock( QueryListener.class );
    FluidGridLayout layout = new FluidGridLayout( new FluidGridConfiguration( LayoutMode.NONE, 100, 200 ) );
    Query query = PassePartout.when( new AlwaysTrueContidtion() );
    layout.addQueryListener( query, listener );

    layout.removeQueryListener( query );
    layout.layout( shell, false );

    verify( listener, never() ).activated( query );
  }

}
