/*********************************************************************************
* Philippe.Vienne@sophia.inria.fr, Copyright (C) 2011.  All rights reserved.    *
* Guillaume.Matheron@sophia.inria.fr, Copyright (C) 2011.  All rights reserved. *
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved.   *
*********************************************************************************/

package org.javascool.builder;

import javax.swing.JPopupMenu;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JFrame;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.javascool.widgets.Console;
import javax.swing.JProgressBar;
import java.util.ArrayList;

/** Définit l'interface graphique pour la construction de proglets. */
public class DialogFrame {
  /** Ouvre une console indépendante pour lancer la construction de proglets. */
  public static void startFrame() {  
    JFrame frame = new JFrame();
    frame.setTitle("Java's Cool 4 Proglet Buidler");
    frame.add(Console.getInstance());
    Console.getInstance().getToolBar().addTool("Sélection et Lancement", "org/javascool/widgets/icons/compile.png", new Runnable() {
	  @Override
	    public void run() {
	    org.javascool.builder.DialogFrame.startProgletMenu(Console.getInstance().getToolBar());
	  }}).setPreferredSize(new Dimension(200, 25));
    Console.getInstance().getToolBar().addTool("Progress Bar", jProgressBar = new JProgressBar());
    jProgressBar.setPreferredSize(new Dimension(400, 25));
    Console.getInstance().getToolBar().addTool("Status Bar", jLabel = new JLabel());
    setUpdate("", 0);
    frame.pack();
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    frame.setVisible(true);
   }

  /** Met à jour la progression de la construction. 
   * @param statut Statut sur l'opération en cours. Un message de 64 caractères max.
   * @param percent Pourcentage de complétion entre 0 et 100.
   */
  public static void setUpdate(String statut, int percent) {
    while(statut.length() < 64) statut += " ";
    if (jLabel != null) 
      jLabel.setText(statut);
    else
      Console.getInstance().show(" Builder : "+statut);
    if (jProgressBar != null)
      jProgressBar.setValue(percent);
  }
  private static JLabel jLabel = null;
  private static JProgressBar jProgressBar = null;

  /** Ouvre un menu de sélection des proglets et de lancement de la construction du Jar. 
   * @param parent Le bouton ou panneau qui lance ce menu.
   * @param reload Indique si les proglets existantes doivent être rechargées. Par défaut false.
   */
  public static void startProgletMenu(Component parent, boolean reload) {
    if(jPopupMenu == null || reload) {
      jPopupMenu = new JPopupMenu();
      if (Builder.getProglets().length > 0) {
	jPopupMenu.add(new JLabel("Sélectionner les proglets à construire:"));
	for(String proglet : Builder.getProglets()) {
	  JCheckBox check = new JCheckBox(proglet);
	  check.setSelected(true);
	  jPopupMenu.add(check);
	}
	jPopupMenu.addSeparator();
	JMenuItem menuitem = new JMenuItem("Construire le jar");
	menuitem.addActionListener(new ActionListener() {
	    @Override
	      public void actionPerformed(ActionEvent e) {
	      ArrayList<String> proglets = new ArrayList<String>();
	      for(Component c : jPopupMenu.getComponents())
		if (c instanceof JCheckBox && ((JCheckBox) c).isSelected())
		  proglets.add(((JCheckBox) c).getText());
	      Builder.build(proglets.toArray(new String[proglets.size()]));
	    }});
	jPopupMenu.add(menuitem);
      } else {
	jPopupMenu.add(new JLabel("Aucune proglet à construire dans ce répertoire"));
      }
    }
    jPopupMenu.show(parent, 0, parent.getHeight());    
  }  
  /**
   * @see #startProgletMenu(Component, boolean)
   */
  public static void startProgletMenu(Component parent) {
    startProgletMenu(parent, false);
  }
  //@ inner-class-variable
  private static JPopupMenu jPopupMenu = null;
}