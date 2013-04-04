/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package univ;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author skiloop
 */
public class SimpleUrlFetcher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        if (args.length < 1) {
            System.out.println("XX SimpleSeedFile");
            return;
        }
        File file = new File(args[0]);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SimpleUrlFetcher.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        String uName = "";
        String line;
        try {
            URL url;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\t", 3);
                if (fields.length < 2) {
                    continue;
                }
                if (fields.length == 3) {
                    url = DepartmentUrlRedriver.request(fields[1], fields[2]);
                    if (url != null) {
                        System.out.println(fields[0] + '\t' + fields[1] + '\t' + fields[2] + '\t' + url.toString());
                    } else {
                        System.out.println(fields[0] + '\t' + fields[1] + '\t' + fields[2]);
                    }
                } else if (fields.length == 2) {
                    url = DepartmentUrlRedriver.request(fields[1], null);
                    if (url != null) {
                        System.out.println(fields[0] + '\t' + fields[1] + '\t' + url.toString());
                    } else {
                        System.out.println(fields[0] + '\t' + fields[1]);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(SimpleUrlFetcher.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (reader != null) {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(SimpleUrlFetcher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
