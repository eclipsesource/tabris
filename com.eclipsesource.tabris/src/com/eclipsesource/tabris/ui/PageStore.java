package com.eclipsesource.tabris.ui;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * A PageStore object can be used to store session specific data and pass them from one page to another. This enables
 * a loose communication between pages.
 * </p>
 *
 * @since 1.0
 */
public class PageStore {

  private final Map<String, Object> store;

  public PageStore() {
    this.store = new HashMap<String, Object>();
  }

  /**
   * <p>
   * Adds a value to this store. Already existing values will be overridden.
   * </p>
   * @param key key to identify the stored value. Must not be empty or <code>null</code>.
   * @param value the value to be stored. Must not be <code>null</code>.
   */
  public void set( String key, Object value ) {
    store.put( key, value );
  }

  /**
   * <p>
   * Adds all Values form the passed in store to this store.
   * </p>
   */
  public void addStore( PageStore store ) {
    this.store.putAll( store.getAll() );
  }

  /**
   * <p>
   * Gets a value from this store, already casted to the passed in type.
   * </p>
   *
   * @throws IllegalArgumentException when the stored value can not be casted to the passed in type.
   */
  @SuppressWarnings("unchecked")
  public <T> T get( String key, Class<T> type ) throws IllegalArgumentException {
    T result = null;
    Object value = store.get( key );
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
    return new HashMap<String, Object>( store );
  }
}
