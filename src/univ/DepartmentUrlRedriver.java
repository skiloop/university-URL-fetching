/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package univ;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author skiloop
 */
public class DepartmentUrlRedriver {
    
    private static final String BAIDU_REQUEST_STR = "http://www.baidu.com/s?wd=";
    private static final String GOOGlE_REQUEST_STR = "https://www.google.com.hk/search?hl=zh-CN&newwindow=1&safe=off&lr=lang_zh-CN&q=";
    private static final String BAIDU_PAGE_CONTRL = "&rn=3&lm=0&ct=1&ft=&tn=baiduadv";
    private static final String GOOGLE_PAGE_CONTRL = "";
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172 Safari/537.22";
    private static HttpURLConnection conn = null;
    private static int count = 0;
    private static final int MAXCOUNT = 10;
    private static final long waitTime = 3500;
    public static final int BAIDU = 2;
    public static final int GOOGLE = 1;
    public static final int MAX_TRY_COUNT = 10;
    public static final int MAX_CONTENT_LENGTH = 512 * 1024;
    public static final int CONNECT_TIME_OUT_LIMIT = 1500;//conection in millisecond
    public static final int READ_TIME_OUT_LIMIT = 2000;//read time out millisecond
    public static long previousFetchTime = 0;
    private static boolean IsColleage = false;
    private static String previousDomain = null;

    /**
     * Get homepage from search engine via sending request.
     *
     * @param univName University Name
     * @param collName College name
     * @param searchEngine which search engine to use
     * @return URL String for the corresponding College of University
     */
    public static URL request(String univName, String collName, int searchEngine) {
        IsColleage = (collName == null || collName.isEmpty() ? false : true);
        System.out.println(univName + "\t" + collName);
        check();
        String urlStr = parse(fetch(formURL(univName, collName, searchEngine)), searchEngine);
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException ex) {
            //Logger.getLogger(DepartmentUrlRedriver.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error creating url:" + urlStr);
        }
        updatePreviousDomain(url);
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
        tryConnect(url);
        try {
            //conn.connect();                
            //System.out.println("Read time out is set:" + conn.getReadTimeout());
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
                System.err.println("Read timeout exception:" + url.toString());
            }
        } catch (IOException ex) {
            //Logger.getLogger(DepartmentUrlRedriver.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("IOError when fetching");
        } finally {
            if (br != null) {
                close(br);
            }
        }
        if (conn != null) {
            conn.disconnect();
        }
        return contents.toString();
    }
    
    private static void tryConnect(URL url) {
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
                System.out.println("Connect timeout now retry" + trycount);
                if (trycount < MAX_TRY_COUNT) {
                    continue;
                } else {
                    break;
                }
            } catch (IOException ex) {
                System.out.println("IOError:" + trycount);
            } finally {
                trycount++;
            }
        } while (trycount < MAX_TRY_COUNT);
        if (trycount >= MAX_TRY_COUNT) {
            System.out.println("Too many connection tries,connection failed.");
        }
    }
    
    private static void close(Reader br) {
        try {
            if (br != null) {
                br.close();
            }
        } catch (IOException ex) {
//            Logger.getLogger(DepartmentUrlRedriver.class
//                    .getName()).log(Level.SEVERE, null, ex);
            System.err.println("Cannot close reader");
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
    
    public static String googleParserTwo(String content) {

        //System.out.println(content);        
        String url = null;
        
        String[] strSplits = content.split("(</cite>|<cite>)", 5);
        String rawUrl;
        if (strSplits.length >= 2) {
            rawUrl = strSplits[1];
            url = rawUrl.replaceAll("( |<span[^>]*>|&rsaquo;|<a href.*</span>.*$|<[^>]+>)", "");
            if (strSplits.length >= 4 && rawUrl.length() > 30
                    && IsColleage && url.startsWith("www")) {
                rawUrl = strSplits[3];
                url = rawUrl.replaceAll("( |<span[^>]*>|&rsaquo;|<a href.*</span>.*$|<[^>]+>)", "");
            }
        }
        
        if (url != null) {
            if (!url.startsWith("http://")) {
                url = "http://" + url;
            }
        }
        return url;
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
    
    public static String googleParser(String content) {

        //System.out.println(content);        
        String url = null;
        
        String[] strSplits = content.split("(<h3 class=\"r\">)", 5);
        String rawUrl = null, rawUrl2 = null;
        if (strSplits.length >= 2) {
            rawUrl = strSplits[1].replaceAll("(<a href=\"|\" target=.*$)", "");
        }
        if (strSplits.length >= 3) {
            rawUrl2 = strSplits[2].replaceAll("(<a href=\"|\" target=.*$)", "");
        }
        if (rawUrl != null && rawUrl2 != null) {
            if (IsColleage && previousDomain != null) {
                if (rawUrl2.contains(previousDomain)
                        && (!rawUrl.contains(previousDomain)
                        || (rawUrl.contains("://www") && !rawUrl2.contains("://www")))) {
                    url = rawUrl2;
                    
                } else {
                    url = rawUrl;
                }
                
            } else if (IsColleage && rawUrl.matches("(baike|baidu)")) {
                url=rawUrl2;
            } else {
                url = rawUrl;
            }
        } else if (rawUrl == null) {
            url = rawUrl2;
        } else {
            url = rawUrl;
        }
        if (url != null && url.startsWith("/interstitial?url=")) {
            url = url.substring("/interstitial?url=".length());
        }
//        if (url != null) {
//            if (!url.startsWith("http://")) {
//                url = "http://" + url;
//            }
//        }
        return url;
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
        if (coll != null && !coll.isEmpty()) {
            requestStr += "+" + coll;
        }
        requestStr += BAIDU_PAGE_CONTRL;
        return requestStr;
    }
    
    private static String formGoogleQueryStr(String univ, String coll) {
        String requestStr = GOOGlE_REQUEST_STR + univ;
        if (coll != null && !coll.isEmpty()) {
            requestStr += "+" + coll;
        }
        requestStr += GOOGLE_PAGE_CONTRL;
        return requestStr;
    }
    
    private static void check() {
        if (previousFetchTime == 0) {
            previousFetchTime = System.currentTimeMillis();
        } else {
            long sleepTime = waitTime - (System.currentTimeMillis() - previousFetchTime);
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {
                }
            }
        }
    }
    
    private static void updatePreviousDomain(URL url) {
        if (!IsColleage && url != null) {
            String[] hostSplit = url.getHost().split("\\.", 2);
            if (hostSplit.length != 2) {
                previousDomain = null;
            }
            String tmpDomain = hostSplit[1];
            if (tmpDomain != null) {
                String[] fields = tmpDomain.split("\\.", 4);
                if (fields.length == 4) {
                    previousDomain = tmpDomain.replace("^[^\\.]+\\.", "");
                } else {
                    previousDomain = tmpDomain;
                }
            } else {
                previousDomain = null;
            }
        }
    }
}
