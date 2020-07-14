/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tintt.kit168;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import tintt.constant.AppConstant;
import tintt.dao.CategoryDAO;
import tintt.crawler.BaseCrawler;
import tintt.crawler.BaseThread;
import tintt.entities.Category;
import tintt.utils.CategoryHelper;
import tintt.utils.ElementChecker;
import tintt.utils.TextUtils;

/**
 *
 * @author TiTi
 */
public class KitCategoryPageCrawler extends BaseCrawler implements Runnable {

    private String url;
    private String categoryName;

    public KitCategoryPageCrawler(ServletContext context, String url, String categoryName) {
        super(context);
        this.url = url;
        this.categoryName = categoryName;
    }

    @Override
    public void run() {
        Category category = createCategory(categoryName);
        if (category == null) {
            Logger.getLogger(KitCategoryPageCrawler.class.getName())
                    .log(Level.SEVERE, null, new Exception("Error: category null"));
            return;
        }
        BufferedReader reader = null;
        try {
            reader = getBufferedReaderForUrl(url);
            String document = getCategoryPageDocument(reader);
            document = TextUtils.refineHtml(document);
            synchronized (BaseThread.getInstance()) {
                while (BaseThread.isSuspended()) {
                    BaseThread.getInstance().wait();
                }
            }
            int lastPage = getLastPage(document);
            for (int i = 1; i < lastPage; i++) {
                String pageUrl = url + "page/" + i;
                Thread modelListCrawler = new Thread(new KitModelListCrawler(getContext(), pageUrl, category));
                modelListCrawler.start();
                //litmit number of running thread
                 if (i % AppConstant.CRAWL_THREAD_REDUCE > 0) {
                    modelListCrawler.join();
                }
            }
            synchronized (BaseThread.getInstance()) {
                while (BaseThread.isSuspended()) {
                    BaseThread.getInstance().wait();
                }
            }
        } catch (IOException | InterruptedException | XMLStreamException e) {
        }
    }

    private static final Object LOCK = new Object();

    private Category createCategory(String name) {
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

    private String getCategoryPageDocument(BufferedReader reader) throws IOException {
        String document = "";
        String line = "";
        boolean isStart = false;
        while ((line = reader.readLine()) != null) {
            if (!isStart && line.contains("<!-- Start of pagination -->")) {
                isStart = true;
            }
            if (isStart) {
                document += line.trim();
            }
            if (isStart && line.contains("<!-- End of pagination -->")) {
                break;
            }
        }
        return document;
    }

    private int getLastPage(String document)
            throws UnsupportedEncodingException, XMLStreamException {
        XMLEventReader eventReader = parseStringToXMLEventReader(document);
        XMLEvent event;
        int lastPage = 1;
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();

                if (ElementChecker.isElementWith(startElement, "a", "class", "page-numbers")) {
                    event = (XMLEvent) eventReader.next();
                    String pageNumberStr = event.asCharacters().toString();
                    lastPage = Integer.parseInt(pageNumberStr);
                }
            }
        }
        return lastPage;
    }
}
