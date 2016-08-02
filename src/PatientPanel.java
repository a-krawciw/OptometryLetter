import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;

/**
 * Created by Windows on 2016-07-28.
 */
public abstract class PatientPanel extends JPanel {
    JFrame frame;
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
        super();

        getInputMap().put(KeyStroke.getKeyStroke("TAB"), "TAB");
        getActionMap().put("TAB", tabFocus);
        p = new Patient();
        setLayout(new MigLayout("", "[150px]", "[30px]"));
        setBackground(Color.WHITE);

        add(new JLabel("First Name"));
        first = new TextEditor(false);
        add(first, "wrap, w 150px!, h 30px!, sg text");

        add(new JLabel("Last Name"));
        last = new TextEditor(false);
        add(last, "wrap, sg text");

        add(new JLabel("Address"));
        address = new TextEditor(false);
        add(address, "wrap, sg text");

        add(new JLabel("Date of Birth"));
        dob = new TextEditor(false);
        add(dob, "wrap, sg text");

        add(new JLabel("MSP Number"));
        msp = new TextEditor(false);
        add(msp, "wrap, sg text");

        add(new JLabel("OD"), "skip");
        add(new JLabel("OS"), "wrap");
        
        add(new JLabel("VA: uncorrected"));

        vaUncorrectedOd = new TextEditor(false);
        add(vaUncorrectedOd, "split 2");
        add(new JLabel("/20"));

        vaUncorrectedOs = new TextEditor(false);
        add(vaUncorrectedOs, "split 2");
        add(new JLabel("/20"), "wrap");

        add(new JLabel("VA: Corrected"));

        vaCorrectedOd = new TextEditor(false);
        add(vaCorrectedOd, "split 2");
        add(new JLabel("/20"));

        vaCorrectedOs = new TextEditor(false);
        add(vaCorrectedOs, "split 2");
        add(new JLabel("/20"), "wrap");

        add(new JLabel("IOP:"));

        iopOd = new TextEditor(false);
        add(iopOd, "split 2");
        add(new JLabel("mmHg"));

        iopOs = new TextEditor(false);
        add(iopOs, "split 2");
        add(new JLabel("mmHg"), "wrap");

        time = new TextEditor(false);
        add(new JLabel("@Time:"));
        add(time, "wrap");

        add(new JLabel("Rx"));

        add(new JLabel("Sphere"), "split 3, growx");
        add(new JLabel("Cylinder"), "growx");
        add(new JLabel("Axis"), "growx");

        add(new JLabel("Sphere"), "split 3, growx");
        add(new JLabel("Cylinder"), "growx");
        add(new JLabel("Axis"), "wrap, growx");
        
        sphOd = new TextEditor(false);
        add(sphOd, "skip, split 5");
        add(new JLabel("x"));
        cylOd = new TextEditor(false);
        add(cylOd);
        add(new JLabel("x"));
        axisOd = new TextEditor(false);
        add(axisOd);

        sphOs = new TextEditor(false);
        add(sphOs, "split 5");
        add(new JLabel("x"));
        cylOs = new TextEditor(false);
        add(cylOs);
        add(new JLabel("x"));
        axisOs = new TextEditor(false);
        add(axisOs, "wrap");

        add(new JLabel("Add"), "skip, split 2");
        addOd = new TextEditor(false);
        add(addOd);

        add(new JLabel("Add"), "split 2");
        addOs = new TextEditor(false);
        add(addOs, "wrap");


        submit = new OpButton("Submit", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p = new Patient();
                p.setFirstName(first.getText());
                p.setLastName(last.getText());
                p.setAddress(address.getText());
                p.setEmail(dob.getText());

                try {
                    p.setMSP(Integer.parseInt(msp.getText()));

                    p.od.setVaUncorrected(Double.parseDouble(vaUncorrectedOd.getText()));
                    p.od.setVaCorrected(Double.parseDouble(vaCorrectedOd.getText()));
                    p.od.setIOP(Double.parseDouble(iopOd.getText()));
                    p.od.setIOPTime(time.getText());
                    p.od.setSphere(Double.parseDouble(sphOd.getText()));
                    p.od.setCyl(Double.parseDouble(sphOd.getText()));
                    p.od.setAxis(Double.parseDouble(axisOd.getText()));

                    p.os.setVaUncorrected(Double.parseDouble(vaUncorrectedOs.getText()));
                    p.os.setVaCorrected(Double.parseDouble(vaCorrectedOs.getText()));
                    p.os.setIOP(Double.parseDouble(iopOs.getText()));
                    p.os.setIOPTime(time.getText());
                    p.os.setSphere(Double.parseDouble(sphOs.getText()));
                    p.os.setCyl(Double.parseDouble(sphOs.getText()));
                    p.os.setAxis(Double.parseDouble(axisOs.getText()));
                }catch (Exception ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(PatientPanel.this, "One of your number fields has a letter in it");
                    return;
                }

                p.serialize();
                patientInfoCompleted(p);
                System.out.println(p + "l");
                frame.setVisible(false);
                frame.dispose();
            }
        });

        add(submit);
        frame = new JFrame("Enter Patient Information");
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public abstract void patientInfoCompleted(Patient p);

    private Action tabFocus = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Component [] comps = getComponents();
            for (int i = 0; i < comps.length; i++) {
                System.out.println(comps[i].getName());
            }
        }
    };


}
