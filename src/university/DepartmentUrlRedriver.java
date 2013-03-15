/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package university;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author skiloop
 */
public class DepartmentUrlRedriver {

    private static final String BAIDU_REQUEST_STR = "http://www.baidu.com/s?wd=";
    private static final String GOOGlE_REQUEST_STR = "http://www.google.com.hk/search?&hl=zh-CN&newwindow=1&safe=off&lr=lang_zh-CN&q=";
    private static final String BAIDU_PAGE_CONTRL = "&rn=3&lm=0&ct=1&ft=&tn=baiduadv";
    private static final String GOOGLE_PAGE_CONTRL = "";
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172 Safari/537.22";
    private static HttpURLConnection conn = null;
    private static int count = 0;
    private static final int MAXCOUNT = 10;
    private static final int sleepTime = 15;
    public static final int BAIDU = 2;
    public static final int GOOGLE = 1;
    public static final int MAX_TRY_COUNT = 10;
    public static final int MAX_CONTENT_LENGTH = 1024 * 1024;
    public static final int CONNECT_TIME_OUT_LIMIT = 600;//conection in millisecond
    public static final int READ_TIME_OUT_LIMIT = 300;//read time out millisecond

    /**
     * Get homepage from search engine via sending request.
     *
     * @param univName University Name
     * @param collName College name
     * @param searchEngine which search engine to use
     * @return URL String for the corresponding College of University
     */
    public static URL request(String univName, String collName, int searchEngine) {
        System.out.println(univName + "\t" + collName);
        String urlStr = parse(fetch(formURL(univName, collName, searchEngine)), searchEngine);
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException ex) {
            Logger.getLogger(DepartmentUrlRedriver.class.getName()).log(Level.SEVERE, null, ex);
        }
        return url;
    }

    public static URL request(String univName, String collName) {
        return request(univName, collName, GOOGLE);
    }

    /**
     * Fetching page with URL from Internet.
     *
     * @param url URL to be fetch
     * @return the page content as String
     */
    private static String fetch(URL url) {
        if (url == null) {
            return null;
        }
        StringBuilder contents = new StringBuilder(2048);
        BufferedReader br = null;

        check();
        try {
            String cookie = "";
            int trycount = 0;
            do {
                try {
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("User-Agent", USER_AGENT);
                    if (cookie.length() != 0) {
                        conn.setRequestProperty("Cookie", cookie);
                    }
                    conn.setConnectTimeout(CONNECT_TIME_OUT_LIMIT);
                    conn.setReadTimeout(READ_TIME_OUT_LIMIT);
                    conn.setInstanceFollowRedirects(false);
                    int code = conn.getResponseCode();
                    if (code == HttpURLConnection.HTTP_MOVED_TEMP) {
                        cookie += conn.getHeaderField("Set-Cookie") + ";";
                    }
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        break;
                    }
                } catch (SocketTimeoutException ex) {
                    //Logger.getLogger(DepartmentUrlRedriver.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Retried connection:"+trycount);
                    if (trycount < MAX_TRY_COUNT) {
                        continue;
                    } else {
                        break;
                    }
                } finally {
                    trycount++;
                }
            } while (trycount < MAX_TRY_COUNT);
            if (trycount >= MAX_TRY_COUNT) {
                System.out.println("Too many connection tries,connection failed.");
            } else {
                //conn.connect();                
                System.out.println("Read time out is set:"+conn.getReadTimeout());
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String urlData = "";
                try {
                    while (urlData != null) {
                        urlData = br.readLine();
                        contents.append(urlData);
                        if (contents.length() >= MAX_CONTENT_LENGTH) {
                            System.err.println("Content exceeds length limit");
                            contents.append('\n');
                            break;
                        }
                    }
                } catch (SocketTimeoutException ex) {
                    //Logger.getLogger(DepartmentUrlRedriver.class.getName()).log(Level.SEVERE, null, ex);
                    System.err.println("Read timeout exception:"+url.toString());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(DepartmentUrlRedriver.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(br);
        }
        return contents.toString();
    }

    private static void close(Reader br) {
        try {
            if (br != null) {

                br.close();

            }
        } catch (IOException ex) {
            Logger.getLogger(DepartmentUrlRedriver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Parse HTML content for link.
     *
     * @param content HTML page content
     * @return link for the Department
     */
    private static String parse(String content, int searchEngine) {
        switch (searchEngine) {
            case GOOGLE:
                return googleParser(content);
            case BAIDU:
                return baiduParser(content);
            default:
                return googleParser(content);
        }
    }

    private static String baiduParser(String content) {
        String[] strSplits = content.split("(<span class=\"g\">)", 3);
        //System.out.println(content);
        String firstResTable = null;
        if (strSplits.length >= 2) {
            firstResTable = strSplits[1];
            String[] spanSplit = firstResTable.split("(span)", 2);
            if (spanSplit.length == 2) {
                String rawUrl = spanSplit[0];
                String url = rawUrl.replaceAll("( class=\"g\">|<[^>]+>|\\d{1,2}小时前|\\d{4}-\\d{1,2}-\\d{1,2}|</)", "");
                url = url.replaceAll(" ", "");
                if (url != null) {
                    if (url.startsWith("http://")) {
                        return url;
                    } else {
                        return "http://" + url;
                    }
                }
            }
        }
        return null;
    }

    private static String googleParser(String content) {
        String[] strSplits = content.split("(<cite>|</cite>)", 3);
        //System.out.println(content);
        String rawUrl = null;
        if (strSplits.length >= 2) {
            rawUrl = strSplits[1];
            String url = rawUrl.replaceAll(" ", "");
            if (url != null) {
                if (url.startsWith("http://")) {
                    return url;
                } else {
                    return "http://" + url;
                }
            }
        }
        return null;
    }

    /**
     * Form request URL.
     *
     * @param univ University Name
     * @param coll College Name
     * @return URL to request
     */
    private static URL formURL(String univ, String coll, int searchEngine) {
        String requestStr = null;
        switch (searchEngine) {
            case GOOGLE:
                requestStr = formGoogleQueryStr(univ, coll);
                break;
            case BAIDU:
                requestStr = formBaiduQueryStr(univ, coll);
                break;
            default:
                requestStr = formGoogleQueryStr(univ, coll);
        }

        URL url = null;
        try {
            url = new URL(requestStr);


        } catch (MalformedURLException ex) {
            Logger.getLogger(DepartmentUrlRedriver.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return url;
    }

    private static String formBaiduQueryStr(String univ, String coll) {
        String requestStr = BAIDU_REQUEST_STR + univ;
        if (coll != null) {
            requestStr += "+" + coll + BAIDU_PAGE_CONTRL;
        }
        return requestStr;
    }

    private static String formGoogleQueryStr(String univ, String coll) {
        String requestStr = GOOGlE_REQUEST_STR + univ;
        if (coll != null) {
            requestStr += "+" + coll + GOOGLE_PAGE_CONTRL;
        }
        return requestStr;
    }

    private static void check() {
        if (count == MAXCOUNT) {
            try {
                Thread.currentThread().sleep(0, sleepTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(DepartmentUrlRedriver.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                count = 0;
                System.out.println("reset count");
            }
        } else {
            count++;
            System.out.println(count);
        }
    }
}
