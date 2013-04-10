---
layout: default
title: Développer autour de Java'sCool 5
---

## Le fichier *Translator.java* pour étendre le langage.

* Le fichier *Translator.java* définit une classe publique qui a pour parent *org.javascool.core.Translator* et permet de définir des variantes du langage pour une proglet donnée.

* La traduction de Java simplifié (dit Jvs) en Java se fait à l'aide d'expression régulières appliquées sur tout le texte source.

* La classe permet aussi de spécifier des import Java spécifique de la proglet.

* Par exemple, le [translateur de la proglet syntheSons](http://javascool.gforge.inria.fr/?page=proglets&action=show&id=syntheSons&helpFile=api/org/javascool/proglets/syntheSons/Translator.java.html) définit : 
  * avec *getImports()* l'import automatiquement des classes utiles;
  * avec *translate()* la traduction de la construction *@tone* en code Java

* Par exemple, le [translateur de la proglet wwwIOI](http://javascool.gforge.inria.fr/?page=proglets&action=show&id=wwwIOI&helpFile=api/org/javascool/proglets/wwwIOI/Translator.java.html) définit :
  * l'analyse du texte source pour ajouter la construction *repeat ( .cond. ) { .expr. }*

* Se référer à la [documentation de la classe](http://javascool.gforge.inria.fr/v4//?page=api&api=org/javascool/core/Translator.html) pour plus de détails et nous [contacter](../../wpages/contact.html) pour co-développer cet élément.