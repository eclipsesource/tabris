/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets.enhancement;

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.ALT_SELECTION;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.BACK_FOCUS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.template.Template;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.eclipsesource.tabris.widgets.enhancement.TreeDecorator.TreePart;


@RunWith( MockitoJUnitRunner.class )
public class TreeDecoratorTest {

  @Mock
  private Tree tree;
  private TreeDecorator decorator;
  private Display display;

  @Before
  public void setUp() {
    Fixture.setUp();
    display = new Display();
    decorator = Widgets.onTree( tree );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testUseTitle() {
    decorator.useTitle( "test" );

    verify( tree ).setToolTipText( eq( "test" ) );
  }

  @Test
  public void testSetAlternativeLeafSelection() {
    decorator.enableAlternativeSelection( TreePart.LEAF );

    verify( tree ).setData( ALT_SELECTION.getKey(), "leaf" );
  }

  @Test
  public void testSetAlternativeBranchSelection() {
    decorator.enableAlternativeSelection( TreePart.BRANCH );

    verify( tree ).setData( ALT_SELECTION.getKey(), "branch" );
  }

  @Test
  public void testSetAlternativeSelectionForAll() {
    decorator.enableAlternativeSelection( TreePart.ALL );

    verify( tree ).setData( ALT_SELECTION.getKey(), "all" );
  }

  @Test
  public void testSetBackButtonEnabledShouldSetBackFocusData() {
    Shell shell = new Shell( display );
    Tree focusTree = new Tree( shell, SWT.NONE );

    Widgets.onTree( focusTree ).setBackButtonNavigationEnabled( true );

    assertEquals( Boolean.TRUE, focusTree.getData( BACK_FOCUS.getKey() ) );
  }

  @Test
  public void testSetBackButtonDisabledShouldSetBackFocusData() {
    Shell shell = new Shell( display );
    Tree focusTree = new Tree( shell, SWT.NONE );

    Widgets.onTree( focusTree ).setBackButtonNavigationEnabled( false );

    assertEquals( Boolean.FALSE, focusTree.getData( BACK_FOCUS.getKey() ) );
  }

  @Test
  public void testEnableBackButtonNavigationReturnsDecorator() {
    Shell shell = new Shell( display );
    Tree focusTree = new Tree( shell, SWT.NONE );
    TreeDecorator treeDecorator = Widgets.onTree( focusTree );

    TreeDecorator actualDecorator = treeDecorator.setBackButtonNavigationEnabled( true );

    assertSame( treeDecorator, actualDecorator );
  }

  @Test
  public void testDisableBackButtonNavigationReturnsDecorator() {
    Shell shell = new Shell( display );
    Tree focusTree = new Tree( shell, SWT.NONE );
    TreeDecorator treeDecorator = Widgets.onTree( focusTree );

    TreeDecorator actualDecorator = treeDecorator.setBackButtonNavigationEnabled( false );

    assertSame( treeDecorator, actualDecorator );
  }

  @Test
  public void testEnableBackButtonNavigationShouldSetNullVariantOnOtherTreesWithBackFocusVariant() {
    Shell shell = new Shell( display );
    Tree tree1 = new Tree( shell, SWT.NONE );
    Tree tree2 = new Tree( shell, SWT.NONE );
    Tree tree3 = new Tree( shell, SWT.NONE );
    tree2.setData( BACK_FOCUS.getKey(), Boolean.TRUE );
    tree3.setData( BACK_FOCUS.getKey(), "anyVariant" );

    Widgets.onTree( tree1 ).setBackButtonNavigationEnabled( true );

    assertEquals( Boolean.TRUE, tree1.getData( BACK_FOCUS.getKey() ) );
    assertNull( tree2.getData( BACK_FOCUS.getKey() ) );
    assertEquals( "anyVariant", tree3.getData( BACK_FOCUS.getKey() ) );
  }

  @Test
  public void testDisableBackButtonNavigationShouldSetNullVariantOnOtherTreesWithBackFocusVariant() {
    Shell shell = new Shell( display );
    Tree tree1 = new Tree( shell, SWT.NONE );
    Tree tree2 = new Tree( shell, SWT.NONE );
    Tree tree3 = new Tree( shell, SWT.NONE );
    tree2.setData( BACK_FOCUS.getKey(), Boolean.TRUE );
    tree3.setData( BACK_FOCUS.getKey(), "anyVariant" );

    Widgets.onTree( tree1 ).setBackButtonNavigationEnabled( false );

    assertEquals( Boolean.FALSE, tree1.getData( BACK_FOCUS.getKey() ) );
    assertNotNull( tree2.getData( BACK_FOCUS.getKey() ) );
    assertEquals( "anyVariant", tree3.getData( BACK_FOCUS.getKey() ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetPreloadedItemsFailsWithNegativeItems() {
    decorator.setPreloadedItems( -1 );
  }

  @Test
  public void testSetPreloadedItemsReturnsDecorator() {
    TreeDecorator actualDecorator = decorator.setPreloadedItems( 1 );

    assertSame( decorator, actualDecorator );
  }

  @Test
  public void testSetPreloadedItemsAsCustomData() {
    decorator.setPreloadedItems( 1 );

    verify( tree ).setData( RWT.PRELOADED_ITEMS, Integer.valueOf( 1 ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullTemplate() {
    decorator.setTemplate( null );
  }

  @Test
  public void testSetTemplateReturnsDecorator() {
    TreeDecorator actualDecorator = decorator.setTemplate( new Template() );

    assertSame( decorator, actualDecorator );
  }

  @Test
  public void testSetsTemplateAsCustomData() {
    Template template = new Template();

    decorator.setTemplate( template );

    verify( tree ).setData( RWT.ROW_TEMPLATE, template );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNegativeItemHeight() {
    decorator.setItemHeight( -1 );
  }

  @Test
  public void testSetItemHeightReturnsDecorator() {
    TreeDecorator actualDecorator = decorator.setItemHeight( 1 );

    assertSame( decorator, actualDecorator );
  }

  @Test
  public void testSetItemHeightInWidget() {
    decorator.setItemHeight( 23 );

    verify( tree ).setData( RWT.CUSTOM_ITEM_HEIGHT, Integer.valueOf( 23 ) );
  }

}
