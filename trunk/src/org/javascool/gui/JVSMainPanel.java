/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javascool.gui;

import java.awt.BorderLayout;
import java.util.HashMap;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.javascool.JVSFile;

/**
 *
 * @author philien
 */
public class JVSMainPanel extends JPanel {

    private JVSToolBar toolbar = new JVSToolBar();
    private JVSFileEditorTabs editorTabs = new JVSFileEditorTabs();
    private HashMap<String, Boolean> haveToSave = new HashMap<String, Boolean>();

    public JVSMainPanel() {
        this.setVisible(true);
        this.setupViewLayout();
        this.setupToolBar();
        this.setupMainPanel();
    }

    /** Setup the Border Layout for the JPanel */
    private void setupViewLayout() {
        BorderLayout layout = new BorderLayout();
        this.setLayout(layout);
    }

    /** Setup the ToolBar */
    private void setupToolBar() {
        // Add Buttons here
        this.add(toolbar, BorderLayout.NORTH);
    }

    /** Setup Main Panel */
    private void setupMainPanel() {
        //tabs.add("Test", JvsMain.logo16, new JPanel());
        this.newFile();
        this.add(editorTabs, BorderLayout.CENTER);
    }

    /** Get the toolbar
     * @return The toolbar
     */
    public JVSToolBar getToolBar() {
        return toolbar;
    }

    public void newFile() {
        String fileId = this.getEditorTabs().openNewFile();
        this.haveToSave.put(fileId, true);
    }

    public void openFile() {
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this.getParent());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getAbsolutePath();
            String fileId = this.getEditorTabs().open(path);
            this.haveToSave.put(fileId, false);
        } else {
        }
    }

    public void saveFile() {
        this.getEditorTabs().saveCurrentFile();
    }
    
    public void mustSave(String fileId){
        this.haveToSave.put(fileId, true);
    }

    public Boolean close() {
        String id = "";
        Boolean[] can_close = new Boolean[this.haveToSave.keySet().toArray().length];
        int i = 0;
        for (Object fileId : this.haveToSave.keySet().toArray()) {
            id = (String) fileId;
            if (this.haveToSave.get(id)) {
                switch (this.saveFileIdBeforeClose(id)) {
                    case 1:
                        can_close[i] = true;
                        break;
                    case 0:
                        can_close[i] = false;
                        break;
                    case -1:
                        return false;
                }
            } else {
                can_close[i] = true;
            }
            if (can_close[i]) {
                this.getEditorTabs().closeFile(id);
            }
            i++;
        }
        for (Boolean can_close_r : can_close) {
            if (can_close_r == false) {
                return false;
            }
        }
        return true;
    }

    private int saveFileIdBeforeClose(String fileId) {
        final JFileChooser fc = new JFileChooser();
        JVSFile file = this.getEditorTabs().getFile(fileId);
        int result = JOptionPane.showConfirmDialog(this.getParent(),
                "Voulez vous enregistrer " + file.getName() + " avant de quitter ?");
        if (result == JOptionPane.YES_OPTION) {
            if (this.getEditorTabs().saveFile(fileId)) {
                this.haveToSave.put(fileId, false);
                return 1;
            } else {
                return 0;
            }
        } else if (result == JOptionPane.NO_OPTION) {
            return 1;
        } else {
            this.haveToSave.put(fileId, true);
            return -1;
        }
    }

    private JVSFileEditorTabs getEditorTabs() {
        return this.editorTabs;
    }
}
