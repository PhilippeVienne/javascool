package org.javascool.proglets.txtCode;

import static org.javascool.macros.Macros.*;
import java.awt.Color;
import javax.swing.JTextArea;
import java.lang.Character;
import java.net.Socket;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.EOFException;
import java.io.FileNotFoundException;

/** Définit les fonctions de la proglet.
 * @see <a href="Functions.java.html">source code</a>
 * @serial exclude
 * @author Christophe Béasse <oceank2@gmail.com>
 */
public class Functions {

  /** Renvoie l'instance de la proglet. */
  private static Panel getPane() {
    return getProgletPane();
  }  

  /*
   * Méthodes liées au filedump
   */
  
  public static void focusOnConsolePanel() {
     org.javascool.gui.Desktop.getInstance().focusOnConsolePanel();  
}

  private static FileReader fileR = null;
  private static FileWriter fileW = null;     
  

   
  public static void openFileReader(String nomFichier) {
    try {  
            fileR = new FileReader(nomFichier);
    }catch (FileNotFoundException e) { 
    }            
  }
  
  public static int readNextCode() {
    int c = -1;
    
    if (fileR == null)
	    throw new RuntimeException("Le fichier READER n'est pas ouvert ! ");  
	    
    try {
        c = fileR.read();
    }catch (EOFException e) { 
    }catch (IOException e) { 
    }        	           
    return c;
  }  
  
  public static void closeFileReader() {
    try {  
            fileR.close();
    }catch (IOException e) { 
    }            
  }  
  
public static void filedump(String nomFichier){

  int [] buffer = new int[16];

  openFileReader(nomFichier);
  int c;
  int offset = 0;
  int i = 0;
  affiche("=======================================\n");	
  affiche("txtCode fileDump :["+nomFichier+"]\n");	
  affiche("=======================================\n");
  sautDeLigne();  
  affiche("      00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F \n");
  while ((c = readNextCode()) != -1) {
      buffer[i++] = c;
      if (i==16) {
         afficheligne(buffer,i,offset);
			 offset++; 		 
       i = 0;
      } // End if            	
  }// End while
  
  afficheligne(buffer,i,offset);       

  closeFileReader();
}    
  
  static void afficheligne(int [] buffer, int i, int offset) {
    if (i>0) {
        affiche(String.format("%04x",offset)+"  ");
			  for (int j=0; j < i ; j++){
            	  affiche(code2HexStr(buffer[j]));
            	  affiche(" ");
			  } 
			  for (int j=i; j < 16 ; j++){
            	  affiche("   ");
			  }                    	
			  affiche(" ");
			  for (int j=0; j < i ; j++){
			    if ((buffer[j]>31) && (buffer[j]<127))
              affiche(code2CarStr(buffer[j]));
          else affiche(".");
			 }
			 sautDeLigne();	 
    } // End if 
  
  
  }
  
  public static void openFileWriter(String nomFichier) {
    try {  
            fileW = new FileWriter(nomFichier);
    }catch (IOException e) { 
    }            
  }
  
  public static void writeNextCode(int i) {
    
    if (fileW == null)
	    throw new RuntimeException("Le fichier WRITER n'est pas ouvert ! ");  
	    
    try {
        fileW.write(i);
    }catch (IOException e) { 
    }        	           
  }  
  
  public static void closeFileWriter() {
    try {  
            fileW.close();
    }catch (IOException e) { 
    }            
  }    


  /** Permet de positionner une marque sur la grille du panel de la proglet 
   * @param i Position horizontale entre 1 et 3.
   * @param j Position verticale entre 1 et 3.
   * @param mark Marque du tictactoe soit 'X', soit 'O'
   */
   
  public static void affiche(String str) {
	  getPane().textArea.append(str);
  }
     
  public static void affiche(char c) {
	  getPane().textArea.append(Character.toString(c));
  }
  
  public static void affiche(int n) {
	  getPane().textArea.append(Integer.toString(n));
  }  
  
  public static void sautDeLigne() {
	  getPane().textArea.append("\n");
  }  
  
  public static String code2HexStr(int code) {
    if (code < 16) return ("0"+Integer.toHexString(code));
    else return Integer.toHexString(code); 
  
  }
  
  public static String code2CarStr(int code) {

    return(Character.toString((char) code));
  
  }  

  /** Remets à zéro la zone d'affichage du proglet */
  public static void resetConsole() {

	getPane().textArea.setText(" ");
	getPane().textArea.setForeground(Color.BLACK);    

  }
  
  /*
   * Méthodes liées à la connection serveur.
   */
  private static SocketServer server = new SocketServer();

  /** Ouverture du socket server. 
   * @see SocketServer#open(int)
   */  
  public static void openSocketServer(int numport) {
    server.open(numport);
  }
  /** Permet de récupérer un message via le socket server. */  
  public static String getMessageViaSocketServer() {
    return server.getMessage();
  }
  /** Permet d'écrire un message sur le socket server. */  
  public static void sendMessageViaSocketServer(String text) {
    server.sendMessage(text);
  }
  /** Renvoie la socket elle-même pour accéder aux fonctions bas-niveau. */
  public static Socket getSocketServer() {
    return server.getSocket();
  }
  /** Fermeture du socket server.  */  
  public static void closeSocketServer() {
    server.close();
  }

  /*
   * Méthodes liées à la connection serveur.
   */
  private static SocketClient client = new SocketClient();

  /** Ouverture du socket client. 
   * @see SocketClient#open(String, int)
   */  
  public static void openSocketClient(String hostname, int numport) {
    client.open(hostname, numport);
  }
  /** Permet de récupérer un message via le socket client. */  
  public static String getMessageViaSocketClient() {
    return client.getMessage();
  }
  /** Permet d'écrire un message sur le socket client. */  
  public static void sendMessageViaSocketClient(String text) {
    client.sendMessage(text);
  }
  /** Renvoie la socket elle-même pour accéder aux fonctions bas-niveau. */
  public static Socket getSocketClient() {
    return client.getSocket();
  }
  /** Fermeture du socket client.  */  
  public static void closeSocketClient() {
    client.close();
  }  


} // class functions
