package com.eclipsesource.tabris.test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import java.io.IOException;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rap.rwt.testfixture.Message;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.internal.events.ActivateAdapter;
import org.eclipse.swt.internal.events.ActivateEvent;
import org.eclipse.swt.widgets.Control;


@SuppressWarnings("restriction")
public class ControlLCATestUtil {

  public static void testActivateListener( Control control ) throws IOException {
    Fixture.markInitialized( control.getDisplay() );
    Fixture.markInitialized( control );
    testRenderAddActivateListener( control );
    testRenderRemoveActivateListener( control );
    testRenderActivateListenerUnchanged( control );
  }

  public static void testFocusListener( Control control ) throws IOException {
    Fixture.markInitialized( control.getDisplay() );
    Fixture.markInitialized( control );
    testRenderAddFocusListener( control );
    testRenderRemoveFocusListener( control );
    testRenderFocusListenerUnchanged( control );
  }

  public static void testMouseListener( Control control ) throws IOException {
    Fixture.markInitialized( control.getDisplay() );
    Fixture.markInitialized( control );
    testRenderAddMouseListener( control );
    testRenderRemoveMouseListener( control );
    testRenderMouseListenerUnchanged( control );
  }

  public static void testKeyListener( Control control ) throws IOException {
    Fixture.markInitialized( control.getDisplay() );
    Fixture.markInitialized( control );
    testRenderAddKeyListener( control );
    testRenderRemoveKeyListener( control );
    testRenderKeyListenerUnchanged( control );
  }

  public static void testTraverseListener( Control control ) throws IOException {
    Fixture.markInitialized( control.getDisplay() );
    Fixture.markInitialized( control );
    testRenderAddTraverseListener( control );
    testRenderRemoveTraverseListener( control );
    testRenderTraverseListenerUnchanged( control );
  }

  public static void testMenuDetectListener( Control control ) throws IOException {
    Fixture.markInitialized( control.getDisplay() );
    Fixture.markInitialized( control );
    testRenderAddMenuDetectListener( control );
    testRenderRemoveMenuDetectListener( control );
    testRenderMenuDetectListenerUnchanged( control );
  }

  public static void testHelpListener( Control control ) throws IOException {
    Fixture.markInitialized( control.getDisplay() );
    Fixture.markInitialized( control );
    testRenderAddHelpListener( control );
    testRenderRemoveHelpListener( control );
    testRenderHelpListenerUnchanged( control );
  }

  private static void testRenderAddActivateListener( Control control ) throws IOException {
    ActivateAdapter listener = new ActivateAdapter() {};
    Fixture.fakeNewRequest( control.getDisplay() );
    Fixture.preserveWidgets();

    ActivateEvent.addListener( control, listener );
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    assertEquals( Boolean.TRUE, message.findListenProperty( control, "activate" ) );

    ActivateEvent.removeListener( control, listener );
  }

  private static void testRenderRemoveActivateListener( Control control ) throws IOException {
    ActivateAdapter listener = new ActivateAdapter() {};
    ActivateEvent.addListener( control, listener );
    Fixture.fakeNewRequest( control.getDisplay() );
    Fixture.preserveWidgets();

    ActivateEvent.removeListener( control, listener );
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    assertEquals( Boolean.FALSE, message.findListenProperty( control, "activate" ) );
  }

  private static void testRenderActivateListenerUnchanged( Control control ) throws IOException {
    ActivateAdapter listener = new ActivateAdapter() {};
    Fixture.fakeNewRequest( control.getDisplay() );
    Fixture.preserveWidgets();

    ActivateEvent.addListener( control, listener );
    Fixture.preserveWidgets();
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    assertNull( message.findListenOperation( control, "activate" ) );

    ActivateEvent.removeListener( control, listener );
  }

  private static void testRenderAddFocusListener( Control control ) throws IOException {
    FocusAdapter listener = new FocusAdapter() {};
    Fixture.fakeNewRequest( control.getDisplay() );
    Fixture.preserveWidgets();

    control.addFocusListener( listener );
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    assertEquals( Boolean.TRUE, message.findListenProperty( control, "focus" ) );

    control.removeFocusListener( listener );
  }

