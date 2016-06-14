/*******************************************************************************
 * Copyright (c) 2013, 2016 EclipseSource and others.
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

import java.io.Serializable;

import org.eclipse.rap.rwt.internal.lifecycle.LifeCycleFactory;
import org.eclipse.rap.rwt.internal.lifecycle.SimpleLifeCycle;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.test.util.TabrisEnvironment;


@SuppressWarnings("restriction")
public class TabrisUIEntryPointTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  @Before
  public void setUp() {
    LifeCycleFactory lifeCycleFactory = getApplicationContext().getLifeCycleFactory();
    lifeCycleFactory.configure( SimpleLifeCycle.class );
    lifeCycleFactory.activate();
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

}
