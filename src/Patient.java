import java.util.Date;

/**
 * Created by Windows on 2016-07-28.
 */
public class Patient extends Person {
    public Eye od = new Eye();
    public Eye os = new Eye();

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    private Date dob;
    public int getMSP() {
        return MSP;
    }

    public void setMSP(int MSP) {
        this.MSP = MSP;
    }

    private int MSP = -1;

    @Override
    public String toString() {
        return lName + "," + fName + "," + MSP;
    }

    public void serialize() {
        super.serialize("data/patients/" + lName +"," + fName + ".ser");
    }

    class Eye {
        double vaUncorrected = 0;
        double vaCorrected = 0;
        double IOP = 0;
        String IOPTime = "";
        double sphere = 0;
        double angle = 0;
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

        public double getAngle() {
            return angle;
        }

        public void setAngle(double angle) {
            this.angle = angle;
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
    }

}
