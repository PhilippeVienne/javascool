/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javascool.proglets.game;

/**
 * This interface defines an object that can tell the manager if it wants to catch an event.
 * For instance, let's say a Drawable EventCatcher Sprite was called 'button'.
 * The end-user registers a listener with : 
 * <code>button.onClick("exit");</code>
 * The event is registered as an EventListener in the main onClick events table.
 * When a click is detected in the main panel, the manager calls the corresponding isForMe method
 * of the EventCatcher associated with the event (here, the Sprite button implements EventCatcher)
 * If EventCatcher::isForMe returns true, then the callback function (here "exit") is called.
 * @author gmatheron
 */
public interface EventCatcher {

    /**
     * Should return true if the event concerns this object
     * @param e The mouse's state (in case of a MouseEvent or a MouseMotionEvent)
     * @return true if the event concerns this object
     */
    public boolean isForMe(MouseState e);

    /**
     * Should return true if the event concerns this object
     * @param e The mouse's wheel's state (in case of a MouseWheelEvent)
     * @return true if the event concerns this object
     */
    public boolean isForMe(MouseWheelState e);

    /**
     * Should return true if the event concerns this object
     * @return true if the event concerns this object
     */
    public boolean isForMe();
}
