/**
 * Functions.java
 * Part of Javascool proglet "game"
 * File creation : 20110704 at revision 48 by gmatheron (INRIA)
 * Last modification 20110704 by gmatheron (INRIA)
 * 
 * gmatheron : guillaume.matheron.06@gmail.com
 */
package org.javascool.proglets.game;

import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.javascool.tools.Console;
import org.javascool.tools.Macros;

/*
 * To use these event listeners, use this syntax : 
 * void toto(MouseState s) {
 *     ...
 * }
 * void main() {
 *     onClick("toto");
 * }
 */
/**
 * 
 * @author gmatheron
 */
public class Functions {
    private static Functions m_singleton;
    private boolean m_mouseDown[] = {false, false, false};
    /*
     * These arrays are designed to store the functions the user assigned a listener
     * A convenience type is used : CallbackFunction
     */
    private java.util.ArrayList<String> m_onClick;
    private java.util.ArrayList<String> m_onMouseEntered;
    private java.util.ArrayList<String> m_onMouseExited;
    private java.util.ArrayList<String> m_onMousePressed;
    private java.util.ArrayList<String> m_onMouseReleased;
    private java.util.ArrayList<String> m_onMouseDown;
    private java.util.ArrayList<String> m_onMouseUp;
    private java.util.ArrayList<String> m_onMouseMoved;
    private java.util.ArrayList<String> m_onMouseDragged;

    /**
     * Used to create a listener that will callback the specified function
     * with one MouseState argument
     * @param s The function to callback
     */
    public static void onClick(String s) {
        m_singleton.m_onClick.add(s);
    }

    /**
     * Used to create a listener that will callback the specified function
     * with one MouseState argument
     * @param s The function to callback
     */
    public static void onMouseEntered(String s) {
        m_singleton.m_onMouseEntered.add(s);
    }

    /**
     * Used to create a listener that will callback the specified function
     * with one MouseState argument
     * @param s The function to callback
     */
    public static void onMouseExited(String s) {
        m_singleton.m_onMouseExited.add(s);
    }

    /**
     * Used to create a listener that will callback the specified function
     * with one MouseState argument
     * @param s The function to callback
     */
    public static void onMousePressed(String s) {
        m_singleton.m_onMousePressed.add(s);
    }

    /**
     * Used to create a listener that will callback the specified function
     * with one MouseState argument
     * @param s The function to callback
     */
    public static void onMouseReleased(String s) {
        m_singleton.m_onMouseReleased.add(s);
    }

    public static void onMouseDown(String s) {
        m_singleton.m_onMouseDown.add(s);
    }

    public static void onMouseUp(String s) {
        m_singleton.m_onMouseUp.add(s);
    }

    public static void onMouseMoved(String s) {
        m_singleton.m_onMouseMoved.add(s);
    }

    public static void onMouseDragged(String s) {
        m_singleton.m_onMouseDragged.add(s);
    }

