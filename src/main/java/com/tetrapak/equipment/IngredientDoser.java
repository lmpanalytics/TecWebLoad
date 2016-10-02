/**
 * 
 */
package com.tetrapak.equipment;

/**
 * @author SEPALMM This class models an Ingredient Doser
 */
public class IngredientDoser {

	String modelType;

	// Service Kits
	int kit3000Interval;
	int kit3000Qty;
	int kit6000Interval;
	int kit6000Qty;
	int kit12000Interval;
	int kit12000Qty;
	// Hopper
	int hopperKit3000Interval;
	int hopperKit3000Qty;
	int hopperKit6000Interval;
	int hopperKit6000Qty;
	int hopperKit12000Interval;
	int hopperKit12000Qty;
	// Mixer
	int mixerKit3000Interval;
	int mixerKit3000Qty;
	int mixerKit6000Interval;
	int mixerKit6000Qty;
	int mixerKit12000Interval;
	int mixerKit12000Qty;
	// Pump
	int pumpKit3000Interval;
	int pumpKit3000Qty;
	int pumpKit6000Interval;
	int pumpKit6000Qty;
	int pumpKit12000Interval;
	int pumpKit12000Qty;
	// Cover
	int coverKit3000Interval;
	int coverKit3000Qty;
	// Agitator
	int agitatorKit6000Interval;
	int agitatorKit6000Qty;
	int agitatorKit12000Interval;
	int agitatorKit12000Qty;

	// Lamella
	int lamellaInterval;
	int lamellaQty;
	// Dasher
	int dasherInterval;
	int dasherQty;
	// PumpHouse
	int pumpHouseInterval;
	int pumpHouseQty;

	public IngredientDoser(String modelType, int kit3000Interval, int kit3000Qty, int kit6000Interval, int kit6000Qty,
			int kit12000Interval, int kit12000Qty, int hopperKit3000Interval, int hopperKit3000Qty,
			int hopperKit6000Interval, int hopperKit6000Qty, int hopperKit12000Interval, int hopperKit12000Qty,
			int mixerKit3000Interval, int mixerKit3000Qty, int mixerKit6000Interval, int mixerKit6000Qty,
			int mixerKit12000Interval, int mixerKit12000Qty, int pumpKit3000Interval, int pumpKit3000Qty,
			int pumpKit6000Interval, int pumpKit6000Qty, int pumpKit12000Interval, int pumpKit12000Qty,
			int coverKit3000Interval, int coverKit3000Qty, int agitatorKit6000Interval, int agitatorKit6000Qty,
			int agitatorKit12000Interval, int agitatorKit12000Qty, int lamellaInterval, int lamellaQty,
			int dasherInterval, int dasherQty, int pumpHouseInterval, int pumpHouseQty) {
		this.modelType = modelType;
		this.kit3000Interval = kit3000Interval;
		this.kit3000Qty = kit3000Qty;
		this.kit6000Interval = kit6000Interval;
		this.kit6000Qty = kit6000Qty;
		this.kit12000Interval = kit12000Interval;
		this.kit12000Qty = kit12000Qty;
		this.hopperKit3000Interval = hopperKit3000Interval;
		this.hopperKit3000Qty = hopperKit3000Qty;
		this.hopperKit6000Interval = hopperKit6000Interval;
		this.hopperKit6000Qty = hopperKit6000Qty;
		this.hopperKit12000Interval = hopperKit12000Interval;
		this.hopperKit12000Qty = hopperKit12000Qty;
		this.mixerKit3000Interval = mixerKit3000Interval;
		this.mixerKit3000Qty = mixerKit3000Qty;
		this.mixerKit6000Interval = mixerKit6000Interval;
		this.mixerKit6000Qty = mixerKit6000Qty;
		this.mixerKit12000Interval = mixerKit12000Interval;
		this.mixerKit12000Qty = mixerKit12000Qty;
		this.pumpKit3000Interval = pumpKit3000Interval;
		this.pumpKit3000Qty = pumpKit3000Qty;
		this.pumpKit6000Interval = pumpKit6000Interval;
		this.pumpKit6000Qty = pumpKit6000Qty;
		this.pumpKit12000Interval = pumpKit12000Interval;
		this.pumpKit12000Qty = pumpKit12000Qty;
		this.coverKit3000Interval = coverKit3000Interval;
		this.coverKit3000Qty = coverKit3000Qty;
		this.agitatorKit6000Interval = agitatorKit6000Interval;
		this.agitatorKit6000Qty = agitatorKit6000Qty;
		this.agitatorKit12000Interval = agitatorKit12000Interval;
		this.agitatorKit12000Qty = agitatorKit12000Qty;
		this.lamellaInterval = lamellaInterval;
		this.lamellaQty = lamellaQty;
		this.dasherInterval = dasherInterval;
		this.dasherQty = dasherQty;
		this.pumpHouseInterval = pumpHouseInterval;
		this.pumpHouseQty = pumpHouseQty;
	}

	public String getModelType() {
		return modelType;
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

	public int getHopperKit3000Interval() {
		return hopperKit3000Interval;
	}

	public int getHopperKit3000Qty() {
		return hopperKit3000Qty;
	}

	public int getHopperKit6000Interval() {
		return hopperKit6000Interval;
	}

	public int getHopperKit6000Qty() {
		return hopperKit6000Qty;
	}

	public int getHopperKit12000Interval() {
		return hopperKit12000Interval;
	}

	public int getHopperKit12000Qty() {
		return hopperKit12000Qty;
	}

	public int getMixerKit3000Interval() {
		return mixerKit3000Interval;
	}

	public int getMixerKit3000Qty() {
		return mixerKit3000Qty;
	}

	public int getMixerKit6000Interval() {
		return mixerKit6000Interval;
	}

	public int getMixerKit6000Qty() {
		return mixerKit6000Qty;
	}

	public int getMixerKit12000Interval() {
		return mixerKit12000Interval;
	}

	public int getMixerKit12000Qty() {
		return mixerKit12000Qty;
	}

	public int getPumpKit3000Interval() {
		return pumpKit3000Interval;
	}

	public int getPumpKit3000Qty() {
		return pumpKit3000Qty;
	}

	public int getPumpKit6000Interval() {
		return pumpKit6000Interval;
	}

	public int getPumpKit6000Qty() {
		return pumpKit6000Qty;
	}

	public int getPumpKit12000Interval() {
		return pumpKit12000Interval;
	}

	public int getPumpKit12000Qty() {
		return pumpKit12000Qty;
	}

	public int getCoverKit3000Interval() {
		return coverKit3000Interval;
	}

	public int getCoverKit3000Qty() {
		return coverKit3000Qty;
	}

	public int getAgitatorKit6000Interval() {
		return agitatorKit6000Interval;
	}

	public int getAgitatorKit6000Qty() {
		return agitatorKit6000Qty;
	}

	public int getAgitatorKit12000Interval() {
		return agitatorKit12000Interval;
	}

	public int getAgitatorKit12000Qty() {
		return agitatorKit12000Qty;
	}

	public int getLamellaInterval() {
		return lamellaInterval;
	}

	public int getLamellaQty() {
		return lamellaQty;
	}

	public int getDasherInterval() {
		return dasherInterval;
	}

	public int getDasherQty() {
		return dasherQty;
	}

	public int getPumpHouseInterval() {
		return pumpHouseInterval;
	}

	public int getPumpHouseQty() {
		return pumpHouseQty;
	}

}
