package com.control.stock.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public Connection recuperaConexion() throws SQLException {
        String url = "jdbc:mysql://localhost/control_stock?useTimeZone=true&serverTimeZone=UTC";
        String user = "root";
        String password = "root12345";

        return DriverManager.getConnection(url,user,password);
    }
}
