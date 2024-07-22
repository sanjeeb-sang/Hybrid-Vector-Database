package bds.query;

import bds.common.Logger;
import bds.common.TablePrinter;
import org.apache.calcite.jdbc.CalciteConnection;
import java.sql.*;

/**
 * Class that processes SQL queries using Calcite Connection.
 */
public class CalciteQueryProcessor {

    private CalciteConnection calciteConnection;
    private static Logger logger = Logger.getInstance();

    /**
     * No argument constructor.
     */
    public CalciteQueryProcessor() {
        setup();
    }

    /**
     * Method to setup the CalciteConnection.
     */
    private void setup() {
        CalciteConnector connector = new CalciteConnector();
        try {
            calciteConnection = connector.connect();
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("Unable to connect to Calcite", "CalciteQueryProcessor");
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to execute the passed query using the created CalciteConnection.
     * This method does not return any data, instead it will print out the result in a formatted manner.
     * @param query the Sql query to execute.
     */
    public void executeQuery(String query) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = calciteConnection.createStatement();
            resultSet = statement.executeQuery(query);
            printResultSet(resultSet);
            statement.close();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Method to execute UPDATE query using CalciteConnection.
     * @param query the query to execute.
     * @return the number of rows impacted.
     */
    public int executeUpdate(String query) {
        Statement statement = null;
        int result = -1;
        try {
            statement = calciteConnection.createStatement();
            result = statement.executeUpdate(query);
            statement.close();

            return result;
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Method to print ResultSet object in a formatted manner.
     * @param resultSet the ResultSet object to print.
     */
    private void printResultSet(ResultSet resultSet) {
        try {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();

            TablePrinter printer = new TablePrinter();

            for (int i = 1; i <= columnCount; i++) {
                printer.addColumnItem(rsmd.getColumnName(i));
            }
            printer.nextRow();

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String columnValue = resultSet.getString(i);
                    printer.addColumnItem(columnValue);
                }
                printer.nextRow();
            }

            printer.printTable();

            resultSet.close();
        } catch (Exception ex) {
            logger.error("Error while processing result Set.", "CalciteQueryProcessor");
            logger.error(ex.getMessage(), "CalciteQueryProcessor");
        }
    }

    /*
     * Method that can be called to close the existing CalciteConnection instance.
     */
    public void close() {
        try {
            calciteConnection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
