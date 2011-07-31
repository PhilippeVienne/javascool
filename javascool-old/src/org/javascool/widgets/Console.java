/**************************************************************
 * Philippe VIENNE, Copyright (C) 2011.  All rights reserved. *
 **************************************************************/
package org.javascool.widgets;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.javascool.gui.JVSMainPanel;
import org.javascool.gui.JVSToolBar;
import org.javascool.tools.Macros;

/**
 * Class Console
 * @author Philippe Vienne
 */
public class Console extends JPanel {

    private static final long serialVersionUID = 1L;
  
    /** Flag to say if a program is running */
    private static boolean running;
    /** The current JVS Program */
    private static Runnable program;
    /** The current Thread */
    private static Thread runThread;
    /** The timer Thread */
    private static Thread timeThread;
    /** The new console */
    private static JTextArea outputPane = Console.getConsoleTextPane();
    /** ScrollPane for the console */
    private static JScrollPane scrolledOutputPane;
    /** ToolBar for the console */
    private static JVSConsoleToolBar toolbar;

    /** The top tool bar in the console */
    private static class JVSConsoleToolBar extends JVSToolBar {
       private static final long serialVersionUID = 1L;

        public JVSConsoleToolBar() {
            super(true);
            init();
            this.setFloatable(false);
        }

        /** Reset all the tool bar */
        @Override
        public void reset() {
            setVisible(false);
            revalidate();
            this.removeAll();
            this.resetButtonsMaps();
            this.addTool("Effacer", "org/javascool/doc-files/icon16/erase.png", new Runnable() {

                @Override
                public void run() {
                    Console.clear();
                }
            });
            this.addSeparator();
            setVisible(true);
            revalidate();
        }

        /** Init the tool bar */
        private void init() {
            this.reset();
            this.add(new JLabel("Aucun programe à executer"));
        }

        /** Function call if no program is compiled */
        public void programNotCompiled() {
            this.reset();
            this.add(new JLabel("Aucun programme à executer"));
            this.revalidate();
        }

        /** Call after a success full comppilation */
        public void programCompiled() {
            this.reset();
            JVSMainPanel.getToolBar().enableStartButton();
            JVSMainPanel.getToolBar().enableExecTimer();
            this.revalidate();
        }

        /** Call when the program start */
        public void programRunning() {
            JVSMainPanel.getToolBar().desactivateStartButton();
            JVSMainPanel.getToolBar().disableCompileButton();
            JVSMainPanel.getToolBar().enableStopButton();
            this.revalidate();
        }

        /** Call when the program stop */
        public void afterRunning() {
            JVSMainPanel.getToolBar().disableStopButton();
            JVSMainPanel.getToolBar().enableStartButton();
            JVSMainPanel.getToolBar().enableCompileButton();
            this.revalidate();
        }

        /** Function call to set up time running */
        public void updateTimeRunning(int sec) {
            JVSMainPanel.getToolBar().updateTimer(sec);
        }

        /** Reset time running to 0 */
        public void resetTimeRunning() {
            JVSMainPanel.getToolBar().updateTimer(0);
        }
    }

    /** Construct a new Panel with the console */
    public Console() {
        BorderLayout layout = new BorderLayout();
        this.setLayout(layout);
        scrolledOutputPane = new JScrollPane(outputPane);
        this.add(Console.scrolledOutputPane, BorderLayout.CENTER);
        this.add(Console.getConsoleToolBar(), BorderLayout.NORTH);
        Console.redirectSystemStreams();
        this.setVisible(true);
    }

    /** Generate the ConsoleTextPane
     * @return The TextArea
     */
    private static JTextArea getConsoleTextPane() {
        JTextArea outPane = new JTextArea();
        outPane.setEditable(false);
        float[] bg = Color.RGBtoHSB(200, 200, 200, null);
        outPane.setBackground(Color.getHSBColor(bg[0], bg[1], bg[2]));
        return outPane;
    }

    /** Generate the ConsoleToolbar
     * @return The TextArea
     */
    private static JVSToolBar getConsoleToolBar() {
        toolbar = new JVSConsoleToolBar();
        return toolbar;
    }

    /** Clear the console */
    public static void clear() {
        outputPane.setText("");
    }

    public static boolean isInApplet=true;
    
    /** Start the current program */
    public static void startProgram() {
        isInApplet=false;
        System.err.println("In Console.startProgram");
   //     if (Console.runThread != null && isRunning()) Console.stopProgram();
        Console.running = true;
        if (JVSMainPanel.getCurrentProglet().hasPanel()) {
            System.err.println("I have a panel !");
            JVSMainPanel.getWidgetTabs().focusOnProgletPanel();
        } else System.err.println("No panel !");
        Console.toolbar.updateTimeRunning(0);

        Console.clear();

        Console.timeThread = new Thread(new Runnable() {

            @Override
            public void run() {
                Console.run(true);
                for (int t = 0; Console.running; t++) {
                    Console.toolbar.updateTimeRunning(t);
                    //FIXME interrupted ? why ?
                    Macros.sleep(1000);
                }
            }
        });
        Console.timeThread.start();
        Console.toolbar.programRunning();
    }

    /** Stop the current program */
    public static void stopProgram() {
        Console.running = false;
        Console.run(false);
        if (Console.timeThread != null) {
            try {
                Console.timeThread.interrupt();
            } catch (Exception e) {
             //   System.err.println("Erreur : " + e.getMessage());
            }
            Console.timeThread = null;
        }
        Console.timeThread = null;
        Console.runThread = null;
        Console.toolbar.afterRunning();
    }

    /** Runs/Stops the program.
     * @param start If true starts the proglet pupil's program, if defined. If false stops the proglet pupil's program.
     */
    private static void run(boolean start) {
        if (Console.runThread != null) {
            try {
                Console.runThread.interrupt();
            } catch (Exception e) {
                System.err.println("Erreur : " + e.getMessage());
            }
            Console.runThread = null;
        }
        if (start) {
            if (Console.program != null) {
                (Console.runThread = new Thread(new Runnable() {

                    @Override
                    public void run() {

                        try {
                            Console.program.run();
                            Console.stopProgram();
                        } catch (Throwable e) {
                            if (!"Programme arrêté !".equals(e.getMessage())) {
                                System.err.println(e.getMessage());
                                e.printStackTrace(System.err);
                            }
                            Console.stopProgram();
                        }

                    }
                })).start();
            } else {
                System.err.println("Undefined runnable");
            }
        }
    }

    /** Update console function
     * @param text The text to add
     */
    private static void updateTextPane(final String text) {
        Console.outputPane.append(text);
    }

    /** Redirect the console outPut */
    private static void redirectSystemStreams() {
        OutputStream out = new OutputStream() {

            @Override
            public void write(final int b) throws IOException {
                Console.updateTextPane(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                Console.updateTextPane(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };

        System.setOut(new PrintStream(out, true));
    }

    /** Get current compiled program
     * @return the program
     */
    public static Runnable getProgram() {
        return program;
    }

    /** Say if a program is running */
    public static Boolean isRunning() {
        return running;
    }

    /** Set a new program
     * @param aProgram the program to set
     */
    public static void setProgram(Runnable aProgram) {
        program = aProgram;
        Console.toolbar.programCompiled();
    }

    /** Get the current Thread
     * @return the runThread
     */
    public static Thread getRunThread() {
        return runThread;
    }
    
    public static void startAsApplet(Applet p) {
        Console.running=true;
        progletPanel=p;
    }
    
    public static Applet progletPanel;
}