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

import static com.eclipsesource.tabris.passepartout.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.passepartout.internal.UIEnvironmentFactory.createEnvironment;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

import com.eclipsesource.tabris.passepartout.Instruction;
import com.eclipsesource.tabris.passepartout.Resource;
import com.eclipsesource.tabris.passepartout.Rule;
import com.eclipsesource.tabris.passepartout.UIEnvironment;
import com.eclipsesource.tabris.passepartout.internal.instruction.BackgroundImageInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.BackgroundInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.FontInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.ForegroundInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.ImageInstruction;


public class ResourceImpl implements Resource, Listener {

  private final List<Rule> rules;
  private Widget[] widgets;

  public ResourceImpl( Rule... rules ) {
    whenNull( rules ).throwIllegalArgument( "Rules must not be null" );
    this.rules = new ArrayList<Rule>();
    for( Rule rule : rules ) {
      whenNull( rule ).throwIllegalArgument( "Rule must not be null" );
      this.rules.add( rule );
    }
  }

  @Override
  public void bindTo( Widget... widgets ) {
    whenNull( widgets ).throwIllegalArgument( "Widgets must not be null" );
    this.widgets = widgets;
    addResizeListener();
    apply();
  }

  private void addResizeListener() {
    Widget widget = widgets[ 0 ];
    whenNull( widget ).throwIllegalArgument( "Widget must not be null" );
    if( widget instanceof Control ) {
      Control control = ( Control )widget;
      if( control.getParent() != null ) {
        control.getParent().addListener( SWT.Resize, this );
      } else {
        control.getShell().addListener( SWT.Resize, this );
      }
    } else {
      Shell activeShell = widget.getDisplay().getShells()[ 0 ];
      activeShell.addListener( SWT.Resize, this );
    }
  }

  @Override
  public void handleEvent( Event event ) {
    apply();
  }

  private void apply() {
    for( Widget widget : widgets ) {
      whenNull( widget ).throwIllegalArgument( "Widget must not be null" );
      if( !widget.isDisposed() ) {
        UIEnvironment environment = createEnvironment( widget );
        InstructionExtractor extractor = new InstructionExtractor( environment );
        List<Instruction> instructions = extractor.extract( rules );
        applyInstructions( widget, instructions );
      }
    }
  }

  private void applyInstructions( Widget widget, List<Instruction> instructions ) {
    for( Instruction instruction : instructions ) {
      if( widget instanceof Control ) {
        applyInstruction( ( Control )widget, instruction );
      } else if( widget instanceof Item ) {
        applyInstruction( ( Item )widget, instruction );
      }
    }
  }

  private void applyInstruction( Control control, Instruction instruction ) {
    if( instruction instanceof FontInstruction ) {
      control.setFont( ( ( FontInstruction )instruction ).getFont() );
    } else if( instruction instanceof ForegroundInstruction ) {
      control.setForeground( ( ( ForegroundInstruction )instruction ).getColor() );
    } else if( instruction instanceof BackgroundInstruction ) {
      control.setBackground( ( ( BackgroundInstruction )instruction ).getColor() );
    } else if( instruction instanceof ImageInstruction ) {
      applyImageOnControl( control, ( ( ImageInstruction )instruction ).getImage() );
    } else if( instruction instanceof BackgroundImageInstruction ) {
      control.setBackgroundImage( ( ( BackgroundImageInstruction )instruction ).getImage() );
    }
  }

  private void applyImageOnControl( Control control, Image image ) {
    if( control instanceof Label ) {
      ( ( Label )control ).setImage( image );
    } else if( control instanceof Button ) {
      ( ( Button )control ).setImage( image );
    }
  }

  private void applyInstruction( Item item, Instruction instruction ) {
    if( instruction instanceof ImageInstruction ) {
      item.setImage( ( ( ImageInstruction )instruction ).getImage() );
    }
  }
}
