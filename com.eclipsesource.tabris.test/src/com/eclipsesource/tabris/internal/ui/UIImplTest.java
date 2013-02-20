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
package com.eclipsesource.tabris.internal.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.ui.ActionConfiguration;
import com.eclipsesource.tabris.ui.PageConfiguration;
import com.eclipsesource.tabris.ui.TransitionListener;
import com.eclipsesource.tabris.ui.UI;


public class UIImplTest {

  @Before
  public void setUp() {
    Fixture.setUp();
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullRemoteUI() {
    new UIImpl( null );
  }

  @Test( expected = IllegalStateException.class )
  public void testAddPageFailsWithDuplicateId() {
    UIImpl ui = new UIImpl( mock( RemoteUI.class ) );
    PageConfiguration configuration1 = PageConfiguration.newPage( "foo", TestPage.class );
    PageConfiguration configuration2 = PageConfiguration.newPage( "foo", TestPage.class );

    ui.addPage( configuration1 );
    ui.addPage( configuration2 );
  }


  @Test( expected = IllegalArgumentException.class )
  public void testAddPageFailsWithNullConfiguration() {
    UIImpl ui = new UIImpl( mock( RemoteUI.class ) );

    ui.addPage( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testAddActionFailsWithNullConfiguration() {
    UIImpl ui = new UIImpl( mock( RemoteUI.class ) );

    ui.addAction( null );
  }

  @Test( expected = IllegalStateException.class )
  public void testAddActionFailsWithDuplicateId() {
    UIImpl ui = new UIImpl( mock( RemoteUI.class ) );
    ActionConfiguration configuration1 = ActionConfiguration.newAction( "foo", TestAction.class );
    ActionConfiguration configuration2 = ActionConfiguration.newAction( "foo", TestAction.class );

    ui.addAction( configuration1 );
    ui.addAction( configuration2 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testAddTransitionListenerFailsWithNullListener() {
    UIImpl ui = new UIImpl( mock( RemoteUI.class ) );

    ui.addTransitionListener( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testremoveTransitionListenerFailsWithNullListener() {
    UIImpl ui = new UIImpl( mock( RemoteUI.class ) );

    ui.removeTransitionListener( null );
  }

  @Test
  public void testAddActionReturnsSameUiInstance() {
    UIImpl ui = new UIImpl( mock( RemoteUI.class ) );

    UI actualUI = ui.addAction( ActionConfiguration.newAction( "foo", TestAction.class ) );

    assertSame( ui, actualUI );
  }

  @Test
  public void testAddActionWithProminenceReturnsSameUiInstance() {
    UIImpl ui = new UIImpl( mock( RemoteUI.class ) );

    UI actualUI = ui.addAction( ActionConfiguration.newAction( "foo", TestAction.class ) );

    assertSame( ui, actualUI );
  }

  @Test
  public void testAddPageDoesNotReturnNull() {
    UIImpl ui = new UIImpl( mock( RemoteUI.class ) );
    PageConfiguration configuration = PageConfiguration.newPage( "foo", TestPage.class ).setTopLevel( true );

    UI actualUI = ui.addPage( configuration );

    assertNotNull( actualUI );
  }

  @Test
    public void testGetDescriptorHolderIsNotSafeCopy() {
      UIImpl ui = new UIImpl( mock( RemoteUI.class ) );

      DescriptorHolder contentHolder1 = ui.getDescriptorHolder();
      DescriptorHolder contentHolder2 = ui.getDescriptorHolder();

      assertSame( contentHolder1, contentHolder2 );
      assertNotNull( contentHolder1 );
    }

  @Test
  public void testAddsPageToHolder() {
    UIImpl ui = new UIImpl( mock( RemoteUI.class ) );
    PageConfiguration configuration = PageConfiguration.newPage( "foo", TestPage.class ).setTopLevel( true );

    ui.addPage( configuration );

    PageDescriptor actualDescriptor = ui.getDescriptorHolder().getPageDescriptor( "foo" );
    assertNotNull( actualDescriptor );
  }

  @Test
  public void testAddsActionToHolder() {
    UIImpl ui = new UIImpl( mock( RemoteUI.class ) );

    ui.addAction( ActionConfiguration.newAction( "foo", TestAction.class ) );

    assertNotNull( ui.getDescriptorHolder().getActionDescriptor( "foo" ) );
  }

  @Test
  public void testAddsActionWitProminenceToHolder() {
    UIImpl ui = new UIImpl( mock( RemoteUI.class ) );

    ui.addAction( ActionConfiguration.newAction( "foo", TestAction.class ) );

    assertNotNull( ui.getDescriptorHolder().getActionDescriptor( "foo" ) );
  }

  @Test
  public void testAddsTransitionListener() {
    UIImpl ui = new UIImpl( mock( RemoteUI.class ) );
    TransitionListener listener = mock( TransitionListener.class );

    ui.addTransitionListener( listener );

    List<TransitionListener> listeners = ui.getTransitionListeners();
    assertTrue( listeners.contains( listener ) );
    assertEquals( 1, listeners.size() );
  }

  @Test
  public void testAddsTransitionListenerReturnsUI() {
    UIImpl ui = new UIImpl( mock( RemoteUI.class ) );
    TransitionListener listener = mock( TransitionListener.class );

    UI actualUI = ui.addTransitionListener( listener );

    assertSame( ui, actualUI );
  }

  @Test
  public void testRemovesTransitionListener() {
    UIImpl ui = new UIImpl( mock( RemoteUI.class ) );
    TransitionListener listener = mock( TransitionListener.class );
    ui.addTransitionListener( listener );

    ui.removeTransitionListener( listener );

    List<TransitionListener> listeners = ui.getTransitionListeners();
    assertTrue( listeners.isEmpty() );
  }

  @Test
  public void testSetsForeground() {
    Display display = new Display();
    RemoteUI remoteUI = mock( RemoteUI.class );
    UIImpl ui = new UIImpl( remoteUI );
    Color color = new Color( display, 233, 233, 233 );

    ui.setForeground( color );

    verify( remoteUI ).setForeground( color );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetForegroundFailsWithNull() {
    RemoteUI remoteUI = mock( RemoteUI.class );
    UIImpl ui = new UIImpl( remoteUI );

    ui.setForeground( null );
  }

  @Test
  public void testSetsBackground() {
    Display display = new Display();
    RemoteUI remoteUI = mock( RemoteUI.class );
    UIImpl ui = new UIImpl( remoteUI );
    Color color = new Color( display, 233, 233, 233 );

    ui.setBackground( color );

    verify( remoteUI ).setBackground( color );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetBackgroundFailsWithNull() {
    RemoteUI remoteUI = mock( RemoteUI.class );
    UIImpl ui = new UIImpl( remoteUI );

    ui.setBackground( null );
  }

}
