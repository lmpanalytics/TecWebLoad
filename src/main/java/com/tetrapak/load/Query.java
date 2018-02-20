/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.load;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.Values;
import org.neo4j.driver.v1.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.equipment.FrigusFreezer;
import com.tetrapak.equipment.Homogeniser;
import com.tetrapak.equipment.IngredientDoser;
import com.tetrapak.equipment.PlateHeatExchanger;
import com.tetrapak.equipment.Separator;
import com.tetrapak.equipment.TubularHeatExchanger;

/**
 * This class runs queries to the database
 *
 * @author SEPALMM
 */
public class Query {

	private static final String HOSTNAME = "localhost:7687";

	// 'For most use cases it is recommended to use a single driver instance
	// throughout an application.'
	// pw neo4j on the server is "s7asTaba". pw neo4j on my local machine is
	// "Tokyo2000"
	private static final Driver DRIVER = GraphDatabase.
//	driver("bolt://" + HOSTNAME + "", AuthTokens.basic("neo4j", "s7asTaba"));
	driver("bolt://" + HOSTNAME + "", AuthTokens.basic("neo4j", "Tokyo2000"));
	// driver("bolt://" + HOSTNAME + "", AuthTokens.basic("neo4j", "neo4j"));

	private static Logger logger = LoggerFactory.getLogger(Query.class);

	/**
	 * Checks that the db is running and lists indexes and constraints in use
	 */
	public static void checkDatabaseIsRunning() {
		try (Session session = DRIVER.session()) {
			StatementResult result = session.run("CALL db.indexes()");
			logger.info("DB is running with indexes:");
			String s1 = "Description", s2 = "State", s3 = "Type";
			System.out.printf("%-40s %-10s %s\n", s1, s2, s3);
			while (result.hasNext()) {
				Record record = result.next();
				System.out.printf("%-40s %-10s %s\n", record.get(0), record.get(1), record.get(2));
			}
			StatementResult result1 = session.run("CALL db.constraints()");
			System.out.println("");
			logger.info("DB is running with constraints:");
			while (result1.hasNext()) {
				Record record = result1.next();
				System.out.printf("%s\n", record.get(0));
			}
		} catch (ClientException e) {
			logger.error("Exception in 'checkDatabaseIsRunning()':" + e + "\nProgram exits[1]...");
			System.exit(1);
		}
	}

	/**
	 * Set indexes prior to creating and/or merging nodes and relationships.
	 *
	 * Note: Creation of indexes is only necessary if the db has been deleted or
	 * if indexes have explicitly been dropped beforehand. Dropping the database
	 * (deleting data) DOES NOT by itself delete the indexes.
	 */
	public static void createIndexesAndConstraints() {
		try (Session session = DRIVER.session()) {

			try (Transaction tx = session.beginTransaction()) {
				// Customer entities
				tx.run("CREATE CONSTRAINT ON " + "(e:Entity) ASSERT e.id IS UNIQUE");
				tx.run("CREATE CONSTRAINT ON " + "(cg:CustGrp) ASSERT cg.id IS UNIQUE");
				tx.run("CREATE CONSTRAINT ON " + "(ct:CustType) ASSERT ct.name IS UNIQUE");

				// Markets
				tx.run("CREATE CONSTRAINT ON " + "(C:Country) ASSERT C.id IS UNIQUE");
				tx.run("CREATE CONSTRAINT ON " + "(m:Market) ASSERT m.id IS UNIQUE");
				tx.run("CREATE CONSTRAINT ON " + "(mg:MarketGrp) ASSERT mg.id IS UNIQUE");
				tx.run("CREATE CONSTRAINT ON " + "(cl:Cluster) ASSERT cl.id IS UNIQUE");
				tx.run("CREATE CONSTRAINT ON " + "(gm:GlobalMarket) ASSERT gm.id IS UNIQUE");

				// Equipments
				tx.run("CREATE CONSTRAINT ON " + "(eq:Equipment) ASSERT eq.id IS UNIQUE");
				tx.run("CREATE INDEX ON :Homogeniser(id)");
				tx.run("CREATE INDEX ON :Separator(id)");
				tx.run("CREATE INDEX ON :PHE(id)");
				tx.run("CREATE INDEX ON :THE(id)");
				tx.run("CREATE INDEX ON :Freezer(id)");
				tx.run("CREATE INDEX ON :IngredientDoser(id)");

				// Homogenisers
				tx.run("CREATE INDEX ON :Function(id)");
				tx.run("CREATE INDEX ON :Piston(id)");
				tx.run("CREATE INDEX ON :PistonSeal(id)");
				tx.run("CREATE INDEX ON :CompressionRing(id)");
				tx.run("CREATE INDEX ON :Forcer(id)");
				tx.run("CREATE INDEX ON :ImpactRing(id)");
				tx.run("CREATE INDEX ON :Seat(id)");
				tx.run("CREATE INDEX ON :Valve(id)");
				tx.run("CREATE INDEX ON :ValveSealing(id)");
				tx.run("CREATE INDEX ON :ValveSeat(id)");
				tx.run("CREATE INDEX ON :PlainBearing(id)");
				tx.run("CREATE INDEX ON :RollerBearing(id)");
				tx.run("CREATE INDEX ON :Bellow(id)");

				// Separators
				tx.run("CREATE INDEX ON :IMkit(id)");
				tx.run("CREATE INDEX ON :MSkit(id)");
				tx.run("CREATE INDEX ON :OWMCkit(id)");
				tx.run("CREATE INDEX ON :FFkit(id)");

				// PHEs
				tx.run("CREATE INDEX ON :CGasket(id)");
				tx.run("CREATE INDEX ON :CPlate(id)");
				tx.run("CREATE INDEX ON :MGasket(id)");
				tx.run("CREATE INDEX ON :MPlate(id)");
				tx.run("CREATE INDEX ON :HGasket(id)");
				tx.run("CREATE INDEX ON :HPlate(id)");
				tx.run("CREATE INDEX ON :PGasket(id)");
				tx.run("CREATE INDEX ON :PPlate(id)");
				tx.run("CREATE INDEX ON :Gasket(id)");
				tx.run("CREATE INDEX ON :Plate(id)");

				// THEs
				tx.run("CREATE INDEX ON :Ctube(id)");
				tx.run("CREATE INDEX ON :CoRing(id)");
				// tx.run("CREATE INDEX ON :Cseal(id)");
				tx.run("CREATE INDEX ON :MTtube(id)");
				tx.run("CREATE INDEX ON :MToRing(id)");
				// tx.run("CREATE INDEX ON :MTseal(id)");
				tx.run("CREATE INDEX ON :CIPtube(id)");
				tx.run("CREATE INDEX ON :CIPoRing(id)");
				// tx.run("CREATE INDEX ON :CIPseal(id)");
				tx.run("CREATE INDEX ON :Tube(id)");
				tx.run("CREATE INDEX ON :ORing(id)");
				tx.run("CREATE INDEX ON :Seal(id)");

				// Frigus Freezers
				tx.run("CREATE INDEX ON :Kit1000(id)");
				tx.run("CREATE INDEX ON :Kit3000(id)");
				tx.run("CREATE INDEX ON :Kit6000(id)");
				tx.run("CREATE INDEX ON :Kit12000(id)");

				tx.run("CREATE INDEX ON :NewPump(id)");
				tx.run("CREATE INDEX ON :ExchangePump(id)");

				tx.run("CREATE INDEX ON :ExtendedKit(id)");
				tx.run("CREATE INDEX ON :StarWheel(id)");
				tx.run("CREATE INDEX ON :Cover(id)");
				tx.run("CREATE INDEX ON :Impeller(id)");
				tx.run("CREATE INDEX ON :GasketKit(id)");
				tx.run("CREATE INDEX ON :Bushing(id)");

				tx.run("CREATE INDEX ON :ScraperKnife(id)");

				tx.run("CREATE INDEX ON :SpareParts(id)");

				tx.run("CREATE INDEX ON :ShaftSeal(id)");

				tx.run("CREATE INDEX ON :CylinderKit(id)");
				tx.run("CREATE INDEX ON :Gaskets(id)");

				tx.run("CREATE INDEX ON :DasherKit(id)");
				tx.run("CREATE INDEX ON :SingleParts(id)");

				// Frigus Ingredient Dosers
				tx.run("CREATE INDEX ON :DosKit3000(id)");
				tx.run("CREATE INDEX ON :DosKit6000(id)");
				tx.run("CREATE INDEX ON :DosKit12000(id)");

				tx.run("CREATE INDEX ON :HopperKit3000(id)");
				tx.run("CREATE INDEX ON :HopperKit6000(id)");
				tx.run("CREATE INDEX ON :HopperKit12000(id)");

				tx.run("CREATE INDEX ON :MixerKit3000(id)");
				tx.run("CREATE INDEX ON :MixerKit6000(id)");
				tx.run("CREATE INDEX ON :MixerKit12000(id)");

				tx.run("CREATE INDEX ON :PumpKitKit3000(id)");
				tx.run("CREATE INDEX ON :PumpKitKit6000(id)");
				tx.run("CREATE INDEX ON :PumpKitKit12000(id)");

				tx.run("CREATE INDEX ON :CoverKit3000(id)");

				tx.run("CREATE INDEX ON :AgitatorKit6000(id)");
				tx.run("CREATE INDEX ON :AgitatorKit12000(id)");

				tx.run("CREATE INDEX ON :Lamella(id)");

				tx.run("CREATE INDEX ON :Dasher(id)");

				tx.run("CREATE INDEX ON :PumpHouse(id)");

				// Parts
				tx.run("CREATE CONSTRAINT ON " + "(p:Part) ASSERT p.id IS UNIQUE");
				tx.run("CREATE CONSTRAINT ON " + "(pf:PartFamily) ASSERT pf.name IS UNIQUE");

				tx.success();
				logger.info("Created Indexes and Constraints");
			}

		} catch (ClientException e) {
			logger.error("Exception in 'createIndexesAndConstraints()':" + e);
		}
	}

	/**
	 * Drops indexes after creating and/or merging nodes and relationships.
	 *
	 * Dropping the database (deleting data) DOES NOT by itself delete the
	 * indexes.
	 */
	public static void dropIdexesAndConstraints() {
		try (Session session = DRIVER.session()) {

			try (Transaction tx = session.beginTransaction()) {
				// Customer entities
				tx.run("DROP CONSTRAINT ON " + "(e:Entity) ASSERT e.id IS UNIQUE");
				tx.run("DROP CONSTRAINT ON " + "(cg:CustGrp) ASSERT cg.id IS UNIQUE");
				tx.run("DROP CONSTRAINT ON " + "(ct:CustType) ASSERT ct.name IS UNIQUE");

				// Markets
				tx.run("DROP CONSTRAINT ON " + "(C:Country) ASSERT C.id IS UNIQUE");
				tx.run("DROP CONSTRAINT ON " + "(m:Market) ASSERT m.id IS UNIQUE");
				tx.run("DROP CONSTRAINT ON " + "(mg:MarketGrp) ASSERT mg.id IS UNIQUE");
				tx.run("DROP CONSTRAINT ON " + "(cl:Cluster) ASSERT cl.id IS UNIQUE");
				tx.run("DROP CONSTRAINT ON " + "(gm:GlobalMarket) ASSERT gm.id IS UNIQUE");

				// Equipments
				tx.run("DROP CONSTRAINT ON " + "(eq:Equipment) ASSERT eq.id IS UNIQUE");
				tx.run("DROP INDEX ON :Homogeniser(id)");
				tx.run("DROP INDEX ON :Separator(id)");
				tx.run("DROP INDEX ON :PHE(id)");
				tx.run("DROP INDEX ON :THE(id)");
				tx.run("DROP INDEX ON :Freezer(id)");
				tx.run("DROP INDEX ON :IngredientDoser(id)");

				// Homogenisers
				tx.run("DROP INDEX ON :Function(id)");
				tx.run("DROP INDEX ON :Piston(id)");
				tx.run("DROP INDEX ON :PistonSeal(id)");
				tx.run("DROP INDEX ON :CompressionRing(id)");
				tx.run("DROP INDEX ON :Forcer(id)");
				tx.run("DROP INDEX ON :ImpactRing(id)");
				tx.run("DROP INDEX ON :Seat(id)");
				tx.run("DROP INDEX ON :Valve(id)");
				tx.run("DROP INDEX ON :ValveSealing(id)");
				tx.run("DROP INDEX ON :ValveSeat(id)");
				tx.run("DROP INDEX ON :PlainBearing(id)");
				tx.run("DROP INDEX ON :RollerBearing(id)");
				tx.run("DROP INDEX ON :Bellow(id)");

				// Separators
				tx.run("DROP INDEX ON :IMkit(id)");
				tx.run("DROP INDEX ON :MSkit(id)");
				tx.run("DROP INDEX ON :OWMCkit(id)");
				tx.run("DROP INDEX ON :FFkit(id)");

				// PHEs
				tx.run("DROP INDEX ON :CGasket(id)");
				tx.run("DROP INDEX ON :CPlate(id)");
				tx.run("DROP INDEX ON :MGasket(id)");
				tx.run("DROP INDEX ON :MPlate(id)");
				tx.run("DROP INDEX ON :HGasket(id)");
				tx.run("DROP INDEX ON :HPlate(id)");
				tx.run("DROP INDEX ON :PGasket(id)");
				tx.run("DROP INDEX ON :PPlate(id)");
				tx.run("DROP INDEX ON :Gasket(id)");
				tx.run("DROP INDEX ON :Plate(id)");

				// THEs
				tx.run("DROP INDEX ON :Ctube(id)");
				tx.run("DROP INDEX ON :CoRing(id)");
				// tx.run("DROP INDEX ON :Cseal(id)");
				tx.run("DROP INDEX ON :MTtube(id)");
				tx.run("DROP INDEX ON :MToRing(id)");
				// tx.run("DROP INDEX ON :MTseal(id)");
				tx.run("DROP INDEX ON :CIPtube(id)");
				tx.run("DROP INDEX ON :CIPoRing(id)");
				// tx.run("DROP INDEX ON :CIPseal(id)");
				tx.run("DROP INDEX ON :Tube(id)");
				tx.run("DROP INDEX ON :ORing(id)");
				tx.run("DROP INDEX ON :Seal(id)");

				// Frigus Freezers
				tx.run("DROP INDEX ON :Kit1000(id)");
				tx.run("DROP INDEX ON :Kit3000(id)");
				tx.run("DROP INDEX ON :Kit6000(id)");
				tx.run("DROP INDEX ON :Kit12000(id)");

				tx.run("DROP INDEX ON :NewPump(id)");
				tx.run("DROP INDEX ON :ExchangePump(id)");

				tx.run("DROP INDEX ON :ExtendedKit(id)");
				tx.run("DROP INDEX ON :StarWheel(id)");
				tx.run("DROP INDEX ON :Cover(id)");
				tx.run("DROP INDEX ON :Impeller(id)");
				tx.run("DROP INDEX ON :GasketKit(id)");
				tx.run("DROP INDEX ON :Bushing(id)");

				tx.run("DROP INDEX ON :ScraperKnife(id)");

				tx.run("DROP INDEX ON :SpareParts(id)");

				tx.run("DROP INDEX ON :ShaftSeal(id)");

				tx.run("DROP INDEX ON :CylinderKit(id)");
				tx.run("DROP INDEX ON :Gaskets(id)");

				tx.run("DROP INDEX ON :DasherKit(id)");
				tx.run("DROP INDEX ON :SingleParts(id)");

				// Frigus Ingredient Dosers
				tx.run("DROP INDEX ON :DosKit3000(id)");
				tx.run("DROP INDEX ON :DosKit6000(id)");
				tx.run("DROP INDEX ON :DosKit12000(id)");

				tx.run("DROP INDEX ON :HopperKit3000(id)");
				tx.run("DROP INDEX ON :HopperKit6000(id)");
				tx.run("DROP INDEX ON :HopperKit12000(id)");

				tx.run("DROP INDEX ON :MixerKit3000(id)");
				tx.run("DROP INDEX ON :MixerKit6000(id)");
				tx.run("DROP INDEX ON :MixerKit12000(id)");

				tx.run("DROP INDEX ON :PumpKitKit3000(id)");
				tx.run("DROP INDEX ON :PumpKitKit6000(id)");
				tx.run("DROP INDEX ON :PumpKitKit12000(id)");

				tx.run("DROP INDEX ON :CoverKit3000(id)");

				tx.run("DROP INDEX ON :AgitatorKit6000(id)");
				tx.run("DROP INDEX ON :AgitatorKit12000(id)");

				tx.run("DROP INDEX ON :Lamella(id)");

				tx.run("DROP INDEX ON :Dasher(id)");

				tx.run("DROP INDEX ON :PumpHouse(id)");

				// Parts
				tx.run("DROP CONSTRAINT ON " + "(p:Part) ASSERT p.id IS UNIQUE");
				tx.run("DROP CONSTRAINT ON " + "(pf:PartFamily) ASSERT pf.name IS UNIQUE");

				tx.success();
				logger.info("Dropped Indexes and Constraints");
			}
		} catch (ClientException e) {
			logger.error("Exception in 'dropIndexesAndConstraints()':" + e);
		}
	}

