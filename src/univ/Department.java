/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package univ;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author skiloop
 */
public class Department implements Comparable {

    /**
     * name of Department
     */
    private String name;
    /**
     * id of the Department
     */
    private int id;
    /**
     * home page of the Department
     */
    private URL homepage;
    /**
     * parameter deciding how good is a Department
     */
    private int score;

    /**
     * Constructor initials name=name_,id=id_,and homepage=url_,score=0
     *
     * @param name_
     * @param id_
     * @param url_
     */
    public Department(String name_, int id_, URL url_) {
        name = name_;
        id = id_;
        homepage = url_;
        score = 0;
    }

    public Department(String name_, int id_, String url) {
        name = name_;
        id = id_;
        try {
            if (url != null&&!url.isEmpty()) {
                homepage = new URL(url);
            } else {
                homepage = null;
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Department.class.getName()).log(Level.SEVERE, null, ex);
            homepage = null;
        }
        score = 0;
    }

    public Department(String _name, int _id) {
        name = _name;
        id = _id;
        homepage = null;
        score = 0;
    }

    public Department(String _name) {
        this(_name, -1);
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Department) {
            return this.hashCode() * 10 + this.score - ((Department) o).score;
        }
        return -1;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the homepage
     */
    public URL getHomepage() {
        return homepage;
    }

    /**
     * @param homepage the homepage to set
     */
    public void setHomepage(URL homepage) {
        this.homepage = homepage;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    public void print() {
        System.out.println("Name:" + name);
        System.out.println("ID:" + id);
        System.out.println("URL:" + homepage);
    }

    public void print(BufferedWriter writer) throws IOException {
        writer.write("Name:" + name);
        writer.newLine();
        writer.write("ID:" + id);
        writer.newLine();
        writer.write("URL:" + homepage);
        writer.newLine();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Department) {
            return (this == null
                    ? obj == null
                    : ((this.name.equals(((Department) obj).name))
                    && (this.id == ((Department) obj).id)));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.name);
        hash = 37 * hash + this.id;
        return hash;
    }
    public String getHost(){
        if(homepage!=null){
            return homepage.getHost();
        }
        return null;
    }
    public String getDomain(){
        String host=getHost();
        if(host!=null){
            return host.split("\\.", 2)[1];
        }
        return null;
    }
}
