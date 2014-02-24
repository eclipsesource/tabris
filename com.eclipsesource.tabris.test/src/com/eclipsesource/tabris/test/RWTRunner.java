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
package com.eclipsesource.tabris.test;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;


public class RWTRunner extends BlockJUnit4ClassRunner {

  public RWTRunner( Class<?> type ) throws InitializationError {
    super( type );
  }

  @Override
  protected void runChild( FrameworkMethod method, RunNotifier notifier ) {
    try {
      Fixture.setUp();
      super.runChild( method, notifier );
    } finally {
      Fixture.tearDown();
    }
  }

}