	/**
	 * Loads IB data from a CSV file located in the working directory.
	 *
	 * @param INPUT_FILE_NAME
	 *            name of csv file to be loaded
	 */
	public static void loadCSV_IB(String INPUT_FILE_NAME) {

		try (Session session = DRIVER.session()) {
			String path = new File("C:/", INPUT_FILE_NAME).toString();
			// System.out.printf("Path is %s%n ",path);
			String cypherPathFormat = path.replace("\\", "/").replace("C:/", "file:///");
			// System.out.printf("cypherPathFormat is %s%n ",cypherPathFormat);
			logger.info("Load data from '{}'", cypherPathFormat);

			// Entities, Customer groups, Customer types and Countries
			String tx = "USING PERIODIC COMMIT LOAD CSV WITH HEADERS FROM \"" + cypherPathFormat
					+ "\" AS line WITH line " + "MERGE (entity: Entity {id: line.CustKey, name: UPPER(line.CustName)}) "
					+ "MERGE (custGrp: CustGrp {id: line.CustGroupKey, name: UPPER(line.CustGroupName)}) "
					+ "MERGE (custType: CustType {name: UPPER(line.CustTypeName)}) "
					+ "MERGE (country: Country {id: line.CountryCode, name: UPPER(line.CountryName)}) "
					+ "MERGE (entity)-[:IN]->(custGrp) " + "MERGE (entity)-[:IN]->(custType) "
					+ "MERGE (entity)-[:LOCATED]->(country)";

			session.run(tx);

			// Markets, Market groups and Clusters
			String tx10 = "USING PERIODIC COMMIT LOAD CSV WITH HEADERS FROM \"" + cypherPathFormat
					+ "\" AS line WITH line " + "MATCH (entity: Entity {id: line.CustKey}) "
					+ "MERGE (market: Market {id: line.MarketKey, name: UPPER(line.MarketName)}) "
					+ "MERGE (marketGrp: MarketGrp {id: line.MarketGroupKey, name: UPPER(line.MarketGroupName)}) "
					+ "MERGE (cluster: Cluster {id: line.Cluster}) "
					+ "MERGE (globalMarket: GlobalMarket {id: 'GLOBAL MARKET'} ) "
					+ "MERGE (entity)-[:LINKED]->(market) " + "MERGE (market)-[:IN]->(marketGrp) "
					+ "MERGE (marketGrp)-[:IN]->(cluster) " + "MERGE (cluster)-[:IN]->(globalMarket)";

			session.run(tx10);

			// Equipments
			String tx20 = "USING PERIODIC COMMIT LOAD CSV WITH HEADERS FROM \"" + cypherPathFormat
					+ "\" AS line WITH line " + "MATCH (entity: Entity {id: line.CustKey} )"
					+ "MERGE (equipment: Equipment {id: line.EquipmentKey, name: line.EquipmentName, serialNo: line.SerialNumber, eQtype: line.EquipmentType, machSystem: line.MachineSystem, material: line.Material, model: line.ModelNumber, constructionYear: toInt(CASE line.ConstructionYear WHEN 'NA' THEN '0' ELSE line.ConstructionYear END), runningHoursPA: toFloat(line.HoursPerYear), heatNoPlatesTubes: toInt(line.HeatNoPlatesTubes), heatPercRegen: toFloat(line.HeatPercRegen), homoValveDesign: line.HomoValveDesign}) "
					+ "MERGE (equipment)-[:IB_ROUTE {qty: toFloat(1), type: 'FINAL'}]->(entity)";

			session.run(tx20);

		} catch (ClientException e) {
			logger.error("Exception in 'loadCSV_IB': " + e + "\nExit code: 1");
			System.exit(1);
		}
	}

	/**
	 * Loads SP sales data from a CSV file located in the working directory.
	 *
	 * @param INPUT_FILE_NAME
	 *            name of csv file to be loaded
	 */
	public static void loadCSV_SP(String INPUT_FILE_NAME) {

		try (Session session = DRIVER.session()) {
			String path = new File("C:/", INPUT_FILE_NAME).toString();
			// System.out.printf("Path is %s%n ",path);
			String cypherPathFormat = path.replace("\\", "/").replace("C:/", "file:///");
			// System.out.printf("cypherPathFormat is %s%n ",cypherPathFormat);
			logger.info("Load data from '{}'", cypherPathFormat);

			// Entities of Final customers
			String tx = "USING PERIODIC COMMIT LOAD CSV WITH HEADERS FROM \"" + cypherPathFormat
					+ "\" AS line WITH line "
					/*
					 * Add any missing nodes (the setdiff) between the db and
					 * the csv file
					 */
					+ "OPTIONAL MATCH (e: Entity {id: line.CustKey} ) " + "WITH e, line " + "WHERE e.id IS NULL "
					+ "MERGE (:Entity {id: line.CustKey, name: UPPER(line.CustName)})";

			session.run(tx);

			// Entities of Customer Groups
			String tx10 = "USING PERIODIC COMMIT LOAD CSV WITH HEADERS FROM \"" + cypherPathFormat
					+ "\" AS line WITH line "
					/*
					 * Add any missing nodes (the setdiff) between the db and
					 * the csv file
					 */
					+ "OPTIONAL MATCH (c: CustGrp {id: line.CustGroupKey} ) " + "WITH c, line " + "WHERE c.id IS NULL "
					+ "MERGE (:CustGrp {id: line.CustGroupKey, name: UPPER(line.CustGroupName)})";

			session.run(tx10);

			// Entities of Customer Types
			String tx20 = "USING PERIODIC COMMIT LOAD CSV WITH HEADERS FROM \"" + cypherPathFormat
					+ "\" AS line WITH line "
					/*
					 * Add any missing nodes (the setdiff) between the db and
					 * the csv file
					 */
					+ "OPTIONAL MATCH (c: CustType {name: line.CustTypeName} ) " + "WITH c, line "
					+ "WHERE c.name IS NULL " + "MERGE (:CustType {name: UPPER(line.CustTypeName)})";

			session.run(tx20);

			// Customer Relationships Customer Key driven
			String tx30 = "USING PERIODIC COMMIT LOAD CSV WITH HEADERS FROM \"" + cypherPathFormat
					+ "\" AS line WITH line " + "MATCH (entity: Entity {id: line.CustKey} ) "
					+ "MATCH (custGrp: CustGrp {id: line.CustGroupKey} ) "
					+ "MATCH (custType: CustType {name: line.CustTypeName} ) " + "MERGE (entity)-[:IN]->(custGrp) "
					+ "MERGE (entity)-[:IN]->(custType)";

			session.run(tx30);

			// Markets
			String tx40 = "USING PERIODIC COMMIT LOAD CSV WITH HEADERS FROM \"" + cypherPathFormat
					+ "\" AS line WITH line "
					/*
					 * Add any missing nodes (the setdiff) between the db and
					 * the csv file
					 */
					+ "OPTIONAL MATCH (m: Market {id: line.MarketKey} ) " + "WITH m, line " + "WHERE m.id IS NULL "
					+ "MERGE (:Market {id: line.MarketKey, name: UPPER(line.MarketName)})";

			session.run(tx40);

			// Market groups
			String tx50 = "USING PERIODIC COMMIT LOAD CSV WITH HEADERS FROM \"" + cypherPathFormat
					+ "\" AS line WITH line "
					/*
					 * Add any missing nodes (the setdiff) between the db and
					 * the csv file
					 */
					+ "OPTIONAL MATCH (m: MarketGrp {id: line.MarketGroupKey} ) " + "WITH m, line "
					+ "WHERE m.id IS NULL "
					+ "MERGE (:MarketGrp {id: line.MarketGroupKey, name: UPPER(line.MarketGroupName)})";

			session.run(tx50);

			// Clusters
			String tx60 = "USING PERIODIC COMMIT LOAD CSV WITH HEADERS FROM \"" + cypherPathFormat
					+ "\" AS line WITH line "
					/*
					 * Add any missing nodes (the setdiff) between the db and
					 * the csv file
					 */
					+ "OPTIONAL MATCH (c: Cluster {id: line.Cluster} ) " + "WITH c, line " + "WHERE c.id IS NULL "
					+ "MERGE (:Cluster {id: line.Cluster})";

			session.run(tx60);

			// Market Relationships Customer Key driven
			String tx70 = "USING PERIODIC COMMIT LOAD CSV WITH HEADERS FROM \"" + cypherPathFormat
					+ "\" AS line WITH line " + "MATCH (entity: Entity {id: line.CustKey} ) "
					+ "MATCH (market: Market {id: line.MarketKey} ) "
					+ "MATCH (marketGrp: MarketGrp {id: line.MarketGroupKey} ) "
					+ "MATCH (cluster: Cluster {id: line.Cluster} ) " + "MERGE (entity)-[:LINKED]->(market) "
					+ "MERGE (market)-[:IN]->(marketGrp) " + "MERGE (marketGrp)-[:IN]->(cluster)";

			session.run(tx70);

			// Parts, Type and Quantity
			String tx80 = "USING PERIODIC COMMIT LOAD CSV WITH HEADERS FROM \"" + cypherPathFormat
					+ "\" AS line WITH line " + "MATCH (entity: Entity {id: line.CustKey} )  "
					+ "MERGE (part: Part {id: line.Material, name: UPPER(line.Type)})  "
					+ "MERGE (partFamily: PartFamily {name: UPPER(line.Type)})  "
					+ "MERGE (part)-[:ROUTE {qty: toFloat(line.Qty), type: 'FINAL'}]->(entity) "
					+ "MERGE (part)-[:MEMBER_OF]->(partFamily)";

			session.run(tx80);

			// House keeping
			try (Transaction tx100 = session.beginTransaction()) {
				// Delete nodes where id or name is 'NA', except Country nodes
				tx100.run(
						"MATCH (n) WHERE (n.id = 'NA' or n.name = 'NA') " + "AND NOT (n:Country) " + "DETACH DELETE n");
				// Set Equipment properties that equals 'NA' to null
				tx100.run("MATCH (e:Equipment)"
						+ "WHERE e.name = 'NA' OR e.serialNo = 'NA' OR e.eQtype = 'NA' OR e.machSystem = 'NA' OR e.material = 'NA' OR e.model = 'NA' OR e.homoValveDesign = 'NA'"
						+ "SET e.name = CASE e.name WHEN 'NA' THEN NULL ELSE e.name END "
						+ "SET e.serialNo = CASE e.serialNo WHEN 'NA' THEN NULL ELSE e.serialNo END "
						+ "SET e.eQtype = CASE e.eQtype WHEN 'NA' THEN NULL ELSE e.eQtype END "
						+ "SET e.machSystem = CASE e.machSystem WHEN 'NA' THEN NULL ELSE e.machSystem END "
						+ "SET e.material = CASE e.material WHEN 'NA' THEN NULL ELSE e.material END "
						+ "SET e.model = CASE e.model WHEN 'NA' THEN NULL ELSE e.model END "
						+ "SET e.homoValveDesign = CASE e.homoValveDesign WHEN 'NA' THEN NULL ELSE e.homoValveDesign END");

				// Set Equipment properties that equals 0 to null
				tx100.run("MATCH (e:Equipment)" + "WHERE e.heatNoPlatesTubes = 0 OR e.heatPercRegen = 0 "
						+ "SET e.heatNoPlatesTubes = CASE e.heatNoPlatesTubes WHEN 0 THEN NULL ELSE e.heatNoPlatesTubes END "
						+ "SET e.heatPercRegen = CASE e.heatPercRegen WHEN 0 THEN NULL ELSE e.heatPercRegen END ");

				tx100.success();

			}
		} catch (ClientException e) {
			logger.error("Exception in 'loadCSV_SP': " + e + "\nExit code: 1");
			System.exit(1);
		}
	}

