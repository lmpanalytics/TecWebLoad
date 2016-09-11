/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.load;

import java.awt.Toolkit;
import java.time.LocalDateTime;

/**
 * This class reads in two text files, one for the Installed base and one for
 * the sales data.
 *
 * Load time of complete IB 2016-06-03, Total time:   8:29.897s 
 *
 * The data is uploaded to a Neo4j Database.
 *
 * @author SEPALMM
 */
public class Load {

    private static final String INPUT_FILE_IB = "dfNeoIB.csv";
    private static final String INPUT_FILE_SP = "dfNeoSP_Inv.csv";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
// 1 Load generic IB and Sales data (use Cypher scripts in file 'Load CSV script to Neo TecWeb.txt'

            Query.checkDatabaseIsRunning();
            System.out.println("\n********* STARTING LOAD PROCESS ********* ");
            Query.createIndexesAndConstraints();
// ***************** START DATA LOAD *****************
//  These methods must be made in sequence

            Query.dropDBdata();
            Query.getDBcontent();
            Query.loadCSV_IB(INPUT_FILE_IB);
            Query.getDBcontent();
            Query.loadCSV_SP(INPUT_FILE_SP);
            Query.getDBcontent();

// ***************** DATA LOAD END *****************
//        2 Write Model specific data
            Query.addHomogeniser();
            Query.addSeparator();
            Query.addPHE();
            Query.addTHE(); 
            Query.addFrigusFreezer();

            Query.dropIdexesAndConstraints();

            Toolkit.getDefaultToolkit().beep();

        } finally {
            Query.closeNeo4jDriver();
            System.out.printf("%n%s > Succesfully closed DB Driver\n",
                    LocalDateTime.now());
        }

    }

}
