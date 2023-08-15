package com.control.stock.controller;

import com.control.stock.factory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {

	public int modificar(String nombre, String descripcion, Integer cantidad, Integer id) throws SQLException {
		ConnectionFactory factory = new ConnectionFactory();
		Connection con = factory.recuperaConexion();

		Statement statement = con.createStatement();

		statement.execute("UPDATE PRODUCTO SET "
				+ " NOMBRE = '" + nombre + "'"
				+ ", DESCRIPCION = '" + descripcion + "'"
				+ ", CANTIDAD = " + cantidad
				+ " WHERE ID = " + id);

		int updateCount = statement.getUpdateCount();

		con.close();

		return updateCount;
	}

	public int eliminar(Integer id) {
		try {
			Connection  con = new ConnectionFactory().recuperaConexion();

			Statement statement = con.createStatement();

			statement.execute("DELETE FROM PRODUCTO WHERE ID = " +id);

			return statement.getUpdateCount();

		} catch (SQLException e){
			throw new RuntimeException(e);
		}
	}

	public List<Map<String, String>> listar() {

		ArrayList<Map<String, String>> resultado = null;

		try {
			Connection con = new ConnectionFactory().recuperaConexion();
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
			statement.execute("INSERT INTO PRODUCTO(nombre,descripcion,cantidad)"
					+"VALUES('" +producto.get("NOMBRE") + "', '"
					+producto.get("DESCRIPCION")+ "', "
					+producto.get("CANTIDAD")+")", Statement.RETURN_GENERATED_KEYS );

			ResultSet resultSet = statement.getGeneratedKeys();

			while (resultSet.next()){
				System.out.println(String.format("dato ingresado", resultSet.getInt(1)));
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