	/**
	 * Pull equipments of type Homogenisers from the database and construct the
	 * functional areas
	 *
	 * @return map of Equipment numbers and Homogeniser class fields
	 */
	private static Map<String, Homogeniser> buildHomogenisers() {
		Map<String, Homogeniser> equipmentMap = new HashMap<>();

		try (Session session = DRIVER.session()) {

			try (Transaction tx = session.beginTransaction()) {
				StatementResult result = tx.run(
						"MATCH (e:Equipment) WHERE e.machSystem = {term1} OR e.machSystem = {term2}\n"
								+ "RETURN e.id AS ID, e.model AS Model, "
								+ "CASE WHEN e.model CONTAINS '150' THEN true WHEN e.model CONTAINS '200' THEN true WHEN e.homoValveDesign = 'TD' THEN true WHEN e.homoValveDesign CONTAINS 'Disc valve' THEN true ELSE false END AS hasTurnableDisk",
						Values.parameters("term1", "ALEX", "term2", "HOMO"));
				tx.success();
				while (result.hasNext()) {
					Record r = result.next();
					String key = r.get("ID").toString();
					String modelNumberEqmnt = r.get("Model").toString();
					boolean hasTurnableDisk = r.get("hasTurnableDisk").asBoolean();

					// Set service intervals for standard 3-piston machines
					/* Piston assembly */
					String piston = "Chromed";
					int pistonInterval = 4_000;
					String pistonSeal = "PSB1";
					int pistonSealInterval = 2_000;
					String compressionRing = "Standard";
					int compressionRingInterval = 4_000;
					/* Forcer assembly */
					String forcer = "HD100";
					int forcerInterval = 12_000;
					int impactRingInterval = 12_000;
					int seatInterval = 12_000;
					/* Valve assembly */
					String valve = "Mushroom";
					int valveInterval = 6_000;
					int valveSealingInterval = 3_000;
					int valveSeatInterval = 12_000;
					/* Crankcase assembly */
					int plainBearingInterval = 12_000;
					int rollerBearingInterval = 24_000;
					int bellowsInterval = 6_000;

					// Set quantities for 3-Piston Machines
					int pistonQty = 3;
					int pistonSealQty = 2 * 3;
					int compressionRingQty = 3;

					int forcerQty = 2;
					int impactRingQty = 0;
					int forcerSeatQty = 2;

					int valveQty = 2 * 3;
					int valveSealingQty = 5 * 3;
					int valveSeatQty = 2 * 3;

					int plainBearingQty = 3;
					int rollerBearingQty = 2;
					int bellowQty = 3;

					/*
					 * Shorten Piston family intervals for Aseptic machines.
					 * Machines are classified as Aseptic if the model number
					 * ends with capital letter A, preceeded by either a space
					 * or a digit
					 */
					if (modelNumberEqmnt.matches("^\".*\\sA\"$") || modelNumberEqmnt.matches("^\".*\\dA\"$")) {
						pistonInterval = 1500;
						pistonSealInterval = 500;
						compressionRingInterval = 500;
					}

					/*
					 * Increase part quantities and Crankcase family intervals
					 * for 5-Piston machines. Machines are classified as
					 * 5-Piston machines if the model number contains '40' or
					 * '350'
					 */
					if (modelNumberEqmnt.contains("40") || modelNumberEqmnt.contains("350")) {

						// Increase part quantities
						pistonQty = 5;
						pistonSealQty = 2 * 5;
						compressionRingQty = 5;

						valveQty = 2 * 5;
						valveSealingQty = 5 * 5;
						valveSeatQty = 2 * 5;

						plainBearingQty = 5;
						rollerBearingQty = 2 + 2;
						bellowQty = 5;

						// Increase intervals
						plainBearingInterval = 24_000;
						rollerBearingInterval = 60_000;
					}

					/*
					 * Increase lifetime for Valves if type is Turnable Disk
					 * i.e., all models 150 or 200 (refer to TB No. 4, 2007-06),
					 * or if TecBase indicates that the valve is of this type
					 */
					if (hasTurnableDisk == true || modelNumberEqmnt.contains("150")
							|| modelNumberEqmnt.contains("200")) {
						valveInterval = 2 * valveInterval;
						valve = "TD";
					}
					equipmentMap.put(key, new Homogeniser(piston, pistonInterval, pistonQty, pistonSeal,
							pistonSealInterval, pistonSealQty, compressionRing, compressionRingInterval,
							compressionRingQty, forcer, forcerInterval, forcerQty, impactRingInterval, impactRingQty,
							seatInterval, forcerSeatQty, valve, valveInterval, valveQty, valveSealingInterval,
							valveSealingQty, valveSeatInterval, valveSeatQty, plainBearingInterval, plainBearingQty,
							rollerBearingInterval, rollerBearingQty, bellowsInterval, bellowQty, pistonSeal));

				}
			}
			logger.info("Built {} Homogenisers", equipmentMap.size());
		} catch (ClientException e) {
			logger.error("Exception in 'buildHomogenisers()':" + e);
		}
		return equipmentMap;
	}

	/**
	 * Pull equipments of type Separators from the database and construct the
	 * functional areas
	 *
	 * @return map of Equipment numbers and Separator class fields
	 */
	private static Map<String, Separator> buildSeparators() {
		Map<String, Separator> equipmentMap = new HashMap<>();

		try (Session session = DRIVER.session()) {

			try (Transaction tx = session.beginTransaction()) {
				StatementResult result = tx
						.run("MATCH (e:Equipment) WHERE e.machSystem = {term1} OR e.machSystem = {term2}\n"
								+ "RETURN e.id AS ID ", Values.parameters("term1", "CENTRI", "term2", "SEPARATOR"));
				tx.success();
				while (result.hasNext()) {
					Record r = result.next();
					String key = r.get("ID").toString();

					// IM kit four changes per year or every 2000h, whatever
					// comes first
					int intermediateKitInterval = 2_000;
					// M kit one change per year or every 8000h, whatever comes
					// first
					int majorKitInterval = 8_000;
					// OWMC once a year
					int oWMCInterval = 8_000;
					int fFInterval = 24_000;

					equipmentMap.put(key,
							new Separator(intermediateKitInterval, majorKitInterval, oWMCInterval, fFInterval));
				}
			}
			logger.info("Built {} Separators", equipmentMap.size());
		} catch (ClientException e) {
			logger.error("Exception in 'buildSeparators()':" + e);
		}
		return equipmentMap;
	}

	/**
	 * Pull equipments of type Plate Heat Exchangers from the database and
	 * construct the functional areas
	 *
	 * @return map of Equipment numbers and Plate Heat Exchangers class fields
	 */
	private static Map<String, PlateHeatExchanger> buildPHEs() {

		Map<String, PlateHeatExchanger> equipmentMap = new HashMap<>();

		Map<String, PHEfamily> PHE_FAMILY_MAP = new HashMap<>();

		PHE_FAMILY_MAP.put("6-30360 0100 1", new PHEfamily("C"));
		PHE_FAMILY_MAP.put("6-30360 0101 2", new PHEfamily("C"));
		PHE_FAMILY_MAP.put("6-30360 0102 1", new PHEfamily("C"));
		PHE_FAMILY_MAP.put("6-30360 0103 1", new PHEfamily("C"));
		PHE_FAMILY_MAP.put("6-30360 0104 1", new PHEfamily("M"));
		PHE_FAMILY_MAP.put("6-30360 0105 1", new PHEfamily("M"));
		PHE_FAMILY_MAP.put("6-30360 0106 1", new PHEfamily("M"));
		PHE_FAMILY_MAP.put("6-30360 0107 1", new PHEfamily("M"));
		PHE_FAMILY_MAP.put("6-30360 0114 1", new PHEfamily("M"));
		PHE_FAMILY_MAP.put("6-30360 0115 1", new PHEfamily("C"));
		PHE_FAMILY_MAP.put("6-30360 0116 1", new PHEfamily("C"));
		PHE_FAMILY_MAP.put("6-30360 0117 1", new PHEfamily("C"));
		PHE_FAMILY_MAP.put("6-30360 0118 1", new PHEfamily("C"));
		PHE_FAMILY_MAP.put("6-30360 0119 1", new PHEfamily("C"));
		PHE_FAMILY_MAP.put("6-30360 0120 1", new PHEfamily("C"));
		PHE_FAMILY_MAP.put("6-30360 0121 1", new PHEfamily("C"));
		PHE_FAMILY_MAP.put("6-30360 0122 1", new PHEfamily("C"));
		PHE_FAMILY_MAP.put("6-30360 0123 1", new PHEfamily("H"));
		PHE_FAMILY_MAP.put("6-30360 0124 1", new PHEfamily("H"));
		PHE_FAMILY_MAP.put("6-30360 0125 1", new PHEfamily("M"));
		PHE_FAMILY_MAP.put("6-30360 0126 1", new PHEfamily("M"));
		PHE_FAMILY_MAP.put("6-30360 0127 1", new PHEfamily("M"));
		PHE_FAMILY_MAP.put("6-30360 0128 1", new PHEfamily("M"));
		PHE_FAMILY_MAP.put("6-30360 0129 1", new PHEfamily("M"));
		PHE_FAMILY_MAP.put("6-30360 0130 1", new PHEfamily("M"));
		PHE_FAMILY_MAP.put("6-30360 0131 1", new PHEfamily("M"));
		PHE_FAMILY_MAP.put("6-30360 0132 1", new PHEfamily("M"));
		PHE_FAMILY_MAP.put("6-30360 0133 1", new PHEfamily("M"));
		PHE_FAMILY_MAP.put("6-30360 0134 1", new PHEfamily("P"));
		PHE_FAMILY_MAP.put("6-30360 0135 1", new PHEfamily("P"));
		PHE_FAMILY_MAP.put("6-30360 0136 1", new PHEfamily("P"));
		PHE_FAMILY_MAP.put("6-30360 0137 1", new PHEfamily("P"));
		PHE_FAMILY_MAP.put("6-30360 0138 1", new PHEfamily("P"));
		PHE_FAMILY_MAP.put("6-30360 0139 1", new PHEfamily("M"));
		PHE_FAMILY_MAP.put("6-30360 0140 1", new PHEfamily("M"));
		PHE_FAMILY_MAP.put("6-30360 0143 1", new PHEfamily("M"));
		PHE_FAMILY_MAP.put("6-30360 0144 1", new PHEfamily("C"));

		try (Session session = DRIVER.session()) {

			try (Transaction tx = session.beginTransaction()) {
				StatementResult result = tx.run(
						"MATCH (e:Equipment) WHERE e.machSystem CONTAINS {term1} AND NOT e.machSystem CONTAINS {term2}\n"
								+ "RETURN e.id AS ID, CASE WHEN e.heatNoPlatesTubes IS NULL THEN 0 ELSE e.heatNoPlatesTubes END AS numberOfPlates, e.material AS material ",
						Values.parameters("term1", "PHE", "term2", "NTP"));
				tx.success();
				while (result.hasNext()) {
					Record r = result.next();

					String key = r.get("ID").toString();
					int numberOfPlates = r.get("numberOfPlates").asInt();
					String equipmentMtrlKey = r.get("material").asString();

					int numberOfPlatesC = 130;
					int numberOfPlatesM = 80;
					int numberOfPlatesH = 80;
					int numberOfPlatesP = 80;

					/*
					 * Family mapping on equipmentMtrlKey results in 80% correct
					 * mapping out of a total installed base of 25 500 PHEs
					 */
					if (PHE_FAMILY_MAP.containsKey(equipmentMtrlKey)) {
						switch (PHE_FAMILY_MAP.get(equipmentMtrlKey).getModel()) {
						case "C":
							if (numberOfPlates > 0) {
								numberOfPlatesC = numberOfPlates;
							}
							equipmentMap.put(key, new PlateHeatExchanger("C", numberOfPlatesC, 12_000, 28_000, 100));
							break;
						case "M":
							if (numberOfPlates > 0) {
								numberOfPlatesM = numberOfPlates;
							}
							equipmentMap.put(key, new PlateHeatExchanger("M", numberOfPlatesM, 12_000, 28_000, 100));
							break;
						case "H":
							if (numberOfPlates > 0) {
								numberOfPlatesH = numberOfPlates;
							}
							equipmentMap.put(key, new PlateHeatExchanger("H", numberOfPlatesH, 12_000, 28_000, 100));
							break;
						case "P":
							if (numberOfPlates > 0) {
								numberOfPlatesP = numberOfPlates;
							}
							equipmentMap.put(key, new PlateHeatExchanger("P", numberOfPlatesP, 12_000, 28_000, 100));
							break;
						default:
							break;
						}
						// If map doesn't contain a mapping then assign family
						// 'N/A'
					} else {
						equipmentMap.put(key, new PlateHeatExchanger("N/A", 80, 12_000, 28_000, 100));
					}
				}
			}
			logger.info("Built {} Plate Heat Exchangers", equipmentMap.size());
		} catch (ClientException e) {
			logger.error("Exception in 'buildPHEs()':" + e);
		}
		return equipmentMap;
	}

