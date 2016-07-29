

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Windows on 2016-07-25.
 */
public class Main {
    static OptometryPanel op;
    public static Color mainBlue = new Color(66, 113, 255);
    public static Person [] people;
    public static void main(String [] args){
        setupFolders();
        loadDoctors();
        op = new OptometryPanel();
        op.showPane();
//        try {
//            new ExportManager().newPrinting();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //new ExportManager().sendFax();
    }

    public static String [] readFile(String name) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(name));
        ArrayList<String> lines = new ArrayList<>();
        in.mark(Short.MAX_VALUE);
        String line = "";

        while((line = in.readLine()) != null){
            lines.add(line);
        }

        in.close();
        String [] toReturn = new String[lines.size()];
        lines.toArray(toReturn);
        return toReturn;

    }

    public static String [] readFile(InputStream fis) throws IOException {
        String s = "";

        try  {
            int content;
            while ((content = fis.read()) != -1) {
                // convert to char and display it
                s += (char) content;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return s.split("\n");
    }

    public static boolean writeFile(String filePath, String name) {
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
            out.write(name);
            out.close();
        } catch (IOException e){
            return false;
        }
        return true;
    }

    public static void loadDoctors(){
        try {
            File docFolder = new File("data/doctors");
            String [] names = docFolder.list();
            people = new Person[names.length];

            for (int i = 0; i < names.length; i++){
                FileInputStream fileIn = new FileInputStream("data/doctors/" + names[i]);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                people[i] = (Person) in.readObject();
                in.close();
                fileIn.close();
            }
            System.out.println(Arrays.toString(names));
        }catch(Exception i) {
            i.printStackTrace();
        }
    }

    public static Person getDoctor(String lName){
        for (int i = 0; i < people.length; i++) {
            if(people[i].toString().equals(lName)) return people[i];
        }
        return null;
    }

    public static void setupFolders(){
        File data = new File("data");
        if(!data.exists()){
            data.mkdir();
        }

        File doctors = new File("data/doctors");
        if(!doctors.exists()){
            doctors.mkdir();
        }
    }
}
