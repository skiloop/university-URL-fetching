/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package univ;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Objects;

/**
 *
 * @author skiloop
 */
public class College extends Department {

    /**
     * host of the College
     */
    private String host;

    /**
     * @return the host
     */
    @Override
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     *
     * @param name_
     * @param id_
     * @param url
     */
    public College(String name_, int id_, URL url) {
        super(name_, id_, url);
        host = super.getHost();
    }

    /**
     *
     * @param _name
     * @param _id
     * @param url
     */
    public College(String _name, int _id, String url) {
        super(_name, _id, url);
        host = super.getHost();
    }

    public College(String _name, int _id) {
        super(_name, _id);
        host = null;
    }

    public College(String _name) {
        super(_name);
        host = null;
    }

    @Override
    public void print() {
        System.out.println("Host:" + host);
        super.print();
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        writer.write("Host:" + host);
        writer.newLine();
        super.print(writer);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof College) {
            return (this == null
                    ? obj == null
                    : ((this.host.equals(((College) obj).host))
                    && ((Department) obj).equals((Department) this)));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.host);
        return hash;
    }

    void fetchLinks(String univName) {
        setHomepage(DepartmentUrlRedriver.request(univName, super.getName()));
    }

    private void updateHost() {
        URL url = super.getHomepage();
        if (url != null) {
            host = url.getHost();
        } else {
            host = null;
        }
    }
    @Override
     public void setHomepage(URL url){
         super.setHomepage(url);
         updateHost();
     }
}
