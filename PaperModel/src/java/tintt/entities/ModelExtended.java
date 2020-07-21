/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tintt.entities;

/**
 *
 * @author TiTi
 */
public class ModelExtended extends Model{

    public ModelExtended(Model model,double totalHour) {
        super(model.getId(), model.getName(), model.getNumOfSheets(), model.getNumOfParts(),model.getDifficulty(),model.getImageSrc(),
                model.getLink(),model.getCategoryId(),model.getTagCollection(),model.getHasInstruction());
        this.totalHour = totalHour;
    }

    
    private double totalHour;

    public double getTotalHour() {
        return totalHour;
    }

    public void setTotalHour(double totalHour) {
        this.totalHour = totalHour;
    }
    
    }
