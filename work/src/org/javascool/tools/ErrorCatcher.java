/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2010.  All rights reserved. *
*******************************************************************************/

package org.javascool.tools;

// Used to report a throwable
import java.lang.reflect.InvocationTargetException;

// Used to frame a message
import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

/** Détecte et rapporte de manière optimisée des erreurs lors de l'exécution.
 * @see <a href="ErrorCatcher.java.html">code source</a>
 * @serial exclude
 */
public class ErrorCatcher {
  // @factory
  private ErrorCatcher() {}

  /** Rapporte une erreur avvec son contexte en tant qu'erreur d'exécution.
   * <p>Permet de fournir une solution acceptable dans une construction de la forme <tt>try { } catch(Thowable e) { ErrorCatcher.report(e); }</tt>.</p>
   * @param error L'erreur ou exception à rapporter.
   * @return Une RuntimeException qui encapsule cette erreur, après avoir imprimé un alerte dans la consol et du diagnostic dans la console d'erreur.
   */
  public static RuntimeException report(Throwable error) {
    if(error instanceof InvocationTargetException)
      report(error.getCause());
    System.out.println(error.toString());
    System.err.println(error.toString());
    for(int i = 0; i < 4 && i < error.getStackTrace().length; i++)
      System.err.println(error.getStackTrace()[i]);
    return error instanceof RuntimeException ? (RuntimeException) error : new RuntimeException(error);
  }
  /** Ouvre une fenêtre d'alerte en cas d'exception intempestive et non prise en compte.
   * <p> Installe un gestionnaire d'exception non interceptée qui recueille des informations sur: 
   * les versions des composants logiciels, le nom du process, la trace de la pile et 
   * l'affiche dans une fenêtre séparée afin d'être recueillies et communiquées par l'utilisateur.</p>
   * @param title Le titre de la fenêtre.
   * @param header Un texte entête expliquant à l'utilisateur quoi faire avec cette sortie d'exception.
   */
  public static void setUncaughtExceptionAlert(String title, String header) {
    uncaughtExceptionAlertTitle = title;
    uncaughtExceptionAlertHeader = header;
    Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
	public void uncaughtException(Thread t, Throwable e) {
	  String s = "";
	  if(uncaughtExceptionAlertOnce <= 1) {
	    s += uncaughtExceptionAlertHeader + "\n";
	    for(String p : new String[] { "javascool.version", "java.version", "os.name", "os.arch", "os.version" }
		)
	      s += "> " + p + " = " + System.getProperty(p) + "\n";
	  }
	  s += "> thread.name = " + t.getName() + "\n";
	  s += "> throwable = " + e + "\n";
	  if(0 < uncaughtExceptionAlertOnce)
	    s += "> count = " + uncaughtExceptionAlertOnce + "\n";
	  s += "> stack-trace = «\n";
	  for(int i = 0; i < t.getStackTrace().length; i++)
	    s += e.getStackTrace()[i] + "\n";
	  s += "»\n";
	  if(uncaughtExceptionAlertOnce == 0) {
	    show(uncaughtExceptionAlertTitle, s);
	  } else {
	    System.err.println(s);
	  }
	  uncaughtExceptionAlertOnce++;
	}
      });
  }
  private static String uncaughtExceptionAlertTitle, uncaughtExceptionAlertHeader;
  private static int uncaughtExceptionAlertOnce = 0;
  // Affichage d'un texte dans une fenêtre
  private static void show(String title, String text) {
    JEditorPane p = new JEditorPane();
    p.setEditable(false);
    p.setText(text);
    JFrame f = new JFrame(title);
    f.getContentPane().add(new JScrollPane(p, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
    f.pack();
    f.setSize(800, 600);
    f.setVisible(true);
  }
}