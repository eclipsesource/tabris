/*******************************************************************************
 * Copyright (c) 2016 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.rap.rwt.internal.lifecycle.RemoteAdapter;
import org.eclipse.swt.internal.widgets.WidgetRemoteAdapter;
import org.eclipse.swt.widgets.Composite;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.test.util.TabrisEnvironment;

@SuppressWarnings("restriction")
public class TabrisRemoteAdapterTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private WidgetRemoteAdapter compositeRemoteAdapter;
  private TabrisRemoteAdapter remoteAdapter;

  @Before
  public void setUp() {
    Composite composite = mock( Composite.class );
    compositeRemoteAdapter = mock( WidgetRemoteAdapter.class );
    when( composite.getAdapter( RemoteAdapter.class ) ).thenReturn( compositeRemoteAdapter );
    remoteAdapter = new TabrisRemoteAdapter<Composite>( composite );
  }

  @Test
  public void testMarkPreserved() {
    remoteAdapter.markPreserved( 3 );

    // bit logic does not affect other properties
    assertFalse( remoteAdapter.hasPreserved( 0 ) );
    assertFalse( remoteAdapter.hasPreserved( 1 ) );
    assertFalse( remoteAdapter.hasPreserved( 2 ) );
    assertTrue( remoteAdapter.hasPreserved( 3 ) );
  }

  @Test
  public void testMarkPreserved_isCleared() {
    remoteAdapter.markPreserved( 3 );

    remoteAdapter.clear();

    assertFalse( remoteAdapter.hasPreserved( 3 ) );
  }

  @Test
  public void testScheduleRender_addsRenderRunnable() throws Exception {
    remoteAdapter.scheduleRender( mock( Runnable.class ) );

    verify( compositeRemoteAdapter ).addRenderRunnable( any( Runnable.class ) );
  }

  @Test
  public void testScheduleRender_addsRenderRunnable_once() throws Exception {
    remoteAdapter.scheduleRender( mock( Runnable.class ) );
    remoteAdapter.scheduleRender( mock( Runnable.class ) );

    verify( compositeRemoteAdapter, times( 1 ) ).addRenderRunnable( any( Runnable.class ) );
  }

  @Test
  public void testScheduleRender_addsRenderRunnable_afterClear() throws Exception {
    remoteAdapter.scheduleRender( mock( Runnable.class ) );
    reset( compositeRemoteAdapter );

    remoteAdapter.clear();
    remoteAdapter.scheduleRender( mock( Runnable.class ) );

    verify( compositeRemoteAdapter, times( 1 ) ).addRenderRunnable( any( Runnable.class ) );
  }

  @Test
  public void testChanged_withBoolean_returnsTrue() throws Exception {
    assertTrue( remoteAdapter.changed( true, false ) );
  }

  @Test
  public void testChanged_withBoolean_returnsFalse() throws Exception {
    assertFalse( remoteAdapter.changed( true, true ) );
  }

  @Test
  public void testChanged_withObject_returnsTrue() throws Exception {
    assertTrue( remoteAdapter.changed( "foo", "bar" ) );
  }

  @Test
  public void testChanged_withObject_returnsFalse() throws Exception {
    assertFalse( remoteAdapter.changed( "foo", "foo" ) );
  }

  @Test
  public void testChanged_withNullObject_returnsTrue() throws Exception {
    assertTrue( remoteAdapter.changed( null, "bar" ) );
  }

}