	/**
	 * Pull equipments of type Tubular Heat Exchangers from the database and
	 * construct the functional areas
	 *
	 * @return map of Equipment numbers and Tubular Heat Exchangers class fields
	 */
	private static Map<String, TubularHeatExchanger> buildTHEs() {

		Map<String, TubularHeatExchanger> equipmentMap = new HashMap<>();

		Map<String, THEfamily> THE_FAMILY_MAP = new HashMap<>();

		THE_FAMILY_MAP.put("2565404-0100", new THEfamily("MT", "25"));
		THE_FAMILY_MAP.put("2565405-0100", new THEfamily("MT", "38"));
		THE_FAMILY_MAP.put("2565406-0100", new THEfamily("MT", "57"));
		THE_FAMILY_MAP.put("2565407-0100", new THEfamily("MT", "70"));
		THE_FAMILY_MAP.put("2565408-0100", new THEfamily("MT", "85"));
		THE_FAMILY_MAP.put("2565409-0100", new THEfamily("MT", "108"));
		THE_FAMILY_MAP.put("2565410-0100", new THEfamily("MT", "129"));
		THE_FAMILY_MAP.put("2565411-0100", new THEfamily("MT", "154"));
		THE_FAMILY_MAP.put("2565412-0100", new THEfamily("CIP", "TBD"));
		THE_FAMILY_MAP.put("2565413-0100", new THEfamily("CIP", "TBD"));
		THE_FAMILY_MAP.put("2565414-0100", new THEfamily("CIP", "TBD"));
		THE_FAMILY_MAP.put("2565415-0100", new THEfamily("CIP", "TBD"));
		THE_FAMILY_MAP.put("2565420-0100", new THEfamily("MT", "70"));
		THE_FAMILY_MAP.put("2565421-0100", new THEfamily("MT", "108"));
		THE_FAMILY_MAP.put("2565422-0100", new THEfamily("MT", "57"));
		THE_FAMILY_MAP.put("2565423-0100", new THEfamily("MT", "70"));
		THE_FAMILY_MAP.put("2579720-0100", new THEfamily("MT", "85"));
		THE_FAMILY_MAP.put("2579721-0100", new THEfamily("MT", "108"));
		THE_FAMILY_MAP.put("2579723-0100", new THEfamily("MT", "154"));
		THE_FAMILY_MAP.put("2594494-0100", new THEfamily("CIP", "TBD"));
		THE_FAMILY_MAP.put("2594495-0100", new THEfamily("CIP", "TBD"));
		THE_FAMILY_MAP.put("2594497-0100", new THEfamily("MT", "TBD"));
		THE_FAMILY_MAP.put("2618560-0100", new THEfamily("MT", "TBD"));
		THE_FAMILY_MAP.put("2827570-0100", new THEfamily("C", "70"));
		THE_FAMILY_MAP.put("2827571-0100", new THEfamily("C", "85"));
		THE_FAMILY_MAP.put("2827571-0100", new THEfamily("C", "85"));
		THE_FAMILY_MAP.put("2827572-0100", new THEfamily("C", "108"));
		THE_FAMILY_MAP.put("2827573-0100", new THEfamily("C", "129"));
		THE_FAMILY_MAP.put("2827574-0100", new THEfamily("C", "154"));
		THE_FAMILY_MAP.put("2827575-0100", new THEfamily("C", "70"));
		THE_FAMILY_MAP.put("2827576-0100", new THEfamily("C", "85"));
		THE_FAMILY_MAP.put("2827577-0100", new THEfamily("C", "108"));
		THE_FAMILY_MAP.put("2827578-0100", new THEfamily("C", "129"));
		THE_FAMILY_MAP.put("2827579-0100", new THEfamily("C", "154"));
		THE_FAMILY_MAP.put("2827580-0100", new THEfamily("C", "TBD"));
		THE_FAMILY_MAP.put("2827581-0100", new THEfamily("C", "70"));
		THE_FAMILY_MAP.put("2827582-0100", new THEfamily("C", "108"));
		THE_FAMILY_MAP.put("2827591-0100", new THEfamily("C", "25"));
		THE_FAMILY_MAP.put("2827592-0100", new THEfamily("C", "29"));
		THE_FAMILY_MAP.put("2827593-0100", new THEfamily("C", "38"));

		try (Session session = DRIVER.session()) {

			try (Transaction tx = session.beginTransaction()) {
				StatementResult result = tx.run(
						"MATCH (e:Equipment) WHERE e.machSystem = {term1} OR e.machSystem = {term2}\n"
								+ "RETURN e.id AS ID, "
								+ "CASE WHEN e.heatNoPlatesTubes IS NULL THEN 0 ELSE e.heatNoPlatesTubes END AS numberOfTubes,"
								+ "CASE WHEN e.heatPercRegen IS NULL THEN 0 ELSE e.heatPercRegen END AS percentageRegeneration,"
								+ " e.material AS material ",
						Values.parameters("term1", "SPIRAFLO", "term2", "THEOLD"));
				tx.success();
				while (result.hasNext()) {
					Record r = result.next();

					String key = r.get("ID").toString();
					int numberOfTubes = r.get("numberOfTubes").asInt();
					int percentageRegeneration = r.get("percentageRegeneration").asInt();
					String equipmentMtrlKey = r.get("material").asString();

					int nonRegTubes = 12;
					int regTubes = 1;
					int holdingTubes = 3;
					if (numberOfTubes > 0) {
						if (numberOfTubes <= 3) {
							holdingTubes = 0;
						}
						regTubes = percentageRegeneration * numberOfTubes / 100;
						nonRegTubes = numberOfTubes - regTubes - holdingTubes;
						if (nonRegTubes < 0) {
							nonRegTubes = 0;
						}
					}

					/*
					 * Family mapping on equipmentMtrlKey results in 95% correct
					 * mapping out of a total installed base of 6 500 THEs
					 */
					if (THE_FAMILY_MAP.containsKey(equipmentMtrlKey)) {
						switch (THE_FAMILY_MAP.get(equipmentMtrlKey).getModel()) {
						case "C":
							equipmentMap.put(key,
									new TubularHeatExchanger("C", nonRegTubes, regTubes, holdingTubes, 6_000, 6_000));
							break;
						case "MT":
							equipmentMap.put(key,
									new TubularHeatExchanger("MT", nonRegTubes, regTubes, holdingTubes, 6_000, 6_000));
							break;
						case "CIP":
							equipmentMap.put(key, new TubularHeatExchanger("CIP", 1, 0, 0, 6_000, 6_000));
							break;
						default:
							break;
						}
						// If map doesn't contain a mapping then assign family
						// 'N/A'
					} else {
						equipmentMap.put(key, new TubularHeatExchanger("N/A", 1, 0, 0, 6_000, 6_000));
					}
				}
			}
			logger.info("Built {} Tubular Heat Exchangers", equipmentMap.size());
		} catch (ClientException e) {
			logger.error("Exception in 'buildTHEs()':" + e);
		}
		return equipmentMap;
	}

