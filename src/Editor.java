import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.plaf.metal.*;
import javax.swing.text.*;

class Editor extends JFrame implements ActionListener, KeyListener {
    // Text component
    JLabel errorLabel;
    JTextPane t;
    JPanel panelNorte;
    JPanel panelSur;
    JEditorPane errorPanel;
    // Frame
    JFrame f;
    JScrollPane textEditor;
    JScrollPane errorEditor;
    private boolean newLine = false;

    // Constructor
    Editor()
    {
        // Create a frame
        f = new JFrame("Editor");

        try {
            // Set metal look and feel
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

            // Set theme to ocean
            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
        }
        catch (Exception e) {
        }

        // Text component
        errorLabel = new JLabel("Error Log");
        t = new JTextPane();
        ((AbstractDocument) t.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace( FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws  BadLocationException {
                SimpleAttributeSet newAttrs = new SimpleAttributeSet();
                if (!newLine) {
                    String letra = text.substring(text.length()-1);
                    if (letra.equalsIgnoreCase("a")) {
                        StyleConstants.setForeground(newAttrs, Color.RED);
                        newLine = true;
                        attrs = newAttrs;
                    } else if (letra.equalsIgnoreCase("e")) {
                        newLine = true;
                        StyleConstants.setForeground(newAttrs, Color.GREEN);
                        attrs = newAttrs;
                    } else if (letra.equalsIgnoreCase("i")) {
                        newLine = true;
                        StyleConstants.setForeground(newAttrs, Color.BLUE);
                        attrs = newAttrs;
                    } else if (letra.equalsIgnoreCase("o")) {
                        newLine = true;
                        StyleConstants.setForeground(newAttrs, Color.MAGENTA);
                        attrs = newAttrs;
                    } else if (letra.equalsIgnoreCase("u")) {
                        newLine = true;
                        StyleConstants.setForeground(newAttrs, Color.CYAN);
                        attrs = newAttrs;
                    }

                } else {
                    if (text.startsWith("#", text.length() - 1)) {
                        StyleConstants.setForeground(newAttrs, Color.DARK_GRAY);
                        newLine = false;
                        attrs = newAttrs;
                    }
                }
                super.replace(fb, offset, length, text, attrs);
            }
        });

        t.addKeyListener(this);
        errorPanel = new JEditorPane();
        errorPanel.setContentType("text/html");
        panelNorte = new JPanel();
        panelSur = new JPanel();
        textEditor = new JScrollPane(t);
        errorEditor = new JScrollPane(errorPanel);
        errorPanel.setEditable(false);
        errorEditor.setPreferredSize(new Dimension(680, 280));
        textEditor.setPreferredSize(new Dimension(680, 400));
        errorLabel.setLabelFor(errorEditor);
//        JPanel panelSur = new JPanel();
//        panelSur.add(errorLabel, errorEditor);
        // Create a menubar
        JMenuBar mb = new JMenuBar();

        // Create amenu for menu
        JMenu m1 = new JMenu("File");

        // Create menu items
        JMenuItem mi1 = new JMenuItem("New");
        JMenuItem mi2 = new JMenuItem("Open");
        JMenuItem mi3 = new JMenuItem("Save");
        JMenuItem mi9 = new JMenuItem("Print");

        // Add action listener
        mi1.addActionListener(this);
        mi2.addActionListener(this);
        mi3.addActionListener(this);
        mi9.addActionListener(this);

        m1.add(mi1);
        m1.add(mi2);
        m1.add(mi3);
        m1.add(mi9);

        // Create a menu for menu
        JMenu m2 = new JMenu("Edit");

        // Create menu items
        JMenuItem mi4 = new JMenuItem("Cut");
        JMenuItem mi5 = new JMenuItem("Copy");
        JMenuItem mi6 = new JMenuItem("Paste");
        JMenuItem mi7 = new JMenuItem("Compile");
        JMenuItem mi8 = new JMenuItem("Run");
        // Add action listener
        mi4.addActionListener(this);
        mi5.addActionListener(this);
        mi6.addActionListener(this);
        mi7.addActionListener(this);
        mi8.addActionListener(this);

        m2.add(mi4);
        m2.add(mi5);
        m2.add(mi6);
        m2.add(mi7);
        m2.add(mi8);

        JMenuItem mc = new JMenuItem("close");

        JMenu m3 = new JMenu("Help");
        JMenuItem m20 = new JMenuItem("About");
        m3.add(m20);

        mc.addActionListener(this);

        mb.add(m1);
        mb.add(m2);
        mb.add(m3);
        mb.add(mc);

        errorPanel.setText("<b style='color:red;'>hola</b>");
        f.setJMenuBar(mb);
//        f.add(t);
//        f.add(errorPanel);
        panelNorte.add(textEditor);
        panelSur.add(errorLabel);
        panelSur.add(errorEditor);
        f.add(panelNorte, BorderLayout.NORTH);
//        f.add(errorLabel, BorderLayout.SOUTH);
        f.add(panelSur, BorderLayout.SOUTH);
        f.setSize(700, 760);
        f.setVisible(true);
    }

