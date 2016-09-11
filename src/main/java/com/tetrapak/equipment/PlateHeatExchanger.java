/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.equipment;

/**
 * This class models a Plate Heat Exchanger
 *
 * @author SEPALMM
 */
public class PlateHeatExchanger {

    String modelType;
    int numberOfPlates;
    int gasketInterval;
    int plateInterval;

//    Process temperature (lifetime factor)
    int processTempC;

    public PlateHeatExchanger(String modelType, int numberOfPlates,
            int gasketInterval, int plateInterval, int processTempC) {
        this.modelType = modelType;
        this.numberOfPlates = numberOfPlates;
        this.gasketInterval = gasketInterval;
        this.plateInterval = plateInterval;
        this.processTempC = processTempC;
    }

    public String getModelType() {
        return modelType;
    }

    public int getNumberOfPlates() {
        return numberOfPlates;
    }

    public int getGasketInterval() {
        return gasketInterval;
    }

    public int getPlateInterval() {
        return plateInterval;
    }

    public int getProcessTempC() {
        return processTempC;
    }

}