    public static void call(String method, State s) {
        try {
            try {
                Console.getProgram().getClass().getMethod(method, State.class).invoke(Console.getProgram(), s);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //TODO
    /**
     * This method should be called at init (see bug report #005)
     * It creates the listeners
     */
    public static void start() {
        m_singleton=new Functions();
        
        m_singleton.m_onClick=new java.util.ArrayList<String>();
        m_singleton.m_onMouseDown=new java.util.ArrayList<String>();
        m_singleton.m_onMouseDragged=new java.util.ArrayList<String>();
        m_singleton.m_onMouseEntered=new java.util.ArrayList<String>();
        m_singleton.m_onMouseExited=new java.util.ArrayList<String>();
        m_singleton.m_onMouseMoved=new java.util.ArrayList<String>();
        m_singleton.m_onMousePressed=new java.util.ArrayList<String>();
        m_singleton.m_onMousePressed=new java.util.ArrayList<String>();
        m_singleton.m_onMouseReleased=new java.util.ArrayList<String>();
        m_singleton.m_onMouseUp=new java.util.ArrayList<String>();
        
        Clock c = new Clock();
        c.setFps(30);
        new Thread(c).start();

        /********* START ANONYMOUS CLASSES ***************/
        Macros.getProgletPanel().addMouseListener(
                new java.awt.event.MouseListener() {

                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        for (int i = 0; i < m_singleton.m_onClick.size(); i++) {
                            call(m_singleton.m_onClick.get(i), (new MouseState(evt)));
                        }
                        System.err.println("Mouse Clicked");
                    }

                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        for (int i = 0; i < m_singleton.m_onMouseEntered.size(); i++) {
                            call(m_singleton.m_onMouseEntered.get(i), (new MouseState(evt)));
                        }
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        for (int i = 0; i < m_singleton.m_onMouseExited.size(); i++) {
                            call(m_singleton.m_onMouseExited.get(i), (new MouseState(evt)));
                        }
                    }

                    @Override
                    public void mousePressed(java.awt.event.MouseEvent evt) {
                        for (int i = 0; i < m_singleton.m_onMousePressed.size(); i++) {
                            call(m_singleton.m_onMousePressed.get(i), (new MouseState(evt)));
                        }
                        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
                            m_singleton.m_mouseDown[0] = true;
                        } else if (evt.getButton() == java.awt.event.MouseEvent.BUTTON2) {
                           m_singleton. m_mouseDown[1] = true;
                        } else if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                            m_singleton.m_mouseDown[2] = true;
                        }
                    }

                    @Override
                    public void mouseReleased(java.awt.event.MouseEvent evt) {
                        for (int i = 0; i < m_singleton.m_onMouseReleased.size(); i++) {
                            call(m_singleton.m_onMouseReleased.get(i), (new MouseState(evt)));
                        }
                        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
                            m_singleton.m_mouseDown[0] = false;
                        } else if (evt.getButton() == java.awt.event.MouseEvent.BUTTON2) {
                            m_singleton.m_mouseDown[1] = false;
                        } else if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                            m_singleton.m_mouseDown[2] = false;
                        }
                    }
                });
        new java.awt.event.MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                for (int i = 0; i < m_singleton.m_onMouseDragged.size(); i++) {
                    call(m_singleton.m_onMouseDragged.get(i), (new MouseState(e)));
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                for (int i = 0; i < m_singleton.m_onMouseMoved.size(); i++) {
                    call(m_singleton.m_onMouseMoved.get(i), (new MouseState(e)));
                }
            }
        };
        /********* END ANONYMOUS CLASSES ***************/
    }

    /**
     * TODO
     */
    class Sprite {

        private javax.swing.JPanel m_panel;

        public void call() {
        }
    }

    public static class Clock implements Runnable {

        private int m_fps;

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000 / m_fps);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
                }
                tick();
            }
        }

        public void setFps(int fps) {
            m_fps = fps;
        }

        private void tick() {
            for (int j = 0; j < 3; j++) {
                if (m_singleton.m_mouseDown[j]) {
                    for (int i = 0; i < m_singleton.m_onMouseDown.size(); i++) {
                        call(m_singleton.m_onMouseDown.get(i), new MouseState(j));
                    }
                } else {
                    for (int i = 0; i < m_singleton.m_onMouseUp.size(); i++) {
                        call(m_singleton.m_onMouseUp.get(i), new MouseState(j));
                    }
                }
            }
        }
    }

    
}

/**
 * Superclass of all the states.
 * 
 * If an abstract (State) class can't be declared into a non-abstract class (Functions)
 * maybe we could switch it to an interface or leave it non-abstract (ugly)
 */
abstract class State {
}

/**
 * A MouseState is passed as a parameter to the user-defined callback functions
 * so it must stay as intuitive as possible
 */
class MouseState extends State {

    private java.awt.event.MouseEvent m_evt;
    private int m_button;

    MouseState(java.awt.event.MouseEvent evt) {
        m_evt = evt;
    }

    MouseState(int button) {
        m_button = button;
    }
    /* 
     * TODO add covenience methods such as x and y. Maybe leave them public
     * in case the user prefers to use s.x rather than x.getX() ?
     */
}