import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Windows on 2016-07-28.
 */
public class PatientPanel extends JDialog {
    private Patient p;
    JTextArea first;
    JTextArea last;
    JTextArea address;
    JTextArea dob;
    JTextArea msp;
    
    JTextArea vaUncorrectedOd;
    JTextArea vaCorrectedOd;
    JTextArea iopOd;
    JTextArea time;
    JTextArea sphOd;
    JTextArea cylOd;
    JTextArea axisOd;
    JTextArea addOd;

    JTextArea vaUncorrectedOs;
    JTextArea vaCorrectedOs;
    JTextArea iopOs;
    JTextArea sphOs;
    JTextArea cylOs;
    JTextArea axisOs;
    JTextArea addOs;
    
    
    JButton submit;
    
    public PatientPanel(Frame f){
        super(f, "Enter Patient Information", true);
        p = new Patient();
        getContentPane().setLayout(new MigLayout("debug", "[150px]", "[30px]"));
        getContentPane().setBackground(Color.WHITE);

        add(new JLabel("First Name"));
        first = new TextEditor();
        add(first, "wrap, w 150px!, h 30px!, sg text");

        add(new JLabel("Last Name"));
        last = new TextEditor();
        add(last, "wrap, sg text");

        add(new JLabel("Address"));
        address = new TextEditor();
        add(address, "wrap, sg text");

        add(new JLabel("Date of Birth"));
        dob = new TextEditor();
        add(dob, "wrap, sg text");

        add(new JLabel("MSP Number"));
        msp = new TextEditor();
        add(msp, "wrap, sg text");

        add(new JLabel("OD"), "skip");
        add(new JLabel("OS"), "wrap");
        
        add(new JLabel("VA: uncorrected"));

        vaUncorrectedOd = new TextEditor();
        add(vaUncorrectedOd, "split 2");
        add(new JLabel("/20"));

        vaUncorrectedOs = new TextEditor();
        add(vaUncorrectedOs, "split 2");
        add(new JLabel("/20"), "wrap");

        add(new JLabel("VA: Corrected"));

        vaCorrectedOd = new TextEditor();
        add(vaCorrectedOd, "split 2");
        add(new JLabel("/20"));

        vaCorrectedOs = new TextEditor();
        add(vaCorrectedOs, "split 2");
        add(new JLabel("/20"), "wrap");

        add(new JLabel("IOP:"));

        iopOd = new TextEditor();
        add(iopOd, "split 2");
        add(new JLabel("mmHg"));

        iopOs = new TextEditor();
        add(iopOs, "split 2");
        add(new JLabel("mmHg"), "wrap");

        time = new TextEditor();
        add(new JLabel("@Time:"));
        add(time, "wrap");

        add(new JLabel("Rx"));

        add(new JLabel("Sphere"), "split 3, growx");
        add(new JLabel("Cylinder"), "growx");
        add(new JLabel("Axis"), "growx");

        add(new JLabel("Sphere"), "split 3, growx");
        add(new JLabel("Cylinder"), "growx");
        add(new JLabel("Axis"), "wrap, growx");
        
        sphOd = new TextEditor();
        add(sphOd, "skip, split 5");
        add(new JLabel("x"));
        cylOd = new TextEditor();
        add(cylOd);
        add(new JLabel("x"));
        axisOd = new TextEditor();
        add(axisOd);

        sphOs = new TextEditor();
        add(sphOs, "split 5");
        add(new JLabel("x"));
        cylOs = new TextEditor();
        add(cylOs);
        add(new JLabel("x"));
        axisOs = new TextEditor();
        add(axisOs, "wrap");

        add(new JLabel("Add"), "skip, split 2");
        addOd = new TextEditor();
        add(addOd);

        add(new JLabel("Add"), "split 2");
        addOs = new TextEditor();
        add(addOs, "wrap");


        submit = new OpButton("Submit", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p = new Patient();
                p.setFirstName(first.getText());
                p.setLastName(last.getText());
                p.setAddress(address.getText());
                p.setEmail(dob.getText());
                p.setMSP(Integer.parseInt(msp.getText()));

                try {
                    p.od.setVaUncorrected(Double.parseDouble(vaUncorrectedOd.getText()));
                    p.od.setVaCorrected(Double.parseDouble(vaCorrectedOd.getText()));
                    p.od.setIOP(Double.parseDouble(iopOd.getText()));


                }catch (Exception ex){
                    JOptionPane.showMessageDialog(PatientPanel.this, "One of your number fields has a letter in it");
                    return;
                }

                p.serialize();
                setVisible(false);
                dispose();
            }
        });

        add(submit);
        pack();
    }

    public Patient getPatientInformation(){
        setVisible(true);
        return p;
    }
}
