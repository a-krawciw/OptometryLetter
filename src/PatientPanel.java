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
    JTextArea email;
    JTextArea msp;
    JButton submit;
    
    public PatientPanel(Frame f){
        super(f, "Enter Patient Information", true);
        p = new Patient();
        setSize(new Dimension(300, 300));
        getContentPane().setLayout(new MigLayout("w 300, h 160", "[150px]", "[30px]"));
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

        add(new JLabel("Email Address"));
        email = new TextEditor();
        add(email, "wrap, sg text");

        add(new JLabel("MSP Number"));
        msp = new TextEditor();
        add(msp, "wrap, sg text");


        submit = new OpButton("Submit", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p = new Patient();
                p.setFirstName(first.getText());
                p.setLastName(last.getText());
                p.setAddress(address.getText());
                p.setEmail(email.getText());
                p.setMSP(Integer.parseInt(msp.getText()));
                p.serialize();
                setVisible(false);
                dispose();
            }
        });

        add(submit);
    }

    public Patient getPatientInormation(){
        setVisible(true);
        return p;
    }
}