	/**
	 * Pull equipments of type Frigus Freezers from the database and construct
	 * the functional areas.
	 *
	 * It first attempts mapping on Material Keys, and if there is no match, it
	 * search modelNumberEqmnt by Regex on descending numbers.
	 *
	 * @return map of Equipment numbers and Frigus Freezer class fields
	 */
	private static Map<String, FrigusFreezer> buildFrigusFreezers() {

		Map<String, FrigusFreezer> equipmentMap = new HashMap<>();

		Map<String, FrigusFreezerFamily> FRIGUS_FREEZER_FAMILY_MAP = new HashMap<>();

		FRIGUS_FREEZER_FAMILY_MAP.put("3094309-0100", new FrigusFreezerFamily("300"));
		FRIGUS_FREEZER_FAMILY_MAP.put("3094310-0100", new FrigusFreezerFamily("600"));
		FRIGUS_FREEZER_FAMILY_MAP.put("3094311-0100", new FrigusFreezerFamily("1200"));
		FRIGUS_FREEZER_FAMILY_MAP.put("3137675-0100", new FrigusFreezerFamily("500"));
		FRIGUS_FREEZER_FAMILY_MAP.put("3137676-0100", new FrigusFreezerFamily("1000"));
		FRIGUS_FREEZER_FAMILY_MAP.put("3137677-0100", new FrigusFreezerFamily("2000"));
		FRIGUS_FREEZER_FAMILY_MAP.put("3137678-0100", new FrigusFreezerFamily("3000"));
		FRIGUS_FREEZER_FAMILY_MAP.put("3137679-0100", new FrigusFreezerFamily("4000"));
		FRIGUS_FREEZER_FAMILY_MAP.put("3137715-0100", new FrigusFreezerFamily("700"));

		try (Session session = DRIVER.session()) {

			try (Transaction tx = session.beginTransaction()) {
				StatementResult result = tx.run(
						"MATCH (e:Equipment) WHERE e.machSystem = {term1} \n"
								+ "RETURN e.id AS ID, e.material AS material, "
								+ "CASE WHEN e.model IS NULL THEN 'N/A' ELSE e.model END AS model",
						Values.parameters("term1", "ICFREEZE"));
				tx.success();
				while (result.hasNext()) {
					Record r = result.next();

					String key = r.get("ID").toString();
					String equipmentMtrlKey = r.get("material").asString();
					String modelNumberEqmnt = r.get("model").asString();

					// Set default values
					String modelType;

					// Intervals
					// Service Kits
					int kit1000Interval = 1_500;
					int kit3000Interval = 6_000;
					int kit6000Interval = 12_000;
					int kit12000Interval = 12_000;
					// Pump
					int newPumpInterval = 30_000;
					int exchangePumpInterval = 30_000;
					// Pump spare parts
					int kitExtendedInterval = 6_000;
					int starWheelInterval = 6_000;
					int coverInterval = 6_000;
					int impellerInterval = 6_000;
					int gasketKitInterval = 1_000;
					int bushingInterval = 1_000;
					// Scraper
					int scraperKnifeInterval = 3_000;
					// Cooling
					int sparePartsInterval = 6_000;
					// Shaft
					int shaftSealInterval = 6_000;
					// Cylinder
					int cylinderKitInterval = 6_000;
					int gasketsInterval = 6_000;

					// Dasher
					int dasherKitInterval = 6_000;
					int singlePartsInterval = 3_000;

					// Quantities
					// Service Kits
					int kit1000Qty = 1;
					int kit3000Qty = 1;
					int kit6000Qty = 1;
					int kit12000Qty = 1;
					// Pump
					int newPumpQty = 2;
					int exchangePumpQty = 2;
					// Pump spare parts
					int kitExtendedQty = 2;
					int starWheelQty = 4;
					int coverQty = 4;
					int impellerQty = 4;
					int gasketKitQty = 2;
					int bushingQty = 4;
					// Scraper
					int scraperKnifeQty = 2;
					// Cooling
					int sparePartsQty = 5;
					// Shaft
					int shaftSealQty = 1;
					// Cylinder
					int cylinderKitQty = 1;
					int gasketQty = 4;

					// Dasher
					int dasherKitQty = 1;
					int singlePartQty = 5;

					/*
					 * Family mapping on equipmentMtrlKey results in 9% correct
					 * mapping out of a total installed base of 6000 Freezers
					 */
					if (FRIGUS_FREEZER_FAMILY_MAP.containsKey(equipmentMtrlKey)) {
						switch (FRIGUS_FREEZER_FAMILY_MAP.get(equipmentMtrlKey).model) {
						case "300":

							modelType = "300";
							// Update quantities
							// Pump
							newPumpQty = 1;
							exchangePumpQty = 1;
							// Pump spare parts
							bushingQty = 1;
							// Scraper
							scraperKnifeQty = 1;

							equipmentMap.put(key, new FrigusFreezer(modelType, kit1000Interval, kit1000Qty,
									kit3000Interval, kit3000Qty, kit6000Interval, kit6000Qty, kit12000Interval,
									kit12000Qty, newPumpInterval, newPumpQty, exchangePumpInterval, exchangePumpQty,
									kitExtendedInterval, kitExtendedQty, starWheelInterval, starWheelQty, coverInterval,
									coverQty, impellerInterval, impellerQty, gasketKitInterval, gasketKitQty,
									bushingInterval, bushingQty, scraperKnifeInterval, scraperKnifeQty,
									sparePartsInterval, sparePartsQty, shaftSealInterval, shaftSealQty,
									cylinderKitInterval, cylinderKitQty, gasketsInterval, gasketQty, dasherKitInterval,
									dasherKitQty, singlePartsInterval, singlePartQty));

							break;
						case "500":

							modelType = "500";

							equipmentMap.put(key, new FrigusFreezer(modelType, kit1000Interval, kit1000Qty,
									kit3000Interval, kit3000Qty, kit6000Interval, kit6000Qty, kit12000Interval,
									kit12000Qty, newPumpInterval, newPumpQty, exchangePumpInterval, exchangePumpQty,
									kitExtendedInterval, kitExtendedQty, starWheelInterval, starWheelQty, coverInterval,
									coverQty, impellerInterval, impellerQty, gasketKitInterval, gasketKitQty,
									bushingInterval, bushingQty, scraperKnifeInterval, scraperKnifeQty,
									sparePartsInterval, sparePartsQty, shaftSealInterval, shaftSealQty,
									cylinderKitInterval, cylinderKitQty, gasketsInterval, gasketQty, dasherKitInterval,
									dasherKitQty, singlePartsInterval, singlePartQty));
							break;
						case "600":

							modelType = "600";
							// Update quantities
							// Pump
							newPumpQty = 1;
							exchangePumpQty = 1;
							// Pump spare parts
							kitExtendedQty = 1;
							starWheelQty = 2;
							coverQty = 2;
							impellerQty = 2;
							gasketKitQty = 1;
							bushingQty = 2;
							// Scraper
							scraperKnifeQty = 3;

							equipmentMap.put(key, new FrigusFreezer(modelType, kit1000Interval, kit1000Qty,
									kit3000Interval, kit3000Qty, kit6000Interval, kit6000Qty, kit12000Interval,
									kit12000Qty, newPumpInterval, newPumpQty, exchangePumpInterval, exchangePumpQty,
									kitExtendedInterval, kitExtendedQty, starWheelInterval, starWheelQty, coverInterval,
									coverQty, impellerInterval, impellerQty, gasketKitInterval, gasketKitQty,
									bushingInterval, bushingQty, scraperKnifeInterval, scraperKnifeQty,
									sparePartsInterval, sparePartsQty, shaftSealInterval, shaftSealQty,
									cylinderKitInterval, cylinderKitQty, gasketsInterval, gasketQty, dasherKitInterval,
									dasherKitQty, singlePartsInterval, singlePartQty));
							break;
						case "700":

							modelType = "700";

							equipmentMap.put(key, new FrigusFreezer(modelType, kit1000Interval, kit1000Qty,
									kit3000Interval, kit3000Qty, kit6000Interval, kit6000Qty, kit12000Interval,
									kit12000Qty, newPumpInterval, newPumpQty, exchangePumpInterval, exchangePumpQty,
									kitExtendedInterval, kitExtendedQty, starWheelInterval, starWheelQty, coverInterval,
									coverQty, impellerInterval, impellerQty, gasketKitInterval, gasketKitQty,
									bushingInterval, bushingQty, scraperKnifeInterval, scraperKnifeQty,
									sparePartsInterval, sparePartsQty, shaftSealInterval, shaftSealQty,
									cylinderKitInterval, cylinderKitQty, gasketsInterval, gasketQty, dasherKitInterval,
									dasherKitQty, singlePartsInterval, singlePartQty));
							break;
						case "1000":

							modelType = "1000";

							equipmentMap.put(key, new FrigusFreezer(modelType, kit1000Interval, kit1000Qty,
									kit3000Interval, kit3000Qty, kit6000Interval, kit6000Qty, kit12000Interval,
									kit12000Qty, newPumpInterval, newPumpQty, exchangePumpInterval, exchangePumpQty,
									kitExtendedInterval, kitExtendedQty, starWheelInterval, starWheelQty, coverInterval,
									coverQty, impellerInterval, impellerQty, gasketKitInterval, gasketKitQty,
									bushingInterval, bushingQty, scraperKnifeInterval, scraperKnifeQty,
									sparePartsInterval, sparePartsQty, shaftSealInterval, shaftSealQty,
									cylinderKitInterval, cylinderKitQty, gasketsInterval, gasketQty, dasherKitInterval,
									dasherKitQty, singlePartsInterval, singlePartQty));
							break;
						case "1200":

							modelType = "1200";
							// Update quantities
							// Service Kits
							kit1000Qty = 0;
							kit12000Qty = 0;

							equipmentMap.put(key, new FrigusFreezer(modelType, kit1000Interval, kit1000Qty,
									kit3000Interval, kit3000Qty, kit6000Interval, kit6000Qty, kit12000Interval,
									kit12000Qty, newPumpInterval, newPumpQty, exchangePumpInterval, exchangePumpQty,
									kitExtendedInterval, kitExtendedQty, starWheelInterval, starWheelQty, coverInterval,
									coverQty, impellerInterval, impellerQty, gasketKitInterval, gasketKitQty,
									bushingInterval, bushingQty, scraperKnifeInterval, scraperKnifeQty,
									sparePartsInterval, sparePartsQty, shaftSealInterval, shaftSealQty,
									cylinderKitInterval, cylinderKitQty, gasketsInterval, gasketQty, dasherKitInterval,
									dasherKitQty, singlePartsInterval, singlePartQty));
							break;
						case "2000":

							modelType = "2000";
							// Update quantities
							// Scraper
							scraperKnifeQty = 4;

							equipmentMap.put(key, new FrigusFreezer(modelType, kit1000Interval, kit1000Qty,
									kit3000Interval, kit3000Qty, kit6000Interval, kit6000Qty, kit12000Interval,
									kit12000Qty, newPumpInterval, newPumpQty, exchangePumpInterval, exchangePumpQty,
									kitExtendedInterval, kitExtendedQty, starWheelInterval, starWheelQty, coverInterval,
									coverQty, impellerInterval, impellerQty, gasketKitInterval, gasketKitQty,
									bushingInterval, bushingQty, scraperKnifeInterval, scraperKnifeQty,
									sparePartsInterval, sparePartsQty, shaftSealInterval, shaftSealQty,
									cylinderKitInterval, cylinderKitQty, gasketsInterval, gasketQty, dasherKitInterval,
									dasherKitQty, singlePartsInterval, singlePartQty));
							break;
						case "3000":

							modelType = "3000";
							// Update quantities
							// Scraper
							scraperKnifeQty = 8;

							equipmentMap.put(key, new FrigusFreezer(modelType, kit1000Interval, kit1000Qty,
									kit3000Interval, kit3000Qty, kit6000Interval, kit6000Qty, kit12000Interval,
									kit12000Qty, newPumpInterval, newPumpQty, exchangePumpInterval, exchangePumpQty,
									kitExtendedInterval, kitExtendedQty, starWheelInterval, starWheelQty, coverInterval,
									coverQty, impellerInterval, impellerQty, gasketKitInterval, gasketKitQty,
									bushingInterval, bushingQty, scraperKnifeInterval, scraperKnifeQty,
									sparePartsInterval, sparePartsQty, shaftSealInterval, shaftSealQty,
									cylinderKitInterval, cylinderKitQty, gasketsInterval, gasketQty, dasherKitInterval,
									dasherKitQty, singlePartsInterval, singlePartQty));
							break;
						case "4000":

							modelType = "4000";
							// Update quantities
							// Scraper
							scraperKnifeQty = 10;

							equipmentMap.put(key, new FrigusFreezer(modelType, kit1000Interval, kit1000Qty,
									kit3000Interval, kit3000Qty, kit6000Interval, kit6000Qty, kit12000Interval,
									kit12000Qty, newPumpInterval, newPumpQty, exchangePumpInterval, exchangePumpQty,
									kitExtendedInterval, kitExtendedQty, starWheelInterval, starWheelQty, coverInterval,
									coverQty, impellerInterval, impellerQty, gasketKitInterval, gasketKitQty,
									bushingInterval, bushingQty, scraperKnifeInterval, scraperKnifeQty,
									sparePartsInterval, sparePartsQty, shaftSealInterval, shaftSealQty,
									cylinderKitInterval, cylinderKitQty, gasketsInterval, gasketQty, dasherKitInterval,
									dasherKitQty, singlePartsInterval, singlePartQty));
							break;
						default:
							break;
						}
						/*
						 * If map doesn't contain a mapping then search
						 * modelNumberEqmnt by Regex on descending numbers. This
						 * results in 90% correct mapping of the Frigus Freezers
						 * in the total installed base of 6000 Freezers
						 */
					} else if (modelNumberEqmnt.contains("CK") || modelNumberEqmnt.contains("GM")
							|| modelNumberEqmnt.contains("BLUE") || modelNumberEqmnt.contains("MARK")) {
						/*
						 * Filter to catch non-applicable models, do nothing
						 * (exclude equipments from the Frigus Freezer mapping)
						 */
						//
					} else if (modelNumberEqmnt.contains("4000")) {

						modelType = "4000";
						// Update quantities
						// Scraper
						scraperKnifeQty = 10;

						equipmentMap.put(key, new FrigusFreezer(modelType, kit1000Interval, kit1000Qty, kit3000Interval,
								kit3000Qty, kit6000Interval, kit6000Qty, kit12000Interval, kit12000Qty, newPumpInterval,
								newPumpQty, exchangePumpInterval, exchangePumpQty, kitExtendedInterval, kitExtendedQty,
								starWheelInterval, starWheelQty, coverInterval, coverQty, impellerInterval, impellerQty,
								gasketKitInterval, gasketKitQty, bushingInterval, bushingQty, scraperKnifeInterval,
								scraperKnifeQty, sparePartsInterval, sparePartsQty, shaftSealInterval, shaftSealQty,
								cylinderKitInterval, cylinderKitQty, gasketsInterval, gasketQty, dasherKitInterval,
								dasherKitQty, singlePartsInterval, singlePartQty));
					} else if (modelNumberEqmnt.contains("3000")) {

						modelType = "3000";
						// Update quantities
						// Scraper
						scraperKnifeQty = 8;

						equipmentMap.put(key, new FrigusFreezer(modelType, kit1000Interval, kit1000Qty, kit3000Interval,
								kit3000Qty, kit6000Interval, kit6000Qty, kit12000Interval, kit12000Qty, newPumpInterval,
								newPumpQty, exchangePumpInterval, exchangePumpQty, kitExtendedInterval, kitExtendedQty,
								starWheelInterval, starWheelQty, coverInterval, coverQty, impellerInterval, impellerQty,
								gasketKitInterval, gasketKitQty, bushingInterval, bushingQty, scraperKnifeInterval,
								scraperKnifeQty, sparePartsInterval, sparePartsQty, shaftSealInterval, shaftSealQty,
								cylinderKitInterval, cylinderKitQty, gasketsInterval, gasketQty, dasherKitInterval,
								dasherKitQty, singlePartsInterval, singlePartQty));
					} else if (modelNumberEqmnt.matches("^.{0,10}2000.*")) {

						modelType = "2000";
						// Update quantities
						// Scraper
						scraperKnifeQty = 4;

						equipmentMap.put(key, new FrigusFreezer(modelType, kit1000Interval, kit1000Qty, kit3000Interval,
								kit3000Qty, kit6000Interval, kit6000Qty, kit12000Interval, kit12000Qty, newPumpInterval,
								newPumpQty, exchangePumpInterval, exchangePumpQty, kitExtendedInterval, kitExtendedQty,
								starWheelInterval, starWheelQty, coverInterval, coverQty, impellerInterval, impellerQty,
								gasketKitInterval, gasketKitQty, bushingInterval, bushingQty, scraperKnifeInterval,
								scraperKnifeQty, sparePartsInterval, sparePartsQty, shaftSealInterval, shaftSealQty,
								cylinderKitInterval, cylinderKitQty, gasketsInterval, gasketQty, dasherKitInterval,
								dasherKitQty, singlePartsInterval, singlePartQty));
					} else if (modelNumberEqmnt.contains("1500")) {

						modelType = "1500";
						// Update quantities
						// Pump
						newPumpQty = 1;
						exchangePumpQty = 1;
						// Pump spare parts
						bushingQty = 1;
						// Scraper
						scraperKnifeQty = 1;

						equipmentMap.put(key, new FrigusFreezer(modelType, kit1000Interval, kit1000Qty, kit3000Interval,
								kit3000Qty, kit6000Interval, kit6000Qty, kit12000Interval, kit12000Qty, newPumpInterval,
								newPumpQty, exchangePumpInterval, exchangePumpQty, kitExtendedInterval, kitExtendedQty,
								starWheelInterval, starWheelQty, coverInterval, coverQty, impellerInterval, impellerQty,
								gasketKitInterval, gasketKitQty, bushingInterval, bushingQty, scraperKnifeInterval,
								scraperKnifeQty, sparePartsInterval, sparePartsQty, shaftSealInterval, shaftSealQty,
								cylinderKitInterval, cylinderKitQty, gasketsInterval, gasketQty, dasherKitInterval,
								dasherKitQty, singlePartsInterval, singlePartQty));
					} else if (modelNumberEqmnt.contains("1200")) {

						modelType = "1200";
						// Update quantities
						// Service Kits
						kit1000Qty = 0;
						kit12000Qty = 0;

						equipmentMap.put(key, new FrigusFreezer(modelType, kit1000Interval, kit1000Qty, kit3000Interval,
								kit3000Qty, kit6000Interval, kit6000Qty, kit12000Interval, kit12000Qty, newPumpInterval,
								newPumpQty, exchangePumpInterval, exchangePumpQty, kitExtendedInterval, kitExtendedQty,
								starWheelInterval, starWheelQty, coverInterval, coverQty, impellerInterval, impellerQty,
								gasketKitInterval, gasketKitQty, bushingInterval, bushingQty, scraperKnifeInterval,
								scraperKnifeQty, sparePartsInterval, sparePartsQty, shaftSealInterval, shaftSealQty,
								cylinderKitInterval, cylinderKitQty, gasketsInterval, gasketQty, dasherKitInterval,
								dasherKitQty, singlePartsInterval, singlePartQty));
					} else if (modelNumberEqmnt.contains("1000")) {

						modelType = "1000";

						equipmentMap.put(key, new FrigusFreezer(modelType, kit1000Interval, kit1000Qty, kit3000Interval,
								kit3000Qty, kit6000Interval, kit6000Qty, kit12000Interval, kit12000Qty, newPumpInterval,
								newPumpQty, exchangePumpInterval, exchangePumpQty, kitExtendedInterval, kitExtendedQty,
								starWheelInterval, starWheelQty, coverInterval, coverQty, impellerInterval, impellerQty,
								gasketKitInterval, gasketKitQty, bushingInterval, bushingQty, scraperKnifeInterval,
								scraperKnifeQty, sparePartsInterval, sparePartsQty, shaftSealInterval, shaftSealQty,
								cylinderKitInterval, cylinderKitQty, gasketsInterval, gasketQty, dasherKitInterval,
								dasherKitQty, singlePartsInterval, singlePartQty));
					} else if (modelNumberEqmnt.contains("700")) {

						modelType = "700";
						// Update quantities
						// Pump
						newPumpQty = 1;
						exchangePumpQty = 1;
						// Pump spare parts
						bushingQty = 1;
						// Scraper
						scraperKnifeQty = 1;

						equipmentMap.put(key, new FrigusFreezer(modelType, kit1000Interval, kit1000Qty, kit3000Interval,
								kit3000Qty, kit6000Interval, kit6000Qty, kit12000Interval, kit12000Qty, newPumpInterval,
								newPumpQty, exchangePumpInterval, exchangePumpQty, kitExtendedInterval, kitExtendedQty,
								starWheelInterval, starWheelQty, coverInterval, coverQty, impellerInterval, impellerQty,
								gasketKitInterval, gasketKitQty, bushingInterval, bushingQty, scraperKnifeInterval,
								scraperKnifeQty, sparePartsInterval, sparePartsQty, shaftSealInterval, shaftSealQty,
								cylinderKitInterval, cylinderKitQty, gasketsInterval, gasketQty, dasherKitInterval,
								dasherKitQty, singlePartsInterval, singlePartQty));
					} else if (modelNumberEqmnt.contains("600")) {

						modelType = "600";
						// Update quantities
						// Pump
						newPumpQty = 1;
						exchangePumpQty = 1;
						// Pump spare parts
						kitExtendedQty = 1;
						starWheelQty = 2;
						coverQty = 2;
						impellerQty = 2;
						gasketKitQty = 1;
						bushingQty = 2;
						// Scraper
						scraperKnifeQty = 3;

						equipmentMap.put(key, new FrigusFreezer(modelType, kit1000Interval, kit1000Qty, kit3000Interval,
								kit3000Qty, kit6000Interval, kit6000Qty, kit12000Interval, kit12000Qty, newPumpInterval,
								newPumpQty, exchangePumpInterval, exchangePumpQty, kitExtendedInterval, kitExtendedQty,
								starWheelInterval, starWheelQty, coverInterval, coverQty, impellerInterval, impellerQty,
								gasketKitInterval, gasketKitQty, bushingInterval, bushingQty, scraperKnifeInterval,
								scraperKnifeQty, sparePartsInterval, sparePartsQty, shaftSealInterval, shaftSealQty,
								cylinderKitInterval, cylinderKitQty, gasketsInterval, gasketQty, dasherKitInterval,
								dasherKitQty, singlePartsInterval, singlePartQty));
					} else if (modelNumberEqmnt.contains("500")) {

						modelType = "500";

						equipmentMap.put(key, new FrigusFreezer(modelType, kit1000Interval, kit1000Qty, kit3000Interval,
								kit3000Qty, kit6000Interval, kit6000Qty, kit12000Interval, kit12000Qty, newPumpInterval,
								newPumpQty, exchangePumpInterval, exchangePumpQty, kitExtendedInterval, kitExtendedQty,
								starWheelInterval, starWheelQty, coverInterval, coverQty, impellerInterval, impellerQty,
								gasketKitInterval, gasketKitQty, bushingInterval, bushingQty, scraperKnifeInterval,
								scraperKnifeQty, sparePartsInterval, sparePartsQty, shaftSealInterval, shaftSealQty,
								cylinderKitInterval, cylinderKitQty, gasketsInterval, gasketQty, dasherKitInterval,
								dasherKitQty, singlePartsInterval, singlePartQty));
					} else if (modelNumberEqmnt.contains("300")) {

						modelType = "300";
						// Update quantities
						// Pump
						newPumpQty = 1;
						exchangePumpQty = 1;
						// Pump spare parts
						bushingQty = 1;
						// Scraper
						scraperKnifeQty = 1;

						equipmentMap.put(key, new FrigusFreezer(modelType, kit1000Interval, kit1000Qty, kit3000Interval,
								kit3000Qty, kit6000Interval, kit6000Qty, kit12000Interval, kit12000Qty, newPumpInterval,
								newPumpQty, exchangePumpInterval, exchangePumpQty, kitExtendedInterval, kitExtendedQty,
								starWheelInterval, starWheelQty, coverInterval, coverQty, impellerInterval, impellerQty,
								gasketKitInterval, gasketKitQty, bushingInterval, bushingQty, scraperKnifeInterval,
								scraperKnifeQty, sparePartsInterval, sparePartsQty, shaftSealInterval, shaftSealQty,
								cylinderKitInterval, cylinderKitQty, gasketsInterval, gasketQty, dasherKitInterval,
								dasherKitQty, singlePartsInterval, singlePartQty));
					} else if (modelNumberEqmnt.contains("KF") || modelNumberEqmnt.contains("FRIGUS")
							|| modelNumberEqmnt.contains("FREEZ")) {
						// Create "generic" instance of most frequent model (KF
						// 1000)

						modelType = "1000";

						equipmentMap.put(key, new FrigusFreezer(modelType, kit1000Interval, kit1000Qty, kit3000Interval,
								kit3000Qty, kit6000Interval, kit6000Qty, kit12000Interval, kit12000Qty, newPumpInterval,
								newPumpQty, exchangePumpInterval, exchangePumpQty, kitExtendedInterval, kitExtendedQty,
								starWheelInterval, starWheelQty, coverInterval, coverQty, impellerInterval, impellerQty,
								gasketKitInterval, gasketKitQty, bushingInterval, bushingQty, scraperKnifeInterval,
								scraperKnifeQty, sparePartsInterval, sparePartsQty, shaftSealInterval, shaftSealQty,
								cylinderKitInterval, cylinderKitQty, gasketsInterval, gasketQty, dasherKitInterval,
								dasherKitQty, singlePartsInterval, singlePartQty));
					} else {
						/*
						 * Do nothing (exclude equipments from the Frigus
						 * Freezer mapping)
						 */
					}

				}
			}
			logger.info("Built {} Frigus Freezers", equipmentMap.size());
		} catch (ClientException e) {
			logger.error("Exception in 'buildFrigusFreezers()':" + e);
		}
		return equipmentMap;
	}

