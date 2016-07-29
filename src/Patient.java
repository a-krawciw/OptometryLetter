/**
 * Created by Windows on 2016-07-28.
 */
public class Patient extends Person {
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
}
