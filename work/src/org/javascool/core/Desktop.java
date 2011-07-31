/*********************************************************************************
 * Philippe.Vienne@sophia.inria.fr, Copyright (C) 2011.  All rights reserved.    *
 * Guillaume.Matheron@sophia.inria.fr, Copyright (C) 2011.  All rights reserved. *
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved.   *
 *********************************************************************************/

package org.javascool.core;

import java.awt.Frame;
import java.awt.Container;
import org.javascool.widgets.HtmlDisplay;
import org.javascool.widgets.ToolBar;

/** Définit les functions d'interaction avec l'interface graphique de JavaScool. 
 * @see <a href="Desktop.java.html">code source</a>
 * @serial exclude
 */
public class Desktop {

  // @static-instance 
  private Desktop() {}
  
  /** Renvoie la fenêtre racine de l'interface graphique. */
  public Frame getFrame() { return null; }

  /** Ouvre un fichier dans l'éditeur.
   * @param location L'URL (Universal Resource Location) du fichier.
   * @throws RuntimeException Si une erreur d'entrée-sortie s'est produite lors de l'exécution.
   */
  public void addFile(String location) {
    throw new RuntimeException("Non implémenté");
  }
  /** Ouvre un document HTML dans l'interface.
   * @param location L'URL (Universal Resource Location) du fichier.
   * @param east Affiche dans le panneau de droite si vrai (valeur par défaut), sinon le panneau de gauche.
   * @throws RuntimeException Si une erreur d'entrée-sortie s'est produite lors de l'exécution.
   */
  public void addTab(String location, boolean east) {
    addTab("Document", new HtmlDisplay().setPage(location), "", null, east, true);
  }
  /**
   * @see #addTab(String, boolean)
   */
  public void addTab(String location) {
    addTab(location, true);
  }

  /** Ajoute un composant graphique à l'interface.
   * @param label Nom du bouton. Chaque composant doit avoir un nom différent.
   * @param pane  Le composant à ajouter.
   * @param title Une description d'une ligne du composant.
   * @param icon  Icone descriptive du composant. Pas d'icone si la valeur est nulle ou le fichier inconnu.
   * @param east Affiche dans le panneau de droite si vrai (valeur par défaut), sinon le panneau de gauche.
   * @param show Rend le composant visible si vrai (valeur par défaut), sinon ne modifie pas l'onglet affiché.
   */
  public void addTab(String label, Container pane, String title, String icon, boolean east, boolean show) {
    throw new RuntimeException("Non implémenté");
  }
  /**
   * @see #addTab(String, Container, String, String, boolean, boolean)
   */
  public void addTab(String label, Container pane, String title, String icon, boolean east) {
    addTab(label, pane, title, icon, east, true);
  }
  /**
   * @see #addTab(String, Container, String, String, boolean, boolean)
   */
  public void addTab(String label, Container pane, String title, String icon) {
    addTab(label, pane, title, icon, true, true);
  }
  /**
   * @see #addTab(String, Container, String, String, boolean, boolean)
   */
  public void addTab(String label, Container pane, String title) {
    addTab(label, pane, title, null, true, true);
  }
  /**
   * @see #addTab(String, Container, String, String, boolean, boolean)
   */
  public void addTab(String label, Container pane) {
    addTab(label, pane, "", null, true, true);
  }

  /** Renvoie un accès à la barre d'outil de l'interface.
   * @return La barre d;outil.
   */
  public ToolBar getToolBar() {
    throw new RuntimeException("Non implémenté");
  }

  /** Crée et/ou renvoie l'unique instance du desktop.
   * <p>Une application ne peut définir qu'un seul desktop.</p>
   */
  public static Desktop getInstance() {
    if (desktop == null) desktop = new Desktop();
    return desktop;
  }
  private static Desktop desktop = null;
}

