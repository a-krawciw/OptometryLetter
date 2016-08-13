import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by Windows on 2016-07-28.
 */
public class Patient extends Person {
    public Eye od = new Eye();
    public Eye os = new Eye();

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(String month) {
        this.month = monthNum(month);
    }

    private int monthNum(String month){
        switch (month){
            case "Jan": return 1;
            case "Feb": return 2;
            case "Mar": return 3;
            case "Apr": return 4;
            case "May": return 5;
            case "Jun": return 6;
            case "Jul": return 7;
            case "Aug": return 8;
            case "Sep": return 9;
            case "Oct": return 10;
            case "Nov": return 11;
            case "Dec": return 12;
        }
        return -1;
    }

    public static String monthName(int month){
        switch (month){
            case 1: return "Jan";
            case 2: return "Feb";
            case 3: return "Mar";
            case 4: return "Apr";
            case 5: return "May";
            case 6: return "Jun";
            case 7: return "Jul";
            case 8: return "Aug";
            case 9: return "Sep";
            case 10: return "Oct";
            case 11: return "Nov";
            case 12: return "Dec";
        }
        return "";
    }

    public void setYear(int year) {
        this.year = year;
    }

     int day = 0;
    int month = 0;
     int year = 0;
    private String doe = "";

    public String getDob(){
        return day + " " + monthName(month) + ", " + year;
    }

    public long getMSP() {
        return MSP;
    }

    public void setMSP(long MSP) {
        this.MSP = MSP;
    }

    private long MSP = -1;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    private String gender = "";


    @Override
    public String toString() {
        return lName + "," + fName + "," + MSP;
    }

    public void serialize() {
        super.serialize("data/patients/" + lName +"," + fName + ".ser");
    }

    public String getDoe() {
        return doe;
    }

    public void setDoe(String doe) {
        this.doe = doe;
    }

    public int getAge() {
        Calendar today = Calendar.getInstance();
        Calendar born = Calendar.getInstance();
        born.set(year, month - 1, day);
        if(today.get(Calendar.DAY_OF_YEAR) > born.get(Calendar.DAY_OF_YEAR))
            return Calendar.getInstance().get(Calendar.YEAR) - year;
        else
            return Calendar.getInstance().get(Calendar.YEAR) - year - 1;
    }

    class Eye implements Serializable {
        double vaUncorrected = 0;
        double vaCorrected = 0;
        double IOP = 0;
        String IOPTime = "";
        double sphere = 0;
        double axis = 0;
        double cyl = 0;
        double add = 0;

        public double getVaUncorrected() {
            return vaUncorrected;
        }

        public void setVaUncorrected(double vaUncorrected) {
            this.vaUncorrected = vaUncorrected;
        }

        public double getVaCorrected() {
            return vaCorrected;
        }

        public void setVaCorrected(double vaCorrected) {
            this.vaCorrected = vaCorrected;
        }

        public double getIOP() {
            return IOP;
        }

        public void setIOP(double IOP) {
            this.IOP = IOP;
        }

        public String getIOPTime() {
            return IOPTime;
        }

        public void setIOPTime(String IOPTime) {
            this.IOPTime = IOPTime;
        }

        public double getSphere() {
            return sphere;
        }

        public void setSphere(double sphere) {
            this.sphere = sphere;
        }

        public double getAxis() {
            return axis;
        }

        public void setAxis(double axis) {
            this.axis = axis;
        }

        public double getCyl() {
            return cyl;
        }

        public void setCyl(double cyl) {
            this.cyl = cyl;
        }

        public double getAdd() {

            return add;
        }

        public void setAdd(double add) {
            this.add = add;
        }



        public Eye(){

        }

        public String opAdd(){
            DecimalFormat d = new DecimalFormat("##.##");
            String s = d.format(add);
            if(add > 0){
                s = "+" + s;
            }
            return s;
        }
    }

}
