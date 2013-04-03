package com.eclipsesource.tabris.ui;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * A {@link PageData} object can be used to store page specific data and pass them from one page to another.
 * This enables a loose communication between pages.
 * </p>
 *
 * @since 1.0
 */
public class PageData {

  private final Map<String, Object> data;

  public PageData() {
    this.data = new HashMap<String, Object>();
  }

  /**
   * <p>
   * Adds a value to this data object. Already existing values will be overridden.
   * </p>
   * @param key key to identify the data value. Must not be empty or <code>null</code>.
   * @param value the value to be stored. Must not be <code>null</code>.
   */
  public void set( String key, Object value ) {
    data.put( key, value );
  }

  /**
   * <p>
   * Adds all values from the provided data to this data.
   * </p>
   */
  public void addData( PageData data ) {
    this.data.putAll( data.getAll() );
  }

  /**
   * <p>
   * Returns a value from this data object, already casted to the provided type.
   * </p>
   *
   * @throws IllegalArgumentException when the data value can not be casted to the passed in type.
   */
  @SuppressWarnings("unchecked")
  public <T> T get( String key, Class<T> type ) throws IllegalArgumentException {
    T result = null;
    Object value = data.get( key );
    if( value != null ) {
      if( type.isAssignableFrom( value.getClass() ) ) {
        result = ( T )value;
      } else {
        throw new IllegalArgumentException( "Value with key " + key + " is not of type " + type.getName() );
      }
    }
    return result;
  }

  Map<String, Object> getAll() {
    return new HashMap<String, Object>( data );
  }
}
