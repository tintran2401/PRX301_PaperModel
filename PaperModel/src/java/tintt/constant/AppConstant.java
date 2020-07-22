/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tintt.constant;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author TiTi
 */
public class AppConstant {

    private static final int CRAWLING_DAY_INTERVAL = 1;
    public static final long CRAWLING_INTERVAL = TimeUnit.DAYS.toMillis(CRAWLING_DAY_INTERVAL);
    public static final int CRAWL_THREAD_REDUCE = 2;
}
