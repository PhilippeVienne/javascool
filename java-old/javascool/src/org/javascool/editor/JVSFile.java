//@deprecated voir org.javascool.gui.JVSFile
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javascool.editor;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author philien
 */
public final class JVSFile {

    public static String defaultCode = "void main(){\n"
            + "\t\n"
            + "}";
    /** The JSon Data of file 
    private JSONObject json; */
    private String fileContent;
    /** The text content of file */
    private String code;
    /** The name of the file */
    private String name;
    /** The path to the file */
    private String path;
    /** The file instance */
    private File file;
    /** Proglet liked to this document */
    private String proglet;

    /** Open a new empty file */
    public JVSFile() {
        this("");
    }

    /** Open a new file from a text
     * @param text The text of new file
     */
    public JVSFile(String text) {
        this(text, false);
    }

    /** Open a file from an url
     * Don't forget to put fromurl to true
     * @param url The url of file
     * @param fromurl  True for open from an url
     */
    public JVSFile(String url, Boolean fromurl) {
        if (!fromurl) {
            this.code = url;
            this.name = "Nouveau fichier";
            this.path = "";
            this.proglet = "default";
            try {
                this.file = File.createTempFile("JVS_TMPFILE_", ".jvs");
                this.file.deleteOnExit();
                this.path = this.file.getAbsolutePath();
            } catch (IOException ex) {
                Logger.getLogger(JVSFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            File file_to_open = new File(url);
            this.name = file_to_open.getName();
            this.path = file_to_open.getAbsolutePath();
            try {
                this.fileContent = JVSFile.readFileAsString(this.path);
            } catch (IOException ex) {
                this.fileContent = "";
            }
            // Read file line by line
            String proprity, value, realCode = "";
            for (String line : fileContent.split("\n")) {
                if (line.startsWith("//$jvs#")) {
                    line = line.replaceFirst("\\/\\/\\$jvs\\#", "");
                    proprity = line.split(":")[0];
                    value = line.split(":", 2)[1];
                    if (proprity.equalsIgnoreCase("proglet")) {
                        this.setProglet(value);
                    }
                } else {
                    realCode += line + "\n";
                }
            }
            this.code = realCode;
            this.file = file_to_open;
        }
        this.refreshData();
    }

    /** Check if file is in tempory memory */
    public Boolean isTmp() {
        return (this.file.getName().startsWith("JVS_TMPFILE_"));
    }

    /** Save file */
    public Boolean save() {
        try {
            this.refreshData();
            FileWriter fstream = new FileWriter(this.getPath());
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(this.fileContent);
            out.close();
            return true;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return false;
    }

    /** Get the file name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /** Set the file name
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /** Get the path to file
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /** Set a new path for the file
     * Use save() to write the file into the new path
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
        this.file = new File(path);
    }

    /** Get the file Instance in memory
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @return the proglet
     */
    public String getProglet() {
        if (this.proglet.equals("default")) {
            return "";
        }
        return proglet;
    }

    /**
     * @param proglet the proglet to set
     */
    public void setProglet(String proglet) {
        this.proglet = proglet;
        this.refreshData();
    }

    /** Get the code in the file
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /** Set the code
     * !! WARNING !! It no write the text to the file, it just save it into the object use save() insted.
     * @param code the text to set
     */
    public void setCode(String code) {
        this.code = code;
        this.refreshData();
    }

    /** Refresh the data to save file */
    private void refreshData() {
        String fileToSave = "";
        fileToSave += "//$jvs#proglet:" + this.proglet+"\n";
        fileToSave += this.getCode();
        this.fileContent = fileToSave;
    }

    /** Read a file */
    public static String readFileAsString(String filePath) throws java.io.IOException {
        byte[] buffer = new byte[(int) new File(filePath).length()];
        BufferedInputStream f = new BufferedInputStream(new FileInputStream(filePath));
        f.read(buffer);
        return new String(buffer);
    }
}