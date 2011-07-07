/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Panel.java
 *
 * Created on 1 juil. 2011, 11:02:55
 */
package org.javascool.proglets.game;

import java.awt.Color;
import java.util.logging.Logger;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author gmatheron
 */
public class Panel extends javax.swing.JPanel {
    private static final long serialVersionUID = 1L;
    private java.util.ArrayList<Drawable> m_items;
    
    /** Creates new form Panel */
    @SuppressWarnings("CollectionWithoutInitialCapacity")
    public Panel() {
        m_items=new java.util.ArrayList<Drawable>();
        initComponents();
    }

    public void help(){
        this.removeAll();
    }
    
    public void stop() {
        getM_items().removeAll(getM_items());
    }
    
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.WHITE);
        
        BufferedImage backBuffer=new BufferedImage(this.getWidth(),this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        for (int i=0; i<getM_items().size(); i++) {
            getM_items().get(i).draw(backBuffer.getGraphics());
        }
        
        g.drawImage(backBuffer, 0, 0, null);
    }
    
    public void addItem(Drawable d) {
        getM_items().add(d);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 512, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 391, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    /**
     * @return the m_items
     */
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public java.util.ArrayList<Drawable> getM_items() {
        return m_items;
    }

    /**
     * @param m_items the m_items to set
     */
    @SuppressWarnings("AssignmentToCollectionOrArrayFieldFromParameter")
    public void setM_items(java.util.ArrayList<Drawable> m_items) {
        this.m_items = m_items;
    }
    private static final Logger LOG = Logger.getLogger(Panel.class.getName());
}
