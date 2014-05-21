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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.template.Template;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;
import com.eclipsesource.tabris.widgets.enhancement.TreeDecorator.TreePart;


public class TreeDecoratorTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private Tree tree;
  private TreeDecorator decorator;
  private Display display;

  @Before
  public void setUp() {
    display = new Display();
    tree = new Tree( new Shell( display ), SWT.NONE );
    decorator = Widgets.onTree( tree );
  }

  @Test
  public void testUseTitle() {
    decorator.useTitle( "test" );

    assertEquals( "test", tree.getToolTipText() );
  }

  @Test
  public void testSetAlternativeLeafSelection() {
    decorator.enableAlternativeSelection( TreePart.LEAF );

    assertEquals( "leaf", tree.getData( ALT_SELECTION.getKey() ) );
  }

  @Test
  public void testSetAlternativeBranchSelection() {
    decorator.enableAlternativeSelection( TreePart.BRANCH );

    assertEquals( "branch", tree.getData( ALT_SELECTION.getKey() ) );
  }

  @Test
  public void testSetAlternativeSelectionForAll() {
    decorator.enableAlternativeSelection( TreePart.ALL );

    assertEquals( "all", tree.getData( ALT_SELECTION.getKey() ) );
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

    assertEquals( Integer.valueOf( 1 ), tree.getData( RWT.PRELOADED_ITEMS ) );
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

    assertSame( template, tree.getData( RWT.ROW_TEMPLATE ) );
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

    assertEquals( Integer.valueOf( 23 ), tree.getData( RWT.CUSTOM_ITEM_HEIGHT ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetRefreshHandlerFailsWithNullHandler() {
    decorator.setRefreshHandler( null );
  }

  @Test
  public void testSetRefreshHandlerReturnsDecorator() {
    RefreshHandler refreshHandler = mock( RefreshHandler.class );

    TreeDecorator actualDecorator = decorator.setRefreshHandler( refreshHandler );

    assertSame( decorator, actualDecorator );
  }

  @Test
  public void testSetRefreshHandlerHooksTree() {
    RefreshHandler refreshHandler = mock( RefreshHandler.class );

    decorator.setRefreshHandler( refreshHandler );

    verify( refreshHandler ).hookToWidget( tree );
  }

  @Test
  public void testSetRefreshHandlerSetsTreeData() {
    RefreshHandler refreshHandler = mock( RefreshHandler.class );
    when( refreshHandler.getId() ).thenReturn( "foo" );

    decorator.setRefreshHandler( refreshHandler );

    Object data = tree.getData( WhiteListEntry.REFRESH_HANDLER.getKey() );
    assertEquals( "foo", data );
  }

}
