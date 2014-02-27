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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class RelayoutListenerTest {

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
  public void testFailsWithNullComposite() {
    new RelayoutListener( null );
  }

  @Test
  public void testRelayoutsCompositeOnEvent() {
    TestComposite composite = new TestComposite( shell );
    RelayoutListener relayoutListener = new RelayoutListener( composite );

    relayoutListener.handleEvent( mock( Event.class ) );

    assertTrue( composite.wasLayouted );
  }

  @Test
  public void testDoesNotRelayoutsCompositeWhenDisposed() {
    TestComposite composite = new TestComposite( shell );
    RelayoutListener relayoutListener = new RelayoutListener( composite );

    composite.dispose();
    relayoutListener.handleEvent( mock( Event.class ) );

    assertFalse( composite.wasLayouted );
  }

  private static class TestComposite extends Composite {

    boolean wasLayouted;

    TestComposite( Composite parent ) {
      super( parent, SWT.NONE );
    }

    @Override
    public void layout( boolean changed, boolean all ) {
      super.layout( changed, all );
      wasLayouted = true;
    }

  }
}
