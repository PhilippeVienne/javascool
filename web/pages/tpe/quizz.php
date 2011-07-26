<?php 
    showBrowser(
        array(
            array("Java's Cool","?"),
            array("Ressources","?page=resources"),
            array("TPE","?page=tpe"),
            array("Quizz","")
        ),
        array(
            array("Démos","?page=tpe&action=demos"),
            array("Exemples","?page=tpe&action=exemples"),
            array("Idées","?page=tpe&action=idees"),
            array("Interventions","?page=tpe&action=interventions"),
            array("Méthode","?page=tpe&action=methode"),
            array("Pépites","?page=tpe&action=pepites"),
            array("Revues","?page=tpe&action=revues")
        )
    );
?>


<p>Les partenaires de <a href="http://sophia-stic.polytechnice.fr">Sophi@Stic</a> s'associent à <a href="http://www.vsp.fr">VSP</a> 
pour vous proposer ces quizz-vidéo afin de découvrir quelques facettes des travaux scientifiques de notre Campus 
(rien d'exhaustif, ni d'institutionnel, ici: juste une manière vivante et collégiale de vous montrer que nous travaillons, ensemble, 
pour développer les connaissances et les technologies de demain).</p>

<script type="text/javascript" src="http://javascool.gforge.inria.fr/v3/documents/speed-dating-09/popupquestion.js"></script>	
<script type="text/javascript" src="http://javascool.gforge.inria.fr/v3/documents/speed-dating-09/dataquestion.js"></script>	

<link href="http://javascool.gforge.inria.fr/v3/documents/speed-dating-09/popupquestion.css" rel="stylesheet" type="text/css"/>

<table align="center" witdh="90%"><tr>

<script language="javascript">

  function show(i) {
    var data = sdata[i];
    document.getElementById('fiche').innerHTML = 
     '<a href="'+data['url']+'">'+data['prenom']+' <b><i>'+data['nom']+'</i></b></a><br /><b>'+data['titre']+'</b><br />' +
     '<div align="center"><a href="javascript:quizzOpen(sdata['+i+']);"><img height="100" align="center" src="'+data['img']+'" alt="A vous de jouer"/></div>' +
     '<div align="center">(cliquer sur l\'imagette pour avoir le quizz,</div>' +
     '<div align="right">ou accéder <i><a href="'+data['link']+'">directement</a></i> à la vidéo).</div>' +
     (data['plus'].length > 0 && false /* get_search('mode') == 'plus' */ ? '<br /><div>En savoir plus: <br /><i>'+data['plus']+'</i></div>' : '');
  }

  document.writeln('<td width="50%" align="left"><div id="fiche">.. / ..</div></td><td width="50%" valign="top"><table witdh="100%"> <tr>');
  for(var i = 0; i < sdata.length; i = i+1) {
    var data = sdata[i];
    document.writeln('<td width="25%"><a href="javascript:show('+i+');"><img height="100" align="center" src="'+data['img']+'" alt="A vous de jouer"/></a></td>');
    if (i % 3 == 2) 
      document.writeln('</tr><tr>');
  }
  document.writeln('</tr></table></td>');

</script>

</tr></table>

<br /><br /><br /><table align="center"><tr>
<td><a href="http://www.eurecom.fr"><img height="35" width="120" src="http://javascool.gforge.inria.fr/v3/documents/speed-dating-09/logo-eurecom.jpg" alt="EURECOM"/></a></td>
<td><a href="http://www.polytech.unice.fr"><img height="35" width="120" src="http://portail.unice.fr/jahia/txt/inc/img/logo_unice.gif" alt="POLYTECH"/></a></td>
<td><a href="http://www.inria.fr/sophia"><img height="35" width="120" src="http://javascool.gforge.inria.fr/v3/documents/speed-dating-09/logo-inria-sophia.jpg" alt="INRIA"/></a></td>
<td><a href="http://www.i3s.unice.fr"><img height="35" width="120" src="http://www.i3s.unice.fr/I3S/images/index/i3s.jpg" alt="I3S"/></a></td>
<td width="31"></td>
<td><a href="http://www.vsp.fr"><img height="35" width="100" src="http://javascool.gforge.inria.fr/v3/documents/speed-dating-09/logo-vsp.jpg" alt="VSP"/></a></td>
</tr></table>
