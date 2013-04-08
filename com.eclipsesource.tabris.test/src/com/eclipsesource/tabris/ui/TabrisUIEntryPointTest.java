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
package com.eclipsesource.tabris.ui;

import static org.eclipse.rap.rwt.internal.service.ContextProvider.getApplicationContext;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.Serializable;

import org.eclipse.rap.rwt.internal.application.ApplicationContextImpl;
import org.eclipse.rap.rwt.internal.lifecycle.LifeCycle;
import org.eclipse.rap.rwt.internal.lifecycle.LifeCycleFactory;
import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.lifecycle.PhaseListener;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.internal.TabrisClient;


@SuppressWarnings("restriction")
public class TabrisUIEntryPointTest {

  @Before
  public void setUp() {
    Fixture.setUp();
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    LifeCycleFactory lifeCycleFactory = getApplicationContext().getLifeCycleFactory();
    lifeCycleFactory.configure( TestLifeCycle.class );
    lifeCycleFactory.activate();
    Fixture.fakeClient( new TabrisClient() );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( TabrisUIEntryPoint.class ) );
  }

  @Test
  public void testCreateTabrisUI() {
    TabrisUI tabrisUI = mock( TabrisUI.class );
    TabrisUIEntryPoint entryPoint = new TabrisUIEntryPoint( tabrisUI );

    entryPoint.createUI();

    verify( tabrisUI ).create( any( Shell.class ) );
  }

  static class TestLifeCycle extends LifeCycle {
    public TestLifeCycle( ApplicationContextImpl applicationContext ) {
      super( applicationContext );
    }
    @Override
    public void execute() throws IOException {
    }
    @Override
    public void requestThreadExec( Runnable runnable ) {
    }
    @Override
    public void addPhaseListener( PhaseListener phaseListener ) {
    }
    @Override
    public void removePhaseListener( PhaseListener phaseListener ) {
    }
    @Override
    public void sleep() {
    }
  }
}
