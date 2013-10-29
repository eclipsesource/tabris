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

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.ui.PageConfiguration;
import com.eclipsesource.tabris.ui.UIConfiguration;


public class UpdateUtilTest {

  @Before
  public void setUp() {
    Fixture.setUp();
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testRegistersUpdaterInUISession() {
    UIUpdater updater = mock( UIUpdater.class );

    UpdateUtil.registerUpdater( updater );

    Object attribute = RWT.getUISession().getAttribute( UpdateUtil.UPDATER_PROPERTY );
    assertSame( updater, attribute );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testRegisterFailsWithNullUpdater() {
    UpdateUtil.registerUpdater( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFirePageUpdateFailsWithNullUI() {
    UpdateUtil.firePageUpdate( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFireUIUpdateFailsWithNullPage() {
    UpdateUtil.fireUiUpdate( null );
  }

  @Test
  public void testNotifiesUpdaterAboutPageChange() {
    UIUpdater updater = mock( UIUpdater.class );
    UpdateUtil.registerUpdater( updater );
    PageConfiguration configuration = mock( PageConfiguration.class );

    UpdateUtil.firePageUpdate( configuration );

    verify( updater ).update( configuration );
  }

  @Test
  public void testNotifiesUpdaterAboutUIChange() {
    UIUpdater updater = mock( UIUpdater.class );
    UpdateUtil.registerUpdater( updater );
    UIConfiguration configuration = mock( UIConfiguration.class );

    UpdateUtil.fireUiUpdate( configuration );

    verify( updater ).update( configuration );
  }
}
