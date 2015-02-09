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

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.tabris.interaction.AppLauncher;
import com.eclipsesource.tabris.interaction.LaunchOptions;
import com.eclipsesource.tabris.interaction.LaunchOptions.App;
import com.eclipsesource.tabris.interaction.MailOptions;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;


public class AppLauncherTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

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
  public void testOpenCreatsCallOperation() {
    RemoteObject remoteObject = environment.getRemoteObject();
    AppLauncher launcher = new AppLauncherImpl();

    launcher.open( new MailOptions( "foo" ) );

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( remoteObject ).call( eq( "open" ), captor.capture() );
    assertEquals( JsonValue.valueOf( App.MAIL.toString() ), captor.getValue().get( "app" ) );
  }

  @Test
  public void testOpenCreatsCallOperationWithProperties() {
    RemoteObject remoteObject = environment.getRemoteObject();
    AppLauncher launcher = new AppLauncherImpl();
    LaunchOptions options = new MailOptions( "foo" );
    options.add( "foo", "bar" );
    options.add( "foo1", "bar1" );

    launcher.open( options );

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( remoteObject ).call( eq( "open" ), captor.capture() );
    assertEquals( JsonValue.valueOf( "bar" ), captor.getValue().get( "foo" ) );
    assertEquals( JsonValue.valueOf( "bar1" ), captor.getValue().get( "foo1" ) );
  }

  @Test
  public void testOpenUrlCreatesCallOperation() {
    RemoteObject remoteObject = environment.getRemoteObject();
    AppLauncher launcher = new AppLauncherImpl();

    launcher.openUrl( "http://foo.bar" );

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( remoteObject ).call( eq( "openUrl" ), captor.capture() );
    assertEquals( JsonValue.valueOf( "http://foo.bar" ), captor.getValue().get( "url" ) );
  }

  @Test
  public void testOpenUrlCreatesCallOperationWithCustomScheme() {
    RemoteObject remoteObject = environment.getRemoteObject();
    AppLauncher launcher = new AppLauncherImpl();

    launcher.openUrl( "tabris://foo.bar" );

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( remoteObject ).call( eq( "openUrl" ), captor.capture() );
    assertEquals( JsonValue.valueOf( "tabris://foo.bar" ), captor.getValue().get( "url" ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testOpenUrlFailsWithNull() {
    AppLauncher launcher = new AppLauncherImpl();

    launcher.openUrl( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testOpenUrlFailsWithEmptyUrl() {
    AppLauncher launcher = new AppLauncherImpl();

    launcher.openUrl( "" );
  }
}
