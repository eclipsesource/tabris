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

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.print.PrintOptions.OutputType.COLOR;
import static com.eclipsesource.tabris.print.PrintOptions.Quality.NORMAL;

import java.io.Serializable;


/**
 * @since 1.4
 */
public class PrintOptions implements Serializable {

  private final String url;
  private String jobName;
  private String printer;
  private boolean showPageRange;
  private boolean showNumberOfCopies;
  private OutputType outputType;
  private boolean duplex;
  private Quality quality;

  public static enum OutputType {
    COLOR, PHOTO, GRAYSCALE;
  }

  public static enum Quality {
    LOW, NORMAL, HIGH;
  }

  public PrintOptions( String url ) {
    whenNull( url ).throwIllegalArgument( "URL must not be null." );
    when( url.isEmpty() ).throwIllegalArgument( "URL must not be empty" );
    this.url = url;
    this.showNumberOfCopies = true;
    this.showPageRange = true;
    this.duplex = false;
    this.outputType = COLOR;
    this.quality = NORMAL;
  }

  public String getURL() {
    return url;
  }

  public PrintOptions setJobName( String jobName ) {
    this.jobName = jobName;
    return this;
  }

  public String getJobName() {
    return jobName;
  }

  public PrintOptions setPrinter( String printer ) {
    this.printer = printer;
    return this;
  }

  public String getPrinter() {
    return printer;
  }

  public PrintOptions setOutputType( OutputType outputType ) {
    whenNull( outputType ).throwIllegalArgument( "OutputType must not be null." );
    this.outputType = outputType;
    return this;
  }

  public OutputType getOutputType() {
    return outputType;
  }

  public PrintOptions setQuality( Quality quality ) {
    whenNull( quality ).throwIllegalArgument( "Quality must not be null." );
    this.quality = quality;
    return this;
  }

  public Quality getQuality() {
    return quality;
  }

  public PrintOptions setShowNumberOfCopies( boolean showNumberOfCopies ) {
    this.showNumberOfCopies = showNumberOfCopies;
    return this;
  }

  public boolean showNumberOfCopies() {
    return showNumberOfCopies;
  }

  public PrintOptions setShowPageRange( boolean showPageRange ) {
    this.showPageRange = showPageRange;
    return this;
  }

  public boolean showPageRange() {
    return showPageRange;
  }

  public PrintOptions setDuplex( boolean duplex ) {
    this.duplex = duplex;
    return this;
  }

  public boolean isDuplex() {
    return duplex;
  }

}