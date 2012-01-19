package org.javascool.builder;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import de.java2html.converter.JavaSource2HTMLConverter;
import de.java2html.javasource.JavaSource;
import de.java2html.javasource.JavaSourceParser;
import de.java2html.javasource.JavaSourceType;
import de.java2html.options.JavaSourceConversionOptions;
import de.java2html.options.JavaSourceStyleEntry;
import de.java2html.util.RGB;

import org.javascool.tools.FileManager;

/**
 * Convertit une portion de source Java ou Jvs en Html colorisé.
 * <p>
 * Note: utilise une version patchée de <a
 * href="http://www.java2html.de">java2html</a>, disponible dans <a href=
 * "http://code.google.com/p/javascool/source/browse/work/lib/jvs2html.jar"
 * >jvs2html.jar</a>, qui doit être dans le CLASSPATH.
 * </p>
 * </p>
 * 
 * @see <a href="Jvs2Html.java.html">code source</a>
 * @serial exclude
 */
public class Jvs2Html {
	// @factory

	/**
	 * Lanceur de la conversion d'une portion de source Java ou Jvs en Html
	 * colorisé.
	 * 
	 * @param usage
	 *            <tt>java org.javascool.builder.Jvs2Html input-file [output-file]</tt>
	 */
	public static void main(String[] usage) {
		// @main
		if (usage.length > 0) {
			FileManager.save(usage.length > 1 ? usage[1] : "stdout:",
					Jvs2Html.run(FileManager.load(usage[0])));
		}
	}

	/**
	 * Convertit une portion de source Java ou JVs en Html colorisé..
	 * 
	 * @param code
	 *            Le code Java ou Jvs.
	 * @return Le code Html généré.
	 * @throws RuntimeException
	 *             Si une erreur d'entrée-sortie s'est produite lors de
	 *             l'éxecution.
	 */
	public static String run(String code) {
		try {
			final StringReader stringReader = new StringReader(code);
			final JavaSource source = new JavaSourceParser()
					.parse(stringReader);
			final JavaSource2HTMLConverter converter = new JavaSource2HTMLConverter();
			final JavaSourceConversionOptions options = JavaSourceConversionOptions
					.getDefault();
			options.getStyleTable().put(JavaSourceType.KEYWORD,
					new JavaSourceStyleEntry(RGB.ORANGE, true, false));
			final StringWriter writer = new StringWriter();
			converter.convert(source, options, writer);
			return "<pre>"
					+ writer.toString().replace("\n", "")
							.replace("<br/>", "\n")
							.replace("&#160;&#160;&#160;&#160;", "\t")
					+ "</pre>";
		} catch (final IOException e) {
			throw new RuntimeException(e + " when converting: «" + code + "»");
		}
	}

	/**
	 * Convertit un répertoire de source Java vers un autre répertoire en source
	 * HTML Colorisé.
	 * 
	 * @param srcDir
	 *            Le répertoire source
	 * @param destDir
	 *            Le répertoire de destination
	 * @return true si la convertion a réussit
	 * @throws RuntimeException
	 *             si il y a une erreur durant la convertion
	 */
	public static boolean runDirectory(String srcDir, String destDir) {
		try {
			new File(srcDir);
			final File dest = new File(destDir);
			final String[] fileList = FileManager.list(srcDir);
			for (final String file : fileList) {
				if (file.endsWith(".java") || file.endsWith(".jvs")) {
					FileManager.save(dest.getCanonicalPath() + File.separator
							+ new File(file).getName() + ".html",
							Jvs2Html.run(FileManager.load(file)));
				}
			}
			return true;
		} catch (final Exception e) {
			throw new RuntimeException(e + " when converting: «" + srcDir + "»");
		}
	}

	private Jvs2Html() {
	}
}