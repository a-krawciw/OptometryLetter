import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Windows on 2016-07-25.
 */
public class Person implements Serializable{
    protected String fName = "";
    protected String lName = "";
    protected String address = "";

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getFax() {
        return fax;
    }

    private String email = "";
    private String fax = "";

    public Person(String [] info){
        fName = info[0];
        lName = info[1];
        address = info[2];
        email = info[3];
        fax = info[4];
    }

    public Person(){}


    public void serialize(String filePath){
        try
        {
            FileOutputStream fileOut = new FileOutputStream(filePath);
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

    public void setFax(String fax) {
        this.fax = fax;
    }

    public boolean equals(Object o){
        return o.toString().equals(this.toString());
    }

    public String forSaving(){
        return lName + "_" + fName;
    }
}
