package com.eclipsesource.tabris.xcallbackurl;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;


public class XCallbackConfigurationTest {

  @Test( expected = IllegalArgumentException.class )
  public void testNullScheme() {
    new XCallbackConfiguration( null, "action" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testEmptyScheme() {
    new XCallbackConfiguration( "", "action" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testNullAction() {
    new XCallbackConfiguration( "target", null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testEmptyAction() {
    new XCallbackConfiguration( "target", "" );
  }

  @Test
  public void testGetScheme() {
    XCallbackConfiguration configuration = new XCallbackConfiguration( "target", "action" );

    String targetScheme = configuration.getTargetScheme();

    assertEquals( "target", targetScheme );
  }

  @Test
  public void testGetTargetAction() {
    XCallbackConfiguration configuration = new XCallbackConfiguration( "target", "action" );

    String targetAction = configuration.getTargetAction();

    assertEquals( "action", targetAction );
  }

  @Test
  public void testGetSource() {
    XCallbackConfiguration configuration = new XCallbackConfiguration( "target", "action" );

    configuration.setXSource( "source" );

    String xSource = configuration.getXSource();
    assertEquals( "source", xSource );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetSourceFailsWithNull() {
    XCallbackConfiguration configuration = new XCallbackConfiguration( "target", "action" );

    configuration.setXSource( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetSourceFailsWithEmptySource() {
    XCallbackConfiguration configuration = new XCallbackConfiguration( "target", "action" );

    configuration.setXSource( "" );
  }

  @Test
  public void testAddParameters() {
    XCallbackConfiguration configuration = new XCallbackConfiguration( "target", "action" );
    configuration.addActionParameter( "foo1", "bar1" );
    configuration.addActionParameter( "foo2", "bar2" );

    Map<String, String> actionParameters = configuration.getActionParameters();
    assertEquals( 2, actionParameters.size() );
    assertEquals( "bar1", actionParameters.get( "foo1" ) );
    assertEquals( "bar2", actionParameters.get( "foo2" ) );
  }

  @Test
  public void testAddParametersIsASafeCopy() {
    XCallbackConfiguration configuration = new XCallbackConfiguration( "target", "action" );
    configuration.addActionParameter( "foo1", "bar1" );

    Map<String, String> actionParameters = configuration.getActionParameters();
    configuration.addActionParameter( "foo2", "bar2" );

    assertEquals( 1, actionParameters.size() );
    assertEquals( "bar1", actionParameters.get( "foo1" ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testAddParameterFailsWithNullKey() {
    XCallbackConfiguration configuration = new XCallbackConfiguration( "target", "action" );

    configuration.addActionParameter( null, "bar1" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testAddParameterFailsWithEmptyKey() {
    XCallbackConfiguration configuration = new XCallbackConfiguration( "target", "action" );

    configuration.addActionParameter( "", "bar1" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testAddParameterFailsWithNullValue() {
    XCallbackConfiguration configuration = new XCallbackConfiguration( "target", "action" );

    configuration.addActionParameter( "foo1", null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testAddParameterFailsWithEmptyValue() {
    XCallbackConfiguration configuration = new XCallbackConfiguration( "target", "action" );

    configuration.addActionParameter( "foo1", "" );
  }

}