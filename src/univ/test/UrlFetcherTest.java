/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package univ.test;

import java.net.URL;
import univ.DepartmentUrlRedriver;

/**
 *
 * @author skiloop
 */
public class UrlFetcherTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String uName = "北京理工大学";
        String[] cName = {null,"宇航学院","继续教育学院","循环经济研究院","艺术设计学院","马克思主义学院","人文社会科学学院"};
        URL url;
        for (int i = 0; i < cName.length; i++) {
            url = DepartmentUrlRedriver.request(uName, cName[i]);
            if (url != null) {
                System.out.println(url.toString());
            } else {
                System.out.println("Null Url");
            }
        }
    }
}
