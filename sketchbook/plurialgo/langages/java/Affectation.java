/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.java;

import java.util.StringTokenizer;
import org.javascool.proglets.plurialgo.divers.Divers;
import org.javascool.proglets.plurialgo.langages.modele.*;

/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Affectation extends ModeleAffectation {

	public Affectation() {
	}
	
	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		if (this.isAffTabSimple()) {
			this.ecrireTabSimple(prog, buf, indent);
		}
		else if (this.isAffMatSimple()) {
			this.ecrireMatSimple(prog, buf, indent);
		}
		else {
			Divers.ecrire(buf, var + " = " + expression + "; ", indent);
		}
	}	
	
	private void ecrireTabSimple(Programme prog, StringBuffer buf, int indent) {
		String txt = expression;
		int debut = txt.indexOf("{");
		int fin = txt.lastIndexOf("}");
		txt = txt.substring(debut+1, fin);
		StringTokenizer tok = new StringTokenizer(txt,",");
		int i = 0;
		while (tok.hasMoreTokens()) {
			String elem = tok.nextToken().trim();
			Divers.ecrire(buf, var + "[" + i + "]" + " = " + elem + "; ", indent);
			i = i+1;
		}
	}
	
	private void ecrireMatSimple(Programme prog, StringBuffer buf, int indent) {
		String txt = Divers.remplacer(expression, " ", "");
		int debut = txt.indexOf("{");
		int fin = txt.lastIndexOf("}");
		txt = txt.substring(debut+1, fin);
		txt = Divers.remplacer(txt, "},{", "@");
		StringTokenizer tok1 = new StringTokenizer(txt,"@");
		int i1 = 0;
		while (tok1.hasMoreTokens()) {
			StringTokenizer tok2 = new StringTokenizer(tok1.nextToken(),"{,}");
			int i2 = 0;
			while (tok2.hasMoreTokens()) {
				String elem = tok2.nextToken();
				Divers.ecrire(buf, var + "[" + i1 + "][" + i2 + "]" + " = " + elem + "; ", indent);
				i2 = i2+1;
			}
			i1 = i1+1;
		}
	}
}
