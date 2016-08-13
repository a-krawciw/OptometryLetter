


import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.*;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFRenderer;
import org.codemonkey.simplejavamail.Mailer;
import org.codemonkey.simplejavamail.TransportStrategy;
import org.codemonkey.simplejavamail.email.Email;
import org.fax4j.FaxClient;
import org.fax4j.FaxClientFactory;
import org.fax4j.FaxJob;


import javax.activation.FileDataSource;
import javax.mail.*;
import javax.swing.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;

/**
 * Created by Windows on 2016-07-26.
 */
public class ExportManager implements Printable {
    Doctor doctor;
    Patient patient;
    String filePath = "";
    PageSize letter = new PageSize(new Rectangle(612, 792));
    Color extraLightGrey;
    PdfFont calibri;

    public ExportManager(){

        PdfFontFactory.register("data/fonts/calibri.ttf", "calibri");
        try {
            calibri = PdfFontFactory.createRegisteredFont("calibri");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
        if (pageIndex > 0) { /* We have only one page, and 'page' is zero-based */
            return NO_SUCH_PAGE;
        }


        Graphics2D g2d = (Graphics2D)g;

        try {
            System.out.println(filePath + ".pdf");
            RandomAccessFile raf = new RandomAccessFile(filePath + ".pdf", "r");
            FileChannel channel = raf.getChannel();
            ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            PDFFile pdffile = new PDFFile(buf);

            PDFPage page = pdffile.getPage(0);

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            PDFRenderer renderer = new PDFRenderer(page, g2d, new java.awt.Rectangle(0, 0, 612, 792), null, java.awt.Color.RED);
            page.waitForFinish();
            renderer.run();
            channel.close();
            raf.close();
        }catch (Exception e){
            e.printStackTrace();
        }


        return PAGE_EXISTS;
    }

    public void setDoctor(Doctor p) {
        doctor = p;
    }

    public void setPatient(Patient p){
        this.patient = p;
    }

    public void setFilePath(String filePath){
        this.filePath = filePath.replace(".pdf", "").replace(".txt", "");
    }

    public void savePDF(TextEditor textEditor) throws Exception {
        PdfDocument pdf = new PdfDocument(new PdfWriter(filePath + ".pdf"));
        System.out.println("Hiya" + filePath);
        Document doc = new Document(pdf, letter);
        PdfPage page = pdf.addNewPage();
        PdfCanvas pdfCanvas = new PdfCanvas(page);
        makeBorder(pdf, pdfCanvas);
        makeHeader(pdf, pdfCanvas);
        makeBody(pdf, pdfCanvas, textEditor);
        doc.close();

    }

    private void makeBorder(PdfDocument doc, PdfCanvas pdf) throws Exception{
        pdf.roundRectangle(20, 20,  572, 752, 5);
        pdf.setLineWidth(15);
        pdf.setStrokeColor(com.itextpdf.kernel.color.Color.LIGHT_GRAY);
        pdf.stroke();

        pdf.rectangle(30, 30, 552, 732);
        pdf.setLineWidth(5);

        pdf.setStrokeColor(new DeviceRgb(240, 240, 240));
        pdf.stroke();
    }

    private void makeHeader(PdfDocument doc, PdfCanvas pdf) throws Exception{


        Rectangle info = new Rectangle(33, 672, 465, 15);
        pdf.rectangle(info);
        pdf.setFillColor(Color.GRAY);
        pdf.fill();

        pdf.rectangle(33, 672, 465, 60);
        pdf.setLineWidth(1);
        pdf.setStrokeColor(Color.BLACK);
        pdf.stroke();

        Rectangle box = new Rectangle(508, 672, 60, 60);
        pdf.rectangle(box);
        pdf.setFillColor(Color.LIGHT_GRAY);
        pdf.fill();

        pdf.rectangle(box);
        pdf.stroke();


        Rectangle headBox = new Rectangle(33, 687, 465, 45);

        Canvas c1 = new Canvas(pdf, doc, headBox);
        PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
        Text title = new Text("Uptown Village Optometry".toUpperCase()).setFont(bold).setFontSize(18).setFontColor(Color.BLACK);
        Paragraph p = new Paragraph().add(title);
        p.setTextAlignment(TextAlignment.CENTER);
        p.setVerticalAlignment(VerticalAlignment.MIDDLE);
        c1.add(p);

        Canvas c2 = new Canvas(pdf, doc, info);
        Text address = new Text("107-3450 Uptown BLVD. Victoria, BC V8Z 0B9 PH. 250-382-2682 FX.250-382-2687".toUpperCase()).setFont(calibri).setFontSize(10).setFontColor(Color.WHITE);
        Paragraph p2 = new Paragraph().add(address);
        p2.setTextAlignment(TextAlignment.CENTER);
        p2.setVerticalAlignment(VerticalAlignment.MIDDLE);
        c2.add(p2);

        
        
    }

    private void makeBody(PdfDocument doc, PdfCanvas pdf, TextEditor text) throws Exception{
        Rectangle rectangle = new Rectangle(34, 90, 544, 562);

        Canvas canvas = new Canvas(pdf, doc, rectangle);

        Text name = new Text(
                "DR." + doctor.getfName().toUpperCase() + " " + doctor.getlName().toUpperCase() + "    Fax: " + doctor.getFax() + "\n" +
                doctor.getAddress() + "\n\n" +
                "RE:" + patient.getlName().toUpperCase() + ", " + patient.getfName() + "     DOB: " + patient.getDob() + "     Age: " + patient.getAge() + "    Gender: " + patient.getGender() +
                "\nMSP: " + patient.getMSP() +
                "\nDate of Examination: " + patient.getDoe()
        ).setFont(calibri).setFontColor(Color.BLACK);
        Paragraph info = new Paragraph().add(name);
        info.setTextAlignment(TextAlignment.LEFT);

        Text message = new Text(
                "\nDear Dr. " + doctor.getlName() + ",\n" + text.getText() +"\n\nMary Jean Krawciw, OD" ).setFont(calibri).setFontColor(Color.BLACK);

        Paragraph letter = new Paragraph().add(message);
        letter.addTabStops(new TabStop(3));
        letter.setTextAlignment(TextAlignment.LEFT);

        Table t = new Table(3){
            @Override
            public Table addCell(String content) {
                Cell c = new Cell();
                c.setBorder(new Border(Color.WHITE, 1) {
                    @Override
                    public void draw(PdfCanvas canvas, float x1, float y1, float x2, float y2, float borderWidthBefore, float borderWidthAfter) {

                    }

                    @Override
                    public void drawCellBorder(PdfCanvas canvas, float x1, float y1, float x2, float y2) {

                    }

                    @Override
                    public int getType() {
                        return 0;
                    }
                });
               // c.setWidth(100);
                c.add(content);
                return super.addCell(c);
            }
        };

       // t.setWidth(300);

        t.setFontColor(Color.BLACK);
        t.setFont(calibri);
        t.setStrokeColor(Color.WHITE);
        t.addCell("");
        t.addCell("OD");
        t.addCell("OS");

        t.startNewRow();
        t.addCell("VA uncorrected");
        t.addCell("20/" + (int) patient.od.getVaUncorrected() );
        t.addCell("20/" + (int) patient.os.getVaUncorrected());

        t.startNewRow();
        t.addCell("VA Corrected");
        t.addCell("20/" + (int) patient.od.getVaCorrected());
        t.addCell("20/" + (int) patient.os.getVaCorrected());

        t.startNewRow();
        t.addCell("IOP:");
        t.addCell(patient.od.getIOP() + " mmHg");
        t.addCell(patient.os.getIOP() + " mmHg");

        t.startNewRow();
        t.addCell("@Time");
        t.addCell(patient.od.getIOPTime());

        t.startNewRow();
        t.addCell("Rx");
        t.addCell("Sph x Cyl x Axis    Add");
        t.addCell("Sph x Cyl x Axis    Add");

        t.startNewRow();
        t.addCell("");
        t.addCell(patient.od.getSphere() + " x " + patient.od.getCyl() + " x " + patient.od.getAxis() + "    " + patient.od.opAdd());
        t.addCell(patient.os.getSphere() + " x " + patient.os.getCyl() + " x " + patient.os.getAxis() + "    " +  patient.os.opAdd());

        canvas.add(info);
        canvas.add(t);
        canvas.add(letter);


    }


    public boolean sendEmail(){

        Email email = new Email();

        email.addRecipient(doctor.toString(), doctor.getEmail(), Message.RecipientType.TO);
        email.setFromAddress("Uptown Villiage Optometry", "mountdougtalks@gmail.com");
        email.setSubject("Refferal for " + patient.getfName() + " " + patient.getlName());
        email.setText("Please see attached referral letter.\nMary Jean Krawciw\nUptown Villiage Optometry");

        try {
            FileDataSource fd = new FileDataSource(filePath + ".pdf");
            email.addAttachment("invitation", fd);

            //new Mailer("smtp.gmail.com", 25, "javamailno", "thisisasilly", TransportStrategy.SMTP_TLS).sendMail(email);
            new Mailer("sm.com", 587, "uptown@villageoptometry", "mdtalks123", TransportStrategy.SMTP_TLS).sendMail(email);
            //new Mailer("smtp.gmail.com", 465, "javamailno@gmail.com", "thisisasilly", TransportStrategy.SMTP_SSL).sendMail(email);
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());

            return false;
        }
        return true;
    }

