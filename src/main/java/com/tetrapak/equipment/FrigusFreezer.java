/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.equipment;

/**
 * This class models a Frigus Freezer
 *
 * @author SEPALMM
 */
public class FrigusFreezer {

    String modelType;
    /*int pumpQty;
    int bushingQty;
    int scraperKnifeQty;*/

    //    Service Kits
    int kit1000Interval;
    int kit1000Qty;
    int kit3000Interval;
    int kit3000Qty;
    int kit6000Interval;
    int kit6000Qty;
    int kit12000Interval;
    int kit12000Qty;
    //    Pump
    int newPumpInterval;
    int newPumpQty;
    int exchangePumpInterval;
    int exchangePumpQty;
//    Pump spare parts
    int kitExtendedInterval;
    int kitExtendedQty;
    int starWheelInterval;
    int starWheelQty;
    int coverInterval;
    int coverQty;
    int impellerInterval;
    int impellerQty;
    int gasketKitInterval;
    int gasketKitQty;
    int bushingInterval;
    int bushingQty;
//    Scraper
    int scraperKnifeInterval;
    int scraperKnifeQty;
    //    Cooling
    int sparePartsInterval;
    int sparePartsQty;
    //    Shaft
    int shaftSealInterval;
    int shaftSealQty;
//    Cylinder
    int cylinderKitInterval;
    int cylinderKitQty ;
    int gasketsInterval;
    int gasketQty;
    
//    Dasher
    int dasherKitInterval;
    int dasherKitQty;
    int singlePartsInterval;
    int singlePartQty;

    public FrigusFreezer(String modelType, int kit1000Interval, int kit1000Qty,
            int kit3000Interval, int kit3000Qty, int kit6000Interval,
            int kit6000Qty, int kit12000Interval, int kit12000Qty,
            int newPumpInterval, int newPumpQty, int exchangePumpInterval,
            int exchangePumpQty, int kitExtendedInterval, int kitExtendedQty,
            int starWheelInterval, int starWheelQty, int coverInterval,
            int coverQty, int impellerInterval, int impellerQty,
            int gasketKitInterval, int gasketKitQty, int bushingInterval,
            int bushingQty, int scraperKnifeInterval, int scraperKnifeQty,
            int sparePartsInterval, int sparePartsQty, int shaftSealInterval,
            int shaftSealQty, int cylinderKitInterval, int cylinderKitQty,
            int gasketsInterval, int gasketQty, int dasherKitInterval,
            int dasherKitQty, int singlePartsInterval, int singlePartQty) {
        this.modelType = modelType;
        this.kit1000Interval = kit1000Interval;
        this.kit1000Qty = kit1000Qty;
        this.kit3000Interval = kit3000Interval;
        this.kit3000Qty = kit3000Qty;
        this.kit6000Interval = kit6000Interval;
        this.kit6000Qty = kit6000Qty;
        this.kit12000Interval = kit12000Interval;
        this.kit12000Qty = kit12000Qty;
        this.newPumpInterval = newPumpInterval;
        this.newPumpQty = newPumpQty;
        this.exchangePumpInterval = exchangePumpInterval;
        this.exchangePumpQty = exchangePumpQty;
        this.kitExtendedInterval = kitExtendedInterval;
        this.kitExtendedQty = kitExtendedQty;
        this.starWheelInterval = starWheelInterval;
        this.starWheelQty = starWheelQty;
        this.coverInterval = coverInterval;
        this.coverQty = coverQty;
        this.impellerInterval = impellerInterval;
        this.impellerQty = impellerQty;
        this.gasketKitInterval = gasketKitInterval;
        this.gasketKitQty = gasketKitQty;
        this.bushingInterval = bushingInterval;
        this.bushingQty = bushingQty;
        this.scraperKnifeInterval = scraperKnifeInterval;
        this.scraperKnifeQty = scraperKnifeQty;
        this.sparePartsInterval = sparePartsInterval;
        this.sparePartsQty = sparePartsQty;
        this.shaftSealInterval = shaftSealInterval;
        this.shaftSealQty = shaftSealQty;
        this.cylinderKitInterval = cylinderKitInterval;
        this.cylinderKitQty = cylinderKitQty;
        this.gasketsInterval = gasketsInterval;
        this.gasketQty = gasketQty;
        this.dasherKitInterval = dasherKitInterval;
        this.dasherKitQty = dasherKitQty;
        this.singlePartsInterval = singlePartsInterval;
        this.singlePartQty = singlePartQty;
    }

    public String getModelType() {
        return modelType;
    }

    public int getKit1000Interval() {
        return kit1000Interval;
    }

    public int getKit1000Qty() {
        return kit1000Qty;
    }

    public int getKit3000Interval() {
        return kit3000Interval;
    }

    public int getKit3000Qty() {
        return kit3000Qty;
    }

    public int getKit6000Interval() {
        return kit6000Interval;
    }

    public int getKit6000Qty() {
        return kit6000Qty;
    }

    public int getKit12000Interval() {
        return kit12000Interval;
    }

    public int getKit12000Qty() {
        return kit12000Qty;
    }

    public int getNewPumpInterval() {
        return newPumpInterval;
    }

    public int getNewPumpQty() {
        return newPumpQty;
    }

    public int getExchangePumpInterval() {
        return exchangePumpInterval;
    }

    public int getExchangePumpQty() {
        return exchangePumpQty;
    }

    public int getKitExtendedInterval() {
        return kitExtendedInterval;
    }

    public int getKitExtendedQty() {
        return kitExtendedQty;
    }

    public int getStarWheelInterval() {
        return starWheelInterval;
    }

    public int getStarWheelQty() {
        return starWheelQty;
    }

    public int getCoverInterval() {
        return coverInterval;
    }

    public int getCoverQty() {
        return coverQty;
    }

    public int getImpellerInterval() {
        return impellerInterval;
    }

    public int getImpellerQty() {
        return impellerQty;
    }

    public int getGasketKitInterval() {
        return gasketKitInterval;
    }

    public int getGasketKitQty() {
        return gasketKitQty;
    }

    public int getBushingInterval() {
        return bushingInterval;
    }

    public int getBushingQty() {
        return bushingQty;
    }

    public int getScraperKnifeInterval() {
        return scraperKnifeInterval;
    }

    public int getScraperKnifeQty() {
        return scraperKnifeQty;
    }

    public int getSparePartsInterval() {
        return sparePartsInterval;
    }

    public int getSparePartsQty() {
        return sparePartsQty;
    }

    public int getShaftSealInterval() {
        return shaftSealInterval;
    }

    public int getShaftSealQty() {
        return shaftSealQty;
    }

    public int getCylinderKitInterval() {
        return cylinderKitInterval;
    }

    public int getCylinderKitQty() {
        return cylinderKitQty;
    }

    public int getGasketsInterval() {
        return gasketsInterval;
    }

    public int getGasketQty() {
        return gasketQty;
    }

    public int getDasherKitInterval() {
        return dasherKitInterval;
    }

    public int getDasherKitQty() {
        return dasherKitQty;
    }

    public int getSinglePartsInterval() {
        return singlePartsInterval;
    }

    public int getSinglePartQty() {
        return singlePartQty;
    }

    

}
