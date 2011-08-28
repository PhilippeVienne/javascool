/*********************************************************************************
 * Philippe.Vienne@sophia.inria.fr, Copyright (C) 2011.  All rights reserved.    *
 * Guillaume.Matheron@sophia.inria.fr, Copyright (C) 2011.  All rights reserved. *
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved.   *
 *********************************************************************************/
package org.javascool.builder;

import org.javascool.tools.FileManager;
import org.javascool.tools.Xml2Xml;
import org.javascool.tools.Pml;
import org.javascool.core.Java2Class;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;
import org.javascool.Core;

/** Cette factory contient les mécanismes de construction d'une application Java's Cool avec des proglets.
 *
 * @see <a href="ProgletsBuilder.java.html">code source</a>
 * @serial exclude
 */
public class ProgletsBuilder {

    static final boolean verbose=false;
    
    /** Définit le file separator dans une expression régulière. */
    private static final String fileRegexSeparator = File.separator.equals("\\") ? "\\\\" : File.separator;

    // @factory
    private ProgletsBuilder() {
    }

    /** Teste si cette version de Java'sCool a la capacité de créer des jar.  */
    public static boolean canBuildProglets() {
        try {
            Class.forName("com.icl.saxon.TransformerFactoryImpl");
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    /** Renvoie les proglets à construire. */
    public static String[] getProglets() {
        ArrayList<String> proglets = new ArrayList<String>();
        for (String dir : FileManager.list(System.getProperty("user.dir"))) {
            if (FileManager.exists(dir + File.separator + "proglet.pml")) {
                proglets.add(dir);
            }
        }
        return proglets.toArray(new String[proglets.size()]);
    }

    /** Construit une nouvelle archive avec les proglets proposées.
     * @param proglets Les proglets sélectionnées. Par défaut toutes les proglets disponibles.
     * @param targetDir Le répertoire cible dans lequel la construction se fait. Si null utilise un répertoire temporaire.
     * @param webdoc Si true compile la javadoc et les jars de chaque proglet (false par défaut).
     * @return La valeur true si la construction est sans erreur, false sinon.
     */
    public static boolean build(String[] proglets, String targetDir, boolean webdoc) {
        if (!canBuildProglets()) {
            throw new IllegalArgumentException("Mauvaise configuration du builder, il faut utiliser le bon jar !");
        }
        try {
            // Définition de la jarre cible.
            String targetJar = System.getProperty("user.dir") + File.separator + "javascool-proglets.jar";
            new File(targetJar).delete();
            System.out.println("Scan des proglets à partir du répertoire: " + System.getProperty("user.dir"));
            // Installation du répertoire de travail.
            File buildDir;
            String jarDir, progletsDir;
            // Création des répertoires cible.
            {
                if (targetDir == null) {
                    buildDir = new File(".build");
                } else {
                    buildDir = new File(targetDir);
                    JarManager.rmDir(buildDir);
                    buildDir.mkdirs();
                }
                jarDir = buildDir + File.separator + "jar";
                progletsDir = jarDir + File.separator + "org" + File.separator + "javascool" + File.separator + "proglets";
                new File(progletsDir).mkdirs();
            }
            DialogFrame.setUpdate("Installation 1/2", 10);
            // Expansion des classes javascool et des proglets existantes dans les jars
            {
                // Expansion des jars du sketchbook
                for (String jar : FileManager.list(System.getProperty("user.dir"), ".*\\.jar")) {
                    if (!jar.matches(".*" + fileRegexSeparator + "javascool-(builder|proglets).jar")) {
                        JarManager.jarExtract(jar, jarDir);
                    }
                }
                // Expansion des jars des proglets
                for (String proglet : proglets) {
                    for (String jar : FileManager.list(proglet, ".*\\.jar")) {
                        JarManager.jarExtract(jar, jarDir);
                    }
                }
                // Expansion des jars de javascool
                String javascoolJar = Core.javascoolJar();
                JarManager.jarExtract(javascoolJar, jarDir, "org/javascool");
                JarManager.jarExtract(javascoolJar, jarDir, "org/fife");
            }
            DialogFrame.setUpdate("Installation 2/2", 20);
            Integer level = 20;
            int up = (10 / proglets.length == 0 ? 1 : 10 / proglets.length);
            // Construction des proglets
            for (String proglet : proglets) {
                ProgletBuild build = new ProgletBuild(proglet, new File(proglet).getAbsolutePath(), jarDir);
                String name = new File(proglet).getName();
                System.out.println("Compilation de " + name + " ...");
                if (build.isprocessing) {
                    System.out.println("!=>proglet processing (En alpha !)");
                    DialogFrame.setUpdate("Construction de " + name + " 1/4", level += up);
                    build.copyFiles();
                    DialogFrame.setUpdate("Construction de " + name + " 2/4", level += up);
                    build.checkProglet();
                    build.setupProcessingProglet();
                    DialogFrame.setUpdate("Construction de " + name + " 3/4", level += up);
                    build.convertHdocs();
                } else {
                    DialogFrame.setUpdate("Construction de " + name + " 1/4", level += up);
                    build.copyFiles();
                    DialogFrame.setUpdate("Construction de " + name + " 2/4", level += up);
                    build.checkProglet();
                    DialogFrame.setUpdate("Construction de " + name + " 3/4", level += up);
                    build.convertHdocs();
                    DialogFrame.setUpdate("Construction de " + name + " 4/4", level += up);
                    build.createHtmlApplet();
                    if (webdoc) {
                        build.javadoc();
                    }
                }
            }
            // Lancement de la compilation de tous les java des proglets
            {
                String[] javaFiles = FileManager.list(progletsDir, ".*\\.java", 1);
                if (javaFiles.length > 0) {
                    javac(javaFiles);
                }
            }
            DialogFrame.setUpdate("Finalisation 1/2", 90);
            System.out.println("Compilation des jarres .. ");
            // Création des jarres avec le manifest
            {
                String version = "Java'sCool v4 on \"" + new Date() + "\" Revision #" + Core.revision;
                Pml manifest = new Pml().set("Main-Class", "org.javascool.Core").
                        set("Manifest-version", version).
                        set("Created-By", "inria.fr (javascool.gforge.inria.fr) ©INRIA: CeCILL V2 + CreativeCommons BY-NC-ND V2").
                        set("Implementation-URL", "http://javascool.gforge.inria.fr").
                        set("Implementation-Vendor", "javascool@googlegroups.com, ou=javascool.gforge.inria.fr, o=inria.fr, c=fr").
                        set("Implementation-Version", version).
                        save(buildDir + "/manifest.jmf");
                // Création des archives pour chaque proglet
                if (webdoc) {
                    for (String proglet : proglets) {
                        String name = new File(proglet).getName();
                        String javascoolPrefix = "org" + File.separator + "javascool" + File.separator;
                        String jarEntries[] = {
                            javascoolPrefix + "Core", "org" + File.separator + "fife",
                            javascoolPrefix + "builder", javascoolPrefix + "core", javascoolPrefix + "gui", javascoolPrefix + "macros", javascoolPrefix + "tools", javascoolPrefix + "widgets",
                            javascoolPrefix + "proglets" + File.separator + name};
                        String tmpJar = buildDir + File.separator + "javascool-proglet-" + name + ".jar";
                        JarManager.jarCreate(tmpJar, buildDir + "/manifest.jmf", jarDir, jarEntries);
                    }
                }
                // Elimination de la proglet sampleCode 
                JarManager.rmDir(new File(progletsDir + File.separator + "sampleCode"));
                // Création de l'archive principale
                JarManager.jarCreate(targetJar, buildDir + "/manifest.jmf", jarDir);
                // Signature et déplacement des "javascool-proglet-"+name+".jar" dans les répetoires des proglets
                if (webdoc) {
                    System.out.print("Signature des jarres: ");
                    System.out.flush();
                    for (String proglet : proglets) {
                        String name = new File(proglet).getName();
                        String tmpJar = buildDir + File.separator + "javascool-proglet-" + name + ".jar";
                        String signedJar = progletsDir + File.separator + name + File.separator + "javascool-proglet-" + name + ".jar";
                        if (new File(signedJar).getParentFile().exists()) {
                            System.out.print(name + " .. ");
                            String keystore = jarDir + File.separator + "org" + File.separator + "javascool" + File.separator + "builder" + File.separator + "javascool.key";
                            String args = "-storepass\tjavascool\t-keypass\tmer,d,azof\t-keystore\t" + keystore + "\t-signedjar\t" + signedJar + "\t" + tmpJar + "\tjavascool";
                            sun.security.tools.JarSigner.main(args.split("\t"));

                        }
                    }
                    System.out.println("ok.");
                }
                DialogFrame.setUpdate("Finalisation 2/2", 100);
            }
            if (targetDir == null) {
                JarManager.rmDir(buildDir);
            }
            System.out.println("Construction achevée avec succès: «" + targetJar + "» a été créé");
            System.out.println("\tIl faut lancer «" + targetJar + "» pour tester/utiliser les proglets.");
            return true;
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.out.println("Erreur inopinée lors de la construction (" + e.getMessage() + "): corriger l'erreur et relancer la construction");
            return false;
        }
    }

    /**
     * @see #build(String[], String, boolean)
     */
    public static boolean build(String[] proglets, String targetDir) {
        return build(proglets, targetDir, false);
    }

    /**
     * @see #build(String[], String, boolean)
     */
    public static boolean build(String[] proglets, boolean webdoc) {
        return build(proglets, null, webdoc);
    }

    /**
     * @see #build(String[], String, boolean)
     */
    public static boolean build(String targetDir, boolean webdoc) {
        return build(getProglets(), targetDir, webdoc);
    }

    /**
     * @see #build(String[], String, boolean)
     */
    public static boolean build(String[] proglets) {
        return build(proglets, null, false);
    }

    /**
     * @see #build(String[], String, boolean)
     */
    public static boolean build(String targetDir) {
        return build(getProglets(), targetDir, false);
    }

    /**
     * @see #build(String[], String, boolean)
     */
    public static boolean build(boolean webdoc) {
        return build(getProglets(), null, webdoc);
    }

    /**
     * @see #build(String[], String, boolean)
     */
    public static boolean build() {
        return build(getProglets(), null, false);
    }

    /** Lance la compilation java sur un groupe de fichiers. */
    private static void javac(String[] javaFiles) {
        if (!Java2Class.compile(javaFiles, true)) {
            throw new IllegalArgumentException("Erreur de compilation java");
        }
    }

    /** Construction de javadoc avec sources en java2html. */
    private static void javadoc(String srcDir, String apiDir) throws IOException {
        apiDir = new File(apiDir).getCanonicalPath();
        new File(apiDir).mkdirs();
        String files[] = FileManager.list(srcDir, ".*\\.java$");
        if (files.length > 0) {
            {
                // Construit l'appel à javadoc
                String argv = "-quiet\t-classpath\t" + Core.javascoolJar() + "\t-d\t" + apiDir
                        + "\t-link\thttp://download.oracle.com/javase/6/docs/api\t-link\thttp://javadoc.fifesoft.com/rsyntaxtextarea"
                        + "\t-public\t-author\t-windowtitle\tJava's Cool v4\t-doctitle\tJava's Cool v4\t-version\t-nodeprecated\t-nohelp\t-nonavbar\t-notree\t-charset\tUTF-8";
                for (String f : files) {
                    argv += "\t" + f;
                }
                // Lance javadoc
                try {
                    com.sun.tools.javadoc.Main.execute(argv.split("\t"));
                } catch (Throwable e) {
                    throw new IOException(e);
                }
            }
            // Construit les sources en HTML à partir de java2html
            {
                // Lance java2html
                Jvs2Html.runDirectory(srcDir, apiDir);
            }
        }
    }
    
    /** Envoie un message de log dans la console.
     * @param text Le message
     * @param onlyVerbose Ne s'affiche que si l'option -v est activé
     */
    public static void log(String text,boolean onlyVerbose){
        if(onlyVerbose){
            if(verbose){
                System.out.println(text);
            }
        } else {
            System.out.println(text);
        }
    }
    
    /** Imprime un message dans la console
     * @see ProgletsBuilder#log(java.lang.String, boolean) 
     */
    public static void log(String text){
        log(text,false);
    }

    /** Contôleur pour la compilation d'une proglet. */
    private static class ProgletBuild {

        /** Le nom de la proglet */
        private String name;
        /** Le répertoire de la proglet à compiler */
        private String progletSrc;
        /** Le répertoire de la proglet compilée */
        private String progletDir;
        /** Le fichier Pml d'information de la proglet */
        private Pml pml;
        /** Le dossier du jar final */
        private String jarDest;
        /** Vrai si la proglet est processing */
        private boolean isprocessing;

        /** Crée un nouveau contôleur pour la compilation d'une proglet.
         * @param name Le nom de la proglet
         * @param progletDir Le répertoire de la proglet à compiler
         * @param pml Le fichier Pml d'information de la proglet
         * @param jarDest Le dossier du jar final
         */
        public ProgletBuild(String name, String progletDir, Pml pml, String jarDest) {
            this.name = name != null ? new File(name).getName() : "?";
            this.pml = pml != null ? pml : new Pml().load(progletDir + File.separator + "proglet.pml");
            try {
                this.progletSrc = progletDir != null ? new File(progletDir).getAbsolutePath() : "";
            } catch (Exception ex) {
                throw new RuntimeException("Le dossier source de " + this.name + " n'existe pas");
            }
            this.jarDest = jarDest != null ? new File(jarDest).getAbsolutePath() : "";
            if (this.pml.getBoolean("processing")) {
                this.isprocessing = true;
            } else {
                this.isprocessing = false;
            }
            this.progletDir = jarDest + "/org/javascool/proglets/".replace("/", File.separator) + this.name;
        }

        /** Lit automatiquement le fichier Pml
         * @see ProgletBuild#ProgletBuild(java.lang.String, java.lang.String, org.javascool.tools.Pml, java.lang.String) 
         */
        public ProgletBuild(String name, String progletDir, String jarDest) {
            this(name, progletDir, null, jarDest);
        }

        /** Copie les fichiers de la proglet de la source à la destination. */
        public void copyFiles() {
            try {
                new File(progletDir).mkdirs();
                JarManager.copyFiles(progletSrc, progletDir);
            } catch (IOException ex) {
                throw new RuntimeException("Erreur lors de la copie des fichiers de " + name, ex);
            }
        }

        /** Vérifie si la proglet respect les specifications */
        public void checkProglet() {
            boolean error = false;
            if (!(name.matches("[a-zA-Z][a-zA-Z0-9][a-zA-Z0-9][a-zA-Z0-9]+") && name.length() <= 20)) {
                System.out.println("Le nom de la proglet «" + name + "» est bizarre il ne doit contenir que des lettres faire au moins quatre caractères et au plus seize et démarrer par une lettre minuscule");
                error = true;
            }
            if (!FileManager.exists(progletDir + File.separator + "help.xml") && !isprocessing) {
                System.out.println("Pas de fichier d'aide pour " + name + ", la proglet ne sera pas construite.");
                error = true;
            }
            if (!pml.isDefined("author")) {
                System.out.println("Le champ «author» n'est pas défini dans " + name + "/proglet.pml, la proglet ne sera pas construite.");
                error = true;
            }
            if (!pml.isDefined("title")) {
                System.out.println("Le champ «title» n'est pas défini dans " + name + "/proglet.pml, la proglet ne sera pas construite.");
                error = true;
            }
            pml.save(progletDir + File.separator + "proglet.php");
            if (error) {
                throw new IllegalArgumentException("La proglet ne respecte pas les spécifications");
            }
        }

        /** Convertit les XML en Htm.
         * Sauf le completion.xml
         */
        public void convertHdocs() {
            for (String doc : FileManager.list(progletDir, ".*\\.xml")) // Escape le fichier de spécification des complétion de l'éditeur
            {
                if (!new File(doc).getName().equals("completion.xml")) {
                    try {
                        FileManager.save(doc.replaceFirst("\\.xml", "\\.htm"),
                                Xml2Xml.run(FileManager.load(doc),
                                FileManager.load(this.jarDest + "/org/javascool/builder/hdoc2htm.xslt".replace("/", File.separator))));
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("dans " + new File(doc).getName() + " : " + e.getMessage());
                    }
                }
            }
        }

        /** Crée la page html de l'applet de la proglet */
        public void createHtmlApplet() {
            FileManager.save(progletDir + File.separator + "applet-tag.htm",
                    "<applet width='560' height='620' code='org.javascool.widgets.PanelApplet' archive='./proglets/" + name + "/javascool-proglet-" + name + ".jar'><param name='panel' value='org.javascool.proglets." + name + ".Panel'/><pre>Impossible de lancer " + name + ": Java n'est pas installé ou mal configuré</pre></applet>\n");
        }

        /** Génère la javadoc de la proglet */
        public void javadoc() {
            try {
                log("Création de la javadoc pour "+name);
                ProgletsBuilder.javadoc(progletDir, progletDir + File.separator + "api");
            } catch (IOException ex) {
                throw new RuntimeException("Erreur lors de la génération de la javadoc");
            }
        }

        /** Configure une proglet Processing */
        public void setupProcessingProglet() {
            if (isprocessing) {
                log("Extrait les archives de la proglet ...");
                for (String jar : FileManager.list(this.progletDir + File.separator + "applet", ".*\\.jar")) {
                    log(jar,true);
                    JarManager.jarExtract(jar, this.jarDest);
                }
                log("Efface les dossiers processing ...");
                JarManager.rmDir(new File(this.progletDir,"applet"));
            } else {
                throw new IllegalStateException("La proglet "+name+" n'est pas en processing");
            }
        }
    }
}
