/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javascool.proglets.game;

import java.util.logging.Logger;

/**
 * This class is parent of all the objects that are rectangular, that can be
 * placed on the proglet's panel and recieve events if the mouse pointer is
 * above them.
 * @author gmatheron
 */
public abstract class Geometry extends LinkedEventGroup {
    /**
     * Stores the width, height and xy position of the object (relative to the
     * origin of the panel and in pixels)
     */
    private int m_w, m_h, m_x, m_y;
    
    /**
     * Constructs an object based solely on its position (its size will be 0x0)
     * @param x The X coordinate of the object's position
     * @param y The Y coordinate of the object's position
     */
    public Geometry(int x, int y) {
        m_x=x; m_y=y;
        m_w=0; m_h=0;
    }
    
    /**
     * Constructs an object based on its position and size
     * @param x The X coordinate of the object's position
     * @param y The Y coordinate of the object's position
     * @param w The object's width
     * @param h The object's height
     */
    public Geometry(int x, int y, int w, int h) {
        m_x=x; m_y=y; m_w=w; m_h=h;
    }
    
    /**
     * Constructs an object places at position (0,0) and with size 0x0
     */
    public Geometry() {
        m_w=0; m_h=0; m_x=0; m_y=0;
    }
    
    /**
     * Get the object's width
     * @return the object's width
     */
    public int getWidth() {
        return m_w;
    }
    
    /**
     * Get the object's height
     * @return the object's height
     */
    public int getHeight() {
        return m_h;
    }
    
    /**
     * Get the object's X coordinate
     * @return the object's X coordinate
     */
    public int getX() {
        return m_x;
    }
    
    /**
     * Get the object's Y coordinate
     * @return the object's Y coordinate
     */
    public int getY() {
        return m_y;
    }
    
    /**
     * Sets the object's width
     * @param w the object's width
     */
    public void setWidth(int w) {
        m_w=w;
    }
    
    /**
     * Sets the object's height
     * @param h the object's height
     */
    public void setHeight(int h) {
        m_h=h;
    }
    
    /**
     * Sets the object's X position
     * @param x the object's X position
     */
    public void setX(int x) {
        m_x=x;
    }
    
    /**
     * Sets the object's Y position
     * @param y the object's Y position
     */
    public void setY(int y) {
        m_y=y;
    }
    
    /**
     * Sets the object's size
     * @param w the object's width
     * @param h the object's height
     */
    public void scale(int w, int h) {
        m_w=w; m_h=h;
    }
    
    /**
     * Sets the object's position
     * @param x the object's X position
     * @param y the object's Y position
     */
    public void position(int x, int y) {
        m_x=x; m_y=y;
    }
    private static final Logger LOG = Logger.getLogger(Geometry.class.getName());

    /**
     * Returns true if the mouse is over the object
     * @param e The mouseEvent from which to extract the cursor's position
     * @return true if the mouse is over the object
     */
    @Override
    public boolean isForMe(MouseState e) {
        return (e.getX()>m_x && e.getX()<m_x+m_w && e.getY()>m_y && e.getY()<m_y+m_h);
    }

    /**
     * Returns true if the mouse is over the object
     * @param f Not used
     * @return true if the mouse is over the object
     */
    @Override
    public boolean isForMe(MouseWheelState f) {
        MouseState e=new MouseState();
        return (e.getX()>m_x && e.getX()<m_x+m_w && e.getY()>m_y && e.getY()<m_y+m_h);
    }

    /**
     * Returns true if the mouse is over the object
     * @return true if the mouse is over the object
     */
    @Override
    public boolean isForMe() {
        MouseState e=new MouseState();
        return (e.getX()>m_x && e.getX()<m_x+m_w && e.getY()>m_y && e.getY()<m_y+m_h);
    }
}
