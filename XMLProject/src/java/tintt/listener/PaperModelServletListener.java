/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tintt.listener;

import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import tintt.dao.ModelDAO;
import tintt.difficulti_estimation.ModelEstimation;
import tintt.entities.Model;
import tintt.kit168.KitThread;
import tintt.mapping_categories.CategoryMappings;
import tintt.papermuseum.PaperMusThread;
import tintt.utils.DBUtils;

/**
 * Web application lifecycle listener.
 *
 * @author TiTi
 */
public class PaperModelServletListener implements ServletContextListener {

    private static KitThread kidThread;
    private static PaperMusThread paperMusThread;
    private static String realPath;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext context = sce.getServletContext();
        realPath = context.getRealPath("/");

        CategoryMappings categoryMappings = getCategoryMappings(realPath);
        context.setAttribute("CATEGORY_MAPPINGS", categoryMappings);
        ModelEstimation modelEstimation = getModelEstimationConfig(realPath);
        context.setAttribute("MODEL_ESTIMATION", modelEstimation);

//        paperMusThread = new PaperMusThread(context);
//        paperMusThread.start();
//        kidThread = new KitThread(context);
//        kidThread.start();
        System.out.println("Went context!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        EntityManager em = DBUtils.getEntityManager();
        if (em != null) {
            em.close();
        }
        System.out.println("Destroy!!!");
    }

    private CategoryMappings getCategoryMappings(String realPath) {
        return CategoryMappings.getCategoryMappings(realPath);
    }

    private ModelEstimation getModelEstimationConfig(String realPath) {
        return ModelEstimation.getModelEstimation(realPath);
    }

    private List<Model> getAllModels() {
        ModelDAO modelDAO = ModelDAO.getInstance();
        return modelDAO.getAllModels();
    }
}
