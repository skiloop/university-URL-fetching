/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package univ;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author skiloop
 */
public class UniversityList extends ArrayList<University> {

    public static void main(String[] args) {
        // TODO code application logic here
        if (args.length < 2) {
            System.out.println(UniversityList.class + "\t univMapFile collegeFile");
            return;
        }
        File univMapFile = new File(args[0]);
        File collegeFile = new File(args[1]);
        File outFile;
        BufferedReader uniBufferedReader = null;
        BufferedReader colBufferedReader = null;
        BufferedWriter writer = null;
        try {
            UniversityList univArrayList = new UniversityList();

            // create BufferedReader
            uniBufferedReader = new BufferedReader(new FileReader(univMapFile));
            // read file
            univArrayList.readUnivMap(uniBufferedReader);
            // close file
            uniBufferedReader.close();
            // open BufferedReader
            colBufferedReader = new BufferedReader(new FileReader(collegeFile));
            // read file
            univArrayList.readCollege(colBufferedReader);
            // close file
            colBufferedReader.close();

            // fetching url
            univArrayList.fetchURL();
            
            // print results
            if (args.length < 3) {
                univArrayList.print();
            }else{
                outFile=new File(args[2]);
                writer = new BufferedWriter(new FileWriter(outFile));
                univArrayList.writer(writer);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UniversityList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UniversityList.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (uniBufferedReader != null) {
                    uniBufferedReader.close();
                }
                if (colBufferedReader != null) {
                    colBufferedReader.close();
                }
                if(writer!=null){
                    writer.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(UniversityList.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void readUnivMap(BufferedReader univMapReader) throws IOException {
        String line;
        while ((line = univMapReader.readLine()) != null) {
            String[] fields = line.split("\t", 2);
            if (fields.length == 2) {
                super.add(new University(fields[1], Integer.parseInt(fields[0])));
            } else {
                System.err.println("Error format:" + line);
            }
        }
    }

    public void readCollege(BufferedReader collegeReader) throws IOException {
        String line;
        while ((line = collegeReader.readLine()) != null) {
            String[] fields = line.split("\t", 3);
            if("不区分院系所".equals(fields[2]))continue;
            if (fields.length == 3) {
                int id = Integer.parseInt(fields[0]);
                int index = this.indexOf(id);
                if (index < 0) {
                    // not found university with id
                    // then add a new university with id
                    super.add(new University(id));
                    index = this.indexOf(id);

                    // just ensure if adding successfully
                    if (index < 0) {
                        System.err.println("Error adding:" + line);
                        continue;
                    }
                }
                // update collegeTreeSet of university with id=id
                super.get(index).add(fields[2], Integer.parseInt(fields[1]));
            }
        }
    }

    public void print() {
        Iterator<University> it = this.iterator();
        while (it.hasNext()) {
            it.next().print();
        }
    }

    public void writer(BufferedWriter writer) throws IOException {
        Iterator<University> it = this.iterator();
        while (it.hasNext()) {
            it.next().print(writer);
        }
    }
    
    public int indexOf(int id){
        for(int i=0;i<super.size();i++){
            if(super.get(i).getId()==id){
                return i;
            }
        }
        return -1;
    }

    public int indexOf(String name){
        if(name==null)return -1;
        for(int i=0;i<super.size();i++){
            if(super.get(i).getName().equals(name)){
                return i;
            }
        }
        return -1;
    }
    private void fetchURL() {
        Iterator<University> it = this.iterator();
        while (it.hasNext()) {
            it.next().fetchLinks();
        }
    }    
    
}