	/**
	 * Pull equipments of type Ingredient Dosers from the database and construct
	 * the functional areas.
	 *
	 * Mapping to models is done on Material Keys.
	 * 
	 * It first attempts mapping on Material Keys, and if there is no match,
	 * then search for old type of Dosers, i.e., Equipment material equals
	 * '2623489-0100', and do a secondary search on modelNumberEqmnt by Regex on
	 * descending numbers.
	 *
	 * @return map of Equipment numbers and Ingredient Doser class fields
	 */
	private static Map<String, IngredientDoser> buildIngredientDosers() {

		Map<String, IngredientDoser> equipmentMap = new HashMap<>();

		Map<String, IngredientDoserFamily> INGREDIENT_DOSER_FAMILY_MAP = new HashMap<>();

		INGREDIENT_DOSER_FAMILY_MAP.put("3137680-0200", new IngredientDoserFamily("FF2000"));
		INGREDIENT_DOSER_FAMILY_MAP.put("3094314-0100", new IngredientDoserFamily("FF2000"));
		INGREDIENT_DOSER_FAMILY_MAP.put("3137682-0200", new IngredientDoserFamily("FF2000"));
		INGREDIENT_DOSER_FAMILY_MAP.put("3137681-0100", new IngredientDoserFamily("FF4000"));
		INGREDIENT_DOSER_FAMILY_MAP.put("3137681-0200", new IngredientDoserFamily("ID4000"));
		INGREDIENT_DOSER_FAMILY_MAP.put("3137683-0100", new IngredientDoserFamily("ID4000"));

		try (Session session = DRIVER.session()) {
			try (Transaction tx = session.beginTransaction()) {
				StatementResult result = tx.run(
						"MATCH (e:Equipment) WHERE e.machSystem = {term1} \n"
								+ "RETURN e.id AS ID, e.material AS material, "
								+ "CASE WHEN e.model IS NULL THEN 'N/A' ELSE e.model END AS model",
						Values.parameters("term1", "ICINCLUS"));
				tx.success();

				while (result.hasNext()) {
					Record r = result.next();

					String key = r.get("ID").toString();
					String equipmentMtrlKey = r.get("material").asString();
					String modelNumberEqmnt = r.get("model").asString();

					// Set default values
					String modelType;

					// Intervals
					// Service Kits (Each higher interval kit contains the
					// shorter interval kit contents.)
					int kit3000Interval = 6_000;
					int kit6000Interval = 12_000;
					int kit12000Interval = 12_000;
					// Hopper
					int hopperKit3000Interval = 6_000;
					int hopperKit6000Interval = 12_000;
					int hopperKit12000Interval = 12_000;
					// Mixer
					int mixerKit3000Interval = 6_000;
					int mixerKit6000Interval = 12_000;
					int mixerKit12000Interval = 12_000;
					// Pump
					int pumpKit3000Interval = 6_000;
					int pumpKit6000Interval = 12_000;
					int pumpKit12000Interval = 12_000;
					// Cover
					int coverKit3000Interval = 6_000;
					// Agitator
					int agitatorKit6000Interval = 12_000;
					int agitatorKit12000Interval = 12_000;

					// Lamella
					int lamellaInterval = 4_000;
					// Dasher
					int dasherInterval = 12_000;
					// PumpHouse
					int pumpHouseInterval = 26_000;

					// Quantities
					// Service Kits
					int kit3000Qty = 1;
					int kit6000Qty = 1;
					int kit12000Qty = 1;
					// Hopper
					int hopperKit3000Qty = 1;
					int hopperKit6000Qty = 1;
					int hopperKit12000Qty = 1;
					// Mixer
					int mixerKit3000Qty = 1;
					int mixerKit6000Qty = 1;
					int mixerKit12000Qty = 1;
					// Pump
					int pumpKit3000Qty = 1;
					int pumpKit6000Qty = 1;
					int pumpKit12000Qty = 1;
					// Cover
					int coverKit3000Qty = 1;
					// Agitator
					int agitatorKit6000Qty = 1;
					int agitatorKit12000Qty = 1;

					// Lamella
					int lamellaQty = 1;
					// Dasher
					int dasherQty = 1;
					// PumpHouse
					int pumpHouseQty = 1;

					if (INGREDIENT_DOSER_FAMILY_MAP.containsKey(equipmentMtrlKey)) {
						switch (INGREDIENT_DOSER_FAMILY_MAP.get(equipmentMtrlKey).model) {
						case "FF2000":

							modelType = "FF2000";
							// Update quantities ?
							// Update Interval ?

							equipmentMap.put(key, new IngredientDoser(modelType, kit3000Interval, kit3000Qty,
									kit6000Interval, kit6000Qty, kit12000Interval, kit12000Qty, hopperKit3000Interval,
									hopperKit3000Qty, hopperKit6000Interval, hopperKit6000Qty, hopperKit12000Interval,
									hopperKit12000Qty, mixerKit3000Interval, mixerKit3000Qty, mixerKit6000Interval,
									mixerKit6000Qty, mixerKit12000Interval, mixerKit12000Qty, pumpKit3000Interval,
									pumpKit3000Qty, pumpKit6000Interval, pumpKit6000Qty, pumpKit12000Interval,
									pumpKit12000Qty, coverKit3000Interval, coverKit3000Qty, agitatorKit6000Interval,
									agitatorKit6000Qty, agitatorKit12000Interval, agitatorKit12000Qty, lamellaInterval,
									lamellaQty, dasherInterval, dasherQty, pumpHouseInterval, pumpHouseQty));

							break;
						case "FF4000":

							modelType = "FF4000";
							// Update quantities ?
							// Update Interval ?

							equipmentMap.put(key, new IngredientDoser(modelType, kit3000Interval, kit3000Qty,
									kit6000Interval, kit6000Qty, kit12000Interval, kit12000Qty, hopperKit3000Interval,
									hopperKit3000Qty, hopperKit6000Interval, hopperKit6000Qty, hopperKit12000Interval,
									hopperKit12000Qty, mixerKit3000Interval, mixerKit3000Qty, mixerKit6000Interval,
									mixerKit6000Qty, mixerKit12000Interval, mixerKit12000Qty, pumpKit3000Interval,
									pumpKit3000Qty, pumpKit6000Interval, pumpKit6000Qty, pumpKit12000Interval,
									pumpKit12000Qty, coverKit3000Interval, coverKit3000Qty, agitatorKit6000Interval,
									agitatorKit6000Qty, agitatorKit12000Interval, agitatorKit12000Qty, lamellaInterval,
									lamellaQty, dasherInterval, dasherQty, pumpHouseInterval, pumpHouseQty));

						case "ID4000":

							modelType = "ID4000";
							// Update quantities ?
							// Update Interval ?

							equipmentMap.put(key, new IngredientDoser(modelType, kit3000Interval, kit3000Qty,
									kit6000Interval, kit6000Qty, kit12000Interval, kit12000Qty, hopperKit3000Interval,
									hopperKit3000Qty, hopperKit6000Interval, hopperKit6000Qty, hopperKit12000Interval,
									hopperKit12000Qty, mixerKit3000Interval, mixerKit3000Qty, mixerKit6000Interval,
									mixerKit6000Qty, mixerKit12000Interval, mixerKit12000Qty, pumpKit3000Interval,
									pumpKit3000Qty, pumpKit6000Interval, pumpKit6000Qty, pumpKit12000Interval,
									pumpKit12000Qty, coverKit3000Interval, coverKit3000Qty, agitatorKit6000Interval,
									agitatorKit6000Qty, agitatorKit12000Interval, agitatorKit12000Qty, lamellaInterval,
									lamellaQty, dasherInterval, dasherQty, pumpHouseInterval, pumpHouseQty));

							break;
						default:
							break;
						}
						/*
						 * If map doesn't contain a mapping then search for old
						 * type of Dosers, i.e., Equipment material equals
						 * '2623489-0100', and do a secondary search on
						 * modelNumberEqmnt by Regex on descending numbers.
						 */
					} else if (equipmentMtrlKey.equals("2623489-0100") && modelNumberEqmnt.contains("2000")) {

						modelType = "FF2000";
						// Update quantities ?
						// Update Interval ?

						equipmentMap.put(key, new IngredientDoser(modelType, kit3000Interval, kit3000Qty,
								kit6000Interval, kit6000Qty, kit12000Interval, kit12000Qty, hopperKit3000Interval,
								hopperKit3000Qty, hopperKit6000Interval, hopperKit6000Qty, hopperKit12000Interval,
								hopperKit12000Qty, mixerKit3000Interval, mixerKit3000Qty, mixerKit6000Interval,
								mixerKit6000Qty, mixerKit12000Interval, mixerKit12000Qty, pumpKit3000Interval,
								pumpKit3000Qty, pumpKit6000Interval, pumpKit6000Qty, pumpKit12000Interval,
								pumpKit12000Qty, coverKit3000Interval, coverKit3000Qty, agitatorKit6000Interval,
								agitatorKit6000Qty, agitatorKit12000Interval, agitatorKit12000Qty, lamellaInterval,
								lamellaQty, dasherInterval, dasherQty, pumpHouseInterval, pumpHouseQty));

					} else if (equipmentMtrlKey.equals("2623489-0100") && modelNumberEqmnt.contains("4000")) {

						modelType = "FF4000";
						// Update quantities ?
						// Update Interval ?

						equipmentMap.put(key, new IngredientDoser(modelType, kit3000Interval, kit3000Qty,
								kit6000Interval, kit6000Qty, kit12000Interval, kit12000Qty, hopperKit3000Interval,
								hopperKit3000Qty, hopperKit6000Interval, hopperKit6000Qty, hopperKit12000Interval,
								hopperKit12000Qty, mixerKit3000Interval, mixerKit3000Qty, mixerKit6000Interval,
								mixerKit6000Qty, mixerKit12000Interval, mixerKit12000Qty, pumpKit3000Interval,
								pumpKit3000Qty, pumpKit6000Interval, pumpKit6000Qty, pumpKit12000Interval,
								pumpKit12000Qty, coverKit3000Interval, coverKit3000Qty, agitatorKit6000Interval,
								agitatorKit6000Qty, agitatorKit12000Interval, agitatorKit12000Qty, lamellaInterval,
								lamellaQty, dasherInterval, dasherQty, pumpHouseInterval, pumpHouseQty));

					} else {
						/*
						 * Do nothing (exclude equipments from the Ingredient
						 * Doser mapping)
						 */
					}
				}
			}
			logger.info("Built {} Ingredient Dosers", equipmentMap.size());

		} catch (ClientException e) {
			logger.error("Exception in 'buildIngredientDosers()':" + e);
		}
		return equipmentMap;
	}

