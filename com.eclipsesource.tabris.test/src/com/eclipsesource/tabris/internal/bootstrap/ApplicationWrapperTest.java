/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal.bootstrap;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.eclipse.rap.rwt.internal.application.ApplicationContext;
import org.eclipse.rap.rwt.internal.application.ApplicationImpl;
import org.junit.Test;


@SuppressWarnings("restriction")
public class ApplicationWrapperTest {
  
  @Test
  public void testCreatesWithRightContext() {
    ApplicationImpl application = mock( ApplicationImpl.class );
    ApplicationConfiguration configuration = mock( ApplicationConfiguration.class );
    
    new ApplicationWrapper( application, configuration );
    
    verify( application ).getAdapter( eq( ApplicationContext.class ) );
  }
}
