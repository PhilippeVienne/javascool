/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javascool.proglets.game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import org.javascool.tools.Macros;

/**
 *
 * @author gmatheron
 */
public class Oval extends Geometry implements Drawable {
    boolean m_solid;
    
    public Oval(int x, int y, int w, int h) {
        super(x,y,w,h);
        m_solid=true;
        ((Panel)Macros.getProgletPanel()).addItem(this);
    }
    
    public Oval(int x, int y, int w, int h, boolean solid) {
        super(x,y,w,h);
        ((Panel)Macros.getProgletPanel()).addItem(this);
        m_solid=solid;
    }
    
    @Override
    public void draw(Graphics g) {
        if (m_solid)
            g.fillOval((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        else
            g.drawOval((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
    }
}
