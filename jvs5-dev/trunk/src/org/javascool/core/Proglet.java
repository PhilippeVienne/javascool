package org.javascool.core;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;

import org.javascool.macros.Macros;
import org.javascool.tools.FileManager;
import org.javascool.tools.Invoke;
import org.javascool.tools.Pml;
import org.javascool.widgets.MainFrame;

public class Proglet {

  /** Méta-données de la proglet. */
  public Pml pml = new Pml();

  /** Définit une proglet à partir d'un répertoire donné.
   * 
   * @param location L'URL (Universal Resource Location) où se trouve la
   *        proglet.
   * @throws IllegalArgumentException Si l'URL est mal formée.
   * @return Cet objet, permettant de définir la construction
   *         <tt>new Proglet().load(..)</tt>. */
  public Proglet load(String location) {
    // Définit les méta-données de la proglet.
    pml.load(location + "proglet.pml", true);
    pml.set("location", location);
    try {
      pml.set("name", new File(location).getName());
    } catch (Exception e) {
      throw new IllegalArgumentException(e + " : " + location + " is a malformed URL");
    }
    if (FileManager.exists(Macros.getResourceURL(location + "completion.xml"))) {
      pml.set("completion", location + "completion.xml");
    }
    if (pml.isDefined("icon") && FileManager.exists(Macros.getResourceURL(location + pml.getString("icon")))) {
      pml.set("icon-location", location + pml.getString("icon"));
    } else {
      pml.set("icon-location", "org/javascool/widgets/icons/scripts.png");
    }
    try {
      Class.forName("org.javascool.proglets." + pml.getString("name") + ".Functions");
      pml.set("has-functions", true);
    } catch (Throwable e) {
      pml.set("has-functions", false);
    }
    if (!pml.isDefined("help-location")) {
      pml.set("help-location", pml.getString("location") + "help.htm");
    }
    try {
      pml.set("jvs-translator", Class.forName("org.javascool.proglets." + pml.getString("name") + ".Translator").newInstance());
    } catch (Throwable e) {
    }
    return this;
  }

  @Override
  public String toString() {
    return pml.toString();
  }

  /** Renvoie le nom de la proglet.
   * 
   * @return Le nom de la proglet. */
  public String getName() {
    return pml.getString("name");
  }

  /** Renvoie le titre de la proglet.
   * 
   * @return Le titre de la proglet. */
  public String getTitle() {
    return pml.getString("title");
  }

  /** Renvoie l'icone de la proglet.
   * 
   * @return Le nom de l'URL de l'icone de la proglet, ou l'icone par defaut
   *         sinon. */
  public String getIcon() {
    return pml.getString("icon-location");
  }

  /** Renvoie la documentation de la proglet.
   * 
   * @return L'URL de la documentation de la proglet. */
  public String getHelp() {
    return pml.getString("help-location");
  }

  /** Renvoie l'url du fichier de completion de la proglet.
   * 
   * @return L'URL de l'xml de completion de la proglet. */
  public String getCompletion() {
    return pml.getString("completion", "");
  }

  /** Indique si la proglet définit des fonctions statiques pour l'utilisateur. */
  public boolean hasFunctions() {
    return pml.getBoolean("has-functions");
  }

  /** Renvoie, si il existe, le panneau graphique à insérer dans javascool.
   * 
   * @return Le panneau graphique de la proglet si il existe, sinon null. */
  public Component getPane() {
    setPane();
    return (Component) pml.getObject("java-pane");
  }

  /** Renvoie, si il existe, le panneau graphique de la proglet.
   * 
   * @return Le panneau graphique de la proglet si il existe, sinon null. */
  public Component getProgletPane() {
    setPane();
    return (Component) pml.getObject("java-proglet-pane");
  }

  private void setPane() {
    if (!pml.isDefined("pane-defined")) {
      pml.set("pane-defined", true);
      if (this.isProcessing()) {
        boolean popup = true;
        try {
          int width = pml.getInteger("width", 500), height = pml.getInteger("height", 500);
          Applet applet = (Applet) Class.forName("" + pml.getString("name") + "").newInstance();
          applet.init();
          applet.setMinimumSize(new Dimension(width, height));
          applet.setMaximumSize(new Dimension(width, height));
          if (popup) {
            popupframe = (new MainFrame() {
              /**
						 * 
						 */
              private static final long serialVersionUID = 543539803612050203L;

              @Override
              public boolean isClosable() {
                return false;
              }
            }).asPopup().reset(getName(), getIcon(), width, height, applet);
            pml.set("java-pane", null);
          } else {
            pml.set("java-pane", applet);
          }
          pml.set("java-proglet-pane", applet);
        } catch (java.lang.ClassNotFoundException e0) {
        } catch (Throwable e) {
          e.printStackTrace();
          System.out.println("Upps erreur de chargement d'une proglet processing : " + e);
        }
      } else {
        try {
          Component pane = (Component) Class.forName("org.javascool.proglets." + pml.getString("name") + ".Panel").newInstance();
          if (pane instanceof JFrame) {
            ((JFrame) pane).setVisible(true);
            pml.set("java-pane", null);
          } else {
            pml.set("java-pane", pane);
          }
          pml.set("java-proglet-pane", pane);
        } catch (java.lang.ClassNotFoundException e0) {
        } catch (Throwable e) {
          e.printStackTrace();
          System.out.println("Upps erreur de chargement d'une proglet : " + e);
        }
      }
    }
  }

  private MainFrame popupframe = null;

  /** Renvoie, si il existe, le translateur de code de la proglet.
   * 
   * @return Le translateur de code de la proglet si il existe, sinon null. */
  public Translator getTranslator() {
    return (Translator) pml.getObject("jvs-translator");
  }

  /** Indique si la proglet a une démo pour l'utilisateur. */
  public boolean hasDemo() {
    return getPane() != null && Invoke.run(getPane(), "start", false);
  }

  /** Lance la démo de la proglet.
   * 
   * @throws RuntimeException si la méthode génère une exception lors de son
   *         appel. */
  public void doDemo() {
    if (hasDemo()) {
      (new Thread() {

        @Override
        public void run() {
          Invoke.run(getPane(), "start");
        }
      }).start();
    }
  }

  /** Indique si la proglet est une proglet processing.
   * 
   * @return La valeur true si cette applet est développée en processing. */
  public boolean isProcessing() {
    return pml.getBoolean("processing");
  }

  /** Démarre la proglet. */
  public void start() {
    if (popupframe != null) {
      popupframe.setVisible(true);
    }
    try {
      if (getPane() != null && getPane() instanceof Applet) {
        ((Applet) getPane()).start();
      }
    } catch (Throwable e) {
      System.err.println("Erreur au démarrage de l'applet/proglet");
    }
  }

  /** Arrête la proglet. */
  public void stop() {
    try {
      if (getPane() != null && getPane() instanceof Applet) {
        ((Applet) getPane()).stop();
      }
    } catch (Throwable e) {
      System.err.println("Erreur à l'arrêt de l'applet/proglet");
    }
    if (popupframe != null) {
      popupframe.setVisible(false);
    }
  }
}