    public void sendFax(){
        //get new instance of a fax client (based on internal + external fax4j.properties file data)
        FaxClient faxClient= FaxClientFactory.createFaxClient();

//create a new fax job
        FaxJob faxJob=faxClient.createFaxJob();

//set fax job values
        faxJob.setFilePath("data/test17.txt");
        faxJob.setPriority(FaxJob.FaxJobPriority.HIGH_PRIORITY);
        faxJob.setTargetAddress("2503807669");
        faxJob.setTargetName("Krawciw");
        faxJob.setSenderEmail("myemail@mycompany.com");
        faxJob.setSenderName("MyName");

//submit fax job
        faxClient.submitFaxJob(faxJob);

//print submitted fax job ID (may not be supported by all SPIs)
        System.out.println("Fax job submitted, ID: "+faxJob.getID());
    }

    public void startPrint(){
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        boolean ok = job.printDialog();
        if (ok) {
            try {
                job.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void save(String toSave){
        if(!Main.writeFile(filePath + ".txt", doctor + "\n" + patient + "\n" + toSave)){
            JOptionPane.showMessageDialog(null, "File could not be saved");
        }
    }

    public String getFileName(){
        String [] temp = filePath.split("\\\\");
        return temp[temp.length - 1];
    }

    public boolean hasAllInformation(){
        return patient != null && doctor != null;
    }

    public boolean isPrintable(){
        File f = new File(filePath + ".pdf");
        return f.exists();
    }







}
