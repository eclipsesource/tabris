package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.internal.Constants.PROPERTY_DATA;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.client.WidgetDataWhiteList;
import org.eclipse.rap.rwt.internal.lifecycle.IRenderRunnable;
import org.eclipse.rap.rwt.internal.protocol.ClientObjectFactory;
import org.eclipse.rap.rwt.internal.protocol.IClientObject;
import org.eclipse.rap.rwt.lifecycle.PhaseEvent;
import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.lifecycle.PhaseListener;
import org.eclipse.rap.rwt.lifecycle.WidgetAdapter;
import org.eclipse.swt.internal.widgets.IDisplayAdapter;
import org.eclipse.swt.internal.widgets.WidgetAdapterImpl;
import org.eclipse.swt.internal.widgets.WidgetTreeVisitor;
import org.eclipse.swt.internal.widgets.WidgetTreeVisitor.AllWidgetTreeVisitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;


// Workaround for bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=402521
// TODO: Remove when bug is fixed
@SuppressWarnings("restriction")
public class ItemDataRenderer implements PhaseListener {

  @Override
  public void beforePhase( PhaseEvent event ) {
    visitAllWidgets( new AllWidgetTreeVisitor() {
      @Override
      public boolean doVisit( Widget widget ) {
        attachRenderRunnable( widget );
        return true;
      }
    } );
  }

  @Override
  public void afterPhase( PhaseEvent event ) {
    // do nothing
  }

  @Override
  public PhaseId getPhaseId() {
    return PhaseId.RENDER;
  }

  private void attachRenderRunnable( Widget widget ) {
    WidgetAdapterImpl adapter = ( WidgetAdapterImpl )widget.getAdapter( WidgetAdapter.class );
    if( !( widget instanceof Control ) && !adapter.isInitialized() ) {
      adapter.setRenderRunnable( new WidgetDataRenderRunnable( widget ) );
    }
  }

  private void visitAllWidgets( WidgetTreeVisitor visitor ) {
    Shell[] shells = getShells( Display.getCurrent() );
    for( int i = 0; i < shells.length; i++ ) {
      Composite shell = shells[ i ];
      WidgetTreeVisitor.accept( shell, visitor );
    }
  }

  private Shell[] getShells( Display display ) {
    return display.getAdapter( IDisplayAdapter.class ).getShells();
  }

  private class WidgetDataRenderRunnable implements IRenderRunnable {

    private final Widget widget;

    public WidgetDataRenderRunnable( Widget widget ) {
      this.widget = widget;
    }

    @Override
    public void afterRender() throws IOException {
      if( widget != null ) {
        renderData( widget );
      }
    }

    private void renderData( Widget widget ) {
      Object[] newValue = getDataAsArray( widget );
      if( newValue.length != 0 ) {
        IClientObject clientObject = ClientObjectFactory.getClientObject( widget );
        Map<Object, Object> data = new HashMap<Object, Object>();
        for( int i = 0; i < newValue.length; i++ ) {
          data.put( newValue[ i ], newValue[ ++i ] );
        }
        clientObject.set( PROPERTY_DATA, data );
      }
    }

    private Object[] getDataAsArray( Widget widget ) {
      List<Object> result = new ArrayList<Object>();
      WidgetDataWhiteList service = RWT.getClient().getService( WidgetDataWhiteList.class );
      String[] dataKeys = service == null ? null : service.getKeys();
      if( dataKeys != null ) {
        for( String key : dataKeys ) {
          if( key != null ) {
            Object value = widget.getData( key );
            if( value != null ) {
              result.add( key );
              result.add( value );
            }
          }
        }
      }
      return result.toArray();
    }

  }

}
