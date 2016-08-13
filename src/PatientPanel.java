import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

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
    JTextArea day;
    JComboBox<String> month;
    JTextArea year;
    JTextArea msp;
    JTextArea doe;
    JButton today;
    JRadioButton male;
    JRadioButton female;

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
    
    public PatientPanel(){
        super();

        getInputMap().put(KeyStroke.getKeyStroke("TAB"), "TAB");
        getActionMap().put("TAB", tabFocus);
        p = new Patient();
        setLayout(new MigLayout("pack", "[150px]", "[30px]"));
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

        day = new TextEditor(false);
        final String [] months = new String[12];
        months[0] = "Jan";
        months[1] = "Feb";
        months[2] = "Mar";
        months[3] = "Apr";
        months[4] = "May";
        months[5] = "Jun";
        months[6] = "Jul";
        months[7] = "Aug";
        months[8] = "Sep";
        months[9] = "Oct";
        months[10] = "Nov";
        months[11] = "Dec";
        month = new JComboBox<>(months);
        month.setForeground(Main.mainBlue);
        month.setBackground(Color.WHITE);
        year = new TextEditor(false);
        add(day, "split 3, w 30");
        add(month);
        add(year, "wrap, w 30");

        add(new JLabel("MSP Number"));
        msp = new TextEditor(false);
        add(msp, "wrap, sg text");

        add(new JLabel("Date of Exam"));
        doe = new TextEditor(false);
        add(doe, "split 2");

        today = new OpButton("Today", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar today = Calendar.getInstance();
                doe.setText(today.get(Calendar.DAY_OF_MONTH) + " " + Patient.monthName(today.get(Calendar.MONTH) + 1) + ", " + today.get(Calendar.YEAR));
            }
        });
        add(today, "wrap");

        add(new JLabel("Gender"));
        male = new JRadioButton("Male");
        male.setBackground(Color.WHITE);
        male.setForeground(Main.mainBlue);
        female = new JRadioButton("Female");
        female.setBackground(Color.WHITE);
        female.setForeground(Main.mainBlue);

        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource().equals(male)){
                    female.setSelected(false);
                } else {
                    male.setSelected(false);
                }
            }
        };
        male.addActionListener(al);
        female.addActionListener(al);

        add(male, "split 2");
        add(female, "wrap");



        add(new JLabel("OD"), "skip");
        add(new JLabel("OS"), "wrap");
        
        add(new JLabel("VA: uncorrected"));

        vaUncorrectedOd = new TextEditor(false);
        add(new JLabel("20 /"), "split 2");
        add(vaUncorrectedOd);

        vaUncorrectedOs = new TextEditor(false);
        add(new JLabel("20 /"), "split 2");
        add(vaUncorrectedOs, "wrap");

        add(new JLabel("VA: Corrected"));

        vaCorrectedOd = new TextEditor(false);
        add(new JLabel("20 /"), "split 2");
        add(vaCorrectedOd);

        vaCorrectedOs = new TextEditor(false);
        add(new JLabel("20 /"), "split 2");
        add(vaCorrectedOs, "wrap");

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
                if(p == null)
                    p = new Patient();
                p.setFirstName(first.getText());
                p.setLastName(last.getText());
                p.setAddress(address.getText());
                p.setMonth((String)month.getSelectedItem());
                p.setDoe(doe.getText());

                p.setGender((male.isSelected())?"Male":"Female");

                try {
                    p.setMSP(Long.parseLong(msp.getText()));

                    p.setDay(Integer.parseInt(day.getText()));
                    p.setYear(Integer.parseInt(year.getText()));

                    p.od.setVaUncorrected(Double.parseDouble(vaUncorrectedOd.getText()));
                    p.od.setVaCorrected(Double.parseDouble(vaCorrectedOd.getText()));
                    p.od.setIOP(Double.parseDouble(iopOd.getText()));
                    p.od.setIOPTime(time.getText());
                    p.od.setSphere(Double.parseDouble(sphOd.getText()));
                    p.od.setCyl(Double.parseDouble(cylOd.getText()));
                    p.od.setAxis(Double.parseDouble(axisOd.getText()));
                    p.od.setAdd(Double.parseDouble(addOd.getText()));

                    p.os.setVaUncorrected(Double.parseDouble(vaUncorrectedOs.getText()));
                    p.os.setVaCorrected(Double.parseDouble(vaCorrectedOs.getText()));
                    p.os.setIOP(Double.parseDouble(iopOs.getText()));
                    p.os.setIOPTime(time.getText());
                    p.os.setSphere(Double.parseDouble(sphOs.getText()));
                    p.os.setCyl(Double.parseDouble(cylOs.getText()));
                    p.os.setAxis(Double.parseDouble(axisOs.getText()));
                    p.os.setAdd(Double.parseDouble(addOs.getText()));
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
        frame.setLocation(OptometryPanel.screenSize.width / 2 - this.getSize().width / 2, OptometryPanel.screenSize.height / 2 - this.getSize().height / 2);
        frame.setVisible(true);
    }
    
    public PatientPanel(Patient p){
        this();
        this.p = p;
        first.setText(p.getfName());
        last.setText(p.getlName());
        address.setText(p.getAddress());
        day.setText(p.day + "");
        month.setSelectedItem(Patient.monthName(p.month));
        year.setText(p.year + "");
        msp.setText(p.getMSP() + "");
        if(p.getGender().equals("Male")){
            male.setSelected(true);
        } else {
            female.setSelected(true);
        }
        doe.setText(p.getDoe());
        
        vaUncorrectedOd.setText(p.od.getVaUncorrected() + "");
        vaCorrectedOd.setText(p.od.getVaCorrected() + "");
        iopOd.setText(p.od.getIOP() + "");
        time.setText(p.od.getIOPTime());
        sphOd.setText(p.od.getSphere() + "");
        cylOd.setText(p.od.getCyl() + "");
        axisOd.setText(p.od.getAxis() + "");
        addOd.setText(p.od.getAdd() + "");

        vaUncorrectedOs.setText(p.os.getVaUncorrected() + "");
        vaCorrectedOs.setText(p.os.getVaCorrected() + "");
        iopOs.setText(p.os.getIOP() + "");
        time.setText(p.os.getIOPTime());
        sphOs.setText(p.os.getSphere() + "");
        cylOs.setText(p.os.getCyl() + "");
        axisOs.setText(p.os.getAxis() + "");
        addOs.setText(p.os.getAdd() + "");

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
