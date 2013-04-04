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
package com.eclipsesource.tabris.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.io.Serializable;
import java.util.Map;

import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.tabris.interaction.AppLauncher;
import com.eclipsesource.tabris.interaction.LaunchOptions;
import com.eclipsesource.tabris.interaction.LaunchOptions.App;
import com.eclipsesource.tabris.interaction.MailOptions;
import com.eclipsesource.tabris.test.TabrisTestUtil;


public class AppLauncherTest {

  @Before
  public void setUp() {
    Fixture.setUp();
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( AppLauncherImpl.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullOptions() {
    AppLauncher launcher = new AppLauncherImpl();

    launcher.open( null );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testOpenCreatsCallOperation() {
    RemoteObject remoteObject = TabrisTestUtil.mockRemoteObject();
    AppLauncher launcher = new AppLauncherImpl();

    launcher.open( new MailOptions( "foo" ) );

    ArgumentCaptor<Map> captor = ArgumentCaptor.forClass( Map.class );
    verify( remoteObject ).call( eq( "open" ), captor.capture() );
    assertEquals( App.MAIL.toString(), captor.getValue().get( "app" ) );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testOpenCreatsCallOperationWithProperties() {
    RemoteObject remoteObject = TabrisTestUtil.mockRemoteObject();
    AppLauncher launcher = new AppLauncherImpl();
    LaunchOptions options = new MailOptions( "foo" );
    options.add( "foo", "bar" );
    options.add( "foo1", "bar1" );

    launcher.open( options );

    ArgumentCaptor<Map> captor = ArgumentCaptor.forClass( Map.class );
    verify( remoteObject ).call( eq( "open" ), captor.capture() );
    assertEquals( "bar", captor.getValue().get( "foo" ) );
    assertEquals( "bar1", captor.getValue().get( "foo1" ) );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testOpenUrlCreatesCallOperation() {
    RemoteObject remoteObject = TabrisTestUtil.mockRemoteObject();
    AppLauncher launcher = new AppLauncherImpl();

    launcher.openUrl( "http://foo.bar" );

    ArgumentCaptor<Map> captor = ArgumentCaptor.forClass( Map.class );
    verify( remoteObject ).call( eq( "openUrl" ), captor.capture() );
    assertEquals( "http://foo.bar", captor.getValue().get( "url" ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testOpenUrlFailsWithNull() {
    AppLauncher launcher = new AppLauncherImpl();

    launcher.openUrl( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testOpenUrlFailsWithInvalidUrl() {
    AppLauncher launcher = new AppLauncherImpl();

    launcher.openUrl( "fooBar" );
  }
}