	/**
	 * Load Homogeniser and Functional areas to DB
	 */
	public static void addHomogeniser() {

		Map<String, Homogeniser> eqMap = buildHomogenisers();

		try (Session session = DRIVER.session()) {

			try (Transaction tx1 = session.beginTransaction()) {
				for (Map.Entry<String, Homogeniser> entry : eqMap.entrySet()) {
					String key = entry.getKey();
					Homogeniser v = entry.getValue();

					// Add Equipment type to the Equipment node label
					String tx = "MATCH (e: Equipment) WHERE e.id = " + key + " " + "MERGE (: Piston:Function {id: "
							+ key + ", pistonMtrl: UPPER(" + toCypherVariableFormat(v.getPiston())
							+ "), pistonDiameter: '#', serviceInterval: toInt(" + v.getPistonInterval()
							+ ")})-[:IN {qty:  toInt(" + v.getPistonQty() + ")}]->(e)"
							+ "MERGE (: PistonSeal:Function {id: " + key + ", pistonSeal: UPPER("
							+ toCypherVariableFormat(v.getPistonSeal()) + "), serviceInterval: toInt("
							+ v.getPistonSealInterval() + ")})-[:IN {qty:  toInt(" + v.getPistonSealQty() + ")}]->(e)"
							+ "MERGE (: CompressionRing:Function {id: " + key + ", compressionRing: UPPER("
							+ toCypherVariableFormat(v.getCompressionRing()) + "), serviceInterval: toInt("
							+ v.getCompressionRingInterval() + ")})-[:IN {qty:  toInt(" + v.getCompressionRingQty()
							+ ")}]->(e)" + "MERGE (: Forcer:Function {id: " + key + ", forcerMtrl: UPPER("
							+ toCypherVariableFormat(v.getForcer()) + "), serviceInterval: toInt("
							+ v.getForcerInterval() + ")})-[:IN {qty:  toInt(" + v.getForcerQty() + ")}]->(e)"
							+ "MERGE (: ImpactRing:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getImpactRingInterval() + ")})-[:IN {qty:  toInt(" + v.getImpactRingQty() + ")}]->(e)"
							+ "MERGE (: Seat:Function {id: " + key + ", serviceInterval: toInt(" + v.getSeatInterval()
							+ ")})-[:IN {qty:  toInt(" + v.getForcerSeatQty() + ")}]->(e)"
							+ "MERGE (: Valve:Function {id: " + key + ", valve: UPPER("
							+ toCypherVariableFormat(v.getValve()) + "), serviceInterval: toInt(" + v.getValveInterval()
							+ ")})-[:IN {qty:  toInt(" + v.getValveQty() + ")}]->(e)"
							+ "MERGE (: ValveSealing:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getValveSealingInterval() + ")})-[:IN {qty:  toInt(" + v.getValveSealingQty()
							+ ")}]->(e)" + "MERGE (: ValveSeat:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getValveSeatInterval() + ")})-[:IN {qty:  toInt(" + v.getValveSeatQty() + ")}]->(e)"
							+ "MERGE (: PlainBearing:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getPlainBearingInterval() + ")})-[:IN {qty:  toInt(" + v.getPlainBearingQty()
							+ ")}]->(e)" + "MERGE (: RollerBearing:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getRollerBearingInterval() + ")})-[:IN {qty:  toInt(" + v.getRollerBearingQty()
							+ ")}]->(e)" + "MERGE (: Bellow:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getBellowsInterval() + ")})-[:IN {qty:  toInt(" + v.getBellowQty() + ")}]->(e)"
							+ " SET e:Homogeniser:Equipment";

					tx1.run(tx);
					tx1.success();
				}
			}

			logger.info("Wrote {} Homogenisers to DB", eqMap.size());
		} catch (ClientException e) {
			logger.error("Exception in 'addHomogeniser()':" + e);
		}
	}

	/**
	 * Load Separator and Functional areas to DB
	 */
	public static void addSeparator() {

		Map<String, Separator> eqMap = buildSeparators();

		try (Session session = DRIVER.session()) {

			try (Transaction tx1 = session.beginTransaction()) {
				for (Map.Entry<String, Separator> entry : eqMap.entrySet()) {
					String key = entry.getKey();
					Separator v = entry.getValue();

					// Add Equipment type to the Equipment node label
					String tx = "MATCH (e: Equipment) WHERE e.id = " + key + " " + "MERGE (: IMkit:Function {id: " + key
							+ ", serviceInterval: toInt(" + v.getIntermediateKitInterval() + ")})-[:IN]->(e)"
							+ "MERGE (: MSkit:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getMajorKitInterval() + ")})-[:IN]->(e)" + "MERGE (: OWMCkit:Function {id: " + key
							+ ", serviceInterval: toInt(" + v.getoWMCkitInterval() + ")})-[:IN]->(e)"
							+ "MERGE (: FFkit:Function {id: " + key + ", serviceInterval: toInt(" + v.getfFkitInterval()
							+ ")})-[:IN]->(e)" + " SET e:Separator:Equipment";

					tx1.run(tx);
					tx1.success();
				}
			}

			logger.info("Wrote {} Separators to DB", eqMap.size());
		} catch (ClientException e) {
			logger.error("Exception in 'addSeparator()':" + e);
		}
	}

	/**
	 * Load Plate Heat Exchanger and Functional areas to DB
	 */
	public static void addPHE() {

		Map<String, PlateHeatExchanger> eqMap = buildPHEs();

		try (Session session = DRIVER.session()) {

			try (Transaction tx1 = session.beginTransaction()) {
				for (Map.Entry<String, PlateHeatExchanger> entry : eqMap.entrySet()) {
					String key = entry.getKey();
					PlateHeatExchanger v = entry.getValue();

					// Add Equipment type to the Equipment node label
					String tx;
					switch (v.getModelType()) {
					case "C":
						tx = "MATCH (e: Equipment) WHERE e.id = " + key + " " + "MERGE (: CGasket:Function {id: " + key
								+ ", gasketQty: toInt(" + v.getNumberOfPlates() + "), serviceInterval: toInt("
								+ v.getGasketInterval() + ")})-[:IN]->(e)" + "MERGE (: CPlate:Function {id: " + key
								+ ", plateQty: toInt(" + v.getNumberOfPlates() + "), serviceInterval: toInt("
								+ v.getPlateInterval() + ")})-[:IN]->(e)" + " SET e:PHE:Equipment";
						break;
					case "M":
						tx = "MATCH (e: Equipment) WHERE e.id = " + key + " " + "MERGE (: MGasket:Function {id: " + key
								+ ", gasketQty: toInt(" + v.getNumberOfPlates() + "), serviceInterval: toInt("
								+ v.getGasketInterval() + ")})-[:IN]->(e)" + "MERGE (: MPlate:Function {id: " + key
								+ ", plateQty: toInt(" + v.getNumberOfPlates() + "), serviceInterval: toInt("
								+ v.getPlateInterval() + ")})-[:IN]->(e)" + " SET e:PHE:Equipment";
						break;
					case "H":
						tx = "MATCH (e: Equipment) WHERE e.id = " + key + " " + "MERGE (: HGasket:Function {id: " + key
								+ ", gasketQty: toInt(" + v.getNumberOfPlates() + "), serviceInterval: toInt("
								+ v.getGasketInterval() + ")})-[:IN]->(e)" + "MERGE (: HPlate:Function {id: " + key
								+ ", plateQty: toInt(" + v.getNumberOfPlates() + "), serviceInterval: toInt("
								+ v.getPlateInterval() + ")})-[:IN]->(e)" + " SET e:PHE:Equipment";
						break;
					case "P":
						tx = "MATCH (e: Equipment) WHERE e.id = " + key + " " + "MERGE (: PGasket:Function {id: " + key
								+ ", gasketQty: toInt(" + v.getNumberOfPlates() + "), serviceInterval: toInt("
								+ v.getGasketInterval() + ")})-[:IN]->(e)" + "MERGE (: PPlate:Function {id: " + key
								+ ", plateQty: toInt(" + v.getNumberOfPlates() + "), serviceInterval: toInt("
								+ v.getPlateInterval() + ")})-[:IN]->(e)" + " SET e:PHE:Equipment";
						break;
					default:
						tx = "MATCH (e: Equipment) WHERE e.id = " + key + " " + "MERGE (: Gasket:Function {id: " + key
								+ ", gasketQty: toInt(" + v.getNumberOfPlates() + "), serviceInterval: toInt("
								+ v.getGasketInterval() + ")})-[:IN]->(e)" + "MERGE (: Plate:Function {id: " + key
								+ ", plateQty: toInt(" + v.getNumberOfPlates() + "), serviceInterval: toInt("
								+ v.getPlateInterval() + ")})-[:IN]->(e)" + " SET e:PHE:Equipment";
						break;
					}

					tx1.run(tx);
					tx1.success();
				}
			}

			logger.info("Wrote {} Plate Heat Exchangers to DB", eqMap.size());
		} catch (ClientException e) {
			logger.error("Exception in 'addPHE()':" + e);
		}
	}

	/**
	 * Load Tubular Heat Exchanger and Functional areas to DB
	 */
	public static void addTHE() {

		Map<String, TubularHeatExchanger> eqMap = buildTHEs();

		try (Session session = DRIVER.session()) {

			try (Transaction tx1 = session.beginTransaction()) {
				for (Map.Entry<String, TubularHeatExchanger> entry : eqMap.entrySet()) {
					String key = entry.getKey();
					TubularHeatExchanger v = entry.getValue();

					// Add Equipment type to the Equipment node label
					String tx;
					switch (v.getModelType()) {
					case "C":
						tx = "MATCH (e: Equipment) WHERE e.id = " + key + " " + "MERGE (: Ctube:Function {id: " + key
								+ ", nonRegTubeQty: toInt(" + v.getNumberOfNonRegenerativeTubes()
								+ "), regTubeQty: toInt(" + v.getNumberOfRegenerativeTubes()
								+ "), holdingTubeQty: toInt(" + v.getNumberOfHoldingTubes() + ")})-[:IN]->(e)"
								+ "MERGE (: CoRing:Function {id: " + key + ", serviceInterval: toInt("
								+ v.getoRingInterval() + ")})-[:IN]->(e)" + "MERGE (: Seal:Function {id: " + key
								+ ", serviceInterval: toInt(" + v.getProdSealInterval() + ")})-[:IN]->(e)"
								+ " SET e:THE:Equipment";
						break;
					case "MT":
						tx = "MATCH (e: Equipment) WHERE e.id = " + key + " " + "MERGE (: MTtube:Function {id: " + key
								+ ", nonRegTubeQty: toInt(" + v.getNumberOfNonRegenerativeTubes()
								+ "), regTubeQty: toInt(" + v.getNumberOfRegenerativeTubes()
								+ "), holdingTubeQty: toInt(" + v.getNumberOfHoldingTubes() + ")})-[:IN]->(e)"
								+ "MERGE (: MToRing:Function {id: " + key + ", serviceInterval: toInt("
								+ v.getoRingInterval() + ")})-[:IN]->(e)" + "MERGE (: Seal:Function {id: " + key
								+ ", serviceInterval: toInt(" + v.getProdSealInterval() + ")})-[:IN]->(e)"
								+ " SET e:THE:Equipment";
						break;
					case "CIP":
						tx = "MATCH (e: Equipment) WHERE e.id = " + key + " " + "MERGE (: CIPtube:Function {id: " + key
								+ ", nonRegTubeQty: toInt(" + v.getNumberOfNonRegenerativeTubes() + ")})-[:IN]->(e)"
								+ "MERGE (: CIPoRing:Function {id: " + key + ", serviceInterval: toInt("
								+ v.getoRingInterval() + ")})-[:IN]->(e)" + "MERGE (: Seal:Function {id: " + key
								+ ", serviceInterval: toInt(" + v.getProdSealInterval() + ")})-[:IN]->(e)"
								+ " SET e:THE:Equipment";
						break;
					default:
						tx = "MATCH (e: Equipment) WHERE e.id = " + key + " " + "MERGE (: Tube:Function {id: " + key
								+ ", nonRegTubeQty: toInt(" + v.getNumberOfNonRegenerativeTubes()
								+ "), regTubeQty: toInt(" + v.getNumberOfRegenerativeTubes()
								+ "), holdingTubeQty: toInt(" + v.getNumberOfHoldingTubes() + ")})-[:IN]->(e)"
								+ "MERGE (: ORing:Function {id: " + key + ", serviceInterval: toInt("
								+ v.getoRingInterval() + ")})-[:IN]->(e)" + "MERGE (: Seal:Function {id: " + key
								+ ", serviceInterval: toInt(" + v.getProdSealInterval() + ")})-[:IN]->(e)"
								+ " SET e:THE:Equipment";
						break;
					}

					tx1.run(tx);
					tx1.success();
				}
			}

			logger.info("Wrote {} Tubular Heat Exchangers to DB", eqMap.size());
		} catch (ClientException e) {
			logger.error("Exception in 'addTHE()':" + e);
		}
	}

	/**
	 * Load Frigus Freezer and Functional areas to DB
	 */
	public static void addFrigusFreezer() {

		Map<String, FrigusFreezer> eqMap = buildFrigusFreezers();

		try (Session session = DRIVER.session()) {

			try (Transaction tx1 = session.beginTransaction()) {
				for (Map.Entry<String, FrigusFreezer> entry : eqMap.entrySet()) {
					String key = entry.getKey();
					FrigusFreezer v = entry.getValue();

					// Add Equipment type to the Equipment node label
					String tx = "MATCH (e: Equipment) WHERE e.id = " + key + " "
					// Service kits
							+ "MERGE (: Kit1000:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getKit1000Interval() + ")})-[:IN {qty:  toInt(" + v.getKit1000Qty() + ")}]->(e)"
							+ "MERGE (: Kit3000:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getKit3000Interval() + ")})-[:IN {qty:  toInt(" + v.getKit3000Qty() + ")}]->(e)"
							+ "MERGE (: Kit6000:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getKit6000Interval() + ")})-[:IN {qty:  toInt(" + v.getKit6000Qty() + ")}]->(e)"
							+ "MERGE (: Kit12000:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getKit12000Interval() + ")})-[:IN {qty:  toInt(" + v.getKit12000Qty() + ")}]->(e)"
							// Pump
							+ "MERGE (: NewPump:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getNewPumpInterval() + ")})-[:IN {qty:  toInt(" + v.getNewPumpQty() + ")}]->(e)"
							+ "MERGE (: ExchangePump:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getExchangePumpInterval() + ")})-[:IN {qty:  toInt(" + v.getExchangePumpQty()
							+ ")}]->(e)"
							// Pump spare parts
							+ "MERGE (: ExtendedKit:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getKitExtendedInterval() + ")})-[:IN {qty:  toInt(" + v.getKitExtendedQty() + ")}]->(e)"
							+ "MERGE (: StarWheel:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getStarWheelInterval() + ")})-[:IN {qty:  toInt(" + v.getStarWheelQty() + ")}]->(e)"
							+ "MERGE (: Cover:Function {id: " + key + ", serviceInterval: toInt(" + v.getCoverInterval()
							+ ")})-[:IN {qty:  toInt(" + v.getCoverQty() + ")}]->(e)"
							+ "MERGE (: Impeller:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getImpellerInterval() + ")})-[:IN {qty:  toInt(" + v.getImpellerQty() + ")}]->(e)"
							+ "MERGE (: GasketKit:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getGasketKitInterval() + ")})-[:IN {qty:  toInt(" + v.getGasketKitQty() + ")}]->(e)"
							+ "MERGE (: Bushing:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getBushingInterval() + ")})-[:IN {qty:  toInt(" + v.getBushingQty() + ")}]->(e)"
							// Scraper
							+ "MERGE (: ScraperKnife:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getScraperKnifeInterval() + ")})-[:IN {qty:  toInt(" + v.getScraperKnifeQty()
							+ ")}]->(e)"
							// Cooling
							+ "MERGE (: SpareParts:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getSparePartsInterval() + ")})-[:IN {qty:  toInt(" + v.getSparePartsQty() + ")}]->(e)"
							// Shaft
							+ "MERGE (: ShaftSeal:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getShaftSealInterval() + ")})-[:IN {qty:  toInt(" + v.getShaftSealQty() + ")}]->(e)"
							// Cylinder
							+ "MERGE (: CylinderKit:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getCylinderKitInterval() + ")})-[:IN {qty:  toInt(" + v.getCylinderKitQty() + ")}]->(e)"
							+ "MERGE (: Gaskets:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getGasketsInterval() + ")})-[:IN {qty:  toInt(" + v.getGasketQty() + ")}]->(e)"
							// Dasher
							+ "MERGE (: DasherKit:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getDasherKitInterval() + ")})-[:IN {qty:  toInt(" + v.getDasherKitQty() + ")}]->(e)"
							+ "MERGE (: SingleParts:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getSinglePartsInterval() + ")})-[:IN {qty:  toInt(" + v.getSinglePartQty() + ")}]->(e)"
							+ " SET e:Freezer:Equipment";

					tx1.run(tx);
					tx1.success();
				}
			}

			logger.info("Wrote {} Frigus Freezers to DB", eqMap.size());
		} catch (ClientException e) {
			logger.error("Exception in 'addFrigusFreezer()':" + e);
		}
	}

	/**
	 * Load Ingredient Doser and Functional areas to DB
	 */
	public static void addIngredientDoser() {

		Map<String, IngredientDoser> eqMap = buildIngredientDosers();

		try (Session session = DRIVER.session()) {

			try (Transaction tx1 = session.beginTransaction()) {
				for (Map.Entry<String, IngredientDoser> entry : eqMap.entrySet()) {
					String key = entry.getKey();
					IngredientDoser v = entry.getValue();

					// Add Equipment type to the Equipment node label
					String tx = "MATCH (e: Equipment) WHERE e.id = " + key + " "
					// Service kits
							+ "MERGE (: DosKit3000:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getKit3000Interval() + ")})-[:IN {qty:  toInt(" + v.getKit3000Qty() + ")}]->(e)"
							+ "MERGE (: DosKit6000:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getKit6000Interval() + ")})-[:IN {qty:  toInt(" + v.getKit6000Qty() + ")}]->(e)"
							+ "MERGE (: DosKit12000:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getKit12000Interval() + ")})-[:IN {qty:  toInt(" + v.getKit12000Qty() + ")}]->(e)"
							// Hopper
							+ "MERGE (: HopperKit3000:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getHopperKit3000Interval() + ")})-[:IN {qty:  toInt(" + v.getHopperKit3000Qty()
							+ ")}]->(e)" + "MERGE (: HopperKit6000:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getHopperKit6000Interval() + ")})-[:IN {qty:  toInt(" + v.getHopperKit6000Qty()
							+ ")}]->(e)" + "MERGE (: HopperKit12000:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getHopperKit12000Interval() + ")})-[:IN {qty:  toInt(" + v.getHopperKit12000Qty()
							+ ")}]->(e)"
							// Mixer
							+ "MERGE (: MixerKit3000:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getMixerKit3000Interval() + ")})-[:IN {qty:  toInt(" + v.getMixerKit3000Qty()
							+ ")}]->(e)" + "MERGE (: MixerKit6000:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getMixerKit6000Interval() + ")})-[:IN {qty:  toInt(" + v.getMixerKit6000Qty()
							+ ")}]->(e)" + "MERGE (: MixerKit12000:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getMixerKit12000Interval() + ")})-[:IN {qty:  toInt(" + v.getMixerKit12000Qty()
							+ ")}]->(e)"
							// Pump
							+ "MERGE (: PumpKit3000:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getPumpKit3000Interval() + ")})-[:IN {qty:  toInt(" + v.getPumpKit3000Qty() + ")}]->(e)"
							+ "MERGE (: PumpKit6000:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getPumpKit6000Interval() + ")})-[:IN {qty:  toInt(" + v.getPumpKit6000Qty() + ")}]->(e)"
							+ "MERGE (: PumpKit12000:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getPumpKit12000Interval() + ")})-[:IN {qty:  toInt(" + v.getPumpKit12000Qty()
							+ ")}]->(e)"
							// Cover
							+ "MERGE (: CoverKit3000:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getCoverKit3000Interval() + ")})-[:IN {qty:  toInt(" + v.getCoverKit3000Qty()
							+ ")}]->(e)"
							// Agitator
							+ "MERGE (: AgitatorKit6000:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getAgitatorKit6000Interval() + ")})-[:IN {qty:  toInt(" + v.getAgitatorKit6000Qty()
							+ ")}]->(e)" + "MERGE (: AgitatorKit12000:Function {id: " + key
							+ ", serviceInterval: toInt(" + v.getAgitatorKit12000Interval() + ")})-[:IN {qty:  toInt("
							+ v.getAgitatorKit12000Qty() + ")}]->(e)"

					// Lamella set
							+ "MERGE (: Lamella:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getLamellaInterval() + ")})-[:IN {qty:  toInt(" + v.getLamellaQty() + ")}]->(e)"
							// Dasher
							+ "MERGE (: Dasher:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getDasherInterval() + ")})-[:IN {qty:  toInt(" + v.getDasherQty() + ")}]->(e)"
							// Pump house
							+ "MERGE (: PumpHouse:Function {id: " + key + ", serviceInterval: toInt("
							+ v.getPumpHouseInterval() + ")})-[:IN {qty:  toInt(" + v.getPumpHouseQty() + ")}]->(e)"

							+ " SET e:IngredientDoser:Equipment";

					tx1.run(tx);
					tx1.success();
				}
			}

			logger.info("Wrote {} Ingredient Dosers to DB", eqMap.size());
		} catch (ClientException e) {
			logger.error("Exception in 'addIngredientDoser()':" + e);
		}
	}

	/**
	 * Handle instances of two different Markets linked to an Entity customer.
	 * Rename the Relationship type to 'LINKED_SEC' between the differing
	 * Market- and Country name, and keep the relationship type as 'LINKED'
	 * between identical Market- and Country names. This will mitigate double
	 * counting of Potentials.
	 */
	public static void fixDualMarketLinks() {

		try (Session session = DRIVER.session()) {

			try (Transaction tx1 = session.beginTransaction()) {

				/**
				 * Rename the Relationship type to 'LINKED_SEC' between
				 * differing Market- and Country names
				 */

				String tx = "MATCH (m1: Market)-[r1]-(e: Entity)-[r2]-(m2: Market) MATCH (e: Entity)--(c: Country) "
						+ "WHERE m1.name <> c.name AND m2.name = c.name CREATE (e)-[r: LINKED_SEC]->(m1) "
						+ "WITH r1 DELETE r1";

				tx1.run(tx);
				tx1.success();

			}

			logger.info("Fixed dual market links to customer entities");
		} catch (ClientException e) {
			logger.error("Exception in 'fixDualMarketLinks()':" + e);
		}
	}

	/**
	 * Helper method that changes the format of a text variable from x to 'x'.
	 * This is how text properties are formated in Cypher.
	 *
	 * @param variable
	 *            the string variable to reformat
	 * @return the reformatted variable
	 */
	private static String toCypherVariableFormat(String variable) {
		return "'" + variable + "'";
	}

	/**
	 * Queries the DB on quantity of Nodes and Relationships
	 */
	public static void getDBcontent() {

		try (Session session = DRIVER.session()) {

			try (Transaction tx1 = session.beginTransaction()) {
				// Count all nodes and relationships
				StatementResult r1 = tx1.run("MATCH (n) RETURN count(n) AS nodes;");
				StatementResult r2 = tx1.run("MATCH ()-->() RETURN count(*) AS relationships");

				tx1.success();

				while (r1.hasNext()) {
					Record record = r1.next();
					logger.info("DB contains {} nodes", record.get("nodes"));
				}
				while (r2.hasNext()) {
					Record record = r2.next();
					logger.info("DB contains {} relationships", record.get("relationships"));
				}
			}
		} catch (ClientException e) {
			logger.error("Exception in 'getDBcontent()':" + e);
		}
	}

	/**
	 * Deletes Nodes and relationships from database
	 */
	public static void dropDBdata() {
		try (Session session = DRIVER.session()) {
			logger.info("Delete Nodes and Relationships from DB");
			// Run single statements one-by-one, OR...
			String tx = "MATCH (eq:Equipment) DETACH DELETE (eq)";
			String tx1 = "MATCH (f:Function) DETACH DELETE (f)";
			String tx2 = "MATCH (p:Part) DETACH DELETE (p)";
			String tx3 = "MATCH (e:Entity) DETACH DELETE (e)";
			String tx4 = "MATCH (c:CustGrp) DETACH DELETE (c)";
                        String tx5 = "MATCH (m:MarketGrp) DETACH DELETE (m);";
			session.run(tx);
			session.run(tx1);
			session.run(tx2);
			session.run(tx3);
			session.run(tx4);
                        session.run(tx5);

		} catch (ClientException e) {
			logger.error("Exception in 'dropDBdata()':" + e);
		}
	}

	/**
	 * Close the DB driver
	 */
	public static void closeNeo4jDriver() {
		DRIVER.session().close();
		DRIVER.close();
	}

	/**
	 * Query method template. Change to public when use.
	 */
	private static void someQuery() {
		try (Session session = DRIVER.session()) {

			// Run single statements one-by-one, OR...
			String tx = "Cypher query or statement";
			session.run(tx);

			// ... Run multiple statements
			try (Transaction tx1 = session.beginTransaction()) {
				tx1.run("Cypher query or statement");
				tx1.success();
			}
			logger.info("|Output message goes here|");
		} catch (ClientException e) {
			logger.error("Exception in 'someQuery()':" + e);
		}
	}

	/**
	 * This is a helper class that models Plate Heat Exchanger families for the
	 * PHE_FAMILY_MAP using names 'C','M','H' or 'P' based on standard
	 * TPPS2003Appendix
	 */
	private static class PHEfamily {

		String model;

		public PHEfamily(String model) {
			this.model = model;
		}

		public String getModel() {
			return model;
		}

	}

	/**
	 * This is a helper class that models Tubular Heat Exchanger families for
	 * the THE_FAMILY_MAP using names 'C', 'MT' or 'CIP', and tube sizes, based
	 * on standard TPPS20033Appendix
	 */
	private static class THEfamily {

		String model;
		String tubeSize;

		public THEfamily(String model, String tubeSize) {
			this.model = model;
			this.tubeSize = tubeSize;
		}

		public String getModel() {
			return model;
		}

		public String getTubeSize() {
			return tubeSize;
		}

	}

	/**
	 * This is a helper class that models Frigus Freezer families for the
	 * FRIGUS_FREEZER_FAMILY_MAP
	 */
	private static class FrigusFreezerFamily {

		String model;

		public FrigusFreezerFamily(String model) {
			this.model = model;
		}

		public String getModel() {
			return model;
		}

	}

	/**
	 * This is a helper class that models Ingredient Doser families for the
	 * INGREDIENT_DOSER_FREEZER_FAMILY_MAP
	 */
	private static class IngredientDoserFamily {
		String model;

		public IngredientDoserFamily(String model) {
			this.model = model;
		}

		public String getModel() {
			return model;
		}

	}
}
