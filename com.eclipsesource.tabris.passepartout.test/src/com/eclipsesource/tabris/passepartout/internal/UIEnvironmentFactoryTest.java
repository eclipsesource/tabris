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
package com.eclipsesource.tabris.passepartout.internal;

import static org.junit.Assert.assertEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.passepartout.Bounds;
import com.eclipsesource.tabris.passepartout.UIEnvironment;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;


public class UIEnvironmentFactoryTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private Shell shell;

  @Before
  public void setUp() {
    shell = new Shell( new Display() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullWidget() {
    UIEnvironmentFactory.createEnvironment( null );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsWithDisposedWidget() {
    shell.dispose();

    UIEnvironmentFactory.createEnvironment( shell );
  }

  @Test
  public void testUsesDisplayAsReferenceForShell() {
    UIEnvironment environment = UIEnvironmentFactory.createEnvironment( shell );

    Bounds referenceBounds = environment.getReferenceBounds();

    assertEquals( BoundsUtil.getBounds( shell.getDisplay().getBounds() ), referenceBounds );
  }

  @Test
  public void testUsesShellAsReferenceForControl() {
    Composite composite = new Composite( shell, SWT.NONE );
    UIEnvironment environment = UIEnvironmentFactory.createEnvironment( composite );

    Bounds referenceBounds = environment.getReferenceBounds();

    assertEquals( BoundsUtil.getBounds( shell.getBounds() ), referenceBounds );
  }

  @Test
  public void testUsesDisplayAsReferenceForItem() {
    ToolBar toolBar = new ToolBar( shell, SWT.NONE );
    ToolItem item = new ToolItem( toolBar, SWT.NONE );
    UIEnvironment environment = UIEnvironmentFactory.createEnvironment( item );

    Bounds referenceBounds = environment.getReferenceBounds();

    assertEquals( BoundsUtil.getBounds( shell.getDisplay().getBounds() ), referenceBounds );
  }

  @Test
  public void testUsesDisplayAsParentForShell() {
    UIEnvironment environment = UIEnvironmentFactory.createEnvironment( shell );

    Bounds parentBounds = environment.getParentBounds();

    assertEquals( BoundsUtil.getBounds( shell.getDisplay().getBounds() ), parentBounds );
  }

  @Test
  public void testUsesParentAsParentForControl() {
    Composite composite = new Composite( shell, SWT.NONE );
    UIEnvironment environment = UIEnvironmentFactory.createEnvironment( composite );

    Bounds parentBounds = environment.getParentBounds();

    assertEquals( BoundsUtil.getBounds( shell.getBounds() ), parentBounds );
  }

  @Test
  public void testUsesShellAsReferenceForItem() {
    ToolBar toolBar = new ToolBar( shell, SWT.NONE );
    ToolItem item = new ToolItem( toolBar, SWT.NONE );
    UIEnvironment environment = UIEnvironmentFactory.createEnvironment( item );

    Bounds parentBounds = environment.getParentBounds();

    assertEquals( BoundsUtil.getBounds( shell.getBounds() ), parentBounds );
  }


  @Test
  public void testUsesSystemFontForParentFontSizeWithoutParent() {
    UIEnvironment environment = UIEnvironmentFactory.createEnvironment( shell );

    int parentFontSize = environment.getParentFontSize();

    int height = shell.getDisplay().getSystemFont().getFontData()[ 0 ].getHeight();
    assertEquals( height, parentFontSize );
  }

  @Test
  public void testUsesParentForParentFontSizeForControl() {
    Composite composite = new Composite( shell, SWT.NONE );
    UIEnvironment environment = UIEnvironmentFactory.createEnvironment( composite );

    int parentFontSize = environment.getParentFontSize();

    int height = shell.getFont().getFontData()[ 0 ].getHeight();
    assertEquals( height, parentFontSize );
  }
}
