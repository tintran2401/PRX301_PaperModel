/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tintt.papermuseum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import static org.eclipse.persistence.queries.ObjectBuildingQuery.LOCK;
import tintt.constant.AppConstant;
import tintt.constant.URLConstants;
import tintt.crawler.BaseCrawler;
import tintt.crawler.BaseThread;
import tintt.dao.CategoryDAO;
import tintt.entities.Category;
import tintt.utils.CategoryHelper;
import tintt.utils.ElementChecker;

/**
 *
 * @author TiTi
 */
public class PaperMusCategoryPageCrawler extends BaseCrawler implements Runnable {

    private static final String MUSEUM_URL = URLConstants.PAPERCRAFTMUSEUM_MUSEUM;
    private String pageUrl;
    private String categoryName;

    public PaperMusCategoryPageCrawler(ServletContext context, String pageUrl, String categoryName) {
        super(context);
        this.pageUrl = pageUrl.replaceAll(" ", "%20");
        this.categoryName = categoryName;
    }

    @Override
    public void run() {
        Category category = createCategory(categoryName);
        if (category == null) {
            Logger.getLogger(PaperMusCategoryCrawler.class.getName())
                    .log(Level.SEVERE, null, new Exception("Error: category null"));
            return;
        }
        
        BufferedReader reader = null;
        
        try {
            reader = getBufferedReaderForUrl(pageUrl);
            String document = getCategoryPageDocument(reader);
            int lastPage = getLastPage(document);
            for (int i = 1; i <=lastPage; i++) {
                String categoryPageUrl = MUSEUM_URL + "/page/" + i + "/?cate=" + categoryName;
                Thread modelCrawler = new Thread(
                        new PaperMusModelCrawler(getContext(), categoryPageUrl, category));
                modelCrawler.start();

                if (i % AppConstant.CRAWL_THREAD_REDUCE > 0) {
                    modelCrawler.join();
                }

                synchronized (BaseThread.getInstance()) {
                    while (BaseThread.isSuspended()) {
                        BaseThread.getInstance().wait();
                    }
                }
            }
        } catch (IOException | InterruptedException | XMLStreamException e) {
            Logger.getLogger(PaperMusCategoryPageCrawler.class.getName())
                    .log(Level.SEVERE, null, e);
        }
    }

    private String getCategoryPageDocument(BufferedReader reader) throws IOException {
        String line = "";
        String document = "<catePages>";
        boolean isStart = false;
        while ((line = reader.readLine()) != null) {
            if (!isStart && line.contains("<div class=\"pagesnav\">")) {
                isStart = true;
            }
            if (isStart) {
                document += line.trim();
            }
            if (isStart && line.contains("</div>")) {
                break;
            }
        }
        document += "</catePages>";
        return document;
    }

    private int getLastPage(String document) throws UnsupportedEncodingException, XMLStreamException {
        int lastPage = 1;
        XMLEventReader eventReader = parseStringToXMLEventReader(document);
        XMLEvent event;
        boolean isStartCounter = false;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "div", "class", "pagesnav")) {
                    isStartCounter = true;
                } else if (ElementChecker.isElementWith(startElement, "a")) {
                    lastPage++;
                }
            } else if (isStartCounter && event.isEndElement()) {
                EndElement endElement = event.asEndElement();

                if (ElementChecker.isElementWith(endElement, "div")) {
                    break;
                }
            }
        }
        return lastPage;
    }

    private static final Object LOCK = new Object();

    protected Category createCategory(String name) {
        synchronized (LOCK) {
            Category category = null;
            String realName = getRealCategoryName(name);
            if (realName != null) {
                CategoryDAO dao = CategoryDAO.getInstance();
                category = dao.getFirstCategory(realName);
                if (category == null) {
                    category = new Category(CategoryHelper.generateUUID(), realName);
                    dao.create(category);
                }
            }
            return category;
        }
    }

    private String getRealCategoryName(String altName) {
        CategoryHelper helper = new CategoryHelper(getContext());
        return helper.getRealCategoryName(altName);
    }
}
