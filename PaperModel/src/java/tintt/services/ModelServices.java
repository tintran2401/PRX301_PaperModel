/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tintt.services;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import tintt.dao.ModelDAO;
import tintt.entities.Model;
import tintt.entities.ModelExtended;

/**
 *
 * @author TiTi
 */
public class ModelServices {

    private static final double AVGPARTPERSHEET = 8.4;
    private static final double TIMECONSTANT = 0.75;

    public List<ModelExtended> getModelByMakingTime(int difficulty, int skillLevel, double hour) {
        double hourPerSheet, totalHours;
        List<ModelExtended> resultList = new ArrayList<>();
        ModelDAO dao = new ModelDAO();

        List<Model> models = dao.getAllModels();
        DecimalFormat df = new DecimalFormat("#.##");
        for (Model model : models) {
            Integer numOfPart = model.getNumOfParts();
            Integer numOfSheet = model.getNumOfSheets();
            if (model.getNumOfParts() == null) {
                hourPerSheet = TIMECONSTANT * difficulty / skillLevel;
                totalHours = hourPerSheet * numOfSheet;
                totalHours = Double.valueOf(df.format(totalHours));
                if ((model.getDifficulty() + 1) / 2 == difficulty && totalHours <= hour) {
                    ModelExtended modelExtend = new ModelExtended(model, totalHours);
                    resultList.add(modelExtend);
                }
            } else {
                double partPerSheet = numOfPart / numOfSheet;
                hourPerSheet = TIMECONSTANT * difficulty / skillLevel * partPerSheet / AVGPARTPERSHEET;
                totalHours = hourPerSheet * numOfSheet;
                totalHours = Double.valueOf(df.format(totalHours));
                if ((model.getDifficulty() + 1) / 2 == difficulty && totalHours <= hour) {
                    ModelExtended modelExtend = new ModelExtended(model, totalHours);
                    resultList.add(modelExtend);
                }
            }
        }
        Collections.sort(resultList, (ModelExtended o1, ModelExtended o2)
                -> Double.valueOf(o2.getTotalHour()).compareTo(o1.getTotalHour()));
        return resultList;
    }

    public List<Model> getModelByTagAndCategory(String category) {
        List<Model> resultList = new ArrayList<>();
        ModelDAO dao = new ModelDAO();
        for (Model model : dao.getModelsByTagAndCate(category)) {
            resultList.add(model);
            if (resultList.size() == 200) {
                break;
            }
        }
        return resultList;
    }

}
