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
package com.eclipsesource.tabris.test;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.remote.ConnectionImpl;
import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import com.eclipsesource.tabris.test.TabrisTestUtil.TestRemoteObject;


@SuppressWarnings( { "restriction", "deprecation" } )
public class RWTEnvironment implements TestRule {

  @Override
  public Statement apply( final Statement base, Description description ) {
    return new Statement() {

      @Override
      public void evaluate() throws Throwable {
        try {
          Fixture.setSkipResourceRegistration( true );
          Fixture.setUp();
          Fixture.fakePhase( PhaseId.PROCESS_ACTION );
          TabrisTestUtil.mockRemoteObject();
          base.evaluate();
        } catch( Throwable shouldNotHappen ) {
          throw shouldNotHappen;
        } finally {
          Fixture.tearDown();
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

  public void runProcessAction() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
  }
}
