package com.eclipsesource.tabris.passepartout.internal.unit;
import static com.eclipsesource.tabris.passepartout.internal.Clauses.when;
import static com.eclipsesource.tabris.passepartout.internal.Clauses.whenNull;

import java.math.BigDecimal;

import com.eclipsesource.tabris.passepartout.Unit;

public class Em implements Unit {

  private final BigDecimal value;

  public Em( BigDecimal value ) {
    whenNull( value ).throwIllegalArgument( "Em must not be null" );
    when( value.compareTo( BigDecimal.ZERO ) <= 0 ).throwIllegalArgument( "Em must be > 0 but was " + value );
    this.value = value;
  }

  @Override
  public BigDecimal getValue() {
    return value;
  }
}