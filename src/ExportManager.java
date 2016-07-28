


import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

/**
 * Created by Windows on 2016-07-26.
 */
public class ExportManager implements Printable {
    Person doctor;
    File filePath = new File("data");
    PageSize letter = new PageSize(new Rectangle(612, 792));

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
        if (pageIndex > 0) { /* We have only one page, and 'page' is zero-based */
            return NO_SUCH_PAGE;
        }

        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         */
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        /* Now we perform our rendering */

        g.drawString("Hello world!", 100, 100);

        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
    }

    public void setDoctor(Person p) {
        doctor = p;
    }

    public void setFilePath(String filePath){
        this.filePath = new File(filePath.replace(".pdf", "").replace(".txt", ""));
    }

    public void savePDF(TextEditor textEditor) throws Exception {
        PdfDocument pdf = new PdfDocument(new PdfWriter(filePath.getPath()+".pdf"));
        Document doc = new Document(pdf, letter);
        PdfPage page = pdf.addNewPage();
        PdfCanvas pdfCanvas = new PdfCanvas(page);

        makeHeader(pdf, pdfCanvas);
        makeBody(pdf, pdfCanvas, textEditor);
        makeFooter(pdf, pdfCanvas);

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

    public void newPrinting(){
        FileInputStream psStream = null;
        try {
            psStream = new FileInputStream("data/happyNess.pdf");
        } catch (FileNotFoundException ffne) {
            ffne.printStackTrace();
        }
        if (psStream == null) {
            return;
        }
        DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
        Doc myDoc = new SimpleDoc(psStream, psInFormat, null);
        PrintService[] services = PrintServiceLookup.lookupPrintServices(psInFormat, null);

        // this step is necessary because I have several printers configured

        PrintService myPrinter = null;
        for (int i = 0; i < services.length; i++){
            String svcName = services[i].toString();
            System.out.println("service found: "+ svcName);
            if (svcName.contains("HP")){
                myPrinter = services[i];
                System.out.println("my printer found: "+svcName);
                break;
            }
        }

        if (myPrinter != null) {
            DocPrintJob job = myPrinter.createPrintJob();
            try {
                job.print(myDoc, new HashPrintRequestAttributeSet());
                job.addPrintJobListener(new PrintJobListener() {
                    @Override
                    public void printDataTransferCompleted(PrintJobEvent pje) {
                        System.out.println("hello");
                    }

                    @Override
                    public void printJobCompleted(PrintJobEvent pje) {
                        System.out.println("2");
                    }

                    @Override
                    public void printJobFailed(PrintJobEvent pje) {
                        System.out.println("3");

                    }

                    @Override
                    public void printJobCanceled(PrintJobEvent pje) {
                        System.out.println("d");

                    }

                    @Override
                    public void printJobNoMoreEvents(PrintJobEvent pje) {
                        System.out.println("s");

                    }

                    @Override
                    public void printJobRequiresAttention(PrintJobEvent pje) {
                        System.out.println("f");

                    }
                });

            } catch (Exception pe) {
                pe.printStackTrace();
            }
        } else {
            System.out.println("no printer services found");
        }
    }





}
