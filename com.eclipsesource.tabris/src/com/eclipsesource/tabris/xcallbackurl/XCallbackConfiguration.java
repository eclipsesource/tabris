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
package com.eclipsesource.tabris.xcallbackurl;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * An {@link XCallbackConfiguration} will be passed to the {@link XCallback} constructor and specifies which App needs
 * to be called. The x-callback-url specification defines additional parts for each call. These are:
 * <ul>
 * <li>The scheme of the targeting App <b>(mandatory)</b></li>
 * <li>The action of the targeting App <b>(mandatory)</b></li>
 * <li>The parameter for the action <b>(optional)</b></li>
 * <li>The x-source aka. means the friendly name of the calling App <b>(optional)</b></li>
 * </ul>
 * </p>
 *
 * @see XCallback
 *
 * @since 1.1
 */
public class XCallbackConfiguration implements Serializable {

  private final Map<String, String> actionParameters;
  private final String targetScheme;
  private final String targetAction;
  private String xSource;

  /**
   * <p>
   * Creates a new {@link XCallbackConfiguration} with the specified target scheme and action.
   * </p>
   */
  public XCallbackConfiguration( String targetScheme, String targetAction ) {
    validateString( targetScheme, "Target scheme" );
    validateString( targetAction, "Target action" );
    this.targetScheme = targetScheme;
    this.targetAction = targetAction;
    this.actionParameters = new HashMap<String, String>();
  }

  /**
   * <p>
   * Adds an action parameter to the {@link XCallbackConfiguration}.
   *
   * <b>Note:</b> The parameter must not be URL encoded. This is done by the framework.
   * </p>
   */
  public void addActionParameter( String name, String value ) {
    validateString( name, "Name" );
    validateString( value, "Value" );
    actionParameters.put( name, value );
  }

  public Map<String, String> getActionParameters() {
    return new HashMap<String, String>( actionParameters );
  }

  public String getTargetScheme() {
    return targetScheme;
  }

  public String getTargetAction() {
    return targetAction;
  }

  /**
   * <p>
   * The x-source attribute of a x-callback specifies the friendly name of the calling App. Many Apps are displaying
   * this name while they performing their task.
   * </p>
   */
  public void setXSource( String xSource ) {
    validateString( xSource, "XSource" );
    this.xSource = xSource;
  }

  public String getXSource() {
    return xSource;
  }

  private void validateString( String string, String name ) {
    whenNull( string ).throwIllegalArgument( name + " must not be null" );
    when( string.isEmpty() ).throwIllegalArgument( name + " must not be empty" );
  }
}