package com.control.stock.controller;

import com.control.stock.factory.ConnectionFactory;

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

		ArrayList<Map<String, String>> resultado = null;

		try (Connection con = new ConnectionFactory().recuperaConexion();) {
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

    public void guardar(Map<String,String> producto) {
		try{
			Connection con = new ConnectionFactory().recuperaConexion();

			Statement statement = con.createStatement();
			statement.execute("INSERT INTO producto(nombre,descripcion,cantidad)"
					+"VALUES('" +producto.get("NOMBRE") + "', '"
					+producto.get("DESCRIPCION") + "', "
					+producto.get("CANTIDAD") +")", Statement.RETURN_GENERATED_KEYS );

			ResultSet resultSet = statement.getGeneratedKeys();

			while (resultSet.next()){
				resultSet.getInt(1);
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