  private static void testRenderRemoveFocusListener( Control control ) throws IOException {
    FocusAdapter listener = new FocusAdapter() {};
    control.addFocusListener( listener );
    Fixture.fakeNewRequest( control.getDisplay() );
    Fixture.preserveWidgets();

    control.removeFocusListener( listener );
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    assertEquals( Boolean.FALSE, message.findListenProperty( control, "focus" ) );
  }

  private static void testRenderFocusListenerUnchanged( Control control ) throws IOException {
    FocusAdapter listener = new FocusAdapter() {};
    Fixture.fakeNewRequest( control.getDisplay() );
    Fixture.preserveWidgets();

    control.addFocusListener( listener );
    Fixture.preserveWidgets();
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    assertNull( message.findListenOperation( control, "focus" ) );

    control.removeFocusListener( listener );
  }

  private static void testRenderAddMouseListener( Control control ) throws IOException {
    MouseAdapter listener = new MouseAdapter() {};
    Fixture.fakeNewRequest( control.getDisplay() );
    Fixture.preserveWidgets();

    control.addMouseListener( listener );
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    assertEquals( Boolean.TRUE, message.findListenProperty( control, "mouse" ) );

    control.removeMouseListener( listener );
  }

  private static void testRenderRemoveMouseListener( Control control ) throws IOException {
    MouseAdapter listener = new MouseAdapter() {};
    control.addMouseListener( listener );
    Fixture.fakeNewRequest( control.getDisplay() );
    Fixture.preserveWidgets();

    control.removeMouseListener( listener );
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    assertEquals( Boolean.FALSE, message.findListenProperty( control, "mouse" ) );
  }

  private static void testRenderMouseListenerUnchanged( Control control ) throws IOException {
    MouseAdapter listener = new MouseAdapter() {};
    Fixture.fakeNewRequest( control.getDisplay() );
    Fixture.preserveWidgets();

    control.addMouseListener( listener );
    Fixture.preserveWidgets();
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    assertNull( message.findListenOperation( control, "mouse" ) );

    control.removeMouseListener( listener );
  }

  private static void testRenderAddKeyListener( Control control ) throws IOException {
    KeyAdapter listener = new KeyAdapter() {};
    Fixture.fakeNewRequest( control.getDisplay() );
    Fixture.preserveWidgets();

    control.addKeyListener( listener );
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    assertEquals( Boolean.TRUE, message.findListenProperty( control, "key" ) );

    control.removeKeyListener( listener );
  }

  private static void testRenderRemoveKeyListener( Control control ) throws IOException {
    KeyAdapter listener = new KeyAdapter() {};
    control.addKeyListener( listener );
    Fixture.fakeNewRequest( control.getDisplay() );
    Fixture.preserveWidgets();

    control.removeKeyListener( listener );
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    assertEquals( Boolean.FALSE, message.findListenProperty( control, "key" ) );
  }

  private static void testRenderKeyListenerUnchanged( Control control ) throws IOException {
    KeyAdapter listener = new KeyAdapter() {};
    Fixture.fakeNewRequest( control.getDisplay() );
    Fixture.preserveWidgets();

    control.addKeyListener( listener );
    Fixture.preserveWidgets();
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    assertNull( message.findListenOperation( control, "key" ) );

    control.removeKeyListener( listener );
  }

  private static void testRenderAddTraverseListener( Control control ) throws IOException {
    TraverseListener listener = new TraverseListener() {
      public void keyTraversed( TraverseEvent e ) {
      }
    };
    Fixture.fakeNewRequest( control.getDisplay() );
    Fixture.preserveWidgets();

    control.addTraverseListener( listener );
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    assertEquals( Boolean.TRUE, message.findListenProperty( control, "traverse" ) );

    control.removeTraverseListener( listener );
  }

