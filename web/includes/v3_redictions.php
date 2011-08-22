<?php // Mécanisme de redirection du site v3
include('includes/mailto.php');

// Effectue la redirection des liens et renvoie vraie si il y eu redirection et faux sinon
function v3_redictions($page) {
  global $v3_redictions_link_table, $v3_redictions_prefix_table, $v3_redictions_v4pages_table;
  // Redirection des liens qui sont dans la table
  if (array_key_exists($page, $v3_redictions_table)) {
    header("Location: ".$v3_redictions_table[$page]);
    return true;
  }
  // Redirection sur les prefix des liens v3
  foreach($v3_redictions_prefix_table as $prefix => $redirect)
    if (strncmp($page, $prefix, strlen($prefix)) == 0) {
      header("Location: ".$redirect);
      return true;
    }
  // Envoie d'un mail si une page bizarre est demandée
  if (!in_array($page, $v3_redictions_v4pages_table))
    mailto("mailto:thierry.vieville@inria.fr?subject=broken-link-on-javascool-v3-web", "Spurious link = $page");
  return false;
}

$v3_redictions_v4pages_table = array("api", "contact", "developers", "home", "proglets", "resources", "run");
		
$v3_redictions_prefix_table = array(
				    "api/" =>  "?page=api",
				    "doc%3Asketchbook" => "http://wiki.inria.fr/sciencinfolycee/JavaScool:ProgletsProcessing");
$v3_redictions_link_table = array(
			     "Accueil" => "?page=home",
			     "Menu" => "?page=home",
			     "Manifest" => "?page=home&action=manifest",
			     "Faq" => "?page=home&action=faq",
			     "Contacts" => "?page=contact",
			     "Contact" => "?page=contact",
			     "Crédits" => "?page=contact&action=credits",
			     "Lancement" => "?page=run",
			     "Telechargement" => "?page=run",
			     "Licence"  => "?page=run",
			     "Developpement" => "?page=developers",
			     "Ressources" => "?page=ressources",
			     "Activites" => "http://wiki.inria.fr/sciencinfolycee/JavaScool:Activites", 
			     "Ailleurs" => "http://wiki.inria.fr/sciencinfolycee/JavaScool:Ailleurs", 
			     "AutresInitiatives" => "http://wiki.inria.fr/sciencinfolycee/JavaScool:AutresInitiatives", 
			     "Cadrage" => "http://wiki.inria.fr/sciencinfolycee/JavaScool:Cadrage",
			     "Pepites" => "?page=resources&action=link-pepite",
			     "Perpites" => "?page=resources&action=link-pepite",
			     "doc%3A%2Fdocuments%2Fnos-ressources%2Fpepites.html" => "?page=resources&action=link-pepite",
			     "Revues" => "?page=resources&action=link-revues",
			     "doc%3A%2Fdocuments%2Fnos-ressources%2Frevues.html" => "?page=resources&action=link-revues",
			     "doc%3A%2Fdocuments%2Fquelques-t-p-e%2Findex.html" => "?page=resources&action=link-revues", 
			     "TPE-Quizz" => "?page=resources&action=link-quizz", 
			     "doc%3A%2Fdocuments%2Fspeed-dating-09%2Findex.html" => "?page=resources&action=link-quizz",
			     "TPE-Sujets" => "?page=resources&action=link-idees-tpe",
			     "TPE-Accueil" => "http://wiki.inria.fr/sciencinfolycee/JavaScool:TPE-Accueil",
			     "TPE-Demos" => "http://wiki.inria.fr/sciencinfolycee/JavaScool:TPE-Demos",
			     "TPE-Exemples" => "http://wiki.inria.fr/sciencinfolycee/JavaScool:TPE-Exemples",
			     "TPE-Interventions" => "http://wiki.inria.fr/sciencinfolycee/JavaScool:TPE-Interventions",
			     "TPE-Methode" => "http://wiki.inria.fr/sciencinfolycee/JavaScool:TPE-Methode",
			     "doc%3Adocuments%2Fquelques-t-p-e%2Faffiche_finale_inria.pdf" => "doc/documents/quelques-t-p-e/affiche_finale_inria.pdf",
			     "doc%3Adocuments%2Fquelques-t-p-e%2Fquelques-t-p-e.pdf" => "doc/documents/quelques-t-p-e/quelques-t-p-e.pdf",
			     );
?>

