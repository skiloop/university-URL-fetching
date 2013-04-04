/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package univ;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Objects;
import java.util.TreeSet;

/**
 *
 * @author skiloop
 */
public class University extends Department {

    /**
     * Domain of the university
     */
    private String domain;
    /**
     * departments of the university,domain to identify between different
     * departments
     */
    TreeSet<College> collegeTreeSet;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    /**
     * @return the domain
     */
    @Override
    public String getDomain() {
        return domain;
    }

    /**
     * @param domain the domain to set
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     *
     * @param name_
     * @param id_
     * @param url
     */
    public University(String name_, int id_, URL url) {
        super(name_, id_, url);
        domain = super.getDomain();
        collegeTreeSet = new TreeSet<>();
    }

    /**
     *
     * @param _name
     * @param _id
     * @param url
     */
    public University(String _name, int _id, String url) {
        super(_name, _id, url);
        domain = super.getDomain();
        collegeTreeSet = new TreeSet<>();
    }

    /**
     *
     * @param _name
     * @param _id
     */
    public University(String _name, int _id) {
        super(_name, _id);
        domain = null;
        collegeTreeSet = new TreeSet<>();
    }

    /**
     *
     * @param _name
     */
    public University(String _name) {
        this(_name, -1);
    }

    public University(int _id) {
        this(null, _id);
    }

    @Override
    public void print() {
        System.out.println("Domain:" + domain);
        super.print();
        Iterator<College> it = collegeTreeSet.iterator();
        while (it.hasNext()) {
            it.next().print();
        }
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        writer.write("Domain:" + domain);
        writer.newLine();
        super.print(writer);
        Iterator<College> it = collegeTreeSet.iterator();
        while (it.hasNext()) {
            it.next().print(writer);
        }
    }

    public void add(College college) {
        collegeTreeSet.add(college);
    }

    public void add(String colName, int colId) {
        collegeTreeSet.add(new College(colName, colId));
    }

    public void add(String colName, int colId,String colUrl) {
        collegeTreeSet.add(new College(colName, colId,colUrl));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof University) {
            return (this == null
                    ? obj == null
                    : ((this.domain.equals(((University) obj).domain))
                    || (getId() == ((University) obj).getId())));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.domain);
        return hash;
    }

    void fetchLinks() {
        String name = super.getName();
        setHomepage(DepartmentUrlRedriver.request(name, ""));
        Iterator<College> it = collegeTreeSet.iterator();
        while (it.hasNext()) {
            it.next().fetchLinks(name);
        }
    }

    @Override
    public void setHomepage(URL url) {
        super.setHomepage(url);
        updateDomain();
    }

    private void updateDomain() {
        URL url = super.getHomepage();
        if (url != null) {
            String tmpDomain = url.getHost().split("\\.", 2)[1];
            if (tmpDomain != null) {
                String[] fields = tmpDomain.split("\\.", 4);
                if (fields.length == 4) {
                    domain = tmpDomain.replace("^[^\\.]+\\.", "");
                } else {
                    domain = tmpDomain;
                }
            } else {
                domain = null;
            }
        } else {
            domain = null;
        }
    }
}
