/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.equipment;

/**
 * This class models a Tubular Heat Exchanger
 *
 * @author SEPALMM
 */
public class TubularHeatExchanger {

    String modelType;
    int numberOfNonRegenerativeTubes;
    int numberOfRegenerativeTubes;
    int numberOfHoldingTubes;
    int oRingInterval;
    int prodSealInterval;

    public TubularHeatExchanger(String modelType,
            int numberOfNonRegenerativeTubes, int numberOfRegenerativeTubes,
            int numberOfHoldingTubes, int oRingInterval, int prodSealInterval) {
        this.modelType = modelType;
        this.numberOfNonRegenerativeTubes = numberOfNonRegenerativeTubes;
        this.numberOfRegenerativeTubes = numberOfRegenerativeTubes;
        this.numberOfHoldingTubes = numberOfHoldingTubes;
        this.oRingInterval = oRingInterval;
        this.prodSealInterval = prodSealInterval;
    }

    public String getModelType() {
        return modelType;
    }

    public int getNumberOfNonRegenerativeTubes() {
        return numberOfNonRegenerativeTubes;
    }

    public int getNumberOfRegenerativeTubes() {
        return numberOfRegenerativeTubes;
    }

    public int getNumberOfHoldingTubes() {
        return numberOfHoldingTubes;
    }

    public int getoRingInterval() {
        return oRingInterval;
    }

    public int getProdSealInterval() {
        return prodSealInterval;
    }

}
