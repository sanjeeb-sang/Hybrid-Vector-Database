package bds.query;

import bds.calcite.CustomSchema;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.calcite.adapter.jdbc.JdbcSchema;
import org.apache.calcite.config.Lex;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaPlus;
import java.sql.*;
import java.util.Properties;

/**
 * Class that helps create and manage CalciteConnection.
 */
public class CalciteConnector {
    private CalciteConnection calciteConnection;

    /**
     * Method to create a new CalciteConnection.
     * @return newly created CalciteConnection.
     * @throws ClassNotFoundException may be thrown.
     * @throws SQLException may be thrown.
     */
    public CalciteConnection connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.calcite.jdbc.Driver");

        Properties info = new Properties();
        info.setProperty("lex", "JAVA");
        Connection connection = DriverManager.getConnection("jdbc:calcite:", info);

        calciteConnection = connection.unwrap(CalciteConnection.class);
        SchemaPlus rootSchema = calciteConnection.getRootSchema();
        Schema schema = new CustomSchema();
        rootSchema.add("hr", schema);

        return calciteConnection;
    }

    /**
     * Method to create a new CalciteConnection that uses MySQL.
     * @return newly created CalciteConnection.
     * @throws ClassNotFoundException may be thrown.
     * @throws SQLException may be thrown.
     */
    public CalciteConnection connectMysql() throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.calcite.jdbc.Driver");

        Properties info = new Properties();
        info.setProperty("lex", Lex.MYSQL.name());

        String mysqlUrl = "jdbc:mysql://localhost:3306/mydb"; // Replace "mydb" with your database name
        Properties mysqlProperties = new Properties();
        mysqlProperties.setProperty("user", "root"); // Replace with your MySQL user
        mysqlProperties.setProperty("password", "password"); // Replace with your MySQL password

        Connection connection = DriverManager.getConnection("jdbc:calcite:", info);

        calciteConnection = connection.unwrap(CalciteConnection.class);
        SchemaPlus rootSchema = calciteConnection.getRootSchema();
        Schema schema = new CustomSchema();

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setDatabaseName("mydb");
        dataSource.setUser("root");
        dataSource.setPassword("password");
        dataSource.setServerName("localhost");
        dataSource.setPort(3306);

        JdbcSchema mysqlSchema = JdbcSchema.create(rootSchema, "MYSQL_DB", dataSource, null, mysqlUrl);

        rootSchema.add("hr", schema);
        rootSchema.add("db", mysqlSchema);

        return calciteConnection;
    }

    /**
     * Method to get the current CalciteConnection.
     * @return the current CalciteConnection.
     */
    public CalciteConnection getCalciteConnection() {
        return calciteConnection;
    }

    /**
     * Method to close the current CalciteConnection.
     */
    public void close() {
        try {
            calciteConnection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
