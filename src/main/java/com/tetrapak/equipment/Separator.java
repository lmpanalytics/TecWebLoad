/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.equipment;

/**
 * This class models a Centrifugal Separator
 *
 * @author SEPALMM
 */
public class Separator {

    //    Service kit intervals
    int intermediateKitInterval;
    int majorKitInterval;
    int oWMCkitInterval;
    int fFkitInterval;

    public Separator(int intermediateKitInterval, int majorKitInterval,
            int oWMCInterval, int fFInterval) {
        this.intermediateKitInterval = intermediateKitInterval;
        this.majorKitInterval = majorKitInterval;
        this.oWMCkitInterval = oWMCInterval;
        this.fFkitInterval = fFInterval;
    }

    public int getIntermediateKitInterval() {
        return intermediateKitInterval;
    }

    public int getMajorKitInterval() {
        return majorKitInterval;
    }

    public int getoWMCkitInterval() {
        return oWMCkitInterval;
    }

    public int getfFkitInterval() {
        return fFkitInterval;
    }

}
