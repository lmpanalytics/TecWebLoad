/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.load;

import java.awt.Toolkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class reads in two text files, one for the Installed base and one for
 * the sales data.
 *
 * Load time of complete IB 2016-06-03, Total time: 8:29.897s
 *
 * The data is uploaded to a Neo4j Database.
 *
 * @author SEPALMM
 */
public class Load {

	private static final String INPUT_FILE_IB = "dfNeoIB.csv";
	private static final String INPUT_FILE_SP = "dfNeoSP_Inv.csv";

	private static Logger logger = LoggerFactory.getLogger(Load.class);

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		try {
			// 1 Load generic IB and Sales data (use Cypher scripts in file
			// 'Load CSV script to Neo TecWeb.txt'

			Query.checkDatabaseIsRunning();
			System.out.println("\n********* STARTING LOAD PROCESS ********* ");
			Query.createIndexesAndConstraints();
			// ***************** START DATA LOAD *****************
			// These methods must be made in sequence

			Query.dropDBdata();
			Query.getDBcontent();
			Query.loadCSV_IB(INPUT_FILE_IB);
			Query.getDBcontent();
			Query.loadCSV_SP(INPUT_FILE_SP);
			Query.fixDualMarketLinks();
			Query.getDBcontent();

			// ***************** DATA LOAD END *****************
			// 2 Write Model specific data
			Query.addHomogeniser();
			Query.addSeparator();
			Query.addPHE();
			Query.addTHE();
			Query.addFrigusFreezer();
			Query.addIngredientDoser();

			Query.dropIdexesAndConstraints();

			Toolkit.getDefaultToolkit().beep();

		} finally {
			Query.closeNeo4jDriver();
			logger.info("Succesfully closed DB Driver");
		}

	}

}
