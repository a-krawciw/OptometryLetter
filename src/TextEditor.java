import com.inet.jortho.FileUserDictionary;
import com.inet.jortho.SpellChecker;
import com.inet.jortho.SpellCheckerOptions;
import com.itextpdf.layout.element.Text;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by Windows on 2016-07-25.
 */
public class TextEditor extends JTextArea implements FocusListener, KeyListener {


    public String filePath = "";
    public Person doc;
    private Frame c;

    public TextEditor(String [] lines){
        super();
        super.setText(arrayAsOne(lines));
        setEditable(true);
        Font f = Font.decode(Font.SANS_SERIF);
        setFont(f);
        setAutoscrolls(true);

    }

    public TextEditor(String line, boolean spellCheck){
        super();
        super.setText(line);
        setEditable(true);
        Font f = Font.decode(Font.SANS_SERIF);
        setFont(f);
        setAutoscrolls(true);

        setLineWrap(true);
        setWrapStyleWord(true);
        setFocusTraversalKeysEnabled(false);

        setBorder(BorderFactory.createLineBorder(Main.mainBlue, 3));
        addFocusListener(this);
        addKeyListener(this);


        if(spellCheck) {
            SpellChecker.setUserDictionaryProvider(new FileUserDictionary());
            SpellChecker.registerDictionaries(this.getClass().getResource("/dictionary"), "en");
            SpellChecker.register(this);
        }
    }

    public TextEditor(String line){
        this(line, true);
    }

    public TextEditor(boolean b){
        this("", b);
    }


    public TextEditor (){
        this("");
    }

    public TextEditor(Frame f){
        this("");
        c = f;
    }

    private String arrayAsOne(String [] lines){
        String expanded = "";
        doc = Main.getDoctor(lines[0]);
        for (int i = 2; i < lines.length; i++){
            expanded += lines[i] + "\n";
        }
        return expanded;
    }

    public void setText(String [] lines){
        setText(arrayAsOne(lines));
    }

    public void saveContents(ExportManager e){
        e.save(getText());
    }



    public void setDoctor(Person doctor){
        doc = doctor;
    }


    @Override
    public void focusGained(FocusEvent e) {
        setBorder(BorderFactory.createLineBorder(Color.RED, 3));
    }

    @Override
    public void focusLost(FocusEvent e) {
        setBorder(BorderFactory.createLineBorder(Main.mainBlue, 3));
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_TAB){
            transferFocus();
            e.consume();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
