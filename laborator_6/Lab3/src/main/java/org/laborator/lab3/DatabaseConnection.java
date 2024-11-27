package org.laborator.lab3;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DataSource dataSource;

    static {
        try {
            // JNDI lookup for DataSource
            Context initContext = new InitialContext();
            dataSource = (DataSource) initContext.lookup("java:comp/env/jdbc/lab3"); // JNDI name defined in context.xml
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error setting up database connection.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection(); // Get a connection from the pool
    }
}
