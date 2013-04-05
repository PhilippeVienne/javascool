package org.javascool.core;

import java.io.File;
import java.io.IOException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.javascool.tools.Pml;
import org.javascool.tools.FileManager;


/** Définit le mécanisme de compilation en ligne d'une proglet dans sa version jvs5.
 * - Attention il faut que la proglet ait été convertie en jvs5 (conversion des docs XML en HTML, du fichier de méta-donnée en .json).
 * @see <a href="Proglet2Jar.java.html">code source</a>
 * @serial exclude
 */
public class Proglet2Jar {
  // @factory
  private Proglet2Jar() {}

  /** Compile sous forme de jar une proglet donnée.
   * <p>Les erreurs de compilation ou de construction s'affichent dans la console.</p>
   * @param jarFile La jarre de stockage du résultat.
   * @param progletDir Le répertoire où se trouvent les fichiers de la proglet.
   * @return La valeur true en cas de succès, false si il y a des erreurs de compilation.
   *
   * @throws RuntimeException Si une erreur d'entrée-sortie s'est produite lors de la compilation ou construction.
   */
  public static boolean buildJar(String jarFile, String progletDir) {
    try { 
      JSONObject params = getProgletParameters(progletDir);
      // Répertoire temporaire de compilation
      String jarDir = FileManager.createTempDir("jvs-build-").getAbsolutePath();
      String targetDir = jarDir + File.separator + "org" + File.separator + "javascool" + File.separator + "proglets" + File.separator + params.getString("name");
      // Extraction des classes de javascool
      String javascoolJar = org.javascool.Core.javascoolJar();
      JarManager.jarExtract(javascoolJar, jarDir);
      // Copy et expansion des ressources
      for (String file : FileManager.list(progletDir)) {
	if (new File(file).isFile()) {
	  if (file.endsWith(".jar") || file.endsWith(".zip")) {
	    JarManager.jarExtract(file, jarDir);
	  } else {
	    String tfile = targetDir + File.separator + new File(file).getName();
	    JarManager.copyFile(file, tfile);
	    // Reconstitution de la version jvs4 de la completion, permet de tester la compatibilité
	    if (tfile.endsWith("/completion.json")) {	      
	      JSONArray in = new JSONArray(FileManager.load(tfile));
	      String out = "";
	      for(int i = 0; i < in.length(); i++) {
		JSONObject o = in.getJSONObject(i);
		out += "<keyword name=\""+o.getString("name")+"\" title=\""+o.getString("title")+"\"><code>"+o.getString("code")+"</code><doc>"+o.getString("doc")+"</doc></keyword>\n";
	      }
	      FileManager.save(tfile.replaceFirst("\\.json$", ".xml"), "<keywords>"+out+"</keywords>");
	    }
	  }
	}
      }
      // Compilation des sources java
      String[] javaFiles = FileManager.list(progletDir, ".*\\.java", 4);
      if (javaFiles.length > 0)
	javac(jarDir, jarDir, javaFiles);
      // Nettoyage des arborescences inutiles
      for (String d : new String[] { "com/com/sun/javadoc", "com/com/sun/tools/javadoc", "com/com/sun/tools/doclets", "com/com/sun/jarsigner" })
	JarManager.rmDir(new File(jarDir + File.separator + d));
      // Construction de la jarre
      String mfData = 
	"Manifest-Version: 1.0\n" +
	"Created-By: "+params.getString("author")+" <"+params.getString("email")+">\n" +
	"Main-Class: org.javascool.Core\n" +
	"Implementation-URL: http://javascool.gforge.inria.fr\n" +
	"Java-Version: 1.7\n" +
	"Implementation-Vendor: javascool@googlegroups.com, ou=javascool.gforge.inria.fr, o=inria.fr, c=fr\n";
      JarManager.jarCreate(jarFile, mfData, jarDir);
        
      JarManager.rmDir(new File(jarDir));
      return true;
    } catch (Throwable e) {
      System.err.println(e);
      return false;
    }
  }

  private static void javac(String classPath, String targetDir, String[] javaFiles) throws IOException {
    try {
      String args[] = new String[javaFiles.length + 4];
      args[0] = "-cp";
      args[1] = classPath;
      args[2] = "-d";
      args[3] = targetDir;
      System.arraycopy(javaFiles, 0, args, 4, javaFiles.length);
      java.io.StringWriter out = new java.io.StringWriter();
      Class.forName("com.sun.tools.javac.Main")
	.getDeclaredMethod("compile",
			   Class.forName("[Ljava.lang.String;"),
			   Class.forName("java.io.PrintWriter"))
	.invoke(null, args, new java.io.PrintWriter(out));
      String sout = out.toString().trim();
      sout = sout.replaceFirst("Note: .* uses unchecked or unsafe operations.\\s*Note: Recompile with -Xlint:unchecked for details.","");
      check(sout.length() == 0, "Erreur de compilation java:\n" + sout);
    } catch (Throwable e) {
      throw new IOException("Erreur à la compilation java, «" + e + "»");
    }
  }
    
  /** Renvoie les paramètres d'une proglet.
   * @param progletDir Le répertoire de la proglet.
   * @return La structure json des paramètres.
   * @throws Une erreur en cas de problème de lecture ou de format.
   */
  public static JSONObject getProgletParameters(String progletDir) {
    // Nom et paramètres de la proglet
    String name = new File(progletDir).getName();
    checkProgletName(name);
    String paramFile = progletDir + File.separator + "proglet.json";
    check(new File(paramFile).exists(), "le fichier de description "+paramFile+" n'existe pas, il faut le créer");
    try {
      JSONObject params = new JSONObject(FileManager.load(paramFile));
      check(params != null, "impossible de lire le fichier de description "+paramFile+" (probablement une erreur de syntaxe)");
      for (String key : new String[] {"name", "title", "author", "email", "icon"})
	check(params.optString(key) != null, "erreur dans "+paramFile+" le paramètre "+key+" doit être défini");
      check(name.equals(params.optString("name")), "il y a une incohérence entre le nom du répertoire '"+name+"' et celui déclaré comme nom de la proglet dans proglet.json '"+params.getString("name")+"'");
      return params;
    } catch(JSONException e) {
      check(false, "erreur "+e+" de lecture du fichier de description "+paramFile+" (probablement une erreur de syntaxe)");
      return null;
    }
  }
  
  // Teste la validité d'un nom de proglet.
  static void checkProgletName(String name) {
    check(4 <= name.length() && name.length() <= 16 && name.matches("[a-z][a-zA-Z0-9]+"), 
	  "le nom \""+ name + "\" est invalide,\n\t il ne doit contenir que des lettres, faire au moins quatre caractères et au plus seize et démarrer par une lettre minuscule");
  }

  // Test la validité d'un éléments de la proglet
  static void check(boolean condition, String errorMessage) {
    if (!condition) throw new IllegalArgumentException("Erreur à la construction de la proglet : "+errorMessage);
  }

  /** Lanceur de la construction de la proglet.
   * @param usage <tt>java org.javascool.core2.Proglet2Jar jarFile progletDir</tt>
   */
  public static void main(String[] usage) {
    // @main
    if(usage.length == 2) {
      buildJar(usage[0], usage[1]);
    }
  }
}

