/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.camera;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;

import org.junit.Test;

import com.eclipsesource.tabris.print.PrintOptions;
import com.eclipsesource.tabris.print.PrintOptions.OutputType;

public class PrintOptionsTest {

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( PrintOptions.class ) );
  }

  @Test
  public void testSetsPrinter() {
    PrintOptions printOptions = new PrintOptions();

    printOptions.setPrinter( "printer" );

    assertEquals( "printer", printOptions.getPrinter() );
  }

  @Test
  public void testSetPrinterReturnsOptions() {
    PrintOptions printOptions = new PrintOptions();

    PrintOptions actualOptions = printOptions.setPrinter( "printer" );

    assertSame( printOptions, actualOptions );
  }

  @Test
  public void testSetsPrinterToNull() {
    PrintOptions printOptions = new PrintOptions();

    printOptions.setPrinter( "printer" );
    printOptions.setPrinter( null );

    assertNull( printOptions.getPrinter() );
  }

  @Test
  public void testDefaultPrinterIsNull() {
    PrintOptions printOptions = new PrintOptions();

    String printer = printOptions.getPrinter();

    assertNull( printer );
  }

  @Test
  public void testSetsJobName() {
    PrintOptions printOptions = new PrintOptions();

    printOptions.setJobName( "Job Name" );

    assertEquals( "Job Name", printOptions.getJobName() );
  }

  @Test
  public void testSetJobNameReturnsOptions() {
    PrintOptions printOptions = new PrintOptions();

    PrintOptions actualOptions = printOptions.setJobName( "Job Name" );

    assertSame( printOptions, actualOptions );
  }

  @Test
  public void testSetsJobNameToNull() {
    PrintOptions printOptions = new PrintOptions();

    printOptions.setJobName( "Job Name" );
    printOptions.setJobName( null );

    assertNull( printOptions.getJobName() );
  }

  @Test
  public void testDefaultJobNameIsNull() {
    PrintOptions printOptions = new PrintOptions();

    String jobName = printOptions.getJobName();

    assertNull( jobName );
  }

  @Test
  public void testSetDuplexReturnsOptions() {
    PrintOptions printOptions = new PrintOptions();

    PrintOptions actualOptions = printOptions.setDuplex( true );

    assertSame( printOptions, actualOptions );
  }

  @Test
  public void testSetsDuplex() {
    PrintOptions printOptions = new PrintOptions();

    printOptions.setDuplex( true );

    assertTrue( printOptions.isDuplex() );
  }

  @Test
  public void testDefaultDuplexIsFalse() {
    PrintOptions printOptions = new PrintOptions();

    boolean duplex = printOptions.isDuplex();

    assertFalse( duplex );
  }

  @Test
  public void testSetShowNumberOfCopiesReturnsOptions() {
    PrintOptions printOptions = new PrintOptions();

    PrintOptions actualOptions = printOptions.setShowNumberOfCopies( false );

    assertSame( printOptions, actualOptions );
  }

  @Test
  public void testSetsShowNumberOfCopiesToFalse() {
    PrintOptions printOptions = new PrintOptions();

    printOptions.setShowNumberOfCopies( false );

    assertFalse( printOptions.showNumberOfCopies() );
  }

  @Test
  public void testDefaultShowNumberOfCopiesIsTrue() {
    PrintOptions printOptions = new PrintOptions();

    boolean showNumberOfCopies = printOptions.showNumberOfCopies();

    assertTrue( showNumberOfCopies );
  }

  @Test
  public void testSetShowPageRangeReturnsOptions() {
    PrintOptions printOptions = new PrintOptions();

    PrintOptions actualOptions = printOptions.setShowPageRange( false );

    assertSame( printOptions, actualOptions );
  }

  @Test
  public void testSetsShowPageRangeToFalse() {
    PrintOptions printOptions = new PrintOptions();

    printOptions.setShowPageRange( false );

    assertFalse( printOptions.showPageRange() );
  }

  @Test
  public void testDefaultShowPageRangeIsTrue() {
    PrintOptions printOptions = new PrintOptions();

    boolean showPageRange = printOptions.showPageRange();

    assertTrue( showPageRange );
  }

  @Test
  public void testSetQualityReturnsOptions() {
    PrintOptions printOptions = new PrintOptions();

    PrintOptions actualOptions = printOptions.setQuality( PrintOptions.Quality.LOW );

    assertSame( printOptions, actualOptions );
  }

  @Test
  public void testSetsQualityToLow() {
    PrintOptions printOptions = new PrintOptions();

    printOptions.setQuality( PrintOptions.Quality.LOW );

    assertEquals( PrintOptions.Quality.LOW, printOptions.getQuality() );
  }

  @Test
  public void testSetsQualityToNormal() {
    PrintOptions printOptions = new PrintOptions();

    printOptions.setQuality( PrintOptions.Quality.NORMAL );

    assertEquals( PrintOptions.Quality.NORMAL, printOptions.getQuality() );
  }

  @Test
  public void testSetsQualityToHigh() {
    PrintOptions printOptions = new PrintOptions();

    printOptions.setQuality( PrintOptions.Quality.HIGH );

    assertEquals( PrintOptions.Quality.HIGH, printOptions.getQuality() );
  }

  @Test
  public void testDefaultQualityIsNormal() {
    PrintOptions printOptions = new PrintOptions();

    PrintOptions.Quality quality = printOptions.getQuality();

    assertEquals( PrintOptions.Quality.NORMAL, quality );
  }

  @Test
  public void testSetOutputTypeReturnsOptions() {
    PrintOptions printOptions = new PrintOptions();

    PrintOptions actualOptions = printOptions.setOutputType( OutputType.COLOR );

    assertSame( printOptions, actualOptions );
  }

  @Test
  public void testSetsOutputTypeToColor() {
    PrintOptions printOptions = new PrintOptions();

    printOptions.setOutputType( OutputType.COLOR );

    assertEquals( OutputType.COLOR, printOptions.getOutputType() );
  }

  @Test
  public void testSetsOutputTypeToGraysale() {
    PrintOptions printOptions = new PrintOptions();

    printOptions.setOutputType( OutputType.GRAYSCALE );

    assertEquals( OutputType.GRAYSCALE, printOptions.getOutputType() );
  }

  @Test
  public void testSetsOutputTypeToPhoto() {
    PrintOptions printOptions = new PrintOptions();

    printOptions.setOutputType( OutputType.PHOTO );

    assertEquals( OutputType.PHOTO, printOptions.getOutputType() );
  }

  @Test
  public void testDefaultOutputTypeIsColor() {
    PrintOptions printOptions = new PrintOptions();

    OutputType outputType = printOptions.getOutputType();

    assertEquals( OutputType.COLOR, outputType );
  }
}