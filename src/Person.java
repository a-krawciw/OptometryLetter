import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Windows on 2016-07-25.
 */
public class Person implements Serializable{
    private String fName = "";
    private String lName = "";
    private String address = "";
    private String email = "";

    public Person(String [] info){
        fName = info[0];
        lName = info[1];
        address = info[2];
        email = info[3];
    }

    public Person(){}

    @Override
    public String toString() {
        return "Dr. " + lName;
    }

    public void serialize(){
        try
        {
            FileOutputStream fileOut = new FileOutputStream("data/doctors/" + lName +".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved");
        }catch(IOException i){
            i.printStackTrace();
        }
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String fName) {
        this.fName = fName;
    }

    public void setLastName(String lName) {
        this.lName = lName;
    }

    public boolean equals(Object o){
        return o.toString().equals(this.toString());
    }
}
