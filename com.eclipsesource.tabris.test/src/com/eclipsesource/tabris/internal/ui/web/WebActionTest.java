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
package com.eclipsesource.tabris.internal.ui.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;

import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.eclipsesource.tabris.internal.ui.ActionDescriptor;
import com.eclipsesource.tabris.internal.ui.ImageUtil;
import com.eclipsesource.tabris.internal.ui.RemoteActionTest;
import com.eclipsesource.tabris.internal.ui.TestAction;
import com.eclipsesource.tabris.test.RWTRunner;
import com.eclipsesource.tabris.ui.UI;


@RunWith( RWTRunner.class )
public class WebActionTest {

  private UI ui;
  private WebUI webUI;
  private ActionDescriptor actionDescriptor;
  private WebAction webAction;
  private Button control;

  @Before
  public void setUp() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Shell shell = new Shell( new Display() );
    ui = mock( UI.class );
    actionDescriptor = mockDescriptor();
    webUI = mock( WebUI.class );
    webAction = new WebAction( ui, webUI, actionDescriptor );
    webAction.createUi( shell );
    control = webAction.getControl();
  }

  @Test
  public void testGetUI() {
    assertSame( ui, webAction.getUI() );
  }

  @Test
  public void testGetDescriptor() {
    assertSame( actionDescriptor, webAction.getDescriptor() );
  }

  @Test
  public void testInitControl() {
    assertFalse( control.isEnabled() );
    assertFalse( control.isVisible() );
    assertNotNull( control.getImage() );
    assertEquals( "bar", control.getToolTipText() );
    assertTrue( control.isListening( SWT.Selection ) );
  }

  @Test
  public void testDestroy() {
    webAction.destroy();

    assertTrue( control.isDisposed() );
  }

  @Test
  public void testSetVisible() {
    webAction.setVisible( true );

    assertTrue( control.getVisible() );
    assertFalse( ( ( RowData )control.getLayoutData() ).exclude );
  }

  @Test
  public void testSetVisible_makesInvisible() {
    webAction.setVisible( false );

    assertFalse( control.getVisible() );
    assertTrue( ( ( RowData )control.getLayoutData() ).exclude );
  }

  @Test
  public void testSetVisible_doesRelayout() {
    webAction.setVisible( false );

    verify( webUI ).layout();
  }

  @Test
  public void testSetEnabled() {
    webAction.setEnabled( true );

    assertTrue( control.isEnabled() );
  }

  @Test
  public void testSelectionEvent_executeAction() {
    control.notifyListeners( SWT.Selection, new Event() );

    TestAction action = ( TestAction )actionDescriptor.getAction();
    assertTrue( action.wasExecuted() );
  }

  private ActionDescriptor mockDescriptor() {
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    when( descriptor.getAction() ).thenReturn( new TestAction() );
    InputStream image = RemoteActionTest.class.getResourceAsStream( "testImage.png" );
    when( descriptor.getImage() ).thenReturn( ImageUtil.getBytes( image ) );
    when( descriptor.getTitle() ).thenReturn( "bar" );
    doReturn( Boolean.FALSE ).when( descriptor ).isEnabled();
    doReturn( Boolean.FALSE ).when( descriptor ).isVisible();
    return descriptor;
  }

}
