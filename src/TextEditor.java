import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Windows on 2016-07-25.
 */
public class TextEditor extends JTextArea {


    ExportManager e = new ExportManager();
    public String filePath = "";
    public Person doc;

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
        setBorder(BorderFactory.createLineBorder(Main.mainBlue, 3));
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

    public void exportPdf(String filePath) throws Exception {
        e.setFilePath(filePath);
        e.setDoctor(doc);
        e.savePDF(this);
    }

    public void exportPdf() throws Exception {
        e.setFilePath(filePath + ".pdf");
        e.savePDF(this);
    }

    public void setDoctor(Person doctor){
        doc = doctor;
    }

    public void startPrint(){
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(e);
        boolean ok = job.printDialog();
        if (ok) {
            try {
                job.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }
    }
}
