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

import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.print.PrintOptions.OutputType.COLOR;
import static com.eclipsesource.tabris.print.PrintOptions.Quality.NORMAL;

import java.io.Serializable;


/**
 * <p>
 * {@link PrintOptions} are used when submitting print jobs to the {@link Printer} service. The options will be
 * transfered to the client and are used to configure the print job.
 * </p>
 *
 * @see Printer
 *
 * @since 1.4
 */
public class PrintOptions implements Serializable {

  /**
   * <p>
   * The output type to send to the printer.
   * </p>
   */
  public static enum OutputType {
    COLOR, PHOTO, GRAYSCALE;
  }

  /**
   * <p>
   * The quality to send to the printer.
   *  </p>
   */
  public static enum Quality {
    LOW, NORMAL, HIGH;
  }

  private String jobName;
  private String printer;
  private boolean showPageRange;
  private boolean showNumberOfCopies;
  private OutputType outputType;
  private boolean duplex;
  private Quality quality;

  public PrintOptions() {
    this.showNumberOfCopies = true;
    this.showPageRange = true;
    this.duplex = false;
    this.outputType = COLOR;
    this.quality = NORMAL;
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