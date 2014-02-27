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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.passepartout.PassePartout;
import com.eclipsesource.tabris.passepartout.Rule;
import com.eclipsesource.tabris.passepartout.internal.condition.AlwaysTrueContidtion;
import com.eclipsesource.tabris.passepartout.internal.instruction.BackgroundImageInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.BackgroundInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.FontInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.ForegroundInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.ImageInstruction;


public class ResourceImplTest {

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
  public void testFailsWithNullRules() {
    Rule[] rules = null;

    new ResourceImpl( rules );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullRule() {
    Rule[] rules = new Rule[] { mock( Rule.class ), null, mock( Rule.class ) };

    new ResourceImpl( rules );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testBindToFailsWithNullWidgets() {
    Rule[] rules = new Rule[] { mock( Rule.class ), mock( Rule.class ) };
    ResourceImpl resource = new ResourceImpl( rules );
    Widget[] widgets = null;

    resource.bindTo( widgets );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testBindToFailsWithNullWidget() {
    Rule[] rules = new Rule[] { mock( Rule.class ), mock( Rule.class ) };
    ResourceImpl resource = new ResourceImpl( rules );
    Widget[] widgets = new Widget[] { shell, null };

    resource.bindTo( widgets );
  }

  @Test
  public void testAppliesFont() {
    Font font = FontDescriptor.createFrom( shell.getDisplay().getSystemFont() ).setHeight( 44 ).createFont( shell.getDisplay() );
    Rule rule = PassePartout.when( new AlwaysTrueContidtion() ).then( new FontInstruction( font ) );
    ResourceImpl resource = new ResourceImpl( rule );

    resource.bindTo( shell );

    assertEquals( font, shell.getFont() );
  }

  @Test
  public void testAppliesForeground() {
    Color color = shell.getDisplay().getSystemColor( SWT.COLOR_BLUE );
    Rule rule = PassePartout.when( new AlwaysTrueContidtion() ).then( new ForegroundInstruction( color ) );
    ResourceImpl resource = new ResourceImpl( rule );

    resource.bindTo( shell );

    assertEquals( color, shell.getForeground() );
  }

  @Test
  public void testAppliesBackground() {
    Color color = shell.getDisplay().getSystemColor( SWT.COLOR_BLUE );
    Rule rule = PassePartout.when( new AlwaysTrueContidtion() ).then( new BackgroundInstruction( color ) );
    ResourceImpl resource = new ResourceImpl( rule );

    resource.bindTo( shell );

    assertEquals( color, shell.getBackground() );
  }

  @Test
  public void testAppliesBackgroundImage() {
    Image image = new Image( shell.getDisplay(), Fixture.class.getResourceAsStream( "/" + Fixture.IMAGE1 ) );
    Rule rule = PassePartout.when( new AlwaysTrueContidtion() ).then( new BackgroundImageInstruction( image ) );
    ResourceImpl resource = new ResourceImpl( rule );

    resource.bindTo( shell );

    assertEquals( image, shell.getBackgroundImage() );
  }

  @Test
  public void testAppliesImageOnLabel() {
    Label label = new Label( shell, SWT.NONE );
    Image image = new Image( shell.getDisplay(), Fixture.class.getResourceAsStream( "/" + Fixture.IMAGE1 ) );
    Rule rule = PassePartout.when( new AlwaysTrueContidtion() ).then( new ImageInstruction( image ) );
    ResourceImpl resource = new ResourceImpl( rule );

    resource.bindTo( label );

    assertEquals( image, label.getImage() );
  }

  @Test
  public void testAppliesImageOnButton() {
    Button button = new Button( shell, SWT.PUSH );
    Image image = new Image( shell.getDisplay(), Fixture.class.getResourceAsStream( "/" + Fixture.IMAGE1 ) );
    Rule rule = PassePartout.when( new AlwaysTrueContidtion() ).then( new ImageInstruction( image ) );
    ResourceImpl resource = new ResourceImpl( rule );

    resource.bindTo( button );

    assertEquals( image, button.getImage() );
  }

  @Test
  public void testAppliesImageOnItem() {
    ToolBar toolBar = new ToolBar( shell, SWT.NONE );
    ToolItem toolItem = new ToolItem( toolBar, SWT.NONE );
    Image image = new Image( shell.getDisplay(), Fixture.class.getResourceAsStream( "/" + Fixture.IMAGE1 ) );
    Rule rule = PassePartout.when( new AlwaysTrueContidtion() ).then( new ImageInstruction( image ) );
    ResourceImpl resource = new ResourceImpl( rule );
    shell.open();

    resource.bindTo( toolItem );

    assertEquals( image, toolItem.getImage() );
  }

  @Test
  public void testRegistersItselfAsResizeListener() {
    Color color = shell.getDisplay().getSystemColor( SWT.COLOR_BLUE );
    Rule rule = PassePartout.when( new AlwaysTrueContidtion() ).then( new ForegroundInstruction( color ) );
    ResourceImpl resource = new ResourceImpl( rule );

    resource.bindTo( shell );

    boolean hasListener = hasResizeListener( resource );
    assertTrue( hasListener );
  }

  @Test
  public void testRegistersItselfAsResizeListenerWithItem() {
    ToolBar toolBar = new ToolBar( shell, SWT.NONE );
    ToolItem toolItem = new ToolItem( toolBar, SWT.NONE );
    Color color = shell.getDisplay().getSystemColor( SWT.COLOR_BLUE );
    Rule rule = PassePartout.when( new AlwaysTrueContidtion() ).then( new ForegroundInstruction( color ) );
    ResourceImpl resource = new ResourceImpl( rule );

    resource.bindTo( toolItem );

    boolean hasListener = hasResizeListener( resource );
    assertTrue( hasListener );
  }

  private boolean hasResizeListener( ResourceImpl resource ) {
    boolean hasListener = false;
    Listener[] listeners = shell.getListeners( SWT.Resize );
    for( Listener listener : listeners ) {
      if( listener == resource ) {
        hasListener = true;
      }
    }
    return hasListener;
  }
}
