package org.javascool.proglets.grapheEtChemins;

/** Définit les fonctions de la proglet.
 *
 * @see <a href="Functions.java.html">code source</a>
 * @serial exclude
 */
public class Functions {
  private static final long serialVersionUID = 1L;
  /** Renvoie l'instance de la proglet pour accéder à ses éléments. */
  private static grapheEtChemins getPane() {
    return org.javascool.macros.Macros.getProgletPane();
  }

  /** Ajoute ou modifie un noeud au graphe (modifie dans le cas ou meme nom employé et différentes coordonnées).
   * @param n Nom du noeud.
   * @param x Abcisse du noeud.
   * @param y Ordonnée du noeud.
   */
  public static void addNode(String n, int x, int y) {
    getPane().myGraph.addNode(n, x, y);
  }
  /** Renvoie l'objet Noeud à partir de son nom.
   * @param n Nom du noeud.
   * @return objet Node.
   */
  public static Node getNode(String n) {
    Node N_;
    N_ = getPane().myGraph.getNode(n);
    return N_;
  }
  /** Cherche noeud plus proche d'une position.
   * @param x Abcisse position.
   * @param y Ordonnée position.
   * @return Nom du noeud.
   */
  public static String getClosestNode(float x, float y) {
    String n_ = null;
    n_ = getPane().myGraph.getClosestNode(x, y);
    return n_;
  }
  /** Détruit un noeud au graphe si il existe.
   * @param n Nom du noeud.
   */
  public static void removeNode(String n) {
    getPane().myGraph.removeNode(n);
  }
  /** Donne la liste de tous les noeuds.
   * @return La liste des noms des noeuds.
   */
  public static String[] getAllNodes() {
    String[] ListN_ = new String[50];
    ListN_ = getPane().myGraph.getAllNodes();
    return ListN_;
  }
  /** Donne la liste des noeuds en lien avec un noeud donné.
   * @param n Nom du noeud dont on veut les noeuds en lien.
   * @return La liste des noms des noeuds en lien avec le noeud donné.
   */
  public static String[] getNodes(String n) {
    String[] ListN_ = new String[50];
    ListN_ = getPane().myGraph.getNodes(n);
    return ListN_;
  }
  /** Ajoute ou modifie un lien entre deux noeuds (modifie dans le cas ou memes noeuds et différent poids attribué).
   * @param nA Premier noeud du lien.
   * @param nB Deuxième noeud du lien.
   * @param p Poids du lien.
   */
  public static void addLink(String nA, String nB, double p) {
    getPane().myGraph.addLink(nA, nB, p);
  }
  /** Ajoute ou modifie un lien entre deux noeuds (modifie dans le cas ou memes noeuds et différent poids attribué).
   * @param nA Premier noeud du lien.
   * @param nB Deuxième noeud du lien.
   * ici poids du lien = distance euclidienne entre les deux noeuds.
   */
  public static void addLink(String nA, String nB) {
    getPane().myGraph.addLink(nA, nB);
  }
  /** Détruit un lien entre deux noeuds si il existe.
   * @param nA Premier noeud du lien.
   * @param nB Deuxième noeud du lien.
   */
  public static void removeLink(String nA, String nB) {
    getPane().myGraph.removeLink(nA, nB);
  }
  /** Affirme si il y a lien entre 2 noeuds.
   * @param nA Premier noeud du lien.
   * @param nB Deuxième noeud du lien.
   * @return "vrai" ou "faux".
   */
  public static boolean isLink(String nA, String nB) {
    boolean a_ = false;
    a_ = getPane().myGraph.isLink(nA, nB);
    return a_;
  }
  /** Donne le poids d'un lien entre deux noeuds.
   * @param nA Premier noeud du lien.
   * @param nB Deuxième noeud du lien.
   * @return Le poids du lien où 0 si il n'y a pas de lien.
   */
  public static double getLink(String nA, String nB) {
    double p_ = 0;
    p_ = getPane().myGraph.getLink(nA, nB);
    return p_;
  }
  /**   Algorithme de Dijkstra
   * @param nStart Noeud départ.
   * @param nEnd Noeud final.
   */
  public static void dijkstra(String nStart, String nEnd) {
    getPane().myGraph.dijkstra(nStart, nEnd);
  }
}