    // If a button is pressed
    public void actionPerformed(ActionEvent e)
    {
        String s = e.getActionCommand();

        if (s.equals("Cut")) {
            t.cut();
        }
        else if (s.equals("Copy")) {
            t.copy();
        }
        else if (s.equals("Paste")) {
            t.paste();
        }
        else if (s.equals("Save")) {
            // Create an object of JFileChooser class
            JFileChooser j = new JFileChooser("f:");

            // Invoke the showsSaveDialog function to show the save dialog
            int r = j.showSaveDialog(null);

            if (r == JFileChooser.APPROVE_OPTION) {

                // Set the label to the path of the selected directory
                File fi = new File(j.getSelectedFile().getAbsolutePath());

                try {
                    // Create a file writer
                    FileWriter wr = new FileWriter(fi, false);

                    // Create buffered writer to write
                    BufferedWriter w = new BufferedWriter(wr);

                    // Write
                    w.write(t.getText());

                    w.flush();
                    w.close();
                }
                catch (Exception evt) {
                    JOptionPane.showMessageDialog(f, evt.getMessage());
                }
            }
            // If the user cancelled the operation
            else
                JOptionPane.showMessageDialog(f, "the user cancelled the operation");
        }
        else if (s.equals("Print")) {
            try {
                // print the file
                t.print();
            }
            catch (Exception evt) {
                JOptionPane.showMessageDialog(f, evt.getMessage());
            }
        }
        else if (s.equals("Open")) {
            // Create an object of JFileChooser class
            JFileChooser j = new JFileChooser("f:");

            // Invoke the showsOpenDialog function to show the save dialog
            int r = j.showOpenDialog(null);

            // If the user selects a file
            if (r == JFileChooser.APPROVE_OPTION) {
                // Set the label to the path of the selected directory
                File fi = new File(j.getSelectedFile().getAbsolutePath());

                try {
                    // String
                    String s1 = "", sl = "";

                    // File reader
                    FileReader fr = new FileReader(fi);

                    // Buffered reader
                    BufferedReader br = new BufferedReader(fr);

                    // Initialize sl
                    sl = br.readLine();

                    // Take the input from the file
                    while ((s1 = br.readLine()) != null) {
                        sl = sl + "\n" + s1;
                    }

                    // Set the text
                    t.setText(sl);
                }
                catch (Exception evt) {
                    JOptionPane.showMessageDialog(f, evt.getMessage());
                }
            }
            // If the user cancelled the operation
            else
                JOptionPane.showMessageDialog(f, "the user cancelled the operation");
        }
        else if (s.equals("New")) {
            t.setText("");
        }
        else if (s.equals("Close")) {
            f.setVisible(false);
        } else if (s.equals("Compile")) {
            String text = t.getText();
            text = text.replaceAll("(?m)^[ \t]*\r?\n", "");
            text = text.trim();
            String mensaje = "Error de sintaxis";
            String[] letras = text.split("#");
            if(letras[0].equals("Inicio")){
                letras[1] = letras[1].trim();
                letras[1] = letras[1].replaceAll("(?m)^[ \t]*\r?\n", "");
                if(letras[1].startsWith("Ensaje(") && letras[1].endsWith(")")) {
                    letras[2] = letras[2].replaceAll("(?m)^[ \t]*\r?\n", "");
                    if (text.endsWith("Out#")) {
                        mensaje = letras[1].substring(7, letras[1].length()-1);
                    }
                }
            }
            errorPanel.setText("<span color='red'>"+mensaje+"</span>");
        }
//

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
