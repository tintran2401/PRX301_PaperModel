/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tintt.crawler;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.servlet.ServletContext;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

/**
 *
 * @author TiTi
 */
public class BaseCrawler {
     private ServletContext context;

    public BaseCrawler(ServletContext context) {
        this.context = context;
    }

    public ServletContext getContext() {
        return context;
    }

    protected BufferedReader getBufferedReaderForUrl(String urlString) 
            throws MalformedURLException, IOException {
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        
        String userAgent = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.180 Mobile Safari/537.36";
        
        connection.addRequestProperty("User-Agent", userAgent);
        InputStream is = connection.getInputStream();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        
        return reader;
    }
    
    protected XMLEventReader parseStringToXMLEventReader(String xmlSection) 
            throws UnsupportedEncodingException, XMLStreamException {
        byte[] byteArray = xmlSection.getBytes("UTF-8");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        
        XMLInputFactory factory = XMLInputFactory.newFactory();
        factory.setProperty(XMLInputFactory.IS_COALESCING, true);
        XMLEventReader reader = factory.createXMLEventReader(inputStream);
        
        return reader;
    }
}
