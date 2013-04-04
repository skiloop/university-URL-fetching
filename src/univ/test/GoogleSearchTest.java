/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package univ.test;

/**
 *
 * @author skiloop
 */
import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GoogleSearchTest {

    public static void main(String[] args) {
        try {
            URL url = new URL("http://www.google.com.hk/search?q=biao");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172 Safari/537.22");

            InputStream in = conn.getInputStream();
            Scanner scanner = new Scanner(in);

            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(GoogleSearchTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GoogleSearchTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}