import com.inet.jortho.FileUserDictionary;
import com.inet.jortho.SpellChecker;
import com.inet.jortho.SpellCheckerOptions;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by Windows on 2016-07-25.
 */
public class TextEditor extends JTextArea implements ComponentListener {


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

    public TextEditor(String line){
        super();
        super.setText(line);
        setEditable(true);
        Font f = Font.decode(Font.SANS_SERIF);
        setFont(f);
        setAutoscrolls(true);

        setLineWrap(true);
        setWrapStyleWord(true);

        setBorder(BorderFactory.createLineBorder(Main.mainBlue, 3));
        SpellChecker.setUserDictionaryProvider(new FileUserDictionary());
        SpellChecker.registerDictionaries(this.getClass().getResource("/dictionary"), "en");
        SpellChecker.register(this);

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
        for (int i = 1; i < lines.length; i++){
            expanded += lines[i] + "\n";
        }
        return expanded;
    }

    public void setText(String [] lines){
        setText(arrayAsOne(lines));
    }

    public void saveContents(String filePath){
        filePath = filePath.replace(".txt", "");
        if(!Main.writeFile(filePath + ".txt", doc + "\n" + getText())){
            JOptionPane.showMessageDialog(this, "File could not be saved");
        } else {
            this.filePath = filePath.replace(".txt", "");
        }
    }



    public void setDoctor(Person doctor){
        doc = doctor;
    }


    public void setContainer(Frame c){
        this.c = c;
    }
    @Override
    public void componentResized(ComponentEvent e) {
        if(c != null)
            c.pack();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
