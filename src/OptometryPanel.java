
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
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
    public static Dimension screenSize;
    public Dimension windowSize ;

    JFileChooser browse = new JFileChooser("data");
    ExportManager e;

    private JPanel menuScreen;
    private OpButton newFile;
    private OpButton editFile;


    private OpButton docAdd;
    private OpButton patientAdd;

    private JPanel editorScreen;
    private TextEditor editor;
    private OpButton save;
    private OpButton savePdf;
    private OpButton email;
    private OpButton print;
    private OpButton back;
    private JLabel currentFile;
    private JLabel currentPatient;


    private JComboBox<Doctor> people;
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

        browse.setFileFilter(new FileNameExtensionFilter("Text", "txt"));

        try {
            setIconImage(ImageIO.read(this.getClass().getResourceAsStream("/images/glasses.png")));
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
             image = ImageIO.read(this.getClass().getResourceAsStream("/images/menu.png"));
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
        menuScreen.add(jl, "pos 0 0, w " + windowSize.width + ", h " + windowSize.height);
    }

    private void createEditor(){
        editorScreen = new JPanel();
        editorScreen.setBackground(Color.WHITE);
        MigLayout mg = new MigLayout("flowy");
        editorScreen.setLayout(mg);

        editor = new TextEditor("");
        docAdd = new OpButton("Add a new Doctor", op);
        patientAdd = new OpButton("Set Patient Info", op);
        save = new OpButton("Save", op);
        savePdf = new OpButton("Export", op);
        email = new OpButton("Email Letter", op);
        print = new OpButton("Print", op);
        back = new OpButton("hello", op);
        try {
            back.setImage(ImageIO.read(this.getClass().getResourceAsStream("/images/arrow.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentFile = new JLabel("Currently editing " + e.getFileName());
        currentPatient = new JLabel("No patient entered");

        editorScreen.add(back, "flowx, split 3");
        editorScreen.add(currentFile, "cell 0 0, growx");
        editorScreen.add(currentPatient, "cell 0 0, growx");
        editorScreen.add(editor, "w 80%, h 90%, wrap");
        editorScreen.add(patientAdd, "cell 1 1, growx, gapbottom 15px");
        editorScreen.add(docAdd, "cell 1 1, growx, gapbottom 40px");
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
        people.addActionListener(op);
        people.setForeground(Main.mainBlue);
        people.setBackground(Color.WHITE);
        editorScreen.add(people, "cell 1 0, growx");
        System.out.println("Hi");
        validate();
    }

    @Override
    public void windowLostFocus(WindowEvent e) {

    }


    private class OptometryListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent event) {

            if(event.getSource().equals(newFile)){
                String [] text = null;
                try {
                    text = Main.readFile(OptometryPanel.this.getClass().getResourceAsStream("/defaultTemplate.txt"));
                } catch (IOException i) {
                    i.printStackTrace();
                }
                e = new ExportManager();
                setStage(EDITOR);
                editor.setText(text);
            } else if(event.getSource().equals(editFile)){
                e = new ExportManager();
                String [] text = null;
                int returnVal = browse.showOpenDialog(OptometryPanel.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = browse.getSelectedFile();
                    if(!file.exists()){
                        JOptionPane.showMessageDialog(OptometryPanel.this, "File could not be opened");
                        return;
                    }
                    try {
                        text = Main.readFile(file.getPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    e.setFilePath(file.getPath());
                } else {
                    JOptionPane.showMessageDialog(OptometryPanel.this, "File could not be opened");
                    return;
                }


                setStage(EDITOR);

                if(text.length > 0 && !text[0].equals("null")) {
                    people.setSelectedItem(Main.getDoctor(text[0]));
                }

                if(text.length > 0 && !text[1].equals("null")) {
                    setPatient(Main.getPatient(text[1]));
                    patientAdd.setText("Edit Patient Info");
                }


                editor.setText(text);
            } else if(event.getSource().equals(docAdd)){
                new DoctorPanel();
            } else if(event.getSource().equals(people)){
                e.setDoctor((Doctor)people.getSelectedItem());
                editor.setDoctor((Doctor)people.getSelectedItem());
            } else if(event.getSource().equals(save)){
                doSaving();
            } else if(event.getSource().equals(savePdf)){
                savePDF();
            } else if(event.getSource().equals(back)){
                if(!(e.filePath == ""))
                    editor.saveContents(e);
                setStage(MENU);
            } else if(event.getSource().equals(print)){
                if(!e.hasAllInformation()){
                    JOptionPane.showMessageDialog(OptometryPanel.this, "You must choose a doctor and enter patient information first");
                    return;
                }
                savePDF();
                e.startPrint();
            } else if(event.getSource().equals(email)){
                if(!e.hasAllInformation()){
                    JOptionPane.showMessageDialog(OptometryPanel.this, "You must choose a doctor first");
                    return;
                }
                int answer  = JOptionPane.showConfirmDialog(OptometryPanel.this, "Are you sure you want to email " + e.doctor);
                savePDF();
                if(answer == JOptionPane.YES_OPTION && e.sendEmail()){
                    JOptionPane.showMessageDialog(OptometryPanel.this, "Email sent");
                }
            } else if (event.getSource().equals(patientAdd)){
                if(e.patient == null) {
                    new PatientPanel() {
                        @Override
                        public void patientInfoCompleted(Patient p) {
                            setPatient(p);
                            patientAdd.setText("Edit Patient Info");
                        }
                    };
                } else {
                    new PatientPanel(e.patient) {
                        @Override
                        public void patientInfoCompleted(Patient p) {
                            setPatient(p);
                            patientAdd.setText("Edit Patient Info");
                        }
                    };
                }

            }
        }
    }

    private void setPatient(Patient p){
        e.setPatient(p);

        currentPatient.setText("Current Patient: " + showFirstBit(p.toString(), 6));
        System.out.println(p);
    }

    private String showFirstBit(String input, int endIndex){

        if(input.length() >  endIndex){
            input = input.substring(0, endIndex) + "...";
        }

        return input;
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

    private void doSaving(){
        if(!e.hasAllInformation()){
            JOptionPane.showMessageDialog(OptometryPanel.this, "You must enter patient info\nand choose a doctor");
            return;
        }

        browse.setSelectedFile(new File("data/" + e.patient.forSaving() + "_" + e.doctor.forSaving()));
        int returnVal = browse.showSaveDialog(OptometryPanel.this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = browse.getSelectedFile();
            e.setFilePath(file.getPath());
            editor.saveContents(e);
            currentFile.setText("Currently editing " + showFirstBit(file.getName(), 6));
        }
    }

    private void savePDF(){
        if(e.filePath == ""){
            JOptionPane.showMessageDialog(OptometryPanel.this, "PDF could not be exported.\nYou must save the document first");
            return;
        }

        try {
            e.savePDF(editor);
            JOptionPane.showMessageDialog(OptometryPanel.this, e.filePath + ".pdf exported successfully");
            String fpath = e.getFileName();
            Patient tp = e.patient;
            Doctor td = e.doctor;
            e = new ExportManager();
            e.setFilePath((fpath.contains("data/")?"":"data/") + fpath);
            e.setPatient(tp);
            e.setDoctor(td);
        } catch (Exception e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(OptometryPanel.this, "The pdf is open in another program. \nYou must close that program in order to continue.");
        }
    }

}
