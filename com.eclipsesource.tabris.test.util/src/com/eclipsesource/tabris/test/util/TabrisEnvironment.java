package com.eclipsesource.tabris.test.util;
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


import static org.mockito.Mockito.mock;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.Client;
import org.eclipse.rap.rwt.internal.remote.ConnectionImpl;
import org.eclipse.rap.rwt.internal.service.ContextProvider;
import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rap.rwt.testfixture.TestRequest;
import org.eclipse.rap.rwt.testfixture.internal.engine.ThemeManagerHelper;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import com.eclipsesource.tabris.TabrisClient;
import com.eclipsesource.tabris.test.util.internal.RemoteObjectHelper;


/**
 * <b>Please Note:</b> This class is preliminary API and may change in future version
 */
@SuppressWarnings( { "restriction", "deprecation" } )
public class TabrisEnvironment implements TestRule {

  @Override
  public Statement apply( final Statement base, Description description ) {
    return new Statement() {

      @Override
      public void evaluate() throws Throwable {
        try {
          Fixture.setSkipResourceRegistration( true );
          Fixture.setUp();
          Fixture.fakePhase( PhaseId.PROCESS_ACTION );
          Fixture.fakeClient( mock( TabrisClient.class ) );
          RemoteObjectHelper.mockRemoteObject();
          base.evaluate();
        } catch( Throwable shouldNotHappen ) {
          throw shouldNotHappen;
        } finally {
          if( ContextProvider.hasContext() ) {
            Fixture.tearDown();
          }
        }
      }
    };
  }

  public RemoteObject getRemoteObject() {
    return RWT.getUISession().getConnection().createRemoteObject( "foo" );
  }

  public RemoteObject getServiceObject() {
    Connection connection = RWT.getUISession().getConnection();
    return ( ( ConnectionImpl )connection ).createServiceObject( "foo" );
  }

  public TabrisRequest getRequest() {
    return new TabrisRequest( ( TestRequest )RWT.getRequest() );
  }

  public void resetThemes() {
    ThemeManagerHelper.resetThemeManager();
  }

  public void setClient( Client client ) {
    Fixture.fakeClient( client );
  }

  public void dispatchSet( JsonObject properties ) {
    ( ( TestRemoteObject )getRemoteObject() ).getHandler().handleSet( properties );
  }

  public void dispatchNotify( String eventName, JsonObject properties ) {
    ( ( TestRemoteObject )getRemoteObject() ).getHandler().handleNotify( eventName, properties );
  }

  public void dispatchCall( String methodName, JsonObject parameters ) {
    ( ( TestRemoteObject )getRemoteObject() ).getHandler().handleCall( methodName, parameters );
  }

  public void dispatchSetOnServiceObject( JsonObject properties ) {
    ( ( TestRemoteObject )getServiceObject() ).getHandler().handleSet( properties );
  }

  public void dispatchNotifyOnServiceObject( String eventName, JsonObject properties ) {
    ( ( TestRemoteObject )getServiceObject() ).getHandler().handleNotify( eventName, properties );
  }

  public void dispatchCallOnServiceObject( String methodName, JsonObject parameters ) {
    ( ( TestRemoteObject )getServiceObject() ).getHandler().handleCall( methodName, parameters );
  }

  public TabrisRequest newRequest() {
    TabrisRequest request = new TabrisRequest( Fixture.fakeNewRequest() );
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    return request;
  }

  public TabrisRequest newGetRequest() {
    TabrisRequest request = new TabrisRequest( Fixture.fakeNewGetRequest() );
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    return request;
  }

  public Image getTestImage() {
    return new Image( Display.getCurrent(), Fixture.class.getResourceAsStream( "/" + Fixture.IMAGE1 ) );
  }

}
