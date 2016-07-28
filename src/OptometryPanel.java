
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * Created by Windows on 2016-07-25.
 */
public class OptometryPanel extends JFrame implements WindowFocusListener {
    public static final int MENU = 0;
    public static final int EDITOR = 1;
    public static final int EXIT = 2;


    int state = MENU;
    private Dimension screenSize;
    public Dimension windowSize ;

    JFileChooser browse = new JFileChooser("data");

    private JPanel menuScreen;
    private OpButton newFile;
    private OpButton editFile;


    private OpButton docAdd;

    private JPanel editorScreen;
    private TextEditor editor;
    private OpButton save;
    private OpButton savePdf;
    private OpButton email;
    private OpButton print;
    private OpButton back;


    private JComboBox<Person> people;
    private JPanel confirmScreen;


    OptometryListener op ;

    public OptometryPanel (){
        super();
        op = new OptometryListener();
        addWindowFocusListener(this);
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        windowSize = new Dimension(screenSize.width/2, screenSize.height/2);
        setPreferredSize(windowSize);
        setResizable(false);
        System.out.println(windowSize);

        //createMenu();
        //createEditor();
        try {
            setIconImage(ImageIO.read(new File("data/images/glasses.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void createMenu(){
        menuScreen = new JPanel();
        menuScreen.setSize(windowSize);
        MigLayout my = new MigLayout("", "[" + windowSize.getWidth()/4 + "px]", "[" + windowSize.getHeight()/6 + "px]");
        menuScreen.setLayout(my);

        Image image = null;
        try {
             image = ImageIO.read(new File("data/images/menu.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel jl = new JLabel(new ImageIcon(image));

        newFile = new OpButton("Write new Letter", op);

        editFile = new OpButton("Edit an Existing Letter", op);


        for (int i = 0; i < 24; i++){
            menuScreen.add(new JLabel(""), "cell " + i % 4 + " " + i / 4);
        }
        menuScreen.add(newFile, "cell 1 3, growx");
        menuScreen.add(editFile, "cell 2 3, growx");
        menuScreen.add(jl, "pos 0 0");
    }

    private void createEditor(){
        editorScreen = new JPanel();
        editorScreen.setBackground(Color.WHITE);
        MigLayout mg = new MigLayout("flowy");
        editorScreen.setLayout(mg);

        editor = new TextEditor("");
        docAdd = new OpButton("Add a new Doctor", op);
        save = new OpButton("Save", op);
        savePdf = new OpButton("Export", op);
        email = new OpButton("Email Letter", op);
        print = new OpButton("Print", op);
        back = new OpButton("hello", op);
        try {
            back.setImage(ImageIO.read(new File("data/images/arrow.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        editorScreen.add(back);
        editorScreen.add(editor, "w 80%, h 90%, wrap");
        editorScreen.add(docAdd, "cell 1 1, growx, gapbottom 40px, gaptop 50px");
        editorScreen.add(save, "cell 1 1, growx, gapbottom 15px");
        editorScreen.add(savePdf, "cell 1 1, growx, gapbottom 15px");
        editorScreen.add(email, "cell 1 1, growx, gapbottom 15px");
        editorScreen.add(print, "cell 1 1, growx");
        createDoctorList();
    }

    public void showPane(){
        getContentPane().removeAll();


        switch (state){
            case MENU: editorScreen = null;
                createMenu();
                add(menuScreen);
                break;
            case EDITOR: menuScreen = null;
                createEditor();
                add(editorScreen);
                break;
            case EXIT: add(confirmScreen);
                break;
        }

        validate();
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        this.setLocation(screenSize.width / 2 - this.getSize().width / 2, screenSize.height / 2 - this.getSize().height / 2);
        setVisible(true);
    }


    @Override
    public void windowGainedFocus(WindowEvent e) {
        Main.loadDoctors();
        if(editorScreen != null){
            createDoctorList();
        }
    }

    public void createDoctorList(){
        if( hasComponent(editorScreen, people)){
            editorScreen.remove(people);
        }

        people = new JComboBox<>(Main.people);
        if(editor.doc != null) {
            people.setSelectedItem(editor.doc);
        }
        people.addActionListener(op);
        editorScreen.add(people, "cell 1 0, growx");
        System.out.println("Hi");
        validate();
    }

    @Override
    public void windowLostFocus(WindowEvent e) {

    }


    private class OptometryListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(newFile)){
                String [] text = null;
                try {
                    text = Main.readFile("data/defaultTemplate.txt");
                } catch (IOException i) {
                    i.printStackTrace();
                }
                setStage(EDITOR);
                editor.setText(text);
            } else if(e.getSource().equals(editFile)){
                String [] text = null;
                int returnVal = browse.showOpenDialog(OptometryPanel.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = browse.getSelectedFile();
                    try {
                        text = Main.readFile(file.getPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(OptometryPanel.this, "File could not be opened");
                }

                setStage(EDITOR);
                editor.setText(text);
            } else if(e.getSource().equals(docAdd)){
                new DoctorPanel();
            } else if(e.getSource().equals(people)){
                editor.setDoctor((Person)people.getSelectedItem());
            } else if(e.getSource().equals(save)){
                int returnVal = browse.showSaveDialog(OptometryPanel.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = browse.getSelectedFile();
                    editor.saveContents(file.getPath().replace(".txt", "")+".txt");
                    try {
                        editor.exportPdf(file.getPath());
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(OptometryPanel.this, "The pdf is open in another program. \nYou must close that program in order to continue.");
                    }
                }
            } else if(e.getSource().equals(savePdf)){
                if(editor.filePath == ""){
                    JOptionPane.showMessageDialog(OptometryPanel.this, "PDF could not be exported.\nYou must save the document first");
                    return;
                }

                try {
                    editor.exportPdf();
                    JOptionPane.showMessageDialog(OptometryPanel.this, editor.filePath + ".pdf exported successfully");
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(OptometryPanel.this, "The pdf is open in another program. \nYou must close that program in order to continue.");
                }
            } else if(e.getSource().equals(back)){
                editor.saveContents(editor.filePath);
                setStage(MENU);
            } else if(e.getSource().equals(print)){
                editor.startPrint();
            } else if(e.getSource().equals(email)){
                editor.e.setDoctor((Person)people.getSelectedItem());
                if(editor.e.sendEmail()){
                    JOptionPane.showMessageDialog(OptometryPanel.this, "Email sent");
                }

            }
        }
    }

    public void setStage(int stage){
        state = stage;
        showPane();
    }

    public boolean hasComponent(JComponent parent, JComponent child){
        Component [] components = parent.getComponents();
        for(int i = 0; i < components.length; i++){
            if(components[i].equals(child)) return true;
        }
        return false;
    }

}
