/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tintt.dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletContext;
import tintt.difficulti_estimation.DifficultyEstimation;
import tintt.difficulti_estimation.ModelEstimation;
import tintt.entities.Model;
import tintt.utils.DBUtils;

/**
 *
 * @author TiTi
 */
public class ModelDAO extends BaseDAO<Model, Integer> {

    public ModelDAO() {

    }
    private static ModelDAO instance;
    private static final Object LOCK = new Object();

    public static ModelDAO getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new ModelDAO();
            }
        }
        return instance;
    }

    public List<Model> getAllModels() {
        EntityManager em = DBUtils.getEntityManager();
        try {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            List<Model> models = em.createNamedQuery("Model.findAll")
                    .getResultList();
            return models;
        } catch (Exception e) {
            Logger.getLogger(ModelDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public Model getModelByLink(String link) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();

            List<Model> models = em.createNamedQuery("Model.findByLink")
                    .setParameter("link", link)
                    .getResultList();

            transaction.commit();

            if (models != null && !models.isEmpty()) {
                return models.get(0);
            }
        } catch (Exception e) {
            Logger.getLogger(ModelDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public synchronized Model saveModelWhileCrawling(ServletContext context, Model model) {
        Model existedModel = getModelByLink(model.getLink());
        if (existedModel == null) {
            refineModel(context, model);
            return create(model);
        }
        refineModel(context, model);
        model.setId(existedModel.getId());
        return update(model);
    }

    public synchronized void refineModel(ServletContext context, Model model) {
        ModelEstimation modelEstimation
                = (ModelEstimation) context.getAttribute("MODEL_ESTIMATION");
        if (modelEstimation == null) {
            return;
        }

        Integer numOfParts = model.getNumOfParts();
        Integer numOfSheets = model.getNumOfSheets();

        if (model.getDifficulty() == null || model.getDifficulty() == 0) {
            if (numOfSheets != null && numOfSheets > 0) {
                if (numOfParts != null && numOfParts > 0) {
                    Double partsPerSheet = 1.0 * numOfParts / numOfSheets;
                    Integer difficulty = estimateDifficulty(partsPerSheet, modelEstimation);

                    model.setDifficulty(difficulty);
                } else {
                    Integer difficulty = estimateDifficulty(numOfSheets, modelEstimation);
                    model.setDifficulty(difficulty);
                }
            }
        }
    }

    private synchronized Integer estimateDifficulty(Double partsPerSheet, ModelEstimation estimation) {
        DifficultyEstimation lowestDifficulty = estimation.getDifficultyEstimation().get(0);

        if (partsPerSheet <= lowestDifficulty.getMaxPartsPerSheet().doubleValue()) {
            return lowestDifficulty.getDifficulty().intValue();
        }

        for (int i = 1; i < estimation.getDifficultyEstimation().size(); i++) {
            DifficultyEstimation de = estimation.getDifficultyEstimation().get(i);
            if (partsPerSheet <= de.getMaxPartsPerSheet().doubleValue()) {
                return de.getDifficulty().intValue();
            }
        }

        return 0;
    }

    private synchronized Integer estimateDifficulty(Integer numOfSheets, ModelEstimation estimation) {
        DifficultyEstimation lowestDifficulty = estimation.getDifficultyEstimation().get(0);

        if (numOfSheets <= lowestDifficulty.getMaxNumberOfSheets().intValue()) {
            return lowestDifficulty.getDifficulty().intValue();
        }

        for (int i = 1; i < estimation.getDifficultyEstimation().size(); i++) {
            DifficultyEstimation de = estimation.getDifficultyEstimation().get(i);
            if (numOfSheets <= de.getMaxNumberOfSheets().intValue()) {
                return de.getDifficulty().intValue();
            }
        }
        return 0;
    }

    public List<Model> getModelsByTagAndCate(String categoryId) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            List<Model> models = em.createNamedQuery("Model.findByTagAndCategory")
                    .setParameter("categoryId", categoryId)
                    .getResultList();
            System.out.println("size: "+models.size());
            return models;
        } catch (Exception e) {
            Logger.getLogger(ModelDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }
}
