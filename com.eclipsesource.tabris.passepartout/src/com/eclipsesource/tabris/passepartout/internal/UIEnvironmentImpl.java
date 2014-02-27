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

import static com.eclipsesource.tabris.passepartout.internal.Clauses.when;
import static com.eclipsesource.tabris.passepartout.internal.Clauses.whenNull;

import com.eclipsesource.tabris.passepartout.Bounds;
import com.eclipsesource.tabris.passepartout.UIEnvironment;


public class UIEnvironmentImpl implements UIEnvironment {

  private final Bounds bounds;
  private final Bounds referenceBounds;
  private final int parentFontSize;

  public UIEnvironmentImpl( Bounds bounds, Bounds referenceBounds, int parentFontSize ) {
    whenNull( bounds ).throwIllegalArgument( "Bounds must not be null" );
    whenNull( referenceBounds ).throwIllegalArgument( "Reference Bounds must not be null" );
    when( parentFontSize < 0 ).throwIllegalArgument( "Parent Fotn Size msut be >= 0 but was " + parentFontSize );
    this.bounds = bounds;
    this.referenceBounds = referenceBounds;
    this.parentFontSize = parentFontSize;
  }

  @Override
  public Bounds getParentBounds() {
    return bounds;
  }

  @Override
  public Bounds getReferenceBounds() {
    return referenceBounds;
  }

  @Override
  public int getParentFontSize() {
    return parentFontSize;
  }

}
