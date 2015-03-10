/*******************************************************************************
*     patrick.raffinat@univ-pau.fr, Copyright (C) 2013.  All rights reserved.  *
*******************************************************************************/
package org.javascool.proglets.plurialgo.langages.javascool;

import java.util.*;
import org.javascool.proglets.plurialgo.divers.*;
import org.javascool.proglets.plurialgo.langages.modele.*;


/**
 * Cette classe hérite de la classe homonyme du modèle.
*/
public class Operation extends ModeleOperation {
	
	public Operation() {
	}
	
	public void ecrire(Programme prog, StringBuffer buf, int indent) {
		Variable retour = (Variable) getRetour();
		Divers.indenter(buf, indent);
		if (isProcedure()) Divers.ecrire(buf, "void");	
		if (isFonction()) prog.ecrireType(buf, retour);	
		Divers.ecrire(buf, " ");
		Divers.ecrire(buf, nom);
		Divers.ecrire(buf, "(");
		for (Iterator<ModeleParametre> iter=parametres.iterator(); iter.hasNext();) {
			Parametre param = (Parametre) iter.next();
			if (param.isInOut()) {
				if (param.isSimple()) {
					prog.ecrireWarning("sous-programme " + this.nom + " : " + param.nom +" ne peut être parametre d'entree-sortie");
				}
			}
			if (param.isOut()) {
				if (param.isSimple()) {
					prog.ecrireWarning("sous-programme " + this.nom + " : " + param.nom +" ne peut être parametre de sortie");
				}
			}
			param.ecrire(prog, buf);
			if (iter.hasNext()) Divers.ecrire(buf, ", ");
		}
		Divers.ecrire(buf, ")");	
		Divers.ecrire(buf, " { ");
		for (Iterator<ModeleVariable> iter=variables.iterator(); iter.hasNext();) {
			Variable var = (Variable) iter.next();
			var.ecrire(prog, buf, indent+1);
		}
		if (isFonction()) {
			retour.ecrire(prog, buf, indent+1);
		}
		for (Iterator<ModeleInstruction> iter=instructions.iterator(); iter.hasNext();) {
			Instruction instr = (Instruction) iter.next();
			instr.ecrire(prog, buf, indent+1);
		}
		if (isFonction()) {
			Divers.ecrire(buf, "return " + retour.nom + ";", indent+1);
		}
		Divers.ecrire(buf, "} ", indent);
	}
	
	public void ecrire(ModeleProgramme prog, StringBuffer buf, int indent) {
		this.ecrire((org.javascool.proglets.plurialgo.langages.javascool.Programme)prog, buf, indent);
	}
	
}