  private static void testRenderRemoveTraverseListener( Control control ) throws IOException {
    TraverseListener listener = new TraverseListener() {
      public void keyTraversed( TraverseEvent e ) {
      }
    };
    control.addTraverseListener( listener );
    Fixture.fakeNewRequest( control.getDisplay() );
    Fixture.preserveWidgets();

    control.removeTraverseListener( listener );
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    assertEquals( Boolean.FALSE, message.findListenProperty( control, "traverse" ) );
  }

  private static void testRenderTraverseListenerUnchanged( Control control ) throws IOException {
    TraverseListener listener = new TraverseListener() {
      public void keyTraversed( TraverseEvent e ) {
      }
    };
    Fixture.fakeNewRequest( control.getDisplay() );
    Fixture.preserveWidgets();

    control.addTraverseListener( listener );
    Fixture.preserveWidgets();
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    assertNull( message.findListenOperation( control, "traverse" ) );

    control.removeTraverseListener( listener );
  }

  private static void testRenderAddMenuDetectListener( Control control ) throws IOException {
    MenuDetectListener listener = new MenuDetectListener() {
      public void menuDetected( MenuDetectEvent e ) {
      }
    };
    Fixture.fakeNewRequest( control.getDisplay() );
    Fixture.preserveWidgets();

    control.addMenuDetectListener( listener );
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    assertEquals( Boolean.TRUE, message.findListenProperty( control, "menuDetect" ) );

    control.removeMenuDetectListener( listener );
  }

  private static void testRenderRemoveMenuDetectListener( Control control ) throws IOException {
    MenuDetectListener listener = new MenuDetectListener() {
      public void menuDetected( MenuDetectEvent e ) {
      }
    };
    control.addMenuDetectListener( listener );
    Fixture.fakeNewRequest( control.getDisplay() );
    Fixture.preserveWidgets();

    control.removeMenuDetectListener( listener );
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    assertEquals( Boolean.FALSE, message.findListenProperty( control, "menuDetect" ) );
  }

  private static void testRenderMenuDetectListenerUnchanged( Control control ) throws IOException {
    MenuDetectListener listener = new MenuDetectListener() {
      public void menuDetected( MenuDetectEvent e ) {
      }
    };
    Fixture.fakeNewRequest( control.getDisplay() );
    Fixture.preserveWidgets();

    control.addMenuDetectListener( listener );
    Fixture.preserveWidgets();
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    assertNull( message.findListenOperation( control, "menuDetect" ) );

    control.removeMenuDetectListener( listener );
  }

  private static void testRenderAddHelpListener( Control control ) throws IOException {
    HelpListener listener = new HelpListener() {
      public void helpRequested( HelpEvent e ) {
      }
    };
    Fixture.fakeNewRequest( control.getDisplay() );
    Fixture.preserveWidgets();

    control.addHelpListener( listener );
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    assertEquals( Boolean.TRUE, message.findListenProperty( control, "help" ) );

    control.removeHelpListener( listener );
  }

  private static void testRenderRemoveHelpListener( Control control ) throws IOException {
    HelpListener listener = new HelpListener() {
      public void helpRequested( HelpEvent e ) {
      }
    };
    control.addHelpListener( listener );
    Fixture.fakeNewRequest( control.getDisplay() );
    Fixture.preserveWidgets();

    control.removeHelpListener( listener );
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    assertEquals( Boolean.FALSE, message.findListenProperty( control, "help" ) );
  }

  private static void testRenderHelpListenerUnchanged( Control control ) throws IOException {
    HelpListener listener = new HelpListener() {
      public void helpRequested( HelpEvent e ) {
      }
    };
    Fixture.fakeNewRequest( control.getDisplay() );
    Fixture.preserveWidgets();

    control.addHelpListener( listener );
    Fixture.preserveWidgets();
    WidgetUtil.getLCA( control ).renderChanges( control );

    Message message = Fixture.getProtocolMessage();
    assertNull( message.findListenOperation( control, "help" ) );

    control.removeHelpListener( listener );
  }
}
