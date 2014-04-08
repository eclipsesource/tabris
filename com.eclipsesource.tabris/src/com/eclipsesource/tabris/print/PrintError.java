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
package com.eclipsesource.tabris.print;

import java.io.Serializable;


/**
 * @since 1.4
 */
public class PrintError implements Serializable {

  private final String message;
  private final String printer;
  private final String jobName;

  public PrintError( String printer, String jobName, String message ) {
    this.printer = printer;
    this.jobName = jobName;
    this.message = message;
  }

  public String getPrinter() {
    return printer;
  }

  public String getJobName() {
    return jobName;
  }

  public String getMessage() {
    return message;
  }

}
