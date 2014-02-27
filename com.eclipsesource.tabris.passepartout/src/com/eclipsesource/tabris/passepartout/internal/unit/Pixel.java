package com.eclipsesource.tabris.passepartout.internal.unit;

import static com.eclipsesource.tabris.passepartout.internal.Clauses.when;

import java.math.BigDecimal;

import com.eclipsesource.tabris.passepartout.Unit;

public class Pixel implements Unit {

  private final BigDecimal value;

  public Pixel( int value ) {
    when( value < 0 ).throwIllegalArgument( "Percentage must be >= 0 but was " + value );
    this.value = BigDecimal.valueOf( value );
  }

  @Override
  public BigDecimal getValue() {
    return value;
  }
}