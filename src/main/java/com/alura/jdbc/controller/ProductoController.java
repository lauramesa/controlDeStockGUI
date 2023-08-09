package com.alura.jdbc.controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {

	public void modificar(String nombre, String descripcion, Integer id) {
		// TODO
	}

	public void eliminar(Integer id) {
		// TODO
	}

	public List<Map<String, String>> listar() {
		String url = "jdbc:mysql://localhost/control_stock?useTimeZone=true&serverTimeZone=UTC";
		String user = "root";
		String password = "root12345";

		ArrayList<Map<String, String>> resultado = null;
		try (Connection con = DriverManager.getConnection(url, user, password)) {
			Statement statement = con.createStatement();
			statement.execute("SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO");

			ResultSet resultSet = statement.getResultSet();
			resultado = new ArrayList<>();
			while (resultSet.next()) {
				Map<String, String> fila = new HashMap<>();
				//resultSet.getInt("ID");
				fila.put("ID", String.valueOf(resultSet.getInt("ID")));
				fila.put("NOMBRE", resultSet.getString("NOMBRE"));
				fila.put("DESCRIPCION", resultSet.getString("DESCRIPCION"));
				fila.put("CANTIDAD", String.valueOf(resultSet.getInt("CANTIDAD")));

				resultado.add(fila);
			}
		} catch (SQLException e) {
			System.err.println("error " + e.getMessage());
		}
		// return new ArrayList<>();
		return resultado;
	}

    public void guardar(Object producto) {
		// TODO
	}

}
