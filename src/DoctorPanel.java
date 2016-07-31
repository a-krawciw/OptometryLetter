import net.miginfocom.layout.BoundSize;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.LayoutCallback;
import net.miginfocom.layout.UnitValue;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Windows on 2016-07-25.
 */
public class DoctorPanel extends JFrame {
    JTextArea first;
    JTextArea last;
    JTextArea address;
    JTextArea email;
    JTextArea fax;
    JButton submit;

    public DoctorPanel(){
        super("Add a doctor");
        getContentPane().setBackground(Color.WHITE);
        MigLayout mig = new MigLayout("w 300, h 160", "[150px]", "[30px]");
        setLayout(mig);

        add(new JLabel("First Name"));
        first = new TextEditor(false);
        add(first, "wrap, w 150px!, h 30px!, sg text");

        add(new JLabel("Last Name"));
        last = new TextEditor(false);
        add(last, "wrap, sg text");

        add(new JLabel("Address"));
        address = new TextEditor(false);
        add(address, "wrap, sg text");

        add(new JLabel("Email Address"));
        email = new TextEditor(false);
        add(email, "wrap, sg text");
//
//        add(new JLabel("Fax Number"));
//        fax = new TextEditor(false);
//        add(fax, "wrap, sg text");

        submit = new OpButton("Submit", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Doctor p = new Doctor();
                p.setFirstName(first.getText());
                p.setLastName(last.getText());
                p.setAddress(address.getText());
                p.setEmail(email.getText());
                p.setFax("");
                p.serialize();
                DoctorPanel.super.dispose();
            }
        });

        add(submit);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }



}
