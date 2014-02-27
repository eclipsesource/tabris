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

import static com.eclipsesource.tabris.passepartout.PassePartout.px;
import static com.eclipsesource.tabris.passepartout.test.PassPartoutTestUtil.createEnvironment;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.InOrder;

import com.eclipsesource.tabris.passepartout.Bounds;
import com.eclipsesource.tabris.passepartout.Query;
import com.eclipsesource.tabris.passepartout.QueryListener;
import com.eclipsesource.tabris.passepartout.internal.condition.AlwaysTrueContidtion;
import com.eclipsesource.tabris.passepartout.internal.condition.MaxWidthCondition;
import com.eclipsesource.tabris.passepartout.internal.condition.MinWidthCondition;


public class QueryNotifierTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToAddListenerWithNullQuery() {
    QueryNotifier notifier = new QueryNotifier();

    notifier.addQueryListener( null, mock( QueryListener.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToAddListenerWithNullListener() {
    QueryNotifier notifier = new QueryNotifier();

    notifier.addQueryListener( mock( Query.class ), null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToRemoveListenerWithNullQuery() {
    QueryNotifier notifier = new QueryNotifier();

    notifier.removeQueryListener( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFaislToRemoveListenerWithNullQuery() {
    QueryNotifier notifier = new QueryNotifier();

    notifier.removeQueryListener( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFaislToNotifyWithNullBounds() {
    QueryNotifier notifier = new QueryNotifier();

    notifier.notifyListeners( null );
  }

  @Test
  public void testNotifiesListenerActivated() {
    QueryNotifier notifier = new QueryNotifier();
    QueryListener listener = mock( QueryListener.class );
    QueryImpl query = new QueryImpl( new AlwaysTrueContidtion() );
    notifier.addQueryListener( query, listener );

    notifier.notifyListeners( createEnvironment( new Bounds( 0, 0, 0, 0 ) ) );

    verify( listener ).activated( query );
  }

  @Test
  public void testNotifiesMultipleValidListenerActivated() {
    QueryNotifier notifier = new QueryNotifier();
    QueryListener listener = mock( QueryListener.class );
    QueryListener listener2 = mock( QueryListener.class );
    QueryImpl query = new QueryImpl( new AlwaysTrueContidtion() );
    Query query2 = new QueryImpl( new AlwaysTrueContidtion() ).and( new MaxWidthCondition( px( 99 ) ) );
    notifier.addQueryListener( query, listener );
    notifier.addQueryListener( query2, listener2 );

    notifier.notifyListeners( createEnvironment( new Bounds( 0, 0, 90, 0 ) ) );

    verify( listener ).activated( query );
    verify( listener2 ).activated( query2 );
  }

  @Test
  public void testNotifiesListenerActivatedOnce() {
    QueryNotifier notifier = new QueryNotifier();
    QueryListener listener = mock( QueryListener.class );
    QueryImpl query = new QueryImpl( new AlwaysTrueContidtion() );
    notifier.addQueryListener( query, listener );

    notifier.notifyListeners( createEnvironment( new Bounds( 0, 0, 0, 0 ) ) );
    notifier.notifyListeners( createEnvironment( new Bounds( 0, 0, 0, 0 ) ) );

    verify( listener, times( 1 ) ).activated( query );
  }

  @Test
  public void testDeactivatesListenerOnQueryChange() {
    QueryNotifier notifier = new QueryNotifier();
    QueryListener listener = mock( QueryListener.class );
    QueryImpl query = new QueryImpl( new MaxWidthCondition( px( 100 ) ) );
    notifier.addQueryListener( query, listener );

    notifier.notifyListeners( createEnvironment( new Bounds( 0, 0, 99, 0 ) ) );
    notifier.notifyListeners( createEnvironment( new Bounds( 0, 0, 199, 0 ) ) );

    InOrder order = inOrder( listener );
    order.verify( listener ).activated( query );
    order.verify( listener ).deactivated( query );
  }

  @Test
  public void testDeactivatesListenerOnQueryChangeAndActivatesValidOne() {
    QueryNotifier notifier = new QueryNotifier();
    QueryListener listener = mock( QueryListener.class );
    QueryListener listener2 = mock( QueryListener.class );
    QueryImpl query = new QueryImpl( new MaxWidthCondition( px( 100 ) ) );
    QueryImpl query2 = new QueryImpl( new MinWidthCondition( px( 101 ) ) );
    notifier.addQueryListener( query, listener );
    notifier.addQueryListener( query2, listener2 );

    notifier.notifyListeners( createEnvironment( new Bounds( 0, 0, 99, 0 ) ) );
    notifier.notifyListeners( createEnvironment( new Bounds( 0, 0, 199, 0 ) ) );
    notifier.notifyListeners( createEnvironment( new Bounds( 0, 0, 99, 0 ) ) );

    InOrder order = inOrder( listener, listener2 );
    order.verify( listener ).activated( query );
    order.verify( listener ).deactivated( query );
    order.verify( listener2 ).activated( query2 );
    order.verify( listener2 ).deactivated( query2 );
    order.verify( listener ).activated( query );
  }
}
