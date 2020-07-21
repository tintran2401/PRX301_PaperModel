/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tintt.kit168;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import tintt.constant.AppConstant;
import tintt.constant.URLConstants;
import tintt.crawler.BaseThread;

/**
 *
 * @author TiTi
 */
public class KitThread extends BaseThread implements Runnable {

    private final String URL = URLConstants.KIT168;
    private final ServletContext context;

    public KitThread(ServletContext context) {
        this.context = context;
    }

    @Override
    public void run() {
        while (true) {
            try {
                KitCategoryCrawler categoryCrawler = new KitCategoryCrawler(context);
                Map<String, String> categories = categoryCrawler.getCategories(URL);

                for (Map.Entry<String, String> entry : categories.entrySet()) {
                    Thread pageCrawlingThread = new Thread(
                            new KitCategoryPageCrawler(context, entry.getKey(), entry.getValue()));
                    pageCrawlingThread.start();
                    
                    synchronized (BaseThread.getInstance()) {
                        while (BaseThread.isSuspended()) {
                            BaseThread.getInstance().wait();
                        }
                    }
                }
                KitThread.sleep(AppConstant.CRAWLING_INTERVAL);
                synchronized (BaseThread.getInstance()) {
                    while (BaseThread.isSuspended()) {
                        BaseThread.getInstance().wait();
                    }
                }
            } catch (InterruptedException e) {
                Logger.getLogger(KitThread.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}
