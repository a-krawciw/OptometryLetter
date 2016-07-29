/**
 * Created by Windows on 2016-07-28.
 */
public class Doctor extends Person {

    @Override
    public String toString() {
        return "Dr. " + lName;
    }

    public void serialize() {
        super.serialize("data/doctors/" + lName +".ser");
    }
}
