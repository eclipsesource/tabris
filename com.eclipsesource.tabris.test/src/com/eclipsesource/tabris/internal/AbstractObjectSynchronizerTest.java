package com.eclipsesource.tabris.internal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rwt.Adaptable;
import org.eclipse.rwt.internal.protocol.ClientObjectAdapter;
import org.eclipse.rwt.internal.protocol.IClientObject;
import org.eclipse.rwt.internal.protocol.IClientObjectAdapter;
import org.eclipse.rwt.lifecycle.PhaseEvent;
import org.eclipse.rwt.lifecycle.PhaseId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;


@SuppressWarnings("restriction")
public class AbstractObjectSynchronizerTest {
	
	private FakeObjectSynchronizer synchronizer;
  private Adaptable object;

  @Before
	public void setUp() {
    Fixture.setUp();
	  object = mock( Adaptable.class );
	  when( object.getAdapter( IClientObjectAdapter.class ) ).thenReturn( new ClientObjectAdapter() );
    FakeObjectSynchronizer original = new FakeObjectSynchronizer( object );
    synchronizer = spy( original );
	}
  
  @After
  public void tearDown() {
    Fixture.tearDown();
  }
  
  @Test
  public void testPhaseId() {
    assertEquals( PhaseId.ANY, synchronizer.getPhaseId() );
  }
  
  @Test
  public void testDispatchesPrepareUIRoot() {
    PhaseEvent event = mock( PhaseEvent.class );
    when( event.getPhaseId() ).thenReturn( PhaseId.PREPARE_UI_ROOT );
    
    synchronizer.afterPhase( event );
    synchronizer.afterPhase( event );
    
    verify( synchronizer, times( 1 ) ).renderInitialization( any( IClientObject.class ), eq( object ) );
  }
  
  @Test
  public void testDispatchesreadData() {
    PhaseEvent event = mock( PhaseEvent.class );
    when( event.getPhaseId() ).thenReturn( PhaseId.READ_DATA );
    
    synchronizer.afterPhase( event );
    
    InOrder order = inOrder( synchronizer );
    order.verify( synchronizer ).readData( eq( object ) );
    order.verify( synchronizer ).preserveValues( eq( object ) );
  }
  
  @Test
  public void testDispatchesProcessAction() {
    PhaseEvent event = mock( PhaseEvent.class );
    when( event.getPhaseId() ).thenReturn( PhaseId.PROCESS_ACTION );
    
    synchronizer.afterPhase( event );
    
    verify( synchronizer ).processAction( eq( object ) );
  }
  
  @Test
  public void testDispatchesRenderChanges() {
    PhaseEvent event = mock( PhaseEvent.class );
    when( event.getPhaseId() ).thenReturn( PhaseId.RENDER );
    
    synchronizer.afterPhase( event );
    
    verify( synchronizer ).renderChanges( eq( object ) );
  }
  
}
