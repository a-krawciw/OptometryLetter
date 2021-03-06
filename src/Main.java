

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Windows on 2016-07-25.
 */
public class Main {
    static OptometryPanel op;
    public static Color mainBlue = new Color(66, 113, 255);
    public static Doctor [] people;
    public static void main(String [] args) throws Exception {
        setupFolders();
        loadDoctors();
        op = new OptometryPanel();
        op.showPane();

//       ExportManager e =  new ExportManager();
//        e.setFilePath("data/test.pdf");
//        e.setDoctor(getDoctor("Dr. Krsa"));
//
//        FileInputStream fileIn = new FileInputStream("data/patients/As,cgh.ser");
//        ObjectInputStream in = new ObjectInputStream(fileIn);
//
//
//        e.setPatient( (Patient) in.readObject());
//        e.savePDF(null);
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
        System.out.println("hiya" + filePath);
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
            people = new Doctor[names.length];

            for (int i = 0; i < names.length; i++){
                FileInputStream fileIn = new FileInputStream("data/doctors/" + names[i]);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                people[i] = (Doctor) in.readObject();
                in.close();
                fileIn.close();
            }
            System.out.println(Arrays.toString(people));
        }catch(Exception i) {
            i.printStackTrace();
        }
    }

    public static Doctor getDoctor(String lName){
        System.out.println("k" + Arrays.toString(people));
        for (int i = 0; i < people.length; i++){
            if(people[i].toString().equals(lName)){
                return people[i];
            }
        }
        return null;
    }

    public static Patient getPatient(String id){
        Patient [] pats = null;
        try {
            File docFolder = new File("data/patients");
            String [] names = docFolder.list();
            pats = new Patient[names.length];

            for (int i = 0; i < names.length; i++){
                FileInputStream fileIn = new FileInputStream("data/patients/" + names[i]);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                pats[i] = (Patient) in.readObject();
                in.close();
                fileIn.close();
            }
            System.out.println("k" + Arrays.toString(pats));
        }catch(Exception i) {
            i.printStackTrace();
        }
        for (int i = 0; i < pats.length; i++) {
            if(pats[i].toString().equals(id)) return pats[i];
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

        File patients = new File("data/patients");
        if(!patients.exists()){
            patients.mkdir();
        }

        File fonts = new File("data/fonts");
        if(!fonts.exists()){
            fonts.mkdir();
            try {
                InputStream fis = Main.class.getResourceAsStream("/calibri.ttf");
                System.out.println("k" + fis.available());
                Files.copy(fis, new File("data/fonts/calibri.ttf").toPath());
            } catch (Exception e){
                e.printStackTrace();
            }

        }


    }

}
