


import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
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
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Sides;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Windows on 2016-07-26.
 */
public class ExportManager implements Printable {
    Person doctor;
    String filePath = "";
    PageSize letter = new PageSize(new Rectangle(612, 792));


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

            PDFRenderer renderer = new PDFRenderer(page, g2d, new java.awt.Rectangle(0, 0, 612, 792), null, Color.RED);
            page.waitForFinish();
            renderer.run();
            channel.close();
            raf.close();
        }catch (Exception e){
            e.printStackTrace();
        }


        return PAGE_EXISTS;
    }

    public void setDoctor(Person p) {
        doctor = p;
    }

    public void setFilePath(String filePath){
        this.filePath = filePath.replace(".pdf", "").replace(".txt", "");
    }

    public void savePDF(TextEditor textEditor) throws Exception {
        PdfDocument pdf = new PdfDocument(new PdfWriter(filePath + ".pdf"));
        Document doc = new Document(pdf, letter);
        PdfPage page = pdf.addNewPage();
        PdfCanvas pdfCanvas = new PdfCanvas(page);
        makeHeader(pdf, pdfCanvas);
        makeBody(pdf, pdfCanvas, textEditor);
        makeFooter(pdf, pdfCanvas);
        doc.close();
        pdf.close();
    }

    private void makeHeader(PdfDocument doc, PdfCanvas pdf) throws Exception{
        Rectangle rectangle = new Rectangle(30, 662, 552, 100);
        pdf.rectangle(rectangle);
        pdf.stroke();
        Canvas canvas = new Canvas(pdf, doc, rectangle);
        PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
        Text title = new Text("Uptown Village Optometry\n3450 Uptown Blvd, Victoria, BC V8Z 0B9\n" +
                "(250) 382-2682").setFont(bold);
        Paragraph p = new Paragraph().add(title);
        p.setTextAlignment(TextAlignment.CENTER);
        canvas.add(p);
    }

    private void makeBody(PdfDocument doc, PdfCanvas pdf, TextEditor text) throws Exception{
        Rectangle rectangle = new Rectangle(30, 90, 552, 562);
        pdf.rectangle(rectangle);
        pdf.stroke();
        Canvas canvas = new Canvas(pdf, doc, rectangle);
        PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
        Text name = new Text("Dear " + doctor + ",\n");
        Text body = new Text(text.getText()).setFont(font);
        Paragraph p = new Paragraph().add(name).add(body);
        p.setTextAlignment(TextAlignment.LEFT);
        canvas.add(p);
    }

    private void makeFooter(PdfDocument doc, PdfCanvas pdf) throws Exception{
        Rectangle rectangle = new Rectangle(30, 30, 552, 50);
        pdf.rectangle(rectangle);
        pdf.stroke();
        Canvas canvas = new Canvas(pdf, doc, rectangle);
        PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
        Text title = new Text("Mary Jean Krawciw").setFont(bold);
        Paragraph p = new Paragraph().add(title);
        p.setTextAlignment(TextAlignment.LEFT);

        Text date = new Text(new Date().toString()).setFont(bold);
        Paragraph p2 = new Paragraph().add(date);
        p2.setTextAlignment(TextAlignment.RIGHT);
        Div d = new Div().add(p2).add(p);
        canvas.add(d);
    }


    public boolean sendEmail(){

        Email email = new Email();

        email.addRecipient(doctor.toString(), doctor.getEmail(), Message.RecipientType.TO);
        email.setFromAddress("Uptown Villiage Optometry", "mountdougtalks@gmail.com");
        email.setSubject("Refferal for ");
        email.setText("Please see attached referral letter.\nMary Jean Krawciw/nUptown Villiage Optometry");

        try {
            FileDataSource fd = new FileDataSource(filePath + ".pdf");
            email.addAttachment("invitation", fd);

            //new Mailer("smtp.gmail.com", 25, "javamailno", "thisisasilly", TransportStrategy.SMTP_TLS).sendMail(email);
            new Mailer("smtp.gmail.com", 587, "mountdougtalks", "mdtalks123", TransportStrategy.SMTP_TLS).sendMail(email);
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








}
