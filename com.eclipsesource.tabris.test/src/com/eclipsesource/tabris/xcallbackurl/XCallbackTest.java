package com.eclipsesource.tabris.xcallbackurl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import com.eclipsesource.tabris.test.RWTRunner;
import com.eclipsesource.tabris.test.TabrisTestUtil;


@RunWith( RWTRunner.class )
public class XCallbackTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullConfiguration() {
    new XCallback( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCallback() {
    XCallback xCallback = new XCallback( mock( XCallbackConfiguration.class ) );

    xCallback.addXCallbackListener( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithRemovingNullCallback() {
    XCallback xCallback = new XCallback( mock( XCallbackConfiguration.class ) );

    xCallback.removeXCallbackListener( null );
  }

  @Test
  public void testUsesConfigAsCallParameter() {
    RemoteObject remoteObject = TabrisTestUtil.mockRemoteObject();
    XCallbackConfiguration configuration = new XCallbackConfiguration( "foo", "bar" );
    XCallback xCallback = new XCallback( configuration );

    xCallback.call();

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( remoteObject ).call( eq( "call" ), captor.capture() );
    assertEquals( "foo", captor.getValue().get( "targetScheme" ).asString() );
    assertEquals( "bar", captor.getValue().get( "targetAction" ).asString() );
  }

  @Test
  public void testUsesConfigAsCallParameterWithXSource() {
    RemoteObject remoteObject = TabrisTestUtil.mockRemoteObject();
    XCallbackConfiguration configuration = new XCallbackConfiguration( "foo", "bar" );
    XCallback xCallback = new XCallback( configuration );
    configuration.setXSource( "foo" );

    xCallback.call();

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( remoteObject ).call( eq( "call" ), captor.capture() );
    assertEquals( "foo", captor.getValue().get( "xSource" ).asString() );
  }

  @Test
  public void testUsesConfigAsCallParameterWithActionParameters() {
    RemoteObject remoteObject = TabrisTestUtil.mockRemoteObject();
    XCallbackConfiguration configuration = new XCallbackConfiguration( "foo", "bar" );
    XCallback xCallback = new XCallback( configuration );
    configuration.addActionParameter( "foo1", "bar1" );

    xCallback.call();

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( remoteObject ).call( eq( "call" ), captor.capture() );
    JsonObject parameter = captor.getValue().get( "actionParameters" ).asObject();
    assertEquals( "bar1", parameter.get( "foo1" ).asString() );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testNotifiesCallbackOnSuccess() {
    XCallback xCallback = new XCallback( mock( XCallbackConfiguration.class ) );
    RemoteObject remoteObject = xCallback.getRemoteObject();
    XCallbackListener listener = mock( XCallbackListener.class );
    xCallback.addXCallbackListener( listener );
    JsonObject properties = new JsonObject();
    JsonObject parameters = new JsonObject();
    parameters.add( "foo", "bar" );
    properties.add( "parameters", parameters );

    TabrisTestUtil.dispatchNotify( remoteObject, "Success", properties );

    ArgumentCaptor<Map> captor = ArgumentCaptor.forClass( Map.class );
    verify( listener ).onSuccess( captor.capture() );
    assertEquals( 1, captor.getValue().size() );
    assertEquals( "bar", captor.getValue().get( "foo" ) );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testNotifiesAllCallbacksOnSuccess() {
    XCallback xCallback = new XCallback( mock( XCallbackConfiguration.class ) );
    RemoteObject remoteObject = xCallback.getRemoteObject();
    XCallbackListener listener = mock( XCallbackListener.class );
    XCallbackListener listener2 = mock( XCallbackListener.class );
    xCallback.addXCallbackListener( listener );
    xCallback.addXCallbackListener( listener2 );

    TabrisTestUtil.dispatchNotify( remoteObject, "Success", null );

    InOrder order = inOrder( listener, listener2 );
    order.verify( listener ).onSuccess( anyMap() );
    order.verify( listener2 ).onSuccess( anyMap() );
  }

  @Test
  public void testNotifiesCallbackOnError() {
    XCallback xCallback = new XCallback( mock( XCallbackConfiguration.class ) );
    RemoteObject remoteObject = xCallback.getRemoteObject();
    XCallbackListener listener = mock( XCallbackListener.class );
    xCallback.addXCallbackListener( listener );
    JsonObject parameters = new JsonObject();
    parameters.add( "errorCode", "42" );
    parameters.add( "errorMessage", "foo" );

    TabrisTestUtil.dispatchNotify( remoteObject, "Error", parameters );

    verify( listener ).onError( "42", "foo" );
  }

  @Test
  public void testNotifiesAllCallbacksOnError() {
    XCallback xCallback = new XCallback( mock( XCallbackConfiguration.class ) );
    RemoteObject remoteObject = xCallback.getRemoteObject();
    XCallbackListener listener = mock( XCallbackListener.class );
    XCallbackListener listener2 = mock( XCallbackListener.class );
    xCallback.addXCallbackListener( listener );
    xCallback.addXCallbackListener( listener2 );
    JsonObject parameters = new JsonObject();
    parameters.add( "errorCode", "42" );
    parameters.add( "errorMessage", "foo" );

    TabrisTestUtil.dispatchNotify( remoteObject, "Error", parameters );

    InOrder order = inOrder( listener, listener2 );
    order.verify( listener ).onError( "42", "foo" );
    order.verify( listener2 ).onError( "42", "foo" );
  }

  @Test
  public void testNotifiesCallbackOnCancel() {
    XCallback xCallback = new XCallback( mock( XCallbackConfiguration.class ) );
    RemoteObject remoteObject = xCallback.getRemoteObject();
    XCallbackListener listener = mock( XCallbackListener.class );
    xCallback.addXCallbackListener( listener );

    TabrisTestUtil.dispatchNotify( remoteObject, "Cancel", null );

    verify( listener ).onCancel();
  }

  @Test
  public void testNotifiesAllCallbacksOnCancel() {
    XCallback xCallback = new XCallback( mock( XCallbackConfiguration.class ) );
    RemoteObject remoteObject = xCallback.getRemoteObject();
    XCallbackListener listener = mock( XCallbackListener.class );
    XCallbackListener listener2 = mock( XCallbackListener.class );
    xCallback.addXCallbackListener( listener );
    xCallback.addXCallbackListener( listener2 );

    TabrisTestUtil.dispatchNotify( remoteObject, "Cancel", null );

    InOrder order = inOrder( listener, listener2 );
    order.verify( listener ).onCancel();
    order.verify( listener2 ).onCancel();
  }

  @Test
  public void testGetsRemoteObjectAsAdapter() {
    XCallback xCallback = new XCallback( mock( XCallbackConfiguration.class ) );

    RemoteObject adapter = xCallback.getAdapter( RemoteObject.class );

    assertSame( adapter, xCallback.getRemoteObject() );
  }

  @Test
  public void testGetsConfigurationAsAdapter() {
    XCallbackConfiguration configuration = mock( XCallbackConfiguration.class );
    XCallback xCallback = new XCallback( configuration );

    XCallbackConfiguration adapter = xCallback.getAdapter( XCallbackConfiguration.class );

    assertSame( adapter, configuration );
  }

  @Test
  public void testDisposeDestroysRemoteObject() {
    RemoteObject remoteObject = TabrisTestUtil.mockRemoteObject();
    XCallbackConfiguration configuration = mock( XCallbackConfiguration.class );
    XCallback xCallback = new XCallback( configuration );

    xCallback.dispose();

    verify( remoteObject ).destroy();
  }

  @Test( expected = IllegalStateException.class )
  public void testFaislWithDoubleDestroy() {
    XCallbackConfiguration configuration = mock( XCallbackConfiguration.class );
    XCallback xCallback = new XCallback( configuration );

    xCallback.dispose();
    xCallback.dispose();
  }

  @Test( expected = IllegalStateException.class )
  public void testFaislCallAfterDestroy() {
    XCallbackConfiguration configuration = mock( XCallbackConfiguration.class );
    XCallback xCallback = new XCallback( configuration );

    xCallback.dispose();
    xCallback.call();
  }

}