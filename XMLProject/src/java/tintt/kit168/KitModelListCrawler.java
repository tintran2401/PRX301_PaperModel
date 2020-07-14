/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tintt.kit168;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import tintt.crawler.BaseCrawler;
import tintt.crawler.BaseThread;
import tintt.dao.ModelDAO;
import tintt.entities.Category;
import tintt.entities.Model;
import tintt.utils.ElementChecker;
import tintt.utils.TextUtils;

/**
 *
 * @author TiTi
 */
public class KitModelListCrawler extends BaseCrawler implements Runnable {

    private String pageUrl;
    private Category category;

    public KitModelListCrawler(ServletContext context, String pageUrl, Category category) {
        super(context);
        this.pageUrl = pageUrl;
        this.category = category;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        try {
            reader = getBufferedReaderForUrl(pageUrl);
            String document = getModelListDocument(reader);

            document = TextUtils.refineHtml(document);
            List<String> modelLinks = getModelLinks(document);

            for (String modelLink : modelLinks) {
                KitModelCrawler modelCrawler
                        = new KitModelCrawler(getContext(),modelLink, category);
                Model model = modelCrawler.getModel();
                if (model == null) {
                    continue;
                }
                    ModelDAO.getInstance().saveModelWhileCrawling(getContext(), model);

                synchronized (BaseThread.getInstance()) {
                    while (BaseThread.isSuspended()) {
                        BaseThread.getInstance().wait();
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private String getModelListDocument(BufferedReader reader) throws IOException {
        String line = "";
        String document = "";
        boolean isStart = false;
        while ((line = reader.readLine()) != null) {
            if (!isStart && line.contains("<div class=\"col-xs-12  col-md-8\" role=\"main\">")) {
                isStart = true;
            }
            if (isStart) {
                document += line.trim();
            }
            if (isStart && line.contains("<!-- Start of pagination -->")) {
                break;
            }
        }
        document += "</div>";
        return document;
    }

    private List<String> getModelLinks(String document)
            throws UnsupportedEncodingException, XMLStreamException {
        XMLEventReader eventReader = parseStringToXMLEventReader(document);
        XMLEvent event = null;
        List<String> links = new ArrayList<>();
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "a", "class", "read-more")) {
                    String link = getHref(startElement);
                    links.add(link);
                }
            }
        }
        return links;
    }

    private String getHref(StartElement element) {
        Attribute href = element.getAttributeByName(new QName("href"));
        return href == null ? "" : href.getValue();
    }

}
