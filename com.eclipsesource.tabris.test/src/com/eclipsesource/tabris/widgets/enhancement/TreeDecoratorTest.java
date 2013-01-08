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

import static com.eclipsesource.tabris.internal.WidgetsUtil.TABRIS_VARIANT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

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
  
  private static final String VARIANT_BACK_FOCUS = "BACK_FOCUS";
  
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
    
    verify( tree ).setData( eq( TABRIS_VARIANT ), eq( "ALT_SELECTION_LEAF" ) );
  }
  
  @Test
  public void testSetAlternativeBranchSelection() {
    decorator.enableAlternativeSelection( TreePart.BRANCH );
    
    verify( tree ).setData( eq( TABRIS_VARIANT ), eq( "ALT_SELECTION_BRANCH" ) );
  }
  
  @Test
  public void testSetAlternativeSelectionForAll() {
    decorator.enableAlternativeSelection( TreePart.ALL );
    
    verify( tree ).setData( eq( TABRIS_VARIANT ), eq( "ALT_SELECTION" ) );
  }
  
  @Test
  public void testSetBackButtonFocusShouldSetBackFocusVariant() {
    Shell shell = new Shell( display );
    Tree focusTree = new Tree( shell, SWT.NONE );
    TreeDecorator treeDecorator = Widgets.onTree( focusTree );
    
    treeDecorator.enableBackButtonNavigation();
        
    assertEquals( VARIANT_BACK_FOCUS, focusTree.getData( TABRIS_VARIANT ) );
  }
  
  @Test
  public void testSetBackButtonFocusShouldSetNullVariantOnOtherTreesWithBackFocusVariant() {
    Shell shell = new Shell( display );
    Tree tree1 = new Tree( shell, SWT.NONE );
    Tree tree2 = new Tree( shell, SWT.NONE );
    Tree tree3 = new Tree( shell, SWT.NONE );
    tree2.setData( TABRIS_VARIANT, VARIANT_BACK_FOCUS );
    tree3.setData( TABRIS_VARIANT, "anyVariant" );
    TreeDecorator treeDecorator = Widgets.onTree( tree1 );
    
    treeDecorator.enableBackButtonNavigation();
    
    assertEquals( VARIANT_BACK_FOCUS, tree1.getData( TABRIS_VARIANT ) );
    assertNull( VARIANT_BACK_FOCUS, tree2.getData( TABRIS_VARIANT ) );
    assertEquals( "anyVariant", tree3.getData( TABRIS_VARIANT ) );
  }

}
