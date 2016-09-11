/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.equipment;

/**
 * This class models a Homogeniser
 *
 * @author SEPALMM
 */
public class Homogeniser {

//    Piston assembly
    String piston;
    int pistonInterval;
    int pistonQty;
    String pistonSeal;
    int pistonSealInterval;
    int pistonSealQty;
    String compressionRing;
    int compressionRingInterval;
    int compressionRingQty;
//    Forcer assembly
    String forcer;
    int forcerInterval;
    int forcerQty;
    int impactRingInterval;
    int impactRingQty;
    int seatInterval;
    int forcerSeatQty;
//    Valve assembly
    String valve;
    int valveInterval;
    int valveQty;
    int valveSealingInterval;
    int valveSealingQty;
    int valveSeatInterval;
    int valveSeatQty;
//    Crankcase assembly
    int plainBearingInterval;
    int plainBearingQty;
    int rollerBearingInterval;
    int rollerBearingQty;
    int bellowsInterval;
    int bellowQty;
//    Product type
    String productAbrasiveness;

    public Homogeniser(String piston, int pistonInterval, int pistonQty,
            String pistonSeal, int pistonSealInterval, int pistonSealQty,
            String compressionRing, int compressionRingInterval,
            int compressionRingQty, String forcer, int forcerInterval,
            int forcerQty, int impactRingInterval, int impactRingQty,
            int seatInterval, int forcerSeatQty, String valve, int valveInterval,
            int valveQty, int valveSealingInterval, int valveSealingQty,
            int valveSeatInterval, int valveSeatQty, int plainBearingInterval,
            int plainBearingQty, int rollerBearingInterval, int rollerBearingQty,
            int bellowsInterval, int bellowQty, String productAbrasiveness) {
        this.piston = piston;
        this.pistonInterval = pistonInterval;
        this.pistonQty = pistonQty;
        this.pistonSeal = pistonSeal;
        this.pistonSealInterval = pistonSealInterval;
        this.pistonSealQty = pistonSealQty;
        this.compressionRing = compressionRing;
        this.compressionRingInterval = compressionRingInterval;
        this.compressionRingQty = compressionRingQty;
        this.forcer = forcer;
        this.forcerInterval = forcerInterval;
        this.forcerQty = forcerQty;
        this.impactRingInterval = impactRingInterval;
        this.impactRingQty = impactRingQty;
        this.seatInterval = seatInterval;
        this.forcerSeatQty = forcerSeatQty;
        this.valve = valve;
        this.valveInterval = valveInterval;
        this.valveQty = valveQty;
        this.valveSealingInterval = valveSealingInterval;
        this.valveSealingQty = valveSealingQty;
        this.valveSeatInterval = valveSeatInterval;
        this.valveSeatQty = valveSeatQty;
        this.plainBearingInterval = plainBearingInterval;
        this.plainBearingQty = plainBearingQty;
        this.rollerBearingInterval = rollerBearingInterval;
        this.rollerBearingQty = rollerBearingQty;
        this.bellowsInterval = bellowsInterval;
        this.bellowQty = bellowQty;
        this.productAbrasiveness = productAbrasiveness;
    }

    public String getPiston() {
        return piston;
    }

    public int getPistonInterval() {
        return pistonInterval;
    }

    public int getPistonQty() {
        return pistonQty;
    }

    public String getPistonSeal() {
        return pistonSeal;
    }

    public int getPistonSealInterval() {
        return pistonSealInterval;
    }

    public int getPistonSealQty() {
        return pistonSealQty;
    }

    public String getCompressionRing() {
        return compressionRing;
    }

    public int getCompressionRingInterval() {
        return compressionRingInterval;
    }

    public int getCompressionRingQty() {
        return compressionRingQty;
    }

    public String getForcer() {
        return forcer;
    }

    public int getForcerInterval() {
        return forcerInterval;
    }

    public int getForcerQty() {
        return forcerQty;
    }

    public int getImpactRingInterval() {
        return impactRingInterval;
    }

    public int getImpactRingQty() {
        return impactRingQty;
    }

    public int getSeatInterval() {
        return seatInterval;
    }

    public int getForcerSeatQty() {
        return forcerSeatQty;
    }

    public String getValve() {
        return valve;
    }

    public int getValveInterval() {
        return valveInterval;
    }

    public int getValveQty() {
        return valveQty;
    }

    public int getValveSealingInterval() {
        return valveSealingInterval;
    }

    public int getValveSealingQty() {
        return valveSealingQty;
    }

    public int getValveSeatInterval() {
        return valveSeatInterval;
    }

    public int getValveSeatQty() {
        return valveSeatQty;
    }

    public int getPlainBearingInterval() {
        return plainBearingInterval;
    }

    public int getPlainBearingQty() {
        return plainBearingQty;
    }

    public int getRollerBearingInterval() {
        return rollerBearingInterval;
    }

    public int getRollerBearingQty() {
        return rollerBearingQty;
    }

    public int getBellowsInterval() {
        return bellowsInterval;
    }

    public int getBellowQty() {
        return bellowQty;
    }

    public String getProductAbrasiveness() {
        return productAbrasiveness;
    }

}
