package org.javascool.gui.editor;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.*;


public class ClosableTabbedPane extends JTabbedPane{

	private static final long serialVersionUID = 2304963236664505495L;
	private TabCloseUI closeUI = new TabCloseUI(this);

	public void paint(Graphics g){
		super.paint(g);
		closeUI.paint(g);
	}

	public void addTab(String title, Component component) {
		if(component instanceof ClosableComponent)
			super.addTab(title+"    ", component);
		else
			super.addTab(title, component);
	}

	public void setTitleAt(int index, String title){
		if(getComponentAt(index) instanceof ClosableComponent&&((ClosableComponent)getComponentAt(index)).isClosable())
			title=title+"    ";
		super.setTitleAt(index, title);
	}


	public String getTabTitleAt(int index) {
		return super.getTitleAt(index).trim();
	}

	private class TabCloseUI implements MouseListener, MouseMotionListener {
		private ClosableTabbedPane  tabbedPane;
		private int closeX = 2 ,closeY = 2, meX = 0, meY = 0;
		private int selectedTab;
		private final int  width = 7, height = 7;
		private Rectangle rectangle = new Rectangle(0,0,width, height);
		public TabCloseUI(ClosableTabbedPane pane) {

			tabbedPane = pane;
			tabbedPane.addMouseMotionListener(this);
			tabbedPane.addMouseListener(this);
		}
		public void mouseEntered(MouseEvent me) {}
		public void mouseExited(MouseEvent me) {}
		public void mousePressed(MouseEvent me) {}
		public void mouseClicked(MouseEvent me) {}
		public void mouseDragged(MouseEvent me) {}



		public void mouseReleased(MouseEvent me) {
			if(closeUnderMouse(me.getX(), me.getY())){
				boolean isToCloseTab = tabAboutToClose(selectedTab);
				if (isToCloseTab && selectedTab > -1){			
					tabbedPane.removeTabAt(selectedTab);
				}
				selectedTab = tabbedPane.getSelectedIndex();
			}
		}

		public void mouseMoved(MouseEvent me) {	
			meX = me.getX();
			meY = me.getY();			
			if(mouseOverTab(meX, meY)){
				controlCursor();
				tabbedPane.repaint();
			}
		}

		private void controlCursor() {
			if(tabbedPane.getTabCount()>0)
				if(closeUnderMouse(meX, meY)){
					tabbedPane.setCursor(new Cursor(Cursor.HAND_CURSOR));	
					//if(selectedTab > -1)
					//tabbedPane.setToolTipTextAt(selectedTab, "Close " +tabbedPane.getTitleAt(selectedTab));
				}
				else{
					tabbedPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					//if(selectedTab > -1)
					//tabbedPane.setToolTipTextAt(selectedTab,"");
				}	
		}

		private boolean closeUnderMouse(int x, int y) {		
			rectangle.x = closeX;
			rectangle.y = closeY;
			return rectangle.contains(x,y);
		}

		public void paint(Graphics g) {

			int tabCount = tabbedPane.getTabCount();
			for(int j = 0; j < tabCount; j++)
				if(tabbedPane.getComponent(j).isShowing()&&tabbedPane.getComponent(j) instanceof ClosableComponent&&((ClosableComponent)tabbedPane.getComponent(j)).isClosable()){			
					int x = tabbedPane.getBoundsAt(j).x + tabbedPane.getBoundsAt(j).width -width-6;
					int y = tabbedPane.getBoundsAt(j).y +7;
					setTitleAt(j, ((ClosableComponent)tabbedPane.getComponent(j)).getFullName());
					drawClose(g,x,y);
					break;
				} else if(tabbedPane.getComponent(j) instanceof ClosableComponent&&!((ClosableComponent)tabbedPane.getComponent(j)).isClosable()){
					setTitleAt(j, ((ClosableComponent)tabbedPane.getComponent(j)).getFullName());
				}
			if(mouseOverTab(meX, meY)){
				drawClose(g,closeX,closeY);
			}
		}

		private void drawClose(Graphics g, int x, int y) {
			if(tabbedPane != null && tabbedPane.getTabCount() > 0){
				Graphics2D g2 = (Graphics2D)g;				
				drawColored(g2, isUnderMouse(x,y)? Color.RED : Color.WHITE, x, y);
			}
		}

		private void drawColored(Graphics2D g2, Color color, int x, int y) {
			g2.setStroke(new BasicStroke(4));
			g2.setColor(Color.BLACK);
			g2.drawLine(x, y, x + width, y + height);
			g2.drawLine(x + width, y, x, y + height);
			g2.setColor(color);
			g2.setStroke(new BasicStroke(2));
			g2.drawLine(x, y, x + width, y + height);
			g2.drawLine(x + width, y, x, y + height);

		}

		private boolean isUnderMouse(int x, int y) {
			if(Math.abs(x-meX)<width && Math.abs(y-meY)<height )
				return  true;		
			return  false;
		}

		private boolean mouseOverTab(int x, int y) {
			int tabCount = tabbedPane.getTabCount();
			for(int j = 0; j < tabCount; j++)
				if(tabbedPane.getBoundsAt(j).contains(meX, meY)&&tabbedPane.getComponent(j) instanceof ClosableComponent&&((ClosableComponent)tabbedPane.getComponent(j)).isClosable()){
					selectedTab = j;
					closeX = tabbedPane.getBoundsAt(j).x + tabbedPane.getBoundsAt(j).width -width-6;
					closeY = tabbedPane.getBoundsAt(j).y +7;					
					return true;
				}
			return false;
		}

	}

	public boolean tabAboutToClose(int tabIndex) {
		return true;
	}


}
