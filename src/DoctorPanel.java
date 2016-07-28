import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Windows on 2016-07-25.
 */
public class DoctorPanel extends JFrame {
    GridLayout gr;
    JTextArea first;
    JTextArea last;
    JTextArea address;
    JTextArea email;
    JButton submit;

    public DoctorPanel(){
        super("Add a doctor");
        gr = new GridLayout(4, 1);
        setLayout(gr);

        add(new JLabel("First Name"));
        first = new JTextArea();
        add(first);

        add(new JLabel("Last Name"));
        last = new JTextArea();
        add(last);

        add(new JLabel("Address"));
        address = new JTextArea();
        add(address);

        add(new JLabel("email address"));
        email = new JTextArea();
        add(email);

        submit = new OpButton("Submit", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Person p = new Person();
                p.setFirstName(first.getText());
                p.setLastName(last.getText());
                p.setAddress(address.getText());
                p.setEmail(email.getText());
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
