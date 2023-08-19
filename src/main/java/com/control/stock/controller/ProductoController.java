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

		PreparedStatement statement = con
				.prepareStatement("UPDATE PRODUCTO SET "
						+ " NOMBRE = ?, "
						+ " DESCRIPCION = ?,"
						+ " CANTIDAD = ?"
						+ " WHERE ID = ?");

		statement.setString(1, nombre);
		statement.setString(2, descripcion);
		statement.setInt(3, cantidad);
		statement.setInt(4, id);
		statement.execute();

		int updateCount = statement.getUpdateCount();

		con.close();

		return updateCount;
	}

	public int eliminar(Integer id) {
		try {
			Connection  con = new ConnectionFactory().recuperaConexion();

			PreparedStatement statement = con
					.prepareStatement("DELETE FROM PRODUCTO WHERE ID = ?");
			statement.setInt(1, id);
			statement.execute();

			int updateCount = statement.getUpdateCount();

			return updateCount;

		} catch (SQLException e){
			throw new RuntimeException(e);
		}
	}

	public List<Map<String, String>> listar() {

		List<Map<String, String>> resultado = new ArrayList<>();

		try {
			ConnectionFactory factory = new ConnectionFactory();
			Connection con = factory.recuperaConexion();

			PreparedStatement statement = con
					.prepareStatement("SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO");
			statement.execute();

			ResultSet resultSet = statement.getResultSet();



			while (resultSet.next()) {
				Map<String, String> fila = new HashMap<>();
				fila.put("ID", String.valueOf(resultSet.getInt("ID")));
				fila.put("NOMBRE", resultSet.getString("NOMBRE"));
				fila.put("DESCRIPCION", resultSet.getString("DESCRIPCION"));
				fila.put("CANTIDAD", String.valueOf(resultSet.getInt("CANTIDAD")));

				resultado.add(fila);
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		// return new ArrayList<>();
		return resultado;
	}

    public void guardar(Map<String,String> producto) {
		try{
			ConnectionFactory factory = new ConnectionFactory();
			Connection con = factory.recuperaConexion();
			//se saca la responsabilidad del jdbc, para concluir la transaccion
			con.setAutoCommit(false);

			String nombre = producto.get("NOMBRE");
			String descripcion = producto.get("DESCRIPCION");
			Integer cantidad = Integer.valueOf(producto.get("CANTIDAD"));
			Integer maximaCantidad = 50;

			PreparedStatement statement = con.prepareStatement("INSERT INTO PRODUCTO(nombre,descripcion,cantidad)" +
					"VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);

			try {
				do {
					/* cada 50 elementos, se crea un nuevo registro
					 * si se crea, un elemento con 74, se crean dos registros  res_1 = 50   res_2 = 24*/
					int cantidadParaGuardar = Math.min(cantidad, maximaCantidad);
					ejecutarRegistro(nombre, descripcion, cantidadParaGuardar, statement);
					cantidad=- maximaCantidad;
				} while (cantidad>0);
				con.commit();
			}catch (Exception e){
				con.rollback();
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private static void ejecutarRegistro(String nombre, String descripcion, Integer cantidad, PreparedStatement statement) throws SQLException {
		statement.setString(1, nombre);
		statement.setString(2, descripcion);
		statement.setInt(3, cantidad);

		statement.execute();

		ResultSet resultSet = statement.getGeneratedKeys();

		while (resultSet.next()){
			System.out.println(String.format("dato ingresado", resultSet.getInt(1)));
		}
	}

}
